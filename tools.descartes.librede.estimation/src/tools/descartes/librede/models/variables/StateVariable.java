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
package tools.descartes.librede.models.variables;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.models.State;

public class StateVariable extends Variable {
	
	private final int index;

	public StateVariable(State state, double value, int stateIdx) {
		super(state, getDerivativeStructure(state, value, stateIdx));
		this.index = stateIdx;
	}
	
	private static DerivativeStructure getDerivativeStructure(State state, double value, int stateIdx) {
		// IMPORTANT: if we need derivatives, set first derivative to one
		if (state.getDerivationOrder() == 0) {
			return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), value);
		} else {
			return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), stateIdx, value);
		}
	}

}
