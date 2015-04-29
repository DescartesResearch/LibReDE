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
package tools.descartes.librede.models.diff;

import static tools.descartes.librede.linalg.LinAlg.horzcat;
import static tools.descartes.librede.linalg.LinAlg.transpose;
import static tools.descartes.librede.linalg.LinAlg.vertcat;

import java.util.ArrayList;
import java.util.List;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public final class JacobiMatrixBuilder {
	
	public static Matrix calculateOfObservationModel(IObservationModel<?, ?> observationModel, Vector x) {
		Vector[] dev = new Vector[observationModel.getOutputSize()];
		for (int i = 0; i < dev.length; i++) {
			IOutputFunction f = observationModel.getOutputFunction(i);
			if (f instanceof IDifferentiableFunction) {
				dev[i] = ((IDifferentiableFunction)f).getFirstDerivatives(x);
			} else {
				throw new IllegalStateException("Output function cannot be derived.");
			}
		}
		
		return transpose(horzcat(dev));
	}
	
	public static Matrix calculateOfConstraints(List<? extends IStateConstraint> constraints, Vector x) {
		Vector[] dev = new Vector[constraints.size()];		
		for (int i = 0; i < dev.length; i++) {
			IStateConstraint c = constraints.get(i);
			if (c instanceof IDifferentiableFunction) {
				dev[i] = ((IDifferentiableFunction)c).getFirstDerivatives(x);
			} else {
				throw new IllegalStateException("Constraint function cannot be derived.");
			}
		}		
		
		return vertcat(dev);		
	}
	
	public static Matrix calculateOfState(IStateModel<?> stateModel, Vector x) {
		List<IDifferentiableFunction> functions = stateModel.getStateDerivatives();
		Vector[] dev = new Vector[functions.size()];		
		for (int i = 0; i < dev.length; i++) {
			dev[i] = functions.get(i).getFirstDerivatives(x);
		}		
		
		return vertcat(dev);		
	}

}
