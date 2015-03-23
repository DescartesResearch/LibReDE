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

import java.util.List;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;

/**
 * This output function describes the relationship between the per-service utilization and the resource demands. 
 * It implements the Service Demand Law:
 * 
 * D_{i,r} = \frac{U_{i,r}}{X_{r}}
 * 
 * with
 * <ul>
 *  <li>D_{i,r} is the resource demand of resource i and service r</li>
 *  <li>U_{i,r} is the utilization at resource i due to service r</li>
 *  <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * The per-service utilization U_{i,r} is calculated from the aggregate utilization U_{i} according to the
 * following apportioning scheme:
 * 
 * U_{i,r} = \frac{U_{i} * R_{r} * X_{r}}{\sum_{v = 1}^{N} R_{v} * X_{v}}
 * 
 * with
 * <ul>
 *  <li>U_{i,r} is the utilization at resource i due to service r</li>
 *  <li>U_{i} is the aggregate utilization at resource i</li>
 *  <li>R_{r} is the average response time of service r</li>
 *  <li>X_{r} is the average throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class ServiceDemandLaw extends AbstractDirectOutputFunction {
	
	private Resource res_i;
	private Service cls_r;
	
	private Query<Scalar, Ratio> utilizationQuery;
	private Query<Vector, Time> avgResponseTimeQuery;
	private Query<Vector, RequestRate> avgThroughputQuery;
	private Query<Scalar, RequestRate> avgThroughputQueryCurrentService;
	
	
	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel - the description of the state
	 * @param repository - the view of the repository with current measurement data
	 * @param service - the service for which the utilization is calculated
	 * @param resource - the resource for which the utilization is calculated
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public ServiceDemandLaw(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Resource resource,
			Service service) {
		super(stateModel, resource, service);
		
		res_i = resource;
		cls_r = service;
		
		utilizationQuery = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResource(res_i).average().using(repository);
		avgResponseTimeQuery = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forAllServices().average().using(repository);
		avgThroughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forAllServices().average().using(repository);
		avgThroughputQueryCurrentService = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forService(service).average().using(repository);
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#isApplicable()
	 */
	@Override
	public boolean isApplicable(List<String> messages) {
		boolean result = true;
		result = result && checkQueryPrecondition(utilizationQuery, messages);
		result = result && checkQueryPrecondition(avgResponseTimeQuery, messages);
		result = result && checkQueryPrecondition(avgThroughputQuery, messages);
		return result;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		/*
		 * We only get the aggregate utilization of a resource. In order to apportion this utilization between
		 * services, we assume R ~ D.
		 */
		Vector R = avgResponseTimeQuery.execute();
		Vector X = avgThroughputQuery.execute();
		double R_r = R.get(avgResponseTimeQuery.indexOf(cls_r));
		double X_r = X.get(avgThroughputQuery.indexOf(cls_r));
		double U_i = utilizationQuery.execute().getValue();
		int p = res_i.getNumberOfServers();
		
		return p * U_i * (R_r * X_r) / (R.dot(X));
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IDirectOutputFunction#getFactor()
	 */
	@Override
	public double getFactor() {
		return avgThroughputQueryCurrentService.execute().getValue();
	}

}
