package edu.kit.ipd.descartes.librede.estimation.models.state.constraints;

import edu.kit.ipd.descartes.librede.estimation.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public class StateBoundsConstraint implements ILinearStateConstraint, IDifferentiableFunction {
	
	private double lower;
	private double upper;
	private int stateVar;
	
	public StateBoundsConstraint(int stateVar, double lowerBound, double upperBound) {
		this.lower = lowerBound;
		this.upper = upperBound;
		this.stateVar = stateVar;
	}
	
	public int getStateVariable() {
		return stateVar;
	}
	
	@Override
	public double getValue(Vector state) {
		return state.get(stateVar);
	}

	@Override
	public double getLowerBound() {
		return lower;
	}

	@Override
	public double getUpperBound() {
		return upper;
	}

	@Override
	public Vector getFirstDerivatives(Vector x) {
		
		return null;
	}

	@Override
	public Matrix getSecondDerivatives(Vector x) {
		return null;
	}

}
