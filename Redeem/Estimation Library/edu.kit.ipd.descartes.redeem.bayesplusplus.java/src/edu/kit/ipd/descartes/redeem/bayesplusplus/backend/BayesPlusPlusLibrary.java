package edu.kit.ipd.descartes.redeem.bayesplusplus.backend;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface BayesPlusPlusLibrary extends Library {
	
	BayesPlusPlusLibrary INSTANCE = (BayesPlusPlusLibrary) Native.loadLibrary(
			"BayesPlusPlus", BayesPlusPlusLibrary.class);
	
	Pointer create_linrz_predict_model(int x_size, int q_size, Callback ffunc);

	void dispose_linrz_predict_model(Pointer model);

	void set_q(Pointer predict_model, Pointer q, int x_size);

	void set_G(Pointer predict_model, Pointer G, int x_size);

	void set_Fx(Pointer predict_model, Pointer Fx, int x_size);

	Pointer create_linrz_uncorrelated_observe_model(int x_size, int z_size, Callback hfunc);

	void dispose_linrz_uncorrelated_observe_model(Pointer model);

	void set_Zv(Pointer observation_model, Pointer Zv, int z_size);

	void set_Hx(Pointer observation_model, Pointer Hx, int x_size, int z_size);

	Pointer create_covariance_scheme(int x_size);

	void dispose_covariance_scheme(Pointer scheme);

	void init_kalman(Pointer scheme, Pointer x_0, Pointer X_0, int x_size);

	void predict(Pointer scheme, Pointer predict_model);

	void observe(Pointer scheme, Pointer observe_model, Pointer z, int z_size);

	void update(Pointer scheme);
	
	void get_x(Pointer scheme, Pointer x);

}
