package edu.kit.ipd.descartes.redeem.estimation.models.state;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.IStateConstraint;

public class ConstantStateModel<C extends IStateConstraint> implements IStateModel<C> {	
	
	private class ConstantFunction implements IDifferentiableFunction {

		@Override
		public Vector getFirstDerivatives(Vector x) {
			return zeros(x.rows());
		}

		@Override
		public Matrix getSecondDerivatives(Vector x) {
			return zeros(x.rows());
		}
		
	}
	
	private List<C> constraints = new ArrayList<C>();
	private Vector initialState;
	private List<IDifferentiableFunction> derivatives = new ArrayList<IDifferentiableFunction>();
	
	public ConstantStateModel(int stateSize, Vector initialState) {
		if (stateSize <= 0) {
			throw new IllegalArgumentException("State size must be greater than 0.");
		}
		if (initialState == null) {
			throw new IllegalArgumentException("Initial state must not be null.");
		}
		if (initialState.rows() != stateSize) {
			throw new IllegalArgumentException("Size of initial state vector must be equal to the state size.");
		}
		
		ConstantFunction func = new ConstantFunction();
		for (int i = 0; i < stateSize; i++) {
			derivatives.add(func);
		}
	}

	@Override
	public int getStateSize() {
		return 0;
	}

	@Override
	public Vector getNextState(Vector state) {
		return state;
	}

	@Override
	public void addConstraint(C constraint) {
		if (constraint == null) {
			throw new IllegalArgumentException("Constraint must not be null.");
		}
		
		constraints.add(constraint);
	}

	@Override
	public List<C> getConstraints() {
		return Collections.unmodifiableList(constraints);
	}

	@Override
	public List<IDifferentiableFunction> getStateDerivatives() {
		return derivatives;
	}
	
	@Override
	public Vector getInitialState() {
		return initialState;
	}

}
