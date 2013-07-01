package edu.kit.ipd.descartes.redeem.estimation.models.observation;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;

public class VectorObservationModel<E extends IOutputFunction> implements IObservationModel<E, Vector> {

	private List<E> outputFunctions;
	private int outputSize;
	
	public VectorObservationModel() {
		outputFunctions = new ArrayList<>();
		outputSize = 0;
	}
	
	public void addOutputFunction(E function) {
		outputFunctions.add(function);
		outputSize++;
	}
	
	@Override
	public int getOutputSize() {
		return outputSize;
	}

	@Override
	public Vector getObservedOutput() {
		double[] temp = new double[outputSize];
		for (int i = 0; i < outputSize; i++) {
			temp[i] = outputFunctions.get(i).getObservedOutput();
		}
		return vector(temp);
	}

	@Override
	public Vector getCalculatedOutput(Vector state) {
		double[] temp = new double[outputSize];
		for (int i = 0; i < outputSize; i++) {
			temp[i] = outputFunctions.get(i).getCalculatedOutput(state);
		}
		return vector(temp);
	}

	@Override
	public E getOutputFunction(int output) {
		return outputFunctions.get(output);
	}
	
	@Override
	public Iterator<E> iterator() {
		return outputFunctions.iterator();
	}

}
