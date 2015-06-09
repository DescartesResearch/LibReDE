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
package tools.descartes.librede.repository;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

public class CachingRepositoryCursorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetIntervalStart() {
		Quantity<Time> time = UnitsFactory.eINSTANCE.createQuantity(3, Time.SECONDS);
		IRepositoryCursor mockedCursor = mock(IRepositoryCursor.class);
		when(mockedCursor.getIntervalStart(0)).thenReturn(time);
		
		CachingRepositoryCursor cursor = new CachingRepositoryCursor(mockedCursor, 5);
		assertThat(cursor.getIntervalStart(0)).isEqualTo(time);
		
		verify(mockedCursor).getIntervalStart(0);
	}
	
	@Test
	public void testGetIntervalEnd() {
		Quantity<Time> time = UnitsFactory.eINSTANCE.createQuantity(3, Time.SECONDS);
		IRepositoryCursor mockedCursor = mock(IRepositoryCursor.class);
		when(mockedCursor.getIntervalEnd(0)).thenReturn(time);
		
		CachingRepositoryCursor cursor = new CachingRepositoryCursor(mockedCursor, 5);
		assertThat(cursor.getIntervalEnd(0)).isEqualTo(time);
		
		verify(mockedCursor).getIntervalEnd(0);
	}
	
	@Test
	public void testGetAggregatedValue() {
		Service service = ConfigurationFactory.eINSTANCE.createService();
		IRepositoryCursor mockedCursor = mock(IRepositoryCursor.class);
		when(mockedCursor.next()).thenReturn(true);
		when(mockedCursor.getLastInterval()).thenReturn(0);
		when(mockedCursor.getAggregatedValue(0, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).thenReturn(5.0);
		
		CachingRepositoryCursor cursor = new CachingRepositoryCursor(mockedCursor, 5);
		cursor.next();	
		
		assertThat(cursor.getAggregatedValue(0, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).isEqualTo(5.0);
		
		assertThat(cursor.getAggregatedValue(0, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).isEqualTo(5.0);
		
		
		// Check that the underlying cursor is only called once, i.e., the cache is used the other times.
		verify(mockedCursor, times(1)).getAggregatedValue(0, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE);
		
		// Add an additional entry
		when(mockedCursor.getLastInterval()).thenReturn(1);
		when(mockedCursor.getAggregatedValue(1, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).thenReturn(6.0);
		
		cursor.next();
		
		assertThat(cursor.getAggregatedValue(0, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).isEqualTo(5.0);
		assertThat(cursor.getAggregatedValue(1, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).isEqualTo(6.0);
	}
	
	@Test
	public void testGetAggregatedValueMultipleIntervals() {
		Service service = ConfigurationFactory.eINSTANCE.createService();
		IRepositoryCursor mockedCursor = mock(IRepositoryCursor.class);
		when(mockedCursor.next()).thenReturn(true);
		when(mockedCursor.getLastInterval()).thenReturn(2);
		when(mockedCursor.getAggregatedValue(0, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).thenReturn(1.0);
		when(mockedCursor.getAggregatedValue(1, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).thenReturn(2.0);
		when(mockedCursor.getAggregatedValue(2, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).thenReturn(3.0);
		
		CachingRepositoryCursor cursor = new CachingRepositoryCursor(mockedCursor, 5);
		cursor.next();	
		cursor.next();
		cursor.next();
		
		assertThat(cursor.getAggregatedValue(0, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).isEqualTo(1.0);		
		assertThat(cursor.getAggregatedValue(1, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).isEqualTo(2.0);
		assertThat(cursor.getAggregatedValue(2, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).isEqualTo(3.0);
	}

}
