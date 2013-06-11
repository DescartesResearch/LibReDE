package edu.kit.ipd.descartes.linalg.backend.colt;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
import edu.kit.ipd.descartes.linalg.backend.MatrixImplementation;
import edu.kit.ipd.descartes.linalg.backend.VectorImplementation;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtMatrix extends DenseDoubleMatrix2D implements
		MatrixImplementation {

	private static final long serialVersionUID = -7263599554445918458L;

	protected static final Algebra ALG = new Algebra();

	public ColtMatrix(int rows, int columns) {
		super(rows, columns);
	}

	public ColtMatrix(double[][] values) {
		super(values);
	}

	public ColtMatrix(int rows, int columns, DoubleStorage storage) {
		super(rows, columns);
		storage.read(elements);
	}

	@Override
	public VectorImplementation row(int row) {
		return (VectorImplementation) viewRow(row);
	}

	@Override
	public VectorImplementation column(int column) {
		return (VectorImplementation) viewColumn(column);
	}

	@Override
	public double[][] toArray2D() {
		return toArray();
	}

	@Override
	public void toDoubleStorage(DoubleStorage storage) {
		storage.write(elements);
	}

	@Override
	public MatrixImplementation plus(double a) {
		ColtMatrix result = like();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				result.setQuick(i, j, getQuick(i, j) + a);
			}
		}
		return result;
	}

	@Override
	public MatrixImplementation minus(double a) {
		ColtMatrix result = like();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				result.setQuick(i, j, getQuick(i, j) - a);
			}
		}
		return result;
	}

	@Override
	public MatrixImplementation times(double a) {
		ColtMatrix result = like();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				result.setQuick(i, j, getQuick(i, j) * a);
			}
		}
		return result;
	}

	@Override
	public MatrixImplementation abs() {
		ColtMatrix result = like();
		for (int i = 0; i < rows(); i++) {
			for (int j = 0; j < columns(); j++) {
				result.setQuick(i, j, Math.abs(getQuick(i, j)));
			}
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
	public MatrixImplementation transpose() {
		if (rows() == 1) {
			return (MatrixImplementation) viewRow(0);
		} else {
			return (MatrixImplementation) ALG.transpose(this);
		}
	}

	@Override
	public MatrixImplementation plus(MatrixImplementation a) {
		ColtMatrix res = (ColtMatrix) this.copy();
		res.assign(toColtMatrix(a), Functions.plus);
		return res;
	}

	@Override
	public MatrixImplementation minus(MatrixImplementation a) {
		ColtMatrix res = (ColtMatrix) this.copy();
		res.assign(toColtMatrix(a), Functions.minus);
		return res;
	}

	@Override
	public MatrixImplementation arrayMultipliedBy(MatrixImplementation a) {
		ColtMatrix res = (ColtMatrix) this.copy();
		res.assign(toColtMatrix(a), Functions.mult);
		return res;
	}

	@Override
	public MatrixImplementation multipliedBy(MatrixImplementation a) {
		if (a instanceof VectorImplementation) {
			ColtVector result = like1D(this.rows());
			zMult(toColtVector(a), result);
			return result;
		} else {
			ColtMatrix result = like(rows(), a.columns());
			zMult(toColtMatrix(a), result);
			return result;
		}
	}

	@Override
	public double[] toArray1D() {
		return elements.clone();
	}

	protected ColtMatrix toColtMatrix(MatrixImplementation a) {
		if (a instanceof ColtMatrix) {
			return (ColtMatrix) a;
		} else {
			return new ColtMatrix(a.toArray2D());
		}
	}

	protected ColtVector toColtVector(MatrixImplementation a) {
		if (a instanceof ColtVector) {
			return (ColtVector) a;
		} else {
			return new ColtVector(a.toArray1D());
		}
	}

	@Override
	public MatrixImplementation appendRows(MatrixImplementation a) {
		if (a.columns() != this.columns()) {
			throw new IllegalArgumentException(
					"Number of columns must be equal.");
		}

		ColtMatrix combined = like(this.rows() + a.rows(),
				this.columns());
		combined.viewPart(0, 0, this.rows(), this.columns()).assign(this);
		combined.viewPart(this.rows(), 0, a.rows(), a.columns()).assign(
				toColtMatrix(a));
		return combined;
	}

	@Override
	public MatrixImplementation appendColumns(MatrixImplementation a) {
		if (a.rows() != this.rows()) {
			throw new IllegalArgumentException("Number of rows must be equal.");
		}

		ColtMatrix combined = like(this.rows(), this.columns()
				+ a.columns());
		combined.viewPart(0, 0, this.rows(), this.columns()).assign(this);
		combined.viewPart(0, this.columns(), a.rows(), a.columns()).assign(
				toColtMatrix(a));
		return combined;
	}

	@Override
	public ColtVector like1D(int arg0) {
		return new ColtVector(arg0);
	}

	@Override
	protected ColtVector like1D(int size, int zero, int stride) {
		return new ColtVector(size, elements, zero, stride);
	}

	@Override
	public ColtMatrix like() {
		return new ColtMatrix(this.rows(), this.columns());
	}

	@Override
	public ColtMatrix like(int rows, int columns) {
		return new ColtMatrix(rows, columns);
	}

	@Override
	public MatrixImplementation copyAndSet(int row, int col, double value) {
		ColtMatrix copy = (ColtMatrix)this.copy();
		copy.set(row, col, value);
		return copy;
	}

}
