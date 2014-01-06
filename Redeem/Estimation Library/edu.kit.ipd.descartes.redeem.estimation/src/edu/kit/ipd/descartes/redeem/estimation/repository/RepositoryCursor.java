package edu.kit.ipd.descartes.redeem.estimation.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.ipd.descartes.redeem.estimation.workload.IModelEntity;


public class RepositoryCursor {
	
	private static class SeriesCacheKey {
		
		public final IMetric metric;
		public final IModelEntity entity;
		
		public SeriesCacheKey(IMetric metric, IModelEntity entity) {
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
		public final IModelEntity entity;
		public final Aggregation aggregation;
		
		public AggregationCacheKey(IMetric metric, IModelEntity entity, Aggregation aggregation) {
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
	private double endTime;
	private double stepSize;
	private Map<SeriesCacheKey, TimeSeries> seriesCache = new HashMap<SeriesCacheKey, TimeSeries>();
	private Map<AggregationCacheKey, Double> aggregationCache = new HashMap<AggregationCacheKey, Double>();
	
	public RepositoryCursor(IMonitoringRepository repository, double startTime, double stepSize) {
		this.repository = repository;
		this.stepSize = stepSize;
		this.currentTime = startTime;
	}
	
	public boolean next() {
		if (currentTime + stepSize <= endTime) {
			currentTime += stepSize;
			seriesCache.clear();
			aggregationCache.clear();
			return true;
		}
		return false;
	}
	
	public double getCurrentIntervalStart() {
		return currentTime - stepSize;
	}
	
	public double getCurrentIntervalLength() {
		return stepSize;
	}
	
	public double getCurrentIntervalEnd() {
		return currentTime;
	}
	
	public double getEndTime() {
		return endTime;
	}
	
	public void setEndTime(double endTime) {
		this.endTime = endTime;
	}
	
	public TimeSeries getValues(IMetric metric, IModelEntity entity) {
		SeriesCacheKey key = new SeriesCacheKey(metric, entity);
		if (!seriesCache.containsKey(key)) {
			TimeSeries series = metric.retrieve(repository, entity, getCurrentIntervalStart(), getCurrentIntervalEnd());
			seriesCache.put(key, series);
			return series;
		}
		return seriesCache.get(key);
	}
	
	public double getAggregatedValue(IMetric metric, IModelEntity entity, Aggregation func) {
		AggregationCacheKey key = new AggregationCacheKey(metric, entity, func);
		if (!aggregationCache.containsKey(key)) {
			double value = metric.aggregate(repository, entity, getCurrentIntervalStart(), getCurrentIntervalEnd(), func);
			aggregationCache.put(key, value);
			return value;
		}		
		return aggregationCache.get(key);
	}
	
	public IMonitoringRepository getRepository() {
		return repository;
	}

	public boolean hasData(IMetric metric, List<IModelEntity> entities,
			Aggregation aggregation) {
		if (metric.isAggregationSupported(aggregation)) {
			boolean data = true;
			for (IModelEntity e : entities) {
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
}
