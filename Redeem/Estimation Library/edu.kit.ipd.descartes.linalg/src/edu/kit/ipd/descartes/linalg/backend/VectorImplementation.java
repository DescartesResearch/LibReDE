package edu.kit.ipd.descartes.linalg.backend;

import edu.kit.ipd.descartes.linalg.Range;

public interface VectorImplementation extends MatrixImplementation {
	
	VectorImplementation slice(Range range);
	
	double dot(VectorImplementation b);
	
	VectorImplementation copyAndSet(Range rows, VectorImplementation values);

}
