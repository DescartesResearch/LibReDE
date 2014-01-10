package edu.kit.ipd.descartes.librede.ipopt.java.backend;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface Eval_H_CB extends Callback {

	public boolean eval_h(int n, Pointer x, boolean new_x, double obj_factor,
			int m, Pointer lambda, boolean new_lambda, int nele_hess,
			Pointer iRow, Pointer jCol, Pointer values, Pointer user_data);

}
