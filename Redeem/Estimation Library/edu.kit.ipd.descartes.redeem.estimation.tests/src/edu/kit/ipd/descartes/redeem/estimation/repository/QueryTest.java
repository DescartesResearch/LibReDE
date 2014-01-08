package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.row;
import static edu.kit.ipd.descartes.linalg.LinAlg.scalar;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.testutil.VectorAssert.assertThat;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import java.util.Arrays;

import org.fest.assertions.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.testutil.MatrixAssert;
import edu.kit.ipd.descartes.redeem.estimation.workload.Resource;
import edu.kit.ipd.descartes.redeem.estimation.workload.Service;
import edu.kit.ipd.descartes.redeem.estimation.workload.WorkloadDescription;

public class QueryTest {

	private MemoryObservationRepository repository;
	private Resource[] resources = new Resource[] { new Resource("CPU"),
			new Resource("HardDisk1"), new Resource("HardDisk2") };
	private Service[] services = new Service[] { new Service("AddToCard"),
			new Service("Payment") };
	private Matrix utilMeasurements = matrix(row(0.2, 0.3, 0.4), row(0.3, 0.4, 0.5), row(0.4, 0.5, 0.6), row(0.5, 0.6, 0.7), row(0.6, 0.7, 0.8));
	private Matrix throughputMeasurements = matrix(row(4, 5), row(6, 7), row(8, 9), row(10, 11), row(12, 13));
	private Matrix rtMeasurements = matrix(row(0.4, 0.5), row(0.6, 0.7), row(0.8, 0.9), row(0.10, 0.11), row(0.12, 0.13));

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		Vector timestamps = vector(1, 2, 3, 4, 5);

		TimeSeries cpuUtilTable = new TimeSeries(timestamps,
				utilMeasurements.column(0));
		cpuUtilTable.setStartTime(0);
		TimeSeries hd1UtilTable = new TimeSeries(timestamps,
				utilMeasurements.column(1));
		hd1UtilTable.setStartTime(0);
		TimeSeries hd2UtilTable = new TimeSeries(timestamps,
				utilMeasurements.column(2));
		hd2UtilTable.setStartTime(0);
		
		TimeSeries addServTputTable = new TimeSeries(timestamps,
				throughputMeasurements.column(0));
		addServTputTable.setStartTime(0);
		TimeSeries payServTputTable = new TimeSeries(timestamps,
				throughputMeasurements.column(1));
		payServTputTable.setStartTime(0);
		
		TimeSeries addServRtTable = new TimeSeries(timestamps,
				rtMeasurements.column(0));
		addServRtTable.setStartTime(0);
		TimeSeries payServRtTable = new TimeSeries(timestamps,
				rtMeasurements.column(1));
		payServRtTable.setStartTime(0);

		repository = new MemoryObservationRepository(new WorkloadDescription(
				Arrays.asList(resources), Arrays.asList(services)));
		repository.setData(StandardMetric.UTILIZATION, resources[0], cpuUtilTable);
		repository.setData(StandardMetric.UTILIZATION, resources[1], hd1UtilTable);
		repository.setData(StandardMetric.UTILIZATION, resources[2], hd2UtilTable);
		repository.setData(StandardMetric.THROUGHPUT, services[0], addServTputTable);
		repository.setData(StandardMetric.THROUGHPUT, services[1], payServTputTable);
		repository.setData(StandardMetric.RESPONSE_TIME, services[0], addServRtTable);
		repository.setData(StandardMetric.RESPONSE_TIME, services[1], payServRtTable);
	}

	@After
	public void tearDown() throws Exception {
	}

	// @Test
	// public void testMeasurementTable() {
	// Vector timestamps = vector(1, 2, 3);
	// Matrix measurements = matrix(row(A[0][0], A[0][1], A[0][2]),
	// row(A[1][0], A[1][1], A[1][2]),
	// row(A[2][0], A[2][1], A[2][2]));
	//
	// Resource[] resources = new Resource[3];
	// resources[0] = new Resource("CPU");
	// resources[1] = new Resource("HardDisk1");
	// resources[2] = new Resource("HardDisk2");
	//
	// TimeSeries cpuUtilTable = new TimeSeries(timestamps,
	// measurements.column(0));
	// TimeSeries hd1UtilTable = new TimeSeries(timestamps,
	// measurements.column(1));
	// TimeSeries hd2UtilTable = new TimeSeries(timestamps,
	// measurements.column(2));
	//
	// MeasurementTable utilizationTable = new MeasurementTable(resources,
	// Metric.UTILIZATION);
	//
	// utilizationTable.addRow(measurements.row(0));
	// utilizationTable.addRow(measurements.row(1));
	// utilizationTable.addRow(measurements.row(2));
	//
	// assertThat(measurements).isEqualTo(utilizationTable.getAllMeasurements(),
	// offset(1e-9));
	// assertThat(measurements.column(0)).isEqualTo(
	// utilizationTable.getColumn(resources[0].getName()), offset(1e-9));
	// assertThat(measurements.column(1)).isEqualTo(
	// utilizationTable.getColumn(resources[1].getName()), offset(1e-9));
	// assertThat(measurements.column(2)).isEqualTo(
	// utilizationTable.getColumn(resources[2].getName()), offset(1e-9));
	// assertThat(measurements.row(measurements.rows() - 1)).isEqualTo(
	// utilizationTable.getLastMeasurement(), offset(1e-9));
	//
	// }
	@Test
	public void testAllQuery() {
		RepositoryCursor cursor = repository.getCursor(0, 5);
		cursor.setEndTime(5);
		assertThat(cursor.next()).isTrue();
		
		Query<Vector> respSingle = QueryBuilder.select(StandardMetric.RESPONSE_TIME).forService(services[1]).all().using(cursor);
		Vector result = respSingle.execute();
		
		assertThat(result).isEqualTo(rtMeasurements.column(1), offset(1e-9));
		
	}

