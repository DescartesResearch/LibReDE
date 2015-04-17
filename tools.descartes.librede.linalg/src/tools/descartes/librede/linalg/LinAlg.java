/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.linalg;

import tools.descartes.librede.linalg.backend.Empty;
import tools.descartes.librede.linalg.backend.IndicesImpl;
import tools.descartes.librede.linalg.backend.MatrixFactory;
import tools.descartes.librede.linalg.backend.RangeImpl;
import tools.descartes.librede.linalg.backend.colt.ColtMatrixFactory;

/**
 * This class provides a number of static helper functions to create instances
 * of <code>Matrix</code>, <code>SquareMatrix</code>, <code>Vector</code>, and
 * <code>Scalar</code>. This class is designed to be included statically.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class LinAlg {

	static final MatrixFactory FACTORY = new ColtMatrixFactory();

	/**
	 * Create an empty <code>Matrix</code> (or <code>Vector</code>).
	 * 
	 * @return a 0x0 matrix instance.
	 * @since 1.0
	 */
	public static Empty empty() {
		return Empty.EMPTY;
	}

	/**
	 * Create a new <code>Matrix</code> instance filled with zeros.
	 * 
	 * @param rows
	 *            number of rows (rows >= 0).
	 * @param columns
	 *            number of columns (columns >= 0).
	 * @return a <code>Matrix</code>, <code>Vector</code>, or
	 *         <code>Scalar</code> instance.
	 * @throws IllegalArgumentException
	 *             if rows < 0 or columns < 0
	 * @since 1.0
	 */
	public static Matrix zeros(int rows, int columns) {
		if (rows < 0 || columns < 0) {
			throw new IllegalArgumentException();
		} else if (rows == 0 || columns == 0) {
			return Empty.EMPTY;
		} else if (columns == 1) {
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

	/**
	 * Create a new <code>Matrix</code> instance filled with ones.
	 * 
	 * @param rows
	 *            number of rows (rows >= 0).
	 * @param columns
	 *            number of columns (columns >= 0).
	 * @return a <code>Matrix</code>, <code>Vector</code>, or
	 *         <code>Scalar</code> instance.
	 * @throws IllegalArgumentException
	 *             if rows < 0 or columns < 0
	 * @since 1.0
	 */
	public static Matrix ones(int rows, int columns) {
		if (rows < 0 || columns < 0) {
			throw new IllegalArgumentException();
		} else if (rows == 0 || columns == 0) {
			return Empty.EMPTY;
		} else if (columns == 1) {
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

	/**
	 * Create a new <code>SquareMatrix</code> instance initialized with the
	 * identity matrix.
	 * 
	 * @param size
	 *            number of rows and columns (size >= 0).
	 * @return a <code>Matrix</code>, or <code>Scalar</code> instance.
	 * @throws IllegalArgumentException
	 *             if size < 0
	 * @since 1.0
	 */
	public static SquareMatrix identity(int size) {
		if (size < 0) {
			throw new IllegalArgumentException();
		} else if (size == 0) {
			return Empty.EMPTY;
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

	/**
	 * Create a new <code>Matrix</code> instance initialized with the given
	 * value.
	 * 
	 * @param rows
	 *            number of rows (rows >= 0).
	 * @param columns
	 *            number of columns (columns >= 0).
	 * @param value
	 *            a double value.
	 * @return a <code>Matrix</code>, <code>SquareMatrix</code>,
	 *         <code>Vector</code>, or <code>Scalar</code> instance.
	 * @throws IllegalArgumentException
	 *             if rows < 0 or columns < 0.
	 * @since 1.0
	 */
	public static Matrix matrix(int rows, int columns, double value) {
		if (columns < 0 || rows < 0) {
			throw new IllegalArgumentException();
		} else if (columns <= 1) {
			if (columns == 0 || rows == 0) {
				return empty();
			} else if (rows == 1) {
				return new Scalar(value);
			} else {
				return FACTORY.createVector(rows, value);
			}
		} else {
			if (columns == rows) {
				return FACTORY.createSquareMatrix(rows, value);
			} else {
				return FACTORY.createMatrix(rows, columns, value);
			}
		}
	}

	/**
	 * Create a new <code>Matrix</code> instance initialized with the given
	 * values.
	 * 
	 * @param values
	 *            a list of double arrays, each containing the values of one row
	 *            (all arrays must have the same length).
	 * @return a <code>Matrix</code>, <code>SquareMatrix</code>,
	 *         <code>Vector</code>, or <code>Scalar</code> instance.
	 * @throws IllegalArgumentException
	 *             if given values do not result in a rectangular array.
	 * @since 1.0
	 */
	public static Matrix matrix(double[]... values) {
		if (values.length < 1) {
			return Empty.EMPTY;
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

	/**
	 * Create a new <code>Matrix</code> instance initialized with the given
	 * function.
	 * 
	 * @param rows
	 *            number of rows (rows >= 0).
	 * @param columns
	 *            number of columns (columns >= 0).
	 * @param init
	 *            a function f(x,y) := z
	 * @return a <code>Matrix</code>, <code>SquareMatrix</code>,
	 *         <code>Vector</code>, or <code>Scalar</code> instance.
	 * @throws IllegalArgumentException
	 *             if rows < 0 or columns < 0.
	 * @since 1.0
	 */
	public static Matrix matrix(int rows, int columns, MatrixFunction init) {
		if (rows < 0 || columns < 0) {
			throw new IllegalArgumentException();
		} else if (rows == 0 || columns == 0) {
			return Empty.EMPTY;
		} else if (columns == 1) {
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

	/**
	 * A helper function for the initialization of a matrix.
	 * 
	 * @param values
	 *            double values of current row
	 * @return double array
	 * @see LinAlg#matrix(double[]...)
	 * @since 1.0
	 */
	public static double[] row(double... values) {
		return values;
	}

	/**
	 * Concatenates a set of matrices vertically.
	 * 
	 * @param first
	 *            base matrix.
	 * @param others
	 *            matrices to concatenate.
	 * @return concatenated matrix.
	 * @since 1.0
	 */
	public static Matrix vertcat(Matrix first, Matrix... others) {
		Matrix res = first;
		for (int i = 0; i < others.length; i++) {
			res = res.appendRows(others[i]);
		}
		return res;
	}

	/**
	 * Concatenates a set of vectors vertically.
	 * 
	 * @param first
	 *            base vector.
	 * @param others
	 *            vectors to concatenate.
	 * @return concatenated vector.
	 * @since 1.0
	 */
	public static Vector vertcat(Vector first, Vector... others) {
		return (Vector) vertcat((Matrix) first, (Matrix[]) others);
	}

	/**
	 * Concatenates an array of matrices vertically.
	 * 
	 * @param matrices
	 *            array of matrix (matrices.length >= 1).
	 * @return concatenated matrix.
	 * @throws IllegalArgumentException
	 *             if matrices.length < 1.
	 * @since 1.0
	 */
	public static Matrix vertcat(Matrix[] matrices) {
		if (matrices.length < 1) {
			throw new IllegalArgumentException();
		}
		Matrix res = matrices[0];
		for (int i = 1; i < matrices.length; i++) {
			res = res.appendRows(matrices[i]);
		}
		return res;
	}

	/**
	 * Concatenates an array of vectors vertically.
	 * 
	 * @param vectors
	 *            array of vectors (vectors.length >= 1).
	 * @return concatenated vector.
	 * @throws IllegalArgumentException
	 *             if vectors.length < 1.
	 * @since 1.0
	 */
	public static Vector vertcat(Vector[] vectors) {
		return (Vector) vertcat((Matrix[]) vectors);
	}

	/**
	 * Concatenates a set of matrices horizontally.
	 * 
	 * @param first
	 *            base matrix.
	 * @param others
	 *            matrices to concatenate.
	 * @return concatenated matrix.
	 * @since 1.0
	 */
	public static Matrix horzcat(Matrix first, Matrix... others) {
		Matrix res = first;
		for (int i = 0; i < others.length; i++) {
			res = res.appendColumns(others[i]);
		}
		return res;
	}

	/**
	 * Concatenates an array of matrices horizontally.
	 * 
	 * @param matrices
	 *            array of matrix (matrices.length >= 1)..
	 * @return concatenated matrix.
	 * @throws IllegalArgumentException
	 *             if matrices.length < 1.
	 * @since 1.0
	 */
	public static Matrix horzcat(Matrix[] matrices) {
		if (matrices.length < 1) {
			throw new IllegalArgumentException();
		}
		Matrix res = matrices[0];
		for (int i = 1; i < matrices.length; i++) {
			res = res.appendColumns(matrices[i]);
		}
		return res;
	}

	/**
	 * Replicates the given <code>Matrix</code> the specified times in vertical
	 * and/or horizontal direction.
	 * 
	 * @param a
	 *            (n x m) matrix to replicate.
	 * @param vertical
	 *            number of times the matrix is replicated vertically (vertical
	 *            >= 1). Set vertical=1 if no replication is desired.
	 * @param horizontal
	 *            number of times the matrix is replicated horizontally
	 *            (horizontal >= 1). Set horizontal=1 if no replication is
	 *            desired.
	 * @return a (n * vertical x m * horizontal) matrix
	 * @throws IllegalArgumentException
	 *             if vertical < 1 or horizontal < 1
	 * @since 1.0
	 */
	public static Matrix repmat(Matrix a, int vertical, int horizontal) {
		if (vertical < 1 || horizontal < 1) {
			throw new IllegalArgumentException();
		}
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

	/**
	 * Absolute value of <code>Matrix</code>.
	 * 
	 * @param a
	 * @return abs(a)
	 * @since 1.0
	 */
	public static Matrix abs(Matrix a) {
		return a.abs();
	}

	/**
	 * Absolute value of <code>Vector</code>.
	 * 
	 * @param a
	 * @return abs(a)
	 * @since 1.0
	 */
	public static Vector abs(Vector a) {
		return a.abs();
	}

	/**
	 * Absolute value of <code>Scalar</code>.
	 * 
	 * @param a
	 * @return abs(a)
	 * @since 1.0
	 */
	public static Scalar abs(Scalar a) {
		return a.abs();
	}

	/**
	 * Arithmetic mean of all elements (except NaN) in <code>Matrix</code>.
	 * 
	 * @param a
	 * @return mean(a)
	 * @since 1.0
	 */
	public static double mean(Matrix a) {
		return a.aggregate(AggregationFunction.SUM) / a.aggregate(AggregationFunction.COUNT);
	}

	/**
	 * Arithmetic mean of all elements (except NaN) in <code>Matrix</code> for
	 * each row or column.
	 * 
	 * @param a
	 * @param dimension
	 *            0 if for each row, 1 if for each column
	 * @return mean(a) for each row or column
	 * @since 1.0
	 */
	public static Vector mean(Matrix a, int dimension) {
		Vector sum = a.aggregate(AggregationFunction.SUM, dimension);
		Vector count = a.aggregate(AggregationFunction.COUNT, dimension);
		return sum.arrayDividedBy(count);
	}

	/**
	 * Sum of all elements (except NaN) in <code>Matrix</code>.
	 * 
	 * @param a
	 * @return sum(a)
	 * @since 1.0
	 */
	public static double sum(Matrix a) {
		return a.aggregate(AggregationFunction.SUM);
	}

	/**
	 * Sum of all elements (except NaN) in <code>Matrix</code> for each row or
	 * column..
	 * 
	 * @param a
	 * @param dimension
	 *            0 if for each row, 1 if for each column
	 * @return sum(a) for each row or column
	 * @since 1.0
	 */
	public static Vector sum(Matrix a, int dimension) {
		return a.aggregate(AggregationFunction.SUM, dimension);
	}

	/**
	 * Calculates 1-norm of <code>Matrix</code>.
	 * 
	 * @param a
	 * @return norm1(a)
	 * @since 1.0
	 */
	public static double norm1(Matrix a) {
		return a.norm1();
	}

	/**
	 * Calculates 2-norm of <code>Matrix</code>.
	 * 
	 * @param a
	 * @return norm2(a)
	 * @since 1.0
	 */
	public static double norm2(Matrix a) {
		return a.norm2();
	}

	/**
	 * Transposes the matrix
	 * 
	 * @param a
	 * @return transposed matrix
	 * @since 1.0
	 */
	public static Matrix transpose(Matrix a) {
		return a.transpose();
	}

	/**
	 * Helper function for creating a continuous <code>Indices</code> instance.
	 * 
	 * @param start
	 *            start of range (inclusive).
	 * @param end
	 *            end of range (exclusive).
	 * @return a <code>Indices</code> instance.
	 * @since 1.0
	 */
	public static Indices range(int start, int end) {
		return new RangeImpl(start, end);
	}

	/**
	 * Helper function for creating a <code>Indices</code> instance.
	 * 
	 * @param indices
	 *            list of indices
	 * @return new <code>Indices</code> instance.
	 */
	public static Indices indices(int... indices) {
		return new IndicesImpl(indices);
	}

	/**
	 * Helper function for creating a <code>Indices</code> instance.
	 * 
	 * @param length
	 *            number of indices
	 * @param init
	 *            function for initilization the indices
	 * @return new <code>Indices</code> instance.
	 */
	public static Indices indices(int length, VectorFunction init) {
		return new IndicesImpl(length, init);
	}

	/**
	 * Creates a new <code>Scalar</code> with the given value.
	 * 
	 * @param value
	 * @return new <code>Scalar</code> instance.
	 * @since 1.0
	 */
	public static Scalar scalar(double value) {
		return new Scalar(value);
	}

	/**
	 * Creates a new <code>SquareMatrix</code> instance filled with the
	 * specified value.
	 * 
	 * @param size
	 *            size of resulting matrix (size >= 0)
	 * @param fill
	 *            default value for all elements
	 * @return new <code>SquareMatrix</code>, or <code>Scalar<code> instance
	 * @throws IllegalArgumentException
	 *             if size < 0
	 * @since 1.0
	 */
	public static SquareMatrix square(int size, double fill) {
		if (size < 0) {
			throw new IllegalArgumentException();
		} else if (size == 0) {
			return Empty.EMPTY;
		} else if (size == 1) {
			return new Scalar(fill);
		}
		return FACTORY.createSquareMatrix(size, fill);
	}

	/**
	 * Creates a new <code>SquareMatrix</code> instance filled with the values
	 * returned by the given function.
	 * 
	 * @param size
	 *            size of resulting matrix (size >= 0)
	 * @param init
	 *            a function f(x,y) := z
	 * @return new <code>SquareMatrix</code>, or <code>Scalar<code> instance
	 * @throws IllegalArgumentException
	 *             if size < 0
	 * @since 1.0
	 */
	public static SquareMatrix square(int size, MatrixFunction init) {
		if (size < 0) {
			throw new IllegalArgumentException();
		} else if (size == 0) {
			return Empty.EMPTY;
		} else if (size == 1) {
			return new Scalar(init.cell(0, 0));
		}
		return FACTORY.createSquareMatrix(size, init);
	}

	/**
	 * Calculates the determinant of a square matrix.
	 * 
	 * @param a
	 * @return det(a)
	 * @since 1.0
	 */
	public static double det(SquareMatrix a) {
		return a.det();
	}

	/**
	 * Calculates the inverse of a square matrix.
	 * 
	 * @param a
	 * @return inverse(a)
	 * @since 1.0
	 */
	public static SquareMatrix inverse(SquareMatrix a) {
		return a.inverse();
	}

	/**
	 * Calculates the rank of a square matrix.
	 * 
	 * @param a
	 * @return rank(a)
	 * @since 1.0
	 */
	public static double rank(SquareMatrix a) {
		return a.rank();
	}

	/**
	 * Calculates the trace value of a square matrix.
	 * 
	 * @param a
	 * @return trace(a)
	 * @since 1.0
	 */
	public static double trace(SquareMatrix a) {
		return a.trace();
	}

	/**
	 * Calculates the power of a square matrix.
	 * 
	 * @param a
	 *            base
	 * @param p
	 *            exponent
	 * @return a^p
	 * @since 1.0
	 */
	public static SquareMatrix pow(SquareMatrix a, int p) {
		return a.pow(p);
	}

	/**
	 * Sorts the rows of matrix.
	 * 
	 * @param a
	 * @param column
	 *            index of the column which values are to be sorted.
	 * @return sorted instance of the same matrix.
	 * @since 1.0
	 */
	public static Matrix sort(Matrix a, int column) {
		return a.sort(column);
	}

	/**
	 * Creates a new n x 1 vector filled with zeros.
	 * 
	 * @param rows
	 *            number of rows (rows >= 0).
	 * @return a <code>Vector</code>, or <code>Scalar</code> instance.
	 * @throws IllegalArgumentException
	 *             if rows < 0.
	 * @since 1.0
	 */
	public static Vector zeros(int rows) {
		if (rows < 0) {
			throw new IllegalArgumentException();
		} else if (rows == 0) {
			return Empty.EMPTY;
		} else if (rows == 1) {
			return Scalar.ZERO;
		} else {
			return FACTORY.createVector(rows, 0);
		}
	}

	/**
	 * Creates a new n x 1 vector filled with ones.
	 * 
	 * @param rows
	 *            number of rows (rows >= 0).
	 * @return a <code>Vector</code>, or <code>Scalar</code> instance.
	 * @throws IllegalArgumentException
	 *             if rows < 0.
	 * @since 1.0
	 */
	public static Vector ones(int rows) {
		if (rows < 0) {
			throw new IllegalArgumentException();
		} else if (rows == 0) {
			return Empty.EMPTY;
		} else if (rows == 1) {
			return Scalar.ONE;
		} else {
			return FACTORY.createVector(rows, 1);
		}
	}

	/**
	 * Creates a new n x 1 vector filled with the specified values.
	 * 
	 * @param values
	 *            a double array containing the values of the vector.
	 * @return a <code>Vector</code>, or <code>Scalar</code> instance.
	 * @since 1.0
	 */
	public static Vector vector(double... values) {
		if (values.length == 0) {
			return Empty.EMPTY;
		} else if (values.length == 1) {
			return new Scalar(values[0]);
		} else {
			return FACTORY.createVector(values);
		}
	}

	/**
	 * Creates a new n x 1 vector filled with values returned by the given
	 * function.
	 * 
	 * @param rows
	 *            number of rows (rows >= 0).
	 * @param init
	 *            a function f(x) := y.
	 * @return a <code>Vector</code>, or <code>Scalar</code> instance.
	 * @throws IllegalArgumentException
	 *             if rows < 0.
	 * @since 1.0
	 */
	public static Vector vector(int rows, VectorFunction init) {
		if (rows < 0) {
			throw new IllegalArgumentException();
		} else if (rows == 0) {
			return Empty.EMPTY;
		} else if (rows == 1) {
			return new Scalar(init.cell(0));
		} else {
			return FACTORY.createVector(rows, init);
		}
	}

	/**
	 * Returns the minimum value of a matrix.
	 * 
	 * @param v
	 * @return min(v)
	 * @since 1.0
	 */
	public static double min(Matrix v) {
		return v.aggregate(AggregationFunction.MINIMUM);
	}

	/**
	 * Returns the minimum value of a matrix for each row or column.
	 * 
	 * @param v
	 * @param dimension
	 *            if dimension == 0 -> row-wise, if dimension == 1 ->
	 *            column-wise
	 * @return min(v) per row/column
	 * @since 1.0
	 */
	public static Vector min(Matrix v, int dimension) {
		return v.aggregate(AggregationFunction.MINIMUM, dimension);
	}

	/**
	 * Returns the maximum value of a matrix.
	 * 
	 * @param v
	 * @return max(v)
	 * @since 1.0
	 */
	public static double max(Matrix v) {
		return v.aggregate(AggregationFunction.MAXIMUM);
	}

	/**
	 * Returns the maximum value of a matrix for each row or column.
	 * 
	 * @param v
	 * @param dimension
	 *            if dimension == 0 -> row-wise, if dimension == 1 ->
	 *            column-wise
	 * @return min(v) per row/column
	 * @since 1.0
	 */
	public static Vector max(Matrix v, int dimension) {
		return v.aggregate(AggregationFunction.MAXIMUM, dimension);
	}
}
