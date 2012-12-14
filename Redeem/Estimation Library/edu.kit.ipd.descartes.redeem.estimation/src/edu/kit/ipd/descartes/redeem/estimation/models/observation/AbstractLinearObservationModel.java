package edu.kit.ipd.descartes.redeem.estimation.models.observation;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public abstract class AbstractLinearObservationModel extends
		AbstractObservationModel implements ILinearObservationModel {
	
	@Override
	public Vector getOutputVector(Vector state) {
		return getInputMatrix().multiply(state);
	}
	
	@Override
	public Matrix getJacobiMatrix(Vector state) {
		return getInputMatrix();
	}
	
	@Override
	public Matrix[] getHessianMatrices(Vector state) {
		return null;
	}

}
