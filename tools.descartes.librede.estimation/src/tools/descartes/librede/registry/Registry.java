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
package tools.descartes.librede.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.MetricsFactory;
import tools.descartes.librede.metrics.MetricsRepository;
import tools.descartes.librede.repository.IMetric;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.UnitsFactory;
import tools.descartes.librede.units.UnitsRepository;

public class Registry {
	
	public static final Registry INSTANCE = new Registry();
	
	private UnitsRepository units = UnitsFactory.eINSTANCE.createUnitsRepository();
	
	private MetricsRepository metrics = MetricsFactory.eINSTANCE.createMetricsRepository();
	
	private Map<String, Class<?>> instances = new HashMap<String, Class<?>>();	
	
	private Map< Class<?>, Set<String> > components = new HashMap< Class<?>, Set<String> >();
	
	private Registry() {}
	
	public void registerMetric(Metric metric) {
		if (metric == null) {
			throw new NullPointerException();
		}
		metrics.getMetrics().add(metric);
	}
	
	public List<Metric> getMetrics() {
		return Collections.unmodifiableList(metrics.getMetrics());
	}
	
	public void registerDimension(Dimension dimension) {
		if (dimension == null) {
			throw new NullPointerException();
		}
		units.getDimensions().add(dimension);
	}
	
	public List<Dimension> getDimensions() {
		return Collections.unmodifiableList(units.getDimensions());
	}

	public void registerImplementationType(Class<?> componentClass, Class<?> instanceClass) {
		Component comp = instanceClass.getAnnotation(Component.class);
		if (comp == null) {
			throw new IllegalArgumentException("The instance class must have a component annotation.");
		}
		String id = comp.alias();
		if (id.isEmpty()) {
			id = instanceClass.getName();
		}
		
		Set<String> impl = components.get(componentClass);
		if (impl == null) {
			impl = new HashSet<String>();
			components.put(componentClass, impl);
		}
		impl.add(id);
		instances.put(id, instanceClass);
	}
	
	public Set<String> getInstances(Class<?> componentClass) {
		Set<String> impl = components.get(componentClass);
		if (impl == null) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(impl);
		}
	}
	
	public Class<?> getInstanceClass(String id) {
		return instances.get(id);
	}
	
	public String getDisplayName(Class<?> instanceClass) {
		Component comp = instanceClass.getAnnotation(Component.class);
		return comp.displayName();
	}
}
