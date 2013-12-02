package edu.kit.ipd.descartes.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix2D;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.SquareMatrix;

public class ColtSquareMatrix extends ColtMatrix implements SquareMatrix {
	
	protected ColtSquareMatrix(DoubleMatrix2D delegate) {
		super(delegate);
	}
	
	public ColtSquareMatrix(double[][] values) {
		super(values);
	}

	public ColtSquareMatrix(int size) {
		super(size, size);
	}
	
	public ColtSquareMatrix(int size, double fill) {
		super(size, size, fill);
	}
	
	public ColtSquareMatrix(int size, MatrixFunction init) {
		super(size, size, init);
	}
	
	@Override
	public double det() {
		return ALG.det(delegate);
	}

	@Override
	public SquareMatrix inverse() {
		DoubleMatrix2D inv = ALG.inverse(delegate); // Returns a matrix of the
												// wrong type!
		return new ColtSquareMatrix(inv);
	}

	@Override
	public double rank() {
		return ALG.rank(delegate);
	}

	@Override
	public double trace() {
		return ALG.trace(delegate);
	}

	@Override
	public SquareMatrix pow(int p) {
		DoubleMatrix2D a = copyMatrix(); // In contrast to the documentation
											// pow alters its first argument
		return new ColtSquareMatrix(ALG.pow(a, p));
	}


}
