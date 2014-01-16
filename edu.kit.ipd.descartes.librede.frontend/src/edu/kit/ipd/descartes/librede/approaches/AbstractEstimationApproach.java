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
package edu.kit.ipd.descartes.librede.approaches;

import edu.kit.ipd.descartes.librede.estimation.algorithm.IEstimationAlgorithm;
import edu.kit.ipd.descartes.librede.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.MatrixBuilder;
import edu.kit.ipd.descartes.linalg.Vector;

public abstract class AbstractEstimationApproach implements IEstimationApproach {
	
	private boolean iterative;
	private WorkloadDescription workload;
	private IEstimationAlgorithm<?, ?> estimator;
	private RepositoryCursor cursor;
	
	@Override
	public void initialize(WorkloadDescription workload,
			RepositoryCursor cursor, int estimationWindow, boolean iterative)
			throws InitializationException {
		this.workload = workload;
		this.cursor = cursor;
		this.iterative = iterative;
	}
	
	protected void setEstimationAlgorithm(IEstimationAlgorithm<?, ?> estimator) {
		this.estimator = estimator;
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