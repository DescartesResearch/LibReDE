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
package tools.descartes.librede.util;

import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.exceptions.NonOverlappingRangeException;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Time;

public class RepositoryUtil {

	public class Range {
		private double start;
		private double end;

		Range(double start, double end) {
			this.setStart(start);
			this.setEnd(end);
		}

		public double getStart() {
			return start;
		}

		public void setStart(double start) {
			this.start = start;
		}

		public double getEnd() {
			return end;
		}

		public void setEnd(double end) {
			this.end = end;
		}
		
		public double getValue(){
			return end-start;
		}
	}

	/**
	 * Returns the maximum intersection interval of all TimeSeries which are
	 * specified in both repository and configuration.
	 * 
	 * @param repository
	 * @param configuration
	 * @return range
	 * @throws NonOverlappingRangeException 
	 */
	public static Range deduceMaximumOverlappingInterval(IMonitoringRepository repository, LibredeConfiguration configuration) throws NonOverlappingRangeException {
		double maxStart = Double.MIN_VALUE;
		double minEnd = Double.MAX_VALUE;
		for (TraceConfiguration trace : configuration.getInput().getObservations()) {
			if (trace.getMappings().size() >= 1) {
				maxStart = Math.max(repository.getMonitoringStartTime(trace.getMetric(), trace.getMappings().get(0)
						.getEntity(), trace.getAggregation()).getValue(Time.SECONDS), maxStart);
				minEnd = Math.min(repository.getMonitoringEndTime(trace.getMetric(), trace.getMappings().get(0)
						.getEntity(), trace.getAggregation()).getValue(Time.SECONDS), minEnd);
			}
		}
		Range range = (new RepositoryUtil()).new Range(maxStart, minEnd);
		if(range.getValue() < 0){
			throw new NonOverlappingRangeException();
		}
		return range;
	}
	
	public static Range deduceMaximumInterval(IMonitoringRepository repository, LibredeConfiguration configuration) {
		double start = Double.MAX_VALUE;
		double end = Double.MIN_VALUE;
		for (TraceConfiguration trace : configuration.getInput().getObservations()) {
			if (trace.getMappings().size() >= 1) {
				start = Math.min(repository.getMonitoringStartTime(trace.getMetric(), trace.getMappings().get(0)
						.getEntity(), trace.getAggregation()).getValue(Time.SECONDS), start);
				end = Math.max(repository.getMonitoringEndTime(trace.getMetric(), trace.getMappings().get(0)
						.getEntity(), trace.getAggregation()).getValue(Time.SECONDS), end);
			}
		}
		return (new RepositoryUtil()).new Range(start, end);
	}
}
