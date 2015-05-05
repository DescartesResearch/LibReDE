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

import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.List;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.diff.IDifferentiableFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;

/**
 * This output function describes the relationship between the mean response
 * time and the resource demands. It implements the following equation:
 * 
 * R_{r} = \sum_{i = 1}{K} \frac{D_{i,r}}{1 - \sum_{v = 1}^{N} X_{v} * D{i,v}}
 * 
 * with
 * <ul>
 * <li>R_{r} is the response time of service r</li>
 * <li>K is the number of resources</li>
 * <li>N is the number of services</li>
 * <li>D_{i,r} is the resource demand of resource i and service r</li>
 * <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class ResponseTimeEquation extends AbstractOutputFunction implements IDifferentiableFunction {

	private static final ErlangCEquation erlangC = new ErlangCEquation();

	private double[] factorials;

	private Service cls_r;

	private boolean useObservedUtilization;

	private Query<Vector, Ratio> utilQuery;
	private Query<Scalar, Time> responseTimeQuery;
	private Query<Vector, RequestRate> throughputQuery;

	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel
	 *            - the description of the state
	 * @param repository
	 *            - a view of the repository with current measurement data
	 * @param service
	 *            - the service for which the response time is calculated
	 * @param useObservedUtilization
	 *            - a flag whether to use observed utilization values or
	 *            calculated
	 * 
	 * @throws NullPointerException
	 *             if any parameter is null
	 * @throws IllegalArgumentException
	 *             if the list of services or resources is empty
	 */
	public ResponseTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Service service, boolean useObservedUtilization) {
		this(stateModel, repository, service, useObservedUtilization, 0);
	}

	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel
	 *            - the description of the state
	 * @param repository
	 *            - a view of the repository with current measurement data
	 * @param service
	 *            - the service for which the response time is calculated
	 * @param useObservedUtilization
	 *            - a flag whether to use observed utilization values or
	 *            calculated
	 * @param historicInterval
	 *            - specifies the number of intervals this function is behind in
	 *            the past.
	 * 
	 * @throws NullPointerException
	 *             if any parameter is null
	 * @throws IllegalArgumentException
	 *             if the list of services or resources is empty
	 */
	public ResponseTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Service service, boolean useObservedUtilization, int historicInterval) {
		super(stateModel, historicInterval);

		cls_r = service;
		this.useObservedUtilization = useObservedUtilization;

		// int maxParallel = 1;
		// for (Resource res : selectedResources) {
		// maxParallel = Math.max(maxParallel,
		// res.getNumberOfParallelServers());
		// }
		// precalculateFactorials(maxParallel);

		if (useObservedUtilization) {
			utilQuery = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE)
					.forResources(stateModel.getResources()).average().using(repository);
		}

		responseTimeQuery = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forService(service)
				.average().using(repository);
		/*
		 * IMPORTANT: Query throughput for all services (including background
		 * services) The repository should return 1 as throughput for background
		 * services.
		 */
		throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND)
				.forServices(stateModel.getAllServices()).average().using(repository);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.functions.IOutputFunction#
	 * isApplicable()
	 */
	@Override
	public boolean isApplicable(List<String> messages) {
		boolean result = true;
		result = result && checkQueryPrecondition(responseTimeQuery, messages);
		result = result && checkQueryPrecondition(throughputQuery, messages);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.functions.IOutputFunction#
	 * getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return responseTimeQuery.get(historicInterval).getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * tools.descartes.librede.models.observation.functions.IOutputFunction#
	 * getCalculatedOutput(tools.descartes.librede.linalg.Vector)
	 */
	@Override
	public double getCalculatedOutput(Vector state) {
		double rt = 0.0;
		Vector X = throughputQuery.get(historicInterval);
		double X_r = X.get(throughputQuery.indexOf(cls_r));

		for (Resource res_i : getStateModel().getResources()) {
			double U_i = getUtilization(historicInterval, res_i, state, X);
			double D_ir = state.get(getStateModel().getStateVariableIndex(res_i, cls_r));
			int p = res_i.getNumberOfServers();
			double busyProp = erlangC.calculateValue(p, U_i);

			rt += D_ir + calculateQueueingTime(state, res_i, U_i, busyProp);
		}
		return rt;
	}

	/**
	 * @param state
	 * @param res_i - the resource i
	 * @param U_i - the utilization of resource i
	 * @param P_q - the probability that all servers are occupied when a new job arrives.
	 * @return
	 */
	private double calculateQueueingTime(Vector state, Resource res_i, double U_i,  double P_q) {
		switch(res_i.getSchedulingStrategy()) {
		case FCFS:
			// TODO: implement FCFS
		case PS:
		case UNKOWN:
			/*
			 * The mean queue length of a single-class, multi-server queue is
			 * 
			 * E[T_q] = \frac{1}{\lambda} * \frac{U_i}{1 - U_i} * P_q
			 * 
			 * (see Harchol-Balter, "Performance Modeling and Design of Computer Systems", p. 262)
			 * 
			 * For PS scheduling (in contrast to FCFS) this also holds for multi-class queues.
			 * 
			 * This formula can be reformulated to
			 * 
			 *  E[T_q] = \frac{D_ir}{1 - U_i} * P_q
			 */
			double D_ir =  state.get(getStateModel().getStateVariableIndex(res_i, cls_r));
			return (D_ir * P_q) / (1 - U_i);
		case IS:
			/*
			 * Infinite server: a job will never be forced to wait for service
			 */
			return 0.0;
		default:
			throw new AssertionError("Unsupported scheduling strategy.");	
		}
		
	}

	private double getUtilization(int historicInterval, Resource res_i, Vector state, Vector X) {
		if (useObservedUtilization) {
			return utilQuery.get(historicInterval).get(utilQuery.indexOf(res_i));
		} else {
			/*
			 * Calculate the utilization using the utilization law.
			 */
			double U_i = 0;
			for (int i = 0; i < X.rows(); i++) {
				Service curService = (Service) throughputQuery.getEntity(i);
				U_i += state.get(getStateModel().getStateVariableIndex(res_i, curService)) * X.get(i);
			}
			return U_i / res_i.getNumberOfServers();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tools.descartes.librede.models.diff.IDifferentiableFunction#
	 * getFirstDerivatives(tools.descartes.librede.linalg.Vector)
	 */
	@Override
	public Vector getFirstDerivatives(final Vector state) {
		return vector(state.rows(), new VectorFunction() {
			@Override
			public double cell(int row) {
				/*
				 * This function calculates the derivatives of the response time
				 * equation i.e. cell(row = \frac{df}{dx_{row}} where x is the
				 * vector of resource demands The following indexes are used: i
				 * - index of the resource corresponding to x_{row} s - index of
				 * the service corresponding to x_{row}
				 */
				Resource res_i = getStateModel().getResource(row);
				Service cls_s = getStateModel().getService(row);

				// get current throughput data
				Vector X = throughputQuery.get(historicInterval);
				double U_i = getUtilization(historicInterval, res_i, state, X);		


				double dev = 0.0;
				if (cls_r.equals(cls_s)) {
					dev += 1;
				}
				return getFirstDerivationOfQueueingTime(state, res_i, cls_s, X, U_i, dev);
			}		
		});
	}
	
	private double getFirstDerivationOfQueueingTime(final Vector state, Resource res_i, Service cls_s, Vector X,
			double U_i, double dev) throws AssertionError {
		switch(res_i.getSchedulingStrategy()) {
		case FCFS:
			// TODO: implement FCFS
		case PS:
		case UNKOWN:
			/*
			 * When calculating derivatives, we need to distinguish between
			 * two cases: 1. We use the observed utilization -> it is a
			 * linear function with simple derivation 2. We use the
			 * utilization law to calculate the utilization -> quotient rule
			 * needs to be applied
			 */			
			double P_q = erlangC.calculateValue(res_i.getNumberOfServers(), U_i);
			double beta = 1 - U_i;
			
			/*
			 * Calculate: (D_ir * \frac{P_q}{1 - U_i})'
			 */
			if (cls_s.equals(cls_r)) {
				dev += P_q / beta;
			}
			if (!useObservedUtilization) {
				double X_s = X.get(throughputQuery.indexOf(cls_s));
				double devP_q = erlangC.calculateFirstDerivative(res_i.getNumberOfServers(), U_i, X_s);
				/*
				 * U_i and P_q are also functions of the state. Therefore, we need
				 * to apply the quotient rule. The first addend of the quotient rule
				 * has already been calculated above.
				 * 
				 * u = D_ir
				 * v = \frac{P_q}{1 - U_i}
				 * 
				 */
				double D_ir = state.get(getStateModel().getStateVariableIndex(res_i, cls_r));
				dev += D_ir * deriveQueueingFactor(beta, X_s, P_q, devP_q);
			}			
			return dev;
		case IS:
			return 1.0;
		default:
			throw new AssertionError("Unsupported scheduling strategy.");	
		}
	}
	
	/*
	 * Calculates: F_q = (\frac{P_q}{beta})' with beta = 1 - U_i
	 */
	private double deriveQueueingFactor(double beta, double X_s, double P_q, double devP_q) {
		return (devP_q / beta +  (P_q * X_s) / (beta * beta));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see tools.descartes.librede.models.diff.IDifferentiableFunction#
	 * getSecondDerivatives(tools.descartes.librede.linalg.Vector)
	 */
	@Override
	public Matrix getSecondDerivatives(final Vector state) {
		return matrix(state.rows(), state.rows(), new MatrixFunction() {
			@Override
			public double cell(int row, int column) {
				/*
				 * This function calculates the second partial derivative of the
				 * response time equation i.e. cell(row, column) =
				 * \frac{df}{dx_{row}dx_{column}} where x is the vector of
				 * resource demands. The following indexes are used: i - index
				 * of the resource corresponding to x_{row} s - index of the
				 * service corresponding to x_{row} j - index of the resource
				 * corresponding to x_{column} t - index of the service
				 * corresponding to x_{column}
				 */
				Resource res_i = getStateModel().getResource(row);
				Service cls_s = getStateModel().getService(row);
				Resource res_j = getStateModel().getResource(column);
				Service cls_t = getStateModel().getService(column);
				
				// if resources of x_{row} and x_{column} do not match the second
				// derivative is zero
				// if we use observed utilization it is only a linear function -> second
				// derivative is zero
				if (!useObservedUtilization && res_i.equals(res_j)) {

		
					return getSecondDerivationOfWaitingTime(state, res_i, cls_s, cls_t);
				}
				return 0.0;
			}

			private double getSecondDerivationOfWaitingTime(final Vector state, Resource res_i, Service cls_s,
					Service cls_t) throws AssertionError {
				/*
				 * Calculates two step derivation: d_xis * d_xit (D_ir * \frac{P_q}{1 - U_i})
				 */
				
				// get current throughput data
				Vector X = throughputQuery.get(historicInterval);
				double X_s = X.get(throughputQuery.indexOf(cls_s));
				double X_t = X.get(throughputQuery.indexOf(cls_t));
				
				double U_i = getUtilization(historicInterval, res_i, state, X);

				switch(res_i.getSchedulingStrategy()) {
				case FCFS:
					// TODO: implement FCFS scheduling
				case PS:
				case UNKOWN:
					int p = res_i.getNumberOfServers();
					double P_q = erlangC.calculateValue(p, U_i);					
					double devsP_q = erlangC.calculateFirstDerivative(p, U_i, X_s);
					double devtP_q = erlangC.calculateFirstDerivative(p, U_i, X_t);
					double devsdevtP_q = erlangC.calculateSecondDerivative(p, U_i, X_s);
					
					double beta = 1 - U_i;
					double D_ir = state.get(getStateModel().getStateVariableIndex(res_i, cls_r));
					double dev = D_ir * (devsdevtP_q / beta + (devsP_q * X_t  + devtP_q * X_s) / (beta * beta) + (P_q * 2 * X_s * X_t) / (beta * beta * beta));			
					if (cls_r.equals(cls_s)) {
						dev += deriveQueueingFactor(beta, X_t, P_q, devtP_q);
					}
					if (cls_r.equals(cls_t)) {
						dev += deriveQueueingFactor(beta, X_s, P_q, devsP_q);
					}					
					return dev;
				case IS:
					return 0.0;
				default:
					throw new AssertionError("Unsupported scheduling strategy.");
				}
			}
		});
	}
}
