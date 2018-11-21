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
/*
 * CWrapper.cpp
 *
 *  Created on: 05.04.2011
 *      Author: Simon Spinner
 */

#include <iostream>
#include "CWrapper.h"
#include "debugHelper.hpp"

IpoptProblem IpOpt_CreateIpoptProblem(Index n, Number* x_L, Number* x_U, Index m, Number* g_L, Number* g_U,
		Index nele_jac, Index nele_hess, Index index_style, Eval_F_CB eval_f, Eval_G_CB eval_g, Eval_Grad_F_CB eval_grad_f,
		Eval_Jac_G_CB eval_jac_g, Eval_H_CB eval_h)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: CreateIpoptProblem(n=" << n << ", x_L, x_U, m=" << m << ", g_L, g_U, nele_jac=" << nele_jac << ", nele_hess=" << nele_hess << ", index_style=" << index_style
			<< ", eval_f, eval_g, eval_grad_f, eval_jac_g, eval_h)" << std::endl;
	std::cout << "INPUT VECTOR: x_L="; print_vector(x_L, n); std::cout << std::endl;
	std::cout << "INPUT VECTOR: x_U="; print_vector(x_U, n); std::cout << std::endl;
	std::cout << "INPUT VECTOR: g_L="; print_vector(g_L, m); std::cout << std::endl;
	std::cout << "INPUT VECTOR: g_U="; print_vector(g_U, m); std::cout << std::endl;
#endif
	IpoptProblem p = CreateIpoptProblem(n, x_L, x_U, m, g_L, g_U, nele_jac, nele_hess, index_style, eval_f, eval_g, eval_grad_f, eval_jac_g, eval_h);
#ifdef TRACE
	std::cout << "END FUNCTION CALL: CreateIpoptProblem" << std::endl;
#endif
	return p;
}

void IpOpt_FreeIpoptProblem(IpoptProblem ipopt_problem)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: FreeIpoptProblem(ipopt_problem)" << std::endl;
#endif
	FreeIpoptProblem(ipopt_problem);
#ifdef TRACE
	std::cout << "END FUNCTION CALL: FreeIpoptProblem" << std::endl;
#endif
}

Bool IpOpt_AddIpoptStrOption(IpoptProblem ipopt_problem, char* keyword, char* val)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: AddIpoptStrOption(ipopt_problem, keyword=" << keyword << ", val=" << val << ")" << std::endl;
#endif
	Bool ret = AddIpoptStrOption(ipopt_problem, keyword, val);
#ifdef TRACE
	std::cout << "END FUNCTION CALL: AddIpoptStrOption=" << ret << std::endl;
#endif
	return ret;
}

Bool IpOpt_AddIpoptNumOption(IpoptProblem ipopt_problem, char* keyword, Number val)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: AddIpoptNumOption(ipopt_problem, keyword=" << keyword << ", val=" << val << ")" << std::endl;
#endif
	Bool ret = AddIpoptNumOption(ipopt_problem, keyword, val);
#ifdef TRACE
	std::cout << "END FUNCTION CALL: AddIpoptNumOption=" << ret << std::endl;
#endif
	return ret;
}

Bool IpOpt_AddIpoptIntOption(IpoptProblem ipopt_problem, char* keyword, Int val)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: AddIpoptIntOption(ipopt_problem, keyword=" << keyword << ", val=" << val << ")" << std::endl;
#endif
	Bool ret = AddIpoptIntOption(ipopt_problem, keyword, val);
#ifdef TRACE
	std::cout << "END FUNCTION CALL: AddIpoptIntOption=" << ret << std::endl;
#endif
	return ret;
}

Bool IpOpt_OpenIpoptOutputFile(IpoptProblem ipopt_problem, char* file_name, Int print_level)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: OpenIpoptOutputFile(ipopt_problem, file_name=" << file_name << ", print_level=" << print_level << ")" << std::endl;
#endif
	Bool ret = OpenIpoptOutputFile(ipopt_problem, file_name, print_level);
#ifdef TRACE
	std::cout << "END FUNCTION CALL: OpenIpoptOutputFile=" << ret << std::endl;
#endif
	return ret;
}

Bool IpOpt_SetIpoptProblemScaling(IpoptProblem ipopt_problem, Number obj_scaling, Number* x_scaling, Number* g_scaling)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: SetIpoptProblemScaling(ipopt_problem, intermediate_cb)" << std::endl;
#endif
	Bool ret = SetIpoptProblemScaling(ipopt_problem, obj_scaling, x_scaling, g_scaling);
#ifdef TRACE
	std::cout << "END FUNCTION CALL: SetIpoptProblemScaling=" << ret << std::endl;
#endif
	return ret;
}

Bool IpOpt_SetIntermediateCallback(IpoptProblem ipopt_problem, Intermediate_CB intermediate_cb)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: SetIntermediateCallback(ipopt_problem, intermediate_cb)" << std::endl;
#endif
	Bool ret = SetIntermediateCallback(ipopt_problem, intermediate_cb);
#ifdef TRACE
	std::cout << "END FUNCTION CALL: SetIntermediateCallback=" << ret << std::endl;
#endif
	return ret;
}

enum ApplicationReturnStatus IpOpt_IpoptSolve(IpoptProblem ipopt_problem, Number* x, Number* g, Number* obj_val, Number* mult_g,
		Number* mult_x_L, Number* mult_x_U, UserDataPtr user_data)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: IpoptSolve(ipopt_problem, x, g, obj_val=" << *obj_val << ", mult_g, mult_x_L, mult_x_U, user_data)" << std::endl;
//	std::cout << "INPUT VECTOR: x="; print_vector(x, ipopt_problem->n); std::cout << std::endl;
//	std::cout << "INPUT VECTOR: g="; print_vector(g, ipopt_problem->m); std::cout << std::endl;
//	std::cout << "INPUT VECTOR: mult_g="; print_vector(mult_g, ipopt_problem->m); std::cout << std::endl;
//	std::cout << "INPUT VECTOR: mult_x_L="; print_vector(mult_x_L, ipopt_problem->n); std::cout << std::endl;
//	std::cout << "INPUT VECTOR: mult_x_U="; print_vector(mult_x_U, ipopt_problem->n); std::cout << std::endl;
#endif
	enum ApplicationReturnStatus status = IpoptSolve(ipopt_problem, x, g, obj_val, mult_g, mult_x_L, mult_x_U, user_data);
#ifdef TRACE
	std::cout << "END FUNCTION CALL: IpoptSolve=" << status << std::endl;
#endif
	return status;
}


