package edu.kit.ipd.descartes.redeem.nnls.test;

import com.sun.jna.Memory;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import edu.kit.ipd.descartes.redeem.nnls.backend.NNLSLibrary;

public class TestScenarios {

	public static int SIZE_OF_M_VECTOR = 4;

	public static void RunTest() {
		double[] coef = new double[4];

		System.out
				.println("\n.......................... RUN 1 ..............................");
		coef[0] = 0;
		coef[1] = 0.1;
		coef[2] = 0.5;
		coef[3] = 0.13;
		TestScenarios.runPolynomTest(coef);

		System.out
				.println("\n.......................... RUN 2 ..............................");
		coef[0] = 0;
		coef[1] = 0.1;
		coef[2] = 0.;
		coef[3] = 0.13;
		TestScenarios.runPolynomTest(coef);

		System.out
				.println("\n.......................... RUN 3 ..............................");
		coef = new double[5];
		coef[0] = 0;
		coef[1] = 0.1;
		coef[2] = 0.5;
		coef[3] = 0.13;
		coef[4] = 0;
		TestScenarios.runPolynomTest(coef);

	}

	private static void runPolynomTest(double[] coef) {
		System.loadLibrary("NNLS");
		Polynomial polynom = new Polynomial(coef.length - 1);
		polynom.setCoef(coef);

		// Inputs
		IntByReference mda = new IntByReference(SIZE_OF_M_VECTOR);
		IntByReference m = new IntByReference(SIZE_OF_M_VECTOR); // mda == m
		IntByReference n = new IntByReference(coef.length - 1);
		Memory a = new Memory(8 * mda.getValue() * n.getValue()); // 8 ==
																	// sizeof(double)
		double[] e = new double[SIZE_OF_M_VECTOR * (coef.length - 1)];
		double[] f = new double[SIZE_OF_M_VECTOR];
		for (int i = 0; i < SIZE_OF_M_VECTOR; ++i)
			f[i] = polynom.getValue(i + 1);

		for (int i = 1; i < coef.length; ++i)
			for (int j = 1; j < SIZE_OF_M_VECTOR + 1; ++j) {
				e[((i - 1) * (SIZE_OF_M_VECTOR)) + j - 1] = Math.pow(j, i);
			}

		System.out.println("---------MATRIX A-------------------------");
		for (int i = 0; i < SIZE_OF_M_VECTOR * (coef.length - 1); ++i)
			System.out.println(e[i]);

		System.out.println("---------M VECTOR-------------------------");
		for (int i = 0; i < SIZE_OF_M_VECTOR; ++i)
			System.out.println(f[i]);
		System.out.println("---------------------------------------------");

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
		System.out.println(polynom.toString());
		System.out.println("calculated coefficients:");
		for (int i = 0; i < res.length; i++) {
			System.out.println(res[i]);
		}
		System.out.println("rnorm:" + rnorm.getValue());
		System.out.println("mode:" + mode.getValue());
	}

}
