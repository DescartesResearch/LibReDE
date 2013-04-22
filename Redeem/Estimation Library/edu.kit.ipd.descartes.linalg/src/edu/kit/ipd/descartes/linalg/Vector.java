package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.backend.MatrixImplementation;
import edu.kit.ipd.descartes.linalg.backend.VectorImplementation;


public class Vector extends Matrix {
	
	Vector(VectorImplementation delegate)  {
		super(delegate);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	<M extends Matrix> M newInstance(MatrixImplementation delegate) {
		return (M) new Vector((VectorImplementation)delegate);
	}

	public double get(int row) {
		return ((VectorImplementation)delegate).get(row);
	}
	
	public Vector slice(Range range) {
		return new Vector(((VectorImplementation)delegate).slice(range));
	}

	public double dot(Vector b) {
		return ((VectorImplementation)delegate).dot((VectorImplementation)b.delegate);
	}
	
	@Override
	public Scalar row(int row) {
		return LinAlg.scalar(get(row));
	}
	
	@Override
	public boolean isVector() {
		return true;
	}
	
}
