package edu.kit.ipd.descartes.librede.factory;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.descartesresearch.librede.configuration.Parameter;


public class Instantiator {
	
	public static Object newInstance(Class<?> type, List<Parameter> parameters) throws InstantiationException, IllegalAccessException {
		Object o = type.newInstance();
		Map<String, Field> parameterDefs = new HashMap<String, Field>();
		
		
		for (Field f : type.getDeclaredFields()) {
			if (f.isAnnotationPresent(ParameterDefinition.class)) {
				ParameterDefinition def = f.getAnnotation(ParameterDefinition.class);
				String defaultValue = def.defaultValue();
				parameterDefs.put(f.getAnnotation(ParameterDefinition.class).name(), f);
				if (defaultValue != null && !defaultValue.isEmpty()) {
					setParameter(o, f, defaultValue);
				}
			}
		}
		
		for (Parameter p : parameters) {
			Field f = parameterDefs.get(p.getName());
			setParameter(o, f, p.getValue());
		}
		
		return o;
	}
	
	private static void setParameter(Object obj, Field f, String value) throws IllegalArgumentException, IllegalAccessException {
		if (f != null) {
			f.setAccessible(true);
			if (f.getType().equals(String.class)) {
				f.set(obj, value);
			} else if (f.getType().equals(File.class)) {
				f.set(obj, new File(value));
			} else if (f.getType().equals(boolean.class)) {
				f.setBoolean(obj, Boolean.parseBoolean(value));
			} else if (f.getType().equals(short.class)) {
				f.setShort(obj, Short.parseShort(value));
			} else if (f.getType().equals(int.class)) {
				f.setInt(obj, Integer.parseInt(value));
			} else if (f.getType().equals(long.class)) {
				f.setLong(obj, Long.parseLong(value));
			} else if (f.getType().equals(double.class)) {
				f.setDouble(obj, Double.parseDouble(value));
			} else if (f.getType().equals(float.class)) {
				f.setFloat(obj, Float.parseFloat(value));
			}
		}
	}

}
