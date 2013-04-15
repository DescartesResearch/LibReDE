package edu.kit.ipd.descartes.redeem.estimation.system;


public class Service implements IModelEntity {
	
	private final String name;	

	public Service(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
