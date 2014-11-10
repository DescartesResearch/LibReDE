package tools.descartes.librede.bayesplusplus;

import tools.descartes.librede.algorithm.IKalmanFilterAlgorithm;
import tools.descartes.librede.registry.Registry;

public class BayesLibrary {
	
	public static void init() {
		Registry.INSTANCE.registerImplementationType(IKalmanFilterAlgorithm.class, ExtendedKalmanFilter.class);
	}

}
