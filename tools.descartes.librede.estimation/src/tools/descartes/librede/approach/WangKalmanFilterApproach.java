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

import java.util.Arrays;
import java.util.List;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.algorithm.IKalmanFilterAlgorithm;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.observation.functions.UtilizationLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.workload.WorkloadDescription;

@Component(displayName="Kalman Filter using Utilization Law")
public class WangKalmanFilterApproach extends AbstractEstimationApproach {
	
	@ParameterDefinition(name = "StateNoiseCovariance", label = "State Noise Covariance", defaultValue = "1.0")
	private double stateNoiseCovariance;
	
	@ParameterDefinition(name = "StateNoiseCoupling", label = "State Noise Coupling", defaultValue = "1.0")
	private double stateNoiseCoupling;
	
	@ParameterDefinition(name = "ObserveNoiseCovariance", label = "Observe Noise Covariance", defaultValue = "0.0001")
	private double observeNoiseCovariance;

	@Override
	protected List<IStateModel<?>> deriveStateModels(
			WorkloadDescription workload, IRepositoryCursor cursor) {
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		for (Resource res : workload.getResources()) {
			for (Service serv : workload.getServices()) {
				builder.addVariable(res, serv);
			}
		}
		return Arrays.asList(builder.build());
	}

	@Override
	protected IObservationModel<?, ?> deriveObservationModel(
			IStateModel<?> stateModel, IRepositoryCursor cursor) {
		VectorObservationModel<IOutputFunction> observationModel = new VectorObservationModel<IOutputFunction>();
		for (Resource res : stateModel.getResources()) {
			UtilizationLaw func = new UtilizationLaw(stateModel, cursor, res);
			observationModel.addOutputFunction(func);
		}
		return observationModel;
	}

	@Override
	protected IEstimationAlgorithm getEstimationAlgorithm(
			EstimationAlgorithmFactory factory) {
		return factory.createInstance(IKalmanFilterAlgorithm.class);
	}
}
