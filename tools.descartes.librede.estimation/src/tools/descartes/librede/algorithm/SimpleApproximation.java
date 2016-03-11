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
import static tools.descartes.librede.linalg.LinAlg.min;
import static tools.descartes.librede.linalg.LinAlg.nanmean;
import static tools.descartes.librede.linalg.LinAlg.nansum;
import static tools.descartes.librede.linalg.LinAlg.vector;

import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.models.EstimationProblem;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;

/**
 * The simple approximation algorithm estimates the resource demands by
 * approximating it with the aggregated result of the direct output of the
 * observation model.
 * 
 * This estimation algorithm only works on observation models consisting of
 * output functions of the type {@link IDirectOutputFunction}.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
@Component(displayName = "Simple Approximation")
public class SimpleApproximation extends AbstractEstimationAlgorithm {

	/*
	 * The aggregation to be used on the output values in the buffer.
	 */
	private Aggregation aggregation;

	/*
	 * The output of the observation model of the last n intervals. (N x M
	 * matrix, with N == estimationWindow, M == size of state model)
	 */
	private Matrix buffer;

	/**
	 * Creates a new instance.
	 * 
	 * @param aggregation
	 *            The aggregation function to be used to obtain the estimates.
	 */
	public SimpleApproximation(Aggregation aggregation) {
		this.aggregation = aggregation;
	}

	@Override
	public void initialize(EstimationProblem problem, 
			IRepositoryCursor cursor,
			int estimationWindow)
			throws InitializationException {
		super.initialize(problem, cursor, estimationWindow);
		
		// Fill with NaN. NaN values are ignored by the aggregation function.
		this.buffer = matrix(estimationWindow, problem.getStateModel().getStateSize(),
				Double.NaN);
	}

	@Override
	public void update() throws EstimationException {

		getStateModel().step(null);
		
		final Vector output = getObservationModel().getObservedOutput();

		// update the buffer with current output of the observation model
		Vector currentEstimate = vector(output.rows(), new VectorFunction() {
			@Override
			public double cell(int row) {
				double factor = getCastedObservationModel().getOutputFunction(row)
						.getFactor();
				if (factor != 0) {
					return  output.get(row) / factor;
				}
				// If the factor is equals to 0.0, this usually
				// means we did not observe any requests in this interval
				// therefore ignore this estimate in the following so that
				// the aggregation is not distorted towards zero.
				return Double.NaN;
			}
		});
		buffer = buffer.circshift(1).setRow(0, currentEstimate);
	}

	@Override
	public Vector estimate() throws EstimationException {
		Vector estimate = empty();
		switch (aggregation) {
		case AVERAGE:
			estimate = nanmean(buffer);
			break;
		case MAXIMUM:
			estimate = max(buffer);
			break;
		case MINIMUM:
			estimate = min(buffer);
			break;
		case SUM:
			estimate = nansum(buffer);
			break;
		case NONE:
			estimate = buffer.row(0);
			break;
		default:
			throw new IllegalStateException();	
		}

		for (int i = 0; i < estimate.rows(); i++) {
			double value = estimate.get(i);
			if (value != value) {
				// NaN --> no observations therefore assume zero as resource demand.
				estimate = estimate.set(i, 0.0);
			}
		}
		return estimate;
	}

	/**
	 * Helper function to cast the observation model to the expected type.
	 */
	@SuppressWarnings("unchecked")
	private IObservationModel<Vector> getCastedObservationModel() {
		return (IObservationModel<Vector>) getObservationModel();
	}
}
