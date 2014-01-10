package edu.kit.ipd.descartes.librede.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.librede.estimation.models.observation.functions.ResponseTimeEquation;
import edu.kit.ipd.descartes.librede.estimation.repository.Query;
import edu.kit.ipd.descartes.librede.estimation.repository.QueryBuilder;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.testutils.Differentiation;
import edu.kit.ipd.descartes.librede.estimation.testutils.ObservationDataGenerator;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;

public class ResponseTimeEquationTest {
	
	private final static int SERVICE_IDX = 2;
	
	private ObservationDataGenerator generator;
	private ResponseTimeEquation law;
	private RepositoryCursor cursor;
	private Service service;
	private Vector state;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		WorkloadDescription workload = generator.getWorkloadDescription();
		cursor = generator.getRepository().getCursor(0, 1);
		cursor.setEndTime(1);
		
		service = workload.getServices().get(SERVICE_IDX);
		
		law = new ResponseTimeEquation(workload, cursor, service, workload.getResources());
		state = generator.getDemands();
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetObservedOutput() {
		Query<Scalar> resp = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(service).average().using(cursor);
		assertThat(law.getObservedOutput()).isEqualTo(resp.execute().getValue(), offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {
		Query<Scalar> resp = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(service).average().using(cursor);
		assertThat(law.getCalculatedOutput(state)).isEqualTo(resp.execute().getValue(), offset(1e-9));
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
