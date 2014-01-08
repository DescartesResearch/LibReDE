package edu.kit.ipd.descartes.redeem.approaches;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import edu.kit.ipd.descartes.redeem.estimation.algorithm.SimpleApproximation;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IDirectOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.ResponseTimeApproximation;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.redeem.estimation.repository.Aggregation;
import edu.kit.ipd.descartes.redeem.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class ResponseTimeApproximationApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "ResponseTimeApproximation";
	
	@Override
	public void initialize(WorkloadDescription workload,
			RepositoryCursor cursor, int estimationWindow, boolean iterative) throws InitializationException {
		super.initialize(workload, cursor, estimationWindow, iterative);
		
		int stateSize = workload.getServices().size();
		
		if (workload.getResources().size() != 1) {
			throw new InitializationException("The response time approximation approach is only applicable on workload models with one resource.");
		}
		
		Resource res = workload.getResources().get(0);
		ConstantStateModel<Unconstrained> stateModel = new ConstantStateModel<Unconstrained>(stateSize, zeros(stateSize));
		VectorObservationModel<IDirectOutputFunction> observationModel = new VectorObservationModel<IDirectOutputFunction>();
		for (int i = 0; i < stateSize; i++) {
			ResponseTimeApproximation func = new ResponseTimeApproximation(workload, cursor, res, workload.getServices().get(i), Aggregation.AVERAGE);
			
			if (!func.isApplicable()) {
				throw new InitializationException("The response time approximation output function is not applicable.");
			}
			
			observationModel.addOutputFunction(func);
		}		
		
		SimpleApproximation estimator = new SimpleApproximation(Aggregation.MINIMUM);
		estimator.initialize(stateModel, observationModel, estimationWindow);
		setEstimationAlgorithm(estimator);
	}
}
