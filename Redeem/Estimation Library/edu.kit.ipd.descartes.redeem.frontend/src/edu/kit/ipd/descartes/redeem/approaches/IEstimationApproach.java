package edu.kit.ipd.descartes.redeem.approaches;

import edu.kit.ipd.descartes.redeem.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.redeem.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public interface IEstimationApproach {
	
	void initialize(WorkloadDescription workload, RepositoryCursor cursor, int estimationWindow, boolean iterative) throws InitializationException;
	
	TimeSeries execute() throws EstimationException;

}
