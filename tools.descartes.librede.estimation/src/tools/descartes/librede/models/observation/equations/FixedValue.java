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
package tools.descartes.librede.models.observation.equations;

import static tools.descartes.librede.linalg.LinAlg.scalar;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public abstract class FixedValue extends ModelEquation {

	protected FixedValue(IStateModel<? extends IStateConstraint> stateModel, int historicInterval) {
		super(stateModel, historicInterval);
	}

	@Override
	public Vector getFactors() {
		return scalar(getConstantValue());
	}
	
	@Override
	public DerivativeStructure getValue(State state) {
		return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), getConstantValue());
	}

	@Override
	public boolean isLinear() {
		return true;
	}
	
	@Override
	public boolean isConstant() {
		return true;
	}

}
