package edu.kit.ipd.descartes.linalg.impl.colt;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorInitializer;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtVector extends Vector {

	private static final Algebra ALG = new Algebra();

	protected static class FlatArrayVector extends DenseDoubleMatrix1D {

		public FlatArrayVector(int rows) {
			super(rows);
		}

		public FlatArrayVector(int size, double[] elements, int zero, int stride) {
			super(size, elements, zero, stride);
		}

		public void readFrom(DoubleStorage storage) {
			storage.read(elements);
		}

		public void writeTo(DoubleStorage storage) {
			storage.write(elements);
		}

		@Override
		public DoubleMatrix1D like(int size) {
			return new FlatArrayVector(size);
		}

		@Override
		public DoubleMatrix2D like2D(int rows, int columns) {
			return new ColtMatrix.FlatArrayMatrix(rows, columns);
		}

	}

	protected FlatArrayVector content;

	protected ColtVector(FlatArrayVector content) {
		this.content = content;
	}

	public ColtVector(int rows) {
		content = new FlatArrayVector(rows);
	}

	public ColtVector(int rows, double fill) {
		content = new FlatArrayVector(rows);
		content.assign(fill);
	}

	public ColtVector(double... values) {
		content = new FlatArrayVector(values.length);
		content.assign(values);
	}

	public ColtVector(int rows, DoubleStorage storage) {
		content = new FlatArrayVector(rows);
		content.readFrom(storage);
	}

	public ColtVector(int rows, VectorInitializer init) {
		content = new FlatArrayVector(rows);
		for (int i = 0; i < rows; i++) {
			content.setQuick(i, init.cell(i));
		}
	}

	public ColtVector(Vector[] vectors) {
		int rows = 0;
		for (int i = 0; i < vectors.length; i++) {
			rows += vectors[i].rows();
		}
		content = new FlatArrayVector(rows);
		int offset = 0;
		for (int i = 0; i < vectors.length; i++) {
			int len = vectors[i].rows();
			content.viewPart(offset, len).assign(
					((ColtVector) vectors[i]).content);
			offset += len;
		}
	}

	@Override
	public double get(int row) {
		return content.get(row);
	}

	@Override
	public int rows() {
		return content.size();
	}

	@Override
	public Vector plus(Vector a) {
		FlatArrayVector res = (FlatArrayVector) content.copy();
		res.assign(((ColtVector) a).content, Functions.plus);
		return new ColtVector(res);
	}

	@Override
	public Vector plus(double d) {
		ColtVector result = new ColtVector(this.rows());
		for (int i = 0; i < content.size(); i++) {
			result.content.setQuick(i, content.getQuick(i) + d);
		}
		return result;
	}

	@Override
	public Vector minus(Vector a) {
		FlatArrayVector res = (FlatArrayVector) content.copy();
		res.assign(((ColtVector) a).content, Functions.minus);
		return new ColtVector(res);
	}

	@Override
	public Vector minus(double d) {
		ColtVector result = new ColtVector(this.rows());
		for (int i = 0; i < content.size(); i++) {
			result.content.setQuick(i, content.getQuick(i) - d);
		}
		return result;
	}

	@Override
	public double multipliedBy(Vector b) {
		return content.zDotProduct(((ColtVector) b).content);
	}

	@Override
	public Vector times(double d) {
		ColtVector result = new ColtVector(this.rows());
		for (int i = 0; i < content.size(); i++) {
			result.content.setQuick(i, content.getQuick(i) * d);
		}
		return result;
	}

	@Override
	protected Vector abs() {
		ColtVector result = new ColtVector(this.rows());
		for (int i = 0; i < content.size(); i++) {
			result.content.setQuick(i, Math.abs(content.getQuick(i)));
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
	public double[] toArray() {
		return content.toArray();
	}

	@Override
	public void toDoubleStorage(DoubleStorage storage) {
		content.writeTo(storage);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < content.size(); i++) {
			if (i > 0) {
				builder.append("; ");
			}
			builder.append(content.get(i));
		}
		builder.append("]");
		return builder.toString();
	}
}
