package edu.kit.ipd.descartes.redeem.estimation.models.observation.impl;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.observation.AbstractLinearObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.repository.IMonitoringRepository;

public class UtilizationLawModel extends AbstractLinearObservationModel {

	private IMonitoringRepository[] utilization;
	private IMonitoringRepository throughput;

	private Matrix inputMatrix;

	public UtilizationLawModel(IMonitoringRepository[] utilization,
			IMonitoringRepository throughput) {
		this.utilization = utilization;
		this.throughput = throughput;
	}

	@Override
	public Matrix getInputMatrix() {
		return inputMatrix;
	}

	@Override
	public int getObservationSize() {
		return utilization.length;
	}

	@Override
	public boolean nextObservation() {
		boolean eos = false;
		for (int i = 0; i < utilization.length; i++) {
			eos = eos || utilization[i].nextSample();
		}
		// update input matrix
		double[][] values;
		if (throughput.nextSample()) {
			eos = true;
			double[] currentThroughput = throughput.getCurrentSample()
					.toArray1D();
			int rows = 1, columns = currentThroughput.length;
			// copy previous samples from input matrix
			if (inputMatrix != null) {
				rows = inputMatrix.rows() + 1;
				values = new double[rows][columns];
				for (int i = 0; i < inputMatrix.rows(); ++i) {
					values[i] = inputMatrix.row(i).toArray1D();
				}
			} else
				values = new double[rows][columns];
			// add current sample to input matrix
			values[rows - 1] = currentThroughput;
			inputMatrix = matrix(values);
		}
		return eos;
	}

	@Override
	public Vector getObservedOutput() {
		double[] util = new double[utilization.length];
		for (int i = 0; i < util.length; i++) {
			util[i] = utilization[i].getCurrentSample().get(0);
		}
		return vector(util);
	}

}
