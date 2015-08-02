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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.rules.AbstractRule;
import tools.descartes.librede.repository.rules.AggregationRule;
import tools.descartes.librede.repository.rules.DerivationRule;
import tools.descartes.librede.repository.rules.RuleDependency;
import tools.descartes.librede.repository.rules.RulesConfig;
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
	
	private class DataEntry<D extends Dimension> {

		private AggregationRule<D> aggregationRule = null;
		private DerivationRule<D> derivationRule = null;
		private TimeSeries data = null;
		private Set<DataEntry<?>> dependentEntries = new HashSet<>();
		private List<DataEntry<?>> requiredEntriesOfDerivation = Collections.emptyList();
		private List<DataEntry<?>> requiredEntriesOfAggregation = Collections.emptyList();
		private Quantity<Time> aggregationInterval;
		private Quantity<Time> startTime;
		private Quantity<Time> endTime;
		
		public void addDependency(DataEntry<?> entry) {
			dependentEntries.add(entry);
		}
		
		public void removeDependency(DataEntry<?> entry) {
			dependentEntries.remove(entry);
		}
		
		public Quantity<Time> getAggregationInterval() {
			return aggregationInterval;
		}
		
		public Quantity<Time> getStartTime() {
			return startTime;
		}
		
		public Quantity<Time> getEndTime() {
			return endTime;
		}
		
		public void setAggregationRule(AggregationRule<D> rule, List<DataEntry<?>> requiredEntries) {
			if (aggregationRule == null || aggregationRule.getPriority() < rule.getPriority()) {
				this.aggregationRule = rule;
				updateRequiredEntries(requiredEntries, requiredEntriesOfDerivation);
				update();
			}
		}
		
		public void setDerivationRule(DerivationRule<D> rule, List<DataEntry<?>> requiredEntries) {
			if (derivationRule == null || derivationRule.getPriority() < rule.getPriority()) {
				this.derivationRule = rule;
				updateRequiredEntries(requiredEntriesOfAggregation, requiredEntries);
				update();
			}
		}
		
		public void setTimeSeries(TimeSeries data, Quantity<Time> aggregationInterval) {
			this.data = data;
			this.aggregationInterval = aggregationInterval;
			this.startTime = UnitsFactory.eINSTANCE.createQuantity(data.getStartTime(), Time.SECONDS);
			this.endTime = UnitsFactory.eINSTANCE.createQuantity(data.getEndTime(), Time.SECONDS);
			update();
		}
		
		public TimeSeries getTimeSeries(MemoryObservationRepository repository, Metric<D> metric, Unit<D> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			if (data != null) {
				return UnitConverter.convertTo(data.subset(start.getValue(Time.SECONDS), end.getValue(Time.SECONDS)), unit.getDimension().getBaseUnit(), unit);
			}
			if (derivationRule != null) {
				return derivationRule.getDerivationHandler().derive(repository, metric, unit, entity, aggregation, start, end);
			}
			return TimeSeries.EMPTY;
		}
		
		private void update() {
			if (data == null) {
				deriveValuesFromDerivedEntries();
			}
			notifyDependentEntries();
		}
		
		private void updateRequiredEntries(List<DataEntry<?>> newReqEntriesAggregation, List<DataEntry<?>> newReqEntriesDerivation) {
			for (DataEntry<?> e : requiredEntriesOfAggregation) {
				e.removeDependency(this);
			}
			for (DataEntry<?> e : requiredEntriesOfDerivation) {
				e.removeDependency(this);
			}
			for (DataEntry<?> e : newReqEntriesAggregation) {
				e.removeDependency(this);
			}
			for (DataEntry<?> e : newReqEntriesDerivation) {
				e.removeDependency(this);
			}
			this.requiredEntriesOfAggregation = newReqEntriesAggregation;
			this.requiredEntriesOfDerivation = newReqEntriesDerivation;
		}
		
		private void deriveValuesFromDerivedEntries() {
			Quantity<Time> startTime = null;
			Quantity<Time> endTime = null;
			Quantity<Time> aggregationInterval = null;
			List<DataEntry<?>> requiredEntries = (derivationRule == null) ? requiredEntriesOfAggregation : requiredEntriesOfDerivation;
			for (DataEntry<?> reqEntry : requiredEntries) {
				// Find maximum start time
				if (startTime == null) {
					startTime = reqEntry.getStartTime();
				} else {
					if (reqEntry.getStartTime().compareTo(startTime) > 0) {
						startTime = reqEntry.getStartTime();
					}					
				}
				// Find minimum end time
				if (endTime == null) {
					endTime = reqEntry.getEndTime();
				} else {
					if (reqEntry.getEndTime().compareTo(endTime) < 0) {
						endTime = reqEntry.getEndTime();
					}
				}
				// Find maximum aggregation interval
				if (aggregationInterval == null) {
					aggregationInterval = reqEntry.getAggregationInterval();
				} else {
					if (reqEntry.getAggregationInterval().compareTo(aggregationInterval) > 0) {
						aggregationInterval = reqEntry.getAggregationInterval();
					}
				}
			}
			this.startTime = startTime;
			this.endTime = endTime;
			this.aggregationInterval = aggregationInterval;
 		}
		
		private void notifyDependentEntries() {
			for (DataEntry<?> entry : dependentEntries) {
				entry.update();
			}
		}		
	}
	
	private Map<DataKey<?>, DataEntry<?>> data = new HashMap<>();
	private final RulesConfig rules = new RulesConfig();
	private WorkloadDescription workload;
	private Quantity<Time> currentTime;
	
	public MemoryObservationRepository(WorkloadDescription workload) {
		this.workload = workload;
		for (Metric<?> m : Registry.INSTANCE.getMetrics()) {
			registerRules(m);			
		}
		registerDefaultHandlers();
	}
	
	public <D extends Dimension> void insert(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries observations) {
		this.setData(m, unit, entity, observations, Aggregation.NONE, ZERO_SECONDS);
	}
	
	public <D extends Dimension> void insert(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries aggregatedObservations, Aggregation aggregation, Quantity<Time> aggregationInterval) {
		this.setData(m, unit, entity, aggregatedObservations, aggregation, aggregationInterval);
	}
	
	private <D extends Dimension> void setData(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries observations, Aggregation aggregation, Quantity<Time> aggregationInterval) {
		DataKey<D> key = new DataKey<D>(m, entity, aggregation);
		DataEntry<D> entry = getEntry(key);
		boolean existing = (entry != null);
		if (!existing) {
			entry = new DataEntry<>();
		}
		entry.setTimeSeries(UnitConverter.convertTo(observations, unit, m.getDimension().getBaseUnit()), aggregationInterval);
		if (!existing) {
			addEntry(key, entry);
		}
	
		log.info("Data" + (existing ? "" : "(replaced)") + ": " + entity.getName() 
			+ ":" + m.getName() 
			+ ":" + aggregation.getLiteral() 
			+ " <- ["
			+ "length=" + observations.samples() + ", "
			+ "mean=" + LinAlg.nanmean(observations.getData(0)).get(0) + ", "
			+ "start=" + observations.getStartTime() +"s, "
			+ "end=" + observations.getEndTime() + "s]");
	}
	
	@SuppressWarnings("unchecked")
	private <D extends Dimension> void addEntry(DataKey<D> key, DataEntry<D> newEntry) {
		data.put(key,  newEntry);
		// This is the first time we add data for this metric, entity and aggregation combination
		notifyNewEntry(key.metric, key.entity, key.aggregation);
	}
	
	private <D extends Dimension> void notifyNewEntry(Metric<D> metric, ModelEntity entity, Aggregation aggregation) {
		for (AggregationRule<?> r : rules.getAggregationRules(metric, aggregation)) {
			List<ModelEntity> entities = r.getNotificationSet(entity);
			for (ModelEntity e : entities) {
				if (r.applies(e)) {
					if (checkDependencies(r, e)) {
						addAggregation(r, e);
					}
				}
			}
		}
		for (DerivationRule<?> r : rules.getDerivationRules(metric, aggregation)) {
			List<ModelEntity> entities = r.getNotificationSet(entity);
			for (ModelEntity e : entities) {
				if (r.applies(e)) {
					if (checkDependencies(r, e)) {
						addDerivation(r, e);
					}
				}
			}
		}
	}
	
	private boolean checkDependencies(AbstractRule<?> rule, ModelEntity entity) {
		List<ModelEntity> scopeEntities = rule.getScopeSet(entity);
		for (ModelEntity e : scopeEntities) {
			for (RuleDependency<?> dep : rule.getDependencies()) {
				if (!exists(dep.getMetric(), e, dep.getAggregation())) {
					return false;
				}
			}
		}
		return true;
	}
	
	private List<DataEntry<?>> getRequiredEntries(AbstractRule<?> rule, ModelEntity entity) {
		List<DataEntry<?>> requiredEntries = new LinkedList<>();
		List<ModelEntity> scopeEntities = rule.getScopeSet(entity);
		for (ModelEntity e : scopeEntities) {
			for (RuleDependency<?> dep : rule.getDependencies()) {
				DataEntry<?> reqEntry = getEntry(dep.getMetric(), e, dep.getAggregation());
				if (reqEntry != null) {
					requiredEntries.add(reqEntry);
				}
			}
		}
		return requiredEntries;
	}
	
	private <D extends Dimension> void addAggregation(AggregationRule<D> t, ModelEntity entity) {
		Metric<D> metric = t.getMetric();
		Aggregation aggregation = t.getAggregation();
		IMetricAggregationHandler<D> handler = t.getAggregationHandler();

		DataKey<D> key = new DataKey<>(metric, entity, aggregation);
		DataEntry<D> entry = getEntry(key);
		boolean newEntry = (entry == null);		
		if (newEntry) {
			entry = new DataEntry<D>();
		}
		entry.setAggregationRule(t, getRequiredEntries(t, entity));
		if (newEntry) {
			addEntry(key, entry);
		}
		log.info("Aggregation" + (newEntry ? "" : " (replaced)") + ": " + entity.getName() + ":" + metric.getName() + ":" + aggregation.getLiteral() + " <- " + handler.toString());
	}
	
	private <D extends Dimension> void addDerivation(DerivationRule<D> t, ModelEntity entity) {
		Metric<D> metric = t.getMetric();
		Aggregation aggregation = t.getAggregation();
		IMetricDerivationHandler<D> handler = t.getDerivationHandler();

		DataKey<D> key = new DataKey<>(metric, entity, aggregation);
		DataEntry<D> entry = getEntry(key);
		boolean newEntry = (entry == null);		
		if (newEntry) {
			entry = new DataEntry<D>();
		}
		entry.setDerivationRule(t, getRequiredEntries(t, entity));
		if (newEntry) {
			addEntry(key, entry);
		}
		log.info("Derivation" + (newEntry ? "" : " (replaced)") + ": " + entity.getName() + ":" + metric.getName() + ":" + aggregation.getLiteral() + " <- " + handler.toString());
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
		if (entry.aggregationRule == null) {
			throw new IllegalStateException("No aggregation handler for " + metric.getName() + " and " + aggregation.getLiteral() + " is available.");
		}
		return entry.aggregationRule.getAggregationHandler().aggregate(this, metric, unit, entity, aggregation, start, end);
	}

	@Override
	public <D extends Dimension> boolean exists(Metric<D> metric, ModelEntity entity, Aggregation aggregation) {
		DataKey<D> key = new DataKey<>(metric, entity, aggregation);
		return data.containsKey(key);
	}

	@Override
	public <D extends Dimension> Quantity<Time> getAggregationInterval(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		return (entry == null) ? ZERO_SECONDS : entry.getAggregationInterval();
	}
	
	@Override
	public <D extends Dimension> Quantity<Time> getMonitoringStartTime(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		return (entry == null) ? ZERO_SECONDS : entry.getStartTime();
	}
	
	@Override
	public <D extends Dimension> Quantity<Time> getMonitoringEndTime(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		return (entry == null) ? ZERO_SECONDS : entry.getEndTime();
	}
	
	private <D extends Dimension> DataEntry<D> getEntry(Metric<D> metric, ModelEntity entity, Aggregation aggregation) {
		return getEntry(new DataKey<D>(metric, entity, aggregation));
	}
	
	@SuppressWarnings("unchecked")
	private <D extends Dimension> DataEntry<D> getEntry(DataKey<D> key) {
		return (DataEntry<D>)data.get(key);
	}
	
	private <D extends Dimension> void registerRules(Metric<D> m) {
		List<AggregationRule<D>> aggregationRules = Registry.INSTANCE.getMetricHandler(m).getAggregationRules();
		for (AggregationRule<D> r : aggregationRules) {
			rules.addAggregationRule(r);
		}
		List<DerivationRule<D>> derivationRules = Registry.INSTANCE.getMetricHandler(m).getDerivationRules();
		for (DerivationRule<D> r : derivationRules) {
			rules.addDerivationRule(r);
		}
	}
	
	private void registerDefaultHandlers() {
		if (log.isDebugEnabled()) {
			log.debug("Register default handlers for metrics");
		}
		for (AggregationRule<?> rule : rules.getDefaultDerivationRules()) {
			for (Resource resource : workload.getResources()) {
				if (rule.applies(resource)) {
					addAggregation(rule, resource);
				}
			}
			for (Service service : workload.getServices()) {
				if (rule.applies(service)) {
					addAggregation(rule, service);
				}
			}
		}
	}
}
