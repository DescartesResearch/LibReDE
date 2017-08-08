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
package tools.descartes.librede.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

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
		private final boolean[] loaded;
		public int lastIndex;
		private final int maxSize;
		
		public CacheEntry(int size) {
			maxSize = size;
			values = new double[size];
			loaded = new boolean[size];
			lastIndex = -1;
		}
		
		public boolean contains(int index) {
			return loaded[index % maxSize];
		}
		
		public double get(int index) {
			return values[index % maxSize];
		}
		
		public void set(int index, double value) {
			values[index % maxSize] = value;
			loaded[index % maxSize] = true;
		}
		
		public void moveTo(int index) {
			for (int i = lastIndex + 1; i <= index; i++) {
				loaded[i % maxSize] = false;
			}
			lastIndex = index;
		}
	}
	
	private static final Logger log = Logger.getLogger(CachingRepositoryCursor.class);
	
	private final IRepositoryCursor delegate;
	private final int cacheSize;
	private int lastInterval = -1;
	private final Map<CacheKey<?>, CacheEntry> aggregationCache = new HashMap<CacheKey<?>, CacheEntry>();
	
	public CachingRepositoryCursor(IRepositoryCursor delegate, int cacheSize) {
		this.delegate = delegate;
		this.cacheSize = cacheSize;
	}

	@Override
	public boolean next() {
		if (delegate.next()) {
			lastInterval = delegate.getLastInterval();
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
		if (interval > lastInterval) {
			throw new IllegalArgumentException();
		}
		
		// interval is in the cached range?
		if (interval > (lastInterval - cacheSize)) {
			CacheEntry entry = getCacheEntry(metric, unit, entity, func);
			// important update cache and invalidate old entries			
			entry.moveTo(lastInterval);
			if (!entry.contains(interval)) {
				double value = delegate.getAggregatedValue(interval, metric, unit, entity, func);
				if(Double.isNaN(value)){
					System.out.println("We got not values for this combination. Fix the start timestamp perhaps...");
				}
				entry.set(interval, value);
				if (log.isTraceEnabled()) {
					log.trace("Set cache entry (interval=" + interval + ", metric=" + metric.getName() + ", unit=" + unit.toString() + ", entity=" + entity.getName() + ", func=" + func.getName() + ", value=" + value + ")");
				}
				return value;
			}
			double value = entry.get(interval);
			if (log.isTraceEnabled()) {
				log.trace("Get cache entry (interval=" + interval + ", metric=" + metric.getName() + ", unit=" + unit.toString() + ", entity=" + entity.getName() + ", func=" + func.getName() + ", value=" + value + ")");
			}
			return value;
		} else {
			return delegate.getAggregatedValue(interval, metric, unit, entity, func);
		}
	}

	@Override
	public IMonitoringRepository getRepository() {
		return delegate.getRepository();
	}

	@Override
	public <D extends Dimension> boolean hasData(int interval, Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		return delegate.hasData(interval , metric, entity, aggregation);
	}

	private <D extends Dimension> CacheEntry getCacheEntry(Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation func) {
		CacheKey<?> key = new CacheKey<D>(metric, unit, entity, func);
		CacheEntry entry = aggregationCache.get(key);
		if (entry == null) {
			entry = new CacheEntry(cacheSize);
			aggregationCache.put(key, entry);
		}
		return entry;
	}

	@Override
	public int getLastInterval() {
		return lastInterval;
	}
	
}
