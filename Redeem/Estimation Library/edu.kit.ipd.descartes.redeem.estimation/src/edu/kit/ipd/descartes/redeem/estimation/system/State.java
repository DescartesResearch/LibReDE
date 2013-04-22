package edu.kit.ipd.descartes.redeem.estimation.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.kit.ipd.descartes.linalg.Range;

public class State {
	
	private Map<Resource, Range> ranges = new HashMap<>();
	private Map<Service, Integer> serviceIndex = new HashMap<>();
	private List<Resource> resources;
	private List<Service> services;
	
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

}
