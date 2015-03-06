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
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.Arrays;

import org.junit.Test;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;

public class RepositoryCursorTest {
	
	Resource[] resources = new Resource[] { WorkloadBuilder.newResource("CPU"), WorkloadBuilder.newResource("HardDisk1"), WorkloadBuilder.newResource("HardDisk2") };
	Service[] services = new Service[] { WorkloadBuilder.newService("AddToCard"), WorkloadBuilder.newService("Payment") };
	
	TimeSeries ts1 = new TimeSeries(vector(2, 3, 4, 6, 8), vector(1, 1, 1, 1, 1));
	TimeSeries ts2 = new TimeSeries(vector(2.2, 3.2, 4.2, 6.2, 8.2), vector(1, 1, 1, 1, 1));
	
	MemoryObservationRepository repository;
	
	public RepositoryCursorTest() {
		WorkloadDescription workload = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
		workload.getResources().addAll(Arrays.asList(resources));
		workload.getServices().addAll(Arrays.asList(services));
		repository = new MemoryObservationRepository(workload);
	}
	
	@Test
	public void test1StepCursor() {
		ts1.setStartTime(1);
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		repository.setCurrentTime(8);
		IRepositoryCursor cur = repository.getCursor(1, 1);
	
		for (int i = 2; i <= 8; i++) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(i - 1);
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(i);
		}
		assertThat(cur.next()).isFalse();
		
	}
	
	@Test
	public void test1StepWithOffsetCursor() {
		ts1.setStartTime(1);
		ts2.setStartTime(1.2);
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts2);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		repository.setCurrentTime(7.2);
		IRepositoryCursor cur = repository.getCursor(1.2, 1);

		for (int i = 0; i < 6; i++) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(1.2 + i, offset(1e-9));
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(2.2 + i, offset(1e-9));
		}
		assertThat(cur.next()).isFalse();		
	}
	
	@Test
	public void test1StepWithAddCursor() {
		ts1.setStartTime(1);
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		repository.setCurrentTime(8);
		IRepositoryCursor cur = repository.getCursor(1, 1);
		
		for (int i = 2; i <= 8; i++) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(i - 1);
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(i);
		}
		assertThat(cur.next()).isFalse();
		
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1.addSample(10.0, 1.0));
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1.addSample(10.0, 1.0));
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1.addSample(10.0, 1.0));
		repository.setCurrentTime(10);
		
		for (int i = 9; i <= 10; i++) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(i - 1);
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(i);
		}
		assertThat(cur.next()).isFalse();		
	}
	
	@Test
	public void test3StepCursor() {	
		ts1.setStartTime(1);
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		repository.setCurrentTime(8);
		IRepositoryCursor cur = repository.getCursor(1, 3);
		
		for (int i = 4; i <= 8; i+=3) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(i - 3);
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(i);
		}
		assertThat(cur.next()).isFalse();
		
	}
	
	@Test
	public void testTooBigStepCursor() {		
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		IRepositoryCursor cur = repository.getCursor(0, 100);
		assertThat(cur.next()).isFalse();		
	}
	
	@Test
	public void testEmptyCursor() {
		repository.setCurrentTime(1);
		IRepositoryCursor cur = repository.getCursor(1, 1);
		assertThat(cur.next()).isFalse();
		
		ts1.setStartTime(1);
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setCurrentTime(8);
		assertThat(cur.next()).isTrue();
		assertThat(cur.getCurrentIntervalStart()).isEqualTo(ts1.getStartTime(), offset(1e-9));
		assertThat(cur.getCurrentIntervalEnd()).isEqualTo(ts1.getStartTime() + 1.0, offset(1e-9));
	}

}
