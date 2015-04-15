/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.datasource;

import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

/**
 * This abstract class provides an implementation to wait for changes in a set
 * of text files, and read the new data from these files into corresponding
 * traces. A subclass has to implement methods in order to parse the contents of
 * the file.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public abstract class AbstractFileDataSource extends AbstractDataSource {

	/**
	 * The maximum number of bytes that are read into a buffer at once.
	 */
	private static final int BUFFER_SIZE = 4096;

	/**
	 * The maximum number of lines to read before a TraceEvent is triggered.
	 */
	private static final int MAX_BUFFERED_LINES = 512;

	private static final Quantity<Time> ZERO = UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS);

	private final Logger log = Logger.getLogger(this.getClass());

	/**
	 * For each monitored file, a channel is created to maintain the current
	 * parse state of the file (position in file, ...). Each file may contain
	 * data for a set of traces.
	 */
	private class Channel implements Closeable {
		private Quantity<Time> channelCurrentTime = ZERO;
		private File file;
		private RandomAccessFile input;
		private double[][] valuesBuffer;
		private byte[] buffer = new byte[BUFFER_SIZE];
		private double[] timestampBuffer = new double[MAX_BUFFERED_LINES];
		private Map<TraceKey, Integer> traces = new HashMap<TraceKey, Integer>();
		private String linePart = ""; // contains any incomplete line at the end
										// of a buffer

		public Channel(File path) throws IOException {
			this.file = path;
			input = new RandomAccessFile(path, "r");
		}

		public File getFile() {
			return file;
		}

		public void addTrace(TraceKey key, int column) {
			// Check that the buffer array is large enough for this number
			// of columns
			if ((valuesBuffer == null) || (valuesBuffer[0].length < (column + 1))) {
				valuesBuffer = new double[MAX_BUFFERED_LINES][column + 1];
			}
			traces.put(key, column);
		}

		/**
		 * Call this method to read all data from the current position in the
		 * file to the end of the file.
		 * 
		 * @return <code>false</code> if the file stream was closed
		 *         unexpectedly.
		 */
		public boolean read() {
			try {
				if (input.getFilePointer() < input.length()) {
					// File has been changed, read additional lines.

					int lineCnt = 0;
					while (true) {
						// We avoid RandomAccessFile#readLine() as it is
						// performance-wise slow (many system calls) and we
						// cannot say whether a line a the end of the input is
						// complete or not.
						int len = input.read(buffer, 0, BUFFER_SIZE);
						boolean eof = (input.getFilePointer() == input.length());
						if (len > 0) {
							// Search for line terminators
							boolean eol = false;
							int s, i; // last line end (s), current index (i)
							for (s = 0, i = 0; i < len; i++) {
								eol = (buffer[i] == '\n' || buffer[i] == '\r');
								if (eol) {
									String line = new String(buffer, s, (i - s));
									if (!linePart.isEmpty()) {
										// Prepend the partial line from the
										// previous buffer
										line = linePart + line;
										linePart = "";
									}

									// Call subclasses to parse the line
									if (!skipLine(file, line)) {
										try {
											timestampBuffer[lineCnt] = parse(file, line, valuesBuffer[lineCnt]);

											lineCnt++;
											if (lineCnt >= MAX_BUFFERED_LINES) {
												// If MAX_BUFFERED_LINES is
												// reached,
												// we notify listeners of new
												// data
												notifySelector(MAX_BUFFERED_LINES);
												lineCnt = 0;
											}
										} catch (ParseException e) {
											// The error should be logged by the
											// implementation of the parse
											// function.
										}
									}
									s = ++i;
									// skip windows line ending
									if ((i < len) && (buffer[i - 1] == '\r') && (buffer[i] == '\n')) {
										i++;
										s++;
									}
								}
							}
							if (i != s) {
								// there is an incomplete line at the end of
								// the input, save that for next iteration
								linePart = new String(buffer, s, len - s);
							}
							if (eof) {
								break;
							}
						}
					}

					if (lineCnt > 0) {
						// Notify listeners of the remaining data
						notifySelector(lineCnt);
					}
				}
				return true;
			} catch (IOException e) {
				log.error("Error reading from input " + file + ". Close input.", e);
				try {
					close();
				} catch (IOException e2) { /* Ignore */
				}
				return false;
			}
		}

		private void notifySelector(int length) {
			Matrix values = matrix(valuesBuffer);
			Vector time = vector(timestampBuffer);
			if (values.rows() > length) {
				values = values.rows(0, length - 1);
				time = time.rows(0, length - 1);
			}
			for (Entry<TraceKey, Integer> trace : traces.entrySet()) {
				TimeSeries newData = new TimeSeries(time, values.column(trace.getValue()));
				// update current time to the maximum observation timestamp
				if (channelCurrentTime == null || newData.getEndTime() > channelCurrentTime.getValue(Time.SECONDS)) {
					channelCurrentTime = UnitsFactory.eINSTANCE.createQuantity(newData.getEndTime(), Time.SECONDS);
				}
				TraceEvent event = new TraceEvent(trace.getKey(), newData, channelCurrentTime);
				notifyListeners(event);
			}
		}

		@Override
		public void close() throws IOException {
			if (input != null) {
				input.close();
				input = null;
			}
		}
	}

	/**
	 * This threads listens to file change event reported by the operating
	 * system.
	 *
	 */
	private class WatchThread extends Thread {

		private Map<File, Channel> watchList = new HashMap<File, Channel>();
		private Set<File> observedDirectories = new HashSet<File>();
		private WatchService watcher;
		private volatile boolean stop;

		public WatchThread() throws IOException {
			watcher = FileSystems.getDefault().newWatchService();
		}

		public Channel getChannel(File file) {
			return watchList.get(file);
		}

		/**
		 * Add a new channel to be watched for changes.
		 * 
		 * @param channel
		 * @throws IOException
		 */
		public void registerChannel(Channel channel) throws IOException {
			File traceFile = channel.getFile();
			synchronized (this) {
				if (!watchList.containsKey(traceFile)) {
					File directory = traceFile.getParentFile();
					if (!observedDirectories.contains(directory)) {
						directory.toPath().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY,
								StandardWatchEventKinds.OVERFLOW);
						observedDirectories.add(directory);
					}
					watchList.put(traceFile, channel);
				}
			}
		}

		/**
		 * Call this method to trigger a read from the files manually.
		 */
		public void poll() {
			synchronized (this) {
				for (Channel channel : watchList.values()) {
					readFromChannel(channel);
				}
			}
		}

		@Override
		public void run() {
			while (!stop) {
				WatchKey key = null;
				try {
					// Wait for file events from the operating system
					key = watcher.take();
				} catch (InterruptedException e) {
					if (stop) {
						return;
					}
				}
				if (key.isValid()) {
					try {
						// Find the channel that needs to be updated.
						for (WatchEvent<?> event : key.pollEvents()) {
							Path directory = (Path) key.watchable();
							Path filename = (Path) event.context();
							Path absolutePath = directory.resolve(filename);
							Channel channel;
							synchronized (this) {
								channel = watchList.get(absolutePath.toFile());
								if (channel != null) {
									// we may also get notifications from
									// unrelevant files
									// in the same directory
									readFromChannel(channel);
								}
							}
						}
					} finally {
						// IMPORTANT: make key ready for new file events.
						key.reset();
					}
				}
			}
		}

		public void close() throws IOException {
			stop = true;
			this.interrupt();
			try {
				this.join();
			} catch (InterruptedException ex) {
				log.error("Interrupted when waiting for thread.", ex);
			}
			watcher.close();
			for (Channel channel : watchList.values()) {
				channel.close();
			}
		}

		private void readFromChannel(Channel channel) {
			if (!channel.read()) {
				// Channel was closed unexpectedly -> remove it from list
				try {
					channel.close();
				} catch (IOException e) {
					log.error("Error closing channel.");
				}
				watchList.remove(channel.getFile());
			}
		}
	}

	private WatchThread parser;

	public AbstractFileDataSource() throws IOException {
		this.parser = new WatchThread();
		this.parser.setDaemon(true);
	}

	public void close() throws IOException {
		if (this.parser != null) {
			this.parser.close();
			try {
				this.parser.join();
			} catch (InterruptedException e) {
				log.error("Error joining parser thread.", e);
			}
		}
		this.parser = null;
	}

	public List<TraceKey> addTrace(TraceConfiguration configuration) throws IOException {
		if (parser == null) {
			throw new IllegalStateException();
		}

		if (!(configuration instanceof FileTraceConfiguration)) {
			throw new IllegalArgumentException("Unsupported trace configuration type.");
		}

		FileTraceConfiguration fileTrace = (FileTraceConfiguration) configuration;
		List<TraceKey> keys = new LinkedList<TraceKey>();

		File inputFile = new File(fileTrace.getFile());
		if (inputFile.exists() && inputFile.canRead()) {
			// Open new channel if necessary
			Channel channel = parser.getChannel(inputFile);
			if (channel == null) {
				channel = new Channel(inputFile);
				parser.registerChannel(channel);
			}
			for (TraceToEntityMapping mapping : fileTrace.getMappings()) {
				TraceKey k = new TraceKey(fileTrace.getMetric(), fileTrace.getUnit(), fileTrace.getInterval(),
						mapping.getEntity());
				channel.addTrace(k, mapping.getTraceColumn() - 1);
				keys.add(k);
			}
			return keys;
		} else {
			throw new FileNotFoundException(inputFile.toString());
		}
	}
	
	public void load() {
		parser.start();
		parser.poll();
	}

	/**
	 * Check whether the line in a file should be parsed.
	 * 
	 * @param file
	 *            source file
	 * @param line
	 *            the content of the line
	 * @return <code>true</code> if this line should be skipped,
	 *         <code>false</code> otherwise.
	 */
	protected abstract boolean skipLine(File file, String line);

	/**
	 * Parses the given line
	 * 
	 * @param file
	 *            source file
	 * @param line
	 *            the content of the line
	 * @param values
	 *            the numerical values parsed from this line. This array may be
	 *            larger or smaller than the actual number of columns in this
	 *            line. If large the additional columns can be ignored. If
	 *            smaller, the array needs to be filled up with NaN values.
	 * @return the timestamp of the obsevation in this line
	 * @throws ParseException
	 *             if the line cannot be parsed correctly
	 */
	protected abstract double parse(File file, String line, double[] values) throws ParseException;
}
