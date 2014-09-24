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
package edu.kit.ipd.descartes.librede.models.diff;

import edu.kit.ipd.descartes.librede.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.librede.models.state.constraints.IStateConstraint;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public final class HessianMatrixBuilder {
	
	public static Matrix calculateOfOutputFunction(IOutputFunction function, Vector x) {
		if (function instanceof IDifferentiableFunction) {
			return ((IDifferentiableFunction)function).getSecondDerivatives(x);
		} else {
			throw new IllegalStateException("Output function cannot be derived.");
		}
	}
	
	public static Matrix calculateOfConstraint(IStateConstraint constraint, Vector x) {
		if (constraint instanceof IDifferentiableFunction) {
			return ((IDifferentiableFunction)constraint).getSecondDerivatives(x);
		} else {
			throw new IllegalStateException("Constraint function cannot be derived.");
		}
	}

}
