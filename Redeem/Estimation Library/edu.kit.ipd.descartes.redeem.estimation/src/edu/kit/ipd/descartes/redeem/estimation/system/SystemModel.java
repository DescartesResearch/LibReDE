package edu.kit.ipd.descartes.redeem.estimation.system;

import java.util.Collections;
import java.util.List;

public class SystemModel {
	
	private List<Resource> resources;
	
	private List<Service> workloadClasses;

	public SystemModel() {

	}

	public List<Resource> getResources() {
		return Collections.unmodifiableList(resources);
	}

	public List<Service> getWorkloadClasses() {
		return Collections.unmodifiableList(workloadClasses);
	}
	
	public State getState() {
		return null;
	}
}
