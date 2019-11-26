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
import static tools.descartes.librede.linalg.LinAlg.indices;
import static tools.descartes.librede.linalg.LinAlg.zeros;
import static tools.descartes.librede.linalg.testutil.MatrixAssert.assertThat;
import static tools.descartes.librede.linalg.testutil.VectorAssert.assertThat;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.linalg.Indices;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.diff.DifferentiationUtils;
import tools.descartes.librede.models.observation.equations.UtilizationLawEquation;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.testutils.Differentiation;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class UtilizationLawTest extends LibredeTest {
		
	private final static int RESOURCE_IDX = 2;
	
	private ObservationDataGenerator generator;
	private UtilizationLawEquation law;
	private IRepositoryCursor cursor;
	private State state;
	private Resource resource;

	@Before
	public void setUp() throws Exception {		
		generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();

		cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		
		resource = generator.getStateModel().getResources().get(RESOURCE_IDX);
		law = new UtilizationLawEquation(generator.getStateModel(), cursor, resource, 0);
		state = generator.getDemands();		
		
		generator.nextObservation();
		cursor.next();
	}

	@Test
	public void testGetIndependentVariables() {
		Vector x = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(cursor).execute();
		Vector varVector = law.getFactors();
		Indices idx = indices(generator.getStateModel().getUserServices().size(), new VectorFunction() {			
			@Override
			public double cell(int row) {
				return generator.getStateModel().getStateVariableIndex(resource, generator.getStateModel().getUserServices().get(row));
			}
		});
		Vector expectedVarVector = zeros(state.getStateSize()).set(idx, x);
		
		assertThat(varVector).isEqualTo(expectedVarVector, offset(1e-9));
	}


	@Test
	public void testGetCalculatedOutput() {
		double util = QueryBuilder.select(StandardMetrics.UTILIZATION).in(Ratio.NONE).forResource(resource).average().using(cursor).execute().getValue();
		assertThat(law.getValue(state).getValue()).isEqualTo(util, offset(1e-9));
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
