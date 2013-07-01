package edu.kit.ipd.descartes.redeem.estimation.models.observation.functions;

import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.testutils.Differentiation;
import edu.kit.ipd.descartes.redeem.estimation.testutils.Observation;
import edu.kit.ipd.descartes.redeem.estimation.testutils.ObservationDataGenerator;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;

public class ServiceDemandLawTest {
	
	private final static int SERVICE_IDX = 2;
	private final static int RESOURCE_IDX = 1;
	
	private ObservationDataGenerator generator;
	private ServiceDemandLaw law;
	private Observation current;
	private Vector state;
	
	private Resource resource;
	private Service service;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		
		resource = generator.getSystemModel().getResources().get(RESOURCE_IDX);
		service = generator.getSystemModel().getServices().get(SERVICE_IDX);
		
		law = new ServiceDemandLaw(generator.getSystemModel(), generator, resource, service);
		current = generator.nextObservation();
		state = generator.getDemands();
	}

	@Test
	public void testGetObservedOutput() {
		Vector x = current.getMeanThroughput();
		Vector r = current.getMeanResponseTime();
		
		assertThat(law.getObservedOutput()).isEqualTo(x.get(SERVICE_IDX) * r.get(SERVICE_IDX) * current.getMeanUtilization().get(RESOURCE_IDX) / x.dot(r), offset(1e-9));
	}

	@Test
	public void testGetCalculatedOutput() {		
		double expected = current.getMeanThroughput().get(SERVICE_IDX) * state.get(generator.getSystemModel().getState().getIndex(resource, service));
		
		assertThat(law.getCalculatedOutput(state)).isEqualTo(expected, offset(1e-9));
	}
	
	@Test
	public void testGetFactor() {
		assertThat(law.getFactor()).isEqualTo(current.getMeanThroughput().get(SERVICE_IDX), offset(1e-9));
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
