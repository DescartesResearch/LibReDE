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

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
import edu.kit.ipd.descartes.linalg.AggregationFunction;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Range;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.linalg.backend.AbstractVector;

public class ColtVector extends AbstractVector {

	private static final Algebra ALG = new Algebra();
	
	protected DoubleMatrix1D delegate;
	
	protected ColtVector(DoubleMatrix1D delegate) {
		this.delegate = delegate;
	}

	public ColtVector(int rows) {
		this(new DenseDoubleMatrix1D(rows));
	}

	public ColtVector(double... values) {
		this(new DenseDoubleMatrix1D(values));
	}
	
	public ColtVector(int rows, double fill) {
		this(new DenseDoubleMatrix1D(rows));
		delegate.assign(fill);
	}
	
	public ColtVector(int rows, VectorFunction init) {
		this(new DenseDoubleMatrix1D(rows));
		for (int i = 0; i < rows; i++) {
			delegate.setQuick(i, init.cell(i));
		}
	}
	
	@Override
	public ColtVector plus(double a) {
		DoubleMatrix1D result = newVector();
		for (int i = 0; i < delegate.size(); i++) {
			result.setQuick(i, delegate.getQuick(i) + a);
		}
		return new ColtVector(result);
	}
	
	@Override
	public ColtVector minus(double a) {
		DoubleMatrix1D result = newVector();
		for (int i = 0; i < delegate.size(); i++) {
			result.setQuick(i, delegate.getQuick(i) - a);
		}
		return new ColtVector(result);
	}

	@Override
	public double dot(Vector b) {
		if (b.rows() != delegate.size()) {
			throw new IllegalArgumentException("A and B must have the same size.");
		}
		return delegate.zDotProduct(getColtVector(b).delegate);
	}

	@Override
	public ColtVector times(double a) {
		DoubleMatrix1D result = newVector();
		for (int i = 0; i < delegate.size(); i++) {
			result.setQuick(i, delegate.getQuick(i) * a);
		}
		return new ColtVector(result);
	}

	@Override
	public ColtVector abs() {
		DoubleMatrix1D result = newVector();
		for (int i = 0; i < delegate.size(); i++) {
			result.setQuick(i, Math.abs(delegate.getQuick(i)));
		}
		return new ColtVector(result);
	}

	@Override
	public double aggregate(AggregationFunction func) {
		int n = delegate.size();
		double aggr = Double.NaN;
		for (int i = 0; i < n; i++) {
			aggr = func.apply(aggr, delegate.getQuick(i));
		}
		return aggr;
	}
	
