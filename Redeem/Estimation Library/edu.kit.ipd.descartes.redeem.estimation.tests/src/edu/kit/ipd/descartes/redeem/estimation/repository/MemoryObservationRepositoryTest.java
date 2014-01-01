package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class MemoryObservationRepositoryTest {
	
	Resource[] resources = new Resource[] { new Resource("CPU"), new Resource("HardDisk1"), new Resource("HardDisk2") };
	Service[] services = new Service[] { new Service("AddToCard"), new Service("Payment") };
	
	TimeSeries ts1 = new TimeSeries(vector(2, 3, 4, 6, 8), vector(1, 1, 1, 1, 1));
	TimeSeries ts2 = new TimeSeries(vector(4, 5, 6, 9, 12), vector(1, 1, 1, 1, 1));
	TimeSeries ts3 = new TimeSeries(vector(10, 12, 13, 14, 15), vector(1, 1, 1, 1, 1));
	
	MemoryObservationRepository repo = new MemoryObservationRepository(new WorkloadDescription(Arrays.asList(resources), Arrays.asList(services)));
	
	@Test
	public void testListResourcesAndServices() {		
		MemoryObservationRepository repository1 = new MemoryObservationRepository(new WorkloadDescription(Arrays.asList(resources), Arrays.asList(services)));
		MemoryObservationRepository repository2 = new MemoryObservationRepository(new WorkloadDescription(Collections.<Resource>emptyList(), Arrays.asList(services)));
		MemoryObservationRepository repository3 = new MemoryObservationRepository(new WorkloadDescription(Arrays.asList(resources), Collections.<Service>emptyList()));
		MemoryObservationRepository repository4 = new MemoryObservationRepository(new WorkloadDescription(Collections.<Resource>emptyList(), Collections.<Service>emptyList()));
		
		assertThat(repository1.listResources()).isEqualTo(Arrays.asList(resources));		
		assertThat(repository1.listServices()).isEqualTo(Arrays.asList(services));
		
		assertThat(repository2.listResources()).isEmpty();
		assertThat(repository2.listServices()).isEqualTo(Arrays.asList(services));
		
		assertThat(repository3.listResources()).isEqualTo(Arrays.asList(resources));
		assertThat(repository3.listServices()).isEmpty();
		
		assertThat(repository4.listResources()).isEmpty();
		assertThat(repository4.listServices()).isEmpty();	
	}

	@Test
	public void testStartAndEndTimestamp() {
		assertThat(repo.getStartTimestamp()).isNaN();
		assertThat(repo.getEndTimestamp()).isNaN();
		
		repo.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		assertThat(repo.getStartTimestamp()).isEqualTo(2.0);
		assertThat(repo.getEndTimestamp()).isEqualTo(8.0);
		
		repo.setData(StandardMetric.THROUGHPUT, services[0], ts2);
		assertThat(repo.getStartTimestamp()).isEqualTo(4.0);
		assertThat(repo.getEndTimestamp()).isEqualTo(8.0);
		
		repo.setData(StandardMetric.THROUGHPUT, services[1], ts3);
		assertThat(repo.getStartTimestamp()).isEqualTo(10.0);
		assertThat(repo.getEndTimestamp()).isEqualTo(8.0);
		
		repo.setData(StandardMetric.UTILIZATION, resources[0], ts1.addSample(10.0, 1.0));
		assertThat(repo.getStartTimestamp()).isEqualTo(10.0);
		assertThat(repo.getEndTimestamp()).isEqualTo(10.0);
	}
	
	@Test
	public void testSetAndGetData() {
		repo.setData(StandardMetric.UTILIZATION, resources[0], ts1);
		assertThat(repo.getData(StandardMetric.UTILIZATION, resources[0]).getData().rows()).isEqualTo(5);
		repo.setData(StandardMetric.UTILIZATION, resources[0], ts1.addSample(10.0, 1.0));
		assertThat(repo.getData(StandardMetric.UTILIZATION, resources[0]).getData().rows()).isEqualTo(6);
	}
	
	@Test
	public void testGetDataEmpty() {
		TimeSeries ts = repo.getData(StandardMetric.RESPONSE_TIME, resources[0]);
		assertThat(ts).isNotNull();
		assertThat(ts.isEmpty()).isTrue();
	}
	

}
