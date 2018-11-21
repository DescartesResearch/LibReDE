/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2018, by Simon Spinner, Johannes Grohmann
 *  and Contributors.
 *
 * Project Info:   http://www.descartes-research.net/
 *
 * All rights reserved. This software is made available under the terms of the
 * Eclipse Public License (EPL) v1.0 as published by the Eclipse Foundation
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * This software is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the Eclipse Public License (EPL)
 * for more details.
 *
 * You should have received a copy of the Eclipse Public License (EPL)
 * along with this software; if not visit http://www.eclipse.org or write to
 * Eclipse Foundation, Inc., 308 SW First Avenue, Suite 110, Portland, 97204 USA
 * Email: license (at) eclipse.org
 *
 * [Java is a trademark or registered trademark of Sun Microsystems, Inc.
 * in the United States and other countries.]
 */
package tools.descartes.librede.ipopt.java.backend;

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
	DERIVATIVE_TEST_PRINT_ALL("derivative_test_print_all"),
	MAX_ITER("max_iter");

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
