package tools.descartes.librede.repository;

import java.util.HashMap;
import java.util.Map;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;

public abstract class AbstractMonitoringRepository implements IMonitoringRepository {
	
	private Map<Metric, IMetricHandler> metricHandlers = new HashMap<Metric, IMetricHandler>();

	@Override
	public TimeSeries select(Metric metric, ModelEntity entity, double start, double end) {
		IMetricHandler handler = metricHandlers.get(metric);
		if (handler == null) {
			throw new IllegalArgumentException("Unsupported metric: " + metric.getName());
		}
		return handler.retrieve(this, metric, entity, start, end);
	}
	
	@Override
	public double select(Metric metric, ModelEntity entity, double start, double end, Aggregation func) {
		IMetricHandler handler = metricHandlers.get(metric);
		if (handler == null) {
			throw new IllegalArgumentException("Unsupported metric: " + metric.getName());
		}
		return handler.aggregate(this, metric, entity, start, end, func);
	}
}
