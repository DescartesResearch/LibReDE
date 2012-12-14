package edu.kit.ipd.descartes.redeem.bayesplusplus.util;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public class NativeHelper {
	
	public static Pointer allocateDoubleArray(int size) {
		return new Memory(size * 8);
	}

}
