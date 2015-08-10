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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IConstrainedNonLinearOptimizationAlgorithm;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.observation.functions.ResponseTimeEquation;
import tools.descartes.librede.models.observation.functions.UtilizationLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.InvocationGraph;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.models.state.constraints.NoRequestsBoundsConstraint;
import tools.descartes.librede.models.state.initial.WeightedTargetUtilizationInitializer;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;

@Component(displayName = "Recursive Optimization using Response Times and Utilization")
public class LiuOptimizationApproach extends AbstractEstimationApproach {
	
	/**
	 * The initial demand is scaled to this utilization level, to avoid bad
	 * starting points (e.g., demands that would result in a utilization value
	 * above 100%)
	 */
	private static final double INITIAL_UTILIZATION = 0.5;

	@Override
	protected List<IStateModel<?>> deriveStateModels(WorkloadDescription workload, IRepositoryCursor cursor) {
		Builder<IStateConstraint> builder = ConstantStateModel.constrainedModelBuilder();
		Set<Service> services = new HashSet<>();
		for (Resource res : workload.getResources()) {
			for (ResourceDemand demand : res.getDemands()) {
				builder.addConstraint(new NoRequestsBoundsConstraint(demand, cursor, 0, Double.POSITIVE_INFINITY));
				builder.addVariable(demand);
				services.add(demand.getService());
			}
		}
		builder.setStateInitializer(new WeightedTargetUtilizationInitializer(INITIAL_UTILIZATION, cursor));
		builder.setInvocationGraph(new InvocationGraph(new ArrayList<>(services), cursor, getEstimationWindow()));
		return Arrays.<IStateModel<?>>asList(builder.build());
	}

	@Override
	protected IObservationModel<?, ?> deriveObservationModel(IStateModel<?> stateModel, IRepositoryCursor cursor) {
		VectorObservationModel<IOutputFunction> observationModel = new VectorObservationModel<IOutputFunction>();
		for (int i = 0; i < getEstimationWindow(); i++) {
			for (Service service : stateModel.getUserServices()) {
				// Current assumption is that services which are not called by others
				// are the system entry services. Since we look at the end to end
				// response time, we only include these services to the estimation.
				// The remaining services are referenced from these system entry services.
				if (service.getIncomingCalls().isEmpty()) {
					ResponseTimeEquation func = new ResponseTimeEquation(stateModel, cursor, service, true, i);
					observationModel.addOutputFunction(func);
				}
			}
			for (Resource resource : stateModel.getResources()) {
				if (resource.getSchedulingStrategy() != SchedulingStrategy.IS) {
					UtilizationLaw func = new UtilizationLaw(stateModel, cursor, resource, i);
					observationModel.addOutputFunction(func);
				}
			}
		}
		return observationModel;
	}

	@Override
	protected IEstimationAlgorithm getEstimationAlgorithm(EstimationAlgorithmFactory factory) {
		return factory.createInstance(IConstrainedNonLinearOptimizationAlgorithm.class);
	}

}
