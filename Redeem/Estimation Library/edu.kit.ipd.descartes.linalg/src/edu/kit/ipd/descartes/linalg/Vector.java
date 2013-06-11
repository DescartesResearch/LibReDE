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
		return ((VectorImplementation)delegate).get(row, 0);
	}
	
	public Vector set(int row, double value) {
		return set(row, 0, value);
	}
	
	public Vector set(Range rows, Vector values) {
		return newInstance(((VectorImplementation)delegate).copyAndSet(rows, (VectorImplementation)values.delegate));
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
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < rows(); i++) {
			if (i > 0) {
				builder.append("; ");
			}
			builder.append(get(i));
		}
		builder.append("]");
		return builder.toString();
	}
	
}
