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
package tools.descartes.librede.approaches;

import java.util.List;

import tools.descartes.librede.algorithm.IEstimationAlgorithm;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.workload.WorkloadDescription;

public abstract class AbstractEstimationApproach implements IEstimationApproach {
	
	private boolean iterative;
	private WorkloadDescription workload;
	private IEstimationAlgorithm<?, ?> estimator;
	private IRepositoryCursor cursor;
	
	@Override
	public void initialize(WorkloadDescription workload,
			IRepositoryCursor cursor, int estimationWindow, boolean iterative)
			throws InitializationException {
		this.workload = workload;
		this.cursor = cursor;
		this.iterative = iterative;
	}
	
	protected void setEstimationAlgorithm(IEstimationAlgorithm<?, ?> estimator) {
		this.estimator = estimator;
	}
	
	@Override
	public boolean checkPreconditions(List<String> messages) {
		boolean result = true;
		IObservationModel<?, ?> observationModel  = this.estimator.getObservationModel();
		for (IOutputFunction func : observationModel) {
			result = result && func.isApplicable(messages);
		}
		IStateModel<?> stateModel = this.estimator.getStateModel();
		for (IStateConstraint constr : stateModel.getConstraints()) {
			result = result && constr.isApplicable(messages);
		}
		return result;
	}
	
	@Override
	public TimeSeries execute() throws EstimationException {
		try {
			MatrixBuilder estimateBuilder = new MatrixBuilder(workload.getState().getStateSize());
			MatrixBuilder timestampBuilder = new MatrixBuilder(1);
			if (iterative) {
				while(cursor.next()) {
					estimator.update();

					timestampBuilder.addRow(cursor.getCurrentIntervalEnd());
					estimateBuilder.addRow(estimator.estimate());
				}
			} else {
				while(cursor.next()) {
					estimator.update();
				}

				timestampBuilder.addRow(cursor.getCurrentIntervalEnd());
				estimateBuilder.addRow(estimator.estimate());
			}
			return new TimeSeries((Vector)timestampBuilder.toMatrix(), estimateBuilder.toMatrix());
		} finally {
			if (estimator != null) {
				estimator.destroy();
			}
		}
	}

}
