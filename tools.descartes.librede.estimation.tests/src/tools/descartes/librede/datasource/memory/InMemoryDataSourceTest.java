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
package tools.descartes.librede.datasource.memory;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.testutil.MatrixAssert.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.FileTraceConfiguration;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.datasource.DataSourceSelector;
import tools.descartes.librede.datasource.TraceEvent;
import tools.descartes.librede.datasource.csv.CsvDataSource;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.testutils.LibredeTest;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;
import tools.descartes.librede.units.UnitsFactory;

public class InMemoryDataSourceTest extends LibredeTest {
	
	private Service[] services = new Service[3];
	
	private Random rand;
	
	private InMemoryDataSource dataSource;
	private DataSourceSelector selector;
	
	private TraceConfiguration configuration1;
	private TraceConfiguration configuration2;
	
	@Before
	public void setup() throws IOException {
		rand = new Random(1);
		
		for (int i = 0; i < services.length; i++) {
			services[i] = ConfigurationFactory.eINSTANCE.createService();
			services[i].setName("service" + i);
		}
	
		configuration1 = createTraceConfiguration("series1", StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND);
		configuration2 = createTraceConfiguration("series2", StandardMetrics.RESPONSE_TIME, Time.SECONDS);		

		dataSource = new InMemoryDataSource();
		
		selector = new DataSourceSelector();
		
		selector.add(dataSource);
	}

	private TraceConfiguration createTraceConfiguration(String location, Metric<?> metric, Unit<?> unit) {		
		TraceConfiguration configuration = ConfigurationFactory.eINSTANCE.createTraceConfiguration();
		configuration.setLocation(location);
		configuration.setMetric(metric);
		configuration.setUnit(unit);
		configuration.setInterval(UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS));
		for (int i = 0; i < services.length; i++) {
			TraceToEntityMapping mapping = ConfigurationFactory.eINSTANCE.createTraceToEntityMapping();
			mapping.setEntity(services[i]);
			mapping.setTraceColumn(i + 1);
			configuration.getMappings().add(mapping);
		}
		return configuration;
	}

	@Test
	public void testOneFilePreloadedSmall() throws IOException, InterruptedException {
		// add data before registering the file with the data source
		// to test whether existing data is also read.
		TimeSeries refData = generateData(256, 10, 0);
		dataSource.append("series1", refData);
		dataSource.addTrace(configuration1);
		dataSource.load();
		
		TraceEvent event;
		TimeSeries[] data = new TimeSeries[services.length];
		while((event = selector.poll()) != null) {
			checkAndStore(event, data);
		}
		assertThat(selector.getLatestObservationTime().getValue(Time.SECONDS)).isEqualTo(255);
		compare(data, refData.getData());		
	}
	
	@Test
	public void testOneFilePreloadedLarge() throws IOException, InterruptedException {
		// add data before registering the file with the data source
		// to test whether existing data is also read.
		TimeSeries refData = generateData(32000, 10, 0);
		dataSource.append("series1", refData);
		dataSource.addTrace(configuration1);
		dataSource.load();
		
		TraceEvent event;
		TimeSeries[] data = new TimeSeries[services.length];
		while((event = selector.poll()) != null) {
			checkAndStore(event, data);
		}
		assertThat(selector.getLatestObservationTime().getValue(Time.SECONDS)).isEqualTo(31999);
		compare(data, refData.getData());		
	}
	
	@Test
	public void testOneFileContinuous() throws IOException, InterruptedException {
		// add data before registering the file with the data source
		// to test whether existing data is also read.
		TimeSeries refData = generateData(256, 10, 0);
		dataSource.addTrace(configuration1);
		dataSource.append("series1", refData);
		dataSource.load();
		
		for (int i = 0; i < 3; i++) {
			TraceEvent event;
			TimeSeries[] data = new TimeSeries[services.length];
			boolean found = false;
			while((event = selector.poll(UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS))) != null) {
				found = true;
				checkAndStore(event, data);
			}
			assertThat(found).isTrue();
			assertThat(selector.getLatestObservationTime().getValue(Time.SECONDS)).isEqualTo(256 * (i + 1) - 1);
			compare(data, refData.getData());
			
			refData = generateData(256, 10, 256 * (i + 1));
			dataSource.append("series1", refData);
		}
	}
	
	@Test
	public void testMultipleFiles() throws IOException, InterruptedException {
		TimeSeries refData1 = generateData(256, 10, 0);
		TimeSeries refData2 = generateData(256, 10, 0);
		dataSource.addTrace(configuration1);
		dataSource.addTrace(configuration2);
		dataSource.append("series1", refData1);
		dataSource.append("series2", refData2);
		dataSource.load();

		for (int i = 0; i < 3; i++) {
			TraceEvent event;
			TimeSeries[] data1 = new TimeSeries[services.length];
			TimeSeries[] data2 = new TimeSeries[services.length];
			boolean found = false;
			while((event = selector.poll(UnitsFactory.eINSTANCE.createQuantity(1, Time.SECONDS))) != null) {
				found = true;
				if (event.getKey().getMetric().equals(StandardMetrics.THROUGHPUT)) {
					checkAndStore(event, data1);
				} else {
					checkAndStore(event, data2);
				}
			}
			assertThat(found).isTrue();
			assertThat(selector.getLatestObservationTime().getValue(Time.SECONDS)).isEqualTo(256 * (i + 1) - 1);
			compare(data1, refData1.getData());
			compare(data2, refData2.getData());
			
			refData1 = generateData(256, 10, 256 * (i + 1));
			refData2 = generateData(256, 10, 256 * (i + 1));
			dataSource.append("series1", refData1);
			dataSource.append("series2", refData2);
		}
	}
	
	private void checkAndStore(TraceEvent event, TimeSeries[] data) {
		assertThat(event).isNotNull();
		
		int idx = 0;
		for (; (idx < services.length) && !event.getKey().getEntity().equals(services[idx]); idx++);
		
		assertThat(idx).isLessThan(services.length);
		
		if (data[idx] == null) {
			data[idx] = event.getData();
		} else {
			data[idx] = data[idx].append(event.getData());
		}
	}
	
	private void compare(TimeSeries[] data, Matrix refData) {
		for (int i = 0; i < data.length; i++) {
			assertThat(data[i].getData()).isEqualTo(refData.column(i), offset(1e-9));
		}
	}
	
	@After
	public void teardown() throws IOException {
		dataSource.close();
	}
	
	private TimeSeries generateData(int rowCount, int columnCount, double timeOffset) throws IOException {
		VectorBuilder time = VectorBuilder.create(rowCount);
		MatrixBuilder values = MatrixBuilder.create(rowCount, columnCount);
		double[] row = new double[columnCount];
		for (int i = 0; i < rowCount; i++) {
			time.add(timeOffset + i);			
			for (int j = 0; j < columnCount; j++) {
				row[j] = rand.nextDouble();
			}
			values.addRow(row);
		}
		return new TimeSeries(time.toVector(), values.toMatrix());
	}
}
