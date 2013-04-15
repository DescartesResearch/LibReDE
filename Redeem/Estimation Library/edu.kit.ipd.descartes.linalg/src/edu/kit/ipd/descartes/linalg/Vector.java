package edu.kit.ipd.descartes.linalg;


public abstract class Vector extends Matrix {

	public abstract double get(int row);
	
	public abstract Vector slice(Range range);

	/*
	 * Algebra functions
	 */

	public abstract double dot(Vector b);

	public abstract Vector times(double d);

	public abstract Vector plus(double d);

	public abstract Vector minus(double d);

	/*
	 * Matrix functions
	 */
	
	@Override
	public Vector column(int column) {
		if (column > 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}
	
	@Override
	public int columns() {
		return 1;
	}
	
	@Override
	public double get(int row, int col) {
		if (col > 0) {
			throw new IndexOutOfBoundsException();
		}
		return get(row);
	}
	
	@Override
	public Vector minus(Matrix a) {
		if (!a.isVector()) {
			throw new IllegalArgumentException("Dimensions of operands do not match.");
		} else {
			return (Vector)this.internalMinus(a);
		}
	}
	
	@Override
	public Matrix multipliedBy(Matrix a) {
		if (a.isVector()) {
			throw new IllegalArgumentException("Dimensions of operands do not match.");
		} else if (a.isScalar()) {
			return this.times(((Scalar)a).getValue());
		} else {
			return this.internalMatrixMultiply(a);
		}
	}
	
	@Override
	public Vector plus(Matrix a) {
		if (!a.isVector()) {
			throw new IllegalArgumentException("Dimensions of operands do not match.");
		} else {
			return (Vector)this.internalPlus(a);
		}
	}
	
	@Override
	public Scalar row(int row) {
		return LinAlg.scalar(get(row));
	}
	
	@Override
	protected Matrix transpose() {
		return LinAlg.matrix(toArray1D());
	}
	
	@Override
	public double[][] toArray2D() {
		double[][] temp = new double[rows()][1];
		for (int i = 0; i < rows(); i++) {
			temp[i][0] = this.get(i);
		}
		return temp;
	}
	
	@Override
	public boolean isVector() {
		return true;
	}
	
}
