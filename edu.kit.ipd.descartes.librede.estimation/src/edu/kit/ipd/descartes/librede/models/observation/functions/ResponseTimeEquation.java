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
package edu.kit.ipd.descartes.librede.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.sum;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.util.Arrays;
import java.util.List;

import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;
import edu.kit.ipd.descartes.librede.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.librede.repository.IRepositoryCursor;
import edu.kit.ipd.descartes.librede.repository.Query;
import edu.kit.ipd.descartes.librede.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
//import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
//import edu.kit.ipd.descartes.linalg.VectorFunction;

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
 * @author Simon Spinner (simon.spinner@kit.edu)
 * @version 1.0
 */
public class ResponseTimeEquation extends AbstractOutputFunction implements IDifferentiableFunction {
	
	private double[] factorials;

	private Service cls_r;
	
	private Query<Scalar> responseTimeQuery;
	private Query<Vector> throughputQuery;
	
	/**
	 * Creates a new instance.
	 * 
	 * @param system - the model of the system
	 * @param repository - a view of the repository with current measurement data
	 * @param service - the service for which the response time is calculated
	 * @param selectedResources - the list of resources which are involved during the processing of the service
	 * 
	 * @throws NullPointerException if any parameter is null
	 * @throws IllegalArgumentException if the list of services or resources is empty
	 */
	public ResponseTimeEquation(WorkloadDescription system, IRepositoryCursor repository,
			Service service, List<Resource> selectedResources
			) {
		super(system, selectedResources, Arrays.asList(service));
		
		cls_r = service;
		
//		int maxParallel = 1;
//		for (Resource res : selectedResources) {
//			maxParallel = Math.max(maxParallel, res.getNumberOfParallelServers());
//		}
//		precalculateFactorials(maxParallel);
		
		responseTimeQuery = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(service).average().using(repository);
		throughputQuery = QueryBuilder.select(StandardMetric.THROUGHPUT).forAllServices().average().using(repository);
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.models.observation.functions.IOutputFunction#isApplicable()
	 */
	@Override
	public boolean isApplicable(List<String> messages) {
		boolean result = true;
		result = result && checkQueryPrecondition(responseTimeQuery, messages);
		result = result && checkQueryPrecondition(throughputQuery, messages);
		return result;
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return responseTimeQuery.execute().getValue();
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.models.observation.functions.IOutputFunction#getCalculatedOutput(edu.kit.ipd.descartes.linalg.Vector)
	 */
	@Override
	public double getCalculatedOutput(final Vector state) {
		double rt = 0.0;
		Vector X = throughputQuery.execute();
		double X_total = sum(X);
		
		for (Resource res_i : getSelectedResources()) {
			Vector D_i = state.slice(getSystem().getState().getRange(res_i));
			double D_ir = state.get(getSystem().getState().getIndex(res_i, cls_r));
			double U_i = X.dot(D_i);

			int p = res_i.getNumberOfServers();
//			double P_q = calculateQueueingProbability(p, U_i);
			
			rt += D_ir / (p - U_i);
		}
		return rt;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.models.diff.IDifferentiableFunction#getFirstDerivatives(edu.kit.ipd.descartes.linalg.Vector)
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
				Resource res_i = getSystem().getState().getResource(row);
				Service cls_s = getSystem().getState().getService(row);
				
				// get resource demands
				Vector D_i = state.slice(getSystem().getState().getRange(res_i));
				double D_ir = state.get(getSystem().getState().getIndex(res_i, cls_r));
				
				// get current throughput data
				Vector X = throughputQuery.execute();
				double X_s = X.get(throughputQuery.indexOf(cls_s));
				
				/*
				 * beta is a shorthand variable for the denominator of the response time equation
				 *  
				 * beta = 1 - \sum_{v = 1}^{R} X_{v} * D_{i,v}
				 */
				double beta = 1 - X.dot(D_i);
				
				/*
				 * Calculate derivatives:
				 * 
				 * \frac{D_{i,r * X_{s}}{beta^{2}} if r != s
				 * 
				 * \frac{D_{i,r * X_{s}}{beta^{2}} + \frac{1}{beta} if r == s				 * 			
				 */
				double dev = (D_ir * X_s) / (beta * beta);				
				if (cls_r.equals(cls_s)) {
					return dev + (1 / beta);
				} else {
					return dev;
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.librede.models.diff.IDifferentiableFunction#getSecondDerivatives(edu.kit.ipd.descartes.linalg.Vector)
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
				Resource res_i = getSystem().getState().getResource(row);
				Service cls_s = getSystem().getState().getService(row);
				Resource res_j = getSystem().getState().getResource(column);
				Service cls_t = getSystem().getState().getService(column);
				
				// if resources of x_{row} and x_{column} do not match the second derivative is zero
				if (res_i.equals(res_j)) {
					// get resource demands
					Vector D_i = state.slice(getSystem().getState().getRange(res_i));
					double D_ir = state.get(getSystem().getState().getIndex(res_i, cls_r));
					
					// get current throughput data
					Vector X = throughputQuery.execute();
					double X_s = X.get(throughputQuery.indexOf(cls_s));
					double X_t = X.get(throughputQuery.indexOf(cls_t));
					
					/*
					 * beta is a shorthand variable for the denominator of the response time equation
					 *  
					 * beta = 1 - \sum_{v = 1}^{R} X_{v} * D_{i,v}
					 */
					double beta = 1 - X.dot(D_i);
					
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
		});
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
