package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.scalar;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.junit.Assert.*;

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
	public void testSubset() {
		TimeSeries r = ts2.subset(2, 4);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(vector(2, 3, 4), offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSubsetIllegal() {
		ts1.subset(2, 1);
	}

}
