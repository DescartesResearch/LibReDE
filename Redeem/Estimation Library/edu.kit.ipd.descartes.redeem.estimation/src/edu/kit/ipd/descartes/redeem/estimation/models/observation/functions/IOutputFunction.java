package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import edu.kit.ipd.descartes.linalg.Vector;

public interface IOutputFunction {
	
	double getObservedOutput();
	
	double getCalculatedOutput(Vector state);
	
}
