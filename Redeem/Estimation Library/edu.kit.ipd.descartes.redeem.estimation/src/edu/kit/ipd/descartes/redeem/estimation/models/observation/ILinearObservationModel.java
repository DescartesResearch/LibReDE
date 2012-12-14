package edu.kit.ipd.descartes.redeem.estimation.models.observation;

import edu.kit.ipd.descartes.linalg.Matrix;

public interface ILinearObservationModel extends IObservationModel {
	
	Matrix getInputMatrix();

}
