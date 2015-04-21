package tools.descartes.librede.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public class CachingRepositoryCursor implements IRepositoryCursor {
	
	private static class CacheKey<D extends Dimension> {
		
		public final Metric<D> metric;
		public final Unit<D> unit;
		public final ModelEntity entity;
		public final Aggregation aggregation;
		
		public CacheKey(Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation aggregation) {
			this.metric = metric;
			this.unit = unit;
			this.entity = entity;
			this.aggregation = aggregation;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((aggregation == null) ? 0 : aggregation.hashCode());
			result = prime * result + ((entity == null) ? 0 : entity.hashCode());
			result = prime * result + ((metric == null) ? 0 : metric.hashCode());
			result = prime * result + ((unit == null) ? 0 : unit.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CacheKey<?> other = (CacheKey<?>) obj;
			if (aggregation != other.aggregation)
				return false;
			if (entity == null) {
				if (other.entity != null)
					return false;
			} else if (!entity.equals(other.entity))
				return false;
			if (metric == null) {
				if (other.metric != null)
					return false;
			} else if (!metric.equals(other.metric))
				return false;
			if (unit == null) {
				if (other.unit != null)
					return false;
			} else if (!unit.equals(other.unit))
				return false;
			return true;
		}		
	
	}
	
	/**
	 * This class implements a circular buffer.
	 *
	 */
	private static class CacheEntry {
		private final double[] values;
		public int lastIndex;
		private final int maxSize;
		
		public CacheEntry(int size) {
			maxSize = size;
			values = new double[size];
			lastIndex = -1;
		}
		
		public double get(int index) {
			if (((lastIndex - maxSize) >= index) || index > lastIndex) {
				// no values available
				return Double.NaN;
			}
			return values[index % maxSize];
		}
		
		public void add(double value) {
			lastIndex++;
			values[lastIndex % maxSize] = value;
		}
	}
	
	private final IRepositoryCursor delegate;
	private final int cacheSize;
	private final Map<CacheKey<?>, CacheEntry> aggregationCache = new HashMap<CacheKey<?>, CacheEntry>();
	
	public CachingRepositoryCursor(IRepositoryCursor delegate, int cacheSize) {
		this.delegate = delegate;
		this.cacheSize = cacheSize;
	}

	@Override
	public boolean next() {
		if (delegate.next()) {
			aggregationCache.clear();
			return true;
		}
		return false;
	}

	@Override
	public void reset() {
		delegate.reset();
		aggregationCache.clear();
	}

	@Override
	public Quantity<Time> getIntervalStart(int interval) {
		return delegate.getIntervalStart(interval);
	}

	@Override
	public Quantity<Time> getIntervalEnd(int interval) {
		return delegate.getIntervalEnd(interval);
	}

	@Override
	public <D extends Dimension> TimeSeries getValues(int interval, Metric<D> metric, Unit<D> unit, ModelEntity entity) {
		return delegate.getValues(interval, metric, unit, entity);
	}

	@Override
	public <D extends Dimension> double getAggregatedValue(int interval, Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation func) {
		CacheEntry entry = getCacheEntry(metric, unit, entity, Aggregation.NONE);
		if (entry.lastIndex < interval) {
			// Load everything up to the requested interval into the cache.
			for (int i = entry.lastIndex + 1; i <= interval; i++) {
				entry.add(delegate.getAggregatedValue(i, metric, unit, entity, func));
			}
		}

		// check that the requested interval is within the cached range
		if (interval > (entry.lastIndex - cacheSize)) {
			return entry.get(interval);
		} else {
			return delegate.getAggregatedValue(interval, metric, unit, entity, func);
		}
	}

	@Override
	public IMonitoringRepository getRepository() {
		return delegate.getRepository();
	}

	@Override
	public <D extends Dimension> boolean hasData(int interval, Metric<D> metric, List<ModelEntity> entities,
			Aggregation aggregation) {
		return delegate.hasData(interval , metric, entities, aggregation);
	}

	private <D extends Dimension> CacheEntry getCacheEntry(Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation func) {
		CacheKey<?> key = new CacheKey<D>(metric, unit, entity, Aggregation.NONE);
		CacheEntry entry = aggregationCache.get(key);
		if (entry == null) {
			entry = new CacheEntry(cacheSize);
		}
		return entry;
	}

	@Override
	public int getLastInterval() {
		return getLastInterval();
	}
	
}
