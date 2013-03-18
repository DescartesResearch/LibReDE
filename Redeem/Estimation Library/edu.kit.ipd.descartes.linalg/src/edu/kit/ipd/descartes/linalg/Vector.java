package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.impl.colt.ColtMatrixFactory;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public abstract class Vector extends Matrix {

	private static MatrixFactory factory = new ColtMatrixFactory();

	public static Vector zeros(int rows) {
		if (rows < 1) {
			throw new IllegalArgumentException();
		} else if (rows == 1) {
			return Scalar.ZERO;
		} else {
			return factory.createVector(rows, 0);
		}
	}


	public static Vector ones(int rows) {
		if (rows < 1) {
			throw new IllegalArgumentException();
		} else if (rows == 1) {
			return Scalar.ONE;
		} else {
			return factory.createVector(rows, 1);
		}
	}

	public static Vector vector(double... values) {
		if (values.length == 1) {
			return new Scalar(values[0]);
		} else {
			return factory.createVector(values);
		}
	}

	public static Vector vector(int rows, VectorInitializer init) {
		if (rows < 1) {
			throw new IllegalArgumentException();
		} else	if (rows == 1) {
			return new Scalar(init.cell(0));
		} else {
			return factory.createVector(rows, init);
		}
	}

	public static Vector vector(int rows, DoubleStorage storage) {
		if (rows < 1) {
			throw new IllegalArgumentException();
		} else	if (rows == 1) {
			double[] temp = new double[1];
			storage.read(temp);
			return new Scalar(temp[0]);
		} else {
			return factory.createVector(rows, storage);
		}
	}

	public static Vector vector(Vector... vectors) {
		return factory.createVector(vectors);
	}
	
	public static Vector abs(Vector v) {
		return (Vector)v.abs();
	}

	public static double sum(Vector v) {
		return v.sum();
	}

	public static double norm1(Vector v) {
		return v.norm1();
	}

	public static double norm2(Vector v) {
		return v.norm2();
	}

	public static int min(Vector v) {
		double min = Double.MAX_VALUE;
		int idx = -1;
		for (int i = 0; i < v.rows(); i++) {
			if (v.get(i) < min) {
				min = v.get(i);
				idx = i;
			}
		}
		return idx;
	}

	public static int max(Vector v) {
		double max = Double.MIN_VALUE;
		int idx = -1;
		for (int i = 0; i < v.rows(); i++) {
			if (v.get(i) > max) {
				max = v.get(i);
				idx = i;
			}
		}
		return idx;
	}

	public abstract double get(int row);

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
		return Scalar.scalar(get(row));
	}
	
	@Override
	protected Matrix transpose() {
		return Matrix.matrix(toArray1D());
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
