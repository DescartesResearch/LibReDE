package edu.kit.ipd.descartes.librede.estimation.workload;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
