package edu.kit.ipd.descartes.redeem.ipopt.java;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.VectorObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.ResponseTimeEquation;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.ILinearStateConstraint;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.UtilizationConstraint;
import edu.kit.ipd.descartes.redeem.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.redeem.estimation.testutils.ObservationDataGenerator;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class MenasceOptimizationTest {

	private static final int ITERATIONS = 100;

	private VectorObservationModel<IOutputFunction> observationModel;
	private ConstantStateModel<ILinearStateConstraint> stateModel;

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testOneServiceOneResource() throws Exception {
		final ObservationDataGenerator generator = new ObservationDataGenerator(42, 1, 1);

		Vector demands = vector(0.05);
		generator.setDemands(demands);
		generator.setUpperUtilizationBound(0.9);
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		RepositoryCursor cursor = generator.getRepository().getCursor(0, 1);
		cursor.setEndTime(ITERATIONS);

		Vector initialEstimate = vector(0.01);
		stateModel = new ConstantStateModel<>(1, initialEstimate);
		
		observationModel = new VectorObservationModel<>();
		observationModel.addOutputFunction(new ResponseTimeEquation(workload, cursor, workload.getServices().get(0), workload.getResources()));
		
		stateModel.addConstraint(new UtilizationConstraint(workload, cursor, workload.getResources().get(0)));

		RecursiveOptimization optim = new RecursiveOptimization();
		optim.initialize(stateModel, observationModel, 10);

		long start = System.nanoTime();

		while(true) {
			generator.nextObservation();
			
			if (!cursor.next()) {
				break;
			}
			
			optim.update();

			Vector estimates = optim.estimate();

			assertThat(estimates).isEqualTo(demands, offset(0.001));
			
//			System.out.println(i + ": " + estimates);
		}

		System.out.println("Duration: " + (System.nanoTime() - start) / 1000000);

		optim.destroy();

	}
	
	@Test
	public void testFiveServicesOneResource() throws Exception {
		final ObservationDataGenerator generator = new ObservationDataGenerator(42, 5, 1);

		Vector demands = vector(0.03, 0.04, 0.05, 0.06, 0.07);
		generator.setDemands(demands);
		generator.setUpperUtilizationBound(0.9);
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		RepositoryCursor cursor = generator.getRepository().getCursor(0, 1);
		cursor.setEndTime(ITERATIONS);

		Vector initialEstimate = vector(0.01, 0.01, 0.01, 0.01, 0.01);
		stateModel = new ConstantStateModel<>(5, initialEstimate);

		observationModel = new VectorObservationModel<>();
		for (Service service : workload.getServices()) {		
			observationModel.addOutputFunction(new ResponseTimeEquation(workload, cursor, service,
					workload.getResources()));
		}
		
		stateModel.addConstraint(new UtilizationConstraint(workload, cursor, workload.getResources().get(0)));

		RecursiveOptimization optim = new RecursiveOptimization();
		optim.initialize(stateModel, observationModel, 10);

		long start = System.nanoTime();

		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			cursor.next();
			
			optim.update();

			Vector estimates = optim.estimate();
			
			assertThat(estimates).isEqualTo(demands, offset(0.001));
			
//			System.out.println(i + ": " + estimates);
		}

		System.out.println("Duration: " + (System.nanoTime() - start) / 1000000);

		optim.destroy();

	}
}
