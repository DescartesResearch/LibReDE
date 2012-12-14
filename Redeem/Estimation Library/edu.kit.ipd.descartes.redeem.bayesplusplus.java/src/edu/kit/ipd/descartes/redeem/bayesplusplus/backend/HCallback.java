package edu.kit.ipd.descartes.redeem.bayesplusplus.backend;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface HCallback extends Callback {
	
	Pointer execute(Pointer x);

}
