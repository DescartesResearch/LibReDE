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
package edu.kit.ipd.descartes.linalg.backend.colt;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;

import java.util.Arrays;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
import edu.kit.ipd.descartes.linalg.AggregationFunction;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.backend.AbstractMatrix;

public class ColtMatrix extends AbstractMatrix {
	
	protected DoubleMatrix2D delegate;

	protected static final Algebra ALG = new Algebra();
	
	protected ColtMatrix(DoubleMatrix2D delegate) {
		this.delegate = delegate;
	}

	public ColtMatrix(int rows, int columns) {
		this(new DenseDoubleMatrix2D(rows, columns));
	}

	public ColtMatrix(double[][] values) {
		this(new DenseDoubleMatrix2D(values));
	}
	
	public ColtMatrix(int rows, int columns, double fill) {
		this(new DenseDoubleMatrix2D(rows,columns));
		delegate.assign(fill);
	}
	
	public ColtMatrix(int rows, int columns, MatrixFunction init) {
		this(new DenseDoubleMatrix2D(rows,columns));
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				delegate.setQuick(i, j, init.cell(i, j));
			}
		}
	}
	
	@Override
	public int rows() {
		return delegate.rows();
	}
	
	@Override
	public int columns() {
		return delegate.columns();
	}
	
	@Override
	public double get(int row, int col) {
		return delegate.get(row, col);
	}

	@Override
	public ColtVector row(int row) {
		return new ColtVector(delegate.viewRow(row));
	}
	
	@Override
	public Matrix rows(int start, int end) {
		if (start == end) {
			return row(start);
		}
		return new ColtMatrix(delegate.viewPart(start, 0, (end - start + 1), delegate.columns()));
	}

	@Override
	public Vector column(int column) {
		if (delegate.rows() == 1) {
			return new Scalar(delegate.get(0, column));
		}
		return new ColtVector(delegate.viewColumn(column));
	}
	
	@Override
	public Matrix columns(int start, int end) {
		if (start == end) {
			return column(start);
		}
		return new ColtMatrix(delegate.viewPart(0, start, delegate.rows(), (end - start + 1)));
	}

	@Override
	public double[][] toArray2D() {
		return delegate.toArray();
	}

	@Override
	public ColtMatrix plus(double a) {
		DoubleMatrix2D result = newMatrix();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				result.setQuick(i, j, delegate.getQuick(i, j) + a);
			}
		}
		return new ColtMatrix(result);
	}

	@Override
	public ColtMatrix minus(double a) {
		DoubleMatrix2D result = newMatrix();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				result.setQuick(i, j, delegate.getQuick(i, j) - a);
			}
		}
		return new ColtMatrix(result);
	}

	@Override
	public ColtMatrix times(double a) {
		DoubleMatrix2D result = newMatrix();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				result.setQuick(i, j, delegate.getQuick(i, j) * a);
			}
		}
		return new ColtMatrix(result);
	}

	@Override
	public ColtMatrix abs() {
		DoubleMatrix2D result = newMatrix();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				result.setQuick(i, j, Math.abs(delegate.getQuick(i, j)));
			}
		}
		return new ColtMatrix(result);
	}

	@Override
	public double norm1() {
		return ALG.norm1(delegate);
	}

	@Override
	public double norm2() {
		return ALG.norm2(delegate);
	}

	@Override
	public Matrix transpose() {
		if (rows() == 1) {
			return new ColtVector(delegate.viewRow(0));
		} else {
			return new ColtMatrix(ALG.transpose(delegate));
		}
	}

	@Override
	public Matrix plus(Matrix a) {
		checkOperandsSameSize(a);
		
		DoubleMatrix2D res = copyMatrix();
		res.assign(toColtMatrix(a).delegate, Functions.plus);
		return new ColtMatrix(res);
	}
	
	@Override
	public Matrix minus(Matrix a) {
		checkOperandsSameSize(a);
		
		DoubleMatrix2D res = copyMatrix();
		res.assign(toColtMatrix(a).delegate, Functions.minus);
		return new ColtMatrix(res);
	}	

	@Override
	public Matrix arrayMultipliedBy(Matrix a) {
		checkOperandsSameSize(a);
		
		DoubleMatrix2D res = copyMatrix();
		res.assign(toColtMatrix(a).delegate, Functions.mult);
		return new ColtMatrix(res);
	}
	
	@Override
	public Matrix arrayDividedBy(Matrix a) {
		checkOperandsSameSize(a);
		
		DoubleMatrix2D res = copyMatrix();
		res.assign(toColtMatrix(a).delegate, Functions.div);
		return new ColtMatrix(res);
	}

	@Override
	public Matrix multipliedBy(Matrix a) {
		checkOperandsInnerDimensions(a);
		
		if (a.isScalar()) {
			return times(((Scalar)a).getValue());
		} else if (a.isVector()) {
			DoubleMatrix1D result = newVector(this.rows());
			delegate.zMult(toColtVector((Vector)a).delegate, result);
			return new ColtVector(result);
		} else {
			DoubleMatrix2D result = newMatrix(delegate.rows(), a.columns());
			delegate.zMult(toColtMatrix(a).delegate, result);
			return new ColtMatrix(result);
		}
	}

	@Override
	public double[] toArray1D() {
		int rows = delegate.rows();
		int columns = delegate.columns();
		double[] ret = new double[rows * columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				ret[i * columns + j] = delegate.getQuick(i, j);
			}
		}
		return ret;
	}

	protected ColtMatrix toColtMatrix(Matrix a) {
		if (a instanceof ColtMatrix) {
			return (ColtMatrix) a;
		} else {
			return new ColtMatrix(a.toArray2D());
		}
	}

	protected ColtVector toColtVector(Vector a) {
		if (a instanceof ColtVector) {
			return (ColtVector) a;
		} else {
			return new ColtVector(a.toArray1D());
		}
	}

	@Override
	public Matrix appendRows(Matrix a) {
		if (a.columns() != this.columns()) {
			throw new IllegalArgumentException(
					"Number of columns must be equal.");
		}

		DoubleMatrix2D combined = newMatrix(delegate.rows() + a.rows(),
				delegate.columns());
		combined.viewPart(0, 0, this.rows(), this.columns()).assign(delegate);
		combined.viewPart(this.rows(), 0, a.rows(), a.columns()).assign(
				toColtMatrix(a).delegate);
		return new ColtMatrix(combined);
	}

	@Override
	public Matrix appendColumns(Matrix a) {
		if (a.rows() != this.rows()) {
			throw new IllegalArgumentException("Number of rows must be equal.");
		}

		DoubleMatrix2D combined = newMatrix(delegate.rows(), delegate.columns()
				+ a.columns());
		combined.viewPart(0, 0, this.rows(), this.columns()).assign(delegate);
		combined.viewPart(0, this.columns(), a.rows(), a.columns()).assign(
				toColtMatrix(a).delegate);
		return new ColtMatrix(combined);
	}
	
	@Override
	public Matrix set(int row, int col, double value) {
		DoubleMatrix2D copy = copyMatrix();
		copy.set(row, col, value);
		return new ColtMatrix(copy);
	}
	
	@Override
	public ColtMatrix sort(int column) {
		return new ColtMatrix(delegate.viewSorted(column));
	}
	
	@Override
	public Matrix subset(int... rows) {
		return new ColtMatrix(delegate.viewSelection(rows, null));
	}
	
	@Override
	public Matrix insertRow(int row, Vector values) {
		int n = delegate.rows();
		int m = delegate.columns();
		if (values.rows() != m) {
			throw new IllegalArgumentException();
		}
		if (row < 0 || row > n) {
			throw new IndexOutOfBoundsException();
		}

		DoubleMatrix2D temp = newMatrix(n + 1, m);
		if (row > 0) {
			temp.viewPart(0, 0, row, m).assign(delegate.viewPart(0, 0, row, m));
		}
		temp.viewPart(row, 0, 1, m).assign(new double[][] { values.toArray1D() });
		if (row < n) {
			temp.viewPart(row + 1, 0, n - row, m).assign(delegate.viewPart(row, 0, n - row, m));
		}
		return new ColtMatrix(temp);
	}
	
	@Override
	public Matrix setRow(int row, Vector values) {
		int n = delegate.rows();
		int m = delegate.columns();
		if (values.rows() != m) {
			throw new IllegalArgumentException();
		}
		if (row < 0 || row > n) {
			throw new IndexOutOfBoundsException();
		}
		DoubleMatrix2D copy = copyMatrix();
		copy.viewPart(row, 0, 1, m).assign(new double[][] { values.toArray1D() });
		return new ColtMatrix(copy);
	}
	
	@Override
	public Matrix circshift(int rows) {
		int n = delegate.rows();
		int offset = rows % n; // in case rows would result in several rounds of shifting (rows >= delegate.rows())
		if (offset != 0) {
			int[] shifted = new int[n];
			for (int i = 0; i < n; i++) {
				int shift = (i + offset) % n;
				if (shift < 0) {
					shifted[n + shift] = i;
				} else {
					shifted[shift] = i;
				}
			}
			return new ColtMatrix(delegate.viewSelection(shifted, null));			
		}
		return this;
	}
	
	@Override
	public double aggregate(AggregationFunction func) {
		int n = delegate.rows();
		int m = delegate.columns();
		double aggr = Double.NaN;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				aggr = func.apply(aggr, delegate.getQuick(i, j));
			}
		}
		return aggr;
	}
	
	@Override
	public Vector aggregate(AggregationFunction func, int dimension) {
		if (dimension < 0 || dimension > 1) {
			throw new IllegalArgumentException();
		}
		int n = delegate.rows();
		int m = delegate.columns();
		double[] aggr = new double[(dimension == 0) ? m : n];
		Arrays.fill(aggr, Double.NaN);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (dimension == 1) {
					aggr[i] = func.apply(aggr[i], delegate.getQuick(i, j));
				} else {
					aggr[j] = func.apply(aggr[j], delegate.getQuick(i, j));
				}
			}
		}
		return vector(aggr);
	}
	
	protected DoubleMatrix1D newVector(int size) {
		return delegate.like1D(size);
	}
	
	protected DoubleMatrix2D newMatrix() {
		return delegate.like();
	}
	
	protected DoubleMatrix2D newMatrix(int rows, int columns) {
		return delegate.like(rows, columns);
	}
	
	protected DoubleMatrix2D copyMatrix() {
		return delegate.copy();
	}
}
