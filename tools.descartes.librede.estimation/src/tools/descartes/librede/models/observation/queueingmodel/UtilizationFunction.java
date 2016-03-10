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
package tools.descartes.librede.models.observation.queueingmodel;

import static tools.descartes.librede.linalg.LinAlg.indices;
import static tools.descartes.librede.linalg.LinAlg.zeros;

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
 * This class can be used to obtain the utilization of a resource.
 * 
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class UtilizationFunction extends LinearModelEquation {

	// The resource of which the utilization is returned
	private final Resource res_i;
	
	private final Query<Vector, RequestRate> throughputQuery;
	private final Query<Scalar, Ratio> contentionQuery;
	
	private final Vector variables; // vector of independent variables which is by default set to zero. The range varFocusedIndices is updated later.
	private final Indices varFocusedIndices; // the indices of the independent variables which is altered by this output function

	/**
	 * Constructor.
	 * 
	 * @param stateModel
	 * @param cursor
	 *            the position in the monitoring data
	 * @param resource
	 *            the resource of which utilization is requested
	 * @param historicInterval
	 *            the monitoring interval for which to calculate the utilization
	 *            (0 == current)
	 * @param calculated
	 *            A flag indicated whether to use observed or calculated
	 *            utilization values
	 */
	public UtilizationFunction(final IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor cursor,
			Resource resource, int historicInterval) {
		super(stateModel, historicInterval);
		this.res_i = resource;
		
		variables = zeros(stateModel.getStateSize());
		varFocusedIndices = indices(resource.getAccessingServices().size(), new VectorFunction() {
			@Override
			public double cell(int row) {
				return stateModel.getStateVariableIndex(res_i, res_i.getAccessingServices().get(row));
			}
		});
		
		/*
		 * IMPORTANT: we query the throughput for all services (including background services). For background services
		 * the repository should by default return 1 as throughput (i.e. constant background work).
		 */
		throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(resource.getAccessingServices()).average().using(cursor);
		contentionQuery = QueryBuilder.select(StandardMetrics.CONTENTION).in(Ratio.NONE).forResource(res_i).average().using(cursor);
		addDataDependency(throughputQuery);
		addDataDependency(contentionQuery);
	}

	@Override
	public Vector getFactors() {
		Vector X = throughputQuery.get(historicInterval);
		// The contention factor specifies the slow down due to scheduling
		// on an underlying resource. As a result the actually available resource
		// time is lower, hence the utilization is higher
		// U_i = X * ((1 + C_i) * D)
		double C_i = contentionQuery.get(historicInterval).getValue();
		return variables.set(varFocusedIndices, X.times(1.0 / this.res_i.getNumberOfServers())).times(1 + C_i);
	}
	
	@Override
	public boolean hasData() {
		return throughputQuery.hasData();
	}

//	/**
//	 * This is an implementation of {@code UtilizationFunction} that returns the
//	 * currently observed utilization.
//	 * 
//	 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
//	 *
//	 */
//	private static class ObservedUtilizationFunction extends UtilizationFunction {
//
//		private final Query<Scalar, Ratio> utilQuery;
//
//		/**
//		 * Constructor.
//		 * 
//		 * @param cursor
//		 * @param resource
//		 * @param historicInterval
//		 */
//		public ObservedUtilizationFunction(IRepositoryCursor cursor, Resource resource, int historicInterval) {
//			super(cursor, resource, historicInterval);
//			this.utilQuery = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResource(resource)
//					.average().using(cursor);
//			addDataDependency(utilQuery);
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see tools.descartes.librede.models.observation.functions.helper.
//		 * UtilizationFunction#getUtilization(tools.descartes.librede.models.
//		 * State)
//		 */
//		@Override
//		public DerivativeStructure getUtilization(State state) {
//			return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), getValue());
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see tools.descartes.librede.models.observation.functions.helper.
//		 * UtilizationFunction#getValue(tools.descartes.librede.models.State)
//		 */
//		@Override
//		public double getValue() {
//			return utilQuery.get(historicInterval).getValue();
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see tools.descartes.librede.models.observation.functions.helper.
//		 * UtilizationFunction#isConstant()
//		 */
//		@Override
//		public boolean isConstant() {
//			return true;
//		}
//	}
//
//	/**
//	 * This is an implementation of {@code UtilizationFunction} that returns a
//	 * calculated utilization using the Utilization Law.
//	 * 
//	 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
//	 *
//	 */
//	private static class CalculatedUtilizationFunction extends UtilizationFunction {
//
//		private final Query<Scalar, Ratio> contentionQuery;
//		private final Query<Vector, RequestRate> throughputQuery;
//
//		/**
//		 * Constructor
//		 * 
//		 * @param cursor
//		 * @param resource
//		 * @param historicInterval
//		 */
//		public CalculatedUtilizationFunction(IRepositoryCursor cursor, Resource resource, int historicInterval) {
//			super(cursor, resource, historicInterval);
//			this.throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND)
//					.forServices(res_i.getAccessingServices()).average().using(cursor);
//			addDataDependency(throughputQuery);
//			this.contentionQuery = QueryBuilder.select(StandardMetrics.CONTENTION).in(Ratio.NONE).forResource(res_i)
//					.average().using(cursor);
//			addDataDependency(contentionQuery);
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see tools.descartes.librede.models.observation.queueingmodel.
//		 * IModelEquation#getValue(tools.descartes.librede.models.State)
//		 */
//		@Override
//		public DerivativeStructure getValue(State state) {
//			/*
//			 * Calculate the utilization using the utilization law.
//			 */
//
//			DerivativeStructure U_i = new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), 0.0);
//			for (Service curService : res_i.getAccessingServices()) {
//				int idx = throughputQuery.indexOf(curService);
//				U_i = U_i.add(state.getVariable(res_i, curService).getDerivativeStructure().multiply(X.get(idx)));
//			}
//			return U_i.divide(res_i.getNumberOfServers()).add(C_i);
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see tools.descartes.librede.models.observation.queueingmodel.
//		 * ILinearModelEquation#getFactors()
//		 */
//		@Override
//		public Vector getFactors() {
//			// sorted according to state variable ordering
//			double C_i = contentionQuery.get(historicInterval).getValue();
//			Vector X = throughputQuery.get(historicInterval);
//			return X;
//		}
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see tools.descartes.librede.models.observation.functions.helper.
//		 * UtilizationFunction#isConstant()
//		 */
//		@Override
//		public boolean isConstant() {
//			return false;
//		}
//	}

}
