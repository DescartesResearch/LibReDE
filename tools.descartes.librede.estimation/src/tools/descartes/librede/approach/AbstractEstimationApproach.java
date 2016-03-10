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
package tools.descartes.librede.approach;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.ResultTable;
import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.EstimationProblem;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.exceptions.OutOfMonitoredRangeException;
import tools.descartes.librede.repository.rules.DataDependency;
import tools.descartes.librede.repository.rules.IRuleActivationHandler;
import tools.descartes.librede.repository.rules.Rule;
import tools.descartes.librede.units.Time;

public abstract class AbstractEstimationApproach implements IEstimationApproach {
	
	private class EstimationProblemActivator implements IRuleActivationHandler {
		@Override
		public void activateRule(IMonitoringRepository repository, Rule.Status ruleStatus, ModelEntity entity) {
			if (!(ruleStatus.getRule() instanceof EstimationProblem)) {
				throw new IllegalArgumentException();
			}
			EstimationProblem problem = (EstimationProblem)ruleStatus.getRule();
			if (!algorithmInstances.containsKey(problem)) {
				IEstimationAlgorithm algorithm = getEstimationAlgorithm(algorithmFactory);
				try {
					algorithm.initialize(problem, cursor, estimationWindow);
					algorithmInstances.put(problem, algorithm);
					log.info("Initialized estimation problem: " + problem.getStateModel());
				} catch (InitializationException e) {
					log.error("Error initializing estimation algorithm for estimation problem: " + problem.getStateModel(), e);
				}
			}
			if (deactivatedProblems.contains(problem)) {				
				log.info("Activated estimation problem: " + problem.getStateModel());
				deactivatedProblems.remove(problem);
			}
		}
		
		@Override
		public void deactivateRule(IMonitoringRepository repository, Rule.Status ruleStatus, ModelEntity entity) {
			if (!(ruleStatus.getRule() instanceof EstimationProblem)) {
				throw new IllegalArgumentException();
			}
			EstimationProblem problem = (EstimationProblem)ruleStatus.getRule();
			if (!deactivatedProblems.contains(problem)) {
				deactivatedProblems.add(problem);
				
				// Create analysis message for user
				StringBuilder message = new StringBuilder("Deactivated estimation problem: ");
				message.append(problem.getStateModel().toString());
				message.append("\n    Reasons:");
				for (DataDependency<?>.Status depStatus : ruleStatus.getDependenciesStatus()) {
					if (!depStatus.isResolved()) {
						message.append("\n    - Observation data of metric ");
						message.append(depStatus.getDependency().getMetric().getName());
						message.append("(").append(depStatus.getDependency().getAggregation()).append(")");
						message.append(" is missing for entities ");
						boolean first = true;
						for (ModelEntity missing : depStatus.getMissingEntities()) {
							if (first) {
								first = false;
							} else {
								message.append(", ");
							}
							message.append(missing.getName());
						}
					}
				}
				log.info(message);
			}
		}
	}
	
	private final Logger log = Logger.getLogger(getClass());

	private boolean iterative;
	private int estimationWindow;
	private EstimationAlgorithmFactory algorithmFactory;
	private WorkloadDescription workload;
	private List<EstimationProblem> problems;
	private final Map<EstimationProblem, IEstimationAlgorithm> algorithmInstances = new HashMap<>();
	private final Set<EstimationProblem> deactivatedProblems = new HashSet<>();
	private IRepositoryCursor cursor;
	private final EstimationProblemActivator activationListener = new EstimationProblemActivator();

	protected abstract List<IStateModel<?>> deriveStateModels(
			WorkloadDescription workload, IRepositoryCursor cursor);

	protected abstract IObservationModel<?> deriveObservationModel(
			IStateModel<?> stateModel, IRepositoryCursor cursor);

	protected abstract IEstimationAlgorithm getEstimationAlgorithm(
			EstimationAlgorithmFactory factory);

	@Override
	public void initialize(WorkloadDescription workload,
			IRepositoryCursor cursor,
			EstimationAlgorithmFactory algorithmFactory, int estimationWindow,
			boolean iterative) {
		this.workload = workload;
		this.cursor = cursor;
		this.algorithmFactory = algorithmFactory;
		this.iterative = iterative;
		this.estimationWindow = estimationWindow;
	}

