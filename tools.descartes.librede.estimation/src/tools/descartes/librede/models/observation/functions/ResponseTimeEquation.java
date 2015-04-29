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
import static tools.descartes.librede.linalg.LinAlg.sum;
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
 * This output function describes the relationship between the mean response time and the resource demands. 
 * It implements the following equation:
 * 
 * R_{r} = \sum_{i = 1}{K} \frac{D_{i,r}}{1 - \sum_{v = 1}^{N} X_{v} * D{i,v}}
 * 
 * with
 * <ul>
 * 	<li>R_{r} is the response time of service r</li>
 * 	<li>K is the number of resources</li>
 * 	<li>N is the number of services</li>
 *  <li>D_{i,r} is the resource demand of resource i and service r</li>
 *  <li>X_{r} is the throughput of service r</li>
 * </ul>
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class ResponseTimeEquation extends AbstractOutputFunction implements IDifferentiableFunction {
	
	private double[] factorials;

	private Service cls_r;
	
	private boolean useObservedUtilization;
	
	private Query<Vector, Ratio> utilQuery;
	private Query<Scalar, Time> responseTimeQuery;
	private Query<Vector, RequestRate> throughputQuery;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel - the description of the state
	 * @param repository - a view of the repository with current measurement data
	 * @param service - the service for which the response time is calculated
	 * @param useObservedUtilization - a flag whether to use observed utilization values or calculated
	 * 
	 * @throws NullPointerException if any parameter is null
	 * @throws IllegalArgumentException if the list of services or resources is empty
	 */
	public ResponseTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Service service, boolean useObservedUtilization) {
		this(stateModel, repository, service, useObservedUtilization, 0);
	}
	/**
	 * Creates a new instance.
	 * 
	 * @param stateModel - the description of the state
	 * @param repository - a view of the repository with current measurement data
	 * @param service - the service for which the response time is calculated
	 * @param useObservedUtilization - a flag whether to use observed utilization values or calculated
	 * @param historicInterval - specifies the number of intervals this function is behind in the past.
	 * 
	 * @throws NullPointerException if any parameter is null
	 * @throws IllegalArgumentException if the list of services or resources is empty
	 */
	public ResponseTimeEquation(IStateModel<? extends IStateConstraint> stateModel, IRepositoryCursor repository,
			Service service, boolean useObservedUtilization, int historicInterval) {
		super(stateModel, historicInterval);
		
		cls_r = service;
		this.useObservedUtilization = useObservedUtilization;
		
//		int maxParallel = 1;
//		for (Resource res : selectedResources) {
//			maxParallel = Math.max(maxParallel, res.getNumberOfParallelServers());
//		}
//		precalculateFactorials(maxParallel);
		
		if (useObservedUtilization) {
			utilQuery = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResources(stateModel.getResources()).average().using(repository);
		}
		
		responseTimeQuery = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forService(service).average().using(repository);
		/*
		 * IMPORTANT: Query throughput for all services (including background services)
		 * The repository should return 1 as throughput for background services.
		 */
		throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(stateModel.getAllServices()).average().using(repository);
	}
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#isApplicable()
	 */
	@Override
	public boolean isApplicable(List<String> messages) {
		boolean result = true;
		result = result && checkQueryPrecondition(responseTimeQuery, messages);
		result = result && checkQueryPrecondition(throughputQuery, messages);
		return result;
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return responseTimeQuery.get(historicInterval).getValue();
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.observation.functions.IOutputFunction#getCalculatedOutput(tools.descartes.librede.linalg.Vector)
	 */
	@Override
	public double getCalculatedOutput(Vector state) {
		double rt = 0.0;
		Vector X = throughputQuery.get(historicInterval);
		double X_total = sum(X);
		
		for (Resource res_i : getStateModel().getResources()) {
			switch(res_i.getSchedulingStrategy()) {
			case PS:
			case UNKOWN:
				// For now we approximate unknown with PS
				rt += calculateResponseTimePS(historicInterval, state, res_i, X);
				break;
			case IS:
				rt += calculateResponseTimeIS(historicInterval, state, res_i, X);
				break;
			case FCFS:
				rt += calculateResponseTimeFCFS(historicInterval, state, res_i, X);
			default:
				throw new AssertionError("Unsupported scheduling strategy.");
			}
		}
		return rt;
	}
	
	private double calculateResponseTimeFCFS(int historicInterval, Vector state, Resource res_i, Vector X) {
		// TODO: Implement FCFS correctly.
		return calculateResponseTimePS(historicInterval, state, res_i, X);
	}
	
	private double calculateResponseTimeIS(int historicInterval, Vector state, Resource res_i, Vector X) {
		return state.get(getStateModel().getStateVariableIndex(res_i, cls_r));		
	}
	
	private double calculateResponseTimePS(int historicInterval, Vector state, Resource res_i, Vector X) {
		double D_ir = state.get(getStateModel().getStateVariableIndex(res_i, cls_r));
		double U_i = getUtilization(historicInterval, res_i, state, X);

		int p = res_i.getNumberOfServers();
//		double P_q = calculateQueueingProbability(p, U_i);
		
		return D_ir / (p - U_i);
	}
	
	private double getUtilization(int historicInterval, Resource res_i, Vector state, Vector X) {
		if (useObservedUtilization) {
			return utilQuery.get(historicInterval).get(utilQuery.indexOf(res_i));
		} else {
			double U_i = 0;
			for (int i = 0; i < X.rows(); i++) {
				Service curService = (Service) throughputQuery.getEntity(i);
				U_i += state.get(getStateModel().getStateVariableIndex(res_i, curService)) * X.get(i);
			}
			return U_i;
		} 
	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.diff.IDifferentiableFunction#getFirstDerivatives(tools.descartes.librede.linalg.Vector)
	 */
	@Override
	public Vector getFirstDerivatives(final Vector state) {
		return vector(state.rows(), new VectorFunction() {		
			@Override
			public double cell(int row) {
				/*
				 * This function calculates the derivatives of the response time equation
				 * i.e. cell(row = \frac{df}{dx_{row}} where x is the vector of resource demands
				 * The following indexes are used:
				 * i - index of the resource corresponding to x_{row}
				 * s - index of the service corresponding to x_{row}
				 */
				Resource res_i = getStateModel().getResource(row);
				Service cls_s = getStateModel().getService(row);
				
				switch(res_i.getSchedulingStrategy()) {
				case PS:
				case UNKOWN:
					return calculateFirstDerivativePS(historicInterval, state, res_i, cls_s);
				case IS:
					return calculateFirstDerivativeIS(historicInterval, state, res_i, cls_s);
				case FCFS:
					return calculateFirstDerivativeFCFS(historicInterval, state, res_i, cls_s);
				default:
					throw new AssertionError("Unsupported scheduling strategy.");
				}
			}
		});
	}
	
	private double calculateFirstDerivativeIS(int historicInterval, Vector state, Resource res_i, Service cls_s) {
		// It is just a linear function R = D --> 1 
		return 1;
	}
	
	private double calculateFirstDerivativeFCFS(int historicInterval, Vector state, Resource res_i, Service cls_s) {
		return calculateFirstDerivativePS(historicInterval, state, res_i, cls_s);
	}
		
	private double calculateFirstDerivativePS(int historicInterval, Vector state, Resource res_i, Service cls_s) {
		// get resource demands
		double D_ir = state.get(getStateModel().getStateVariableIndex(res_i, cls_r));
		
		// get current throughput data
		Vector X = throughputQuery.get(historicInterval);
		double X_s = X.get(throughputQuery.indexOf(cls_s));
		
		/*
		 * beta is a shorthand variable for the denominator of the response time equation
		 *  
		 * beta = 1 - \sum_{v = 1}^{R} X_{v} * D_{i,v}
		 */
		double beta = 1 - getUtilization(historicInterval, res_i, state, X);
		
		
		/*
		 * When calculating derivatives, we need to distinguish between two cases:
		 * 1. We use the observed utilization -> it is a linear function with simple derivation
		 * 2. We use the utilization law to calculate the utilization -> quotient rule needs to be applied
		 */
		double dev = 0.0;
		if (useObservedUtilization) {
			if (cls_r.equals(cls_s)) {
				dev += (1 / beta);
			}
		} else {
			/*
			 * Calculate derivatives:
			 * 
			 * \frac{D_{i,r * X_{s}}{beta^{2}} if r != s
			 * 
			 * \frac{D_{i,r * X_{s}}{beta^{2}} + \frac{1}{beta} if r == s				 * 			
			 */
			dev += (D_ir * X_s) / (beta * beta);
			if (cls_r.equals(cls_s)) {
				dev += (1 / beta);
			}
		}
		return dev;

	}

	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.diff.IDifferentiableFunction#getSecondDerivatives(tools.descartes.librede.linalg.Vector)
	 */
	@Override
	public Matrix getSecondDerivatives(final Vector state) {
		return matrix(state.rows(), state.rows(), new MatrixFunction() {			
			@Override
			public double cell(int row, int column) {
				/* 
				 * This function calculates the second partial derivative of the response time equation
				 * i.e. cell(row, column) = \frac{df}{dx_{row}dx_{column}} where x is the vector of resource demands.
				 * The following indexes are used:
				 * i - index of the resource corresponding to x_{row}
				 * s - index of the service corresponding to x_{row}
				 * j - index of the resource corresponding to x_{column}
				 * t - index of the service corresponding to x_{column}
				 */
				Resource res_i = getStateModel().getResource(row);
				Service cls_s = getStateModel().getService(row);
				Resource res_j = getStateModel().getResource(column);
				Service cls_t = getStateModel().getService(column);
				
				switch(res_i.getSchedulingStrategy()) {
				case PS:
				case UNKOWN:
					return calculateSecondDerivativePS(historicInterval, state, res_i, res_j, cls_s, cls_t);
				case IS:
					return calculateSecondDerivativeIS(historicInterval, state, res_i, res_j, cls_s, cls_t);
				case FCFS:
					return calculateSecondDerivativeFCFS(historicInterval, state, res_i, res_j, cls_s, cls_t);
				default:
					throw new AssertionError("Unsupported scheduling strategy.");
				}
			}
		});
	}
	
	private double calculateSecondDerivativeIS(int historicInterval, Vector state, Resource res_i, Resource res_j, Service cls_s, Service cls_t) {
		return 0;	
	}
	
	private double calculateSecondDerivativeFCFS(int historicInterval, Vector state, Resource res_i, Resource res_j, Service cls_s, Service cls_t) {
		return calculateSecondDerivativePS(historicInterval, state, res_i, res_j, cls_s, cls_t);
	}
	
	private double calculateSecondDerivativePS(int historicInterval, Vector state, Resource res_i, Resource res_j, Service cls_s, Service cls_t) {
		// if resources of x_{row} and x_{column} do not match the second derivative is zero
		// if we use observed utilization it is only a linear function -> second derivative is zero
		if (!useObservedUtilization && res_i.equals(res_j)) {
			// get resource demands
			double D_ir = state.get(getStateModel().getStateVariableIndex(res_i, cls_r));
			
			// get current throughput data
			Vector X = throughputQuery.get(historicInterval);
			double X_s = X.get(throughputQuery.indexOf(cls_s));
			double X_t = X.get(throughputQuery.indexOf(cls_t));
			
			/*
			 * beta is a shorthand variable for the denominator of the response time equation
			 *  
			 * beta = 1 - \sum_{v = 1}^{R} X_{v} * D_{i,v}
			 */
			double beta = 1 - getUtilization(historicInterval, res_i, state, X);
			
			/* 
			 * Calculate 2nd derivatives:
			 * \frac{2 * D_{i,r} * X_{s} * X_{t}}{beta^{3}} if r != s and r != t
			 * 
			 * \frac{2 * D_{i,r} * X_{s} * X_{t}}{beta^{3}} + \frac{2 * X_{s}}{beta^{2}} if r == s == t
			 * 
			 * \frac{2 * D_{i,r} * X_{s} * X_{t}}{beta^{3}} + \frac{X_{t}}{beta^{2}} if r == s and r != t
			 * 
			 * \frac{2 * D_{i,r} * X_{s} * X_{t}}{beta^{3}} + \frac{X_{s}}{beta^{2}} if r != s and r == t					 * 
			 */					
			double dev = (2 * D_ir * X_s * X_t) / (beta * beta * beta);
			
			if (cls_r.equals(cls_t) || cls_r.equals(cls_s)) {
				if (cls_t.equals(cls_s)) {
					return dev + 2 * X_s / (beta * beta);
				} else if (cls_r.equals(cls_s)) {
					return dev + X_t / (beta * beta);
				} else {
					return dev + X_s / (beta * beta);
				}
			} else {
				return dev;
			}
								
		} else {				
			return 0.0;
		}
	}
	
/*	private double calculatePhi0(int p, double U_i) {
		double phi0 = 0.0;
		for (int a = 0; a < p; a++) {
			phi0 += Math.pow(p * U_i, a) / factorials[a];
		}
		phi0 += Math.pow(p * U_i, p) / (factorials[p] * (1 - U_i));		
		return 1 / phi0;
	}
	
	private double calculateQueueingProbability(int p, double U_i) {
		if (p == 1) {
			return U_i;
		} else {
			double phi0 = calculatePhi0(p, U_i);
			return (Math.pow(p * U_i, p) * phi0) / (factorials[p] * (1 - U_i));
		}
	}
	
	private void precalculateFactorials(int p) {
		factorials = new double[p + 1];
		factorials[0] = 1.0;
		factorials[1] = 1.0;
		
		for (int i = 2; i <= p; i++) {
			factorials[i] = factorials[i - 1] * i;
		}
	}*/
}
