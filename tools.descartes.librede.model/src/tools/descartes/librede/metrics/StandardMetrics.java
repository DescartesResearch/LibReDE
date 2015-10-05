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
package tools.descartes.librede.metrics;

import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;

/**
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public interface StandardMetrics {
	public static final Metric<RequestRate> ARRIVAL_RATE = MetricsFactory.eINSTANCE.createMetric("ARRIVAL_RATE", "arrival rate", RequestRate.INSTANCE, Aggregation.AVERAGE);
	
	public static final Metric<RequestCount> ARRIVALS = MetricsFactory.eINSTANCE.createMetric("ARRIVALS", "arrivals", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.SUM, Aggregation.CUMULATIVE_SUM, Aggregation.MINIMUM, Aggregation.MAXIMUM);
	
	public static final Metric<Time> BUSY_TIME = MetricsFactory.eINSTANCE.createMetric("BUSY_TIME", "busy time", Time.INSTANCE, Aggregation.SUM, Aggregation.CUMULATIVE_SUM);
	
	public static final Metric<RequestCount> DEPARTURES = MetricsFactory.eINSTANCE.createMetric("DEPARTURES", "departures", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.SUM, Aggregation.CUMULATIVE_SUM, Aggregation.MINIMUM, Aggregation.MAXIMUM);
	
	public static final Metric<Time> IDLE_TIME = MetricsFactory.eINSTANCE.createMetric("IDLE_TIME", "idle time", Time.INSTANCE, Aggregation.SUM, Aggregation.CUMULATIVE_SUM);
	
	public static final Metric<RequestCount> QUEUE_LENGTH_SEEN_ON_ARRIVAL = MetricsFactory.eINSTANCE.createMetric("QUEUE_LENGTH_SEEN_ON_ARRIVAL", "queue length seen on arrival", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.AVERAGE, Aggregation.MAXIMUM, Aggregation.MINIMUM);
	
	public static final Metric<Time> RESPONSE_TIME = MetricsFactory.eINSTANCE.createMetric("RESPONSE_TIME", "response time", Time.INSTANCE, Aggregation.NONE, Aggregation.AVERAGE, Aggregation.CUMULATIVE_SUM, Aggregation.SUM, Aggregation.MAXIMUM, Aggregation.MINIMUM);
	
	public static final Metric<RequestRate> THROUGHPUT = MetricsFactory.eINSTANCE.createMetric("THROUGHPUT", "throughput", RequestRate.INSTANCE, Aggregation.AVERAGE);
	
	public static final Metric<Ratio> UTILIZATION = MetricsFactory.eINSTANCE.createMetric("UTILIZATION", "utilization", Ratio.INSTANCE, Aggregation.AVERAGE);
	
	public static final Metric<RequestCount> VISITS = MetricsFactory.eINSTANCE.createMetric("VISITS", "visits", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.AVERAGE);
	
	public static final Metric<Time> RESIDENCE_TIME = MetricsFactory.eINSTANCE.createMetric("RESIDENCE_TIME", "residence time", Time.INSTANCE, Aggregation.NONE, Aggregation.AVERAGE, Aggregation.CUMULATIVE_SUM, Aggregation.SUM, Aggregation.MAXIMUM, Aggregation.MINIMUM);
	
	public static final Metric<Time> RESOURCE_DEMAND = MetricsFactory.eINSTANCE.createMetric("RESOURCE_DEMAND", "resource demand", Time.INSTANCE, Aggregation.AVERAGE);
	
	public static final Metric<Time> STEAL_TIME = MetricsFactory.eINSTANCE.createMetric("STEAL_TIME", "steal time", Time.INSTANCE, Aggregation.SUM);
	
	public static final Metric<Ratio> CONTENTION = MetricsFactory.eINSTANCE.createMetric("CONTENTION", "contention", Ratio.INSTANCE, Aggregation.AVERAGE);
}
