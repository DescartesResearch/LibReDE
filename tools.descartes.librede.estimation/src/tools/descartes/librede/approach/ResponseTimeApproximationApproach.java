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

import org.apache.log4j.Logger;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.algorithm.SimpleApproximation;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.OutputFunction;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.ResponseTimeApproximation;
import tools.descartes.librede.models.observation.queueingmodel.ConstantValue;
import tools.descartes.librede.models.observation.queueingmodel.ResponseTimeApproximationEquation;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;

@Component(displayName = "Approximation with Response Times")
public class ResponseTimeApproximationApproach extends AbstractEstimationApproach {
	
	private static final Logger log = Logger.getLogger(ResponseTimeApproximation.class);
	
	public static final String NAME = "ResponseTimeApproximation";
	
	@Override
	protected List<IStateModel<?>> deriveStateModels(
			WorkloadDescription workload, IRepositoryCursor cursor) {
		List<IStateModel<?>> stateModels = new ArrayList<IStateModel<?>>();
		for (Resource res : workload.getResources()) {
			Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
			for (ResourceDemand demand : res.getDemands()) {
				if (!demand.getService().isBackgroundService()) {
					builder.addVariable(demand);
				} else {
					log.warn("Background services are not supported by Approximation with Response Times approach. Service \"" + demand.getService().getName() + "\" will be ignored at resource \"" + res.getName() + "\".");
				}
			}
			stateModels.add(builder.build());
		}		
		return stateModels;
	}
	
	@Override
	protected IObservationModel<?> deriveObservationModel(
			IStateModel<?> stateModel, IRepositoryCursor cursor) {
		Resource resource = stateModel.getResources().toArray(new Resource[1])[0];
		VectorObservationModel observationModel = new VectorObservationModel();
		for (Service service : stateModel.getUserServices()) {
			ResponseTimeApproximationEquation func = new ResponseTimeApproximationEquation(stateModel, cursor, resource, service, Aggregation.AVERAGE);
			observationModel.addOutputFunction(new OutputFunction(func, new ConstantValue(stateModel, 1.0)));
		}
		return observationModel;
	}

	@Override
	protected IEstimationAlgorithm getEstimationAlgorithm(
			EstimationAlgorithmFactory factory) {
		return new SimpleApproximation(Aggregation.AVERAGE);
	}
}
