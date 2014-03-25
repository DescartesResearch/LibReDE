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

import java.util.List;

import edu.kit.ipd.descartes.librede.estimation.repository.IRepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.Query;
import edu.kit.ipd.descartes.librede.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Scalar;

public class ResponseTimeFCFS extends AbstractDirectOutputFunction {
	
	private Query<Scalar> responseTimeQuery;
	private Query<Scalar> queueLengthQuery;

	public  ResponseTimeFCFS(WorkloadDescription system, IRepositoryCursor repository,
			Resource resource, Service service) {
		super(system, resource, service);
		
		responseTimeQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(service).average().using(repository);
		queueLengthQuery = QueryBuilder.select(StandardMetric.QUEUE_LENGTH_SEEN_ON_ARRIVAL).forResource(resource).average().using(repository);
	}

	@Override
	public boolean isApplicable(List<String> messages) {
		boolean result = true;
		result = result && checkQueryPrecondition(responseTimeQuery, messages);
		result = result && checkQueryPrecondition(queueLengthQuery, messages);
		return result;
	}

	@Override
	public double getObservedOutput() {
		return responseTimeQuery.execute().getValue();
	}

	@Override
	public double getFactor() {
		return queueLengthQuery.execute().plus(1).getValue();
	}

}
