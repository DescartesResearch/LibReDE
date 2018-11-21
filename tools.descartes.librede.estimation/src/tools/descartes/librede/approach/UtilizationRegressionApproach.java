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
package tools.descartes.librede.approach;

import java.util.ArrayList;
import java.util.List;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.algorithm.ILeastSquaresRegressionAlgorithm;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.OutputFunction;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.equations.UtilizationLawEquation;
import tools.descartes.librede.models.observation.equations.UtilizationValue;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;

@Component(displayName = "Least-squares Regression using Utilization Law")
public class UtilizationRegressionApproach extends AbstractEstimationApproach {
	
	@Override
	protected List<IStateModel<?>> deriveStateModels(
			WorkloadDescription workload, IRepositoryCursor cursor) {
		// For each resource we create a separate state model as
		// linear regression only supports one output variable (Utilization)
		// at a time.
		List<IStateModel<?>> stateModels = new ArrayList<IStateModel<?>>();
		for (Resource res : workload.getResources()) {
			Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
			for (ResourceDemand demand : res.getDemands()) {
				builder.addVariable(demand);
			}
			stateModels.add(builder.build());
		}
		return stateModels;
	}

	@Override
	protected IObservationModel<?> deriveObservationModel(
			IStateModel<?> stateModel, IRepositoryCursor cursor) {
		VectorObservationModel om = new VectorObservationModel();
		for (Resource resource : stateModel.getResources()) {
			UtilizationLawEquation func = new UtilizationLawEquation(stateModel, cursor, resource, 0);
			om.addOutputFunction(new OutputFunction(new UtilizationValue(stateModel, cursor, resource, 0), func));
		}		
		return om;
	}

	@Override
	protected IEstimationAlgorithm getEstimationAlgorithm(
			EstimationAlgorithmFactory factory) {
		return factory.createInstance(ILeastSquaresRegressionAlgorithm.class);
	}
}
