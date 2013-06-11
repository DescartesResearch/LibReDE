package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import java.util.Arrays;

import edu.kit.ipd.descartes.linalg.LinAlg;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;
import edu.kit.ipd.descartes.redeem.estimation.system.SystemModel;

public abstract class AbstractDirectOutputFunction extends
		AbstractLinearOutputFunction implements IDirectOutputFunction {
	
	private Vector zeros;
	private int idx;

	protected AbstractDirectOutputFunction(SystemModel system,
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
