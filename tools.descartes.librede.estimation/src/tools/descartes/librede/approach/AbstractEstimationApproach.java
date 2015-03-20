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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.ResultTable;
import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.units.Time;

public abstract class AbstractEstimationApproach implements IEstimationApproach {
	
	private static final Logger log = Logger.getLogger(AbstractEstimationApproach.class);

	private boolean iterative;
	private int estimationWindow;
	private EstimationAlgorithmFactory algorithmFactory;
	private WorkloadDescription workload;
	private List<IEstimationAlgorithm> algorithms;
	private IRepositoryCursor cursor;

	protected abstract List<IStateModel<?>> deriveStateModels(
			WorkloadDescription workload, IRepositoryCursor cursor);

	protected abstract IObservationModel<?, ?> deriveObservationModel(
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
		algorithms = new ArrayList<IEstimationAlgorithm>(
				stateModels.size());		
		
		for (IStateModel<?> sm : stateModels) {
			IObservationModel<?, ?> om = deriveObservationModel(sm, cursor);
			IEstimationAlgorithm algo = getEstimationAlgorithm(algorithmFactory);
			algo.initialize(sm, om, estimationWindow);
			algorithms.add(algo);
		}
	}
	
	@Override
	public void pruneEstimationDefinitions() {
		List<IEstimationAlgorithm> temp = new ArrayList<IEstimationAlgorithm>(algorithms);
		
		Set<Resource> ignoredResources = new HashSet<Resource>();
		Set<Service> ignoredServices = new HashSet<Service>();
		List<String> messages = new ArrayList<String>();
		for (IEstimationAlgorithm a : temp) {
			boolean isApplicable = true;
			
			for (IStateConstraint constr : a.getStateModel().getConstraints()) {
				isApplicable = isApplicable && constr.isApplicable(messages);
			}
			for (IOutputFunction func : a.getObservationModel()) {
				isApplicable = isApplicable && func.isApplicable(messages);
			}
			if (!isApplicable) {
				ignoredResources.addAll(a.getStateModel().getResources());
				ignoredServices.addAll(a.getStateModel().getServices());				
				algorithms.remove(a);
			}
		}
		if (messages.size() > 0) {
			StringBuilder warning = new StringBuilder("State model pruned\n");
			warning.append(" -- Approach \"").append(Registry.INSTANCE.getDisplayName(this.getClass())).append("\" cannot be applied.\n");
			warning.append(" -- Ignored resources: ");
			boolean first = true;
			for(Resource r : ignoredResources) {
				if (!first) {
					warning.append(", ");
				}
				warning.append(r.getName());
				first = false;
			}
			warning.append(" --\n");
			warning.append(" -- Ignored services: ");
			first = true;
			for (Service s : ignoredServices) {
				if (!first) {
					warning.append(", ");
				}
				warning.append(s.getName());
				first = false;
			}
			warning.append(" --\n");
			warning.append(" -- Reason:\n");
			for (String message : messages) {
				warning.append("    -- ").append(message).append("\n");
			}
			log.warn(warning);			
		}
	}

	@Override
	public ResultTable executeEstimation() throws EstimationException {
		if (algorithms == null) {
			throw new IllegalStateException();
		}

		try {
			ResultTable.Builder builder = ResultTable.builder(
					this.getClass(), workload);

			if (iterative) {
				while (cursor.next()) {					
					builder.next(cursor.getCurrentIntervalEnd().getValue(Time.MILLISECONDS));
					
					for (IEstimationAlgorithm a : algorithms) {
						a.update();
						Vector curEstimates = a.estimate();
						for (int i = 0; i < curEstimates.rows(); i++) {
							builder.set(a.getStateModel().getResource(i), a.getStateModel().getService(i), curEstimates.get(i));
						}
					}
					
					builder.save();
				}
			} else {
				while(cursor.next()) {
					for (IEstimationAlgorithm a : algorithms) {
						a.update();
					}
				}
				
				builder.next(cursor.getCurrentIntervalEnd().getValue(Time.MILLISECONDS));
				for (IEstimationAlgorithm a : algorithms) {
					Vector curEstimates = a.estimate();
					for (int i = 0; i < curEstimates.rows(); i++) {
						builder.set(a.getStateModel().getResource(i), a.getStateModel().getService(i), curEstimates.get(i));
					}
				}
				builder.save();
			}
			return builder.build();
		} finally {
			for (IEstimationAlgorithm a: algorithms) {
				a.destroy();
			}
		}
	}

}
