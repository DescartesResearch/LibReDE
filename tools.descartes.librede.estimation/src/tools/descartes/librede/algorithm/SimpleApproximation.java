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
package tools.descartes.librede.algorithm;

import static tools.descartes.librede.linalg.LinAlg.empty;
import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.max;
import static tools.descartes.librede.linalg.LinAlg.mean;
import static tools.descartes.librede.linalg.LinAlg.min;
import static tools.descartes.librede.linalg.LinAlg.sum;
import static tools.descartes.librede.linalg.LinAlg.vector;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.functions.IDirectOutputFunction;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.repository.Aggregation;

public class SimpleApproximation extends AbstractEstimationAlgorithm {
	
	private Aggregation aggregation;
	private Matrix buffer;
	
	public SimpleApproximation(Aggregation aggregation) {
		this.aggregation = aggregation;
	}

	@Override
	public void initialize(IStateModel<?> stateModel,
			IObservationModel<?, ?> observationModel, int estimationWindow) throws InitializationException {
		super.initialize(stateModel, observationModel, estimationWindow);
		this.buffer = matrix(estimationWindow, stateModel.getStateSize(), Double.NaN);
	}
	
	@Override
	public void update() throws EstimationException {
		final Vector output = getObservationModel().getObservedOutput();		
		
		Vector currentEstimate = vector(output.rows(), new VectorFunction() {			
			@Override
			public double cell(int row) {
				return output.get(row) / getCastedObservationModel().getOutputFunction(row).getFactor();
			}
		});
		buffer = buffer.circshift(1).setRow(0, currentEstimate);		
	}

	@Override
	public Vector estimate() throws EstimationException {
		switch(aggregation) {
		case AVERAGE:
			return mean(buffer, 0);
		case MAXIMUM:
			return max(buffer, 0);
		case MINIMUM:
			return min(buffer, 0);
		case SUM:
			return sum(buffer, 0);
		case NONE:
			return buffer.row(0);				
		}
		return empty();
	}

	@Override
	public void destroy() {
	}
	
	@SuppressWarnings("unchecked")
	private IObservationModel<IDirectOutputFunction, Vector> getCastedObservationModel() {
		return (IObservationModel<IDirectOutputFunction, Vector>) getObservationModel();
	}
}
