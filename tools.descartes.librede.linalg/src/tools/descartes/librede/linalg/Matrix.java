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


public interface Matrix {

	/**
	 * Access value at specified position in matrix.
	 * 
	 * @param row
	 *            row index (0 <= row < rows())
	 * @param col
	 *            column index (0 <= col < columns())
	 * @return value at [row, col]
	 * @throws IndexOutOfBoundsException
	 * @since 1.0
	 */
	double get(int row, int col);

	/**
	 * Updates the value at the specified position in the matrix. The original
	 * matrix is kept unchanged. A copy including the changes is returned
	 * instead.
	 * 
	 * @param row
	 *            row index (0 <= row < rows())
	 * @param col
	 *            column index (0 <= col < columns())
	 * @param value
	 *            new value
	 * @return new matrix instance with updated value.
	 * @throws IndexOutOfBoundsException
	 * @since 1.0
	 */
	Matrix set(int row, int col, double value);

	/**
	 * Appends the given matrix as additional columns to this matrix. The
	 * original matrix is kept unchanged.
	 * 
	 * @param a
	 *            the matrix to append (this.rows() == a.rows()).
	 * @return a new <code>Matrix</code> instance with the appended columns.
	 * @throws IllegalArgumentException
	 *             if this.rows() != a.rows().
	 * @since 1.0
	 */
	Matrix appendColumns(Matrix a);

	/**
	 * Appends the given matrix as additional rows to this matrix. The original
	 * matrix is kept unchanged.
	 * 
	 * @param a
	 *            the matrix to append (this.columns() == a.columns()).
	 * @return a new <code>Matrix</code> instance with the appended rows.
	 * @throws IllegalArgumentException
	 *             if this.columns() != a.columns().
	 * @since 1.0
	 */
	Matrix appendRows(Matrix a);

	/**
	 * @return number of rows of this matrix
	 * @since 1.0
	 */
	int rows();

	/**
	 * @return number columns of this matrix
	 * @since 1.0
	 */
	int columns();

	/**
	 * Returns a row vector.
	 * 
	 * @param row
	 *            index of row to return (0 <= row < rows())
	 * @return a <code>Vector</code> of the specified row
	 * @throws IndexOutOfBoundsException
	 * @since 1.0
	 */
	Vector row(int row);
	
	Matrix rows(Indices rows);

	/**
	 * Returns a column vector.
	 * 
	 * @param column
	 *            index of column to return (0 <= col < columns())
	 * @return a <code>Vector</code> of the specified column
	 * @throws IndexOutOfBoundsException
	 * @since 1.0
	 */
	Vector column(int column);
	
	Matrix columns(Indices cols);

	/**
	 * @return true if matrix is a vector (n x 1), false otherwise.
	 * @since 1.0
	 */
	boolean isVector();

	/**
	 * @return true if matrix is a scalar (1 x 1), false otherwise.
	 * @since 1.0
	 */
	boolean isScalar();

	/**
	 * @return true if matrix is empty (0 x 0), false otherwise.
	 */
	boolean isEmpty();

	Matrix plus(Matrix a);

	Matrix plus(double a);

	Matrix minus(Matrix a);

	Matrix minus(double a);

	Matrix multipliedBy(Matrix a);

	Matrix arrayMultipliedBy(Matrix a);
	
	Matrix arrayDividedBy(Matrix a);

	Matrix times(double a);
	
	Matrix mldivide(Matrix b);

	double norm1();

	double norm2();

	Matrix abs();

	Matrix transpose();

	Matrix sort(int column);

	Matrix insertRow(int row, Vector values);
	
	Matrix setRow(int row, Vector values);
	
	Matrix circshift(int rows);
	
	double aggregate(AggregationFunction func);
	
	Vector aggregate(AggregationFunction func, int dimension);

	/*
	 * Conversion functions
	 */

	double[] toArray1D();

	double[][] toArray2D();
}
