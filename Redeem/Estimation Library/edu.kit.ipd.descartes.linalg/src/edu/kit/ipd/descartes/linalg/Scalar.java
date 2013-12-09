package edu.kit.ipd.descartes.linalg;


public class Scalar implements Vector {

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

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Override
	public Scalar transpose() {
		return this;
	}

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
	@Override
	public Scalar plus(double a) {
		return new Scalar(value + a);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Scalar minus(double a) {
		return new Scalar(value - a);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Scalar times(double a) {
		return new Scalar(value * a);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Scalar plus(Matrix a) {
		if (!a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return new Scalar(value	+ ((Scalar) a).value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Scalar arrayMultipliedBy(Matrix a) {
		if (!a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return new Scalar(value * ((Scalar) a).value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Scalar minus(Matrix a) {
		if (!a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return new Scalar(value	- ((Scalar) a).value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Scalar multipliedBy(Matrix a) {
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

	@SuppressWarnings("unchecked")
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
	public int[] sort(int column) {
		if (column != 0) {
			throw new IndexOutOfBoundsException();
		}
		return new int[] { 0 };
	}

	@Override
	public double get(int row) {
		if (row != 0) {
			throw new IndexOutOfBoundsException();
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Scalar set(int row, int col, double value) {
		if (col != 0) {
			throw new IndexOutOfBoundsException();
		}
		return set(row, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Scalar set(int row, double value) {
		if (row != 0) {
			throw new IndexOutOfBoundsException();
		}
		return new Scalar(value);
	}

	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
	@Override
	public Scalar subset(int[] indeces) {
		if (indeces.length != 1 || indeces[0] != 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}
}
