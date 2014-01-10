package edu.kit.ipd.descartes.librede.ipopt.java.backend;

import java.lang.ref.WeakReference;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

public enum IpoptOptionKeyword {

	TOL("tol"),
	PRINT_LEVEL("print_level"),
	MU_STRATEGY("mu_strategy"),
	NLP_LOWER_BOUND_INF("nlp_lower_bound_inf"),
	NLP_UPPER_BOUND_INF("nlp_upper_bound_inf"),
	BOUND_PUSH("bound_push"),
	BOUND_FRAC("bound_frac"),
	WARM_START_INIT_POINT("warm_start_init_point"),
	CHECK_DERIVATIVES_FOR_NANINF("check_derivatives_for_naninf"),
	DERIVATIVE_TEST("derivative_test"),
	DERIVATIVE_TEST_PRINT_ALL("derivative_test_print_all");

	private String keyword;
	/*
	 * Ensure that this reference does not block the GC from freeing the native
	 * memory.
	 */
	private WeakReference<Pointer> nativeString;

	private IpoptOptionKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Pointer toNativeString() {
		if (nativeString == null || nativeString.get() == null) {
			Pointer p = new Memory(keyword.length() + 1);
			p.setString(0, keyword);
			nativeString = new WeakReference<Pointer>(p);
		}
		return nativeString.get();
	}
}
