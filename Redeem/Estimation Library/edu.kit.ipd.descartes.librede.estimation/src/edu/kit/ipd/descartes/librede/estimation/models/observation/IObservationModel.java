package edu.kit.ipd.descartes.librede.estimation.models.observation;

import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.linalg.Vector;

public interface IObservationModel<E extends IOutputFunction, O extends Vector> extends Iterable<E> {
	
	int getOutputSize();
	
	O getObservedOutput();
	
	O getCalculatedOutput(Vector state);
	
	E getOutputFunction(int output);
	
}
