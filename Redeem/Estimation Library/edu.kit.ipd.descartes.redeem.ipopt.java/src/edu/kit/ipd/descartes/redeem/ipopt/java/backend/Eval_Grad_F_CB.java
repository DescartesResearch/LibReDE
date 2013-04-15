package edu.kit.ipd.descartes.redeem.ipopt.java.backend;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface Eval_Grad_F_CB extends Callback {

	public boolean eval_grad_f(int n, Pointer x, boolean new_x, Pointer grad_f,
			Pointer user_data);

}
