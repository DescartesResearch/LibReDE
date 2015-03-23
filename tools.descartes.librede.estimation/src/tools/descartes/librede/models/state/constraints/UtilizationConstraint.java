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

import static tools.descartes.librede.linalg.LinAlg.zeros;

import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.diff.IDifferentiableFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;

public class UtilizationConstraint implements ILinearStateConstraint, IDifferentiableFunction {

	private Resource res_i;
	
	private IStateModel<? extends IStateConstraint> stateModel;
	
	private Query<Vector, RequestRate> throughputQuery;
	
	public UtilizationConstraint(Resource resource, IRepositoryCursor repository) {
		this.res_i = resource;
		
		throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forAllServices().average().using(repository);
	}
	
	@Override
	public double getLowerBound() {
		return 0;
	}

	@Override
	public double getUpperBound() {
		return 1;
	}

	@Override
	public double getValue(Vector state) {
		if (stateModel == null) {
			throw new IllegalStateException();
		}
		Vector D_i = state.slice(stateModel.getStateVariableIndexRange(res_i));
		Vector X = throughputQuery.execute();
		return X.dot(D_i);
	}

	@Override
	public Vector getFirstDerivatives(Vector x) {
		return throughputQuery.execute();
	}

	@Override
	public Matrix getSecondDerivatives(Vector x) {
		return zeros(x.rows(), x.rows());
	}
	
	@Override
	public boolean isApplicable(List<String> messages) {
		if (!throughputQuery.hasData()) {
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

}
