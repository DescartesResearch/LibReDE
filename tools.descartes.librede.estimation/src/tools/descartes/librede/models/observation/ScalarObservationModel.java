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
package tools.descartes.librede.models.observation;

import static tools.descartes.librede.linalg.LinAlg.scalar;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.repository.rules.DataDependency;

public class ScalarObservationModel<E extends IOutputFunction> implements IObservationModel<E, Scalar> {
	
	private static class DefaultOutputWeighting implements IOutputWeightingFunction {
		@Override
		public Vector getOutputWheights() {
			return scalar(1);
		}		
	}
	
	private final IOutputWeightingFunction weights;
	private final E outputFunction;
	
	public ScalarObservationModel(E outputFunction) {
		this.weights = new DefaultOutputWeighting();
		this.outputFunction = outputFunction;
	}
	
	public ScalarObservationModel(E outputFunction, IOutputWeightingFunction weights) {
		this.weights = weights;
		this.outputFunction = outputFunction;
	}
	
	@Override
	public int getOutputSize() {
		return 1;
	}

	@Override
	public Scalar getObservedOutput() {
		return scalar(outputFunction.getObservedOutput());
	}

	@Override
	public Scalar getCalculatedOutput(Vector state) {
		return scalar(outputFunction.getCalculatedOutput(state));
	}
	
	@Override
	public E getOutputFunction(int output) {
		if (output != 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		return outputFunction;
	}

	@Override
	public Iterator<E> iterator() {
		ArrayList<E> temp = new ArrayList<E>(1);
		temp.add(outputFunction);		
		return temp.iterator();
	}

	@Override
	public IOutputWeightingFunction getOutputWeightsFunction() {
		return weights;
	}

	@Override
	public List<DataDependency<?>> getDataDependencies() {
		return outputFunction.getDataDependencies();
	}

}
