package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

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
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class ServiceDemandLawTest {
	
	private final static int SERVICE_IDX = 2;
	private final static int RESOURCE_IDX = 1;
	
	private ObservationDataGenerator generator;
	private ServiceDemandLaw law;
	private RepositoryCursor cursor;
	private Vector state;
	
	private Resource resource;
	private Service service;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		cursor = generator.getRepository().getCursor(1);
		
		resource = workload.getResources().get(RESOURCE_IDX);
		service = workload.getServices().get(SERVICE_IDX);
		
		law = new ServiceDemandLaw(workload, cursor, resource, service);
		state = generator.getDemands();	
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetObservedOutput() {
		Vector x = QueryBuilder.select(Metric.THROUGHPUT).forAllServices().average().using(cursor).execute();
		Vector r = QueryBuilder.select(Metric.AVERAGE_RESPONSE_TIME).forAllServices().average().using(cursor).execute();
		double util = QueryBuilder.select(Metric.UTILIZATION).forResource(resource).average().using(cursor).execute().getValue();
		
		assertThat(law.getObservedOutput()).isEqualTo(x.get(SERVICE_IDX) * r.get(SERVICE_IDX) * util / x.dot(r), offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {
		double x = QueryBuilder.select(Metric.THROUGHPUT).forService(service).average().using(cursor).execute().getValue();
		double expected = x * state.get(generator.getWorkloadDescription().getState().getIndex(resource, service));
		
		assertThat(law.getCalculatedOutput(state)).isEqualTo(expected, offset(1e-9));
	}
	
	@Test
	public void testGetFactor() {
		double x = QueryBuilder.select(Metric.THROUGHPUT).forService(service).average().using(cursor).execute().getValue();
		assertThat(law.getFactor()).isEqualTo(x, offset(1e-9));
	}

	@Test
	public void testGetFirstDerivatives() {
		Vector diff = Differentiation.diff1(law, state);
		assertThat(law.getFirstDerivatives(state)).isEqualTo(diff, offset(1e-4));
	}

	@Test
	public void testGetSecondDerivatives() {
		Matrix diff = Differentiation.diff2(law, state);
		assertThat(law.getSecondDerivatives(state)).isEqualTo(diff, offset(1e0));
	}

}
