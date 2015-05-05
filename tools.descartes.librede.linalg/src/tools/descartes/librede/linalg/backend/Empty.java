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
package tools.descartes.librede.linalg.backend;

import tools.descartes.librede.linalg.AggregationFunction;
import tools.descartes.librede.linalg.Indices;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.SquareMatrix;
import tools.descartes.librede.linalg.Vector;

public final class Empty implements Vector, SquareMatrix {
	
	public static final Empty EMPTY = new Empty();
	
	private Empty() { }

	@Override
	public Vector set(int row, double value) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Empty set(Indices rows, Vector values) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Matrix set(int row, int col, double value) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Matrix appendColumns(Matrix a) {
		return a;
	}

	@Override
	public Matrix appendRows(Matrix a) {
		return a;
	}

	@Override
	public Empty plus(Matrix a) {
		if (!a.isEmpty() && !a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return this;
	}

	@Override
	public Empty plus(double a) {
		return this;
	}

	@Override
	public Empty minus(Matrix a) {
		if (!a.isEmpty() && !a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return this;
	}

	@Override
	public Empty minus(double a) {
		return this;
	}

	@Override
	public Empty multipliedBy(Matrix a) {
		if (!a.isEmpty() && !a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return this;
	}

	@Override
	public Empty arrayMultipliedBy(Matrix a) {
		if (!a.isEmpty() && !a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return this;
	}
	
	@Override
	public Empty arrayDividedBy(Matrix a) {
		if (!a.isEmpty() && !a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return this;
	}

	@Override
	public Empty times(double a) {
		return this;
	}
	
	@Override
	public Matrix mldivide(Matrix b) {
		if (b.isEmpty()) {
			return this;
		}
		throw new IllegalArgumentException("Incompatible dimensions.");
	}

	@Override
	public Empty abs() {
		return this;
	}

	@Override
	public Empty transpose() {
		return this;
	}

	@Override
	public double get(int row, int col) {
		throw new IndexOutOfBoundsException();
	}
	
	@Override
	public Vector get(Indices rows) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int rows() {
		return 0;
	}

	@Override
	public int columns() {
		return 0;
	}

	@Override
	public Vector row(int row) {
		throw new IndexOutOfBoundsException();
	}
	
	@Override
	public Vector rows(Indices rows) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public Vector column(int column) {
		throw new IndexOutOfBoundsException();
	}
	
	@Override
	public Matrix columns(Indices columns) {
		throw new IndexOutOfBoundsException();
	}
	
	@Override
	public boolean isVector() {
		return true;
	}

	@Override
	public boolean isScalar() {
		return true;
	}
	
	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public double norm1() {
		return Double.NaN;
	}

	@Override
	public double norm2() {
		return Double.NaN;
	}

	@Override
	public Empty sort(int column) {
		return this;
	}

	@Override
	public double[] toArray1D() {
		return new double[0];
	}

	@Override
	public double[][] toArray2D() {
		return new double[0][];
	}

	@Override
	public double get(int row) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public double dot(Vector b) {
		if (!b.isEmpty()) {
			throw new IllegalArgumentException();
		}
		return 0;
	}

	@Override
	public double det() {
		return 1;
	}

	@Override
	public SquareMatrix inverse() {
		return Empty.EMPTY;
	}

	@Override
	public SquareMatrix pow(int a) {
		return Empty.EMPTY;
	}

	@Override
	public double rank() {
		return 0;
	}

	@Override
	public double trace() {
		return 0;
	}
	
	@Override
	public Matrix insertRow(int row, Vector vector) {
		if (row != 0) {
			throw new IndexOutOfBoundsException();
		}
		return vector.transpose();
	}
	
	@Override
	public Matrix setRow(int row, Vector values) {
		return insertRow(row, values);
	}
	
	@Override
	public Vector circshift(int rows) {
		return this;
	}
	
	@Override
	public double aggregate(AggregationFunction func) {
		return Double.NaN;
	}
	
	@Override
	public Vector aggregate(AggregationFunction func, int dimension) {
		return this;
	}

}