	@Override
	public Vector aggregate(AggregationFunction func, int dimension) {
		if (dimension > 0) {
			throw new IllegalArgumentException();
		}
		return new Scalar(aggregate(func));
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
	public double[] toArray1D() {
		return delegate.toArray();
	}

	@Override
	public ColtVector plus(Matrix a) {
		checkOperandsSameSize(a);	
		
		DoubleMatrix1D res = copyVector();
		res.assign(getColtVector(a).delegate, Functions.plus);
		return new ColtVector(res);
	}
	
	@Override
	public ColtVector minus(Matrix a) {
		checkOperandsSameSize(a);
		
		DoubleMatrix1D res = copyVector();
		res.assign(getColtVector(a).delegate, Functions.minus);
		return new ColtVector(res);
	}

	@Override
	public ColtVector arrayMultipliedBy(Matrix a) {
		checkOperandsSameSize(a);
		
		DoubleMatrix1D res = copyVector();
		res.assign(getColtVector(a).delegate, Functions.mult);
		return new ColtVector(res);
	}
	
	@Override
	public ColtVector arrayDividedBy(Matrix a) {
		checkOperandsSameSize(a);
		
		DoubleMatrix1D res = copyVector();
		res.assign(getColtVector(a).delegate, Functions.div);
		return new ColtVector(res);
	}

	@Override
	public Matrix multipliedBy(Matrix a) {
		checkOperandsInnerDimensions(a);
		
		if (a.isScalar()) {
			return times(((Scalar)a).getValue());
		} else {
			ColtVector vector = getColtVector(a);
			// TODO buffer
			DoubleMatrix2D result = ALG.multOuter(delegate, vector.delegate, null);
			return new ColtMatrix(result);
		}
	}
	
	private ColtVector getColtVector(Matrix a) {
		if (a instanceof ColtVector) {
			return (ColtVector)a;
		} else {
			return new ColtVector(a.toArray1D());
		}
	}
	
	private ColtMatrix getColtMatrix(Matrix a) {
		if (a instanceof ColtMatrix) {
			return (ColtMatrix)a;
		} else {
			return new ColtMatrix(a.toArray2D());
		}
	}
	
	@Override
	public Matrix appendColumns(Matrix a) {		
		if (a.rows() != this.rows()) {
			throw new IllegalArgumentException("Number of rows must be equal.");
		}
		
		DoubleMatrix2D combined = newMatrix(this.rows(), 1 + a.columns());
		combined.viewPart(0, 0, this.rows(), 1).assign(toArray2D());
		combined.viewPart(0, 1, a.rows(), a.columns()).assign(getColtMatrix(a).delegate);
		return new ColtMatrix(combined);
	}
	
	@Override
	public Vector appendRows(Matrix a) {
		if (a.columns() != 1) {
			throw new IllegalArgumentException("Number of columns must be equal 1.");
		}
		DoubleMatrix1D combined = newVector(delegate.size() + a.rows());
		combined.viewPart(0, delegate.size()).assign(delegate);
		combined.viewPart(delegate.size(), a.rows()).assign(getColtVector(a).delegate);		
		return new ColtVector(combined);
	}
	
	@Override
	public Matrix transpose() {
		DoubleMatrix2D matrix = newMatrix(1, delegate.size());
		for (int i = 0; i < delegate.size(); i++) {
			matrix.setQuick(0, i, delegate.getQuick(i));
		}
		return new ColtMatrix(matrix);
	}

	@Override
	public double get(int row, int col) {
		if (col != 0) {
			throw new IndexOutOfBoundsException();
		}
		return delegate.get(row);
	}

	@Override
	public int rows() {
		return delegate.size();
	}

	@Override
	public int columns() {
		return 1;
	}

	@Override
	public ColtVector column(int column) {
		if (column > 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}
	
	@Override
	public ColtVector columns(int start, int end) {
		if (start != 0 || end != 0) {
			throw new IndexOutOfBoundsException();
		}
		return this;
	}
	
	@Override
	public Vector rows(int start, int end) {
		if (start == end) {
			return row(start);
		}
		return new ColtVector(delegate.viewPart(start, (end - start + 1)));
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
	public ColtVector sort(int column) {
		if (column != 0) {
			throw new IndexOutOfBoundsException();
		}
		
		return new ColtVector(delegate.viewSorted());
	}

	@Override
	public double get(int row) {
		return delegate.get(row);
	}

	@Override
	public Vector set(int row, double value) {
		DoubleMatrix1D copy = copyVector();
		copy.set(row, value);
		return  new ColtVector(copy);
	}

	@Override
	public Vector set(int row, int col, double value) {
		if (col != 0) {
			throw new IndexOutOfBoundsException();
		}		
		return set(row, value);
	}

	@Override
	public Vector set(Range rows, Vector values) {
		DoubleMatrix1D copy = copyVector();
		copy.viewPart(rows.getStart(), rows.getLength()).assign(getColtVector(values).delegate);
		return new ColtVector(copy);
	}

	@Override
	public Vector slice(Range range) {
		if (range.getLength() == 1) {
			return new Scalar(delegate.get(range.getStart()));
		}
		DoubleMatrix1D part = delegate.viewPart(range.getStart(), range.getEnd() - range.getStart());
		return new ColtVector(part);
	}
	
	@Override
	public Vector subset(int...indeces) {
		if (indeces.length == 1) {
			return new Scalar(delegate.get(indeces[0]));
		} else {
			return new ColtVector(delegate.viewSelection(indeces));
		}
	}
	
	@Override
	public Vector row(int row) {
		return new Scalar(delegate.get(row));
	}
	
	@Override
	public Vector insertRow(int row, Vector values) {
		if (values.rows() != 1) {
			throw new IllegalArgumentException();
		}
		if (row < 0 || row > delegate.size()) {
			throw new IndexOutOfBoundsException();
		}
		int n = delegate.size();
		DoubleMatrix1D temp = newVector(n + 1);
		if (row > 0) {
			temp.viewPart(0, row).assign(delegate.viewPart(0, row));
		}
		temp.viewPart(row, 1).assign(values.get(0));
		if (row < n) {
			temp.viewPart(row + 1, n - row).assign(delegate.viewPart(row, n - row));
		}
		return new ColtVector(temp);
	}
	
	@Override
	public Vector setRow(int row, Vector values) {
		if (values.rows() != 1) {
			throw new IllegalArgumentException();
		}
		return set(row, values.get(0));
	}
	
	@Override
	public Vector circshift(int rows) {
		int n = delegate.size();
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
			return new ColtVector(delegate.viewSelection(shifted));			
		}
		return this;
	}
	
	private DoubleMatrix1D newVector() {
		return delegate.like();
	}
	
	private DoubleMatrix1D newVector(int size) {
		return delegate.like(size);
	}
	
	private DoubleMatrix2D  newMatrix(int rows, int columns) {
		return delegate.like2D(rows, columns);
	}
	
	private DoubleMatrix1D copyVector() {
		return delegate.copy();
	}
}