package edu.kit.ipd.descartes.librede.estimation.models.diff;

import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.IStateConstraint;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public final class HessianMatrixBuilder {
	
	public static Matrix calculateOfOutputFunction(IOutputFunction function, Vector x) {
		if (function instanceof IDifferentiableFunction) {
			return ((IDifferentiableFunction)function).getSecondDerivatives(x);
		} else {
			throw new IllegalStateException("Output function cannot be derived.");
		}
	}
	
	public static Matrix calculateOfConstraint(IStateConstraint constraint, Vector x) {
		if (constraint instanceof IDifferentiableFunction) {
			return ((IDifferentiableFunction)constraint).getSecondDerivatives(x);
		} else {
			throw new IllegalStateException("Constraint function cannot be derived.");
		}
	}

}
