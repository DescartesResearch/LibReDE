/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceFilter;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
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

	private final Logger log = Loggers.DATASOURCE_LOG;
	
	public static abstract class Stream {
		
		public abstract void open() throws IOException;
	
		public abstract int read(byte[] data, int offste, int length) throws IOException;
		
		public abstract void close() throws IOException;
		
	}
	
	private static class FileStream extends Stream {
		private final File file;
		private RandomAccessFile input;
		
		public FileStream(File path) throws IOException {
			this.file = path;
		}
		
		public void open() throws IOException {
			input = new RandomAccessFile(file, "r");			
		}

		@Override
		public int read(byte[] data, int offset, int length) throws IOException {
			if (input.getFilePointer() >= input.length()) {
				return -1;
			}
			return input.read(data, offset, length);
		}

		@Override
		public void close() throws IOException {
			input.close();			
		}
		
		@Override
		public String toString() {
			return file.toString();
		}

		@Override
		public int hashCode() {
			return ((file == null) ? 0 : file.hashCode());
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FileStream other = (FileStream) obj;
			if (file == null) {
				if (other.file != null)
					return false;
			} else if (!file.equals(other.file))
				return false;
			return true;
		}

		public File getFile() {
			return file;
		}

	}
	
	private static class SocketStream extends Stream {
		private final SocketAddress address;
		private SocketChannel socket;
		
		public SocketStream(String host, int port) throws IOException {
			this.address = new InetSocketAddress(host, port);
		}
		
		@Override
		public void open() throws IOException {
			socket = SocketChannel.open();
			socket.configureBlocking(false);
			socket.connect(address);
		}

		@Override
		public int read(byte[] data, int offset, int length) throws IOException {
			return socket.read(ByteBuffer.wrap(data, offset, length));
		}

		@Override
		public void close() throws IOException {
			socket.close();			
		}
		
		public SocketChannel getSocket() {
			return socket;
		}

		@Override
		public int hashCode() {
			return ((address == null) ? 0 : address.hashCode());
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SocketStream other = (SocketStream) obj;
			if (address == null) {
				if (other.address != null)
					return false;
			} else if (!address.equals(other.address))
				return false;
			return true;
		}

	
	}

	/**
	 * For each monitored file, a channel is created to maintain the current
	 * parse state of the file (position in file, ...). Each file may contain
	 * data for a set of traces.
	 */
	private class Channel implements Closeable {
		private Quantity<Time> channelCurrentTime = ZERO;
		private Stream input;
		private String[][] valuesBuffer = new String[MAX_BUFFERED_LINES][0];
		private byte[] buffer = new byte[BUFFER_SIZE];
		private double[] timestampBuffer = new double[MAX_BUFFERED_LINES];
		private Map<TraceKey, Integer> traces = new HashMap<TraceKey, Integer>();
		private String linePart = ""; // contains any incomplete line at the end
										// of a buffer
		private int readLines = 0;

		public Channel(Stream input) throws IOException {
			this.input = input;
			input.open();
		}

		public Stream getStream() {
			return input;
		}

		public void addTrace(TraceKey key, int column) {
			int maxColumn = column;
			for (TraceFilter filter : key.getFilters()) {
				maxColumn = Math.max(filter.getTraceColumn(), maxColumn);
			}
			
			synchronized(traces) {
				// Check that the buffer array is large enough for this number
				// of columns
				if (valuesBuffer[0].length < (maxColumn + 1)) {
					valuesBuffer = new String[MAX_BUFFERED_LINES][maxColumn + 1];
				}
				traces.put(key, column);
			}
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
				int lineCnt = 0;
				while (true) {
					// We avoid RandomAccessFile#readLine() as it is
					// performance-wise slow (many system calls) and we
					// cannot say whether a line a the end of the input is
					// complete or not.
					int len = input.read(buffer, 0, BUFFER_SIZE);
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
								
								// New line parsed
								readLines++;

								// Call subclasses to parse the line
								if (!skipLine(input, line, readLines)) {
									try {
										synchronized(traces) {
											timestampBuffer[lineCnt] = parse(input, line, valuesBuffer[lineCnt], readLines);
											lineCnt++;
											if (lineCnt >= MAX_BUFFERED_LINES) {
												// If MAX_BUFFERED_LINES is
												// reached,
												// we notify listeners of new
												// data
												notifySelector(MAX_BUFFERED_LINES);
												lineCnt = 0;
											}
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
					} else {
						break;
					}
				}

				if (lineCnt > 0) {
					// Notify listeners of the remaining data
					synchronized(traces) {
						notifySelector(lineCnt);
					}
				}
				return true;
			} catch (IOException e) {
				log.error("Error reading from input " + input + ". Close input.", e);
				try {
					close();
				} catch (IOException e2) { /* Ignore */
				}
				return false;
			}
		}

		private void notifySelector(int length) {
			for (Entry<TraceKey, Integer> trace : traces.entrySet()) {
				TimeSeries newData = getNewData(trace.getKey(), trace.getValue(), length);
				if (!newData.isEmpty()) {
					// update current time to the maximum observation timestamp
					if (channelCurrentTime == null || newData.getEndTime() > channelCurrentTime.getValue(Time.SECONDS)) {
						channelCurrentTime = UnitsFactory.eINSTANCE.createQuantity(newData.getEndTime(), Time.SECONDS);
					}
					TraceEvent event = new TraceEvent(trace.getKey(), newData, channelCurrentTime);
					notifyListeners(event);
				}
			}
		}

		private TimeSeries getNewData(TraceKey key, int column, int length) {
			if (column < 0) {
				throw new IllegalArgumentException();
			}
			VectorBuilder timestamps = VectorBuilder.create(length);
			VectorBuilder values = VectorBuilder.create(length);
			List<TraceFilter> filters = key.getFilters();
			for (int i = 0; i < length; i++) {
				if (applyFilters(filters, valuesBuffer[i])) {
					try {
						// The read lines counter is already increased further so we need
						// do recalculate the actual position in the file.
						double value = parseNumber(input, valuesBuffer[i][column], readLines - (length - i - 1));
						timestamps.add(timestampBuffer[i]);
						values.add(value);
					} catch (ParseException e) {
						// The error should be logged by the
						// implementation of the parse
						// function.
					}
				}
			}
			Vector ts = timestamps.toVector();
			if (ts.isEmpty()) {
				return TimeSeries.EMPTY;
			}
			return new TimeSeries(ts, values.toVector());
		}

		private boolean applyFilters(List<TraceFilter> filters, String[] line) {
			for (TraceFilter f : filters) {
				if (!f.getValue().equals(line[f.getTraceColumn()])) {
					return false;
				}
			}
			return true;
		}

		@Override
		public void close() throws IOException {
			if (input != null) {
				input.close();
				input = null;
			}
		}
	}
	
	private abstract class ReaderThread extends Thread {
		protected Map<Stream, Channel> watchList = new HashMap<>();
		protected volatile boolean stop;		

		public Channel getChannel(Stream stream) {
			return watchList.get(stream);
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
		
		protected void readFromChannel(Channel channel) {
			if (!channel.read()) {
				// Channel was closed unexpectedly -> remove it from list
				try {
					channel.close();
				} catch (IOException e) {
					log.error("Error closing channel.");
				}
				watchList.remove(channel.getStream());
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
			for (Channel channel : watchList.values()) {
				channel.close();
			}
		}
	}
	
	private class SocketReaderThread extends ReaderThread {		
		private final Selector selector;
		private final Queue<Channel> newChannels = new LinkedList<>();
		
		public SocketReaderThread() throws IOException {
			selector = Selector.open();
		}
		
		public void registerChannel(Channel channel) throws IOException {
			Stream stream = channel.getStream();
			if (stream instanceof SocketStream) {
				synchronized (newChannels) {
					newChannels.add(channel);
				}
				selector.wakeup();
				watchList.put(stream, channel);
			} else {
				throw new IllegalArgumentException();
			}
		}
		
		@Override
		public void run() {
			try {
				while(!stop) {
					synchronized (newChannels) {
						while(!newChannels.isEmpty()) {
							Channel cur = newChannels.poll();
							SocketStream socketStream = (SocketStream)cur.getStream();
							log.info("Open socket " + socketStream.getSocket().getRemoteAddress());
							socketStream.getSocket().register(selector, SelectionKey.OP_CONNECT, cur);
						}
					}
					
					selector.select();
					
					for (SelectionKey key : selector.selectedKeys()) {
						if (!key.isValid()) {
							continue;
						}
						
						if (key.isReadable()) {
							Channel curChannel = (Channel)key.attachment();
							if (curChannel != null) {
								log.info("Read newly available data from " +key.channel() + ".");
								readFromChannel(curChannel);
							}
						} else if(key.isConnectable()) {
							SocketChannel curChannel = (SocketChannel)key.channel();
							if (curChannel.finishConnect()) {
								log.info("Connection to " + curChannel + " established.");								
								curChannel.register(selector, SelectionKey.OP_READ, key.attachment());
								key.cancel();
							}
						}
					}
				}				
			} catch (IOException e) {
				log.error("Error waiting for new data.", e);
			}		
		}
		
		@Override
		public void close() throws IOException{
			super.close();
			selector.close();
		}
	}

	/**
	 * This threads listens to file change event reported by the operating
	 * system.
	 *
	 */
	private class WatchThread extends ReaderThread {
		private Map<File, Channel> channels = new HashMap<>();
		private Set<File> observedDirectories = new HashSet<File>();
		private WatchService watcher;

		public WatchThread() throws IOException {
			watcher = FileSystems.getDefault().newWatchService();
		}

		/**
		 * Add a new channel to be watched for changes.
		 * 
		 * @param channel
		 * @throws IOException
		 */
		public void registerChannel(Channel channel) throws IOException {
			Stream stream = channel.getStream();
			if (stream instanceof FileStream) {
				synchronized (this) {
					if (!watchList.containsKey(stream)) {
						File traceFile = ((FileStream)stream).getFile();
						File directory = traceFile.getParentFile();
						if (!observedDirectories.contains(directory)) {
							WatchKey key = directory.toPath().register(watcher, StandardWatchEventKinds.ENTRY_MODIFY,
									StandardWatchEventKinds.OVERFLOW);
							observedDirectories.add(directory);
						}
						channels.put(traceFile, channel);
						watchList.put(stream, channel);
					}
				}
			} else {
				throw new IllegalArgumentException();
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
						break;
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
								channel = channels.get(absolutePath.toFile());
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
			try {
				watcher.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void close() throws IOException {
			super.close();
			watcher.close();
		}		
	}

	private WatchThread fileWatcher;
	private SocketReaderThread socketWatcher;

	public AbstractFileDataSource() throws IOException {
		this.fileWatcher = new WatchThread();
		this.fileWatcher.setDaemon(true);
		this.socketWatcher = new SocketReaderThread();
		this.socketWatcher.setDaemon(true);
	}

	public void close() throws IOException {
		if (this.fileWatcher != null) {
			this.fileWatcher.close();
			try {
				this.fileWatcher.join();
			} catch (InterruptedException e) {
				log.error("Error joining fileWatcher thread.", e);
			}
		}
		this.fileWatcher = null;
		if (this.socketWatcher != null) {
			this.socketWatcher.close();
			try {
				this.socketWatcher.join();
			} catch (InterruptedException e) {
				log.error("Error joining socketWatcher thread");
			}
		}
		this.socketWatcher = null;
	}

	public List<TraceKey> addTrace(TraceConfiguration configuration) throws IOException {
		if (fileWatcher == null) {
			throw new IllegalStateException();
		}

		Channel channel;
		if (configuration instanceof FileTraceConfiguration) {
			FileTraceConfiguration fileTrace = (FileTraceConfiguration) configuration;
			File inputFile = new File(fileTrace.getFile());
			if (inputFile.exists() && inputFile.canRead()) {
				FileStream stream = new FileStream(inputFile);
				// Open new channel if necessary		
				channel = fileWatcher.getChannel(stream);
				if (channel == null) {
					channel = new Channel(stream);
					fileWatcher.registerChannel(channel);
				}
			} else {
				throw new FileNotFoundException(inputFile.toString());
			}
			
		} else {
			URI location;
			try {
				location = new URI(configuration.getLocation());
				if (location.getScheme().equals("socket")) {
					String host = location.getHost();
					int port = location.getPort();
					SocketStream stream = new SocketStream(host, port);
					// Open new channel if necessary		
					channel = socketWatcher.getChannel(stream);
					if (channel == null) {
						channel = new Channel(stream);
						socketWatcher.registerChannel(channel);
					}
				} else {
					log.error("Unkown scheme " + location.getScheme());
					return Collections.emptyList();
				}				
			} catch (URISyntaxException e) {
				log.error("Could not parse location URI.", e);
				return Collections.emptyList();
			}			
		}
		List<TraceKey> keys = new LinkedList<TraceKey>();
		for (TraceToEntityMapping mapping : configuration.getMappings()) {
			if (mapping.getTraceColumn() <= 0) {
				 throw new IllegalArgumentException("The column in an entity mapping must be larger than 0.");
			}
			TraceKey k = new TraceKey(configuration.getMetric(), configuration.getUnit(), configuration.getInterval(),
					mapping.getEntity(), configuration.getAggregation(), mapping.getFilters());
			notifyListenersNewKey(k);
			channel.addTrace(k, mapping.getTraceColumn() - 1);
			keys.add(k);
		}
		return keys;
	}

	public void load() {
		fileWatcher.start();
		fileWatcher.poll();
		socketWatcher.start();
		socketWatcher.poll();
	}

	/**
	 * Check whether the line in a file should be parsed.
	 * 
	 * @param stream
	 *            source stream
	 * @param line
	 *            the content of the line
	 * @param readLines
	 *            the current line number
	 * @return <code>true</code> if this line should be skipped,
	 *         <code>false</code> otherwise.
	 */
	protected abstract boolean skipLine(Stream stream, String line, int readLines);

	/**
	 * Parses the given line
	 * 
	 * @param stream
	 *            source stream
	 * @param line
	 *            the content of the line
	 * @param readLines
	 *            the current line number
	 * @param values
	 *            the column values parsed from this line. This array may be
	 *            larger or smaller than the actual number of columns in this
	 *            line. If large the additional columns can be ignored. If
	 *            smaller, the array needs to be filled up with NaN values.
	 * @return the timestamp of the observation in this line
	 * @throws ParseException
	 *             if the line cannot be parsed correctly
	 */
	protected abstract double parse(Stream stream, String line, String[] values, int readLines) throws ParseException;

	/**
	 * Parses a floating-point number from a string.
	 *
	 * @param stream
	 *            source stream
	 * @param value
	 *            the floating point number in String representation.
	 * @param readLines
	 *            the current line number
	 * @return a double value
	 * @throws ParseException
	 *             if the number cannot be parsed correctly
	 */
	protected abstract double parseNumber(Stream stream, String value, int readLines) throws ParseException;
}
