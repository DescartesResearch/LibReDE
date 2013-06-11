package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.backend.MatrixImplementation;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class Matrix {
	
	final MatrixImplementation delegate;
	
	Matrix(MatrixImplementation delegate)  {
		this.delegate = delegate;
	}
	
	@SuppressWarnings("unchecked")
	<M extends Matrix> M newInstance(MatrixImplementation delegate) {
		return (M) new Matrix(delegate);
	}

	public double get(int row, int col) {
		return delegate.get(row, col);
	}
	
	public <M extends Matrix> M set(int row, int col, double value) {
		return newInstance(delegate.copyAndSet(row, col, value));
	}

	public int rows() {
		return delegate.rows();
	}

	public int columns() {
		return delegate.columns();
	}

	public Vector row(int row) {
		return new Vector(delegate.row(row));
	}

	public Vector column(int column) {
		return new Vector(delegate.column(column));
	}
	
	public boolean isVector() {
		return false;
	}
	
	public boolean isScalar() {
		return false;
	}

	/*
	 * Algebra functions
	 */

	public <M extends Matrix> M plus(M a) {
		if (a.columns() != this.columns() || a.rows() != this.rows()) {
			throw new IllegalArgumentException("Both operands must have the same size.");
		}
		return newInstance(delegate.plus(a.delegate));
	}

	public <M extends Matrix> M plus(double a) {
		return newInstance(delegate.plus(a));
	}

	public <M extends Matrix> M minus(M a) {
		if (a.columns() != this.columns() || a.rows() != this.rows()) {
			throw new IllegalArgumentException("Both operands must have the same size.");
		}
		return newInstance(delegate.minus(a.delegate));
	}

	public <M extends Matrix> M minus(double a) {
		return newInstance(delegate.minus(a));
	}

	public <M extends Matrix> M multipliedBy(M a) {
		if (columns() != a.rows()) {
			throw new IllegalArgumentException("Inner dimensions of operands must be equal.");
		} else {
			if (a.isScalar()) {
				return this.times(((Scalar)a).getValue());
			} else {
				return newInstance(delegate.multipliedBy(a.delegate));
			}
		}
	}
	
	public <M extends Matrix> M arrayMultipliedBy(M a) {
		if (a.columns() != this.columns() || a.rows() != this.rows()) {
			throw new IllegalArgumentException("Both operands must have the same size.");
		}
		return newInstance(delegate.arrayMultipliedBy(a.delegate));
	}

	public <M extends Matrix> M times(double a) {
		return newInstance(delegate.times(a));
	}
	
	/*
	 * Conversion functions
	 */
	
	public double[] toArray1D() {
		return delegate.toArray1D();
	}

	public double[][] toArray2D() {
		return delegate.toArray2D();
	}

	public void toDoubleStorage(DoubleStorage storage) {
		delegate.toDoubleStorage(storage);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < rows(); i++) {
			builder.append("[");
			for (int j = 0; j < columns(); j++) {
				if (j > 0) {
					builder.append("; ");
				}
				builder.append(get(i, j));
			}
			builder.append("]");
		}
		builder.append("]");
		return builder.toString();
	}

}
