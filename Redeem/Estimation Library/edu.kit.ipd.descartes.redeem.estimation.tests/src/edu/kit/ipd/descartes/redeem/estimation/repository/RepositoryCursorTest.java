package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import java.util.Arrays;

import org.junit.Test;

import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class RepositoryCursorTest {
	
	Resource[] resources = new Resource[] { new Resource("CPU"), new Resource("HardDisk1"), new Resource("HardDisk2") };
	Service[] services = new Service[] { new Service("AddToCard"), new Service("Payment") };
	
	TimeSeries ts1 = new TimeSeries(vector(2, 3, 4, 6, 8), vector(1, 1, 1, 1, 1));
	TimeSeries ts2 = new TimeSeries(vector(2.2, 3.2, 4.2, 6.2, 8.2), vector(1, 1, 1, 1, 1));
	
	IMonitoringRepository repository = new MemoryObservationRepository(new WorkloadDescription(Arrays.asList(resources), Arrays.asList(services)));
	
	@Test
	public void test1StepCursor() {		
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		RepositoryCursor cur = repository.getCursor(1);
		
		for (int i = 3; i <= 8; i++) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(i - 1);
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(i);
		}
		assertThat(cur.next()).isFalse();
		
	}
	
	@Test
	public void test1StepWithOffsetCursor() {		
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts2);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		RepositoryCursor cur = repository.getCursor(1);
		
		for (int i = 0; i < 5; i++) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(2.2 + i);
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(3.2 + i);
		}
		assertThat(cur.next()).isFalse();		
	}
	
	@Test
	public void test1StepWithAddCursor() {		
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		RepositoryCursor cur = repository.getCursor(1);
		
		for (int i = 3; i <= 8; i++) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(i - 1);
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(i);
		}
		assertThat(cur.next()).isFalse();
		
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1.addSample(10.0, 1.0));
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1.addSample(10.0, 1.0));
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1.addSample(10.0, 1.0));
		
		for (int i = 9; i <= 10; i++) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(i - 1);
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(i);
		}
		assertThat(cur.next()).isFalse();		
	}
	
	@Test
	public void test3StepCursor() {		
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		RepositoryCursor cur = repository.getCursor(3);
		
		for (int i = 5; i <= 8; i+=3) {
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
		RepositoryCursor cur = repository.getCursor(100);
		assertThat(cur.next()).isFalse();		
	}
	
	@Test
	public void testEmptyCursor() {		
		RepositoryCursor cur = repository.getCursor(1);
		assertThat(cur.next()).isFalse();
		assertThat(cur.getCurrentIntervalStart()).isNaN();
		assertThat(cur.getCurrentIntervalEnd()).isNaN();
		
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		assertThat(cur.next()).isTrue();
		assertThat(cur.getCurrentIntervalStart()).isEqualTo(ts1.getStartTime(), offset(1e-9));
		assertThat(cur.getCurrentIntervalEnd()).isEqualTo(ts1.getStartTime() + 1.0, offset(1e-9));
	}

}
