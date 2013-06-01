package edu.kit.ipd.descartes.redeem.estimation.repository;

import java.util.HashMap;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Range;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorInitializer;
import edu.kit.ipd.descartes.redeem.estimation.repository.Query.Aggregation;
import edu.kit.ipd.descartes.redeem.estimation.system.IModelEntity;
import edu.kit.ipd.descartes.redeem.estimation.system.Resource;
import edu.kit.ipd.descartes.redeem.estimation.system.Service;
import static edu.kit.ipd.descartes.linalg.LinAlg.*;

/**
 * This class implements the IMonitoringRepository
 * 
 * @author Mehran Saliminia
 * 
 */
public class MatrixMonitoringRepository implements IMonitoringRepository {

	HashMap<Metric, MeasurementTable> repository = new HashMap<Metric, MeasurementTable>();

	public MatrixMonitoringRepository() {

	}
	
	@Override
	public <T extends Matrix> Result<T> execute(Query<T> query) {

		if (query.getType().equals(Query.Type.ALL_RESOURCES)
				|| query.getType().equals(Query.Type.ALL_SERVICES))
			return executeSelectQueryForAllEntities(query);
		else
			return executeSelectQueryForEntity(query);
	}

	public void createMesurementTable(MeasurementTable measurementTable) {
		if (measurementTable != null
				&& !repository.containsKey(measurementTable.getMetric()
						.toString()))
			repository.put(measurementTable.getMetric(),
					measurementTable);
	}

	@SuppressWarnings("unchecked")
	private <T extends Matrix> Result<T> executeSelectQueryForAllEntities(
			Query<T> query) {
		MeasurementTable table = repository.get(query.getMetric());
		IModelEntity[] entities;

		//zero Vector
		Vector zero = vector(table.columns(), new VectorInitializer() {					
			@Override
			public double cell(int row) {
				return 0;
			}
		});
		
		// TODO throw exception
		if (table == null)
			return null;

		entities = new IModelEntity[table.columns()];
		if (query.getType().equals(Query.Type.ALL_SERVICES))
			for (int i = 0; i < entities.length; ++i)
				entities[i] = new Service(table.getEntityNames()[i]);

		if (query.getType().equals(Query.Type.ALL_RESOURCES))
			for (int i = 0; i < entities.length; ++i)
				entities[i] = new Resource(table.getEntityNames()[i]);

		if (query.getAggregation().equals(Aggregation.LAST))
			return new Result<T>((T) table.getLastMeasurement(), entities);
		else if (query.getAggregation().equals(Aggregation.AVERAGE)) {	
			if(query.getWindowSize() == 0 )
				return new Result<T>((T) zero, entities);
			
			Matrix measurements;
			int numberOfRows = table.getSize();

			measurements = table.getAllMeasurements().row(numberOfRows - 1);
			for (int i = 1; i < query.getWindowSize(); ++i) {
				measurements = measurements.plus(table.getAllMeasurements().row(
						numberOfRows - i - 1));
			}
			numberOfRows = query.getWindowSize();

			measurements = measurements.times(1.0/(double)numberOfRows);

			return new Result<T>((T) measurements, entities);
		} else {
			if(query.getWindowSize() == 0 )
				return new Result<T>((T) zero, entities);
			
			Matrix measurements;
			int numberOfRows = table.getSize();

			measurements = table.getAllMeasurements().row(numberOfRows - 1);
			for (int i = 1; i < query.getWindowSize(); ++i)
				measurements = measurements.plus(table.getAllMeasurements().row(
						numberOfRows - i - 1));

			return new Result<T>((T) measurements, entities);
		}

	}

	@SuppressWarnings("unchecked")
	private <T extends Matrix> Result<T> executeSelectQueryForEntity(
			Query<T> query) {
		MeasurementTable table = repository.get(query.getMetric());
		IModelEntity[] entities;

		if (table == null)
			return null;

		entities = new IModelEntity[1];
		entities[0] = query.getEntity();

		if (query.getAggregation().equals(Aggregation.LAST)) {
			Vector column = table.getColumn(query.getEntity().getName());
			Scalar last = column.row(column.rows() - 1);
			return new Result<T>((T) last, entities);
		}

		else if (query.getAggregation().equals(Aggregation.AVERAGE)) {
			if(query.getWindowSize() == 0 )
				return new Result<T>((T) scalar(0), entities);
			
			Vector entityData = table.getColumn(query.getEntity().getName());
			if (entityData.rows() > query.getWindowSize()) {
				entityData = entityData.slice(new Range(entityData.rows() - query
						.getWindowSize() , entityData.rows()));
			}
			double sum = sum(entityData);
			double avg = sum / entityData.rows();
			return new Result<T>((T) scalar(avg), entities);
		} else {
			if(query.getWindowSize() == 0 )
				return new Result<T>((T) scalar(0), entities);
			
			Vector entityData = table.getColumn(query.getEntity().getName());
			if (entityData.rows() > query.getWindowSize()) {
				entityData = entityData.slice(new Range(entityData.rows() - query
						.getWindowSize() , entityData.rows()));
			}
			double sum = sum(entityData);
			return new Result<T>((T) scalar(sum), entities);
		}
	}

	@Override
	public boolean hasNext(Metric metric) {
		MeasurementTable table = repository.get(metric);
		return table.hasNext();
	}

	@Override
	public Vector next(Metric metric) {
		MeasurementTable table = repository.get(metric);
		return table.next();
	}

}
