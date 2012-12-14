package edu.kit.ipd.descartes.redeem.estimation.models.observation;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public interface IObservationModel {
	
	Vector getOutputVector(Vector state);
	
	Matrix getJacobiMatrix(Vector state);
	
	Matrix[] getHessianMatrices(Vector state);

}
