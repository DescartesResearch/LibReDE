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

	private static final Logger log = Logger.getLogger(DataSourceSelector.class);

	// The minimum latest observation time of all data sources
	private Quantity<Time> minLatestObservationTime = ZERO;

	// The maximum latest observation time of all data sources
	private Quantity<Time> maxLatestObservationTime = ZERO;

	private Set<IDataSource> dataSources = new HashSet<IDataSource>();

	// the waiting line of events
	private BlockingQueue<TraceEvent> events = new LinkedBlockingQueue<TraceEvent>();

	// a list of all traces with their corresponding last observation time.
	private Map<TraceKey, Quantity<Time>> latestObservations = new HashMap<TraceKey, Quantity<Time>>();

	// number of intervals after which a trace is considered to be unavailable.
	private int timeoutIntervals = 3;

	// the absolute timeout time
	// depending on aggregation interval max(timeout, timeoutIntervals *
	// interval) is the actual timeout
	private Quantity<Time> timeout = UnitsFactory.eINSTANCE.createQuantity(5, Time.MINUTES);

	@Override
	public void dataAvailable(IDataSource datasource, TraceEvent e) {
		checkIsOpen();
		Quantity<Time> oldLastestObservationTime = latestObservations.get(e.getKey());
		Quantity<Time> newLatestObservationTime = e.getLastObservationTime();
		// Update the internal timestamps
		if (oldLastestObservationTime == null
				|| (oldLastestObservationTime.getValue(Time.SECONDS) < newLatestObservationTime.getValue(Time.SECONDS))) {
			latestObservations.put(e.getKey(), newLatestObservationTime);
			Quantity<Time> oldMaxLatestObservationTime = maxLatestObservationTime;

			if ((newLatestObservationTime.getValue(Time.SECONDS) > maxLatestObservationTime.getValue(Time.SECONDS))) {
				maxLatestObservationTime = newLatestObservationTime;
			}
			minLatestObservationTime = maxLatestObservationTime;
			for (Quantity<Time> time : latestObservations.values()) {
				// check whether this trace is timed out
				if (!isTimeout(maxLatestObservationTime, time, e.getKey().getInterval())) {
					if (minLatestObservationTime.getValue(Time.SECONDS) > time.getValue(Time.SECONDS)) {
						minLatestObservationTime = time;
					}
				} else {
					if (!isTimeout(oldMaxLatestObservationTime, time, e.getKey().getInterval())) {
						// the trace just timed out so inform the user once.
						log.warn("The trace " + e.getKey().getMetric() + ":" + e.getKey().getUnit() + ":"
								+ e.getKey().getInterval() + ":" + e.getKey().getEntity().getName()
								+ " timed out. Stop waiting for new measurement data.");
					}
				}
			}
		}
		events.offer(e);
	}

	/**
	 * The absolute timeout is a time value after which a trace is considered to
	 * time out. The maximum between the absolute and the relative timeout is
	 * the effective timeout for a trace.
	 * 
	 * @param timeout
	 */
	public void setAbsoluteTimeout(Quantity<Time> timeout) {
		this.timeout = timeout;
	}

	public Quantity<Time> getAbsoluteTimeout() {
		return timeout;
	}

	/**
	 * The relative timeout specifies after how many intervals (as specified for
	 * the trace) without measurement data a trace is considered to time out.
	 * The maximum between the absolute and the relative timeout is the
	 * effective timeout for a trace.
	 * 
	 * @param timeoutIntervals
	 */
	public void setRelativeTimeout(int timeoutIntervals) {
		this.timeoutIntervals = timeoutIntervals;
	}

	public int getRelativeTimeout() {
		return timeoutIntervals;
	}

	/**
	 * The latest observation time is the latest time for which all traces have
	 * monitoring (i.e., some traces might already have newer data available).
	 * Traces considere to be timed out, i.e. in the configured timespan there
	 * was no new data available, are excluded from the last observation time.
	 * 
	 * @return
	 */
	public Quantity<Time> getLatestObservationTime() {
		checkIsOpen();
		return minLatestObservationTime;
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

	private boolean isTimeout(Quantity<Time> latestObservationTime, Quantity<Time> time, Quantity<Time> aggrInterval) {
		double curTimeout = Math.max(timeout.getValue(Time.SECONDS), aggrInterval.getValue(Time.SECONDS)
				* timeoutIntervals);
		return latestObservationTime.minus(time).getValue(Time.SECONDS) >= curTimeout;
	}
}
