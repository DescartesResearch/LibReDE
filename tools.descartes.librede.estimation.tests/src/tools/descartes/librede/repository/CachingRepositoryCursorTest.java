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
		
		verify(mockedCursor, times(1)).getAggregatedValue(0, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE);
		
		when(mockedCursor.getLastInterval()).thenReturn(1);
		when(mockedCursor.getAggregatedValue(1, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).thenReturn(6.0);
		
		cursor.next();
		
		assertThat(cursor.getAggregatedValue(0, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).isEqualTo(5.0);
		assertThat(cursor.getAggregatedValue(1, StandardMetrics.RESPONSE_TIME, Time.SECONDS, service, Aggregation.AVERAGE)).isEqualTo(6.0);

	}

}
