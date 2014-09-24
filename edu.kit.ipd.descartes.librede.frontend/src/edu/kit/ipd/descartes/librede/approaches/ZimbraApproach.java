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

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;

import java.util.List;

import net.descartesresearch.librede.configuration.Resource;
import net.descartesresearch.librede.configuration.Service;
import edu.kit.ipd.descartes.librede.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.ipopt.java.RecursiveOptimization;
import edu.kit.ipd.descartes.librede.models.observation.ScalarObservationModel;
import edu.kit.ipd.descartes.librede.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.librede.models.observation.functions.ILinearOutputFunction;
import edu.kit.ipd.descartes.librede.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.librede.models.observation.functions.ResponseTimeEquation;
import edu.kit.ipd.descartes.librede.models.observation.functions.UtilizationLaw;
import edu.kit.ipd.descartes.librede.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.librede.models.state.constraints.ILinearStateConstraint;
import edu.kit.ipd.descartes.librede.models.state.constraints.StateBoundsConstraint;
import edu.kit.ipd.descartes.librede.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.librede.models.state.constraints.UtilizationConstraint;
import edu.kit.ipd.descartes.librede.nnls.LeastSquaresRegression;
import edu.kit.ipd.descartes.librede.repository.IRepositoryCursor;
import edu.kit.ipd.descartes.librede.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.MatrixBuilder;
import edu.kit.ipd.descartes.linalg.Vector;

public class ZimbraApproach implements IEstimationApproach {
	
	public static final String NAME = "Zimbra";
	
	private Vector curCpuEstimates;
	private LeastSquaresRegression cpuEstimator;
	private RecursiveOptimization systemEstimator;
	private IRepositoryCursor cursor;
	private WorkloadDescription workload;
	private boolean iterative;
	
	private class DemandConstraint extends StateBoundsConstraint {

		public DemandConstraint(int stateVar) {
			super(stateVar, 0, 0);
		}
		
		@Override
		public double getLowerBound() {
			return curCpuEstimates.get(getStateVariable());
		}
		
		@Override
		public double getUpperBound() {
			return curCpuEstimates.get(getStateVariable());
		}
		
	}
	
	@Override
	public void initialize(WorkloadDescription workload,
			IRepositoryCursor cursor, int estimationWindow, boolean iterative)
			throws InitializationException {
		this.workload = workload;
		this.cursor = cursor;
		this.iterative = iterative;
		
		Resource cpu = workload.getResource("cpu");
		Resource hd = workload.getResource("hd");
		
		ConstantStateModel<Unconstrained> cpuSM = new ConstantStateModel<Unconstrained>(workload.getState().getStateSize(), zeros(workload.getState().getStateSize()));
		
		UtilizationLaw law = new UtilizationLaw(workload, cursor, cpu);		
		ScalarObservationModel<ILinearOutputFunction> cpuOM = new ScalarObservationModel<ILinearOutputFunction>(law);
		
		cpuEstimator = new LeastSquaresRegression();
		cpuEstimator.initialize(cpuSM, cpuOM, estimationWindow);
		
		ConstantStateModel<ILinearStateConstraint> systemSM = new ConstantStateModel<ILinearStateConstraint>(workload.getState().getStateSize(), zeros(workload.getState().getStateSize()));
		systemSM.addConstraint(new UtilizationConstraint(workload, cursor, hd));
		for (Service service : workload.getServices()) {
			int stateVar = workload.getState().getIndex(cpu, service);
			systemSM.addConstraint(new DemandConstraint(stateVar));
		}
		
		VectorObservationModel<IOutputFunction> systemOM = new VectorObservationModel<IOutputFunction>();
		for (Service service : workload.getServices()) {
			systemOM.addOutputFunction(new ResponseTimeEquation(workload, cursor, service, workload.getResources()));
		}
		
		systemEstimator = new RecursiveOptimization();
		systemEstimator.initialize(systemSM, systemOM, estimationWindow);
		
	}
	
	@Override
	public TimeSeries execute() throws EstimationException {
		try {
			MatrixBuilder estimateBuilder = new MatrixBuilder(workload.getState().getStateSize());
			MatrixBuilder timestampBuilder = new MatrixBuilder(1);
			if (iterative) {
				while(cursor.next()) {
					cpuEstimator.update();
					curCpuEstimates = cpuEstimator.estimate();
					
					systemEstimator.update();

					timestampBuilder.addRow(cursor.getCurrentIntervalEnd());
					estimateBuilder.addRow(systemEstimator.estimate());
				}
			} else {
				while(cursor.next()) {
					cpuEstimator.update();
					curCpuEstimates = cpuEstimator.estimate();
					
					systemEstimator.update();
				}

				timestampBuilder.addRow(cursor.getCurrentIntervalEnd());
				estimateBuilder.addRow(systemEstimator.estimate());
			}
			return new TimeSeries((Vector)timestampBuilder.toMatrix(), estimateBuilder.toMatrix());
		} finally {
			if (cpuEstimator != null) {
				cpuEstimator.destroy();
			}
			if (systemEstimator != null) {
				systemEstimator.destroy();
			}
		}
		

	}

	@Override
	public boolean checkPreconditions(List<String> messages) {
		// TODO Auto-generated method stub
		return false;
	}

}
