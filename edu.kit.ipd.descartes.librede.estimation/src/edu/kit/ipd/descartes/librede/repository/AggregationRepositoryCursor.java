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
package edu.kit.ipd.descartes.librede.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.descartesresearch.librede.configuration.ModelEntity;


public class AggregationRepositoryCursor implements IRepositoryCursor {
	
	private static class SeriesCacheKey {
		
		public final IMetric metric;
		public final ModelEntity entity;
		
		public SeriesCacheKey(IMetric metric, ModelEntity entity) {
			this.metric = metric;
			this.entity = entity;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((entity == null) ? 0 : entity.hashCode());
			result = prime * result
					+ ((metric == null) ? 0 : metric.hashCode());
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
			return true;
		}		
	}
	
	private static class AggregationCacheKey {
		
		public final IMetric metric;
		public final ModelEntity entity;
		public final Aggregation aggregation;
		
		public AggregationCacheKey(IMetric metric, ModelEntity entity, Aggregation aggregation) {
			this.metric = metric;
			this.entity = entity;
			this.aggregation = aggregation;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((aggregation == null) ? 0 : aggregation.hashCode());
			result = prime * result
					+ ((entity == null) ? 0 : entity.hashCode());
			result = prime * result
					+ ((metric == null) ? 0 : metric.hashCode());
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
			return true;
		}		
	
	}
	
	private IMonitoringRepository repository;
	private double currentTime;
	private double startTime;
	private double stepSize;
	private Map<SeriesCacheKey, TimeSeries> seriesCache = new HashMap<SeriesCacheKey, TimeSeries>();
	private Map<AggregationCacheKey, Double> aggregationCache = new HashMap<AggregationCacheKey, Double>();
	
	public AggregationRepositoryCursor(IMonitoringRepository repository, double startTime, double stepSize) {
		this.repository = repository;
		this.stepSize = stepSize;
		this.startTime = startTime;
		this.currentTime = startTime;
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#next()
	 */
	@Override
	public boolean next() {
		if (currentTime + stepSize <= repository.getCurrentTime()) {
			currentTime += stepSize;
			seriesCache.clear();
			aggregationCache.clear();
			return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#seek(int)
	 */
	@Override
	public boolean seek(int interval) {
		double newCurrentTime = startTime + stepSize * interval;
		if (newCurrentTime <= repository.getCurrentTime()) {
			currentTime = newCurrentTime;
			seriesCache.clear();
			aggregationCache.clear();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#seek(int)
	 */
	@Override
	public boolean seek(double newCurrentTime) {
		if (newCurrentTime <= repository.getCurrentTime()) {
			currentTime = newCurrentTime;
			seriesCache.clear();
			aggregationCache.clear();
			return true;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#getCurrentIntervalStart()
	 */
	@Override
	public double getCurrentIntervalStart() {
		return currentTime - stepSize;
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#getCurrentIntervalLength()
	 */
	@Override
	public double getCurrentIntervalLength() {
		return stepSize;
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#getCurrentIntervalEnd()
	 */
	@Override
	public double getCurrentIntervalEnd() {
		return currentTime;
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#getValues(edu.kit.ipd.descartes.librede.repository.IMetric, edu.kit.ipd.descartes.librede.workload.IModelEntity)
	 */
	@Override
	public TimeSeries getValues(IMetric metric, ModelEntity entity) {
		SeriesCacheKey key = new SeriesCacheKey(metric, entity);
		if (!seriesCache.containsKey(key)) {
			TimeSeries series = metric.retrieve(repository, entity, getCurrentIntervalStart(), getCurrentIntervalEnd());
			seriesCache.put(key, series);
			return series;
		}
		return seriesCache.get(key);
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#getAggregatedValue(edu.kit.ipd.descartes.librede.repository.IMetric, edu.kit.ipd.descartes.librede.workload.IModelEntity, edu.kit.ipd.descartes.librede.repository.Aggregation)
	 */
	@Override
	public double getAggregatedValue(IMetric metric, ModelEntity entity, Aggregation func) {
		AggregationCacheKey key = new AggregationCacheKey(metric, entity, func);
		if (!aggregationCache.containsKey(key)) {
			double value = metric.aggregate(repository, entity, getCurrentIntervalStart(), getCurrentIntervalEnd(), func);
			aggregationCache.put(key, value);
			return value;
		}		
		return aggregationCache.get(key);
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#getRepository()
	 */
	@Override
	public IMonitoringRepository getRepository() {
		return repository;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.repository.IRepositoryCursor#hasData(edu.kit.ipd.descartes.librede.repository.IMetric, java.util.List, edu.kit.ipd.descartes.librede.repository.Aggregation)
	 */
	@Override
	public boolean hasData(IMetric metric, List<ModelEntity> entities,
			Aggregation aggregation) {
		if (metric.isAggregationSupported(aggregation)) {
			boolean data = true;
			for (ModelEntity e : entities) {
				if (aggregation == Aggregation.NONE) {
					data = data && metric.hasData(repository, e, 0.0);
				} else {
					data = data && metric.hasData(repository, e, stepSize);
				}
			}
			return data;
		}
		return false;
	}
	
	@Override
	public int getAvailableIntervals() {
		return (int) ((repository.getCurrentTime() - startTime) / stepSize);
	}
}
