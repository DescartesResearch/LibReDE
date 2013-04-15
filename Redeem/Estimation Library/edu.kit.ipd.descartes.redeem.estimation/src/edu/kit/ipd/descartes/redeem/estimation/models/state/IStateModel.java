package edu.kit.ipd.descartes.redeem.estimation.models.state;

import java.util.List;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.IStateConstraint;

public interface IStateModel<C extends IStateConstraint> {
	
	int getStateSize();
	
	Vector getNextState(Vector state);
	
	void addConstraint(C constraint);
	
	List<C> getConstraints();
	
	List<IDifferentiableFunction> getStateDerivatives();
	
	Vector getInitialState();

}
