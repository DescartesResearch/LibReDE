package edu.kit.ipd.descartes.redeem.nativehelper;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class NativeHelper {
	
	private static final int DOUBLE_BYTE_SIZE = 8;
	
	public static Pointer allocateDoubleArray(int size) {
		return new Memory(size * DOUBLE_BYTE_SIZE);
	}
	
	public static void setDoubleArray(Pointer array, int idx, double value) {
		array.setDouble(idx * DOUBLE_BYTE_SIZE, value);
	}

}
