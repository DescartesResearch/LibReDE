package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.backend.MatrixFactory;
import edu.kit.ipd.descartes.linalg.backend.colt.ColtMatrixFactory;

public class LinAlg {
	
	static final MatrixFactory FACTORY = new ColtMatrixFactory();

	public static Matrix zeros(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new IllegalArgumentException();
		} else	if (columns == 1) {
			if (rows == 1) {
				return Scalar.ZERO;
			} else {
				return FACTORY.createVector(rows, 0);
			}
		} else {
			if (rows == columns) {
				return FACTORY.createSquareMatrix(rows, 0);
			} else {
				return FACTORY.createMatrix(rows, columns, 0);
			}
		}
	}

	public static Matrix ones(int rows, int columns) {
		if (rows < 1 || columns < 1) {
			throw new IllegalArgumentException();
		} else	if (columns == 1) {
			if (rows == 1) {
				return Scalar.ONE;
			} else {
				return FACTORY.createVector(rows, 1);
			}
		} else {
			if (rows == columns) {
				return FACTORY.createSquareMatrix(rows, 1);
			} else {
				return FACTORY.createMatrix(rows, columns, 1);
			}
		}
	}

	public static Matrix identity(int size) {
		if (size < 1) {
			throw new IllegalArgumentException();
		} else if (size == 1) {
			return Scalar.ONE;
		} else {
			return FACTORY.createSquareMatrix(size, new MatrixFunction() {
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
					return FACTORY.createVector(values.length, new VectorFunction() {					
						@Override
						public double cell(int row) {
							return temp[row][0];
						}
					});
				}
			} else {
				if (columns == values.length) {
					return FACTORY.createSquareMatrix(values);
				} else {
					return FACTORY.createMatrix(values);
				}
			}			
		}
	}

	public static Matrix matrix(int rows, int columns, MatrixFunction init) {
		if (rows < 1 || columns < 1) {
			throw new IllegalArgumentException();
		} else	if (columns == 1) {
			if (rows == 1) {
				return new Scalar(init.cell(0, 0));
			} else {	
				final MatrixFunction temp = init;
				return FACTORY.createVector(rows, new VectorFunction() {					
					@Override
					public double cell(int row) {
						return temp.cell(row, 0);
					}
				});
			}
		} else {
			if (rows == columns) {
				return FACTORY.createSquareMatrix(rows, init);
			} else {
				return FACTORY.createMatrix(rows, columns, init);
			}
		}
	}

	public static double[] row(double... values) {
		return values;
	}
	
	public static Matrix vertcat(Matrix...rows) {
		if (rows.length < 1) {
			throw new IllegalArgumentException("At least one row required.");
		}		
		Matrix res = rows[0];
		for (int i = 1; i < rows.length; i++) {
			res = res.appendRows(rows[i]);
		}
		return res;
	}
	
	public static Matrix horzcat(Matrix...cols) {
		if (cols.length < 1) {
			throw new IllegalArgumentException("At least one column required.");
		}
		Matrix res = cols[0];
		for (int i = 1; i < cols.length; i++) {
			res = res.appendColumns(cols[i]);
		}
		return res;
	}
	
	public static Matrix repmat(Matrix a, int vertical, int horizontal) {
		Matrix res = a;
		for (int i = 1; i < horizontal; i++) {
			res = res.appendColumns(a);
		}
		
		Matrix row = res;
		for (int i = 1; i < vertical; i++) {
			res = res.appendRows(row);
		}
		return res;
	}

	public static <M extends Matrix> M abs(M a) {
		return a.abs();
	}
	
	public static double mean(Vector a) {
		return a.mean();
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
	
	public static Range range(int start, int end) {
		return new Range(start, end);
	}
	
	public static Scalar scalar(double value) {
		return new Scalar(value);
	}
	
	public static SquareMatrix square(int size, double fill) {
		if (size < 1) {
			throw new IllegalArgumentException();
		}
		return FACTORY.createSquareMatrix(size, fill);
	}
	
	public static SquareMatrix square(int size, MatrixFunction init) {
		if (size < 1) {
			throw new IllegalArgumentException();
		}
		return FACTORY.createSquareMatrix(size, init);
	}

	public static double det(SquareMatrix a) {
		return a.det();
	}

	public static SquareMatrix inverse(SquareMatrix a) {
		return a.inverse();
	}

	public static double rank(SquareMatrix a) {
		return a.rank();
	}

	public static double trace(SquareMatrix a) {
		return a.trace();
	}
	
	public static SquareMatrix pow(SquareMatrix a, int p) {
		return a.pow(p);		
	}
	
	public static int[] sort(Matrix a, int column) {
		return a.sort(column);
	}
	
	public static Vector zeros(int rows) {
		if (rows < 1) {
			throw new IllegalArgumentException();
		} else if (rows == 1) {
			return Scalar.ZERO;
		} else {
			return FACTORY.createVector(rows, 0);
		}
	}


	public static Vector ones(int rows) {
		if (rows < 1) {
			throw new IllegalArgumentException();
		} else if (rows == 1) {
			return Scalar.ONE;
		} else {
			return FACTORY.createVector(rows, 1);
		}
	}

	public static Vector vector(double... values) {
		if (values.length == 1) {
			return new Scalar(values[0]);
		} else {
			return FACTORY.createVector(values);
		}
	}

	public static Vector vector(int rows, VectorFunction init) {
		if (rows < 1) {
			throw new IllegalArgumentException();
		} else	if (rows == 1) {
			return new Scalar(init.cell(0));
		} else {
			return FACTORY.createVector(rows, init);
		}
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
}