//	@Test
//	public void testMatrixMonitoringRepositoryQueries() {
//		RepositoryCursor view = repository.getCursor(1);
//		view.next();
//
//		// queries
//		// Query to get last measured utilization of HardDisk1
//		Query<Scalar> utilizationQuery;
//		utilizationQuery = QueryBuilder.select(Metric.UTILIZATION)
//				.forResource(resources[1]).average().using(view);
//
//		// Query to get last measured utilization of all resources
//		Query<Vector> lastUtilOfAllResourcesQuery;
//		lastUtilOfAllResourcesQuery = QueryBuilder.select(Metric.UTILIZATION)
//				.forAllResources().average().using(view);
//
//		// Query to get last measured throughput of all resources
//		Query<Vector> lastThroughputAllServicesQuery = QueryBuilder
//				.select(Metric.THROUGHPUT).forAllServices().average()
//				.using(view);
//
//		assertThat(utilMeasurements.get(2, 1)).isEqualTo(
//				utilizationQuery.execute().getValue(), offset(1e-9));
//		assertThat(resources[1].getName()).isEqualTo(
//				utilizationQuery.getEntity(0).getName());
//
//		assertThat(utilMeasurements.row(utilMeasurements.rows() - 1))
//				.isEqualTo(lastUtilOfAllResourcesQuery.execute(), offset(1e-9));
//		assertThat(
//				throughputMeasurements.row(throughputMeasurements.rows() - 1))
//				.isEqualTo(lastThroughputAllServicesQuery.execute(),
//						offset(1e-9));
//	}
//
//	@Test
//	public void testAggregateQueriesForEntity() {
//		RepositoryCursor view = repository.getCursor(1);
//		view.next();
//
//		for (int i = 0; i < 3; ++i) {
//
//			Query<Scalar> utilizationQueryAvg;
//			utilizationQueryAvg = QueryBuilder.select(Metric.UTILIZATION)
//					.forResource(resources[1]).average().using(view);
//
//			Query<Scalar> utilizationQuerySum;
//			utilizationQuerySum = QueryBuilder.select(Metric.UTILIZATION)
//					.forResource(resources[1]).sum().using(view);
//
//			double sum = 0;
//			double last = A[2][1];
//			double avg = 0;
//
//			for (int j = 0; j < i; ++j) {
//				sum += A[2 - j][1];
//			}
//
//			if (i != 0)
//				avg = sum / i;
//
//			Assertions.assertThat(sum).isEqualTo(
//					utilizationQuerySum.execute().getValue(), offset(1e-9));
//			Assertions.assertThat(avg).isEqualTo(
//					utilizationQueryAvg.execute().getValue(), offset(1e-9));
//
//		}
//
//	}
//
//	@Test
//	public void testAggregateQueriesForAllResources() {
//		for (int i = 1; i < 3; ++i) {
//			RepositoryCursor view = repository.getCursor(i + 1);
//			view.next();
//
//			Query<Vector> utilizationQueryAvg;
//			utilizationQueryAvg = QueryBuilder.select(Metric.UTILIZATION)
//					.forAllResources().average().using(view);
//
//			Query<Vector> utilizationQuerySum;
//			utilizationQuerySum = QueryBuilder.select(Metric.UTILIZATION)
//					.forAllResources().sum().using(view);
//
//			Vector last = vector(A[2][0], A[2][1], A[2][2]);
//			Vector sum = vector(0, 0, 0);
//
//			for (int j = 0; j < i; ++j) {
//				sum = sum.plus(vector(A[2 - j][0], A[2 - j][1], A[2 - j][2]));
//			}
//
//			Vector avg = vector(0, 0, 0);
//			if (i != 0) {
//				avg = sum.times(1.0 / (double) i);
//			}
//
//			MatrixAssert.assertThat(sum).isEqualTo(
//					utilizationQuerySum.execute(), offset(1e-9));
//			MatrixAssert.assertThat(avg).isEqualTo(
//					utilizationQueryAvg.execute(), offset(1e-9));
//
//		}
//	}
}
