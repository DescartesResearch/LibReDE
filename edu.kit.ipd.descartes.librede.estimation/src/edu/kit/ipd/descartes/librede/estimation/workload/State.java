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
package edu.kit.ipd.descartes.librede.estimation.workload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;
import edu.kit.ipd.descartes.linalg.Range;

public class State {
	
	private final Map<Resource, Range> ranges = new HashMap<Resource, Range>();
	private final Map<Service, Integer> serviceIndex = new HashMap<Service, Integer>();
	private final List<Resource> resources;
	private final List<Service> services;
	private final int stateSize;
	
	State(List<Resource> resources, List<Service> services) {
		this.services = services;
		this.resources = resources;
		for (int i = 0; i < services.size(); i++) {
			serviceIndex.put(services.get(i), i);
		}
		for (int i = 0; i < resources.size(); i++) {
			Range r = new Range(i * services.size(), (i + 1) * services.size());
			ranges.put(resources.get(i), r);
		}
		this.stateSize = resources.size() * services.size();
	}
	
	public Range getRange(Resource resource) {
		return ranges.get(resource);
	}
	
	public int getIndex(Resource resource, Service service) {		
		Range r = ranges.get(resource);
		return r.getStart() + serviceIndex.get(service);
	}
	
	public Resource getResource(int index) {
		return resources.get(index / services.size());
	}
	
	public Service getService(int index) {
		return services.get(index % services.size());
	}
	
	public int getStateSize() {
		return stateSize;
	}

}
