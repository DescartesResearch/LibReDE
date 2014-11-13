package tools.descartes.librede.models.state;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;

public class StateVariable implements Comparable<StateVariable> {
	private final Resource resource;
	private final Service service;
	
	public StateVariable(Resource resource, Service service) {
		this.resource = resource;
		this.service = service;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public Service getService() {
		return service;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((resource == null) ? 0 : resource.hashCode());
		result = prime * result
				+ ((service == null) ? 0 : service.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StateVariable other = (StateVariable) obj;
		if (resource == null) {
			if (other.resource != null)
				return false;
		} else if (!resource.equals(other.resource))
			return false;
		if (service == null) {
			if (other.service != null)
				return false;
		} else if (!service.equals(other.service))
			return false;
		return true;
	}

	@Override
	public int compareTo(StateVariable o) {
		int ret = resource.getName().compareTo(o.resource.getName());
		if (ret == 0) {
			ret = service.getName().compareTo(o.service.getName());
		}
		return ret;
	}
}
