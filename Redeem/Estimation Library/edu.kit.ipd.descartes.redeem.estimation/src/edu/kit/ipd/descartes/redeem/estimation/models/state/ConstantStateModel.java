package edu.kit.ipd.descartes.redeem.estimation.models.state;

import static edu.kit.ipd.descartes.linalg.Matrix.*;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.IJacobiMatrix;

public class ConstantStateModel implements IStateModel, IJacobiMatrix {
	
	

	@Override
	public int getStateSize() {
		return 0;
	}

	@Override
	public Vector getNextState(Vector state) {
		return state;
	}

	@Override
	public Matrix getJacobiMatrix(Vector state) {
		return identity(state.rows());
	}

}
