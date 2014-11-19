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
		Set<String> candidates = Registry.INSTANCE.getInstances(componentClass);

		for (String cand : candidates) {
			try {
				return (IEstimationAlgorithm) Instantiator.newInstance(Registry.INSTANCE.getInstanceClass(cand), Collections.emptyList());
			} catch(Exception ex) {
				log.error("Error instantiating estimation algorithm " + cand + ". Skip it...", ex);
			}
		}
		return null;
	}

}
