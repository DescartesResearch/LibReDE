package tools.descartes.librede.algorithm;

import java.util.Collections;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.registry.Instantiator;
import tools.descartes.librede.registry.Registry;

public class EstimationAlgorithmFactory {
	
	private static final Logger log = Logger.getLogger(EstimationAlgorithmFactory.class);
	
	private LibredeConfiguration configuration;
	
	public EstimationAlgorithmFactory(LibredeConfiguration configuration) {
		this.configuration = configuration;
	}
	
	public IEstimationAlgorithm createInstance(Class<?> componentClass) {
		Set<Class<?>> candidates = Registry.INSTANCE.getImplementationClasses(componentClass);

		for (Class<?> cl : candidates) {
			try {
				return (IEstimationAlgorithm) Instantiator.newInstance(cl, Collections.emptyList());
			} catch(Exception ex) {
				log.error("Error instantiating estimation algorithm " + cl.toString() + ". Skip it...", ex);
			}
		}
		return null;
	}

}
