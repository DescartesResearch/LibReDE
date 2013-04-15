package edu.kit.ipd.descartes.linalg.impl.colt;

import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixInitializer;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.impl.colt.ColtVector.FlatArrayVector;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtMatrix extends Matrix {

	private static final Algebra ALG = new Algebra();

	protected static class FlatArrayMatrix extends DenseDoubleMatrix2D {

		public FlatArrayMatrix(int rows, int columns) {
			super(rows, columns);
		}

		public FlatArrayMatrix(double[][] values) {
			super(values);
		}

		public void readFrom(DoubleStorage storage) {
			storage.read(elements);
		}

		public void writeTo(DoubleStorage storage) {
			storage.write(elements);
		}

		@Override
		public DoubleMatrix1D like1D(int arg0) {
			return new ColtVector.FlatArrayVector(arg0);
		}

		@Override
		protected DoubleMatrix1D like1D(int size, int zero, int stride) {
			return new FlatArrayVector(size, elements, zero, stride);
		}

		@Override
		public DoubleMatrix2D like() {
			return new FlatArrayMatrix(this.rows(), this.columns());
		}

		@Override
		public DoubleMatrix2D like(int rows, int columns) {
			return new FlatArrayMatrix(rows, columns);
		}
		
		public double[] getElements() {
			return elements;
		}
	}

	protected FlatArrayMatrix content;

	protected ColtMatrix(FlatArrayMatrix content) {
		this.content = content;
	}

	public ColtMatrix(int rows, int columns) {
		content = new FlatArrayMatrix(rows, columns);
	}

	public ColtMatrix(double[][] values) {
		content = new FlatArrayMatrix(values);
	}

	public ColtMatrix(int rows, int columns, double fill) {
		content = new FlatArrayMatrix(rows, columns);
		content.assign(fill);
	}

	public ColtMatrix(int rows, int columns, DoubleStorage storage) {
		content = new FlatArrayMatrix(rows, columns);
		content.readFrom(storage);
	}

	public ColtMatrix(int rows, int columns, MatrixInitializer init) {
		content = new FlatArrayMatrix(rows, columns);
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				content.setQuick(i, j, init.cell(i, j));
			}
		}
	}

	@Override
	public double get(int row, int col) {
		return content.get(row, col);
	}

	@Override
	public Vector row(int row) {
		return new ColtVector((FlatArrayVector) content.viewRow(row));
	}

	@Override
	public Vector column(int column) {
		return new ColtVector((FlatArrayVector) content.viewColumn(column));
	}

	@Override
	public int rows() {
		return content.rows();
	}

	@Override
	public int columns() {
		return content.columns();
	}

	@Override
	public double[][] toArray2D() {
		return content.toArray();
	}

	@Override
	public void toDoubleStorage(DoubleStorage storage) {
		content.writeTo(storage);
	}

	@Override
	public Matrix plus(double a) {
		ColtMatrix result = new ColtMatrix(this.rows(), this.columns());
		for (int i = 0; i < content.rows(); i++) {
			for (int j = 0; j < content.columns(); j++) {
				result.content.setQuick(i, j, content.getQuick(i, j) + a);
			}
		}
		return result;
	}

	@Override
	public Matrix minus(double a) {
		ColtMatrix result = new ColtMatrix(this.rows(), this.columns());
		for (int i = 0; i < content.rows(); i++) {
			for (int j = 0; j < content.columns(); j++) {
				result.content.setQuick(i, j, content.getQuick(i, j) - a);
			}
		}
		return result;
	}

	@Override
	public Matrix times(double a) {
		ColtMatrix result = new ColtMatrix(this.rows(), this.columns());
		for (int i = 0; i < content.rows(); i++) {
			for (int j = 0; j < content.columns(); j++) {
				result.content.setQuick(i, j, content.getQuick(i, j) * a);
			}
		}
		return result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < content.rows(); i++) {
			builder.append("[");
			for (int j = 0; j < content.columns(); j++) {
				if (j > 0) {
					builder.append("; ");
				}
				builder.append(content.get(i, j));
			}
			builder.append("]");
		}
		builder.append("]");
		return builder.toString();
	}

	@Override
	protected Matrix abs() {
		ColtMatrix result = new ColtMatrix(this.rows(), this.columns());
		for (int i = 0; i < content.rows(); i++) {
			for (int j = 0; j < content.columns(); j++) {
				result.content.setQuick(i, j, Math.abs(content.getQuick(i, j)));
			}
		}
		return result;
	}

	@Override
	protected double sum() {
		return content.zSum();
	}

	@Override
	protected double norm1() {
		return ALG.norm1(content);
	}

	@Override
	protected double norm2() {
		return ALG.norm2(content);
	}

	@Override
	protected Matrix transpose() {
		if (content.rows() == 1) {
			return this.row(0);
		} else {
			return new ColtMatrix((FlatArrayMatrix) ALG.transpose(content));
		}
	}

	@Override
	protected Matrix internalPlus(Matrix a) {
		FlatArrayMatrix res = (FlatArrayMatrix) content.copy();
		res.assign(getMatrixContent(a), Functions.plus);
		return new ColtMatrix(res);
	}

	@Override
	protected Matrix internalMinus(Matrix a) {
		FlatArrayMatrix res = (FlatArrayMatrix) content.copy();
		res.assign(getMatrixContent(a), Functions.minus);
		return new ColtMatrix(res);
	}

	@Override
	protected Matrix internalMatrixMultiply(Matrix a) {
		if (a.isVector()) {
			FlatArrayVector result = new FlatArrayVector(this.rows());
			this.content.zMult(getVectorContent(a), result);
			return new ColtVector(result);
		} else {
			FlatArrayMatrix result = new FlatArrayMatrix(this.rows(), a.columns());
			this.content.zMult(getMatrixContent(a), result);
			return new ColtMatrix(result);
		}		
	}

	@Override
	public double[] toArray1D() {
		return content.getElements();
	}
	
	private FlatArrayMatrix getMatrixContent(Matrix a) {
		if (a instanceof ColtMatrix) {
			return ((ColtMatrix)a).content;
		} else {
			return new FlatArrayMatrix(a.toArray2D());
		}
	}
	
	private FlatArrayVector getVectorContent(Matrix a) {
		if (a instanceof ColtVector) {
			return ((ColtVector)a).content;
		} else {
			return new FlatArrayVector(a.toArray1D());
		}
	}

	@Override
	protected Matrix appendRows(Matrix a) {
		if (a.columns() != this.columns()) {
			throw new IllegalArgumentException("Number of columns must be equal.");
		}
		
		FlatArrayMatrix combined = new FlatArrayMatrix(this.rows() + a.rows(), this.columns());
		combined.viewPart(0, 0, this.rows(), this.columns()).assign(this.content);
		combined.viewPart(this.rows(), 0, a.rows(), a.columns()).assign(getMatrixContent(a));
		return new ColtMatrix(combined);
	}

	@Override
	protected Matrix appendColumns(Matrix a) {
		if (a.rows() != this.rows()) {
			throw new IllegalArgumentException("Number of rows must be equal.");
		}
		
		FlatArrayMatrix combined = new FlatArrayMatrix(this.rows(), this.columns() + a.columns());
		combined.viewPart(0, 0, this.rows(), this.columns()).assign(this.content);
		combined.viewPart(0, this.columns(), a.rows(), a.columns()).assign(getMatrixContent(a));
		return new ColtMatrix(combined);
	}

}
