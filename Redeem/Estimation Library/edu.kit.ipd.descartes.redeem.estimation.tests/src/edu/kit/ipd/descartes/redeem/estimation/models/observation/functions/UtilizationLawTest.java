package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.repository.Metric;
import edu.kit.ipd.descartes.redeem.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.redeem.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.redeem.estimation.testutils.Differentiation;
import edu.kit.ipd.descartes.redeem.estimation.testutils.ObservationDataGenerator;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class UtilizationLawTest {
		
	private final static int RESOURCE_IDX = 2;
	
	private ObservationDataGenerator generator;
	private UtilizationLaw law;
	private RepositoryCursor cursor;
	private Vector state;
	private Resource resource;

	@Before
	public void setUp() throws Exception {		
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		cursor = generator.getRepository().getCursor(1);
		
		resource = workload.getResources().get(RESOURCE_IDX);
		law = new UtilizationLaw(workload, cursor, resource);
		state = generator.getDemands();		
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetIndependentVariables() {
		Vector x = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average().using(cursor).execute();
		Vector varVector = law.getIndependentVariables();		
		Vector expectedVarVector = zeros(state.rows()).set(generator.getWorkloadDescription().getState().getRange(resource), x);
		
		assertThat(varVector).isEqualTo(expectedVarVector, offset(1e-9));
	}

	@Test
	public void testGetObservedOutput() {
		double util = QueryBuilder.select(Metric.UTILIZATION).forResource(resource).average().using(cursor).execute().getValue();
		assertThat(law.getObservedOutput()).isEqualTo(util, offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {
		double util = QueryBuilder.select(Metric.UTILIZATION).forResource(resource).average().using(cursor).execute().getValue();
		assertThat(law.getCalculatedOutput(state)).isEqualTo(util, offset(1e-9));
	}

	@Test
	public void testGetFirstDerivatives() {
		Vector diff = Differentiation.diff1(law, state);
		assertThat(law.getFirstDerivatives(state)).isEqualTo(diff, offset(1e-4));
	}

	@Test
	public void testGetSecondDerivatives() {
		Matrix diff = Differentiation.diff2(law, state);
		assertThat(law.getSecondDerivatives(state)).isEqualTo(diff, offset(1e-4));
	}

}
