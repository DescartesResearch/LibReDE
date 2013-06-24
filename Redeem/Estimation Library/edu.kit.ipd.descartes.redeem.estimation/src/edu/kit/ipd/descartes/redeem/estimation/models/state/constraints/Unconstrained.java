package edu.kit.ipd.descartes.redeem.estimation.models.state.constraints;

import edu.kit.ipd.descartes.linalg.Vector;

public final class Unconstrained implements IStateConstraint {
	
	private Unconstrained() {
		throw new AssertionError("Class Unconstrained cannot be instantiated.");
	}

	@Override
	public double getLowerBound() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getUpperBound() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getValue(Vector state) {
		throw new UnsupportedOperationException();
	}

}
