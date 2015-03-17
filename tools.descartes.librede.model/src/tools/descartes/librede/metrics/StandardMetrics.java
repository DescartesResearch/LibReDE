package tools.descartes.librede.metrics;

import tools.descartes.librede.units.Proportion;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;

/**
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public interface StandardMetrics {
	public static final Metric ARRIVAL_RATE = MetricsFactory.eINSTANCE.createMetric("ARRIVAL_RATE", RequestRate.INSTANCE, Aggregation.AVERAGE);
	
	public static final Metric ARRIVALS = MetricsFactory.eINSTANCE.createMetric("ARRIVALS", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.SUM, Aggregation.MINIMUM, Aggregation.MAXIMUM);
	
	public static final Metric BUSY_TIME = MetricsFactory.eINSTANCE.createMetric("BUSY_TIME", Time.INSTANCE, Aggregation.SUM);
	
	public static final Metric DEPARTURES = MetricsFactory.eINSTANCE.createMetric("DEPARTURES", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.SUM, Aggregation.MINIMUM, Aggregation.MAXIMUM);
	
	public static final Metric IDLE_TIME = MetricsFactory.eINSTANCE.createMetric("IDLE_TIME", Time.INSTANCE, Aggregation.SUM);
	
	public static final Metric QUEUE_LENGTH_SEEN_ON_ARRIVAL = MetricsFactory.eINSTANCE.createMetric("QUEUE_LENGTH_SEEN_ON_ARRIVAL", RequestCount.INSTANCE, Aggregation.NONE, Aggregation.AVERAGE, Aggregation.MAXIMUM, Aggregation.MINIMUM);
	
	public static final Metric RESPONSE_TIME = MetricsFactory.eINSTANCE.createMetric("RESPONSE_TIME", Time.INSTANCE, Aggregation.NONE, Aggregation.AVERAGE, Aggregation.MAXIMUM, Aggregation.MINIMUM);
	
	public static final Metric THROUGHPUT = MetricsFactory.eINSTANCE.createMetric("THROUGHPUT", RequestRate.INSTANCE, Aggregation.AVERAGE);
	
	public static final Metric UTILIZATION = MetricsFactory.eINSTANCE.createMetric("UTILIZATION", Proportion.INSTANCE, Aggregation.AVERAGE);
}
