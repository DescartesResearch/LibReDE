package tools.descartes.librede.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;

public class ColtVectorBuilder extends VectorBuilder {
	
	private DoubleMatrix1D values;
	private int last;
	private int capacity;
	
	public ColtVectorBuilder(int capacity) {
		this.capacity = capacity;
		values = new DenseDoubleMatrix1D(capacity);
		last = 0;
	}
	
	@Override
	public void add(double value) {
		if (last == capacity) {
			grow();
		}
		values.set(last, value);
		last++;
	}
	
	@Override
	public Vector toVector() {
		if (last < capacity) {
			return new ColtVector(values.viewPart(0, last));
		}
		return new ColtVector(values);
	}
	
	private void grow() {
		capacity = capacity * 2;
		DoubleMatrix1D newValues = new DenseDoubleMatrix1D(capacity);
		newValues.viewPart(0, values.size()).assign(values);
		values = newValues;
	}
	

}
