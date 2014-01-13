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
package edu.kit.ipd.descartes.librede.estimation.models.observation.functions;

import edu.kit.ipd.descartes.librede.estimation.repository.Query;
import edu.kit.ipd.descartes.librede.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;

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
 * @author Simon Spinner (simon.spinner@kit.edu)
 * @version 1.0
 */
public class ServiceDemandLaw extends AbstractDirectOutputFunction {
	
	private Resource res_i;
	private Service cls_r;
	
	private Query<Scalar> utilizationQuery;
	private Query<Vector> avgResponseTimeQuery;
	private Query<Vector> avgThroughputQuery;
	private Query<Scalar> avgThroughputQueryCurrentService;
	
	
	/**
	 * Creates a new instance.
	 * 
	 * @param system - the model of the system
	 * @param repository - the view of the repository with current measurement data
	 * @param service - the service for which the utilization is calculated
	 * @param resource - the resource for which the utilization is calculated
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public ServiceDemandLaw(WorkloadDescription system, RepositoryCursor repository,
			Resource resource,
			Service service) {
		super(system, resource, service);
		
		res_i = resource;
		cls_r = service;
		
		utilizationQuery = QueryBuilder.select(StandardMetric.UTILIZATION).forResource(res_i).average().using(repository);
		avgResponseTimeQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forAllServices().average().using(repository);
		avgThroughputQuery = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(repository);
		avgThroughputQueryCurrentService = QueryBuilder.select(StandardMetric.THROUGHPUT).forService(service).average().using(repository);
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction#isApplicable()
	 */
	@Override
	public boolean isApplicable() {
		return utilizationQuery.hasData() && avgResponseTimeQuery.hasData() && avgThroughputQuery.hasData();
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction#getObservedOutput()
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
		int p = res_i.getNumberOfParallelServers();
		
		return p * U_i * (R_r * X_r) / (R.dot(X));
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IDirectOutputFunction#getFactor()
	 */
	@Override
	public double getFactor() {
		return avgThroughputQueryCurrentService.execute().getValue();
	}

}
