package edu.kit.ipd.descartes.redeem.approaches;

import edu.kit.ipd.descartes.linalg.MatrixBuilder;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.algorithm.IEstimationAlgorithm;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.redeem.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public abstract class AbstractEstimationApproach implements IEstimationApproach {
	
	private boolean iterative;
	private WorkloadDescription workload;
	private IEstimationAlgorithm<?, ?> estimator;
	private RepositoryCursor cursor;
	
	@Override
	public void initialize(WorkloadDescription workload,
			RepositoryCursor cursor, int estimationWindow, boolean iterative)
			throws InitializationException {
		this.workload = workload;
		this.cursor = cursor;
		this.iterative = iterative;
	}
	
	protected void setEstimationAlgorithm(IEstimationAlgorithm<?, ?> estimator) {
		this.estimator = estimator;
	}
	
	@Override
	public TimeSeries execute() throws EstimationException {
		try {
			MatrixBuilder estimateBuilder = new MatrixBuilder(workload.getState().getStateSize());
			MatrixBuilder timestampBuilder = new MatrixBuilder(1);
			if (iterative) {
				while(cursor.next()) {
					estimator.update();

					timestampBuilder.addRow(cursor.getCurrentIntervalEnd());
					estimateBuilder.addRow(estimator.estimate());
				}
			} else {
				while(cursor.next()) {
					estimator.update();
				}

				timestampBuilder.addRow(cursor.getCurrentIntervalEnd());
				estimateBuilder.addRow(estimator.estimate());
			}
			return new TimeSeries((Vector)timestampBuilder.toMatrix(), estimateBuilder.toMatrix());
		} finally {
			if (estimator != null) {
				estimator.destroy();
			}
		}
	}

}
