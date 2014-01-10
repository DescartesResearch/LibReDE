/**
 * ==============================================
 *  LibReDE : Library for Resource Demand Estimation
 * ==============================================
 *
 * (c) Copyright 2013-2014, by Simon Spinner and Contributors.
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
package edu.kit.ipd.descartes.librede.ipopt.java.backend;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;

public interface IpoptLibrary extends Library {

	public static IpoptLibrary INSTANCE = (IpoptLibrary) Native.loadLibrary(
			"IpOpt", IpoptLibrary.class);

	public static final int IP_SOLVE_SUCCEEDED = 0;

	public static final int IP_ACCEPTABLE_LEVEL = 1;

	public static final int IP_INFEASIBLE_PROBLEM = 2;

	public static final int IP_SEARCH_DIRECTION_TOO_SMALL = 3;

	public static final int IP_DIVERGING_ITERATES = 4;

	public static final int IP_USER_REQUESTED_STOP = 5;

	public static final int IP_FEASIBLE_POINT_FOUND = 6;

	public static final int IP_ITERATION_EXCEEDED = -1;

	public static final int IP_RESTORATION_FAILED = -2;

	public static final int IP_ERROR_IN_STEP_COMPUTATION = -3;

	public static final int IP_CPUTIME_EXCEEDED = -4;

	public static final int IP_NOT_ENOUGH_DEGREES_OF_FRE = -10;

	public static final int IP_INVALID_PROBLEM_DEFINITION = -11;

	public static final int IP_INVALID_OPTION = -12;

	public static final int IP_INVALID_NUMBER_DETECTED = -13;

	public static final int IP_UNRECOVERABLE_EXCEPTION = -100;

	public static final int IP_NON_IPOPT_EXCEPTION = -101;

	public static final int IP_INSUFFICIENT_MEMORY = -102;

	public static final int IP_INTERNAL_ERROR = -199;

	public static final int IP_REGULAR_MODE = 0;

	public static final int IP_RESTORATION_PHASE_MODE = 1;

	public Pointer IpOpt_CreateIpoptProblem(int n, Pointer x_L, Pointer x_U, int m,
			Pointer g_L, Pointer g_U, int nele_jac, int nele_hess,
			int index_style, Callback eval_f, Callback eval_g,
			Callback eval_grad_f, Callback eval_jac_g, Callback eval_h);

	public void IpOpt_FreeIpoptProblem(Pointer ipopt_problem);

	boolean IpOpt_AddIpoptStrOption(Pointer ipopt_problem, Pointer keyword, Pointer val);

	boolean IpOpt_AddIpoptNumOption(Pointer ipopt_problem, Pointer keyword, double val);

	boolean IpOpt_AddIpoptIntOption(Pointer ipopt_problem, Pointer keyword, int val);

	boolean IpOpt_OpenIpoptOutputFile(Pointer ipopt_problem, Pointer file_name,
			int print_level);

	boolean IpOpt_SetIpoptProblemScaling(Pointer ipopt_problem, double obj_scaling,
			Pointer x_scaling, Pointer g_scaling);

	boolean IpOpt_SetIntermediateCallback(Pointer ipopt_problem,
			Callback intermediate_cb);

	int IpOpt_IpoptSolve(Pointer ipopt_problem, Pointer x, Pointer g,
			DoubleByReference obj_val, Pointer mult_g, Pointer mult_x_L,
			Pointer mult_x_U, Pointer user_data);

}
