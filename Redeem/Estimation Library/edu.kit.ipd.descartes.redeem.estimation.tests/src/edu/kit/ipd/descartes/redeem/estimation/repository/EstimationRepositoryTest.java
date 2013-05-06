package edu.kit.ipd.descartes.redeem.estimation.repository;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;
import static edu.kit.ipd.descartes.linalg.LinAlg.row;
import static edu.kit.ipd.descartes.linalg.testutil.MatrixAssert.assertThat;
import static org.fest.assertions.api.Assertions.offset;

import org.fest.assertions.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;

public class EstimationRepositoryTest {

	private static final double[][] A = new double[][] { { 1, 2, 3 },
			{ 4, 5, 6 }, { 7, 8, 9 } };
	private static final double[][] B = new double[][] { { 1, 4 },
			{ 16, 25 }, {36, 49}};

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMeasurementTable() {
		Matrix measurements = matrix(row(A[0][0], A[0][1], A[0][2]),
				row(A[1][0], A[1][1], A[1][2]),
				row(A[2][0], A[2][1], A[2][2]));
		
		Resource[] resources = new Resource[3];
		resources[0] = new Resource("CPU");
		resources[1] = new Resource("HardDisk1");
		resources[2] = new Resource("HardDisk2");
		
		MeasurementTable utilizationTable = new MeasurementTable(resources,
				Metric.UTILIZATION);
			
		utilizationTable.addRow(measurements.row(0));
		utilizationTable.addRow(measurements.row(1));
		utilizationTable.addRow(measurements.row(2));
		
		assertThat(measurements).isEqualTo(utilizationTable.getAllMesurenments(),
				offset(1e-9));
		assertThat(measurements.column(0)).isEqualTo(
				utilizationTable.getColumn(resources[0].getName()), offset(1e-9));
		assertThat(measurements.column(1)).isEqualTo(
				utilizationTable.getColumn(resources[1].getName()), offset(1e-9));
		assertThat(measurements.column(2)).isEqualTo(
				utilizationTable.getColumn(resources[2].getName()), offset(1e-9));
		assertThat(measurements.row(measurements.rows() - 1)).isEqualTo(
				utilizationTable.getLastMeasurement(), offset(1e-9));

	}
	
	@Test
	public void testMatrixMonitoringRepositoryQueries() {
		
		Matrix utilMeasurements = matrix(row(A[0][0], A[0][1], A[0][2]),
				row(A[1][0], A[1][1], A[1][2]),
				row(A[2][0], A[2][1], A[2][2]));
		
		Matrix throughputMeasurements = matrix(row(B[0][0], B[0][1]),
				row(B[1][0], B[1][1]),
				row(B[2][0], B[2][1]));
		
		Resource[] resources = new Resource[3];
		resources[0] = new Resource("CPU");
		resources[1] = new Resource("HardDisk1");
		resources[2] = new Resource("HardDisk2");
		
		Service[] services = new Service[2];
		services[0] = new Service("AddToCard");
		services[1] = new Service("Payment");
		
		MeasurementTable utilizationTable = new MeasurementTable(resources,
				Metric.UTILIZATION);
		MeasurementTable throughputTable = new MeasurementTable(services,
				Metric.THROUGHPUT);
			
		utilizationTable.addRow(utilMeasurements.row(0));
		utilizationTable.addRow(utilMeasurements.row(1));
		utilizationTable.addRow(utilMeasurements.row(2));
		
		throughputTable.addRow(throughputMeasurements.row(0));
		throughputTable.addRow(throughputMeasurements.row(1));
		throughputTable.addRow(throughputMeasurements.row(2));
		
		MatrixMonitoringRepository repository = new MatrixMonitoringRepository();
		repository.createMesurementTable(utilizationTable);
		repository.createMesurementTable(throughputTable);
		
		//queries
		//Query to get last measured utilization of HardDisk1
		Query<Scalar> utilizationQuery;
		utilizationQuery = QueryBuilder.select(Metric.UTILIZATION)
				.forResource(resources[1]).last();
		
		// Query to get last measured utilization of all resources
		Query<Vector> lastUtilOfAllResourcesQuery;
		lastUtilOfAllResourcesQuery = QueryBuilder.select(Metric.UTILIZATION)
				.forAllResources().last();

		// Query to get last measured throughput of all resources
		Query<Vector> lastThroughputAllServicesQuery = QueryBuilder.select(Metric.THROUGHPUT)
				.forAllServices().last();

		Result<Scalar> lastUtilOfResource = repository
				.execute(utilizationQuery);
		Result<Vector> lastUtilOfAllResource = repository
				.execute(lastUtilOfAllResourcesQuery);
		Result<Vector> lastThroughputOfAllResource = repository
				.execute(lastThroughputAllServicesQuery);

		Assertions.assertThat(utilMeasurements.get(2, 1)).isEqualTo(
				lastUtilOfResource.getData().getValue(), offset(1e-9));
		Assertions.assertThat(resources[1].getName()).isEqualTo(
				lastUtilOfResource.getEntity(0).getName());
		
		assertThat(utilMeasurements.row(utilMeasurements.rows() - 1)).isEqualTo(
				lastUtilOfAllResource.getData(), offset(1e-9));
		assertThat(throughputMeasurements.row(throughputMeasurements.rows() - 1)).isEqualTo(
				lastThroughputOfAllResource.getData(), offset(1e-9));
	}
}
