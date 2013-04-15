package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import edu.kit.ipd.descartes.linalg.Vector;

public interface ILinearOutputFunction extends IOutputFunction {
	
	Vector getIndependentVariables();

}
