package edu.kit.ipd.descartes.librede.approaches;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import edu.kit.ipd.descartes.librede.estimation.exceptions.EstimationException;
import edu.kit.ipd.descartes.librede.estimation.exceptions.InitializationException;
import edu.kit.ipd.descartes.librede.estimation.models.observation.ScalarObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ILinearOutputFunction;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ResponseTimeEquation;
import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.UtilizationLaw;
import edu.kit.ipd.descartes.librede.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.ILinearStateConstraint;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.StateBoundsConstraint;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.Unconstrained;
import edu.kit.ipd.descartes.librede.estimation.models.state.constraints.UtilizationConstraint;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.librede.ipopt.java.RecursiveOptimization;
import edu.kit.ipd.descartes.librede.nnls.LeastSquaresRegression;
import edu.kit.ipd.descartes.linalg.MatrixBuilder;
import edu.kit.ipd.descartes.linalg.Vector;

public class ZimbraApproach implements IEstimationApproach {
	
	public static final String NAME = "Zimbra";
	
	private Vector curCpuEstimates;
	private LeastSquaresRegression cpuEstimator;
	private RecursiveOptimization systemEstimator;
	private RepositoryCursor cursor;
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
			RepositoryCursor cursor, int estimationWindow, boolean iterative)
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

}
