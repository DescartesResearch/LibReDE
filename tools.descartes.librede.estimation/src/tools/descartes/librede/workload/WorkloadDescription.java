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
package tools.descartes.librede.workload;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;

public class WorkloadDescription {
	
	private final List<Resource> resources;
	
	private Map<String, Resource> namesToResources;
	
	private final List<Service> services;
	
	private Map<String, Service> namesToServices;
	
	private final State state;

	public WorkloadDescription(List<Resource> resources, List<Service> services) {
		this.resources = resources;
		this.services = services;
		this.state = new State(resources, services);
		initMaps();
	}

	public List<Resource> getResources() {
		return Collections.unmodifiableList(resources);
	}

	public List<Service> getServices() {
		return Collections.unmodifiableList(services);
	}
	
	public Resource getResource(String name) {
		return namesToResources.get(name);
	}
	
	public Service getService(String name) {
		return namesToServices.get(name);
	}
	
	public State getState() {
		return state;
	}
	
	private void initMaps() {
		namesToResources = new HashMap<String, Resource>();
		namesToServices = new HashMap<String, Service>();
		
		for (Resource r : resources) {
			if (resources.contains(r.getName())) {
				//TODO
			} else {
				namesToResources.put(r.getName(), r);
			}
		}
		
		for (Service s : services) {
			if (services.contains(s.getName())) {
				//TODO
			} else {
				namesToServices.put(s.getName(), s);
			}
		}
	}
}
