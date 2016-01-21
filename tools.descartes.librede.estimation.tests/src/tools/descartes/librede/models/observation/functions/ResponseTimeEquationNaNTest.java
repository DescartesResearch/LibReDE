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
package tools.descartes.librede.models.observation.functions;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.testutil.MatrixAssert.assertThat;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Scalar;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.diff.DifferentiationUtils;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.testutils.Differentiation;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

@RunWith(Parameterized.class)
public class ResponseTimeEquationNaNTest extends LibredeTest {

	private ObservationDataGenerator generator;
	private ResponseTimeEquation law1;
	private ResponseTimeEquation law2;
	private IRepositoryCursor cursor;
	private Service service1;
	private Service service2;
	private State state;
	private boolean useObservedUtilization;
	private int numServers;
	
	public ResponseTimeEquationNaNTest(boolean useObservedUtilization, int numServers) {
		this.useObservedUtilization = useObservedUtilization;
		this.numServers = numServers;
	}
	
	@Parameters
	public static Collection<Object[]> testData() {
		return Arrays.asList(new Object[][] {{ true, 1 }, { false, 1 }, { true, 2 }, { false, 2 }});
	}

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 2, 2, numServers);
		generator.setDemands(vector(0.25, 0.0, 0.0, 0.0));
		
		cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		service1 = generator.getWorkloadDescription().getServices().get(0);
		service2 = generator.getWorkloadDescription().getServices().get(1);
		
		law1 = new ResponseTimeEquation(generator.getStateModel(), cursor, service1, useObservedUtilization);
		law2 = new ResponseTimeEquation(generator.getStateModel(), cursor, service2, useObservedUtilization);
		state = generator.getDemands();
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetObservedOutput() {
		Query<Scalar, Time> resp = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forService(service1).average().using(cursor);
		assertThat(law1.getObservedOutput()).isEqualTo(resp.execute().getValue(), offset(1e-9));
		assertThat(law2.getObservedOutput()).isZero();
	}

	@Test
	public void testGetCalculatedOutput() {
		Query<Scalar, Time> resp = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forService(service1).average().using(cursor);
		assertThat(law1.getCalculatedOutput(state).getValue()).isEqualTo(resp.execute().getValue(), offset(1e-9));
		assertThat(law2.getCalculatedOutput(state).getValue()).isZero();
	}

	@Test
	public void testDifferentiation() {
		Vector diff1 = Differentiation.diff1(law1, state);
		Matrix diff2 = Differentiation.diff2(law1, state);
		
		DerivativeStructure s = law1.getCalculatedOutput(new State(state.getStateModel(), state.getVector(), 2)).getDerivativeStructure();
		assertThat(DifferentiationUtils.getFirstDerivatives(s)).isEqualTo(diff1, offset(1e-4));
		assertThat(DifferentiationUtils.getSecondDerivatives(s)).isEqualTo(diff2, offset(1e-4));
	}

}
