package edu.kit.ipd.descartes.redeem.estimation.kalmanfilter;

import static edu.kit.ipd.descartes.linalg.Matrix.*;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.bayesplusplus.StateModel;

public class ConstantStateModel extends StateModel {
	
	public ConstantStateModel(int stateSize, Vector stateNoiseCovariance, Matrix stateNoiseCoupling) {
		super(stateSize, stateNoiseCovariance, stateNoiseCoupling);
		setJacobi(identity(stateSize));
	}

	@Override
	public Vector nextState(Vector currentState) {
		return currentState;
	}

	@Override
	public void calculateJacobi(Vector currentState) {
	}

}
