package edu.kit.ipd.descartes.librede.ipopt.java.backend;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface Eval_G_CB extends Callback {

	public boolean eval_g(int n, Pointer x, boolean new_x, int m, Pointer g,
			Pointer user_data);

}
