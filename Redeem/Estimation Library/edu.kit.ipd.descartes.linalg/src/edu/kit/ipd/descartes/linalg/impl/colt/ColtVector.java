package edu.kit.ipd.descartes.linalg.impl.colt;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Range;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorInitializer;
import edu.kit.ipd.descartes.linalg.impl.colt.ColtMatrix.FlatArrayMatrix;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtVector extends Vector {

	private static final Algebra ALG = new Algebra();

	protected static class FlatArrayVector extends DenseDoubleMatrix1D {

		public FlatArrayVector(int rows) {
			super(rows);
		}
		
		public FlatArrayVector(double[] elements) {
			super(elements);
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
			if (vectors[i] instanceof ColtVector) {
				content.viewPart(offset, len).assign(
					((ColtVector) vectors[i]).content);
			} else {
				content.viewPart(offset, len).assign(vectors[i].toArray1D());
			}
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
	public Vector plus(double d) {
		ColtVector result = new ColtVector(this.rows());
		for (int i = 0; i < content.size(); i++) {
			result.content.setQuick(i, content.getQuick(i) + d);
		}
		return result;
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
	public double dot(Vector b) {
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
	public double[] toArray1D() {
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

	@Override
	protected Matrix internalPlus(Matrix a) {
		FlatArrayVector res = (FlatArrayVector) content.copy();
		res.assign(getVectorContent(a), Functions.plus);
		return new ColtVector(res);
	}

	@Override
	protected Matrix internalMinus(Matrix a) {
		FlatArrayVector res = (FlatArrayVector) content.copy();
		res.assign(getVectorContent(a), Functions.minus);
		return new ColtVector(res);
	}

	@Override
	protected Matrix internalMatrixMultiply(Matrix a) {
		if (a.rows() == 1) {
			FlatArrayVector vector = new FlatArrayVector(a.toArray1D());			
			FlatArrayMatrix result = (FlatArrayMatrix)ALG.multOuter(this.content, vector, null);
			return new ColtMatrix(result);			
		} else {
			throw new IllegalArgumentException("Dimensions of operands do not match.");
		}
	}
	
	private FlatArrayVector getVectorContent(Matrix a) {
		if (a instanceof ColtVector) {
			return ((ColtVector)a).content;
		} else {
			return new FlatArrayVector(a.toArray1D());
		}
	}
	
	private FlatArrayMatrix getMatrixContent(Matrix a) {
		if (a instanceof ColtMatrix) {
			return ((ColtMatrix)a).content;
		} else {
			return new FlatArrayMatrix(a.toArray2D());
		}
	}

	@Override
	public Vector slice(Range range) {
		return new ColtVector((FlatArrayVector) content.viewPart(range.getStart(), range.getEnd() - range.getStart()));
	}
	
	@Override
	protected Matrix appendColumns(Matrix a) {		
		if (a.rows() != this.rows()) {
			throw new IllegalArgumentException("Number of rows must be equal.");
		}
		
		FlatArrayMatrix combined = new FlatArrayMatrix(this.rows(), 1 + a.columns());
		combined.viewPart(0, 0, this.rows(), 1).assign(this.toArray2D());
		combined.viewPart(0, 1, a.rows(), a.columns()).assign(getMatrixContent(a));
		return new ColtMatrix(combined);
	}
	
	@Override
	protected Matrix appendRows(Matrix a) {
		if (a.columns() != 1) {
			throw new IllegalArgumentException("Number of columns must be equal.");
		}
		return new ColtVector(new Vector[] {this, (Vector)a});
	}
}
