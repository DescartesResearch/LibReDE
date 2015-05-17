package tools.descartes.librede.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;

public class ColtMatrixBuilder extends MatrixBuilder {
	
	private DoubleMatrix2D matrix;
	private int last;
	private int capacity;
	
	public ColtMatrixBuilder(int capacity, int columns) {
		matrix = new DenseDoubleMatrix2D(capacity, columns);
		last = 0;
		this.capacity = capacity;
	}

	@Override
	public void addRow(Vector values) {
		if (values.rows() != matrix.columns()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (last == capacity) {
			grow();
		}
		for (int i = 0; i < values.rows(); i++) {
			matrix.setQuick(last, i, values.get(i));
		}
		last++;
	}

	@Override
	public void addRow(double... values) {
		if (values.length != matrix.columns()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		if (last == capacity) {
			grow();
		}
		for (int i = 0; i < values.length; i++) {
			matrix.setQuick(last, i, values[i]);
		}
		last++;		
	}

	@Override
	public Matrix toMatrix() {
		if (matrix.columns() > 1) {
			if (last == capacity) {
				return new ColtMatrix(matrix);
			} else {
				return new ColtMatrix(matrix.viewPart(0, 0, last, matrix.columns()));
			}
		}
		return new ColtVector(matrix.viewColumn(0));
	}
	
	private void grow() {
		capacity = capacity * 2;
		DoubleMatrix2D newValues = new DenseDoubleMatrix2D(capacity, matrix.columns());
		newValues.viewPart(0, 0, matrix.rows(), matrix.columns()).assign(matrix);
		matrix = newValues;
	}
	
	

}
