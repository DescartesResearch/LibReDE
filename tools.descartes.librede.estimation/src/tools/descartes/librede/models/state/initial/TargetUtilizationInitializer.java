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

import static tools.descartes.librede.linalg.LinAlg.sum;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.List;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.RequestRate;

/**
 * This class initializes a state model with demands so that a specified target
 * utilization is reached for the throughput values observed at the system so
 * far. The resulting utilization does not need to match the actual system
 * utilization, it should be chosen so that it is a good starting point for an
 * estimation algorithm.
 * 
 * NOTE: The initial demands for all services will be equal.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class TargetUtilizationInitializer implements IStateInitializer {

	private final double targetUtilization;
	private final IRepositoryCursor cursor;

	/**
	 * @param targetUtilization the requested initial target utilization
	 * @param cursor handle to the monitoring data
	 */
	public TargetUtilizationInitializer(double targetUtilization, IRepositoryCursor cursor) {
		this.cursor = cursor;
		this.targetUtilization = targetUtilization;
	}

	@Override
	public Vector getInitialValue(IStateModel<?> stateModel) {
		Query<Vector, RequestRate> throughput = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(stateModel.getServices()).average().using(cursor);
		double totalThroughput = sum(throughput.execute());
		final double initialDemand = targetUtilization / totalThroughput;
		return vector(stateModel.getStateSize(), new VectorFunction() {
			@Override
			public double cell(int row) {
				return initialDemand;
			}
		});
	}

}
