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
package edu.kit.ipd.descartes.librede.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.empty;
import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.row;
import static edu.kit.ipd.descartes.linalg.LinAlg.scalar;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.junit.Before;
import org.junit.Test;

import edu.kit.ipd.descartes.librede.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.repository.TimeSeries.Interpolation;
import edu.kit.ipd.descartes.linalg.Matrix;
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
		ts1.setInterpolationMethod(Interpolation.LINEAR);
		ts1.setStartTime(0.0);
		ts2.setInterpolationMethod(Interpolation.LINEAR);
		ts2.setStartTime(0.0);
	}

	@Test
	public void testCreate() {
		TimeSeries tsNew = new TimeSeries(time1, data1);
		assertThat(tsNew.getTime()).isEqualTo(time1, offset(1e-9));
		assertThat(tsNew.getData(0)).isEqualTo(data1, offset(1e-9));
		assertThat(tsNew.getInterpolationMethod()).isEqualTo(Interpolation.NONE);
	}
	
	@Test
	public void testAddSample() {
		TimeSeries tsAdd = ts1.addSample(4, 0.4);
		assertThat(tsAdd).isNotNull();
		assertThat(tsAdd.getInterpolationMethod()).isEqualTo(Interpolation.LINEAR);
		assertThat(ts1.getTime()).isEqualTo(time1, offset(1e-9));
		assertThat(ts1.getData(0)).isEqualTo(data1, offset(1e-9));
		assertThat(tsAdd.getTime()).isEqualTo((Vector)vertcat(time1, scalar(4)), offset(1e-9));
		assertThat(tsAdd.getData(0)).isEqualTo((Vector)vertcat(data1, scalar(0.4)), offset(1e-9));
		
		tsAdd = ts1.addSample(2.5, 0.25);
		assertThat(tsAdd).isNotNull();
		assertThat(tsAdd.getInterpolationMethod()).isEqualTo(Interpolation.LINEAR);
		assertThat(ts1.getTime()).isEqualTo(time1, offset(1e-9));
		assertThat(ts1.getData(0)).isEqualTo(data1, offset(1e-9));
		assertThat(tsAdd.getTime()).isEqualTo(vector(1,2,2.5,3), offset(1e-9));
		assertThat(tsAdd.getData(0)).isEqualTo(vector(0.1,0.2,0.25,0.3), offset(1e-9));
		
		tsAdd = ts1.addSample(0, 0);
		assertThat(tsAdd).isNotNull();
		assertThat(tsAdd.getInterpolationMethod()).isEqualTo(Interpolation.LINEAR);
		assertThat(ts1.getTime()).isEqualTo(time1, offset(1e-9));
		assertThat(ts1.getData(0)).isEqualTo(data1, offset(1e-9));
		assertThat(tsAdd.getTime()).isEqualTo((Vector)vertcat(scalar(0), time1), offset(1e-9));
		assertThat(tsAdd.getData(0)).isEqualTo((Vector)vertcat(scalar(0), data1), offset(1e-9));
		
		tsAdd = TimeSeries.EMPTY.addSample(1, 0.1);
		assertThat(tsAdd).isNotNull();
		assertThat(tsAdd.getTime()).isEqualTo(scalar(1), offset(1e-9));
		assertThat(tsAdd.getData(0)).isEqualTo(scalar(0.1), offset(1e-9));
		
		tsAdd = tsAdd.addSample(2, 0.2);
		assertThat(tsAdd.getTime()).isEqualTo(vector(1, 2), offset(1e-9));
		assertThat(tsAdd.getData(0)).isEqualTo(vector(0.1, 0.2), offset(1e-9));
	}
	
	@Test
	public void testGetStartAndEndTime() {
		double start = ts1.getStartTime();
		double end = ts1.getEndTime();
		assertThat(start).isEqualTo(0.0);
		assertThat(end).isEqualTo(3.0);
	}
	
	@Test
	public void testGet() {
		double v1 = ts1.get(2, 0);
		double v2 = ts2.get(2, 0);
		assertThat(v1).isEqualTo(0.2);
		assertThat(v2).isEqualTo(0.2);
	}
	
	@Test
	public void testGetNotFound() {
		ts1.setInterpolationMethod(Interpolation.NONE);
		double value = ts1.get(2.5, 0);
		assertThat(value).isNaN();
		ts1.setInterpolationMethod(Interpolation.PIECEWISE_CONSTANT);
		value = ts1.get(2.5, 0);
		assertThat(value).isEqualTo(0.3, offset(1e-9));
		ts1.setInterpolationMethod(Interpolation.LINEAR);
		value = ts1.get(2.5, 0);
		assertThat(value).isEqualTo(0.25, offset(1e-9));
	}
	
	@Test
	public void testGetOutOfBounds() {
		double value = ts1.get(-1.0, 0);
		assertThat(value).isNaN();
		value = ts1.get(4.0, 0);
		assertThat(value).isNaN();
	}
	
	@Test
	public void testTimeWeightedMean() {
		ts2.setInterpolationMethod(Interpolation.PIECEWISE_CONSTANT);
		
		// Test in the middle of the range
		double r = ts2.subset(2, 4).timeWeightedMean(0);
		assertThat(r).isEqualTo(0.35, offset(1e-5));
		
		// Test border cases
		r = ts2.subset(0, 1).timeWeightedMean(0);
		assertThat(r).isEqualTo(0.1, offset(1e-9));
				
		r = ts2.subset(4, 5).timeWeightedMean(0);
		assertThat(r).isEqualTo(0.5, offset(1e-9));

		// Test single timestamp		
		r = ts2.subset(3, 3).timeWeightedMean(0);
		assertThat(r).isNaN();
		
		// Test out of range cases
		r = ts2.subset(0.25, 0.75).timeWeightedMean(0);
		assertThat(r).isEqualTo(0.1, offset(1e-5));
		
		r = ts2.subset(0.25, 5).timeWeightedMean(0);
		assertThat(r).isEqualTo((0.75 * 0.1 + 0.2 + 0.3 + 0.4 + 0.5) / 4.75, offset(1e-5));
		
		// Test inbetween two elements
		r = ts2.subset(2.25, 2.75).timeWeightedMean(0);
		assertThat(r).isEqualTo(0.3, offset(1e-5));;
				
		r = ts2.subset(1.25, 3.75).timeWeightedMean(0);
		assertThat(r).isEqualTo((0.75 * 0.2 + 0.3 + 0.75 * 0.4) / 2.5, offset(1e-5));
				
		r = ts2.subset(2.25, 3).timeWeightedMean(0);
		assertThat(r).isEqualTo(0.3, offset(1e-9));
				
		r = ts2.subset(2, 2.75).timeWeightedMean(0);
		assertThat(r).isEqualTo(0.3, offset(1e-9));
	}
	
	@Test
	public void testMeanSingleElement() {
		TimeSeries ts3 = new TimeSeries(vector(1), vector(0.1));
		ts3.setInterpolationMethod(Interpolation.LINEAR);
		ts3.setStartTime(0);
		
		double r = ts3.subset(0, 1).timeWeightedMean(0);
		assertThat(r).isEqualTo(0.1, offset(1e-9));
		
		r = ts3.subset(0, 1).mean(0);
		assertThat(r).isEqualTo(0.1, offset(1e-9));
	}
	
	@Test
	public void testTimeWeightedMeanIncompleteData() {
		TimeSeries ts3 = new TimeSeries(vector(3, 4, 5, 6, 7, 8, 9, 10, 11), vector(0.3, 0.4, Double.NaN, Double.NaN, 0.7, Double.NaN, 0.9, 1.0, 1.1));
		ts3.setInterpolationMethod(Interpolation.LINEAR);
		ts3.setStartTime(2);
		
		double r = ts3.subset(2, 4).timeWeightedMean(0);
		assertThat(r).isEqualTo((0.3 + 0.4) / 2, offset(1e-5));
		
		r = ts3.subset(3, 6).timeWeightedMean(0);
		assertThat(r).isEqualTo(0.4, offset(1e-5));	
	}
	
	@Test
	public void testMean() {
		// Test in the middle of the range
		double r = ts2.subset(2, 4).mean(0);
		assertThat(r).isEqualTo(0.35, offset(1e-5));
		
		// Test border cases
		r = ts2.subset(0, 1).mean(0);
		assertThat(r).isEqualTo(0.1, offset(1e-9));
				
		r = ts2.subset(4, 5).mean(0);
		assertThat(r).isEqualTo(0.5, offset(1e-9));

		// Test single timestamp		
		r = ts2.subset(3, 3).mean(0);
		assertThat(r).isNaN();
		
		// Test out of range  cases
		r = ts2.subset(0.25, 0.75).mean(0);
		assertThat(r).isNaN();
		
		r = ts2.subset(0.25, 5).mean(0);
		assertThat(r).isEqualTo((0.1 + 0.2 + 0.3 + 0.4 + 0.5) / 5, offset(1e-5));
		
		// Test inbetween two elements
		r = ts2.subset(2.25, 2.75).mean(0);
		assertThat(r).isNaN();
				
		r = ts2.subset(1.25, 3.75).mean(0);
		assertThat(r).isEqualTo(0.25, offset(1e-5));
				
		r = ts2.subset(2.25, 3).mean(0);
		assertThat(r).isEqualTo(0.3, offset(1e-9));
				
		r = ts2.subset(2, 2.75).mean(0);
		assertThat(r).isNaN();
	}
	
	@Test
	public void testSubset() {
		// Test empty time series
		TimeSeries r = TimeSeries.EMPTY.subset(1, 5);
		assertThat(r.isEmpty()).isTrue();
		
		// Test in the middle of the range
		r = ts2.subset(2, 4);
		assertThat(r).isNotNull();
		assertThat(r.getInterpolationMethod()).isEqualTo(Interpolation.LINEAR);
		assertThat(r.getTime()).isEqualTo(vector(3, 4), offset(1e-9));
		assertThat(r.getStartTime()).isEqualTo(2, offset(1e-9));
		assertThat(r.getEndTime()).isEqualTo(4, offset(1e-9));
		
		// Test border cases
		r = ts2.subset(0, 1);
		assertThat(r).isNotNull();
		assertThat(r.getInterpolationMethod()).isEqualTo(Interpolation.LINEAR);
		assertThat(r.getTime()).isEqualTo(vector(1), offset(1e-9));
		assertThat(r.getStartTime()).isEqualTo(0, offset(1e-9));
		assertThat(r.getEndTime()).isEqualTo(1, offset(1e-9));
		
		r = ts2.subset(4, 5);
		assertThat(r).isNotNull();
		assertThat(r.getInterpolationMethod()).isEqualTo(Interpolation.LINEAR);
		assertThat(r.getTime()).isEqualTo(vector(5), offset(1e-9));
		assertThat(r.getStartTime()).isEqualTo(4, offset(1e-9));
		assertThat(r.getEndTime()).isEqualTo(5, offset(1e-9));
		
		// Test single timestamp		
		r = ts2.subset(3, 3);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(empty(), offset(1e-9));
		
		// Test out of range  cases
		r = ts2.subset(0.25, 0.75);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(empty(), offset(1e-9));
		
		// Test inbetween two elements
		r = ts2.subset(2.25, 2.75);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(empty(), offset(1e-9));
		
		r = ts2.subset(1.25, 3.75);
		assertThat(r).isNotNull();
		assertThat(r.getInterpolationMethod()).isEqualTo(Interpolation.LINEAR);
		assertThat(r.getTime()).isEqualTo(vector(2, 3), offset(1e-9));
		assertThat(r.getStartTime()).isEqualTo(1.25, offset(1e-9));
		assertThat(r.getEndTime()).isEqualTo(3.75, offset(1e-9));
		
		r = ts2.subset(2.25, 3);
		assertThat(r).isNotNull();
		assertThat(r.getInterpolationMethod()).isEqualTo(Interpolation.LINEAR);
		assertThat(r.getTime()).isEqualTo(scalar(3), offset(1e-9));
		assertThat(r.getStartTime()).isEqualTo(2.25, offset(1e-9));
		assertThat(r.getEndTime()).isEqualTo(3, offset(1e-9));
		
		r = ts2.subset(2, 2.75);
		assertThat(r).isNotNull();
		assertThat(r.getTime()).isEqualTo(empty(), offset(1e-9));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSubsetIllegal1() {
		ts1.subset(2, 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSubsetIllegal2() {
		ts1.subset(-1, 1);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testSubsetIllegal3() {
		ts1.subset(4, 6);
	}
	
	@Test
	public void testEmptyTimeSeries() {
		TimeSeries tsempty = TimeSeries.EMPTY;
		assertThat(tsempty.isEmpty());
		assertThat(tsempty.getStartTime()).isNaN();
		assertThat(tsempty.getEndTime()).isNaN();
		assertThat(tsempty.getTime()).isEqualTo(empty());
		assertThat(tsempty.getData(0)).isEqualTo(empty());
	}
	
	@Test
	public void testGetData() {
		TimeSeries ts = new TimeSeries(vector(1, 2), matrix(row(1, 1, 1), row(2, 2, 2)));
		Matrix data = ts.getData();
		assertThat(data.columns()).isEqualTo(3);
		assertThat(data.rows()).isEqualTo(2);
	}

}
