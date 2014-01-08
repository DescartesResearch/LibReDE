package edu.kit.ipd.descartes.redeem.approaches;

import static edu.kit.ipd.descartes.linalg.LinAlg.ones;
import edu.kit.ipd.descartes.redeem.bayesplusplus.ExtendedKalmanFilter;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.ResponseTimeEquation;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.UtilizationLaw;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.redeem.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class ZhangKalmanFilterApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "ZhangKalmanFilter";

	@Override
	public void initialize(WorkloadDescription workload,
			RepositoryCursor cursor, int estimationWindow, boolean iterative)
			throws InitializationException {
		super.initialize(workload, cursor, estimationWindow, iterative);
		
		int stateSize = workload.getServices().size();
		
		ConstantStateModel<Unconstrained> stateModel = new ConstantStateModel<Unconstrained>(stateSize, ones(stateSize).times(0.01));
		
		VectorObservationModel<IOutputFunction> observationModel = new VectorObservationModel<IOutputFunction>();
		for (Service serv : workload.getServices()) {
			ResponseTimeEquation func = new ResponseTimeEquation(workload, cursor, serv, workload.getResources());
			
			if (!func.isApplicable()) {
				throw new InitializationException("The response time equation output function is not applicable.");
			}
			
			observationModel.addOutputFunction(func);
		}
		
		
		for (Resource res : workload.getResources()) {
			UtilizationLaw func = new UtilizationLaw(workload, cursor, res);
		
			if (!func.isApplicable()) {
				throw new InitializationException("The utilization law output function is not applicable.");
			}
			
			observationModel.addOutputFunction(func);
		}

		ExtendedKalmanFilter estimator = new ExtendedKalmanFilter();
		estimator.initialize(stateModel, observationModel, estimationWindow);
		setEstimationAlgorithm(estimator);
	}



}
