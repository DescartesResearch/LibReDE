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
import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.testutil.MatrixAssert.assertThat;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.diff.DifferentiationUtils;
import tools.descartes.librede.models.observation.equations.ResponseTimeApproximationEquation;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.testutils.Differentiation;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class ResponseTimeApproximationNaNTest extends LibredeTest {
	
	private final static int STATE_IDX = 1;
	
	private ObservationDataGenerator generator;
	private ResponseTimeApproximationEquation law;
	private State state;
	
	private Resource resource;
	private Service service;
	private IRepositoryCursor cursor;

	@Before
	public void setUp() throws Exception {
		generator = new ObservationDataGenerator(42, 2, 1);
		generator.setDemands(vector(0.25, 0));

		cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		ResourceDemand demand = generator.getStateModel().getResourceDemand(STATE_IDX);
		resource = demand.getResource();
		service = demand.getService();
		
		law = new ResponseTimeApproximationEquation(generator.getStateModel(), cursor, resource, service, Aggregation.AVERAGE);
		state = generator.getDemands();
		
		generator.nextObservation();
		cursor.next();
	}
	
	@Test
	public void testGetFactor() {
		assertThat(law.getConstantValue()).isZero();
	}

	@Test
	public void testGetCalculatedOutput() {
		assertThat(law.getValue(state).getValue()).isZero();
	}
	
	@Test
	public void testGetDerivatives() {
		Vector diff1 = Differentiation.diff1(law, state);
		Matrix diff2 = Differentiation.diff2(law, state);
		
		DerivativeStructure s = law.getValue(new State(state.getStateModel(), state.getVector(), 2));
		assertThat(DifferentiationUtils.getFirstDerivatives(s)).isEqualTo(diff1, offset(1e-4));
		assertThat(DifferentiationUtils.getSecondDerivatives(s)).isEqualTo(diff2, offset(1e-4));
	}
}
