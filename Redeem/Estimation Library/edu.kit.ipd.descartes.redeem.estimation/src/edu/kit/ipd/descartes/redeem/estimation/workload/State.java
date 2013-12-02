package edu.kit.ipd.descartes.redeem.estimation.workload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
