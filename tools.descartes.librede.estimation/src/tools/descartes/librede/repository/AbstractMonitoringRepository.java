package tools.descartes.librede.repository;

import java.util.HashMap;
import java.util.Map;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public abstract class AbstractMonitoringRepository implements IMonitoringRepository {
	
	public static class DefaultMetricHandler implements IMetricHandler {

		@Override
		public TimeSeries select(IMonitoringRepository repository, Metric metric, Unit unit, ModelEntity entity, Quantity start,
				Quantity end) {
			TimeSeries series = repository.select(metric, unit, entity);
			return series.subset(start.getValue(Time.SECONDS), end.getValue(Time.SECONDS));
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric metric, Unit unit, ModelEntity entity, Quantity start,
				Quantity end, Aggregation func) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean contains(IMonitoringRepository repository, Metric metric, ModelEntity entity,
				Quantity aggregationInterval) {
			return repository.contains(metric, entity, aggregationInterval, false);
		}
		
	}
	
	private Map<Metric, IMetricHandler> metricHandlers = new HashMap<Metric, IMetricHandler>();

	@Override
	public TimeSeries select(Metric metric, Unit unit, ModelEntity entity, Quantity start, Quantity end) {
		IMetricHandler handler = getMetricHandler(metric);
		return handler.select(this, metric, unit, entity, start, end);
	}
	
	@Override
	public double select(Metric metric, Unit unit, ModelEntity entity, Quantity start, Quantity end, Aggregation func) {
		IMetricHandler handler = getMetricHandler(metric);
		return handler.aggregate(this, metric, unit, entity, start, end, func);
	}
	
	@Override
	public boolean contains(Metric metric, ModelEntity entity, Quantity maximumAggregationInterval) {
		return contains(metric, entity, maximumAggregationInterval, true);
	}

	protected IMetricHandler getMetricHandler(Metric metric) {
		IMetricHandler handler = metricHandlers.get(metric);
		if (handler == null) {
			handler = Registry.INSTANCE.getMetricHandler(metric);
			if (handler == null) {
				throw new IllegalArgumentException("Unsupported metric: " + metric.getName());
			}
			metricHandlers.put(metric, handler);
		}
		return handler;
	}
}
