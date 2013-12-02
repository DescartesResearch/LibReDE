package edu.kit.ipd.descartes.linalg.backend.colt;

import cern.colt.Sorting;
import cern.colt.function.IntComparator;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
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
	public ColtVector internalPlus(double d) {
		DoubleMatrix1D result = newVector();
		for (int i = 0; i < delegate.size(); i++) {
			result.setQuick(i, delegate.getQuick(i) + d);
		}
		return new ColtVector(result);
	}

	@Override
	public ColtVector internalMinus(double d) {
		DoubleMatrix1D result = newVector();
		for (int i = 0; i < delegate.size(); i++) {
			result.setQuick(i, delegate.getQuick(i) - d);
		}
		return new ColtVector(result);
	}

	@Override
	public double dot(Vector b) {
		return delegate.zDotProduct(getColtVector(b).delegate);
	}

	@Override
	public ColtVector internalTimes(double d) {
		DoubleMatrix1D result = newVector();
		for (int i = 0; i < delegate.size(); i++) {
			result.setQuick(i, delegate.getQuick(i) * d);
		}
		return new ColtVector(result);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector abs() {
		DoubleMatrix1D result = newVector();
		for (int i = 0; i < delegate.size(); i++) {
			result.setQuick(i, Math.abs(delegate.getQuick(i)));
		}
		return new ColtVector(result);
	}

	@Override
	public double sum() {
		return delegate.zSum();
	}

	@Override
	public double mean() {
		return sum() / rows();
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
	public ColtVector internalPlus(Matrix a) {
		DoubleMatrix1D res = copyVector();
		res.assign(getColtVector(a).delegate, Functions.plus);
		return new ColtVector(res);
	}

	@Override
	public ColtVector internalMinus(Matrix a) {
		DoubleMatrix1D res = copyVector();
		res.assign(getColtVector(a).delegate, Functions.minus);
		return new ColtVector(res);
	}
	
	@Override
	public ColtVector internalArrayMultipliedBy(Matrix a) {
		DoubleMatrix1D res = copyVector();
		res.assign(getColtVector(a).delegate, Functions.mult);
		return new ColtVector(res);
	}

	@Override
	public ColtMatrix internalMultipliedBy(Matrix a) {
		ColtVector vector = getColtVector(a);
		// TODO buffer
		DoubleMatrix2D result = ALG.multOuter(delegate, vector.delegate, null);
		return new ColtMatrix(result);			
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
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
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
	
	@SuppressWarnings("unchecked")
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
	public double[][] toArray2D() {
		double[][] temp = new double[rows()][1];
		for (int i = 0; i < rows(); i++) {
			temp[i][0] = this.get(i);
		}
		return temp;
	}

	@Override
	public int[] sort(int column) {
		if (column != 0) {
			throw new IndexOutOfBoundsException();
		}
		
		int[] idx = new int[delegate.size()];
		for (int i = 0; i < idx.length; i++) {
			idx[i] = i;
		}	
		
		Sorting.quickSort(idx, 0, idx.length, new IntComparator() {			
			@Override
			public int compare(int idx1, int idx2) {
				double val1 = delegate.getQuick(idx1);
				double val2 = delegate.getQuick(idx2);
				if (val1 < val2) {
					return -1;
				} else if (val1 > val2) {
					return 1;
				}
				return 0;
			}
		});
		return idx;
	}

	@Override
	public double get(int row) {
		return delegate.get(row);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector set(int row, double value) {
		DoubleMatrix1D copy = copyVector();
		copy.set(row, value);
		return  new ColtVector(copy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector set(int row, int col, double value) {
		if (col != 0) {
			throw new IndexOutOfBoundsException();
		}		
		return set(row, value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector set(Range rows, Vector values) {
		DoubleMatrix1D copy = copyVector();
		copy.viewPart(rows.getStart(), rows.getLength()).assign(getColtVector(values).delegate);
		return new ColtVector(copy);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Vector slice(Range range) {
		DoubleMatrix1D part = delegate.viewPart(range.getStart(), range.getEnd() - range.getStart());
		return new ColtVector(part);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Vector subset(int[] indeces) {
		return new ColtVector(delegate.viewSelection(indeces));
	}

	@Override
	public Vector row(int row) {
		return new Scalar(delegate.get(row));
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
