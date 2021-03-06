/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.eclipse.emf.ecore.util.EcoreUtil;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.Task;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.exceptions.NoMonitoringDataException;
import tools.descartes.librede.repository.exceptions.OutOfMonitoredRangeException;
import tools.descartes.librede.repository.rules.DataDependency;
import tools.descartes.librede.repository.rules.DerivationRule;
import tools.descartes.librede.repository.rules.Rule;
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

	private static final Quantity<Time> MAX_SECONDS = UnitsFactory.eINSTANCE.createQuantity(Double.MAX_VALUE,
			Time.SECONDS);

	private static final Quantity<Time> MIN_SECONDS = UnitsFactory.eINSTANCE.createQuantity(-Double.MAX_VALUE,
			Time.SECONDS);

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

		private final DataKey<D> key;
		private DerivationRule<D> derivationRule = null;
		private IMetricDerivationHandler<D> derivationHandler = null;
		private TimeSeries data = null;
		private Set<DataEntry<?>> dependentEntries = new HashSet<>();
		private List<DataEntry<?>> requiredEntries = Collections.emptyList();
		private Quantity<Time> aggregationInterval;
		private Quantity<Time> startTime;
		private Quantity<Time> endTime;

		public DataEntry(DataKey<D> key) {
			this.key = key;
		}

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

		public DataKey<D> getKey() {
			return key;
		}

		public void setDerivationRule(DerivationRule<D> rule, IMetricDerivationHandler<D> handler,
				List<DataEntry<?>> requiredEntries) {
			if ((rule.getAggregation() == Aggregation.NONE) && (data != null)) {
				// Prevent the registration of a derivation rules for
				// Aggregation.NONE
				// if this entry already contains null. This rule would be
				// redundant and
				// can cause loops in the dependency graph.
				return;
			}
			if (derivationRule == null || derivationRule.getPriority() < rule.getPriority()) {
				this.derivationRule = rule;
				this.derivationHandler = handler;
				updateRequiredEntries(requiredEntries);
				update();
			}
		}

		public void setTimeSeries(TimeSeries data, Quantity<Time> aggregationInterval) {
			if ((derivationRule != null) && (derivationRule.getAggregation() == Aggregation.NONE)) {
				// Special case for Aggregation.NONE --> No derivation rule may
				// be set if
				// time series data is set.
				this.derivationRule = null;
				this.derivationHandler = null;
			}
			this.data = data;
			this.aggregationInterval = aggregationInterval;
			this.startTime = UnitsFactory.eINSTANCE.createQuantity(data.getStartTime(), Time.SECONDS);
			this.endTime = UnitsFactory.eINSTANCE.createQuantity(data.getEndTime(), Time.SECONDS);
			update();
		}

		public TimeSeries getRawData() {
			return data;
		}

		public TimeSeries getTimeSeries(MemoryObservationRepository repository, Metric<D> metric, Unit<D> unit,
				ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
			if (data != null) {
				return UnitConverter.convertTo(data.subset(start.getValue(Time.SECONDS), end.getValue(Time.SECONDS)),
						unit.getDimension().getBaseUnit(), unit);
			}
			if (derivationHandler != null) {
				return derivationHandler.derive(repository, metric, unit, entity, aggregation, start, end);
			}
			return TimeSeries.EMPTY;
		}

		private void update() {
			if (data == null) {
				deriveValuesFromDerivedEntries();
			}
			notifyDependentEntries();
		}

		private void updateRequiredEntries(List<DataEntry<?>> newReqEntriesDerivation) {
			for (DataEntry<?> e : requiredEntries) {
				e.removeDependency(this);
			}
			for (DataEntry<?> e : newReqEntriesDerivation) {
				// The entry may depend on itself (e.g., if aggregation is done
				// on the contained timeseries)
				// To prevent endless recursion on notification, exclude this
				// from dependencies
				if (!e.equals(this)) {
					e.addDependency(this);
				}
			}
			this.requiredEntries = newReqEntriesDerivation;
		}

		private void deriveValuesFromDerivedEntries() {
			// If this entry does not depend on others, we assume it can provide
			// values
			// for all possible time values.
			Quantity<Time> startTime = MIN_SECONDS;
			Quantity<Time> endTime = MAX_SECONDS;
			Quantity<Time> aggregationInterval = ZERO_SECONDS;
			for (DataEntry<?> reqEntry : requiredEntries) {
				// Find maximum start time
				if (reqEntry.getStartTime().compareTo(startTime) > 0) {
					startTime = reqEntry.getStartTime();
				}
				// Find minimum end time
				if (reqEntry.getEndTime().compareTo(endTime) < 0) {
					endTime = reqEntry.getEndTime();
				}
				// Find maximum aggregation interval
				if (reqEntry.getAggregationInterval().compareTo(aggregationInterval) > 0) {
					aggregationInterval = reqEntry.getAggregationInterval();
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

	private final Map<DataKey<?>, DataEntry<?>> data = new HashMap<>();
	private final RulesConfig rules = new RulesConfig();
	private final WorkloadDescription workload;
	private Quantity<Time> currentTime;
	private final Set<IMonitoringRepositoryListener> listeners = new HashSet<>();
	private final Set<ModelEntity> entities = new HashSet<>();

	public MemoryObservationRepository(WorkloadDescription workload) {
		this.workload = workload;
		collectEntities();
		log.info("Set up in-memory observation repository");
		for (Metric<?> m : Registry.INSTANCE.getMetrics()) {
			registerRules(m);
		}
		rules.logConfigDump();
	}

	private void collectEntities() {
		for (Iterator<?> it = EcoreUtil.getAllContents(workload, true); it.hasNext();) {
			Object cur = it.next();
			if (cur instanceof ModelEntity) {
				entities.add((ModelEntity) cur);
			}
		}
	}

	@Override
	public void addListener(IMonitoringRepositoryListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeListener(IMonitoringRepositoryListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void addRule(Rule rule) {
		rules.addRule(rule);

		// Check whether this rule can be directly activated
		// (if all data dependencies are available)
		checkRules(Collections.singletonList(rule), entities);
	}

	@Override
	public void removeRule(Rule rule) {
		rules.removeRule(rule);
	}

	public <D extends Dimension> void append(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries observations) {
		this.append(m, unit, entity, observations, Aggregation.NONE, ZERO_SECONDS);
	}

	public <D extends Dimension> void append(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries observations,
			Aggregation aggregation, Quantity<Time> aggregationInterval) {
		DataKey<D> key = new DataKey<D>(m, entity, aggregation);
		DataEntry<D> entry = getEntry(key);
		if (entry == null) {
			this.setData(m, unit, entity, observations, aggregation, aggregationInterval);
		} else {
			TimeSeries existing = entry.getRawData();
			// IMPORTANT: ensure that existing and new data have the same unit
			TimeSeries newData = UnitConverter.convertTo(observations, unit, m.getDimension().getBaseUnit());
			if (existing != null) {
				newData = existing.append(newData);
			}
			this.setData(m, unit, entity, newData, aggregation, aggregationInterval);
		}
	}

	public <D extends Dimension> void insert(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries observations) {
		this.setData(m, unit, entity, UnitConverter.convertTo(observations, unit, m.getDimension().getBaseUnit()),
				Aggregation.NONE, ZERO_SECONDS);
	}

	public <D extends Dimension> void insert(Metric<D> m, Unit<D> unit, ModelEntity entity,
			TimeSeries aggregatedObservations, Aggregation aggregation, Quantity<Time> aggregationInterval) {
		this.setData(m, unit, entity,
				UnitConverter.convertTo(aggregatedObservations, unit, m.getDimension().getBaseUnit()), aggregation,
				aggregationInterval);
	}

	private <D extends Dimension> void setData(Metric<D> m, Unit<D> unit, ModelEntity entity, TimeSeries observations,
			Aggregation aggregation, Quantity<Time> aggregationInterval) {
		DataKey<D> key = new DataKey<D>(m, entity, aggregation);
		DataEntry<D> entry = getEntry(key);
		boolean existing = (entry != null);
		observations.setInterpolationMethod(Registry.INSTANCE.getMetricHandler(m).getInterpolation());
		if (!existing) {
			entry = new DataEntry<>(key);
			entry.setTimeSeries(observations, aggregationInterval);
			addEntry(key, entry);
		} else {
			TimeSeries existingData = entry.getRawData();
			if (existingData != null) {
				entry.setTimeSeries(existingData.append(observations), aggregationInterval);
			} else {
				entry.setTimeSeries(observations, aggregationInterval);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug((existing ? "New" : "Replaced") + " time series entry " + entity + "/" + m + "/" + aggregation);
		}
	}

	private <D extends Dimension> void addEntry(DataKey<D> key, DataEntry<D> newEntry) {
		data.put(key, newEntry);
		// This is the first time we add data for this metric, entity and
		// aggregation combination
		notifyNewEntry(key.metric, key.entity, key.aggregation);

	}

	private <D extends Dimension> void notifyNewEntry(Metric<D> metric, ModelEntity entity, Aggregation aggregation) {
		for (IMonitoringRepositoryListener curListener : listeners) {
			curListener.entryAdded(metric, entity, aggregation);
		}
		checkRules(rules.getDerivationRules(metric, aggregation), Collections.singleton(entity));
	}

	private void checkRules(List<Rule> rules, Set<ModelEntity> entities) {
		Set<ModelEntity> notificationSet = new HashSet<>();

		for (Rule r : rules) {
			for (ModelEntity curEntity : entities) {
				if (r.getDependencies().isEmpty()) {
					// this is a default rule without dependencies
					// it is always activated
					r.checkStatus(this, curEntity);
				} else {
					// First we determine the entities that may be affected
					// by the new entry (e.g., new data for a service, may also
					// enable new derivations for external calls to this
					// service)
					for (DataDependency<?> dep : r.getDependencies()) {
						notificationSet.addAll(dep.getScope().getNotificationSet(curEntity));
					}
					for (ModelEntity e : notificationSet) {
						r.checkStatus(this, e);
					}
					notificationSet.clear();
				}
			}
		}
	}

	private List<DataEntry<?>> getRequiredEntries(DerivationRule<?> rule, ModelEntity entity) {
		List<DataEntry<?>> requiredEntries = new LinkedList<>();
		for (DataDependency<?> dep : rule.getDependencies()) {
			Set<? extends ModelEntity> scopeEntities = dep.getScope().getScopeSet(entity);
			for (ModelEntity e : scopeEntities) {
				DataEntry<?> reqEntry = getEntry(dep.getMetric(), e, dep.getAggregation());
				if (reqEntry == null) {
					throw new NoMonitoringDataException(dep.getMetric(), dep.getAggregation(), entity);
				}
				requiredEntries.add(reqEntry);
			}
		}
		return requiredEntries;
	}

	public <D extends Dimension> void insertDerivation(DerivationRule<D> t, ModelEntity entity) {
		Metric<D> metric = t.getMetric();
		Aggregation aggregation = t.getAggregation();

		DataKey<D> key = new DataKey<>(metric, entity, aggregation);
		DataEntry<D> entry = getEntry(key);
		boolean newEntry = (entry == null);
		if (newEntry) {
			entry = new DataEntry<D>(key);
		}

		try {
			List<DataEntry<?>> requiredEntries = getRequiredEntries(t, entity);
			// self-referential rules are only allowed if the associated
			// entry contains actual (non-derived) monitoring data.
			// Otherwise, we may replace rules which triggered
			// the activation of this rule.
			for (DataEntry<?> curEntry : requiredEntries) {
				if (key.equals(curEntry.getKey()) && (curEntry.getRawData() == null)) {
					if (log.isDebugEnabled()) {
						log.debug("Derivation entry " + entity + "/" + metric + "/" + aggregation
								+ " not updated due to self-referential check failure.");
					}
					return; // abort this insert operation
				}
			}
			entry.setDerivationRule(t, t.getDerivationHandler(), requiredEntries);
			if (newEntry) {
				addEntry(key, entry);
			}
			if (log.isDebugEnabled()) {
				log.debug((newEntry ? "New" : "Replaced") + " derivation entry " + entity + "/" + metric + "/"
						+ aggregation);
			}
		} catch (NoMonitoringDataException ex) {
			log.warn("Could not initiliaze derivation entry.", ex);
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
	public WorkloadDescription getWorkload() {
		return workload;
	}

	@Override
	public <D extends Dimension> TimeSeries select(Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation aggregation) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		if (entry == null) {
			throw new NoMonitoringDataException(metric, aggregation, entity);
		}
		return entry.getTimeSeries(this, metric, unit, entity, aggregation, NaN, NaN);
	}

	@Override
	public <D extends Dimension> TimeSeries select(Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
		DataEntry<D> entry = getCheckedEntry(metric, entity, aggregation, start, end);
		return entry.getTimeSeries(this, metric, unit, entity, aggregation, start, end);
	}

	@Override
	public <D extends Dimension> double aggregate(Metric<D> metric, Unit<D> unit, ModelEntity entity,
			Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
		DataEntry<D> entry = getCheckedEntry(metric, entity, aggregation, start, end);
		if (entry.derivationHandler == null) {
			throw new IllegalStateException("No derivation handler for " + metric.getName() + " and "
					+ aggregation.getLiteral() + " is available.");
		}
		return entry.derivationHandler.aggregate(this, metric, unit, entity, aggregation, start, end);
	}

	private <D extends Dimension> DataEntry<D> getCheckedEntry(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		if (entry == null) {
			throw new NoMonitoringDataException(metric, aggregation, entity);
		}
		if ((entry.startTime.compareTo(start) > 0) || (entry.endTime.compareTo(end) < 0)) {
			throw new OutOfMonitoredRangeException(metric, aggregation, entity, start, end, entry.startTime,
					entry.endTime);
		}
		return entry;
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
		if (entry == null) {
			throw new NoMonitoringDataException(metric, aggregation, entity);
		}
		return entry.getAggregationInterval();
	}

	@Override
	public <D extends Dimension> Quantity<Time> getMonitoringStartTime(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		if (entry == null) {
			throw new NoMonitoringDataException(metric, aggregation, entity);
		}
		return entry.getStartTime();
	}

	@Override
	public <D extends Dimension> Quantity<Time> getMonitoringEndTime(Metric<D> metric, ModelEntity entity,
			Aggregation aggregation) {
		DataEntry<D> entry = getEntry(metric, entity, aggregation);
		if (entry == null) {
			throw new NoMonitoringDataException(metric, aggregation, entity);
		}
		return entry.getEndTime();
	}

	private <D extends Dimension> DataEntry<D> getEntry(Metric<D> metric, ModelEntity entity, Aggregation aggregation) {
		return getEntry(new DataKey<D>(metric, entity, aggregation));
	}

	@SuppressWarnings("unchecked")
	private <D extends Dimension> DataEntry<D> getEntry(DataKey<D> key) {
//		for (java.util.Map.Entry<DataKey<?>, DataEntry<?>> e : data.entrySet()) {
//			if (e.getValue().data == null) {
//				System.out.println(e);
//				System.out.println(e.getKey().aggregation);
//				System.out.println(e.getKey().entity);
//				System.out.println(e.getKey().metric);
//				System.out.println("Is null");
//			} else {
//				System.out.println("Kein problem");
//			}
//		}
		return (DataEntry<D>) data.get(key);
	}

	private <D extends Dimension> void registerRules(Metric<D> m) {
		List<DerivationRule<D>> derivationRules = Registry.INSTANCE.getMetricHandler(m).getDerivationRules();
		for (DerivationRule<D> r : derivationRules) {
			addRule(r);
		}
	}

	public void logContentDump() {
		for (Resource resource : workload.getResources()) {
			logEntityContentDump(resource);
		}
		for (Service service : workload.getServices()) {
			logEntityContentDump(service);
			for (Task task : service.getTasks()) {
				logEntityContentDump(task);
			}
		}
	}

	private void logEntityContentDump(ModelEntity entity) {
		StringBuilder dump = new StringBuilder(entity.toString());
		dump.append(": ");
		boolean empty = true;
		for (Metric<?> m : Registry.INSTANCE.getMetrics()) {
			for (Aggregation a : Aggregation.values()) {
				DataEntry<?> entry = getEntry(m, entity, a);
				if (entry != null) {
					empty = false;
					dump.append(m).append("/").append(a);
					if (entry.data != null) {
						dump.append("(").append("length=").append(entry.data.samples()).append(", mean=")
								.append(entry.data.mean(0));
						dump.append(", start=").append(entry.data.getStartTime()).append("s, end=")
								.append(entry.data.getEndTime()).append("s)");
					}
					dump.append(", ");
				}
			}
		}
		log.info(dump);
	}

	@Override
	public void accept(IMonitoringRepositoryVisitor visitor) {
		for (DataKey<?> key : data.keySet()) {
			DataEntry<?> entry = data.get(key);
			if (entry.data != null) {
				visitor.visitTimeSeries(key.entity, key.metric, key.aggregation, entry.aggregationInterval, entry.data);
			}
		}

	}
}
