package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.backend.colt.ColtVector;


public class Scalar implements Vector, SquareMatrix {

	public static final Scalar ZERO = new Scalar(0);
	public static final Scalar ONE = new Scalar(1);

	private double value;

	public Scalar(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
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
	public boolean isEmpty() {
		return false;
	}

	@Override
	public Scalar abs() {
		return new Scalar(Math.abs(value));
	}

	@Override
	public double mean() {
		return value;
	}
	
	@Override
	public double sum() {
		return value;
	}

	@Override
	public double norm1() {
		return Math.abs(value);
	}

	@Override
	public double norm2() {
		return value * value;
	}

	@Override
	public Scalar transpose() {
		return this;
	}

	@Override
	public Vector appendRows(Matrix a) {
		if (a.columns() != 1) {
			throw new IllegalArgumentException(
					"Number of columns must be equal.");
		}
		double[] vector = new double[a.rows() + 1];
		vector[0] = value;
		System.arraycopy(a.toArray1D(), 0, vector, 1, a.rows());
		
		
		return LinAlg.FACTORY.createVector(vector);
	}

	@Override
	public Matrix appendColumns(Matrix a) {
		if (a.rows() != 1) {
			throw new IllegalArgumentException("Number of rows must be equal.");
		}
		double[][] matrix = new double[1][a.columns() + 1];
		matrix[0][0] = value;
		System.arraycopy(a.toArray1D(), 0, matrix[0], 1, a.columns());
		return LinAlg.FACTORY.createMatrix(matrix);
	}

	@Override
	public double get(int row, int col) {
		if (row != 0 || col != 0) {
			throw new IndexOutOfBoundsException();
		}
		return value;
	}

	@Override
	public int rows() {
		return 1;
	}

	@Override
	public int columns() {
		return 1;
	}

	@Override
	public Scalar row(int row) {
		if (row != 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}

	@Override
	public Scalar column(int column) {
		if (column != 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}

	@Override
	public Scalar plus(double a) {
		return new Scalar(value + a);
	}

	@Override
	public Scalar minus(double a) {
		return new Scalar(value - a);
	}

	@Override
	public Scalar times(double a) {
		return new Scalar(value * a);
	}

	@Override
	public Scalar plus(Matrix a) {
		if (!a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return new Scalar(value	+ ((Scalar) a).value);
	}

	@Override
	public Scalar arrayMultipliedBy(Matrix a) {
		if (!a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return new Scalar(value * ((Scalar) a).value);
	}

	@Override
	public Scalar minus(Matrix a) {
		if (!a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return new Scalar(value	- ((Scalar) a).value);
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
	public double[][] toArray2D() {
		return new double[][] { { value } };
	}

	@Override
	public Scalar slice(Range range) {
		if (range.getStart() != 0 || range.getEnd() != 1) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}

	@Override
	public double dot(Vector b) {
		if (!b.isScalar()) {
			throw new IllegalArgumentException(
					"Dimensions of operands do not match.");
		}
		return value * ((Scalar) b).value;
	}

	@Override
	public Scalar sort(int column) {
		if (column != 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}

	@Override
	public double get(int row) {
		if (row != 0) {
			throw new IndexOutOfBoundsException();
		}
		return value;
	}

	@Override
	public Scalar set(int row, int col, double value) {
		if (col != 0) {
			throw new IndexOutOfBoundsException();
		}
		return set(row, value);
	}

	@Override
	public Scalar set(int row, double value) {
		if (row != 0) {
			throw new IndexOutOfBoundsException();
		}
		return new Scalar(value);
	}

	@Override
	public Scalar set(Range rows, Vector values) {
		if (rows.getStart() != 0 || rows.getEnd() != 1) {
			throw new IndexOutOfBoundsException();
		}
		if (values.rows() != 1) {
			throw new IllegalArgumentException(
					"Size of values vector must match range specification.");
		}
		return new Scalar(values.get(0, 0));
	}
	
	@Override
	public Scalar subset(int...indeces) {
		if (indeces.length != 1 || indeces[0] != 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}
	
	public Scalar subset(int start, int end) {
		if (start != 0 || end != 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}
	
	@Override
	public Vector insertRow(int row, double... values) {
		if (values.length != 1) {
			throw new IllegalArgumentException();
		}
		if (row < 0 || row > 1) {
			throw new IndexOutOfBoundsException();
		}
		if (row == 0) {
			return new ColtVector(values[0], value);
		} else {
			return new ColtVector(value, values[0]);
		}
	}
	
	@Override
	public String toString() {
		return "[" + value + "]";
	}

	@Override
	public double det() {
		return value;
	}

	@Override
	public Scalar inverse() {
		return new Scalar(1 / value);
	}

	@Override
	public Scalar pow(int a) {
		return new Scalar(Math.pow(value, a));
	}

	@Override
	public double rank() {
		return (value == 0) ? 0 : 1;
	}

	@Override
	public double trace() {
		return value;
	}
}
