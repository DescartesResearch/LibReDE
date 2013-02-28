package edu.kit.ipd.descartes.linalg.impl.colt;

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
	public double[][] toArray() {
		return content.toArray();
	}

	@Override
	public void toDoubleStorage(DoubleStorage storage) {
		content.writeTo(storage);
	}

	@Override
	public Matrix appendRow(Vector row) {
		double[][] newRow = new double[1][row.rows()];
		newRow[0]= row.toArray();
		double[][] matrix = content.toArray();
		// append two arrays
		double[][] values = new double[matrix.length + newRow.length][];
		System.arraycopy(matrix, 0, values, 0, matrix.length);
		System.arraycopy(newRow, 0, values, matrix.length, newRow.length);

		content = new FlatArrayMatrix(content.rows() + 1, content.columns());
		content.assign(values);
		return new ColtMatrix(content);
	}

	@Override
	public Matrix plus(Matrix a) {
		FlatArrayMatrix res = (FlatArrayMatrix) content.copy();
		res.assign(((ColtMatrix) a).content, Functions.plus);
		return new ColtMatrix(res);
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
	public Matrix minus(Matrix a) {
		FlatArrayMatrix res = (FlatArrayMatrix) content.copy();
		res.assign(((ColtMatrix) a).content, Functions.minus);
		return new ColtMatrix(res);
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
	public Matrix multipliedBy(Matrix a) {
		FlatArrayMatrix result = new FlatArrayMatrix(this.rows(), a.columns());
		this.content.zMult(((ColtMatrix) a).content, result);
		return new ColtMatrix(result);
	}

	@Override
	public Vector multipliedBy(Vector a) {
		FlatArrayVector result = new FlatArrayVector(this.rows());
		this.content.zMult(((ColtVector) a).content, result);
		return new ColtVector(result);
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
	protected double det() {
		return ALG.det(content);
	}

	@Override
	protected Matrix inverse() {
		DoubleMatrix2D inv = ALG.inverse(content); // Returns a matrix of the
													// wrong type!
		FlatArrayMatrix ret = new FlatArrayMatrix(inv.rows(), inv.columns());
		ret.assign(inv);
		return new ColtMatrix(ret);
	}

	@Override
	protected double rank() {
		return ALG.rank(content);
	}

	@Override
	protected double trace() {
		return ALG.trace(content);
	}

	@Override
	protected Matrix transpose() {
		return new ColtMatrix((FlatArrayMatrix) ALG.transpose(content));
	}

	@Override
	protected Matrix pow(int p) {
		DoubleMatrix2D a = content.copy(); // In contrast to the documentation
											// pow alters its first argument
		return new ColtMatrix((FlatArrayMatrix) ALG.pow(a, p));
	}

}
