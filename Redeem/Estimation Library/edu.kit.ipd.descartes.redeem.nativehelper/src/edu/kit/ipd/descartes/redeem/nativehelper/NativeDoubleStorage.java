package edu.kit.ipd.descartes.redeem.nativehelper;

import com.sun.jna.Pointer;

import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class NativeDoubleStorage implements DoubleStorage {
	
	private Pointer nativeMemory;
	
	public NativeDoubleStorage(Pointer memory) {
		super();
		this.nativeMemory = memory;
	}

	@Override
	public void read(double[] elements) {
		nativeMemory.read(0, elements, 0, elements.length);
	}

	@Override
	public void write(double[] elements) {
		nativeMemory.write(0, elements, 0, elements.length);
	}

}
