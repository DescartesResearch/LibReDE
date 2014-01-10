package edu.kit.ipd.descartes.librede.approaches;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import edu.kit.ipd.descartes.librede.estimation.algorithm.SimpleApproximation;
import edu.kit.ipd.descartes.librede.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IDirectOutputFunction;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ServiceDemandLaw;
import edu.kit.ipd.descartes.librede.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.librede.estimation.repository.Aggregation;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.AggregationFunction;

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
