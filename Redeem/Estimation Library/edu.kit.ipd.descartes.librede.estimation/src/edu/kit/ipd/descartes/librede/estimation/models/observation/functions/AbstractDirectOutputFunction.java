package edu.kit.ipd.descartes.librede.estimation.models.observation.functions;

import java.util.Arrays;

import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.LinAlg;
import edu.kit.ipd.descartes.linalg.Vector;

public abstract class AbstractDirectOutputFunction extends
		AbstractLinearOutputFunction implements IDirectOutputFunction {
	
	private Vector zeros;
	private int idx;

	protected AbstractDirectOutputFunction(WorkloadDescription system,
			Resource resource,
			Service service) {
		super(system, Arrays.asList(resource), Arrays.asList(service));
		
		zeros = LinAlg.zeros(getSystem().getState().getStateSize());
		idx = getSystem().getState().getIndex(resource, service);
	}

	@Override
	public Vector getIndependentVariables() {
		return zeros.set(idx, getFactor());
	}

}
