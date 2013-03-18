package edu.kit.ipd.descartes.linalg.impl.colt;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.SquareMatrix;
import edu.kit.ipd.descartes.linalg.impl.colt.ColtMatrix.FlatArrayMatrix;

public class ColtSquareMatrix extends SquareMatrix {
	
	private static final Algebra ALG = new Algebra();
	
	public ColtSquareMatrix(Matrix wrappedMatrix) {
		super(wrappedMatrix);
	}

	@Override
	protected double det() {
		return ALG.det(getMatrixContent(delegate));
	}

	@Override
	protected SquareMatrix inverse() {
		DoubleMatrix2D inv = ALG.inverse(getMatrixContent(delegate)); // Returns a matrix of the
																		// wrong type!
		FlatArrayMatrix ret = new FlatArrayMatrix(inv.rows(), inv.columns());
		ret.assign(inv);
		return new ColtSquareMatrix(new ColtMatrix(ret));
	}

	@Override
	protected double rank() {
		return ALG.rank(getMatrixContent(delegate));
	}

	@Override
	protected double trace() {
		return ALG.trace(getMatrixContent(delegate));
	}

	@Override
	protected SquareMatrix pow(int p) {
		DoubleMatrix2D a = getMatrixContent(delegate).copy(); // In contrast to the documentation
											// pow alters its first argument
		return new ColtSquareMatrix(new ColtMatrix((FlatArrayMatrix) ALG.pow(a, p)));
	}
	
	private FlatArrayMatrix getMatrixContent(Matrix a) {
		if (a instanceof ColtMatrix) {
			return ((ColtMatrix)a).content;
		} else {
			return new FlatArrayMatrix(a.toArray2D());
		}
	}

}
