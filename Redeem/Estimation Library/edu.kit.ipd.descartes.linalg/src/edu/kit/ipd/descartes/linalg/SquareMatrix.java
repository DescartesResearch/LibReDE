package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public abstract class SquareMatrix extends Matrix {

	protected Matrix delegate;
	
	protected SquareMatrix(Matrix wrappedMatrix) {
		if (wrappedMatrix.columns() != wrappedMatrix.rows()) {
			throw new IllegalArgumentException("Matrix is not square");
		}
		this.delegate = wrappedMatrix;
	}

	public double get(int row, int col) {
		return delegate.get(row, col);
	}

	public int columns() {
		return delegate.columns();
	}

	public Vector column(int column) {
		return delegate.column(column);
	}

	public boolean equals(Object obj) {
		return delegate.equals(obj);
	}

	public int hashCode() {
		return delegate.hashCode();
	}

	public int rows() {
		return delegate.rows();
	}

	public Vector row(int row) {
		return delegate.row(row);
	}

	public boolean isVector() {
		return delegate.isVector();
	}

	public boolean isScalar() {
		return delegate.isScalar();
	}

	public SquareMatrix plus(Matrix a) {
		return LinAlg.square(delegate.plus(a));
	}

	public SquareMatrix plus(double a) {
		return LinAlg.square(delegate.plus(a));
	}

	public SquareMatrix minus(Matrix a) {
		return LinAlg.square(delegate.minus(a));
	}

	public SquareMatrix minus(double a) {
		return LinAlg.square(delegate.minus(a));
	}

	public SquareMatrix multipliedBy(Matrix a) {
		return LinAlg.square(delegate.multipliedBy(a));
	}

	public SquareMatrix times(double a) {
		return LinAlg.square(delegate.times(a));
	}

	public double[] toArray1D() {
		return delegate.toArray1D();
	}

	public double[][] toArray2D() {
		return delegate.toArray2D();
	}

	public void toDoubleStorage(DoubleStorage storage) {
		delegate.toDoubleStorage(storage);
	}

	public String toString() {
		return delegate.toString();
	}

	@Override
	protected Matrix internalPlus(Matrix a) {
		// This function should never be called.
		throw new UnsupportedOperationException();
	}

	@Override
	protected Matrix internalMinus(Matrix a) {
		// This function should never be called.
		throw new UnsupportedOperationException();
	}

	@Override
	protected Matrix internalMatrixMultiply(Matrix a) {
		// This function should never be called.
		throw new UnsupportedOperationException();
	}

	@Override
	protected SquareMatrix abs() {
		return LinAlg.square(delegate.abs());
	}

	@Override
	protected double sum() {
		return delegate.sum();
	}

	@Override
	protected double norm1() {
		return delegate.norm1();
	}

	@Override
	protected double norm2() {
		return delegate.norm2();
	}

	@Override
	protected SquareMatrix transpose() {
		return LinAlg.square(delegate.transpose());
	}
	
	@Override
	protected Matrix appendColumns(Matrix a) {
		return delegate.appendColumns(a);
	}
	
	@Override
	protected Matrix appendRows(Matrix a) {
		return delegate.appendRows(a);
	}

	protected abstract double det();

	protected abstract SquareMatrix inverse();

	protected abstract double rank();

	protected abstract double trace();

	protected abstract SquareMatrix pow(int p);

}
