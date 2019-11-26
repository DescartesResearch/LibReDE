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
package tools.descartes.librede.datasource.kieker;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import tools.descartes.librede.datasource.Loggers;

/**
 * This class is a thread, that has a watchService, that
 * watches the directories in the watchlist. 
 * Every directory in the watchlist is handled by another KiekerChannel.
 * @author torsten
 *
 */
public class KiekerWatchThread extends Thread {
	/**
	 * The logging instance
	 */
	private final Logger log = Loggers.DATASOURCE_LOG;
	/**
	 * Indicates, if the thread was stopped.
	 */
	protected volatile boolean stop;	
	/**
	 * 
	 */
	private boolean isInitialized;
	/**
	 * A list of all the directories, that watchservice listens to
	 * with the KiekerChannel, that deals with the informations
	 * in the directory.
	 */
	private Map<String, KiekerChannel> watchList = new HashMap<>();
	/**
	 * The warchService is a Java instance, that listens to changes in a directory.
	 */
	private WatchService watcher;

	/**
	 * The constructor initializes the watchservice.
	 * @throws IOException
	 */
	public KiekerWatchThread() throws IOException {
		watcher = FileSystems.getDefault().newWatchService();
		this.isInitialized = false;
	}
	/**
	 * Here we can ask for a channel, that deals with a given directory.
	 * @param inputdirectory
	 * @return
	 */
	public KiekerChannel getChannel(File inputdirectory) {
		return watchList.get(inputdirectory.getAbsolutePath());
	}
	
	/**
	 * Add a new channel to be watched for changes.
	 * 
	 * @param channel
	 * @throws IOException
	 */
	public void registerChannel(KiekerChannel channel) throws IOException {
		synchronized (this) {
			//check if the input directory is already watched
			if (!watchList.containsKey(channel.getInputDirectory().getAbsolutePath())) {
				//if not watch it now
					WatchKey key = channel.getInputDirectory().toPath().register(watcher, 
							StandardWatchEventKinds.ENTRY_MODIFY, 
							StandardWatchEventKinds.ENTRY_CREATE,
							StandardWatchEventKinds.OVERFLOW);
				//and add it to the internal list
				watchList.put(channel.getInputDirectory().getAbsolutePath(), channel);
			}
		}
	}
	
	
	/**
	 * Call this method to trigger a read from the files manually.
	 * This is usually only done one time at the start of the procedure.
	 */
	public void poll() {
		synchronized (this) {
			//for all directories/channels we have
			for (KiekerChannel channel : watchList.values()) {
				//read one time.
				readFromChannel(channel);
			}
		}
	}
	
	/**
	 * Read from the given channel. 
	 * (This will publish data, if the buffer is full, or the file is at the end)
	 * @param channel
	 */
	protected void readFromChannel(KiekerChannel channel) {
		if (!channel.read()) {
			// Channel was closed unexpectedly -> remove it from list
			try {
				channel.close();
			} catch (IOException e) {
				log.error("Error closing channel.");
			}
			watchList.remove(channel.getInputDirectory().getAbsolutePath());
		}
	}
	/**
	 * This method runs in the background until we close the thread.
	 */
	@Override
	public void run() {
		while (!stop) {
			if(!isInitialized){
				for (Entry<String, KiekerChannel> entry : watchList.entrySet()) {
					readFromChannel(entry.getValue());
				}
				isInitialized = true;
			}
			WatchKey key = null;
			try {
				// Wait for file events from the operating system
				key = watcher.take();
			} catch (InterruptedException e) {
				if (stop) {
					return;
				}
			}
			//if it is a valid event
			if (key.isValid()) {
				try {
					// Find the channel that needs to be updated.
					for (WatchEvent<?> event : key.pollEvents()) {
						//get the directory we deal with
						Path directory = (Path) key.watchable();
						//and the file that changed
						Path filename = (Path) event.context();
						//actually we only trigger that something changed
						//and then try to read more data. so the file that changed
						//does not really matter.
						KiekerChannel channel;
						synchronized (this) {
							//check if we have a channel, that whatches this directory.
							channel = watchList.get(directory.toString());
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
	/**
	 * Stop this thread.
	 * @throws IOException
	 */
	public void close() throws IOException {
		stop = true;
		this.interrupt();
		try {
			this.join();
		} catch (InterruptedException ex) {
			log.error("Interrupted when waiting for thread.", ex);
		}
		for (KiekerChannel channel : watchList.values()) {
			channel.close();
		}
	}
}
