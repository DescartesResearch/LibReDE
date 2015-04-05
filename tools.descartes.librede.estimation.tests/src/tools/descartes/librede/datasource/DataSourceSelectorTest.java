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
package tools.descartes.librede.datasource;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.junit.Test;

import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class DataSourceSelectorTest extends LibredeTest {

	@Test
	public void testSingleDs() throws IOException {
		Quantity<Time> t1 = UnitsFactory.eINSTANCE.createQuantity(1.0, Time.SECONDS);
		
		try (DataSourceSelector selector = new DataSourceSelector()) {
			IDataSource ds1 = mock(IDataSource.class);
			
			selector.add(ds1);
			selector.dataAvailable(ds1, new TraceEvent(new TraceKey(null, null, t1, null), null, t1));
			
			assertThat(selector.getLatestObservationTime()).isNotNull();
			assertThat(selector.getLatestObservationTime().getValue(Time.SECONDS)).isEqualTo(t1.getValue(Time.SECONDS));
		}
	}
	
	@Test
	public void testMultipleDs() throws IOException {
		Quantity<Time> t1 = UnitsFactory.eINSTANCE.createQuantity(2.0, Time.SECONDS);
		Quantity<Time> t2 = UnitsFactory.eINSTANCE.createQuantity(1.0, Time.SECONDS);
		Quantity<Time> t3 = UnitsFactory.eINSTANCE.createQuantity(3.0, Time.SECONDS);
		
		try (DataSourceSelector selector = new DataSourceSelector()) {
			IDataSource ds1 = mock(IDataSource.class);
			IDataSource ds2 = mock(IDataSource.class);
			
			selector.add(ds1);
			selector.add(ds2);
			// ds1 -> t1
			// ds2 -> t2
			selector.dataAvailable(ds1, new TraceEvent(new TraceKey(StandardMetrics.ARRIVALS, null, t2, null), null, t1));
			selector.dataAvailable(ds2, new TraceEvent(new TraceKey(StandardMetrics.RESPONSE_TIME, null, t2, null), null, t2));
			
			assertThat(selector.getLatestObservationTime()).isNotNull();
			assertThat(selector.getLatestObservationTime().getValue(Time.SECONDS)).isEqualTo(t2.getValue(Time.SECONDS));
			
			// ds1 -> t3
			// ds1 -> t3
			selector.dataAvailable(ds1, new TraceEvent(new TraceKey(StandardMetrics.ARRIVALS, null, t2, null), null, t3));
			selector.dataAvailable(ds2, new TraceEvent(new TraceKey(StandardMetrics.RESPONSE_TIME, null, t2, null), null, t3));
			assertThat(selector.getLatestObservationTime()).isNotNull();
			assertThat(selector.getLatestObservationTime().getValue(Time.SECONDS)).isEqualTo(t3.getValue(Time.SECONDS));		
		}
		
	}
	

}
