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

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;


public class AggregationRepositoryCursor implements IRepositoryCursor {
	
	private static final Quantity ZERO_SECONDS = UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS);
	
	private static class SeriesCacheKey {
		
		public final Metric metric;
		public final Unit unit;
		public final ModelEntity entity;
		
		public SeriesCacheKey(Metric metric, Unit unit, ModelEntity entity) {
			this.metric = metric;
			this.unit = unit;
			this.entity = entity;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
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
			SeriesCacheKey other = (SeriesCacheKey) obj;
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
	
	private static class AggregationCacheKey {
		
		public final Metric metric;
		public final Unit unit;
		public final ModelEntity entity;
		public final Aggregation aggregation;
		
		public AggregationCacheKey(Metric metric, Unit unit, ModelEntity entity, Aggregation aggregation) {
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
			AggregationCacheKey other = (AggregationCacheKey) obj;
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
	
	private IMonitoringRepository repository;
	private Quantity currentTime;
	private Quantity startTime;
	private Quantity stepSize;
	private Map<SeriesCacheKey, TimeSeries> seriesCache = new HashMap<SeriesCacheKey, TimeSeries>();
	private Map<AggregationCacheKey, Double> aggregationCache = new HashMap<AggregationCacheKey, Double>();
	
	public AggregationRepositoryCursor(IMonitoringRepository repository, Quantity startTime, Quantity stepSize) {
		this.repository = repository;
		this.stepSize = stepSize;
		this.startTime = startTime;
		this.currentTime = startTime;
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#next()
	 */
	@Override
	public boolean next() {
		Quantity newTime = currentTime.plus(stepSize);
		if (newTime.compareTo(repository.getCurrentTime()) <= 0) {
			currentTime = newTime;
			seriesCache.clear();
			aggregationCache.clear();
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#seek(int)
	 */
	@Override
	public boolean seek(int interval) {
		Quantity newCurrentTime = startTime.plus(stepSize.times(interval));
		if (newCurrentTime.compareTo(repository.getCurrentTime()) <= 0) {
			currentTime = newCurrentTime;
			seriesCache.clear();
			aggregationCache.clear();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#seek(int)
	 */
	@Override
	public boolean seek(Quantity newCurrentTime) {
		if (newCurrentTime.compareTo(repository.getCurrentTime()) <= 0) {
			currentTime = newCurrentTime;
			seriesCache.clear();
			aggregationCache.clear();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getCurrentIntervalStart()
	 */
	@Override
	public Quantity getCurrentIntervalStart() {
		return currentTime.minus(stepSize);
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getCurrentIntervalLength()
	 */
	@Override
	public Quantity getCurrentIntervalLength() {
		return stepSize;
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getCurrentIntervalEnd()
	 */
	@Override
	public Quantity getCurrentIntervalEnd() {
		return currentTime;
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getValues(tools.descartes.librede.metrics.Metric, tools.descartes.librede.configuration.ModelEntity)
	 */
	@Override
	public TimeSeries getValues(Metric metric, Unit unit, ModelEntity entity) {
		SeriesCacheKey key = new SeriesCacheKey(metric, unit, entity);
		if (!seriesCache.containsKey(key)) {
			TimeSeries series = repository.select(metric, unit, entity, getCurrentIntervalStart(), getCurrentIntervalEnd());
			seriesCache.put(key, series);
			return series;
		}
		return seriesCache.get(key);
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getAggregatedValue(tools.descartes.librede.metrics.Metric, tools.descartes.librede.configuration.ModelEntity, tools.descartes.librede.repository.Aggregation)
	 */
	@Override
	public double getAggregatedValue(Metric metric, Unit unit, ModelEntity entity, Aggregation func) {
		AggregationCacheKey key = new AggregationCacheKey(metric, unit, entity, func);
		if (!aggregationCache.containsKey(key)) {
			double value = repository.select(metric, unit, entity, getCurrentIntervalStart(), getCurrentIntervalEnd(), func);
			aggregationCache.put(key, value);
			return value;
		}		
		return aggregationCache.get(key);
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#getRepository()
	 */
	@Override
	public IMonitoringRepository getRepository() {
		return repository;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.repository.IRepositoryCursor#hasData(tools.descartes.librede.metrics.Metric, java.util.List, tools.descartes.librede.repository.Aggregation)
	 */
	@Override
	public boolean hasData(Metric metric, List<ModelEntity> entities,
			Aggregation aggregation) {
		if (metric.isAggregationAllowed(aggregation)) {
			boolean data = true;
			for (ModelEntity e : entities) {
				if (aggregation == Aggregation.NONE) {
					data = data && repository.contains(metric, e, ZERO_SECONDS);
				} else {
					data = data && repository.contains(metric, e, stepSize);
				}
			}
			return data;
		}
		return false;
	}
	
	@Override
	public int getAvailableIntervals() {
		return (int) ((repository.getCurrentTime().minus(startTime).getValue(Time.SECONDS)) / stepSize.getValue(Time.SECONDS));
	}
}
