package edu.kit.ipd.descartes.redeem.estimation.io;

import edu.kit.ipd.descartes.linalg.Vector;

public interface MeasurementStream {
	
	public int size();
	
	public boolean nextSample();
	
	public Vector getCurrentSample();

}
