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
import tools.descartes.librede.repository.Aggregation;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.StandardMetric;
import tools.descartes.librede.workload.WorkloadDescription;

/**
 * This output function approximates the resource demands with the observed response times (min, max, or mean) of a service.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class ResponseTimeApproximation extends AbstractDirectOutputFunction {
	
	private final Service cls_r;
	
	private final Query<Scalar> individualResponseTimesQuery;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param system the model of the system
	 * @param repository the repository with current measurement data
	 * @param service the service for which the response time is calculated
	 * @param resource the resource for which the response time is calculated
	 * @param aggregation specifies whether average, minimum or maximum of the observed response time is used as approximation for the resource demand.
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public ResponseTimeApproximation(WorkloadDescription system, IRepositoryCursor repository, Resource resource,
			Service service, Aggregation aggregation) {
		super(system, resource, service);
		
		cls_r = service;
		
		switch(aggregation) {
		case AVERAGE:
			individualResponseTimesQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(cls_r).average().using(repository);
			break;
		case MAXIMUM:
			individualResponseTimesQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(cls_r).max().using(repository);
			break;
		case MINIMUM:
			individualResponseTimesQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(cls_r).min().using(repository);
			break;
		default:
			throw new IllegalArgumentException();
		}		
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#isApplicable()
	 */
	@Override
	public boolean isApplicable(List<String> messages) {
		boolean result = true;
		result = result && checkQueryPrecondition(individualResponseTimesQuery, messages);
		return result;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IDirectOutputFunction#getFactor()
	 */
	@Override
	public double getFactor() {
		// approximate response times directly with resource demands --> R = 1.0 * D
		return 1.0;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return individualResponseTimesQuery.execute().getValue();
	}

}