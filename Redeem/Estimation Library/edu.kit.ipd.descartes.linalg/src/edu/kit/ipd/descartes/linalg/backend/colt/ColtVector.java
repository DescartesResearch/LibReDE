package edu.kit.ipd.descartes.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
import edu.kit.ipd.descartes.linalg.Range;
import edu.kit.ipd.descartes.linalg.backend.MatrixImplementation;
import edu.kit.ipd.descartes.linalg.backend.VectorImplementation;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtVector extends DenseDoubleMatrix1D implements VectorImplementation {

	private static final long serialVersionUID = 1464728927753092222L;
	private static final Algebra ALG = new Algebra();

	public ColtVector(int rows) {
		super(rows);
	}

	public ColtVector(double... values) {
		super(values);
	}
	
	public ColtVector(int size, DoubleStorage storage) {
		super(size);
		storage.read(elements);
	}

	public ColtVector(int size, double[] elements, int zero, int stride) {
		super(size, elements, zero , stride);
	}

	@Override
	public ColtVector plus(double d) {
		ColtVector result = new ColtVector(this.rows());
		for (int i = 0; i < size(); i++) {
			result.setQuick(i, getQuick(i) + d);
		}
		return result;
	}

	@Override
	public ColtVector minus(double d) {
		ColtVector result = new ColtVector(this.rows());
		for (int i = 0; i < size(); i++) {
			result.setQuick(i, getQuick(i) - d);
		}
		return result;
	}

	@Override
	public double dot(VectorImplementation b) {
		return zDotProduct(getColtVector(b));
	}

	@Override
	public VectorImplementation times(double d) {
		ColtVector result = new ColtVector(this.rows());
		for (int i = 0; i < size(); i++) {
			result.setQuick(i, getQuick(i) * d);
		}
		return result;
	}

	@Override
	public VectorImplementation abs() {
		ColtVector result = new ColtVector(this.rows());
		for (int i = 0; i < size(); i++) {
			result.setQuick(i, Math.abs(getQuick(i)));
		}
		return result;
	}

	@Override
	public double sum() {
		return zSum();
	}

	@Override
	public double norm1() {
		return ALG.norm1(this);
	}

	@Override
	public double norm2() {
		return ALG.norm2(this);
	}

	@Override
	public double[] toArray1D() {
		return toArray();
	}

	@Override
	public void toDoubleStorage(DoubleStorage storage) {
		storage.write(elements);
	}

	@Override
	public ColtVector plus(MatrixImplementation a) {
		ColtVector res = (ColtVector) copy();
		res.assign(getColtVector(a), Functions.plus);
		return res;
	}

	@Override
	public ColtVector minus(MatrixImplementation a) {
		ColtVector res = (ColtVector) copy();
		res.assign(getColtVector(a), Functions.minus);
		return res;
	}
	
	@Override
	public MatrixImplementation arrayMultipliedBy(MatrixImplementation a) {
		ColtVector res = (ColtVector) copy();
		res.assign(getColtVector(a), Functions.mult);
		return res;
	}

	@Override
	public MatrixImplementation multipliedBy(MatrixImplementation a) {
		ColtVector vector = getColtVector(a);
		ColtMatrix result = (ColtMatrix)ALG.multOuter(this, vector, null);
		return result;			
	}
	
	private ColtVector getColtVector(MatrixImplementation a) {
		if (a instanceof ColtVector) {
			return (ColtVector)a;
		} else {
			return new ColtVector(a.toArray1D());
		}
	}
	
	private ColtMatrix getColtMatrix(MatrixImplementation a) {
		if (a instanceof ColtMatrix) {
			return (ColtMatrix)a;
		} else {
			return new ColtMatrix(a.toArray2D());
		}
	}

	@Override
	public VectorImplementation slice(Range range) {
		return (VectorImplementation) viewPart(range.getStart(), range.getEnd() - range.getStart());
	}
	
	@Override
	public ColtMatrix appendColumns(MatrixImplementation a) {		
		if (a.rows() != this.rows()) {
			throw new IllegalArgumentException("Number of rows must be equal.");
		}
		
		ColtMatrix combined = new ColtMatrix(this.rows(), 1 + a.columns());
		combined.viewPart(0, 0, this.rows(), 1).assign(this.toArray2D());
		combined.viewPart(0, 1, a.rows(), a.columns()).assign(getColtMatrix(a));
		return combined;
	}
	
	@Override
	public ColtVector appendRows(MatrixImplementation a) {
		if (a.columns() != 1) {
			throw new IllegalArgumentException("Number of columns must be equal 1.");
		}
		ColtVector combined = new ColtVector(this.size() + a.rows());
		combined.viewPart(0, this.size()).assign(this);
		combined.viewPart(this.size(), a.rows()).assign(getColtVector(a));		
		return combined;
	}
	
	@Override
	public DoubleMatrix1D like(int size) {
		return new ColtVector(size);
	}

	@Override
	public DoubleMatrix2D like2D(int rows, int columns) {
		return new ColtMatrix(rows, columns);
	}

	@Override
	public MatrixImplementation transpose() {
		ColtMatrix matrix = new ColtMatrix(1, size());
		for (int i = 0; i < size(); i++) {
			matrix.setQuick(0, i, getQuick(i));
		}
		return matrix;
	}

	@Override
	public double get(int row, int col) {
		if (col != 0) {
			throw new IndexOutOfBoundsException();
		}
		return get(row);
	}

	@Override
	public int rows() {
		return size();
	}

	@Override
	public int columns() {
		return 1;
	}

	@Override
	public VectorImplementation row(int row) {
		//TODO
		return null;
	}

	@Override
	public VectorImplementation column(int column) {
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
	public MatrixImplementation copyAndSet(int row, int col, double value) {
		if (col != 0) {
			throw new IndexOutOfBoundsException();
		}		
		ColtVector copy = (ColtVector)this.copy();
		copy.set(row, value);
		return copy;
	}

	@Override
	public VectorImplementation copyAndSet(Range rows, VectorImplementation values) {
		ColtVector copy = (ColtVector)this.copy();
		copy.viewPart(rows.getStart(), rows.getLength()).assign(getColtVector(values));
		return copy;
	}
}
