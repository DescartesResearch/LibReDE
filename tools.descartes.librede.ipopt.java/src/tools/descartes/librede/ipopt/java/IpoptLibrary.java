package tools.descartes.librede.ipopt.java;

import tools.descartes.librede.algorithm.IConstrainedNonLinearOptimizationAlgorithm;
import tools.descartes.librede.registry.Registry;

public class IpoptLibrary {
	public static void init() {
		Registry.INSTANCE.registerImplementationType(IConstrainedNonLinearOptimizationAlgorithm.class, RecursiveOptimization.class);
	}
}
