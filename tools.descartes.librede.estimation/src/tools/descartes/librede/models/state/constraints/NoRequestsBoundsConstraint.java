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

import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.diff.IDifferentiableFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.StateVariable;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.RequestRate;

/**
 * This bounds constraints check if throughput is larger than zero for the
 * specified state variable. If not, the demand is required to be zero. Otherwise
 * a predefined value is used.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class NoRequestsBoundsConstraint implements IStateBoundsConstraint, IDifferentiableFunction {
	
	private Query<Scalar, RequestRate> throughputQuery;	
	private final double lowerBound;	
	private final double upperBound;	
	private final StateVariable variable;
	private IStateModel<? extends IStateConstraint> stateModel;
	
	public NoRequestsBoundsConstraint(Resource resource, Service service, IRepositoryCursor cursor, double lowerBound, double upperBound) {
		this.lowerBound = lowerBound;
		this.upperBound = upperBound;
		this.variable = new StateVariable(resource, service);
		this.throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forService(service).average().using(cursor);
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
	public boolean isApplicable(List<String> messages) {
		if (!throughputQuery.isExecutable()) {
			StringBuilder msg = new StringBuilder("DATA PRECONDITION: ");
			msg.append("metric = ").append(throughputQuery.getMetric().toString()).append(" ");
			msg.append("entities = { ");
			for(ModelEntity entity : throughputQuery.getEntities()) {
				msg.append(entity.getName()).append(" ");
			}
			msg.append(" } ");
			messages.add(msg.toString());
			return false;
		}
		return true;
	}

	@Override
	public void setStateModel(IStateModel<? extends IStateConstraint> model) {
		this.stateModel = model;
	}

	@Override
	public Vector getFirstDerivatives(Vector x) {
		return null;
	}

	@Override
	public Matrix getSecondDerivatives(Vector x) {
		return null;
	}

	@Override
	public StateVariable getStateVariable() {
		return this.variable;
	}

}
