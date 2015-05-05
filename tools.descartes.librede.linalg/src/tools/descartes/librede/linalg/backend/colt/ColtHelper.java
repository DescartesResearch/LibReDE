package tools.descartes.librede.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;

public class ColtHelper {
	
	private static final Algebra ALG = new Algebra();
	
	private static Matrix wrap(DoubleMatrix2D matrix) {
		int rows = matrix.rows();
		int columns = matrix.columns();
		if (columns == 1) {
			if (rows == 1) {
				return new Scalar(matrix.getQuick(0, 0));
			} else {
				return new ColtVector(matrix.viewColumn(0));
			}
		} else {
			if (rows == columns) {
				return new ColtSquareMatrix(matrix);
			} else {
				return new ColtMatrix(matrix);
			}
		}		
	}
	
	public static ColtMatrix toColtMatrix(Matrix a) {
		if (a instanceof ColtMatrix) {
			return (ColtMatrix) a;
		} else {
			return new ColtMatrix(a.toArray2D());
		}
	}

	public static ColtVector toColtVector(Vector a) {
		if (a instanceof ColtVector) {
			return (ColtVector) a;
		} else {
			return new ColtVector(a.toArray1D());
		}
	}
	
	public static Matrix solve(Matrix a, Matrix b) {
		if (a.rows() != b.rows()) {
			throw new IllegalArgumentException("A and B must have the same number of rows.");
		}
		ColtMatrix aMat = toColtMatrix(a);
		ColtMatrix bMat = toColtMatrix(b);

		DoubleMatrix2D x = ALG.solve(aMat.delegate, bMat.delegate);		
		return wrap(x);
	}

}
