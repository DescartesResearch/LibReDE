package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.empty;
import static edu.kit.ipd.descartes.linalg.LinAlg.scalar;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Vector;

public class TimeSeriesTest {
	
	private Vector time1 = vector(1,2,3);
	private Vector data1 = vector(0.1,0.2,0.3);
	private Vector time2 = vector(1,5,2,4,3);
	private Vector data2 = vector(0.1,0.5,0.2,0.4,0.3);
	
	private TimeSeries ts1;
	private TimeSeries ts2;
	
	@Before
	public void setup() {
		ts1 = new TimeSeries(time1, data1);
		ts2 = new TimeSeries(time2, data2);
	}

	@Test
	public void testCreate() {
		assertThat(ts1.getTime()).isEqualTo(time1, offset(1e-9));
		assertThat(ts1.getData()).isEqualTo(data1, offset(1e-9));
	}
	
	@Test
	public void testAddSample() {
		TimeSeries tsAdd = ts1.addSample(4, 0.4);
		assertThat(tsAdd).isNotNull();
		assertThat(ts1.getTime()).isEqualTo(time1, offset(1e-9));
		assertThat(ts1.getData()).isEqualTo(data1, offset(1e-9));
		assertThat(tsAdd.getTime()).isEqualTo((Vector)vertcat(time1, scalar(4)), offset(1e-9));
		assertThat(tsAdd.getData()).isEqualTo((Vector)vertcat(data1, scalar(0.4)), offset(1e-9));
		
		tsAdd = ts1.addSample(2.5, 0.25);
		assertThat(tsAdd).isNotNull();
		assertThat(ts1.getTime()).isEqualTo(time1, offset(1e-9));
		assertThat(ts1.getData()).isEqualTo(data1, offset(1e-9));
		assertThat(tsAdd.getTime()).isEqualTo(vector(1,2,2.5,3), offset(1e-9));
		assertThat(tsAdd.getData()).isEqualTo(vector(0.1,0.2,0.25,0.3), offset(1e-9));
		
		tsAdd = ts1.addSample(0, 0);
		assertThat(tsAdd).isNotNull();
		assertThat(ts1.getTime()).isEqualTo(time1, offset(1e-9));
		assertThat(ts1.getData()).isEqualTo(data1, offset(1e-9));
		assertThat(tsAdd.getTime()).isEqualTo((Vector)vertcat(scalar(0), time1), offset(1e-9));
		assertThat(tsAdd.getData()).isEqualTo((Vector)vertcat(scalar(0), data1), offset(1e-9));
	}
	
	@Test
	public void testGetStartAndEndTime() {
		double start = ts1.getStartTime();
		double end = ts1.getEndTime();
		assertThat(start).isEqualTo(1.0);
		assertThat(end).isEqualTo(3.0);
	}
	
	@Test
	public void testGet() {
		double v1 = ts1.get(2);
		double v2 = ts2.get(2);
		assertThat(v1).isEqualTo(0.2);
		assertThat(v2).isEqualTo(0.2);
	}
	
	@Test
	public void testGetNotFound() {
		double value = ts1.get(2.5);
		assertThat(value).isEqualTo(0.25, offset(1e-5));
	}
	
	@Test
	public void testGetOutOfBounds() {
		double value = ts1.get(0.0);
		assertThat(value).isNaN();
		value = ts1.get(4.0);
		assertThat(value).isNaN();
	}
	
	@Test
	public void testTimeWeightedMean() {
		// Test in the middle of the range
		double r = ts2.timeWeightedMean(2, 4);
		assertThat(r).isEqualTo(0.35, offset(1e-5));
		
		// Test border cases
		r = ts2.timeWeightedMean(0, 1);
		assertThat(r).isEqualTo(0.1, offset(1e-9));
				
		r = ts2.timeWeightedMean(4, 6);
		assertThat(r).isEqualTo(0.5, offset(1e-9));

		// Test single timestamp		
		r = ts2.timeWeightedMean(3, 3);
		assertThat(r).isNaN();
		
		// Test out of range cases
		r = ts2.timeWeightedMean(0.25, 0.75);
		assertThat(r).isEqualTo(0.1, offset(1e-5));
		
		r = ts2.timeWeightedMean(0.25, 5.75);
		assertThat(r).isEqualTo((0.75 * 0.1 + 0.2 + 0.3 + 0.4 + 0.5) / 4.75, offset(1e-5));
		
		r = ts2.timeWeightedMean(10, 11);
		assertThat(r).isNaN();
		
		// Test inbetween two elements
		r = ts2.timeWeightedMean(2.25, 2.75);
		assertThat(r).isNaN();
				
		r = ts2.timeWeightedMean(1.25, 3.75);
		assertThat(r).isEqualTo((0.75 * 0.2 + 0.3 + 0.75 * 0.4) / 2.5, offset(1e-5));
				
		r = ts2.timeWeightedMean(2.25, 3);
		assertThat(r).isEqualTo(0.3, offset(1e-9));
				
		r = ts2.timeWeightedMean(2, 2.75);
		assertThat(r).isEqualTo(0.3, offset(1e-9));
	}
	
