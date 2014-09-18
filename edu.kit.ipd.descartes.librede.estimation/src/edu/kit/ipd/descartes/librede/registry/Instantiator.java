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
