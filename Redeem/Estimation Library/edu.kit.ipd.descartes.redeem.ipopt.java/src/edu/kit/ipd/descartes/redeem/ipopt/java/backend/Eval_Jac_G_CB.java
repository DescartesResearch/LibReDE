package edu.kit.ipd.descartes.redeem.ipopt.java.backend;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface Eval_Jac_G_CB extends Callback {

	public boolean eval_jac_g(int n, Pointer x, boolean new_x, int m,
			int nele_jac, Pointer iRow, Pointer jCol, Pointer values,
			Pointer user_data);

}
