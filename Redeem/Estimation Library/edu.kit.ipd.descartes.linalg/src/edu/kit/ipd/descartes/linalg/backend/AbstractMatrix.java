package edu.kit.ipd.descartes.linalg.backend;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;

public abstract class AbstractMatrix implements Matrix {
	
	protected SharedBuffer result = null;
	
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
	
	@SuppressWarnings("unchecked")
	public <M extends Matrix> M plus(M a) {
		if (a.columns() != this.columns() || a.rows() != this.rows()) {
			throw new IllegalArgumentException("Both operands must have the same size.");
		}
		return (M) internalPlus(a);
	}

	@SuppressWarnings("unchecked")
	public <M extends Matrix> M plus(double a) {
		return (M) internalPlus(a);
	}

	@SuppressWarnings("unchecked")
	public <M extends Matrix> M minus(M a) {
		if (a.columns() != this.columns() || a.rows() != this.rows()) {
			throw new IllegalArgumentException("Both operands must have the same size.");
		}
		return (M) internalMinus(a);
	}

	@SuppressWarnings("unchecked")
	public <M extends Matrix> M minus(double a) {
		return (M) internalMinus(a);
	}

	@SuppressWarnings("unchecked")
	public <M extends Matrix> M multipliedBy(M a) {
		if (columns() != a.rows()) {
			throw new IllegalArgumentException("Inner dimensions of operands must be equal.");
		} else {
			if (a.isScalar()) {
				return (M) internalTimes(((Scalar)a).getValue());
			} else {
				return (M) internalMultipliedBy(a);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public <M extends Matrix> M arrayMultipliedBy(M a) {
		if (a.columns() != this.columns() || a.rows() != this.rows()) {
			throw new IllegalArgumentException("Both operands must have the same size.");
		}
		return (M) internalArrayMultipliedBy(a);
	}

	@SuppressWarnings("unchecked")
	public <M extends Matrix> M times(double a) {
		return (M) internalTimes(a);
	}
	
	protected abstract Matrix internalPlus(Matrix a);
	
	protected abstract Matrix internalPlus(double a);
	
	protected abstract Matrix internalMinus(Matrix a);
	
	protected abstract Matrix internalMinus(double a);
	
	protected abstract Matrix internalTimes(double a);
	
	protected abstract Matrix internalArrayMultipliedBy(Matrix a);
	
	protected abstract Matrix internalMultipliedBy(Matrix a);
	
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
