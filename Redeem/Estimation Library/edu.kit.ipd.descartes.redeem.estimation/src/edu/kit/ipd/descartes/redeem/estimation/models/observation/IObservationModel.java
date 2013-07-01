package edu.kit.ipd.descartes.redeem.estimation.models.observation;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;

public interface IObservationModel<E extends IOutputFunction, O extends Vector> extends Iterable<E> {
	
	int getOutputSize();
	
	O getObservedOutput();
	
	O getCalculatedOutput(Vector state);
	
	E getOutputFunction(int output);
	
}
