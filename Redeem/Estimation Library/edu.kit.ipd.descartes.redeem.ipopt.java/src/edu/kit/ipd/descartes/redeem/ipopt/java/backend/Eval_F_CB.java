package edu.kit.ipd.descartes.redeem.ipopt.java.backend;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface Eval_F_CB extends Callback {

	public boolean execute(int n, Pointer x, boolean new_x, Pointer obj_value,
			Pointer user_data);

}
