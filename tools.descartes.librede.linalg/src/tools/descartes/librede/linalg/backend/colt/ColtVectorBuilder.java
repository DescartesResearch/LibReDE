package tools.descartes.librede.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;

public class ColtVectorBuilder extends VectorBuilder {
	
	private final DoubleMatrix1D values;
	private int last;
	private final int maxCapacity;
	private boolean setAllowed = true;
	
	public ColtVectorBuilder(int maxCapacity) {
		this.maxCapacity = maxCapacity;
		values = new DenseDoubleMatrix1D(maxCapacity);
		last = 0;
	}
	
	@Override
	public void add(double value) {
		if (last == maxCapacity) {
			throw new ArrayIndexOutOfBoundsException();
		}
		values.set(last, value);
		last++;
	}
	
	@Override
	public void set(int idx, double value) {
		if (setAllowed) {
			values.set(idx, value);
		} else {
			throw new IllegalStateException();
		}		
	}
	
	@Override
	public Vector toVector() {
		setAllowed = false; // from now on only append is allowed, otherwise we would allow side-effects!
		if (last < maxCapacity) {
			return new ColtVector(values.viewPart(0, last));
		}
		return new ColtVector(values);
	}

}
