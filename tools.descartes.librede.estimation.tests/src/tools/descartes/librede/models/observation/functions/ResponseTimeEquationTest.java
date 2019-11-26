/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
import tools.descartes.librede.models.observation.equations.ResponseTimeEquation;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.testutils.Differentiation;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.units.Time;

@RunWith(Parameterized.class)
public class ResponseTimeEquationTest extends LibredeTest {
	
	private final static int STATE_IDX = 0;
	
	private ObservationDataGenerator generator;
	private ResponseTimeEquation law;
	private IRepositoryCursor cursor;
	private Service service;
	private State state;
	private boolean useObservedUtilization;
	private int numServers;
	
	public ResponseTimeEquationTest(boolean useObservedUtilization, int numServers) {
		this.useObservedUtilization = useObservedUtilization;
		this.numServers = numServers;
	}
	
	@Parameters
	public static Collection<Object[]> testData() {
		return Arrays.asList(new Object[][] {{ true, 1 }, { false, 1 }, { true, 2 }, { false, 2 }});
	}

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 5, 4, numServers);
		generator.setRandomDemands();
		
		cursor = generator.getCursor();
		
		service = generator.getStateModel().getResourceDemand(STATE_IDX).getService();
		
		law = new ResponseTimeEquation(generator.getStateModel(), cursor, service, useObservedUtilization, 0);
		state = generator.getDemands();
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetCalculatedOutput() {
		Query<Scalar, Time> resp = QueryBuilder.select(StandardMetrics.RESPONSE_TIME).in(Time.SECONDS).forService(service).average().using(cursor);
		assertThat(law.getValue(state).getValue()).isEqualTo(resp.execute().getValue(), offset(1e-9));
	}

	@Test
	public void testDifferentiation() {
		Vector diff1 = Differentiation.diff1(law, state);
		Matrix diff2 = Differentiation.diff2(law, state);
		
		DerivativeStructure s = law.getValue(new State(state.getStateModel(), state.getVector(), 2));
		assertThat(DifferentiationUtils.getFirstDerivatives(s)).isEqualTo(diff1, offset(1e-4));
		assertThat(DifferentiationUtils.getSecondDerivatives(s)).isEqualTo(diff2, offset(1e-4));
	}
}
