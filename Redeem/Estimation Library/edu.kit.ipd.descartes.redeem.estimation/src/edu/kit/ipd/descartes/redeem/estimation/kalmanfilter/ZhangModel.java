package edu.kit.ipd.descartes.redeem.estimation.kalmanfilter;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
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
	public Vector nextObservation(Vector currentState, Vector...additionalInformation) {
		if (additionalInformation.length < 1) {
			throw new IllegalArgumentException("Throughput information is missing.");
		}
		
		Vector currentThroughput = additionalInformation[0];
		
		Vector output = Vector.create(getObservationSize());
		for (int i = 0; i < output.rowCount(); i++) {
			output.set(i, 0.0);
		}

		for (int i = 0; i < numResources; i++) {
			double u = calculateUtilization(i, currentState, currentThroughput);
			double u1 = 1 - u;

			for (int j = 0; j < numWorkloadClasses; j++) {
				output.set(j, getServiceDemand(i, j, currentState) / u1);
			}
			output.set(numWorkloadClasses + i, u);
		}

		return output;
	}

	@Override
	public void calculateJacobi(Vector currentState, Vector...additionalInformation) {
		if (additionalInformation.length < 1) {
			throw new IllegalArgumentException("Throughput information is missing.");
		}
		
		Vector currentThroughput = additionalInformation[0];		
		
		Matrix jacobi = Matrix.create(numResources + numWorkloadClasses, numResources * numWorkloadClasses);
		for (int focusedResource = 0; focusedResource < numResources; focusedResource++) {
			double u = calculateUtilization(focusedResource, currentState, currentThroughput);

			double u1 = 1 - u;
			double u2 = u1 * u1;

			for (int cls1 = 0; cls1 < numWorkloadClasses; cls1++) {
				for (int cls2 = 0; cls2 < numWorkloadClasses; cls2++) {
					double R = 0.0;
					for (int res = 0; res < numResources; res++) {
						double lambda = currentThroughput.get(cls2);
						double D = getServiceDemand(res, cls1, currentState);
						int P = numProcessors[res];

						if ((res == focusedResource) && (cls1 == cls2)) {
							R += (u1 + lambda * D / P) / u2;
						} else {
							R += (lambda * D / P) / u2;
						}
					}

					int row = cls1;
					int col = focusedResource * numWorkloadClasses + cls2;

					jacobi.set(row, col, R);
				}
			}

			for (int res = 0; res < numResources; res++) {
				for (int cls = 0; cls < numWorkloadClasses; cls++) {

					int row = numWorkloadClasses + res;
					int col = focusedResource * numWorkloadClasses + cls;

					if (res == focusedResource) {
						jacobi.set(row, col, currentThroughput.get(cls) / numProcessors[res]);
					} else {
						jacobi.set(row, col, 0);
					}
				}
			}
		}
		
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
