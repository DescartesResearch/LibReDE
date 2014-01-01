package edu.kit.ipd.descartes.redeem.estimation.workload;

import java.util.UUID;


public class Resource implements IModelEntity {
	
	private final UUID id;
	private final String name;
	private final int parallelServers;
		
	public Resource(String name) {
		this(name, 1);
	}
	
	public Resource(String name, int parallelServers) {
		this.id = UUID.randomUUID();
		this.name = name;
		this.parallelServers = parallelServers;
	}
	
	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public int getNumberOfParallelServers() {
		return parallelServers;
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
		Resource other = (Resource) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Resource: " + name;
	}
}