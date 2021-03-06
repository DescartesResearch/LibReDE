/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.models.observation;

import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.State;
import tools.descartes.librede.repository.rules.DataDependency;

public class VectorObservationModel implements IObservationModel<Vector> {
	
	private class DefaultWeightingFunction implements IOutputWeightingFunction {
		@Override
		public Vector getOutputWheights() {
			return LinAlg.ones(outputSize);
		}		
	}

	private final IOutputWeightingFunction weights;
	private final List<OutputFunction> outputFunctions;
	private int outputSize;
	
	public VectorObservationModel() {
		weights = new DefaultWeightingFunction();
		outputFunctions = new ArrayList<>();
		outputSize = 0;
	}

	public VectorObservationModel(IOutputWeightingFunction weights) {
		this.weights = weights;
		outputFunctions = new ArrayList<>();
		outputSize = 0;
	}
	
	public void addOutputFunction(OutputFunction function) {
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
			OutputFunction func = outputFunctions.get(i);
			if (func.hasData()) {
				temp[i] = outputFunctions.get(i).getObservedOutput();
			}
		}
		return vector(temp);
	}

	@Override
	public Vector getCalculatedOutput(State state) {
		double[] temp = new double[outputSize];
		for (int i = 0; i < outputSize; i++) {
			OutputFunction func = outputFunctions.get(i);
			if (func.hasData()) {
				temp[i] = func.getCalculatedOutput(state).getValue();
			}
		}
		return vector(temp);
	}

	@Override
	public OutputFunction getOutputFunction(int output) {
		return outputFunctions.get(output);
	}
	
	@Override
	public Iterator<OutputFunction> iterator() {
		return outputFunctions.iterator();
	}

	@Override
	public IOutputWeightingFunction getOutputWeightsFunction() {
		return weights;
	}

	@Override
	public List<DataDependency<?>> getDataDependencies() {
		List<DataDependency<?>> deps = new ArrayList<>();
		for (OutputFunction func : outputFunctions) {
			deps.addAll(func.getDataDependencies());
		}
		return Collections.unmodifiableList(deps);
	}
}
