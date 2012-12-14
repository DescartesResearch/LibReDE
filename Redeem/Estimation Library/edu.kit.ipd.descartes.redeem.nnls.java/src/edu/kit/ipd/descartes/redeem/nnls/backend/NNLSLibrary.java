package edu.kit.ipd.descartes.redeem.nnls.backend;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

public interface NNLSLibrary extends Library {

	public static NNLSLibrary INSTANCE = (NNLSLibrary) Native.loadLibrary(
			"NNLS", NNLSLibrary.class);

	public void nnls_(Pointer a, IntByReference mda, IntByReference m, IntByReference n, Pointer b,
			Pointer x, DoubleByReference rnorm, Pointer w, Pointer zz, Pointer index,
			IntByReference mode);

}
