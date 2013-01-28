package edu.kit.ipd.descartes.redeem.estimation.models.observation;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.IJacobiMatrix;

public abstract class AbstractLinearObservationModel extends
		AbstractObservationModel implements ILinearObservationModel, IJacobiMatrix {
	
	@Override
	public Vector getCalculatedOutputVector(Vector state) {
		return getInputMatrix().multipliedBy(state);
	}
	
	@Override
	public Matrix getJacobiMatrix(Vector state) {
		return getInputMatrix();
	}
	
}
