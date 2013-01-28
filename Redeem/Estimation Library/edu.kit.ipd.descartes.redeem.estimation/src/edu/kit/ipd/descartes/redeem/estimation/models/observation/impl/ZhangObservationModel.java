package edu.kit.ipd.descartes.redeem.estimation.models.observation.impl;

import static edu.kit.ipd.descartes.linalg.Matrix.matrix;
import static edu.kit.ipd.descartes.linalg.Vector.vector;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixInitializer;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorInitializer;
import edu.kit.ipd.descartes.redeem.estimation.io.MeasurementStream;
import edu.kit.ipd.descartes.redeem.estimation.models.IJacobiMatrix;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.AbstractObservationModel;

public class ZhangObservationModel extends AbstractObservationModel implements IJacobiMatrix {
	
	private MeasurementStream[] utilization;
	private MeasurementStream throughput;
	private MeasurementStream averageResponseTimes;
	
	private int numResources;
	private int numWorkloadClasses;	
	private int[] numProcessors;
	
	private Vector calculatedUtilization = null;
	
	@Override
	public boolean nextObservation() {
		boolean eos = false;
		
		for (int i = 0; i < utilization.length; i++) {
			eos = eos || utilization[i].nextSample();
		}
		
		eos = eos || throughput.nextSample();
		eos = eos || averageResponseTimes.nextSample();
		
		calculatedUtilization = null;
		
		return eos;
	}
	
	@Override
	public Vector getObservedOutputVector() {
		double[] util = new double[utilization.length];
		for (int i = 0; i < util.length; i++) {
			util[i] = utilization[i].getCurrentSample().get(0);
		}
		return vector(averageResponseTimes.getCurrentSample(), vector(util));
	}

	@Override
	public Vector getCalculatedOutputVector(final Vector state) {	
		
		calculateUtilization(state, throughput.getCurrentSample());
		
		Vector currentResponseTimes = vector(numWorkloadClasses, new VectorInitializer() {			
			@Override
			public double cell(int row) {
				double sumRT = 0.0;
				for (int i = 0; i < numResources; i++) {
					sumRT += getServiceDemand(i, row, state) / (1 - calculatedUtilization.get(i));
				}
				return sumRT;
			}
		});
		
		return vector(currentResponseTimes, calculatedUtilization);
	}

	@Override
	public Matrix getJacobiMatrix(final Vector state) {
		
		calculateUtilization(state, throughput.getCurrentSample());
		
		Matrix jacobi = matrix(numResources + numWorkloadClasses, numResources * numWorkloadClasses, new MatrixInitializer() {			
			@Override
			public double cell(int row, int column) {
				int res1 = row - numWorkloadClasses;
				int cls1 = row;				
				int res2 = column / numWorkloadClasses;
				int cls2 = column % numWorkloadClasses;
				
				if (row < numWorkloadClasses) {				
					double u = calculatedUtilization.get(res2);
					double u1 = 1 - u;
					double u2 = u1 * u1;
					
					double R = 0.0;
					for (int res = 0; res < numResources; res++) {
						double lambda = throughput.getCurrentSample().get(cls2);
						double D = getServiceDemand(res, cls1, state);
						int P = numProcessors[res];

						if ((res == res2) && (cls1 == cls2)) {
							R += (u1 + lambda * D / P) / u2;
						} else {
							R += (lambda * D / P) / u2;
						}
					}
					return R;					
				} else {
					if (res1 == res2) {
						return throughput.getCurrentSample().get(cls2) / numProcessors[res1];
					} else {
						return 0;
					}
				}				
			}
		});
		
		return jacobi;
	}

	@Override
	public int getObservationSize() {
		return utilization.length + averageResponseTimes.size();
	}
	
	
	private void calculateUtilization(final Vector currentState, final Vector currentThroughput) {
		calculatedUtilization = vector(numResources, new VectorInitializer() {		
			@Override
			public double cell(int row) {
				return calculateUtilization(row, currentState, currentThroughput);
			}
		});
	}
	
	private double calculateUtilization(int resourceIndex, Vector currentState, Vector currentThroughput) {
		double u = 0.0;
		for (int i = 0; i < numWorkloadClasses; i++) {
			double lambda = currentThroughput.get(i);
			u += lambda * getServiceDemand(resourceIndex, i, currentState);
		}
		u /= numProcessors[resourceIndex];
		if (u > 0.99) {
			u = 0.99;
		}
		return u;
	}
	
	private double getServiceDemand(int resource, int workloadClass, Vector currentState) {
		return currentState.get(resource * numWorkloadClasses + workloadClass);
	}

}
