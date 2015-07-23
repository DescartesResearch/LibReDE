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

import static tools.descartes.librede.linalg.LinAlg.indices;
import static tools.descartes.librede.linalg.LinAlg.zeros;

import java.util.List;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.linalg.Indices;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;

/**
 * This output function implements the Utilization Law:
 * 
 * U_{i} = \sum_{r = 1}^{N} X_{r} * D_{i,r}
 * 
 * with
 * <ul>
 * 	<li>U_{i} is the utilization of resource i
 * 	<li>N is the number of services</li>
 *  <li>D_{i,r} is the resource demand of resource i and service r</li>
 *  <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class UtilizationLaw extends AbstractLinearOutputFunction {
	
	private Resource res_i;
	
	private final Query<Vector, RequestRate> throughputQuery;
	private final Query<Scalar, Ratio> utilizationQuery;
	
	private final Vector variables; // vector of independent variables which is by default set to zero. The range varFocusedIndices is updated later.
	private final Indices varFocusedIndices; // the indices of the independent variables which is altered by this output function

	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel - the description of the state
	 * @param repository - the repository with current measurement data
	 * @param resource - the resource for which the utilization is calculated
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public UtilizationLaw(final IStateModel<? extends IStateConstraint> stateModel, final IRepositoryCursor repository,
			final Resource resource) {
		this(stateModel, repository, resource, 0);
	}
	
	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel - the description of the state
	 * @param repository - the repository with current measurement data
	 * @param resource - the resource for which the utilization is calculated
	 * @param historicInterval - specifies the number of intervals this function is behind in the past.
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 */
	public UtilizationLaw(final IStateModel<? extends IStateConstraint> stateModel, final IRepositoryCursor repository,
			final Resource resource, int historicInterval) {
		super(stateModel, historicInterval);
		
		this.res_i = resource;
		
		variables = zeros(stateModel.getStateSize());
		varFocusedIndices = indices(resource.getAccessingServices().size(), new VectorFunction() {
			@Override
			public double cell(int row) {
				return stateModel.getStateVariableIndex(resource, resource.getAccessingServices().get(row));
			}
		});
		
		/*
		 * IMPORTANT: we query the throughput for all services (including background services). For background services
		 * the repository should by default return 1 as throughput (i.e. constant background work).
		 */
		throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(resource.getAccessingServices()).average().using(repository);
		utilizationQuery = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResource(res_i).average().using(repository);
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#isApplicable()
	 */
	@Override
	public boolean isApplicable(List<String> messages) {
		boolean result = true;
		result = result && checkQueryPrecondition(throughputQuery, messages);
		result = result && checkQueryPrecondition(utilizationQuery, messages);
		return result;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.ILinearOutputFunction#getIndependentVariables()
	 */
	@Override
	public Vector getIndependentVariables() {
		Vector X = throughputQuery.get(historicInterval);
		return variables.set(varFocusedIndices, X.times(1.0 / this.res_i.getNumberOfServers()));
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return utilizationQuery.get(historicInterval).getValue();
	}
	
	@Override
	public boolean hasData() {
		return throughputQuery.hasData(historicInterval) && utilizationQuery.hasData(historicInterval);
	}
}
