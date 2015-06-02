package tools.descartes.librede.algorithm;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.junit.Assert.*;
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.VectorObservationModel;
import tools.descartes.librede.models.observation.functions.IDirectOutputFunction;
import tools.descartes.librede.models.observation.functions.ResponseTimeApproximation;
import tools.descartes.librede.models.observation.functions.ServiceDemandLaw;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class SimpleApproximationTest extends LibredeTest {
	
	private static final int ITERATIONS = 100;
	
	private Vector demands = vector(0.25, 0.1, 0.3, 0.4, 0.0);
	private IRepositoryCursor cursor;
	private SimpleApproximation algorithm;
	private ObservationDataGenerator generator;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 1);
		generator.setDemands(demands);
		
		cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0.0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1.0, Time.SECONDS));
		
			
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSDL() throws EstimationException, InitializationException {
		IStateModel<Unconstrained> stateModel = generator.getStateModel();
		VectorObservationModel<IDirectOutputFunction> model = new VectorObservationModel<>();
		for (Service service : generator.getStateModel().getAllServices()) {
			model.addOutputFunction(new ServiceDemandLaw(stateModel, cursor, stateModel.getResources().get(0), service));
		}
		
		algorithm = new SimpleApproximation(Aggregation.AVERAGE);
		algorithm.initialize(stateModel, model, 60);
		
		long start = System.nanoTime();

		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			cursor.next();
			
			algorithm.update();

			Vector estimates = algorithm.estimate();
			
			assertThat(estimates).isEqualTo(demands, offset(0.001));
		}

		System.out.println("Duration: " + (System.nanoTime() - start) / 1000000);

		algorithm.destroy();
	}
	
	@Test
	public void testRespApprox() throws EstimationException, InitializationException {
		IStateModel<Unconstrained> stateModel = generator.getStateModel();
		VectorObservationModel<IDirectOutputFunction> model = new VectorObservationModel<>();
		for (Service service : generator.getStateModel().getAllServices()) {
			model.addOutputFunction(new ResponseTimeApproximation(stateModel, cursor, stateModel.getResources().get(0), service, Aggregation.AVERAGE));
		}
		
		algorithm = new SimpleApproximation(Aggregation.AVERAGE);
		algorithm.initialize(stateModel, model, 60);		
		
		long start = System.nanoTime();

		for (int i = 0; i < ITERATIONS; i++) {
			generator.nextObservation();
			cursor.next();
			
			algorithm.update();

			Vector estimates = algorithm.estimate();
			
			assertThat(estimates.get(4)).isZero();
		}

		System.out.println("Duration: " + (System.nanoTime() - start) / 1000000);

		algorithm.destroy();
	}

}
