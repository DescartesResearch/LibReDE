package edu.kit.ipd.descartes.librede.estimation.workload;

import java.util.UUID;


public class Service implements IModelEntity {
	
	private final UUID id;
	private final String name;	

	public Service(String name) {
		super();
		this.id = UUID.randomUUID();
		this.name = name;
	}
	
	@Override
	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}


	
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Service other = (Service) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Service: " + name;
	}
	
}
