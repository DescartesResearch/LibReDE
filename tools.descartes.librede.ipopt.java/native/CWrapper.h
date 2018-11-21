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
 * CWrapper.h
 *
 *  Created on: 24.05.2013
 *      Author: Simon Spinner
 */

#ifndef CWRAPPER_H_
#define CWRAPPER_H_

#include "IpStdCInterface.h"

extern "C" {

IpoptProblem IpOpt_CreateIpoptProblem(Index n, Number* x_L, Number* x_U, Index m, Number* g_L, Number* g_U,
		Index nele_jac, Index nele_hess, Index index_style, Eval_F_CB eval_f, Eval_G_CB eval_g, Eval_Grad_F_CB eval_grad_f,
		Eval_Jac_G_CB eval_jac_g, Eval_H_CB eval_h);

void IpOpt_FreeIpoptProblem(IpoptProblem ipopt_problem);

Bool IpOpt_AddIpoptStrOption(IpoptProblem ipopt_problem, char* keyword, char* val);

Bool IpOpt_AddIpoptNumOption(IpoptProblem ipopt_problem, char* keyword, Number val);

Bool IpOpt_AddIpoptIntOption(IpoptProblem ipopt_problem, char* keyword, Int val);

Bool IpOpt_OpenIpoptOutputFile(IpoptProblem ipopt_problem, char* file_name, Int print_level);

Bool IpOpt_SetIpoptProblemScaling(IpoptProblem ipopt_problem, Number obj_scaling, Number* x_scaling, Number* g_scaling);

Bool IpOpt_SetIntermediateCallback(IpoptProblem ipopt_problem, Intermediate_CB intermediate_cb);

enum ApplicationReturnStatus IpOpt_IpoptSolve(IpoptProblem ipopt_problem, Number* x, Number* g, Number* obj_val, Number* mult_g,
		Number* mult_x_L, Number* mult_x_U, UserDataPtr user_data);

}

#endif /* CWRAPPER_H_ */
