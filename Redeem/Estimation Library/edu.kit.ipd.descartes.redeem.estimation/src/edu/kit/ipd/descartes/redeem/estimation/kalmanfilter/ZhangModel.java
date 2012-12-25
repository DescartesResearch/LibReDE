package edu.kit.ipd.descartes.redeem.estimation.kalmanfilter;

import static edu.kit.ipd.descartes.linalg.Matrix.*;
import static edu.kit.ipd.descartes.linalg.Vector.*;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixInitializer;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorInitializer;
import edu.kit.ipd.descartes.redeem.bayesplusplus.MeasurementModel;

public class ZhangModel extends MeasurementModel {
	
	private int numResources;
	private int numWorkloadClasses;
	private int[] numProcessors;

	public ZhangModel(int numResources, int numWorkloadClasses, int[] numProcessors,
			Vector observeNoiseCovariance) {
		super(numResources * numWorkloadClasses, 
				numResources + numWorkloadClasses, observeNoiseCovariance);
		this.numResources = numResources;
		this.numWorkloadClasses = numWorkloadClasses;
		this.numProcessors = numProcessors;
	}

	@Override
	public Vector nextObservation(final Vector currentState, final Vector...additionalInformation) {
		if (additionalInformation.length < 1) {
			throw new IllegalArgumentException("Throughput information is missing.");
		}
		
		final Vector currentThroughput = additionalInformation[0];
		
		final Vector currentUtilization = vector(numResources, new VectorInitializer() {		
			@Override
			public double cell(int row) {
				return calculateUtilization(row, currentState, currentThroughput);
			}
		});
		
		final Vector currentResponseTimes = vector(numWorkloadClasses, new VectorInitializer() {			
			@Override
			public double cell(int row) {
				double sumRT = 0.0;
				for (int i = 0; i < numResources; i++) {
					sumRT += getServiceDemand(i, row, currentState) / (1 - currentUtilization.get(i));
				}
				return sumRT;
			}
		});
		
		return vector(currentResponseTimes, currentUtilization);
	}

	@Override
	public void calculateJacobi(final Vector currentState, final Vector...additionalInformation) {
		if (additionalInformation.length < 1) {
			throw new IllegalArgumentException("Throughput information is missing.");
		}
		
		final Vector currentThroughput = additionalInformation[0];
		
		final Vector currentUtilization = vector(numResources, new VectorInitializer() {		
			@Override
			public double cell(int row) {
				return calculateUtilization(row, currentState, currentThroughput);
			}
		});
		
		Matrix jacobi = matrix(numResources + numWorkloadClasses, numResources * numWorkloadClasses, new MatrixInitializer() {			
			@Override
			public double cell(int row, int column) {
				int res1 = row - numWorkloadClasses;
				int cls1 = row;				
				int res2 = column / numWorkloadClasses;
				int cls2 = column % numWorkloadClasses;
				
				if (row < numWorkloadClasses) {				
					double u = currentUtilization.get(res2);
					double u1 = 1 - u;
					double u2 = u1 * u1;
					
					double R = 0.0;
					for (int res = 0; res < numResources; res++) {
						double lambda = currentThroughput.get(cls2);
						double D = getServiceDemand(res, cls1, currentState);
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
						return currentThroughput.get(cls2) / numProcessors[res1];
					} else {
						return 0;
					}
				}				
			}
		});
		
		setJacobi(jacobi);
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
