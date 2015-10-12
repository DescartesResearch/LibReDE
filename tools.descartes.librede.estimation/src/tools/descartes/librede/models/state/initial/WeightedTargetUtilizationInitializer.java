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

import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Vector;
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
		Set<Service> systemServices = new HashSet<>();
		for (Service curService : stateModel.getUserServices()) {
			if (curService.getIncomingCalls().isEmpty()) {
				systemServices.add(curService);
			}
		}		
		Query<Vector, Time> respTime = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forServices(systemServices).average().using(cursor);
		Query<Vector, RequestRate> throughput = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(stateModel.getUserServices()).average().using(cursor);
	
		Vector tput = throughput.execute();
		Vector rt = respTime.execute();
		double[] initialState = new double[stateModel.getStateSize()];
		Set<ResourceDemand> demands = new HashSet<>();
		for (Service curService : systemServices) {			
			// Collect all resource demands
			demands.clear();
			demands.addAll(curService.getResourceDemands());
			Set<Service> calledServices = stateModel.getInvocationGraph().getCalledServices(curService);
			for (Service called : calledServices) {
				demands.addAll(called.getResourceDemands());
			}
			
			// distribute the observed response time evenly between the resource demands
			double baseDemand = 0.0;
			double curRespTime = rt.get(respTime.indexOf(curService));
			if (!Double.isNaN(curRespTime)) {
				baseDemand = curRespTime / demands.size();
			}
			// No request was observed for this system services
			// --> we gained no information on the possible demand so skip the distribution between participating services.
			if (baseDemand > 0.0) {
				for (ResourceDemand curDemand : demands) {
					double visits = 1.0;
					if (curDemand.getService() != curService) {
						visits = stateModel.getInvocationGraph().getInvocationCount(curService, curDemand.getService());
					}
					int stateVarIdx = stateModel.getStateVariableIndex(curDemand.getResource(), curDemand.getService());
					if (visits > 0.0) {
						if (initialState[stateVarIdx] > 0) {
							// in a complex control graph a service may be called by different services
							// as a workaround we always use the minimum of the calculated initial demands
							// as a starting point. (The response time is an upper bound on the demands)
							initialState[stateVarIdx] = Math.min(initialState[stateVarIdx], baseDemand / visits);
						} else {
							initialState[stateVarIdx] = baseDemand / visits;
						}
					}
				}
			}
		}
		// utilizations close to 100% or above turned out to cause convergence
		// issues with many
		// approaches that depend on it as a starting point. Therefore, we scale
		// the demands to a value
		// so that the utilization at the beginning is at a configured initial
		// point (e.g., 50%).
		for (Resource res : stateModel.getResources()) {
			List<Service> accessingServices = res.getAccessingServices();
			double util = 0.0;
			for (Service curService : accessingServices) {
				util += tput.get(throughput.indexOf(curService));
			}
			util = util / res.getNumberOfServers();
			//IMPORTANT: only scale the resource demands if util > 0, otherwise NaNs result
			if (util > 0) {
				double correctionFactor = targetUtilization / util;
				
				for (Service curService : accessingServices) {
					int idx = stateModel.getStateVariableIndex(res, curService);
					initialState[idx] = initialState[idx] * correctionFactor; 
				}
			}
		}	

		// assume each resource has the same initial demands
		return vector(initialState);
	}
}
