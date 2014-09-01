package edu.kit.ipd.descartes.librede.factory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ComponentRegistry {
	
	public static final ComponentRegistry INSTANCE = new ComponentRegistry();
	
	private Map< Class<?>, Class<?> > itemClasses = new HashMap<>();
	
	private Map< Class<?>, Set<Class<?>> > implementations = new HashMap<>();
	
	private ComponentRegistry() {}
	
	public void registerImplementationType(Class<?> componentClass, Class<?> implementationClass) {
		Set<Class<?>> impl = implementations.get(componentClass);
		if (impl == null) {
			impl = new HashSet<Class<?>>();
			implementations.put(componentClass, impl);
		}
		impl.add(implementationClass);
	}
	
	public void registerItemType(Class<?> componentClass, Class<?> itemClass) {
		itemClasses.put(componentClass, itemClass);
	}

	public Set<Class<?>> getImplementationClasses(Class<?> componentClass) {
		Set<Class<?>> impl = implementations.get(componentClass);
		if (impl == null) {
			return Collections.emptySet();
		} else {
			return Collections.unmodifiableSet(impl);
		}
	}
	
	public Class<?> getItemClass(Class<?> componentClass) {
		return itemClasses.get(componentClass);
	}
}
