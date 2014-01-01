package edu.kit.ipd.descartes.linalg.backend;

import edu.kit.ipd.descartes.linalg.Matrix;

public abstract class AbstractMatrix implements Matrix {
	
	@Override
	public boolean isVector() {
		return false;
	}
	
	@Override
	public boolean isScalar() {
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	protected void checkOperandsInnerDimensions(Matrix m) {
		if (columns() != m.rows()) {
			throw new IllegalArgumentException("Inner dimensions of operands must be equal.");
		}
	}
	
	protected void checkOperandsSameSize(Matrix m) {
		if (m.columns() != this.columns() || m.rows() != this.rows()) {
			throw new IllegalArgumentException("Both operands must have the same size.");
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < rows(); i++) {
			builder.append("[");
			for (int j = 0; j < columns(); j++) {
				if (j > 0) {
					builder.append("; ");
				}
				builder.append(get(i, j));
			}
			builder.append("]");
		}
		builder.append("]");
		return builder.toString();
	}
}
