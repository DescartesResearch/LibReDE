package edu.kit.ipd.descartes.librede.ipopt.java.backend;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;

public interface Intermediate_CB extends Callback {

	public boolean execute(int alg_mod, int iter_count, double obj_value,
			double inf_pr, double inf_du, double mu, double d_norm,
			double regularization_size, double alpha_du, double alpha_pr,
			int ls_trials, Pointer user_data);

}
