package edu.kit.ipd.descartes.redeem.estimation.models.diff;

import static edu.kit.ipd.descartes.linalg.LinAlg.horzcat;
import static edu.kit.ipd.descartes.linalg.LinAlg.transpose;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;

import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.ILinearStateConstraint;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.IStateConstraint;

public final class JacobiMatrixBuilder {
	
	public static Matrix calculateOfObservationModel(IObservationModel<IOutputFunction, Vector> observationModel, Vector x) {
		List<Vector> dev = new ArrayList<Vector>();
		for (IOutputFunction f : observationModel) {
			if (f instanceof IDifferentiableFunction) {
				dev.add(((IDifferentiableFunction)f).getFirstDerivatives(x));
			} else {
				throw new IllegalStateException("Output function cannot be derived.");
			}
		}
		
		return transpose(horzcat(dev.toArray(new Vector[dev.size()])));
	}
	
	public static Matrix calculateOfConstraints(List<? extends IStateConstraint> constraints, Vector x) {
		List<Vector> dev = new ArrayList<Vector>();
		
		for (IStateConstraint c : constraints) {
			if (c instanceof IDifferentiableFunction) {
				dev.add(((IDifferentiableFunction)c).getFirstDerivatives(x));
			} else {
				throw new IllegalStateException("Constraint function cannot be derived.");
			}
		}		
		
		return vertcat(dev.toArray(new Vector[dev.size()]));		
	}
	
	public static Matrix calculateOfState(IStateModel<?> stateModel, Vector x) {
		List<Vector> dev = new ArrayList<Vector>();
		
		for (IDifferentiableFunction f : stateModel.getStateDerivatives()) {
			dev.add(f.getFirstDerivatives(x));
		}		
		
		return vertcat(dev.toArray(new Vector[dev.size()]));		
	}

}
