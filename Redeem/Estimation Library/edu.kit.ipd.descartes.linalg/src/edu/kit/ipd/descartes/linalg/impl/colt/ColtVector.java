package edu.kit.ipd.descartes.linalg.impl.colt;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class ColtVector extends Vector {
	
	protected static class FlatArrayVector extends DenseDoubleMatrix1D {

		public FlatArrayVector(int rows) {
			super(rows);
		}		
		
		public FlatArrayVector(int size, double[] elements, int zero, int stride) {
			super(size, elements, zero, stride);
		}

		public void readFrom(DoubleStorage storage) {
			storage.read(elements);
		}
		
		public void writeTo(DoubleStorage storage) {
			storage.write(elements);
		}
	
	}
	
	protected FlatArrayVector content;
	
	protected ColtVector(FlatArrayVector content) {
		this.content = content;
	}
	
	public ColtVector(int rows) {
		content = new FlatArrayVector(rows);
	}
	
	@Override
	public void assign(double... d) {
		content.assign(d);		
	}
	
	@Override
	public void set(int row, double value) {
		content.set(row, value);		
	}

	@Override
	public double get(int row) {
		return content.get(row);
	}

	@Override
	public int rowCount() {
		return content.size();
	}
	
	@Override
	public double multiply(Vector b) {
		return content.zDotProduct(((ColtVector)b).content);
	}
	
	@Override
	public void mapMultiplyToSelf(double d) {
		for (int i = 0; i < content.size(); i++) {
			content.setQuick(i, content.getQuick(i) * d);
		}
	}
	
	@Override
	public double sum() {
		return content.zSum();
	}

	@Override
	public double[] toArray() {
		return content.toArray();
	}

	@Override
	public void readFrom(DoubleStorage storage) {
		content.readFrom(storage);
	}

	@Override
	public void writeTo(DoubleStorage storage) {
		content.writeTo(storage);		
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < content.size(); i++) {
			if (i > 0) {
				builder.append("; ");
			}
			builder.append(content.get(i));
		}
		builder.append("]");
		return builder.toString();
	}
}
