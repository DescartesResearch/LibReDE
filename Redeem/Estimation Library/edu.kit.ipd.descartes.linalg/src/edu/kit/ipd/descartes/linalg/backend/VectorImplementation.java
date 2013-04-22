package edu.kit.ipd.descartes.linalg.backend;

import edu.kit.ipd.descartes.linalg.Range;
import edu.kit.ipd.descartes.linalg.Vector;

public interface VectorImplementation extends MatrixImplementation {
	
	double get(int row);
	
	VectorImplementation slice(Range range);
	
	double dot(VectorImplementation b);

}
