/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
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
import static tools.descartes.librede.linalg.LinAlg.vector;

import java.util.Arrays;

import org.junit.Test;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.exceptions.NoMonitoringDataException;
import tools.descartes.librede.repository.rules.DerivationRule;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;

public class MemoryObservationRepositoryTest extends LibredeTest {
	
	Resource[] resources = new Resource[] { WorkloadBuilder.newResource("CPU"), WorkloadBuilder.newResource("HardDisk1"), WorkloadBuilder.newResource("HardDisk2") };
	Service[] services = new Service[] { WorkloadBuilder.newService("AddToCard"), WorkloadBuilder.newService("Payment") };
	
	TimeSeries ts1 = new TimeSeries(vector(2, 3, 4, 6, 8), vector(1, 1, 1, 1, 1));
	TimeSeries ts2 = new TimeSeries(vector(4, 5, 6, 9, 12), vector(1, 1, 1, 1, 1));
	TimeSeries ts3 = new TimeSeries(vector(10, 12, 13, 14, 15), vector(1, 1, 1, 1, 1));
	
	MemoryObservationRepository repo;

	public MemoryObservationRepositoryTest() {
		WorkloadDescription workload = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
		workload.getResources().addAll(Arrays.asList(resources));
		workload.getServices().addAll(Arrays.asList(services));
		repo = new MemoryObservationRepository(workload);
	}
	
	@Test
	public void testListResourcesAndServices() {
		WorkloadDescription workload1 = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
		workload1.getResources().addAll(Arrays.asList(resources));
		workload1.getServices().addAll(Arrays.asList(services));
		MemoryObservationRepository repository1 = new MemoryObservationRepository(workload1);
		
		assertThat(repository1.listResources()).isEqualTo(Arrays.asList(resources));		
		assertThat(repository1.listServices()).isEqualTo(Arrays.asList(services));
		
		WorkloadDescription workload2 = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
		workload2.getServices().addAll(Arrays.asList(services));
		MemoryObservationRepository repository2 = new MemoryObservationRepository(workload2);
		
		assertThat(repository2.listResources()).isEmpty();
		assertThat(repository2.listServices()).isEqualTo(Arrays.asList(services));
		
		WorkloadDescription workload3 = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
		workload3.getResources().addAll(Arrays.asList(resources));
		MemoryObservationRepository repository3 = new MemoryObservationRepository(workload3);
		
		assertThat(repository3.listResources()).isEqualTo(Arrays.asList(resources));
		assertThat(repository3.listServices()).isEmpty();
		
		WorkloadDescription workload4 = ConfigurationFactory.eINSTANCE.createWorkloadDescription();
		MemoryObservationRepository repository4 = new MemoryObservationRepository(workload4);
		
		assertThat(repository4.listResources()).isEmpty();
		assertThat(repository4.listServices()).isEmpty();	
	}

	@Test
	public void testSetAndGetData() {
		repo.insert(StandardMetrics.UTILIZATION, Ratio.NONE, resources[0], ts1);
		assertThat(repo.select(StandardMetrics.UTILIZATION, Ratio.NONE, resources[0], Aggregation.NONE).getData(0).rows()).isEqualTo(5);
		repo.insert(StandardMetrics.UTILIZATION, Ratio.NONE, resources[0], ts2);
		assertThat(repo.select(StandardMetrics.UTILIZATION, Ratio.NONE, resources[0], Aggregation.NONE).getData(0).rows()).isEqualTo(10);
	}
	
	@Test(expected = NoMonitoringDataException.class)
	public void testGetDataEmpty() {
		TimeSeries ts = repo.select(StandardMetrics.RESPONSE_TIME, Time.SECONDS, resources[0], Aggregation.AVERAGE);
		assertThat(ts).isNotNull();
		assertThat(ts.isEmpty()).isTrue();
	}
	
	@Test
	public void testInsertDerivationSelfReferential() {
		DerivationRule<RequestRate> selfRef = DerivationRule.rule(StandardMetrics.THROUGHPUT, Aggregation.AVERAGE).requiring(StandardMetrics.THROUGHPUT, Aggregation.NONE);
		repo.insertDerivation(selfRef, services[0]);
		
		assertThat(repo.exists(StandardMetrics.THROUGHPUT, services[0], Aggregation.AVERAGE)).isFalse();		
	}
	
	@Test
	public void testInsertDerivation() {
		DerivationRule<Ratio> selfRef = DerivationRule.rule(StandardMetrics.UTILIZATION, Aggregation.AVERAGE).requiring(StandardMetrics.UTILIZATION, Aggregation.NONE);
		repo.insert(StandardMetrics.UTILIZATION, Ratio.NONE, resources[0], ts1);
		repo.insertDerivation(selfRef, resources[0]);
		
		assertThat(repo.exists(StandardMetrics.UTILIZATION, resources[0], Aggregation.AVERAGE)).isTrue();		
	}
}
