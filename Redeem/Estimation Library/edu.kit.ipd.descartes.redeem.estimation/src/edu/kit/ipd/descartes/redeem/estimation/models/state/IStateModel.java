package edu.kit.ipd.descartes.redeem.estimation.models.state;

import edu.kit.ipd.descartes.linalg.Vector;

public interface IStateModel {
	
	int getStateSize();
	
	Vector getNextState(Vector state);

}
