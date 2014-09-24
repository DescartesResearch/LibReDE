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
package edu.kit.ipd.descartes.librede.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.kit.ipd.descartes.librede.repository.IMetric;

public class Registry {
	
	public static final Registry INSTANCE = new Registry();
	
	private Map<String, IMetric> metrics = new HashMap<String, IMetric>();	
	
	private Map< Class<?>, Set<Class<?>> > implementations = new HashMap< Class<?>, Set<Class<?>> >();
	
	private Registry() {}
	
	public void registerMetric(String literal, IMetric metric) {
		if (literal == null || metric == null) {
			throw new NullPointerException();
		}
		metrics.put(literal, metric);
	}
	
	public Collection<IMetric> getMetrics() {
		return metrics.values();
	}
	
	public IMetric getMetric(String literal) {
		return metrics.get(literal);
	}
	
	public void registerImplementationType(Class<?> componentClass, Class<?> implementationClass) {
		Set<Class<?>> impl = implementations.get(componentClass);
		if (impl == null) {
			impl = new HashSet<Class<?>>();
			implementations.put(componentClass, impl);
		}
		impl.add(implementationClass);
	}
	
	public Set<Class<?>> getImplementationClasses(Class<?> componentClass) {
		Set<Class<?>> impl = implementations.get(componentClass);
		if (impl == null) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(impl);
		}
	}
	
	public String getDisplayName(Class<?> implementationClass) {
		Component comp = implementationClass.getAnnotation(Component.class);
		if (comp != null) {
			return comp.displayName();
		} else {
			return implementationClass.toString();
		}
	}
}
