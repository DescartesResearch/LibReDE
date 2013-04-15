package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class Scalar extends Vector {
	
	public static final Scalar ZERO = new Scalar(0);
	public static final Scalar ONE = new Scalar(1);
	
	private double value;
	
	protected Scalar(double value) {
		this.value = value;
	}
	
	public double getValue() {
		return value;
	}
	
	@Override
	public double get(int row) {
		if (row > 0) {
			throw new IndexOutOfBoundsException();
		}
		return value;
	}

	@Override
	public int rows() {
		return 1;
	}

	@Override
	public double dot(Vector b) {
		if (!b.isScalar()) {
			throw new IllegalArgumentException("Dimensions of operands do not match.");
		}		
		return value * ((Scalar)b).value;
	}

	@Override
	public Scalar times(double d) {
		return new Scalar(value * d);
	}

	@Override
	public Scalar plus(Matrix a) {
		if (!a.isScalar()) {
			throw new IllegalArgumentException("Dimensions of operands do not match.");
		}
		return new Scalar(value + ((Scalar)a).value);
	}

	@Override
	public Scalar plus(double d) {
		return new Scalar(value + d);
	}

	@Override
	public Scalar minus(Matrix a) {
		if (!a.isScalar()) {
			throw new IllegalArgumentException("Dimensions of operands do not match.");
		}
		return new Scalar(value - ((Scalar)a).value);
	}

	@Override
	public Scalar minus(double d) {
		return new Scalar(value - d);
	}
	
	@Override
	public Matrix multipliedBy(Matrix a) {
		return a.times(value);
	}

	@Override
	public double[] toArray1D() {
		return new double[] { value };
	}

	@Override
	public void toDoubleStorage(DoubleStorage storage) {
		storage.write(new double[] { value });
	}

	@Override
	protected Scalar abs() {
		return new Scalar(Math.abs(value));
	}

	@Override
	protected double sum() {
		return value;
	}

	@Override
	protected double norm1() {
		return Math.abs(value);
	}

	@Override
	protected double norm2() {
		return value * value;
	}
	
	@Override
	public boolean isScalar() {
		return true;
	}
	
	@Override
	public boolean isVector() {
		return false;
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
	public Scalar slice(Range range) {
		if (range.getStart() != 0 && range.getEnd() != 1) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}

	@Override
	protected Matrix appendRows(Matrix a) {
		if (a.columns() != 1) {
			throw new IllegalArgumentException("Number of columns must be equal.");
		}
		return LinAlg.vector(this, (Vector)a);
	}

	@Override
	protected Matrix appendColumns(Matrix a) {
		if (a.rows() != 1) {
			throw new IllegalArgumentException("Number of rows must be equal.");
		}
		return LinAlg.vector(this, a.row(0)).transpose();
	}

}
