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

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import tools.descartes.librede.algorithm.EstimationAlgorithmFactory;
import tools.descartes.librede.algorithm.IConstrainedNonLinearOptimizationAlgorithm;
import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.IOutputWeightingFunction;
import tools.descartes.librede.models.observation.OutputFunction;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.equations.ResponseTimeEquation;
import tools.descartes.librede.models.observation.equations.ResponseTimeValue;
import tools.descartes.librede.models.observation.equations.UtilizationLawEquation;
import tools.descartes.librede.models.observation.equations.UtilizationValue;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.InvocationGraph;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.models.state.constraints.NoRequestsBoundsConstraint;
import tools.descartes.librede.models.state.initial.WeightedTargetUtilizationInitializer;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.RequestRate;

/**
 * See:
 * Parameter inference of queueing models for it systems using end-to-end measurements.
 * Z Liu, L Wynter, CH Xia, F Zhang - Performance Evaluation, 2006
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
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
		builder.setInvocationGraph(new InvocationGraph(workload.getServices(), cursor, getEstimationWindow()));
		return Arrays.<IStateModel<?>>asList(builder.build());
	}

	@Override
	protected IObservationModel<?> deriveObservationModel(IStateModel<?> stateModel, IRepositoryCursor cursor) {
		// Weight the individual equations by the throughput of the corresponding entities.
		// See "Parameter inference of queueing models for it systems using end-to-end measurements.
	    // Z Liu, L Wynter, CH Xia, F Zhang - Performance Evaluation, 2006" for details
		ThroughputWeights weightsFunction = new ThroughputWeights();
		VectorObservationModel observationModel = new VectorObservationModel(weightsFunction);
		List<ModelEntity> entities = new LinkedList<>();
		for (int i = 0; i < getEstimationWindow(); i++) {
			for (Service service : stateModel.getUserServices()) {
				// Current assumption is that services which are not called by others
				// are the system entry services. Since we look at the end to end
				// response time, we only include these services to the estimation.
				// The remaining services are referenced from these system entry services.
				if (service.getIncomingCalls().isEmpty()) {
					ResponseTimeEquation func = new ResponseTimeEquation(stateModel, cursor, service, true, i);
					observationModel.addOutputFunction(new OutputFunction(new ResponseTimeValue(stateModel, cursor, service, i), func));
					entities.add(service);
				}
			}
			for (Resource resource : stateModel.getResources()) {
				if (resource.getSchedulingStrategy() != SchedulingStrategy.IS) {
					UtilizationLawEquation func = new UtilizationLawEquation(stateModel, cursor, resource, i);
					observationModel.addOutputFunction(new OutputFunction(new UtilizationValue(stateModel, cursor, resource, i), func));
					entities.add(resource);
				}
			}
		}
		weightsFunction.setEntities(cursor, entities);
		return observationModel;
	}

	@Override
	protected IEstimationAlgorithm getEstimationAlgorithm(EstimationAlgorithmFactory factory) {
		return factory.createInstance(IConstrainedNonLinearOptimizationAlgorithm.class);
	}
	
	private class ThroughputWeights implements IOutputWeightingFunction {

		private Query<Vector, RequestRate> tputQuery;
		private List<ModelEntity> entities;
		
		public void setEntities(IRepositoryCursor cursor, List<ModelEntity> entities) {
			this.entities = entities;
			Set<Service> services = new HashSet<>();
			for (ModelEntity curEntity : entities) {
				if (curEntity instanceof Service) {
					services.add((Service)curEntity);
				}
			}
			tputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(services).average().using(cursor);
		}

		@Override
		public Vector getOutputWheights() {
			Vector tput = tputQuery.execute();
			double sumTput = LinAlg.sum(tput).get(0);
			VectorBuilder weights = VectorBuilder.create(entities.size());
			for (ModelEntity curEntity : entities) {
				if (curEntity instanceof Service) {
					if (sumTput > 0.0) {
						weights.add(tput.get(tputQuery.indexOf(curEntity)) / sumTput);
					} else {
						weights.add(0);
					}
				} else if (curEntity instanceof tools.descartes.librede.configuration.Resource) {
					weights.add(1);
				}
			}
			return weights.toVector();
		}
		
	}

}
