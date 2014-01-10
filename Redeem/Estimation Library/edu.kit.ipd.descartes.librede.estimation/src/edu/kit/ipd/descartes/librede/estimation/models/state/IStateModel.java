package edu.kit.ipd.descartes.librede.estimation.models.state;

import java.util.List;

import edu.kit.ipd.descartes.librede.estimation.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.IStateConstraint;
import edu.kit.ipd.descartes.linalg.Vector;

public interface IStateModel<C extends IStateConstraint> {
	
	int getStateSize();
	
	Vector getNextState(Vector state);
	
	void addConstraint(C constraint);
	
	List<C> getConstraints();
	
	List<IDifferentiableFunction> getStateDerivatives();
	
	Vector getInitialState();

}
