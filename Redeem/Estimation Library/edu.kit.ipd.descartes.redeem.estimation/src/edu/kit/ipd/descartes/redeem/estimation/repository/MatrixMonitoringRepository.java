package edu.kit.ipd.descartes.redeem.estimation.repository;

import java.util.HashMap;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
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

	HashMap<String, MeasurementTable> repository = new HashMap<String, MeasurementTable>();

	public MatrixMonitoringRepository() {

	}

	@Override
	public <T extends Matrix> Result<T> execute(Query<T> query) {

		if (query.getType().equals(Query.Type.ALL_RESOURCES)
				|| query.getType().equals(Query.Type.ALL_SERVICES))
			return executeSelectQueryForAllEntities(query);
		else
			return  executeSelectQueryForEntity(query);
	}

	public void createMesurementTable(MeasurementTable measurementTable) {
		if (measurementTable != null
				&& !repository.containsKey(measurementTable.getMetric()
						.toString()))
			repository.put(measurementTable.getMetric().toString(),
					measurementTable);
	}

	private <T extends Matrix> Result<T> executeSelectQueryForAllEntities(
			Query<T> query) {
		MeasurementTable table = repository.get(query.getMetric().toString());
		IModelEntity[] entities;

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
			return  new Result<T>((T) table.getLastMeasurement(),
					entities);
		else if (query.getAggregation().equals(Aggregation.AVERAGE)) {
			double sum = sum(table.getAllMesurenments());
			double avg = sum / (table.columns() * table.getSize());
			return   new Result<T>((T) scalar(avg), entities);
		} else {
			double sum = sum(table.getAllMesurenments());
			return  new Result<T>((T) scalar(sum), entities);
		}

	}

	private <T extends Matrix> Result<T> executeSelectQueryForEntity(
			Query<T> query) {
		MeasurementTable table = repository.get(query.getMetric().toString());
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
			double sum = sum(table.getColumn(query.getEntity().getName()));
			double avg = sum / (table.columns() * table.getSize());
			return  new Result<T>((T) scalar(avg), entities);
		} else {
			double sum = sum(table.getColumn(query.getEntity().getName()));
			return  new Result<T>((T) scalar(sum), entities);
		}
	}

}
