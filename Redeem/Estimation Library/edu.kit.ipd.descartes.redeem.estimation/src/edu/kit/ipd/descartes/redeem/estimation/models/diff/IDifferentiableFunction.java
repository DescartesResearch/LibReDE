package edu.kit.ipd.descartes.redeem.estimation.models.diff;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public interface IDifferentiableFunction {
	
	Vector getFirstDerivatives(Vector x);
	
	Matrix getSecondDerivatives(Vector x);

}
