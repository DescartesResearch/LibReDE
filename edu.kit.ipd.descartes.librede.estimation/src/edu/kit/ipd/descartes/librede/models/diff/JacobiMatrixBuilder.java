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

import static edu.kit.ipd.descartes.linalg.LinAlg.horzcat;
import static edu.kit.ipd.descartes.linalg.LinAlg.transpose;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;

import java.util.ArrayList;
import java.util.List;

import edu.kit.ipd.descartes.librede.models.observation.IObservationModel;
import edu.kit.ipd.descartes.librede.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.librede.models.state.IStateModel;
import edu.kit.ipd.descartes.librede.models.state.constraints.IStateConstraint;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

public final class JacobiMatrixBuilder {
	
	public static Matrix calculateOfObservationModel(IObservationModel<IOutputFunction, Vector> observationModel, Vector x) {
		List<Vector> dev = new ArrayList<Vector>();
		for (IOutputFunction f : observationModel) {
			if (f instanceof IDifferentiableFunction) {
				dev.add(((IDifferentiableFunction)f).getFirstDerivatives(x));
			} else {
				throw new IllegalStateException("Output function cannot be derived.");
			}
		}
		
		return transpose(horzcat(dev.toArray(new Vector[dev.size()])));
	}
	
	public static Matrix calculateOfConstraints(List<? extends IStateConstraint> constraints, Vector x) {
		List<Vector> dev = new ArrayList<Vector>();
		
		for (IStateConstraint c : constraints) {
			if (c instanceof IDifferentiableFunction) {
				dev.add(((IDifferentiableFunction)c).getFirstDerivatives(x));
			} else {
				throw new IllegalStateException("Constraint function cannot be derived.");
			}
		}		
		
		return vertcat(dev.toArray(new Vector[dev.size()]));		
	}
	
	public static Matrix calculateOfState(IStateModel<?> stateModel, Vector x) {
		List<Vector> dev = new ArrayList<Vector>();
		
		for (IDifferentiableFunction f : stateModel.getStateDerivatives()) {
			dev.add(f.getFirstDerivatives(x));
		}		
		
		return vertcat(dev.toArray(new Vector[dev.size()]));		
	}

}
