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
package edu.kit.ipd.descartes.librede.estimation.algorithm;

import static edu.kit.ipd.descartes.linalg.LinAlg.empty;
import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.max;
import static edu.kit.ipd.descartes.linalg.LinAlg.mean;
import static edu.kit.ipd.descartes.linalg.LinAlg.min;
import static edu.kit.ipd.descartes.linalg.LinAlg.sum;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import edu.kit.ipd.descartes.librede.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IDirectOutputFunction;
import edu.kit.ipd.descartes.librede.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.librede.estimation.repository.Aggregation;
import edu.kit.ipd.descartes.linalg.AggregationFunction;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;

public class SimpleApproximation implements IEstimationAlgorithm<ConstantStateModel<Unconstrained>, IObservationModel<IDirectOutputFunction, Vector>> {
	
	private ConstantStateModel<Unconstrained> stateModel;
	private IObservationModel<IDirectOutputFunction, Vector> observationModel;
	private Aggregation aggregation;
	private Matrix buffer;
	
	public SimpleApproximation(Aggregation aggregation) {
		this.aggregation = aggregation;
	}

	@Override
	public void initialize(ConstantStateModel<Unconstrained> stateModel,
			IObservationModel<IDirectOutputFunction, Vector> observationModel, int estimationWindow) throws InitializationException {
		this.stateModel = stateModel;
		this.observationModel = observationModel;
		this.buffer = matrix(estimationWindow, stateModel.getStateSize(), Double.NaN);
	}
	
	@Override
	public void update() throws EstimationException {
		final Vector output = observationModel.getObservedOutput();		
		
		Vector currentEstimate = vector(output.rows(), new VectorFunction() {			
			@Override
			public double cell(int row) {
				return output.get(row) / observationModel.getOutputFunction(row).getFactor();
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
}
