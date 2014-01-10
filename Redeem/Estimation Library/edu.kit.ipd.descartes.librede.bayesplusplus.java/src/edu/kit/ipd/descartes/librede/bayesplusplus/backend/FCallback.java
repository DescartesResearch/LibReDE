package edu.kit.ipd.descartes.librede.bayesplusplus.backend;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface FCallback extends Callback {
	
	Pointer execute(Pointer x);

}
