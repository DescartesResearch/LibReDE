package edu.kit.ipd.descartes.redeem.estimation.models.observation;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;


import java.util.ArrayList;
import java.util.Iterator;

import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;

public class ScalarObservationModel<E extends IOutputFunction> implements IObservationModel<E, Scalar> {
	
	private final E equation;
	
	public ScalarObservationModel(E equation) {
		this.equation = equation;
	}
	
	@Override
	public int getOutputSize() {
		return 1;
	}

	@Override
	public Scalar getObservedOutput() {
		return scalar(equation.getObservedOutput());
	}

	@Override
	public Scalar getCalculatedOutput(Vector state) {
		return scalar(equation.getCalculatedOutput(state));
	}

	@Override
	public Iterator<E> iterator() {
		ArrayList<E> temp = new ArrayList<E>(1);
		temp.add(equation);		
		return temp.iterator();
	}

}
