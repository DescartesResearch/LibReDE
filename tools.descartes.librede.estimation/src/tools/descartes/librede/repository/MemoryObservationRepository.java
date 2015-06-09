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
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;

/**
 * This class implements the IMonitoringRepository
 * 
 * @author Mehran Saliminia, Simon Spinner
 * 
 */
public class MemoryObservationRepository implements IMonitoringRepository {
	
	private static final Quantity<Time> ZERO_SECONDS = UnitsFactory.eINSTANCE.createQuantity(0.0, Time.SECONDS);
	
	private static final Quantity<Time> NaN = UnitsFactory.eINSTANCE.createQuantity(Double.NaN, Time.SECONDS);	
	
	private static final Logger log = Logger.getLogger(MemoryObservationRepository.class);
	
	private static class DataKey<D extends Dimension> {
		
		public final Metric<D> metric;
		public final ModelEntity entity;
		public final Aggregation aggregation;
		
		public DataKey(Metric<D> metric, ModelEntity entity, Aggregation aggregation) {
			this.metric = metric;
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
			DataKey<?> other = (DataKey<?>) obj;
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
	
	private abstract static class DataEntry<D extends Dimension> {

		public IMetricAggregationHandler<D> aggregationHandler = null;
		
		public abstract TimeSeries getTimeSeries(MemoryObservationRepository repository, Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end);
		
		public abstract Quantity<Time> getAggregationInterval(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity, Aggregation aggregation);
		
		public abstract Quantity<Time> getStartTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity, Aggregation aggregation);
		
		public abstract Quantity<Time> getEndTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity, Aggregation aggregation);
	}
	
	private static class TimeSeriesDataEntry<D extends Dimension> extends DataEntry<D> {
		private final TimeSeries data;
		private final Quantity<Time> aggregationInterval;
		private final Quantity<Time> startTime;
		private final Quantity<Time> endTime;
		
		public TimeSeriesDataEntry(TimeSeries data, Quantity<Time> aggregationInterval) {
			this.data = data;
			this.aggregationInterval = aggregationInterval;
			this.startTime = UnitsFactory.eINSTANCE.createQuantity(data.getStartTime(), Time.SECONDS);
			this.endTime = UnitsFactory.eINSTANCE.createQuantity(data.getEndTime(), Time.SECONDS);
		}

		@Override
		public TimeSeries getTimeSeries(MemoryObservationRepository repository, Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			return UnitConverter.convertTo(data.subset(start.getValue(Time.SECONDS), end.getValue(Time.SECONDS)), unit.getDimension().getBaseUnit(), unit);
		}

		@Override
		public Quantity<Time> getAggregationInterval(MemoryObservationRepository repository, Metric<D> metric,
				ModelEntity entity, Aggregation aggregation) {
			return aggregationInterval;
		}

		@Override
		public Quantity<Time> getStartTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity,
				Aggregation aggregation) {
			return startTime;
		}

