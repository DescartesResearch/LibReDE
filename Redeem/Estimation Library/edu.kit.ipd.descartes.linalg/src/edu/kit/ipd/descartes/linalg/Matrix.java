package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.impl.colt.ColtMatrixFactory;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public abstract class Matrix {

	private static MatrixFactory factory = new ColtMatrixFactory();

	public static Matrix zeros(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new IllegalArgumentException();
		} else	if (columns == 1) {
			if (rows == 1) {
				return Scalar.ZERO;
			} else {
				return factory.createVector(rows, 0);
			}
		} else {
			return factory.createMatrix(rows, columns, 0);
		}
	}

	public static Matrix ones(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new IllegalArgumentException();
		} else	if (columns == 1) {
			if (rows == 1) {
				return Scalar.ONE;
			} else {
				return factory.createVector(rows, 1);
			}
		} else {
			return factory.createMatrix(rows, columns, 1);
		}
	}

	public static Matrix identity(int size) {
		if (size < 1) {
			throw new IllegalArgumentException();
		} else if (size == 1) {
			return Scalar.ONE;
		} else {
			return factory.createMatrix(size, size, new MatrixInitializer() {
				@Override
				public double cell(int row, int column) {
					if (row == column) {
						return 1.0;
					}
					return 0.0;
				}
			});
		}
	}

	public static Matrix matrix(double[]... values) {
		if (values.length < 1) {
			throw new IllegalArgumentException();
		} else {
			int columns = values[0].length;
			for (int i = 1; i < values.length; i++) {
				if (values[i].length != columns) {
					throw new IllegalArgumentException("Array must be rectangular");
				}
			}
			
			if (columns == 1) {
				if (values.length == 1) {
					return new Scalar(values[0][0]);
				} else {
					final double[][] temp = values;
					return factory.createVector(values.length, new VectorInitializer() {					
						@Override
						public double cell(int row) {
							return temp[row][0];
						}
					});
				}
			} else {
				return factory.createMatrix(values);
			}			
		}
	}

	public static Matrix matrix(int rows, int columns, MatrixInitializer init) {
		if (rows < 1 || columns < 1) {
			throw new IllegalArgumentException();
		} else	if (columns == 1) {
			if (rows == 1) {
				return new Scalar(init.cell(0, 0));
			} else {	
				final MatrixInitializer temp = init;
				return factory.createVector(rows, new VectorInitializer() {					
					@Override
					public double cell(int row) {
						return temp.cell(row, 0);
					}
				});
			}
		} else {
			return factory.createMatrix(rows, columns, init);
		}
	}

	public static Matrix matrix(int rows, int columns, DoubleStorage storage) {
		if (rows < 1 || columns < 1) {
			throw new IllegalArgumentException();
		} else	if (columns == 1) {
			if (rows == 1) {
				double[] temp = new double[1];
				storage.read(temp);
				return new Scalar(temp[0]);
			} else {				
				return factory.createVector(rows, storage);
			}
		} else {
			return factory.createMatrix(rows, columns, storage);
		}
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

	public static Matrix transpose(Matrix a) {
		return a.transpose();
	}

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
}
