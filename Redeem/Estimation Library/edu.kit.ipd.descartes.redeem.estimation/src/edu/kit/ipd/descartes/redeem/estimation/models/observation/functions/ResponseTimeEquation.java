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
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

public class ResponseTimeEquation extends AbstractOutputFunction implements IDifferentiableFunction {

	private Service cls_r;
	
	private Query<Scalar> responseTimeQuery;
	private Query<Vector> throughputQuery;
	
	public ResponseTimeEquation(SystemModel system, IMonitoringRepository repository,
			List<Resource> selectedResources,
			Service workloadClass) {
		super(system, selectedResources, Arrays.asList(workloadClass));
		
		if (workloadClass == null) {
			throw new IllegalArgumentException();
		}
		
		cls_r = workloadClass;
		
		responseTimeQuery = repository.select(Metric.RESPONSE_TIME).forService(workloadClass).average();
		throughputQuery = repository.select(Metric.THROUGHPUT).forAllServices().average();
	}
	
	@Override
	public double getObservedOutput() {
		return responseTimeQuery.execute().getValue();
	}

	@Override
	public double getCalculatedOutput(final Vector state) {
		double rt = 0.0;
		for (Resource res_i : getSelectedResources()) {
			Vector D_i = state.slice(getSystem().getState().getRange(res_i));
			double D_ir = state.get(getSystem().getState().getIndex(res_i, cls_r));
			
			Vector X = throughputQuery.execute();
			
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
				
				Vector X = throughputQuery.execute();
				double X_s = X.get(throughputQuery.getIndex(cls_s));
				
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
					
					Vector X = throughputQuery.execute();
					double X_s = X.get(throughputQuery.getIndex(cls_s));
					double X_t = X.get(throughputQuery.getIndex(cls_t));
					
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
