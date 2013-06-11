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
