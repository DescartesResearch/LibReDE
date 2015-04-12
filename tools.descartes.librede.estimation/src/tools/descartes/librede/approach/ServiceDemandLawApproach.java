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
import java.util.List;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.algorithm.SimpleApproximation;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IDirectOutputFunction;
import tools.descartes.librede.models.observation.functions.ServiceDemandLaw;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;

@Component(displayName="Service Demand Law")
public class ServiceDemandLawApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "ServiceDemandLaw";
	
	@Override
	protected List<IStateModel<?>> deriveStateModels(
			WorkloadDescription workload, IRepositoryCursor cursor) {
		List<IStateModel<?>> stateModels = new ArrayList<IStateModel<?>>();
		
		for (Resource res : workload.getResources()) {
			Builder<Unconstrained> stateModelBuilder = ConstantStateModel.unconstrainedModelBuilder();
			for (Service service : workload.getServices()) {
				stateModelBuilder.addVariable(res, service);
			}
			stateModels.add(stateModelBuilder.build());
		}
		
		return stateModels;
	}
	
	@Override
	protected IObservationModel<?,?> deriveObservationModel(
			IStateModel<?> stateModel, IRepositoryCursor cursor) {
		VectorObservationModel<IDirectOutputFunction> observationModel = new VectorObservationModel<IDirectOutputFunction>();
		for (Resource resource : stateModel.getResources()) {
			for (Service service : stateModel.getUserServices()) {
				ServiceDemandLaw func = new ServiceDemandLaw(stateModel, cursor, resource, service);
				observationModel.addOutputFunction(func);
			}
		}
		return observationModel;
	}

	@Override
	protected IEstimationAlgorithm getEstimationAlgorithm(
			EstimationAlgorithmFactory factory) {
		return new SimpleApproximation(Aggregation.AVERAGE);
	}
}
