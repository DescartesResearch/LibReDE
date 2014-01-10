package edu.kit.ipd.descartes.librede.estimation.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.ipd.descartes.librede.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;

/**
 * This class implements the IMonitoringRepository
 * 
 * @author Mehran Saliminia
 * 
 */
public class MemoryObservationRepository implements IMonitoringRepository {
	
	private static class DataKey {
		
		public final IMetric metric;
		public final IModelEntity entity;
		
		public DataKey(IMetric metric, IModelEntity entity) {
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
			DataKey other = (DataKey) obj;
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
	
	private static class DataEntry {
		public TimeSeries data;
		public double aggregationInterval = 0;
	}
	
	private Map<DataKey, DataEntry> data = new HashMap<DataKey, DataEntry>();
	private WorkloadDescription workload;	
	
	public MemoryObservationRepository(WorkloadDescription workload) {
		this.workload = workload;
	}
	
	public double getAggregationInterval(IMetric m, IModelEntity entity) {
		DataKey key = new DataKey(m, entity);
		DataEntry entry = data.get(key);
		if (entry == null) {
			return -1;
		}
		return entry.aggregationInterval;
	}
	
	public TimeSeries getData(IMetric m, IModelEntity entity) {
		DataKey key = new DataKey(m, entity);
		DataEntry entry = data.get(key);
		if (entry == null) {
			return TimeSeries.EMPTY;
		}
		return entry.data;
	}
	
	public void setData(IMetric m, IModelEntity entity, TimeSeries observations) {
		this.setData(m, entity, observations, 0);
	}
	
	public void setAggregatedData(IMetric m, IModelEntity entity, TimeSeries aggregatedObservations) {
		this.setData(m, entity, aggregatedObservations, aggregatedObservations.getAverageTimeIncrement());
	}
	
	public void setAggregatedData(IMetric m, IModelEntity entity, TimeSeries aggregatedObservations, double aggregationInterval) {
		this.setData(m, entity, aggregatedObservations, aggregationInterval);
	}
	
	private void setData(IMetric m, IModelEntity entity, TimeSeries observations, double aggregationInterval) {
		DataKey key = new DataKey(m, entity);
		DataEntry entry = data.get(key);
		if (entry == null) {
			entry = new DataEntry();
			data.put(key, entry);
		}
		entry.data = observations;
		entry.aggregationInterval = aggregationInterval;
	}
	
	@Override
	public boolean containsData(IMetric m,
			IModelEntity entity, double maximumAggregationInterval) {
		DataKey key = new DataKey(m, entity);
		DataEntry entry = data.get(key);
		if (entry == null) {
			return false;
		}
		return entry.aggregationInterval <= maximumAggregationInterval;
	}
	
	@Override
	public List<Resource> listResources() {
		return workload.getResources();
	}
	
	@Override
	public List<Service> listServices() {
		return workload.getServices();
	}
	
	@Override
	public RepositoryCursor getCursor(double startTime, double stepSize) {
		return new RepositoryCursor(this, startTime, stepSize);
	}
}
