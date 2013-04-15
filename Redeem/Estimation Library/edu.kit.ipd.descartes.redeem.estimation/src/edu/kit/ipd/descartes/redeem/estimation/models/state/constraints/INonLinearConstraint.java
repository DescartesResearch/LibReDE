package edu.kit.ipd.descartes.redeem.estimation.models.state.constraints;

import edu.kit.ipd.descartes.linalg.Vector;

public interface INonLinearConstraint extends IStateConstraint {
	
	double getLowerBound();
	
	double getUpperBound();
	
	double getValue(Vector state);

}
