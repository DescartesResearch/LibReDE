/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
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
package edu.kit.ipd.descartes.librede.estimation.models.observation;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.linalg.Vector;

public class VectorObservationModel<E extends IOutputFunction> implements IObservationModel<E, Vector> {

	private List<E> outputFunctions;
	private int outputSize;
	
	public VectorObservationModel() {
		outputFunctions = new ArrayList<E>();
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
