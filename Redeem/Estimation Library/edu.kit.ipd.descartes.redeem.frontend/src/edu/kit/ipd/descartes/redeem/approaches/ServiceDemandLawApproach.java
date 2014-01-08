package edu.kit.ipd.descartes.redeem.approaches;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import edu.kit.ipd.descartes.linalg.AggregationFunction;
import edu.kit.ipd.descartes.redeem.estimation.algorithm.SimpleApproximation;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IDirectOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.ServiceDemandLaw;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.redeem.estimation.repository.Aggregation;
import edu.kit.ipd.descartes.redeem.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class ServiceDemandLawApproach extends AbstractEstimationApproach {
	
	public static final String NAME = "ServiceDemandLaw";
	
	@Override
	public void initialize(WorkloadDescription workload,
			RepositoryCursor cursor, int estimationWindow, boolean iterative) throws InitializationException {
		super.initialize(workload, cursor, estimationWindow, iterative);
		
		int stateSize = workload.getServices().size();
		
		ConstantStateModel<Unconstrained> stateModel = new ConstantStateModel<Unconstrained>(stateSize, zeros(stateSize));
		
		VectorObservationModel<IDirectOutputFunction> observationModel = new VectorObservationModel<IDirectOutputFunction>();
		for (Resource res : workload.getResources()) {
			for (Service service : workload.getServices()) {
				ServiceDemandLaw func = new ServiceDemandLaw(workload, cursor, res, service);
				
				if (!func.isApplicable()) {
					throw new InitializationException("The service demand law output function is not applicable.");
				}
				
				observationModel.addOutputFunction(func);
			}
		}

		SimpleApproximation estimator = new SimpleApproximation(Aggregation.AVERAGE);
		estimator.initialize(stateModel, observationModel, estimationWindow);
		setEstimationAlgorithm(estimator);
	}
}
