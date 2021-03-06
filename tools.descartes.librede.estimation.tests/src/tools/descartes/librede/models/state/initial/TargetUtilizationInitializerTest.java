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
package tools.descartes.librede.models.state.initial;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.range;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.testutils.ObservationDataGenerator;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

@RunWith(Parameterized.class)
public class TargetUtilizationInitializerTest extends LibredeTest {
	
	@Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
                 { 0.1, 0.2 }, { 0.4, 0.5 }, { 0.9, 1.0 }  
           });
    }
    
    private double lowerUtil;
    private double upperUtil;

	public TargetUtilizationInitializerTest(double lowerUtil, double upperUtil) {
		this.lowerUtil = lowerUtil;
		this.upperUtil = upperUtil;
	}

	@Test
	public void testInitializer() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 5, 4);
		generator.setRandomDemands();
		generator.setLowerUtilizationBound(lowerUtil);
		generator.setUpperUtilizationBound(upperUtil);
		
		IRepositoryCursor cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		generator.nextObservation();
		cursor.next();
		
		TargetUtilizationInitializer initializer = new TargetUtilizationInitializer(0.5, cursor);
		Vector initialDemands = initializer.getInitialValue(generator.getStateModel());
		for (int i = 1; i < initialDemands.rows(); i++) {
			assertThat(initialDemands.get(i)).isEqualTo(initialDemands.get(i - 1), offset(1e-9));
		}
		
		Vector throughput = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(cursor).execute();
		
		for (int i = 0; i < 4; i++) {
			double util = initialDemands.get(range(i * 5, (i + 1) * 5)).dot(throughput);
			assertThat(util).isEqualTo(0.5, offset(1e-9));
		}
	}
	
	@Test
	public void testInitializerWithZero() {
		ObservationDataGenerator generator = new ObservationDataGenerator(42, 2, 1);
		generator.setDemands(vector(0.25, 0.0));
		generator.setLowerUtilizationBound(lowerUtil);
		generator.setUpperUtilizationBound(upperUtil);
		
		IRepositoryCursor cursor = generator.getRepository().getCursor(UnitsFactory.eINSTANCE.createQuantity(0, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		generator.nextObservation();
		cursor.next();
		
		TargetUtilizationInitializer initializer = new TargetUtilizationInitializer(0.5, cursor);
		Vector initialDemands = initializer.getInitialValue(generator.getStateModel());
		for (int i = 1; i < initialDemands.rows(); i++) {
			assertThat(initialDemands.get(i)).isEqualTo(initialDemands.get(i - 1), offset(1e-9));
		}
		
		Vector throughput = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND).forServices(generator.getStateModel().getUserServices()).average().using(cursor).execute();
		double util = initialDemands.dot(throughput);
		assertThat(util).isEqualTo(0.5, offset(1e-9));
	}

}
