package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.impl.colt.ColtMatrixFactory;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public abstract class Matrix {

	private static MatrixFactory factory = new ColtMatrixFactory();

	public static Matrix zeros(int rows, int columns) {
		return factory.create(rows, columns);
	}

	public static Matrix ones(int rows, int columns) {
		return factory.create(rows, columns, 1);
	}

	public static Matrix identity(int size) {
		return factory.create(size, size, new MatrixInitializer() {
			@Override
			public double cell(int row, int column) {
				if (row == column) {
					return 1.0;
				}
				return 0.0;
			}
		});
	}

	public static Matrix matrix(double[]... values) {
		return factory.create(values);
	}

	public static Matrix matrix(int rows, int columns, MatrixInitializer init) {
		return factory.create(rows, columns, init);
	}

	public static Matrix matrix(int rows, int columns, DoubleStorage storage) {
		return factory.create(rows, columns, storage);
	}

	public static double[] row(double... values) {
		return values;
	}

	public static Matrix abs(Matrix a) {
		return a.abs();
	}

	public static double sum(Matrix a) {
		return a.sum();
	}

	public static double norm1(Matrix a) {
		return a.norm1();
	}

	public static double norm2(Matrix a) {
		return a.norm2();
	}

	public static double det(Matrix a) {
		return a.det();
	}

	public static Matrix inverse(Matrix a) {
		return a.inverse();
	}

	public static double rank(Matrix a) {
		return a.rank();
	}

	public static double trace(Matrix a) {
		return a.trace();
	}

	public static Matrix transpose(Matrix a) {
		return a.transpose();
	}

	public static Matrix pow(Matrix a, int p) {
		return a.pow(p);
	}

	public abstract double get(int row, int col);

	public abstract int rows();

	public abstract int columns();

	public abstract Vector row(int row);

	public abstract Vector column(int column);
	
	public abstract Matrix appendRow(Vector row);

	/*
	 * Algebra functions
	 */

	public abstract Matrix plus(Matrix a);

	public abstract Matrix plus(double a);

	public abstract Matrix minus(Matrix a);

	public abstract Matrix minus(double a);

	public abstract Vector multipliedBy(Vector a);

	public abstract Matrix multipliedBy(Matrix a);

	public abstract Matrix times(double a);

	/*
	 * Conversion functions
	 */

	public abstract double[][] toArray();

	public abstract void toDoubleStorage(DoubleStorage storage);

	/*
	 * Internal functions
	 */

	protected abstract Matrix abs();

	protected abstract double sum();

	protected abstract double norm1();

	protected abstract double norm2();

	protected abstract double det();

	protected abstract Matrix inverse();

	protected abstract double rank();

	protected abstract double trace();

	protected abstract Matrix transpose();

	protected abstract Matrix pow(int p);

}
