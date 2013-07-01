package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.util.Arrays;
import java.util.List;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
//import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
//import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.redeem.estimation.repository.Result;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

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

	private Service cls_r;
	
	private IMonitoringRepository repository;
	
	private Query<Scalar> responseTimeQuery;
	private Query<Vector> throughputQuery;
	
	private int WINDOW_SIZE = 2;
		
	/**
	 * Creates a new instance.
	 * 
	 * @param system - the model of the system
	 * @param repository - the repository with current measurement data
	 * @param service - the service for which the response time is calculated
	 * @param selectedResources - the list of resources which are involved during the processing of the service
	 * 
	 * @throws {@link NullPointerException} if any parameter is null
	 * @thorws {@link IllegalArgumentException} if the list of services or resources is empty
	 */
	public ResponseTimeEquation(WorkloadDescription system, IMonitoringRepository repository,
			Service service, List<Resource> selectedResources
			) {
		super(system, selectedResources, Arrays.asList(service));
		
		this.repository = repository;
		
		cls_r = service;
		
		responseTimeQuery = QueryBuilder.select(Metric.RESPONSE_TIME).forService(service).average(WINDOW_SIZE);
		throughputQuery = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average(WINDOW_SIZE);
	}
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction#getObservedOutput()
	 */
	@Override
	public double getObservedOutput() {
		return repository.execute(responseTimeQuery).getData().getValue();
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction#getCalculatedOutput(edu.kit.ipd.descartes.linalg.Vector)
	 */
	@Override
	public double getCalculatedOutput(final Vector state) {
		double rt = 0.0;		
		for (Resource res_i : getSelectedResources()) {
			Vector D_i = state.slice(getSystem().getState().getRange(res_i));
			double D_ir = state.get(getSystem().getState().getIndex(res_i, cls_r));
			
			Vector X = repository.execute(throughputQuery).getData();
			
			rt += D_ir / (1 - X.dot(D_i));
		}
		return rt;
	}

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.diff.IDifferentiableFunction#getFirstDerivatives(edu.kit.ipd.descartes.linalg.Vector)
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
				Result<Vector> throughputResult = repository.execute(throughputQuery);
				
				Vector X = throughputResult.getData();
				double X_s = X.get(throughputResult.getIndex(cls_s));
				
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
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.diff.IDifferentiableFunction#getSecondDerivatives(edu.kit.ipd.descartes.linalg.Vector)
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
					Result<Vector> throughputResult = repository.execute(throughputQuery);
					
					Vector X = throughputResult.getData();
					double X_s = X.get(throughputResult.getIndex(cls_s));
					double X_t = X.get(throughputResult.getIndex(cls_t));
					
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
}
