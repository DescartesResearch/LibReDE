package edu.kit.ipd.descartes.redeem.estimation.system;


public abstract class Resource implements IModelEntity {
	
	private final String name;
		
	public Resource(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}