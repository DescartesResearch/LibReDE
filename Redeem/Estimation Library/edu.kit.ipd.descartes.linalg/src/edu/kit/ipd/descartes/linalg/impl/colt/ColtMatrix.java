package edu.kit.ipd.descartes.linalg.impl.colt;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.impl.colt.ColtVector.FlatArrayVector;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtMatrix extends Matrix {
	
	private static final Algebra ALG = new Algebra(); 
	
	protected static class FlatArrayMatrix extends DenseDoubleMatrix2D {
		
		public FlatArrayMatrix(int rows, int columns) {
			super(rows, columns);
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
	
	@Override
	public void assign(double[][] values) {
		content.assign(values);
	}
	
	@Override
	public void set(int row, int col, double value) {
		content.set(row, col, value);
	}

	@Override
	public double get(int row, int col) {
		return content.get(row, col);
	}
	
	@Override
	public Vector rowVector(int row) {
		return new ColtVector((FlatArrayVector)content.viewRow(row));
	}
	
	@Override
	public Vector columnVector(int column) {
		return new ColtVector((FlatArrayVector)content.viewColumn(column));
	}

	@Override
	public int rowCount() {
		return content.rows();
	}

	@Override
	public int columnCount() {
		return content.columns();
	}

	@Override
	public double[][] toArray() {
		return content.toArray();
	}

	@Override
	public void readFrom(DoubleStorage storage) {
		content.readFrom(storage);
	}

	@Override
	public void writeTo(DoubleStorage storage) {
		content.writeTo(storage);		
	}
	
	@Override
	public Matrix multiply(Matrix a) {
		return new ColtMatrix((FlatArrayMatrix)ALG.mult(content, ((ColtMatrix)a).content));
	}
	
	@Override
	public Vector multiply(Vector a) {
		return new ColtVector((FlatArrayVector)ALG.mult(content, ((ColtVector)a).content));
	}
}
