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
package tools.descartes.librede.repository.handlers;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.ones;
import static tools.descartes.librede.linalg.LinAlg.vector;

import org.junit.Test;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.MemoryObservationRepository;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.repository.WorkloadBuilder;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class RequestRateAggregationHandlerTest extends LibredeTest {

	@Test
	public void test() {
		WorkloadDescription workload = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
		Service wc0 = WorkloadBuilder.newService("WC0");
		workload.getServices().add(wc0);
		MemoryObservationRepository repo = new MemoryObservationRepository(workload);
		// Values chosen so that if specified range is not considered correctly the resulting throughput is different
		// between 2 and 4: throughput 2 per sec. Otherwise throughput is higher
		TimeSeries departures = new TimeSeries(vector(1, 1.25, 1.5, 1.75, 2, 2.5, 3, 3.75, 4, 4.25, 4.5, 4.75), ones(12));
		repo.insert(StandardMetrics.DEPARTURES, RequestCount.REQUESTS, wc0, departures);
		
		RequestRateAggregationHandler handler = new RequestRateAggregationHandler(StandardMetrics.DEPARTURES);
		double result = handler.aggregate(repo, StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND, wc0, Aggregation.AVERAGE, 
				UnitsFactory.eINSTANCE.createQuantity(2, Time.SECONDS), UnitsFactory.eINSTANCE.createQuantity(4, Time.SECONDS));
		assertThat(result).isEqualTo(2.0, offset(1e-6));
	}
	
}