	@Test
	public void testMeanSingleElement() {
		TimeSeries ts3 = new TimeSeries(vector(1), vector(0.1));
		
		double r = ts3.timeWeightedMean(0, 1);
		assertThat(r).isEqualTo(0.1, offset(1e-9));
		
		r = ts3.mean(0, 1);
		assertThat(r).isEqualTo(0.1, offset(1e-9));
	}
	
	@Test
	public void testTimeWeightedMeanIncompleteData() {
		TimeSeries ts3 = new TimeSeries(vector(3, 4, 5, 6, 7, 8, 9, 10, 11), vector(0.3, 0.4, Double.NaN, Double.NaN, 0.7, Double.NaN, 0.9, 1.0, 1.1));
		
		double r = ts3.timeWeightedMean(1, 4);
		assertThat(r).isEqualTo((0.3 + 0.4) / 2, offset(1e-5));
		
		r = ts3.timeWeightedMean(3, 6);
		assertThat(r).isEqualTo(0.4, offset(1e-5));	
	}
	
	@Test
	public void testMean() {
		// Test in the middle of the range
		double r = ts2.mean(2, 4);
		assertThat(r).isEqualTo(0.35, offset(1e-5));
		
		// Test border cases
		r = ts2.mean(0, 1);
		assertThat(r).isEqualTo(0.1, offset(1e-9));
				
		r = ts2.mean(4, 6);
		assertThat(r).isEqualTo(0.5, offset(1e-9));

		// Test single timestamp		
		r = ts2.mean(3, 3);
		assertThat(r).isNaN();
		
		// Test out of range  cases
		r = ts2.mean(0.25, 0.75);
		assertThat(r).isNaN();
		
		r = ts2.mean(0.25, 5.75);
		assertThat(r).isEqualTo((0.1 + 0.2 + 0.3 + 0.4 + 0.5) / 5, offset(1e-5));
		
		r = ts2.mean(10, 11);
		assertThat(r).isNaN();
		
		// Test inbetween two elements
		r = ts2.mean(2.25, 2.75);
		assertThat(r).isNaN();
				
		r = ts2.mean(1.25, 3.75);
		assertThat(r).isEqualTo(0.25, offset(1e-5));
				
		r = ts2.mean(2.25, 3);
		assertThat(r).isEqualTo(0.3, offset(1e-9));
				
		r = ts2.mean(2, 2.75);
		assertThat(r).isNaN();
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testMeanIllegal() {
		ts1.mean(2, 1);
	}
	
	@Test
	public void testSubset() {
		// Test in the middle of the range
		TimeSeries r = ts2.subset(2, 4);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(vector(3, 4), offset(1e-9));
		
		// Test border cases
		r = ts2.subset(0, 1);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(vector(1), offset(1e-9));
		
		r = ts2.subset(4, 6);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(vector(5), offset(1e-9));
		
		// Test single timestamp		
		r = ts2.subset(3, 3);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(empty(), offset(1e-9));
		
		// Test out of range  cases
		r = ts2.subset(0.25, 0.75);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(empty(), offset(1e-9));
		
		r = ts2.subset(0.25, 5.75);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(ts2.getTime(), offset(1e-9));
		
		r = ts2.subset(10, 11);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(empty(), offset(1e-9));
		
		// Test inbetween two elements
		r = ts2.subset(2.25, 2.75);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(empty(), offset(1e-9));
		
		r = ts2.subset(1.25, 3.75);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(vector(2, 3), offset(1e-9));
		
		r = ts2.subset(2.25, 3);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(scalar(3), offset(1e-9));
		
		r = ts2.subset(2, 2.75);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(empty(), offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSubsetIllegal() {
		ts1.subset(2, 1);
	}
	
	@Test
	public void testEmptyTimeSeries() {
		TimeSeries tsempty = TimeSeries.EMPTY;
		assertThat(tsempty.getStartTime()).isNaN();
		assertThat(tsempty.getEndTime()).isNaN();
		assertThat(tsempty.getTime()).isEqualTo(empty());
		assertThat(tsempty.getData()).isEqualTo(empty());
	}

}
