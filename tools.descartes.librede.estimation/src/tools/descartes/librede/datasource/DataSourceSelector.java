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

import java.io.Closeable;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

/**
 * The selector is used to collect TraceEvents from a set of data sources. It
 * provides an asynchronous interface to poll or wait for events.
 * 
 * The data source selector also keeps track of the current time of the
 * different data sources. If one data source falls behind by a certain time, it
 * will be ignored until new data is available.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class DataSourceSelector implements Closeable, IDataSourceListener {

	private static final Quantity<Time> ZERO = UnitsFactory.eINSTANCE.createQuantity(0.0, Time.SECONDS);

	private static final Logger log = Loggers.DATASOURCE_LOG;

	// The minimum latest observation time of all data sources
	private Quantity<Time> minLatestObservationTime = null;

	// The maximum first observation time of all data sources
	private Quantity<Time> maxFirstObservationTime = null;

	private Set<IDataSource> dataSources = new HashSet<IDataSource>();

	// the waiting line of events
	private BlockingQueue<TraceEvent> events = new LinkedBlockingQueue<TraceEvent>();

	// a list of all traces with their corresponding last observation time.
	private Map<TraceKey, Quantity<Time>> latestObservations = new HashMap<TraceKey, Quantity<Time>>();

	// a list of all traces with their corresponding first observation time.
	private Map<TraceKey, Quantity<Time>> firstObservations = new HashMap<TraceKey, Quantity<Time>>();

	@Override
	public synchronized void dataAvailable(IDataSource datasource, TraceEvent e) {
		checkIsOpen();
		Quantity<Time> oldLastestObservationTime = latestObservations.get(e.getKey());
		Quantity<Time> oldFirstObservationTime = firstObservations.get(e.getKey());
		Quantity<Time> newLatestObservationTime = e.getLastObservationTime();
		// Update the internal start timestamps
		if (oldFirstObservationTime == null || (oldFirstObservationTime.getValue(Time.SECONDS) > newLatestObservationTime.getValue(Time.SECONDS))) {
			firstObservations.put(e.getKey(), newLatestObservationTime);
			maxFirstObservationTime = null;
		}		
		
		// Update the internal end timestamps
		if (oldLastestObservationTime == null
				|| (oldLastestObservationTime.getValue(Time.SECONDS) < newLatestObservationTime.getValue(Time.SECONDS))) {
			latestObservations.put(e.getKey(), newLatestObservationTime);
			minLatestObservationTime = null;
		}
		events.offer(e);
	}


	/**
	 * The latest observation time is the latest time for which all traces have
	 * monitoring (i.e., some traces might already have newer data available).
	 * 
	 * @return a time quantity or null if no time could be determined.
	 */
	public synchronized Quantity<Time> getLatestObservationTime() {
		checkIsOpen();
		if (minLatestObservationTime == null) {
			// Determine minimum latest observation time.
			for (TraceKey curKey : latestObservations.keySet()) {
				Quantity<Time> curLatestTime = latestObservations.get(curKey);
				if ((minLatestObservationTime == null) || 
						(minLatestObservationTime.getValue(Time.SECONDS) > curLatestTime.getValue(Time.SECONDS))) {
					minLatestObservationTime = curLatestTime;
				}
			}
		}
		return minLatestObservationTime;
	}
	
	/**
	 * The first observation time is the first time for which all traces have
	 * monitoring (i.e., some traces might have older data available).
	 * 
	 * @return a time quantity or null if no time could be determined.
	 */
	public synchronized Quantity<Time> getFirstObservationTime() {
		checkIsOpen();
		if (maxFirstObservationTime == null) {
			// Determine maximum first observation time.
			for (TraceKey curKey : firstObservations.keySet()) {
				Quantity<Time> curFirstTime = firstObservations.get(curKey);
				if ((maxFirstObservationTime == null) || 
						(maxFirstObservationTime.getValue(Time.SECONDS) < curFirstTime.getValue(Time.SECONDS))) {
					maxFirstObservationTime = curFirstTime;
				}
			}
		}
		return maxFirstObservationTime;
	}

	/**
	 * Polls for the next trace event.
	 * 
	 * @return the trace event or <code>null</code> if none is available.
	 */
	public TraceEvent poll() {
		checkIsOpen();
		return events.poll();
	}

	/**
	 * Polls for the next trace event and waits for the specified time if none
	 * is available.
	 * 
	 * @param timeout
	 * @return the trace event or <code>null</code> if none is available.
	 * @throws InterruptedException
	 */
	public TraceEvent poll(Quantity<Time> timeout) throws InterruptedException {
		checkIsOpen();
		return events.poll((long) timeout.getValue(Time.MILLISECONDS), TimeUnit.MILLISECONDS);
	}

	/**
	 * Waits until a new trace event is available.
	 * 
	 * @return the trace event or <code>null</code> if none is available.
	 * @throws InterruptedException
	 */
	public TraceEvent take() throws InterruptedException {
		checkIsOpen();
		return events.take();
	}

	public void add(IDataSource datasource) {
		checkIsOpen();
		datasource.addListener(this);
		dataSources.add(datasource);
	}

	public void remove(IDataSource datasource) {
		checkIsOpen();
		datasource.removeListener(this);
		dataSources.remove(datasource);
	}

	@Override
	public void close() throws IOException {
		if (dataSources != null) {
			for (IDataSource ds : dataSources) {
				ds.removeListener(this);
			}
			dataSources = null;
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close(); // Safety net
		super.finalize();
	}

	private void checkIsOpen() {
		if (dataSources == null) {
			throw new IllegalStateException();
		}
	}
}
