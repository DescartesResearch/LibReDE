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
	
	public static class DefaultMetricHandler<D extends Dimension> implements IMetricHandler<D> {
				
		private Metric<D> metric;
		
		public DefaultMetricHandler(Metric<D> metric) {
			this.metric = metric;
		}
		
		@Override
		public Metric<D> getMetric() {
			return metric;
		}
		
		@Override
		public TimeSeries select(IMonitoringRepository repository, Metric<D> metric, Unit<D> unit, ModelEntity entity, Quantity<Time> start,
				Quantity<Time> end) {
			TimeSeries series = repository.select(metric, unit, entity);
			return series.subset(start.getValue(Time.SECONDS), end.getValue(Time.SECONDS));
		}

		@Override
		public double aggregate(IMonitoringRepository repository, Metric<D> metric, Unit<D> unit, ModelEntity entity, Quantity<Time> start,
				Quantity<Time> end, Aggregation func) {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean contains(IMonitoringRepository repository, Metric<D> metric, ModelEntity entity,
				Quantity<Time> aggregationInterval) {
			return repository.contains(metric, entity, aggregationInterval, false);
		}
		
	}
	
	private Map<Metric<?>, IMetricHandler<?>> metricHandlers = new HashMap<Metric<?>, IMetricHandler<?>>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public  TimeSeries select(Metric<? extends Dimension> metric, Unit<? extends Dimension> unit, ModelEntity entity, Quantity<Time> start, Quantity<Time> end) {
		return getMetricHandler(metric, unit).select(this, (Metric)metric, (Unit)unit, entity, start, end);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public double select(Metric<? extends Dimension> metric, Unit<? extends Dimension> unit, ModelEntity entity, Quantity<Time> start, Quantity<Time> end, Aggregation func) {
		return getMetricHandler(metric, unit).aggregate(this, (Metric)metric, (Unit)unit, entity, start, end, func);
	}
	
	@Override
	public boolean contains(Metric<? extends Dimension> metric, ModelEntity entity, Quantity<Time> maximumAggregationInterval) {
		return contains(metric, entity, maximumAggregationInterval, true);
	}
	
	private void checkTypes(IMetricHandler<?> handler, Metric<? extends Dimension> metric, Unit<? extends Dimension> unit) {
		if (!handler.getMetric().equals(metric)) {
			throw new AssertionError("Incompatible metric handler.");
		}
		if (unit != null) {
			if (!metric.getDimension().equals(unit.getDimension())) {
				throw new IllegalArgumentException("Incompatible dimensions.");
			}
		}
	}

	@SuppressWarnings("unchecked") // the registry should ensure that always compatible pairs of metrics+ handlers are registered.
	protected IMetricHandler<?> getMetricHandler(Metric<?> metric, Unit<?> unit) {
		IMetricHandler<?> handler = metricHandlers.get(metric);
		if (handler == null) {
			handler = Registry.INSTANCE.getMetricHandler(metric);
			if (handler == null) {
				throw new IllegalArgumentException("Unsupported metric: " + metric.getName());
			}
			metricHandlers.put(metric, handler);
		}
		checkTypes(handler, metric, unit);
		return handler;
	}
}
