package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.util.Arrays;
import java.util.List;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixInitializer;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorInitializer;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.redeem.estimation.repository.Result;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

public class ResponseTimeEquation extends AbstractOutputFunction implements IDifferentiableFunction {

	private Service cls_r;
	
	private IMonitoringRepository repository;
	
	private Query<Scalar> responseTimeQuery;
	private Query<Vector> throughputQuery;
	
	public ResponseTimeEquation(SystemModel system, IMonitoringRepository repository,
			List<Resource> selectedResources,
			Service workloadClass) {
		super(system, selectedResources, Arrays.asList(workloadClass));
		
		if (workloadClass == null) {
			throw new IllegalArgumentException();
		}
		
		this.repository = repository;
		
		cls_r = workloadClass;
		
		responseTimeQuery = QueryBuilder.select(Metric.RESPONSE_TIME).forService(workloadClass).average();
		throughputQuery = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average();
	}
	
	@Override
	public double getObservedOutput() {
		return repository.execute(responseTimeQuery).getData().getValue();
	}

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

	@Override
	public Vector getFirstDerivatives(final Vector state) {
		return vector(state.rows(), new VectorInitializer() {		
			@Override
			public double cell(int row) {
				Resource res_i = getSystem().getState().getResource(row);
				Service cls_s = getSystem().getState().getWorkloadClass(row);
				
				Vector D_i = state.slice(getSystem().getState().getRange(res_i));
				double D_ir = state.get(getSystem().getState().getIndex(res_i, cls_r));
				
				Result<Vector> throughputResult = repository.execute(throughputQuery);
				
				Vector X = throughputResult.getData();
				double X_s = X.get(throughputResult.getIndex(cls_s));
				
				double beta = 1 - X.dot(D_i);
				
				if (cls_r.equals(cls_s)) {
					return (D_ir * X_s + beta) / (beta * beta);
				} else {
					return (D_ir * X_s) / (beta * beta);
				}
			}
		});
	}

	@Override
	public Matrix getSecondDerivatives(final Vector state) {
		return matrix(state.rows(), state.rows(), new MatrixInitializer() {			
			@Override
			public double cell(int row, int column) {
				Resource res_i = getSystem().getState().getResource(row);
				Service cls_s = getSystem().getState().getWorkloadClass(row);
				Resource res_j = getSystem().getState().getResource(column);
				Service cls_t = getSystem().getState().getWorkloadClass(column);
				
				if (res_i.equals(res_j)) {
					Vector D_i = state.slice(getSystem().getState().getRange(res_i));
					double D_ir = state.get(getSystem().getState().getIndex(res_i, cls_r));
					
					Result<Vector> throughputResult = repository.execute(throughputQuery);
					
					Vector X = throughputResult.getData();
					double X_s = X.get(throughputResult.getIndex(cls_s));
					double X_t = X.get(throughputResult.getIndex(cls_t));
					
					double beta = 1 - X.dot(D_i);
					
					double sigma = (2 * D_ir * X_s * X_t) / (beta * beta * beta);
					
					if (cls_r.equals(cls_t)) {
						if (cls_r.equals(cls_s)) {
							return sigma + 2 * X_s / (beta * beta);
						} else {
							return sigma + X_s / (beta * beta);
						}
					} else {
						return sigma;
					}
										
				} else {				
					return 0.0;
				}
			}
		});
	}
}
