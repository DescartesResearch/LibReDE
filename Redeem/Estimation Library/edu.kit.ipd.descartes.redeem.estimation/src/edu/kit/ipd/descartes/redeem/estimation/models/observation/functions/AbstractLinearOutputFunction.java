package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;

import java.util.List;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public abstract class AbstractLinearOutputFunction extends AbstractOutputFunction implements ILinearOutputFunction, IDifferentiableFunction {
	
	protected AbstractLinearOutputFunction(WorkloadDescription system,
			List<Resource> selectedResources,
			List<Service> selectedClasses) {
		super(system, selectedResources, selectedClasses);
	}
	
	@Override
	public double getCalculatedOutput(Vector state) {
		return getIndependentVariables().dot(state);
	}

	@Override
	public Vector getFirstDerivatives(Vector state) {
		return getIndependentVariables();
	}

	@Override
	public Matrix getSecondDerivatives(Vector state) {
		return zeros(state.rows(), state.rows());
	}

}
