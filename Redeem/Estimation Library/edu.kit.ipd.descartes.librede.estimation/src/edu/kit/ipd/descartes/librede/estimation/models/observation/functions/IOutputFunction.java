package edu.kit.ipd.descartes.librede.estimation.models.observation.functions;

import edu.kit.ipd.descartes.linalg.Vector;

public interface IOutputFunction {
	
	boolean isApplicable();
	
	double getObservedOutput();
	
	double getCalculatedOutput(Vector state);
	
}
