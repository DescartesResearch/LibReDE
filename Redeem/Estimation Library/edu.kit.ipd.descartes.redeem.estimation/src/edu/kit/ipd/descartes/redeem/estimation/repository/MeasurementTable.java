package edu.kit.ipd.descartes.redeem.estimation.repository;

import java.util.Arrays;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.system.IModelEntity;
import static edu.kit.ipd.descartes.linalg.LinAlg.*;

/**
 * This class is a container that holds measurement data. Each column represents
 * the measured data for a specific resource/service.
 * 
 * @author Mehran Saliminia
 * 
 * 
 */
public class MeasurementTable {

	// entityNames contains the names of resources/services
	private String[] entityNames;
	private Matrix table;
	private Metric metric;

	public MeasurementTable(IModelEntity[] entities, Metric metric) {
		this.entityNames = new String[entities.length];
		for (int i = 0; i < entities.length; ++i)
			this.entityNames[i] = entities[i].getName();
		this.metric = metric;
	}

	public int getSize() {
		return table.rows();
	}

	// returns the last measurement for all resources/services
	public Vector getLastMeasurement() {
		// returns the last row
		return table.row(table.rows() - 1);
	}

	// returns measurements for specific resource/service
	public Vector getColumn(String entityNames) {
		int index = Arrays.asList(this.entityNames).indexOf(entityNames);
		if (index >= 0)
			return table.column(index);
		else
			return null;
	}

	// add new measurement for all services/resources
	public void addRow(Vector newMesurement) {
		if (newMesurement.rows() > entityNames.length)
			throw new IndexOutOfBoundsException("Column Index out of range.");
		if(table != null)
			table = vertcat(table,matrix(row(newMesurement.toArray1D())));
		else
			table = matrix(row(newMesurement.toArray1D()));
	}

	public Metric getMetric() {
		return metric;
	}

	public Matrix getAllMesurenments() {
		return table;
	}

	public String[] getEntityNames() {
		return this.entityNames;
	}

	public int columns() {
		return table.columns();
	}

}