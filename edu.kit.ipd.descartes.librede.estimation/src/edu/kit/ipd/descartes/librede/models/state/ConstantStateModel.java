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
package edu.kit.ipd.descartes.librede.models.state;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.kit.ipd.descartes.librede.models.diff.IDifferentiableFunction;
import edu.kit.ipd.descartes.librede.models.state.constraints.IStateConstraint;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public class ConstantStateModel<C extends IStateConstraint> implements IStateModel<C> {	
	
	private class ConstantFunction implements IDifferentiableFunction {
		
		private final Vector firstDev;
		private final Vector secondDev;
		
		public ConstantFunction(int stateSize, int varIdx) {
			firstDev = zeros(stateSize).set(varIdx, 1.0);
			secondDev = zeros(stateSize);
		}

		@Override
		public Vector getFirstDerivatives(Vector x) {
			return firstDev;
		}

		@Override
		public Matrix getSecondDerivatives(Vector x) {
			return secondDev;
		}
		
	}
	
	private int stateSize;
	private List<C> constraints = new ArrayList<C>();
	private Vector initialState;
	private List<IDifferentiableFunction> derivatives = new ArrayList<IDifferentiableFunction>();
	
	public ConstantStateModel(int stateSize, Vector initialState) {
		if (stateSize <= 0) {
			throw new IllegalArgumentException("State size must be greater than 0.");
		}
		if (initialState == null) {
			throw new IllegalArgumentException("Initial state must not be null.");
		}
		if (initialState.rows() != stateSize) {
			throw new IllegalArgumentException("Size of initial state vector must be equal to the state size.");
		}
		
		this.stateSize = stateSize;
		this.initialState = initialState;
		
		for (int i = 0; i < stateSize; i++) {
			derivatives.add(new ConstantFunction(stateSize, i));
		}
	}

	@Override
	public int getStateSize() {
		return stateSize;
	}

	@Override
	public Vector getNextState(Vector state) {
		return state;
	}

	@Override
	public void addConstraint(C constraint) {
		if (constraint == null) {
			throw new IllegalArgumentException("Constraint must not be null.");
		}
		
		constraints.add(constraint);
	}

	@Override
	public List<C> getConstraints() {
		return Collections.unmodifiableList(constraints);
	}

	@Override
	public List<IDifferentiableFunction> getStateDerivatives() {
		return derivatives;
	}
	
	@Override
	public Vector getInitialState() {
		return initialState;
	}

}
