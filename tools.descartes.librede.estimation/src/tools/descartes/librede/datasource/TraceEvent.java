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

import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;

/**
 * A trace event is used to signal that new monitoring data is available from a
 * trace.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class TraceEvent {

	private final TraceKey key;
	private final TimeSeries data;
	private final Quantity<Time> lastObservationTime;

	public TraceEvent(TraceKey key, TimeSeries data, Quantity<Time> lastObservationTime) {
		this.key = key;
		this.data = data;
		this.lastObservationTime = lastObservationTime;
	}

	/**
	 * @return a TraceKey identifying the source trace.
	 */
	public TraceKey getKey() {
		return key;
	}

	/**
	 * @return a TimeSeries containing the new monitoring data.
	 */
	public TimeSeries getData() {
		return data;
	}

	/**
	 * @return the timestamp of the newest datapoint in the measurement data
	 */
	public Quantity<Time> getLastObservationTime() {
		return lastObservationTime;
	}
}
