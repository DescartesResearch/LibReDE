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
	public static final Metric ARRIVAL_RATE = MetricsFactory.eINSTANCE.createMetric("ARRIVAL_RATE", "arrival rate", RequestRate.INSTANCE, Aggregation.AVERAGE);
	
	public static final Metric ARRIVALS = MetricsFactory.eINSTANCE.createMetric("ARRIVALS", "arrivals", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.SUM, Aggregation.MINIMUM, Aggregation.MAXIMUM);
	
	public static final Metric BUSY_TIME = MetricsFactory.eINSTANCE.createMetric("BUSY_TIME", "busy time", Time.INSTANCE, Aggregation.SUM);
	
	public static final Metric DEPARTURES = MetricsFactory.eINSTANCE.createMetric("DEPARTURES", "departures", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.SUM, Aggregation.MINIMUM, Aggregation.MAXIMUM);
	
	public static final Metric IDLE_TIME = MetricsFactory.eINSTANCE.createMetric("IDLE_TIME", "idle time", Time.INSTANCE, Aggregation.SUM);
	
	public static final Metric QUEUE_LENGTH_SEEN_ON_ARRIVAL = MetricsFactory.eINSTANCE.createMetric("QUEUE_LENGTH_SEEN_ON_ARRIVAL", "queue length seen on arrival", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.AVERAGE, Aggregation.MAXIMUM, Aggregation.MINIMUM);
	
	public static final Metric RESPONSE_TIME = MetricsFactory.eINSTANCE.createMetric("RESPONSE_TIME", "response time", Time.INSTANCE, Aggregation.NONE, Aggregation.AVERAGE, Aggregation.MAXIMUM, Aggregation.MINIMUM);
	
	public static final Metric THROUGHPUT = MetricsFactory.eINSTANCE.createMetric("THROUGHPUT", "throughput", RequestRate.INSTANCE, Aggregation.AVERAGE);
	
	public static final Metric UTILIZATION = MetricsFactory.eINSTANCE.createMetric("UTILIZATION", "utilization", Ratio.INSTANCE, Aggregation.AVERAGE);
}
