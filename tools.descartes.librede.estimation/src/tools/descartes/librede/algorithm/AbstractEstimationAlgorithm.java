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
package tools.descartes.librede.algorithm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.rules.IRuleActivationHandler;
import tools.descartes.librede.repository.rules.Rule;
import tools.descartes.librede.repository.rules.DependencyScope;
import tools.descartes.librede.units.Time;

/**
 * This abstract class provides standard implementations for some of the methods in {@link IEstimationAlgorithm}.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public abstract class AbstractEstimationAlgorithm implements IEstimationAlgorithm, IRuleActivationHandler<Time> {
	
	private static final Logger log = Logger.getLogger(AbstractEstimationAlgorithm.class);
	
	private IStateModel<?> stateModel;
	private IObservationModel<?, ?> observationModel;
	private IRepositoryCursor cursor;
	private final List<Rule<?>> dependencies = new LinkedList<Rule<?>>();
	private final Set<Rule<?>> unsatisfiedDependencies = new HashSet<Rule<?>>();
	private boolean activated = false;
	
	@Override
	public void initialize(IStateModel<?> stateModel, IObservationModel<?, ?> observationModel,
			IRepositoryCursor cursor, int estimationWindow) throws InitializationException {
		this.stateModel = stateModel;
		this.observationModel = observationModel;
		this.cursor = cursor;
		initializeDependencies();
		for (Rule<?> rule : dependencies) {
			cursor.getRepository().addRule(rule);
		}
	}

	@Override
	public IStateModel<?> getStateModel() {
		return stateModel;
	}

	@Override
	public IObservationModel<?, ?> getObservationModel() {
		return observationModel;
	}

	@Override
	public void destroy() {
		for (Rule<?> rule : dependencies) {
			cursor.getRepository().removeRule(rule);
		}
	}
	
	@Override
	public void activateRule(IMonitoringRepository repository, Rule<Time> rule, ModelEntity entity) {
		if (unsatisfiedDependencies.contains(rule)) {
			if (log.isDebugEnabled()) {
				log.debug("Satisfied dependency rule: " + rule);
			}
			unsatisfiedDependencies.remove(rule);
			if (unsatisfiedDependencies.isEmpty()) {
				activateAlgorithm();
			}
		}
	}
	
	private void activateAlgorithm() {
		log.info("Activated state model: " + stateModel);
		activated = true;
	}
	
	@Override
	public void deactivateRule(IMonitoringRepository repository, Rule<Time> rule, ModelEntity entity) {
		if (!unsatisfiedDependencies.add(rule)) {
			if (log.isDebugEnabled()) {
				log.debug("Unsatisfied dependency rule: " + rule);
			}
			if (activated) {
				deactivateAlgorithm();
			}
		}				
	}
	
	private void deactivateAlgorithm() {
		activated = false;
	}
	
	protected void initializeDependencies() {
	}
	
	protected void addDependency(Query<?,?> query) {
		Rule<Time> newRule = Rule.rule(StandardMetrics.RESOURCE_DEMAND, Aggregation.AVERAGE);
		newRule.requiring(query.getMetric(), query.getAggregation(), DependencyScope.fixedScope(query.getEntities()));
		newRule.build(this);
		dependencies.add(newRule);
		unsatisfiedDependencies.add(newRule);
	}
	
}