		@Override
		public Quantity<Time> getEndTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity,
				Aggregation aggregation) {
			return endTime;
		}
	}
	
	private static class DerivedDataEntry<D extends Dimension> extends DataEntry<D> {
		public IMetricDerivationHandler<D> derivationHandler = null;

		@Override
		public TimeSeries getTimeSeries(MemoryObservationRepository repository, Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			if (derivationHandler != null) {
				return derivationHandler.derive(repository, metric, unit, entity, aggregation, start, end);
			}
			return TimeSeries.EMPTY;
		}

		@Override
		public Quantity<Time> getAggregationInterval(MemoryObservationRepository repository, Metric<D> metric,
				ModelEntity entity, Aggregation aggregation) {
			if (derivationHandler != null) {
				return derivationHandler.getAggregationInterval(repository, metric, entity, aggregation);
			}
			return aggregationHandler.getAggregationInterval(repository, metric, entity, aggregation);
		}

		@Override
		public Quantity<Time> getStartTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity,
				Aggregation aggregation) {
			if (derivationHandler != null) {
				return derivationHandler.getStartTime(repository, metric, entity, aggregation);
			}
			return aggregationHandler.getStartTime(repository, metric, entity, aggregation);
		}

		@Override
		public Quantity<Time> getEndTime(MemoryObservationRepository repository, Metric<D> metric, ModelEntity entity,
				Aggregation aggregation) {
			if (derivationHandler != null) {
				return derivationHandler.getEndTime(repository, metric, entity, aggregation);
			}
			return aggregationHandler.getEndTime(repository, metric, entity, aggregation);
		}
	}
	
	private Map<DataKey<?>, DataEntry<?>> data = new HashMap<>();
	private WorkloadDescription workload;
	private Quantity<Time> currentTime;
	
	public MemoryObservationRepository(WorkloadDescription workload) {
		this.workload = workload;
		registerDefaultHandlers();
	}
	
	public <D extends Dimension> void insert(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries observations) {
		this.setData(m, unit, entity, observations, Aggregation.NONE, ZERO_SECONDS);
	}
	
	public <D extends Dimension> void insert(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries aggregatedObservations, Aggregation aggregation, Quantity<Time> aggregationInterval) {
		this.setData(m, unit, entity, aggregatedObservations, aggregation, aggregationInterval);
	}
	
	private <D extends Dimension> void setData(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries observations, Aggregation aggregation, Quantity<Time> aggregationInterval) {
		DataEntry<D> oldEntry = getEntry(m, entity, aggregation);
		
		TimeSeriesDataEntry<D> entry = new TimeSeriesDataEntry<>(UnitConverter.convertTo(observations, unit, m.getDimension().getBaseUnit()), aggregationInterval);
		if (oldEntry != null) {
			entry.aggregationHandler = oldEntry.aggregationHandler;
		}
		data.put(new DataKey<D>(m, entity, aggregation),  entry);
		
		log.info("Data" + ((oldEntry== null) ? "" : "(replaced)") + ": " + entity.getName() 
			+ ":" + m.getName() 
			+ ":" + aggregation.getLiteral() 
			+ " <- ["
			+ "length=" + observations.samples() + ", "
			+ "mean=" + LinAlg.nanmean(observations.getData(0)).get(0) + ", "
			+ "start=" + observations.getStartTime() +"s, "
			+ "end=" + observations.getEndTime() + "s]");
		
		if (oldEntry == null) {
			// This is the first time we add data for this metric, entity and aggregation combination
			// Install all aggregation and derivation handlers
			Registry.INSTANCE.getMetricHandler(m).registerHandlers(this, entity, aggregation);
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
	
	@Override
	public <D extends Dimension> TimeSeries select(Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation aggregation) {
		return select(metric, unit, entity, aggregation, NaN, NaN);
	}

	@Override
	public <D extends Dimension> TimeSeries select(Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		if (entry == null) {
			return TimeSeries.EMPTY;
		}
		return entry.getTimeSeries(this, metric, unit, entity, aggregation, start, end);
	}

	@Override
	public <D extends Dimension> double aggregate(Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		if (entry == null) {
			return Double.NaN;
		}
		if (entry.aggregationHandler == null) {
			throw new IllegalStateException("No aggregation handler for " + metric.getName() + " and " + aggregation.getLiteral() + " is available.");
		}
		return entry.aggregationHandler.aggregate(this, metric, unit, entity, aggregation, start, end);
	}

	@Override
	public <D extends Dimension> boolean exists(Metric<D> metric, ModelEntity entity, Aggregation aggregation) {
		DataKey<D> key = new DataKey<>(metric, entity, aggregation);
		return data.containsKey(key);
	}

	@Override
	public <D extends Dimension> void addMetricDerivationHandler(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation, IMetricDerivationHandler<D> handler) {
		DataEntry<D> oldEntry = getEntry(metric, entity, aggregation);
		
		DerivedDataEntry<D> entry = new DerivedDataEntry<>();
		entry.derivationHandler = handler;
		if (oldEntry != null) {
			entry.aggregationHandler = oldEntry.aggregationHandler;
		}
		data.put(new DataKey<D>(metric, entity, aggregation),  entry);
		
		log.info("Derivation" + ((oldEntry == null) ? "" : " (replaced)") + ": " + entity.getName() + ":" + metric.getName() + ":" + aggregation.getLiteral() + " <- " + handler.toString());
		
		if (oldEntry == null) {
			// This is the first time we add data for this metric, entity and aggregation combination
			// Install all aggregation and derivation handlers
			Registry.INSTANCE.getMetricHandler(metric).registerHandlers(this, entity, aggregation);
		}
		
	}

	@Override
	public <D extends Dimension> void addMetricAggregationHandler(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation, IMetricAggregationHandler<D> handler) {
		log.info("Aggregation: " + entity.getName() + ":" + metric.getName() + ":" + aggregation.getLiteral() + " <- " + handler.toString());
		
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		boolean newEntry = (entry == null);
		
		if (newEntry) {
			entry = new DerivedDataEntry<D>();
			data.put(new DataKey<D>(metric, entity, aggregation), entry);
		}
		if (entry.aggregationHandler == null) {
			entry.aggregationHandler = handler;
		}
		
		log.info("Aggregation" + (newEntry ? "" : " (replaced)") + ": " + entity.getName() + ":" + metric.getName() + ":" + aggregation.getLiteral() + " <- " + handler.toString());

		if (newEntry) {
			// This is the first time we add data for this metric, entity and aggregation combination
			// Install all aggregation and derivation handlers
			Registry.INSTANCE.getMetricHandler(metric).registerHandlers(this, entity, aggregation);
		}		
	}
	
	@Override
	public <D extends Dimension> Quantity<Time> getAggregationInterval(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		return (entry == null) ? ZERO_SECONDS : entry.getAggregationInterval(this, metric, entity, aggregation);
	}
	
	@Override
	public <D extends Dimension> Quantity<Time> getMonitoringStartTime(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		return (entry == null) ? ZERO_SECONDS : entry.getStartTime(this, metric, entity, aggregation);
	}
	
	@Override
	public <D extends Dimension> Quantity<Time> getMonitoringEndTime(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		return (entry == null) ? ZERO_SECONDS : entry.getEndTime(this, metric, entity, aggregation);
	}
	
	@SuppressWarnings("unchecked")
	private <D extends Dimension> DataEntry<D> getEntry(Metric<D> metric, ModelEntity entity, Aggregation aggregation) {
		DataKey<D> key = new DataKey<>(metric, entity, aggregation);
		return (DataEntry<D>)data.get(key);
	}
	
	private void registerDefaultHandlers() {
		if (log.isDebugEnabled()) {
			log.debug("Register default handlers for metrics");
		}
		
		for (Metric<?> metric : Registry.INSTANCE.getMetrics()) {
			for (Resource resource : workload.getResources()) {
				Registry.INSTANCE.getMetricHandler(metric).registerDefaultHandlers(this, resource);
			}
			for (Service service : workload.getServices()) {
				Registry.INSTANCE.getMetricHandler(metric).registerDefaultHandlers(this, service);
			}
		}
	}
}
