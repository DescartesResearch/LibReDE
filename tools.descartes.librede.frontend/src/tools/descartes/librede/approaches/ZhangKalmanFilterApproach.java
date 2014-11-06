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
package tools.descartes.librede.approaches;

import static tools.descartes.librede.linalg.LinAlg.ones;
import tools.descartes.librede.bayesplusplus.ExtendedKalmanFilter;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.observation.functions.ResponseTimeEquation;
import tools.descartes.librede.models.observation.functions.UtilizationLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.workload.WorkloadDescription;

@Component(displayName = "Kalman Filter using Response Times and Utilization")
public class ZhangKalmanFilterApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "ZhangKalmanFilter";
	
	@ParameterDefinition(name = "StateNoiseCovariance", label = "State Noise Covariance", defaultValue = "1.0")
	private double stateNoiseCovariance;
	
	@ParameterDefinition(name = "StateNoiseCoupling", label = "State Noise Coupling", defaultValue = "1.0")
	private double stateNoiseCoupling;
	
	@ParameterDefinition(name = "ObserveNoiseCovariance", label = "Observe Noise Covariance", defaultValue = "0.0001")
	private double observeNoiseCovariance;

	@Override
	public void initialize(WorkloadDescription workload,
			IRepositoryCursor cursor, int estimationWindow, boolean iterative)
			throws InitializationException {
		super.initialize(workload, cursor, estimationWindow, iterative);
		
		int stateSize = workload.getServices().size();
		
		ConstantStateModel<Unconstrained> stateModel = new ConstantStateModel<Unconstrained>(stateSize, ones(stateSize).times(0.01));
		
		VectorObservationModel<IOutputFunction> observationModel = new VectorObservationModel<IOutputFunction>();
		for (Service serv : workload.getServices()) {
			ResponseTimeEquation func = new ResponseTimeEquation(workload, cursor, serv, workload.getResources());
			observationModel.addOutputFunction(func);
		}
		
		
		for (Resource res : workload.getResources()) {
			UtilizationLaw func = new UtilizationLaw(workload, cursor, res);
			observationModel.addOutputFunction(func);
		}

		ExtendedKalmanFilter estimator = new ExtendedKalmanFilter();
		estimator.setStateNoiseCouplingConstant(stateNoiseCoupling);
		estimator.setStateNoiseCovarianceConstant(stateNoiseCovariance);
		estimator.setObserveNoiseConstant(observeNoiseCovariance);
		estimator.initialize(stateModel, observationModel, estimationWindow);
		setEstimationAlgorithm(estimator);
	}



}
