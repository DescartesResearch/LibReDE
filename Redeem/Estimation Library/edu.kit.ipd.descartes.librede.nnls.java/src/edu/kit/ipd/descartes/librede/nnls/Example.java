package edu.kit.ipd.descartes.librede.nnls;

import com.sun.jna.Memory;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;

import edu.kit.ipd.descartes.librede.nnls.backend.NNLSLibrary;

public class Example {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary("NNLS");
		
		// Inputs
		IntByReference mda = new IntByReference(4);
		IntByReference m = new IntByReference(4); // mda == m
		IntByReference n = new IntByReference(2);
		Memory a = new Memory(8 * mda.getValue() * n.getValue()); // 8 == sizeof(double)
		a.write(0, new double[] {1, 2, 3, 4, 1, 4, 9, 16}, 0,  mda.getValue() * n.getValue()); // Fortran expects column-order array!		
		Memory b = new Memory(8 * mda.getValue());
		b.write(0, new double[] {0.6, 2.2, 4.8, 8.4}, 0, mda.getValue());
		
		// Outputs
		Memory x = new Memory(8 * n.getValue());
		DoubleByReference rnorm = new DoubleByReference();
		IntByReference mode = new IntByReference();
		
		// Temporary working arrays
		Memory w = new Memory(8 * n.getValue());
		Memory zz = new Memory(8 * m.getValue());
		Memory index = new Memory(4 * n.getValue());		
		
		NNLSLibrary.INSTANCE.nnls_(a, mda, m, n, b, x, rnorm, w, zz, index, mode);
		
		double[] res = new double[n.getValue()];
		x.read(0, res, 0, res.length);
		for (int i = 0; i < res.length; i++) {
			System.out.println(res[i]);
		}
		System.out.println(rnorm.getValue());
		System.out.println(mode.getValue());
	
	}

}
