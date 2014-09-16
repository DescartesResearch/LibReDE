package edu.kit.ipd.descartes.librede.factory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.kit.ipd.descartes.librede.estimation.repository.IMetric;

public class Registry {
	
	public static final Registry INSTANCE = new Registry();
	
	private Map<String, IMetric> metrics = new HashMap<>();	
	
	private Map< Class<?>, Set<Class<?>> > implementations = new HashMap<>();
	
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
