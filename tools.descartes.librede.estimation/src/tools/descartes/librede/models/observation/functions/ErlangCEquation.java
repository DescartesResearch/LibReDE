package tools.descartes.librede.models.observation.functions;

import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.vector;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Vector;

public class ErlangCEquation {
	
	// The step size with which the factors array is increased if it
	// is too small
	private static final int INCREASE_STEP = 10;

	// The degree of the polynomials used to approximate the Erlang-C function
	// A degree of 6 seems to be acceptable for k <= 15 for (tested with matlab)
	private static final int DEGREE = 6;
	
	// The parameters of the polynomial used to approximate the Erlang-C function
	// first index is k: number of Servers
	private double[][] factors = new double[INCREASE_STEP][];
	
	public double calculateValue(int k, double utilization) {
		if (k == 1) {
			// special case
			return utilization;
		} else {
			double[] factors = getFactors(k);
			double value = 0;
			double u = utilization;
			for (int i = 0; i < factors.length; i++) {
				value += factors[i] * u;
				u *= utilization;
			}
			return value;
		}
	}

	public double calculateFirstDerivative(int k, double utilization, double utilizationFirstDerivative) {
		if (k == 1) {
			return utilizationFirstDerivative;		
		} else {
			double[] factors = getFactors(k);
			double u = utilization;
			double value = factors[0] * utilizationFirstDerivative;
			for (int i = 1; i < factors.length; i++) {
				value += (i + 1) * factors[i] * u * utilizationFirstDerivative;
				u *= utilization;
			}
			return value;
		}
	}
	
	public double calculateSecondDerivative(int k, double utilization, double utilizationFirstDerivative) {
		if (k == 1) {
			return 0.0;
		} else {
			double[] factors = getFactors(k);
			double value = 0.0;
			double u = 1.0;
			for (int i = 1; i < factors.length; i++) {
				value += (i + 1) * factors[i] * (i * u * utilizationFirstDerivative);
				u *= utilization;
			}
			return value;
		}
	}
	
	private double[] getFactors(int k) {
		if (k >= factors.length || factors[k - 1] == null) {
			precalculateFactors(k);
		}
		return factors[k - 1];
	}
	
	private void precalculateFactors(int k) {
		// if there is not enough space in the factors array, increase it.
		if (k >= factors.length) {
			int newSize = (k / INCREASE_STEP + 1) * INCREASE_STEP; // always increase by steps of 10
			double[][] oldFactors = factors;
			factors = new double[newSize][];
			System.arraycopy(oldFactors, 0, factors, 0, oldFactors.length);
		}
		
		// now we fit a n-degree polynomial function to the calculated values
		final double[] erlangValues = calculateErlangC(k);
		Matrix a = matrix(erlangValues.length, DEGREE, new MatrixFunction() {			
			@Override
			public double cell(int row, int column) {
				return Math.pow(row / 100.0, column + 1);
			}
		});
		Vector b = vector(erlangValues);
		Matrix x = a.mldivide(b);
		if (!x.isVector()) {
			throw new IllegalStateException();
		}
		factors[k - 1] = x.toArray1D();
	}
	
	private double[] calculateErlangC(int k) {
		double[] func = new double[101];
		func[0] = 0;
		func[100] = 1;
		
		for (int u = 1; u < 100; u++) {
			double util = u / 100.0;
			double phi0 = 0.0;
			double factorial = 1.0;
			for (int i = 0; i < k; i++) {
				factorial = (i == 0) ? factorial : factorial * i;
			    phi0 += Math.pow(k * util, i) / factorial;
			}
			factorial = factorial * k;
			phi0 += Math.pow(k * util, k) / (factorial * (1 - util));
			phi0 = 1 / phi0;
			func[u] = (Math.pow(k * util, k) * phi0) / (factorial * (1 - util));
		}
		return func;
	}

}