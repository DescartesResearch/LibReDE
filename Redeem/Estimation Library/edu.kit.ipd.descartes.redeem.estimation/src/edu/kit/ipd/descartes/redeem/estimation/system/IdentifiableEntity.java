package edu.kit.ipd.descartes.redeem.estimation.system;

public abstract class IdentifiableEntity {

	private final String identifier;
	
	public IdentifiableEntity(String identifier) {
		super();
		this.identifier = identifier;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	
}
