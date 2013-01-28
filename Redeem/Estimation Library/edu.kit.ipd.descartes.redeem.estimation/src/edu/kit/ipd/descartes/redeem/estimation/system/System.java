package edu.kit.ipd.descartes.redeem.estimation.system;

import java.util.List;

public class System extends IdentifiableEntity {
	
	private List<Resource> resources;
	
	private Workload workload;

	public System(String identifier) {
		super(identifier);
	}
}
