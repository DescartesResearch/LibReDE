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
package tools.descartes.librede.models.state.constraints;

import java.util.List;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.diff.IDifferentiableFunction;

public class StateBoundsConstraint implements ILinearStateConstraint, IDifferentiableFunction {
	
	private double lower;
	private double upper;
	private int stateVar;
	
	public StateBoundsConstraint(int stateVar, double lowerBound, double upperBound) {
		this.lower = lowerBound;
		this.upper = upperBound;
		this.stateVar = stateVar;
	}
	
	public int getStateVariable() {
		return stateVar;
	}
	
	@Override
	public double getValue(Vector state) {
		return state.get(stateVar);
	}

	@Override
	public double getLowerBound() {
		return lower;
	}

	@Override
	public double getUpperBound() {
		return upper;
	}

	@Override
	public Vector getFirstDerivatives(Vector x) {
		
		return null;
	}

	@Override
	public Matrix getSecondDerivatives(Vector x) {
		return null;
	}

	@Override
	public boolean isApplicable(List<String> messages) {
		return true;
	}

}
