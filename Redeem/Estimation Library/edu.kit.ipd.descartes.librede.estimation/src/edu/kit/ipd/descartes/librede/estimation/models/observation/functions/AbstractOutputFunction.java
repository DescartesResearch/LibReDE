package edu.kit.ipd.descartes.librede.estimation.models.observation.functions;

import java.util.List;

import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;

public abstract class AbstractOutputFunction implements IOutputFunction {
	
	private WorkloadDescription system;
	private List<Resource> selectedResources;
	private List<Service> selectedClasses;

	protected AbstractOutputFunction(WorkloadDescription system, List<Resource> selectedResources,
			List<Service> selectedClasses) {		
		if (system == null || selectedResources == null || selectedClasses == null) {
			throw new NullPointerException();
		}
		if (selectedResources.size() < 1 || selectedClasses.size() < 1) {
			throw new IllegalArgumentException();
		}
		
		this.system = system;
		this.selectedResources = selectedResources;
		this.selectedClasses = selectedClasses;
	}

	public WorkloadDescription getSystem() {
		return system;
	}

	public List<Resource> getSelectedResources() {
		return selectedResources;
	}

	public List<Service> getSelectedWorkloadClasses() {
		return selectedClasses;
	}	
}
