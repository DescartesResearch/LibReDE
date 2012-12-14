package edu.kit.ipd.descartes.redeem.ipopt.java.backend;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface Eval_H_CB extends Callback {

	public boolean execute(int n, Pointer x, boolean new_x, double obj_factor,
			int m, Pointer lambda, boolean new_lambda, int nele_hess,
			Pointer iRow, Pointer jCol, Pointer values, Pointer user_data);

}
