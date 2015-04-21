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
package tools.descartes.librede.ipopt.java;

import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.mean;
import static tools.descartes.librede.linalg.LinAlg.norm2;
import static tools.descartes.librede.linalg.LinAlg.transpose;
import static tools.descartes.librede.nativehelper.NativeHelper.nativeVector;

import com.sun.jna.Pointer;

import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.diff.JacobiMatrixBuilder;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.registry.Component;

@Component(displayName="Non-linear Constrained Optimization")
public class RecursiveOptimization extends NonlinearOptimization {
	
	private Matrix estimationBuffer;
	
	// Flag indicating whether this is the first iteration.
	private boolean initialized = false;
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.algorithm.IEstimationAlgorithm#initialize(tools.descartes.librede.models.state.IStateModel, tools.descartes.librede.models.observation.IObservationModel, int)
	 */
	@Override
	public void initialize(IStateModel<?> stateModel,
			IObservationModel<?, ?> observationModel, int estimationWindow) throws InitializationException {
		super.initialize(stateModel, observationModel, estimationWindow);
		
		estimationBuffer = matrix(estimationWindow, stateSize, Double.NaN);
	}
	

	/* (non-Javadoc)
	 * @see tools.descartes.librede.algorithm.IEstimationAlgorithm#update()
	 */
	@Override
	public void update() {
		if (stateSize < 0) {
			throw new IllegalStateException("Method initialize() must be called before calling estimate().");
		}		
		
		if (!initialized) {
			// Set initial state
			Vector initialState = getStateModel().getInitialState();
			if (!initialState.isEmpty()) {
				setState(initialState);
				initialized = true;
			}
		}
		
		if (initialized) {
			Vector estimate = solve();
			
			estimationBuffer = estimationBuffer.circshift(1).setRow(0, estimate);		
		}
	}
	
	@Override
	public Vector estimate() throws EstimationException {
		return mean(estimationBuffer, 0);
	}

	public void update(Pointer x, boolean new_x) {
		if (new_x) {
			current = nativeVector(stateSize, x);
			IObservationModel<?, ?> observationModel = getObservationModel();
		
			Vector o_real = observationModel.getObservedOutput();
			Vector o_calc = observationModel.getCalculatedOutput(current);
			error = o_real.minus(o_calc);			
			
			obj = norm2(error);
						
			jacobi = JacobiMatrixBuilder.calculateOfObservationModel(observationModel, current);
			
			objGrad = (Vector)transpose(jacobi).multipliedBy(error).times(-2.0);
		}
	}

	@Override
	public void destroy() {
		
	}	
}
