package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import java.util.List;

import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;

public abstract class AbstractOutputFunction implements IOutputFunction {
	
	private SystemModel system;
	private List<Resource> selectedResources;
	private List<Service> selectedClasses;

	protected AbstractOutputFunction(SystemModel system, List<Resource> selectedResources,
			List<Service> selectedClasses) {		
		if (system == null || selectedResources == null || selectedClasses == null) {
			throw new IllegalArgumentException();
		}
		if (selectedResources.size() < 1 || selectedClasses.size() < 1) {
			throw new IllegalArgumentException();
		}
		
		this.system = system;
		this.selectedResources = selectedResources;
		this.selectedClasses = selectedClasses;
	}

	public SystemModel getSystem() {
		return system;
	}

	public List<Resource> getSelectedResources() {
		return selectedResources;
	}

	public List<Service> getSelectedWorkloadClasses() {
		return selectedClasses;
	}	
}
