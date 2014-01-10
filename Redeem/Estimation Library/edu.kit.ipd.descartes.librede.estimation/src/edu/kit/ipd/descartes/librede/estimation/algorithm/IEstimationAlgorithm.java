package edu.kit.ipd.descartes.librede.estimation.algorithm;

import edu.kit.ipd.descartes.librede.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.state.IStateModel;
import edu.kit.ipd.descartes.linalg.Vector;

public interface IEstimationAlgorithm<S extends IStateModel<?>, O extends IObservationModel<?, ?>> {
	
	void initialize(S stateModel, O observationModel, int estimationWindow) throws InitializationException;
	
	void update() throws EstimationException;
	
	Vector estimate() throws EstimationException;
	
	void destroy();

}
