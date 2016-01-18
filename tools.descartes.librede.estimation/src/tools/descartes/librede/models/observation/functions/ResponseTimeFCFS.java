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

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.Time;

public class ResponseTimeFCFS extends AbstractDirectOutputFunction {
	
	private Query<Scalar, Time> responseTimeQuery;
	private Query<Scalar, RequestCount> queueLengthQuery;

	public  ResponseTimeFCFS(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Resource resource, Service service, int historicInterval) {
		super(stateModel, resource, service, historicInterval);
		
		responseTimeQuery = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forService(service).average().using(repository);
		queueLengthQuery = QueryBuilder.select(StandardMetrics.QUEUE_LENGTH_SEEN_ON_ARRIVAL).in(RequestCount.REQUESTS).forResource(resource).average().using(repository);
		addDataDependency(responseTimeQuery);
		addDataDependency(queueLengthQuery);
	}
	
	@Override
	public double getObservedOutput() {
		return responseTimeQuery.get(historicInterval).getValue();
	}

	@Override
	public double getFactor() {
		return queueLengthQuery.get(historicInterval).plus(1).getValue();
	}
	
	@Override
	public boolean hasData() {
		return responseTimeQuery.hasData(historicInterval) && queueLengthQuery.hasData(historicInterval);
	}

}
