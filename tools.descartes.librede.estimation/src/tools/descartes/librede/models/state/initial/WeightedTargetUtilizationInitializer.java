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
package tools.descartes.librede.models.state.initial;

import static tools.descartes.librede.linalg.LinAlg.empty;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;

/**
 * This class initializes a state model with demands so that a specified target
 * utilization is reached for the throughput values observed at the system so
 * far. The resulting utilization does not need to match the actual system
 * utilization, it should be chosen so that it is a good starting point for an
 * estimation algorithm. The demands are weighted according to the response
 * times observed so far. This assumes that the response time is proportional to
 * the resource demand.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class WeightedTargetUtilizationInitializer implements IStateInitializer {

	private final IRepositoryCursor cursor;
	private final double targetUtilization;

	public WeightedTargetUtilizationInitializer(double targetUtilization, IRepositoryCursor cursor) {
		this.cursor = cursor;
		this.targetUtilization = targetUtilization;
	}

	@Override
	public Vector getInitialValue(IStateModel<?> stateModel) {
		Query<Vector, Time> respTime = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(stateModel.getUserServices()).average().using(cursor);
		Query<Vector, RequestRate> throughput = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(stateModel.getUserServices()).average().using(cursor);

		int resourceCount = stateModel.getResources().size();
		
		Vector initialDemands = respTime.execute();
		for (int i = 0; i < initialDemands.rows(); i++) {
			if (Double.isNaN(initialDemands.get(i))) {
				// not enough observations yet to initialized the state
				return empty();
			}
		}
		if (resourceCount > 1) {
			// If we have several resources, then distribute the demands evenly
			// between the resources
			initialDemands = initialDemands.times(1.0 / resourceCount);
		}

		// utilizations close to 100% or above turned out to cause convergence
		// issues with many
		// approaches that depend on it as a starting point. Therefore, we scale
		// the demands to a value
		// so that the utilization at the beginning is at a configured initial
		// point (e.g., 50%).
		VectorBuilder initialState = VectorBuilder.create(stateModel.getAllServices().size());
		for (Resource res : stateModel.getResources()) {
			double util = initialDemands.dot(throughput.execute()) / res.getNumberOfServers();
			Vector curDemands = initialDemands.times(targetUtilization / util);
			
			for (Service service : res.getServices()) {
				int idx = throughput.indexOf(service);
				initialState.set(stateModel.getStateVariableIndex(res,  service), curDemands.get(idx));
			}
		}	

		// assume each resource has the same initial demands
		return initialState.toVector();
	}
}
