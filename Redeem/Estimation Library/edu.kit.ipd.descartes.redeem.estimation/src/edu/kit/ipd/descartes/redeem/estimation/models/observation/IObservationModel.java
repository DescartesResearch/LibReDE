package edu.kit.ipd.descartes.redeem.estimation.models.observation;

import edu.kit.ipd.descartes.linalg.Vector;

public interface IObservationModel {
	
	int getObservationSize();
	
	boolean nextObservation();
	
	Vector getObservedOutputVector();
	
	Vector getCalculatedOutputVector(Vector state);

}
