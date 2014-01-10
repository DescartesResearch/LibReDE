package edu.kit.ipd.descartes.librede.approaches;

import edu.kit.ipd.descartes.librede.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;

public interface IEstimationApproach {
	
	void initialize(WorkloadDescription workload, RepositoryCursor cursor, int estimationWindow, boolean iterative) throws InitializationException;
	
	TimeSeries execute() throws EstimationException;

}
