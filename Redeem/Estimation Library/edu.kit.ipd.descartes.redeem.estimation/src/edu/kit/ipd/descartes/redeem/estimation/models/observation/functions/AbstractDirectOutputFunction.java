package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import java.util.List;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;

public abstract class AbstractDirectOutputFunction extends
		AbstractLinearOutputFunction implements IDirectOutputFunction {

	protected AbstractDirectOutputFunction(SystemModel system,
			List<Resource> selectedResources,
			List<Service> selectedClasses) {
		super(system, selectedResources, selectedClasses);
	}

	@Override
	public Vector getIndependentVariables() {
		return null;
	}

}
