package edu.kit.ipd.descartes.linalg;

import static edu.kit.ipd.descartes.linalg.LinAlg.matrix;

import java.util.ArrayList;
import java.util.List;

public class MatrixBuilder {
	
	private int columns;
	private List<double[]> buffer = new ArrayList<double[]>();
	
	public MatrixBuilder(int columns) {
		this.columns = columns;
	}
	
	public void addRow(Vector values) {
		addRow(values.toArray1D());
	}
	
	public void addRow(double...values) {
		if (values.length != columns) {
			throw new IllegalArgumentException();
		}
		buffer.add(values);
	}
	
	public Matrix toMatrix() {
		double[][] values = buffer.toArray(new double[buffer.size()][]);
		return matrix(values);
	}

}
