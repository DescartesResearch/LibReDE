package edu.kit.ipd.descartes.redeem.estimation.system;

import java.util.Collections;
import java.util.List;

public class SystemModel {
	
	private final List<Resource> resources;
	
	private final List<Service> services;
	
	private final State state;

	public SystemModel(List<Resource> resources, List<Service> services) {
		this.resources = resources;
		this.services = services;
		this.state = new State(resources, services);
	}

	public List<Resource> getResources() {
		return Collections.unmodifiableList(resources);
	}

	public List<Service> getServices() {
		return Collections.unmodifiableList(services);
	}
	
	public State getState() {
		return state;
	}
}
