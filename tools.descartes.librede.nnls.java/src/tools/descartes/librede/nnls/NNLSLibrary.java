package tools.descartes.librede.nnls;

import tools.descartes.librede.algorithm.ILeastSquaresRegressionAlgorithm;
import tools.descartes.librede.registry.Registry;

public class NNLSLibrary {
	public static void init() {
		Registry.INSTANCE.registerImplementationType(ILeastSquaresRegressionAlgorithm.class, LeastSquaresRegression.class);
	}
}