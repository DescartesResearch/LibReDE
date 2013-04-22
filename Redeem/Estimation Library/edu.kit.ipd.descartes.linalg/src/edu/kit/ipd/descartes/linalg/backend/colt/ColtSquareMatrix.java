package edu.kit.ipd.descartes.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix2D;
import edu.kit.ipd.descartes.linalg.backend.SquareMatrixImplementation;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtSquareMatrix extends ColtMatrix implements SquareMatrixImplementation {
	
	private static final long serialVersionUID = 6300083743869129514L;
	
	public ColtSquareMatrix(double[][] values) {
		super(values);
	}

	public ColtSquareMatrix(int size) {
		super(size, size);
	}
	
	public ColtSquareMatrix(int size, DoubleStorage storage) {
		super(size, size, storage);
	}
	
	@Override
	public double det() {
		return ALG.det(this);
	}

	@Override
	public SquareMatrixImplementation inverse() {
		DoubleMatrix2D inv = ALG.inverse(this); // Returns a matrix of the
												// wrong type!
		ColtSquareMatrix ret = new ColtSquareMatrix(inv.rows());
		ret.assign(inv);
		return ret;
	}

	@Override
	public double rank() {
		return ALG.rank(this);
	}

	@Override
	public double trace() {
		return ALG.trace(this);
	}

	@Override
	public SquareMatrixImplementation pow(int p) {
		DoubleMatrix2D a = this.copy(); // In contrast to the documentation
											// pow alters its first argument
		return (SquareMatrixImplementation) ALG.pow(a, p);
	}
	
	@Override
	public ColtSquareMatrix like() {
		return new ColtSquareMatrix(rows());
	}
	
	@Override
	public ColtMatrix like(int rows, int columns) {
		if (rows == columns) {
			return new ColtSquareMatrix(rows);
		} else {
			return new ColtMatrix(rows, columns);
		}
	}
}
