package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public abstract class Matrix {

	public abstract double get(int row, int col);

	public abstract int rows();

	public abstract int columns();

	public abstract Vector row(int row);

	public abstract Vector column(int column);
	
	public boolean isVector() {
		return false;
	}
	
	public boolean isScalar() {
		return false;
	}

	/*
	 * Algebra functions
	 */

	public Matrix plus(Matrix a) {
		if (a.isScalar() || a.isVector()) {
			throw new IllegalArgumentException("Dimensions of operands do not match.");
		} else {
			return this.internalPlus(a);
		}
	}

	public abstract Matrix plus(double a);

	public Matrix minus(Matrix a) {
		if (a.isScalar() || a.isVector()) {
			throw new IllegalArgumentException("Dimensions of operands do not match.");
		} else {
			return this.internalMinus(a);
		}
	}

	public abstract Matrix minus(double a);

	public Matrix multipliedBy(Matrix a) {
		if (a.isScalar()) {
			return this.times(((Scalar)a).getValue());
		} else {
			return this.internalMatrixMultiply(a);
		}
	}

	public abstract Matrix times(double a);
	
	protected abstract Matrix internalPlus(Matrix a);
	
	protected abstract Matrix internalMinus(Matrix a);
	
	protected abstract Matrix internalMatrixMultiply(Matrix a);

	/*
	 * Conversion functions
	 */
	
	public abstract double[] toArray1D();

	public abstract double[][] toArray2D();

	public abstract void toDoubleStorage(DoubleStorage storage);

	/*
	 * Internal functions
	 */

	protected abstract Matrix abs();

	protected abstract double sum();

	protected abstract double norm1();

	protected abstract double norm2();

	protected abstract Matrix transpose();
	
	protected abstract Matrix appendRows(Matrix a);
	
	protected abstract Matrix appendColumns(Matrix a);
}
