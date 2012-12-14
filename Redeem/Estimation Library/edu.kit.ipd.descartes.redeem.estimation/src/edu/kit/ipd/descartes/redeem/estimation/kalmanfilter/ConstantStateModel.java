package edu.kit.ipd.descartes.redeem.estimation.kalmanfilter;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.StateModel;

public class ConstantStateModel extends StateModel {
	
	public ConstantStateModel(int stateSize, Vector stateNoiseCovariance, Matrix stateNoiseCoupling) {
		super(stateSize, stateNoiseCovariance, stateNoiseCoupling);
		Matrix identity = Matrix.create(stateSize, stateSize);
		for (int i = 0; i < stateSize; i++) {
			for (int j = 0; j < stateSize; j++) {
				identity.set(i, j, 0);
			}
			identity.set(i, i, 1);
		}
		setJacobi(identity);
	}

	@Override
	public Vector nextState(Vector currentState) {
		return currentState;
	}

	@Override
	public void calculateJacobi(Vector currentState) {
	}

}
