package edu.kit.ipd.descartes.librede.estimation.models.state.constraints;

import edu.kit.ipd.descartes.linalg.Vector;

public interface IStateConstraint {
	
	double getLowerBound();
	
	double getUpperBound();
	
	double getValue(Vector state);
	
}
