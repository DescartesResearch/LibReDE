package edu.kit.ipd.descartes.redeem.approaches;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.ResponseTimeEquation;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.ILinearStateConstraint;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.UtilizationConstraint;
import edu.kit.ipd.descartes.redeem.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.redeem.ipopt.java.RecursiveOptimization;

public class MenasceOptimizationApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "MenasceOptimization";
	
	@Override
	public void initialize(WorkloadDescription workload,
			RepositoryCursor cursor, int estimationWindow, boolean iterative)
			throws InitializationException {
		super.initialize(workload, cursor, estimationWindow, iterative);
		
		int stateSize = workload.getState().getStateSize();
		
		ConstantStateModel<ILinearStateConstraint> stateModel = new ConstantStateModel<ILinearStateConstraint>(stateSize, zeros(stateSize));
		for (Resource res : workload.getResources()) {
			stateModel.addConstraint(new UtilizationConstraint(workload, cursor, res));
		}
		
		VectorObservationModel<IOutputFunction> observationModel = new VectorObservationModel<IOutputFunction>();
		for (Service service : workload.getServices()) {
			ResponseTimeEquation func = new ResponseTimeEquation(workload, cursor, service, workload.getResources());
			
			if (!func.isApplicable()) {
				throw new InitializationException("The service demand law output function is not applicable.");
			}
			
			observationModel.addOutputFunction(func);
		}

		RecursiveOptimization estimator = new RecursiveOptimization();
		estimator.initialize(stateModel, observationModel, estimationWindow);
		setEstimationAlgorithm(estimator);
	}
	
}
