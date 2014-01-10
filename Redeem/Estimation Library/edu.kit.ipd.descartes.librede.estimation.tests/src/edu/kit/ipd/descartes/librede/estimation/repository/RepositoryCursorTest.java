package edu.kit.ipd.descartes.librede.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import java.util.Arrays;

import org.junit.Test;

import edu.kit.ipd.descartes.librede.estimation.repository.IMonitoringRepository;
import edu.kit.ipd.descartes.librede.estimation.repository.MemoryObservationRepository;
import edu.kit.ipd.descartes.librede.estimation.repository.RepositoryCursor;
import edu.kit.ipd.descartes.librede.estimation.repository.StandardMetric;
import edu.kit.ipd.descartes.librede.estimation.repository.TimeSeries;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;

public class RepositoryCursorTest {
	
	Resource[] resources = new Resource[] { new Resource("CPU"), new Resource("HardDisk1"), new Resource("HardDisk2") };
	Service[] services = new Service[] { new Service("AddToCard"), new Service("Payment") };
	
	TimeSeries ts1 = new TimeSeries(vector(2, 3, 4, 6, 8), vector(1, 1, 1, 1, 1));
	TimeSeries ts2 = new TimeSeries(vector(2.2, 3.2, 4.2, 6.2, 8.2), vector(1, 1, 1, 1, 1));
	
	IMonitoringRepository repository = new MemoryObservationRepository(new WorkloadDescription(Arrays.asList(resources), Arrays.asList(services)));
	
	@Test
	public void test1StepCursor() {
		ts1.setStartTime(1);
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1);
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1);
		RepositoryCursor cur = repository.getCursor(1, 1);
		cur.setEndTime(8);
		
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
		RepositoryCursor cur = repository.getCursor(1.2, 1);
		cur.setEndTime(7.2);

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
		RepositoryCursor cur = repository.getCursor(1, 1);
		cur.setEndTime(8);
		
		for (int i = 2; i <= 8; i++) {
			assertThat(cur.next()).isTrue();
			assertThat(cur.getCurrentIntervalStart()).isEqualTo(i - 1);
			assertThat(cur.getCurrentIntervalEnd()).isEqualTo(i);
		}
		assertThat(cur.next()).isFalse();
		
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1.addSample(10.0, 1.0));
		repository.setData(StandardMetric.UTILIZATION, resources[1], ts1.addSample(10.0, 1.0));
		repository.setData(StandardMetric.UTILIZATION, resources[2], ts1.addSample(10.0, 1.0));
		cur.setEndTime(10);
		
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
		RepositoryCursor cur = repository.getCursor(1, 3);
		cur.setEndTime(8);
		
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
		RepositoryCursor cur = repository.getCursor(0, 100);
		assertThat(cur.next()).isFalse();		
	}
	
	@Test
	public void testEmptyCursor() {		
		RepositoryCursor cur = repository.getCursor(1, 1);
		cur.setEndTime(1);
		assertThat(cur.next()).isFalse();
		
		ts1.setStartTime(1);
		repository.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		cur.setEndTime(8);
		assertThat(cur.next()).isTrue();
		assertThat(cur.getCurrentIntervalStart()).isEqualTo(ts1.getStartTime(), offset(1e-9));
		assertThat(cur.getCurrentIntervalEnd()).isEqualTo(ts1.getStartTime() + 1.0, offset(1e-9));
	}

}
