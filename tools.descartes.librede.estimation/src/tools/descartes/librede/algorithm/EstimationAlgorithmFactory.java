/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
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
