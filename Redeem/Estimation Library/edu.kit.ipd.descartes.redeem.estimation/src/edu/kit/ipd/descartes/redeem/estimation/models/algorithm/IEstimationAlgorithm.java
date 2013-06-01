package edu.kit.ipd.descartes.redeem.estimation.models.algorithm;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel;
import edu.kit.ipd.descartes.redeem.exceptions.EstimationException;

public interface IEstimationAlgorithm<S extends IStateModel<?>, O extends IObservationModel<?, ?>> {
	
	void initialize(S stateModel, O observationModel);
	
	Vector estimate() throws EstimationException;
	
	void destroy();

}