	public void constructEstimationDefinitions() throws InitializationException{
		if (workload == null || cursor == null) {
			throw new IllegalStateException();
		}
		List<IStateModel<?>> stateModels = deriveStateModels(workload, cursor);
		for(Iterator<IStateModel<?>> it = stateModels.iterator(); it.hasNext();) {
			if(it.next().getStateSize() == 0) {
				log.info("Prune empty state model.");
				it.remove();
			}
		}
		
		problems = new LinkedList<>();		
		
		for (IStateModel<?> sm : stateModels) {
			IObservationModel<?> om = deriveObservationModel(sm, cursor);
			EstimationProblem prob = new EstimationProblem(sm, om);
			prob.setActivationHandler(activationListener);
			problems.add(prob);
			// register with the repository so that we are informed when
			// new observation data gets available
			cursor.getRepository().addRule(prob);
		}
	}
	
	@Override
	public void pruneEstimationDefinitions() {
		// TODO Remove this function from interface!		
	}
	
	@Override
	public ResultTable executeEstimation() throws EstimationException {
		try {
			ResultTable.Builder builder = ResultTable.builder(
					this.getClass(), workload);
			int iterations = 0;
			if (iterative) {
				while (cursor.next()) {					
					builder.next(cursor.getIntervalEnd(cursor.getLastInterval()).getValue(Time.MILLISECONDS));
					
					for (EstimationProblem currentProblem : problems) {
						if (!deactivatedProblems.contains(currentProblem)) {
							IEstimationAlgorithm a = algorithmInstances.get(currentProblem);
							if (a == null) {
								// should only happen if initilization logic is incorrect.
								log.error("Could not initialize apporach");
								throw new EstimationException("Could not initialize approach" + Registry.INSTANCE.getDisplayName(getClass()));
							}
							try {
								a.update();
								Vector curEstimates = a.estimate();
								for (int j = 0; j < curEstimates.rows(); j++) {
									builder.set(a.getStateModel().getResourceDemand(j), curEstimates.get(j));
								}
							} catch(OutOfMonitoredRangeException ex) {
								if (!deactivatedProblems.contains(currentProblem)) {
									StringBuilder message = new StringBuilder("Deactivated estimation problem: ");
									message.append(currentProblem.getStateModel().toString());
									message.append("\n    Reasons:");
									message.append("\n    " + ex.getMessage());
									log.warn(message);
									deactivatedProblems.add(currentProblem);
								}
							}
						}
					}
					iterations++;
					builder.save();
				}
			} else {
				while(cursor.next()) {
					for (EstimationProblem currentProblem : problems) {
						if (!deactivatedProblems.contains(currentProblem)) {
							IEstimationAlgorithm a = algorithmInstances.get(currentProblem);
							if (a == null) {
								// should only happen if initilization logic is incorrect.
								log.error("Could not initialize apporach");
								throw new EstimationException("Could not initialize approach" + Registry.INSTANCE.getDisplayName(getClass()));
							}

							try {
								a.update();
							} catch(OutOfMonitoredRangeException ex) {
								if (!deactivatedProblems.contains(currentProblem)) {
									StringBuilder message = new StringBuilder("Deactivated estimation problem: ");
									message.append(currentProblem.getStateModel().toString());
									message.append("\n    Reasons:");
									message.append("\n    " + ex.getMessage());
									log.warn(message);
									deactivatedProblems.add(currentProblem);
								}
							}
						}
					}
					iterations++;
				}
				
				builder.next(cursor.getIntervalEnd(cursor.getLastInterval()).getValue(Time.MILLISECONDS));
				for (EstimationProblem currentProblem : problems) {
					if (!deactivatedProblems.contains(currentProblem)) {
						IEstimationAlgorithm a = algorithmInstances.get(currentProblem);
						if (a == null) {
							// should only happen if initilization logic is incorrect.
							throw new IllegalStateException();
						}
						Vector curEstimates = a.estimate();
						for (int i = 0; i < curEstimates.rows(); i++) {
							builder.set(a.getStateModel().getResourceDemand(i), curEstimates.get(i));
						}
					}
				}
				builder.save();
			}
			
			
			if (iterations > 0) {
				log.info("Number of iterations for estimation approach: " + iterations);
			} else {
				log.warn("Estimation was skipped (zero iterations): start time=" + cursor.getIntervalStart(0) + ", current time=" + cursor.getRepository().getCurrentTime() + ", step size=" + cursor.getIntervalEnd(0).minus(cursor.getIntervalStart(0)));
			}
			
			return builder.build();
		} finally {
			for (IEstimationAlgorithm a : algorithmInstances.values()) {
				a.destroy();
			}
		}
	}
	
	protected int getEstimationWindow() {
		return estimationWindow;
	}

}
