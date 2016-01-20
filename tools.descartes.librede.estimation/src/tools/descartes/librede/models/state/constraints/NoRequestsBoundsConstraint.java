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
package tools.descartes.librede.models.state.constraints;

import java.util.Collections;
import java.util.List;

import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.repository.rules.DataDependency;
import tools.descartes.librede.repository.rules.DependencyScope;
import tools.descartes.librede.units.RequestRate;

/**
 * This bounds constraints check if throughput is larger than zero for the
 * specified state variable. If not, the demand is required to be zero. Otherwise
 * a predefined value is used.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class NoRequestsBoundsConstraint implements IStateBoundsConstraint {
	
	private Query<Scalar, RequestRate> throughputQuery;	
	private final double lowerBound;	
	private final double upperBound;	
	private final ResourceDemand variable;
	private IStateModel<? extends IStateConstraint> stateModel;
	
	public NoRequestsBoundsConstraint(ResourceDemand demand, IRepositoryCursor cursor, double lowerBound, double upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.variable = demand;
		this.throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forService(demand.getService()).average().using(cursor);
	}

	@Override
	public double getLowerBound() {
		return (throughputQuery.execute().get(0) == 0.0) ? 0.0 : lowerBound;
	}

	@Override
	public double getUpperBound() {
		return (throughputQuery.execute().get(0) == 0.0) ? 0.0 : upperBound;
	}

	@Override
	public double getValue(Vector state) {
		if (stateModel == null) {
			throw new IllegalStateException();
		}
		return state.get(stateModel.getStateVariableIndex(variable.getResource(), variable.getService()));
	}

	@Override
	public void setStateModel(IStateModel<? extends IStateConstraint> model) {
		this.stateModel = model;
	}

	@Override
	public ResourceDemand getStateVariable() {
		return this.variable;
	}

	@Override
	public List<DataDependency<?>> getDataDependencies() {
		return Collections.<DataDependency<?>>singletonList(new DataDependency<>(throughputQuery.getMetric(), throughputQuery.getAggregation(), DependencyScope.fixedScope(throughputQuery.getEntities())));
	}

}
