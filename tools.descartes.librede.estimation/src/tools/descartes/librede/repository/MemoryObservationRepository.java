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
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;

/**
 * This class implements the IMonitoringRepository
 * 
 * @author Mehran Saliminia
 * 
 */
public class MemoryObservationRepository extends AbstractMonitoringRepository {
	
	private static final Quantity<Time> ZERO_SECONDS = UnitsFactory.eINSTANCE.createQuantity(0.0, Time.SECONDS);
	
	private static class DataKey {
		
		public final Metric<?> metric;
		public final ModelEntity entity;
		
		public DataKey(Metric<?> metric, ModelEntity entity) {
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
		public double aggregationIntervalInSeconds = 0;
	}
	
	private Map<DataKey, DataEntry> data = new HashMap<DataKey, DataEntry>();
	private WorkloadDescription workload;
	private Quantity<Time> currentTime;
	
	public MemoryObservationRepository(WorkloadDescription workload) {
		this.workload = workload;
	}
	
	public boolean isAggregated(Metric<? extends Dimension> m, ModelEntity entity) {
		DataKey key = new DataKey(m, entity);
		DataEntry entry = data.get(key);
		if (entry == null) {
			return false;
		}
		return (entry.aggregationIntervalInSeconds > 0);
	}
	
	public TimeSeries select(Metric<? extends Dimension> m, Unit<? extends Dimension> unit, ModelEntity entity) {
		DataKey key = new DataKey(m, entity);
		DataEntry entry = data.get(key);
		if (entry == null) {
			return TimeSeries.EMPTY;
		}
		return UnitConverter.convertTo(entry.data, m.getDimension().getBaseUnit(), unit);
	}
	
	public void insert(Metric<? extends Dimension> m, Unit<? extends Dimension> unit, ModelEntity entity, TimeSeries observations) {
		this.setData(m, unit, entity, observations, ZERO_SECONDS);
	}
	
	public void insert(Metric<? extends Dimension> m, Unit<? extends Dimension> unit, ModelEntity entity, TimeSeries aggregatedObservations, Quantity<Time> aggregationInterval) {
		this.setData(m, unit, entity, aggregatedObservations, aggregationInterval);
	}
	
	private void setData(Metric<? extends Dimension> m, Unit<? extends Dimension> unit, ModelEntity entity, TimeSeries observations, Quantity<Time> aggregationInterval) {
		DataKey key = new DataKey(m, entity);
		DataEntry entry = data.get(key);
		if (entry == null) {
			entry = new DataEntry();
			data.put(key, entry);
		}
		entry.data = UnitConverter.convertTo(observations, unit, m.getDimension().getBaseUnit());
		entry.aggregationIntervalInSeconds = aggregationInterval.getUnit().convertTo(aggregationInterval.getValue(), Time.SECONDS);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean contains(Metric<? extends Dimension> m,
			ModelEntity entity, Quantity<Time> maximumAggregationInterval, boolean includeDerived) {		
		if (includeDerived) {
			return getMetricHandler(m, null).contains(this, (Metric)m, entity, maximumAggregationInterval);
		} else {
			double requestedIntervalInSeconds = maximumAggregationInterval.getUnit().convertTo(maximumAggregationInterval.getValue(), Time.SECONDS);
			DataKey key = new DataKey(m, entity);
			DataEntry entry = data.get(key);
			if (entry == null) {
				return false;
			}
			return entry.aggregationIntervalInSeconds <= requestedIntervalInSeconds;
		}
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
	public IRepositoryCursor getCursor(Quantity<Time> startTime, Quantity<Time> stepSize) {
		return new AggregationRepositoryCursor(this, startTime, stepSize);
	}
	
	@Override
	public Quantity<Time> getCurrentTime() {
		return currentTime;
	}
	
	@Override
	public void setCurrentTime(Quantity<Time> currentTime) {
		this.currentTime = currentTime;
	}
	
	@Override
	public WorkloadDescription getWorkload()  {
		return workload;
	}
}
