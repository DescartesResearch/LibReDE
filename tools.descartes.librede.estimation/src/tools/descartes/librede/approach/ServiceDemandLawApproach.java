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

import static tools.descartes.librede.linalg.LinAlg.zeros;
import tools.descartes.librede.algorithm.SimpleApproximation;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IDirectOutputFunction;
import tools.descartes.librede.models.observation.functions.ServiceDemandLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.Aggregation;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.workload.WorkloadDescription;

@Component(displayName="Service Demand Law")
public class ServiceDemandLawApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "ServiceDemandLaw";
	
	@Override
	public void initialize(WorkloadDescription workload,
			IRepositoryCursor cursor, int estimationWindow, boolean iterative) throws InitializationException {
		super.initialize(workload, cursor, estimationWindow, iterative);
		
		int stateSize = workload.getServices().size();
		
		ConstantStateModel<Unconstrained> stateModel = new ConstantStateModel<Unconstrained>(stateSize, zeros(stateSize));
		
		VectorObservationModel<IDirectOutputFunction> observationModel = new VectorObservationModel<IDirectOutputFunction>();
		for (Resource res : workload.getResources()) {
			for (Service service : workload.getServices()) {
				ServiceDemandLaw func = new ServiceDemandLaw(workload, cursor, res, service);
				observationModel.addOutputFunction(func);
			}
		}

		SimpleApproximation estimator = new SimpleApproximation(Aggregation.AVERAGE);
		estimator.initialize(stateModel, observationModel, estimationWindow);
		setEstimationAlgorithm(estimator);
	}
}