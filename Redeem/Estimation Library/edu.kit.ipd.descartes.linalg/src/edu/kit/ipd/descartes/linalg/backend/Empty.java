package edu.kit.ipd.descartes.linalg.backend;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Range;
import edu.kit.ipd.descartes.linalg.SquareMatrix;
import edu.kit.ipd.descartes.linalg.Vector;

public final class Empty implements Vector, SquareMatrix {
	
	public static final Empty EMPTY = new Empty();
	
	private Empty() { }

	@Override
	public <V extends Vector> V set(int row, double value) {
		throw new IndexOutOfBoundsException();
	}

	@Override
	public <V extends Vector> V set(Range rows, Vector values) {
		throw new IndexOutOfBoundsException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty slice(Range range) {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty subset(int[] indeces) {
		return this;
	}

	@Override
	public <M extends Matrix> M set(int row, int col, double value) {
		throw new IndexOutOfBoundsException();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <M extends Matrix> M appendColumns(Matrix a) {
		return (M)a;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <M extends Matrix> M appendRows(Matrix a) {
		return (M)a;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty plus(Matrix a) {
		if (!a.isEmpty() && !a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty plus(double a) {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty minus(Matrix a) {
		if (!a.isEmpty() && !a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty minus(double a) {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty multipliedBy(Matrix a) {
		if (!a.isEmpty() && !a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty arrayMultipliedBy(Matrix a) {
		if (!a.isEmpty() && !a.isScalar()) {
			throw new IllegalArgumentException();
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty times(double a) {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty abs() {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Empty transpose() {
		return this;
	}

	@Override
	public double get(int row, int col) {
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
		return this;
	}

	@Override
	public Vector column(int column) {
		return this;
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
	public double sum() {
		return Double.NaN;
	}

	@Override
	public int[] sort(int column) {
		return new int[0];
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
		return Double.NaN;
	}

	@Override
	public double dot(Vector b) {
		if (!b.isEmpty()) {
			throw new IllegalArgumentException();
		}
		return 0;
	}

	@Override
	public double mean() {
		return Double.NaN;
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

}
