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
package tools.descartes.librede.models.observation.functions;

import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.models.variables.OutputVariable;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Time;

/**
 * This output function describes the relationship between the mean response
 * time and the resource demands. It implements the following equation:
 * 
 * R_{r} = \sum_{i = 1}{K} \frac{D_{i,r}}{1 - \sum_{v = 1}^{N} X_{v} * D{i,v}}
 * 
 * with
 * <ul>
 * <li>R_{r} is the response time of service r</li>
 * <li>K is the number of resources</li>
 * <li>N is the number of services</li>
 * <li>D_{i,r} is the resource demand of resource i and service r</li>
 * <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class ResponseTimeEquation extends AbstractOutputFunction {

	protected final Service cls_r;

	protected final Query<Scalar, Time> responseTimeQuery;
	
	private final tools.descartes.librede.models.observation.queueingmodel.ResponseTimeEquation respTimeEquation;

	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel
	 *            - the description of the state
	 * @param repository
	 *            - a view of the repository with current measurement data
	 * @param service
	 *            - the service for which the response time is calculated
	 * @param useObservedUtilization
	 *            - a flag whether to use observed utilization values or
	 *            calculated
	 * 
	 * @throws NullPointerException
	 *             if any parameter is null
	 * @throws IllegalArgumentException
	 *             if the list of services or resources is empty
	 */
	public ResponseTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Service service, boolean useObservedUtilization) {
		this(stateModel, repository, service, useObservedUtilization, 0);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel
	 *            - the description of the state
	 * @param repository
	 *            - a view of the repository with current measurement data
	 * @param service
	 *            - the service for which the response time is calculated
	 * @param useObservedUtilization
	 *            - a flag whether to use observed utilization values or
	 *            calculated
	 * @param historicInterval
	 *            - specifies the number of intervals this function is behind in
	 *            the past.
	 * 
	 * @throws NullPointerException
	 *             if any parameter is null
	 * @throws IllegalArgumentException
	 *             if the list of services or resources is empty
	 */
	public ResponseTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Service service, boolean useObservedUtilization, int historicInterval) {
		super(stateModel, historicInterval);

		respTimeEquation = new tools.descartes.librede.models.observation.queueingmodel.ResponseTimeEquation(stateModel, repository, service, useObservedUtilization, historicInterval);
		cls_r = service;
		responseTimeQuery = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forService(service)
				.average().using(repository);
		addDataDependency(responseTimeQuery);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.functions.IOutputFunction#
	 * getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		double rt = responseTimeQuery.get(historicInterval).getValue();
		return (rt != rt) ? 0.0 : rt;
	}

	@Override
	public OutputVariable getCalculatedOutput(State state) {
		return new OutputVariable(state, respTimeEquation.getValue(state));
	}

	@Override
	public boolean hasData() {
		boolean ret = responseTimeQuery.hasData(historicInterval) && responseTimeQuery.hasData();
		return ret;
	}
	
//	public static class LinearVariant extends ResponseTimeEquation implements ILinearOutputFunction {
//
//		public LinearVariant(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
//				Service service) {
//			super(stateModel, repository, service, true);
//		}
//
//		@Override
//		public Vector getIndependentVariables() {
//			Vector X = throughputQuery.get(historicInterval);
//			if (X.get(throughputQuery.indexOf(cls_r)) == 0.0) {
//				// no request observed in this interval
//				return zeros(getStateModel().getStateSize());
//			}
//
//			Vector result = zeros(getStateModel().getStateSize());
//			for (Resource res_i : accessedResources.keySet()) {
//				Map<Service, ResidenceTimeEquation> accessingServices = residenceTimeEquations.get(res_i);
//				for (Service curService : accessingServices.keySet()) {
//					double visits = 1;
//					if (!curService.equals(cls_r)) {
//						visits = invocations.getInvocationCount(cls_r, curService, historicInterval);
//					}
//					ResidenceTimeEquation R_ir = accessingServices.get(curService);
//					DerivativeStructure curRt = R_ir.getLinearResidenceTimeFactors(state).multiply(visits);
//					if (rt == null) {
//						rt = curRt;
//					} else {
//						rt = rt.add(curRt);
//					}
//				}
//			}
//			return result;
//		}
//		
//	}
}
