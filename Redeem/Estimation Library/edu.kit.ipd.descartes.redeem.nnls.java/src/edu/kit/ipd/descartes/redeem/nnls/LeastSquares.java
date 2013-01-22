package edu.kit.ipd.descartes.redeem.nnls;

import com.sun.jna.Memory;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.nnls.backend.NNLSLibrary;
/**
 * 
 * @author Mehran Saliminia
 *
 */
public class LeastSquares {

	public LeastSquares() {

	}

	// Estimates an ordinary least squares regression model with one independent
	// variable.
	// y = intercept + slope * x
	public double simpleRegression(Matrix data) throws Exception {
		throw new Exception("Not implemented!");
	}

	// Implements the Non-Negative Least-Squares (NNLS) algorithm
	// We are initially given the m*n matrix E and the m-vector F
	// Minimize ||Ex-F|| subject to x >= 0
	public Vector nnls(Matrix E, Vector F) {
		try {
			System.loadLibrary("NNLS");
			// The solution vector
			Vector result;

			// Check inputs
			if (E == null || F == null || E.rows() != F.rows())
				throw new Exception("Invalid inputs!");

			// Inputs
			int SIZE_OF_M_VECTOR = F.rows();

			IntByReference mda = new IntByReference(SIZE_OF_M_VECTOR);
			IntByReference m = new IntByReference(SIZE_OF_M_VECTOR); // mda == m
			IntByReference n = new IntByReference(E.columns());
			Memory a = new Memory(8 * mda.getValue() * n.getValue()); // 8 ==
																		// sizeof(double)
			double[] e = new double[SIZE_OF_M_VECTOR * (E.columns())];
			double[] f = new double[SIZE_OF_M_VECTOR];
			for (int i = 0; i < SIZE_OF_M_VECTOR; ++i)
				f[i] = F.get(i);
			int count = 0;
			for (int i = 0; i < E.columns(); ++i)
				for (int j = 0; j < E.rows(); ++j) {
					e[count] = E.get(j, i); // column-order
					count++;
				}
			a.write(0, e, 0, mda.getValue() * n.getValue()); // Fortran expects
																// column-order
																// array!
			Memory b = new Memory(8 * mda.getValue());
			b.write(0, f, 0, mda.getValue());

			// Outputs
			Memory x = new Memory(8 * n.getValue());
			DoubleByReference rnorm = new DoubleByReference();
			IntByReference mode = new IntByReference();

			// Temporary working arrays
			Memory w = new Memory(8 * n.getValue());
			Memory zz = new Memory(8 * m.getValue());
			Memory index = new Memory(4 * n.getValue());
			NNLSLibrary.INSTANCE.nnls_(a, mda, m, n, b, x, rnorm, w, zz, index,
					mode);
			double[] res = new double[n.getValue()];
			x.read(0, res, 0, res.length);
			result = Vector.vector(res);
			return result;

		} catch (Exception ex) {
			System.out.println("[NNLS]: failed!");
			return null;
		}
	}

}
