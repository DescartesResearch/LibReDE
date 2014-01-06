package edu.kit.ipd.descartes.redeem.estimation.algorithm;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.redeem.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel;

public interface IEstimationAlgorithm<S extends IStateModel<?>, O extends IObservationModel<?, ?>> {
	
	void initialize(S stateModel, O observationModel, int estimationWindow) throws InitializationException;
	
	void update() throws EstimationException;
	
	Vector estimate() throws EstimationException;
	
	void destroy();

}
