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
/*
 * CWrapper.cpp
 *
 *  Created on: 05.04.2011
 *      Author: Simon Spinner
 */

#include <iostream>
#include "CWrapper.h"
#include "debugHelper.hpp"

const int ERROR = -1;
const int OK = 0;

static const char* last_error = 0;

BF::Linrz_predict_model* create_linrz_predict_model(std::size_t x_size, std::size_t q_size, H_callback hfunc)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: create_linrz_predict_model(" << x_size << ", " << q_size << ", hfunc)" << std::endl;
#endif

	BF::Linrz_predict_model* model = 0;
	try {
		model = new Linrz_predict_model_java_wrapper(x_size, q_size, hfunc);
	}
	catch(const std::exception& e)
	{
		last_error = e.what();
	}

#ifdef TRACE
	std::cout << "END FUNCTION CALL: create_linrz_predict_model" << std::endl;
#endif
	return model;
}

void dispose_linrz_predict_model(BF::Linrz_predict_model* model)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: dispose_linrz_predict_model(model)" << std::endl;
#endif

	delete model;

#ifdef TRACE
	std::cout << "END FUNCTION CALL: dispose_linrz_predict_model" << std::endl;
#endif
}

void set_q(BF::Linrz_predict_model* predict_model, double* q, std::size_t x_size)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: set_q(model, q, x_size=" << x_size << ")" << std::endl;
	std::cout << "INPUT VECTOR: q="; print_vector(q, x_size); std::cout << std::endl;
#endif

	std::copy(q, q + x_size, predict_model->q.begin());

#ifdef TRACE
	std::cout << "TRANSFORMED VECTOR: q="; print_vector(predict_model->q, x_size); std::cout << std::endl;
#endif
#ifdef ASSERT
	assert_transformed_vector(predict_model->q, q);
#endif
#ifdef TRACE
	std::cout << "END FUNCTION CALL: set_q" << std::endl;
#endif
}

void set_G(BF::Linrz_predict_model* predict_model, double* G, std::size_t x_size)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: set_G(model, G, x_size=" << x_size << ")" << std::endl;
	std::cout << "INPUT MATRIX: G="; print_matrix(G, x_size, x_size); std::cout << std::endl;
#endif

	std::copy(G, G + x_size*x_size, predict_model->G.data().begin());

#ifdef TRACE
	std::cout << "TRANSFORMED MATRIX: G="; print_matrix(predict_model->G, x_size, x_size); std::cout << std::endl;
#endif
#ifdef ASSERT
	assert_transformed_matrix(predict_model->G, G);
#endif
#ifdef TRACE
	std::cout << "END FUNCTION CALL: set_G" << std::endl;
#endif
}

void set_Fx(BF::Linrz_predict_model* predict_model, double* Fx, std::size_t x_size)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: set_Fx(model, Fx, x_size=" << x_size << ")" << std::endl;
	std::cout << "INPUT MATRIX: Fx="; print_matrix(Fx, x_size, x_size); std::cout << std::endl;
#endif

	std::copy(Fx, Fx + x_size*x_size, predict_model->Fx.data().begin());

#ifdef TRACE
	std::cout << "TRANSFORMED MATRIX: Fx="; print_matrix(predict_model->Fx, x_size, x_size); std::cout << std::endl;
#endif
#ifdef ASSERT
	assert_transformed_matrix(predict_model->Fx, Fx);
#endif
#ifdef TRACE
	std::cout << "END FUNCTION CALL: set_Fx" << std::endl;
#endif
}

BF::Linrz_uncorrelated_observe_model* create_linrz_uncorrelated_observe_model(std::size_t x_size, std::size_t z_size, F_callback ffunc)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: create_linrz_uncorrelated_observe_model(x_size=" << x_size << ", z_size=" << z_size << ", ffunc)" << std::endl;
#endif

	BF::Linrz_uncorrelated_observe_model* model = 0;
	try
	{
		model = new Linrz_uncorrelated_observe_model_java_wrapper(x_size, z_size, ffunc);
	}
	catch(const std::exception& e)
	{
		last_error = e.what();
	}

#ifdef TRACE
	std::cout << "END FUNCTION CALL: create_linrz_uncorrelated_observe_mode" << std::endl;
#endif
	return model;
}

void dispose_linrz_uncorrelated_observe_model(BF::Linrz_uncorrelated_observe_model* model)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: dispose_linrz_uncorrelated_observe_model(model)" << std::endl;
#endif

	delete model;

#ifdef TRACE
	std::cout << "END FUNCTION CALL: dispose_linrz_uncorrelated_observe_model" << std::endl;
#endif
}

void set_Zv(BF::Linrz_uncorrelated_observe_model* observation_model, double* Zv, std::size_t z_size)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: set_Zv(observation_model, Zv, z_size=" << z_size << ")" << std::endl;
	std::cout << "INPUT VECTOR: Zv="; print_vector(Zv, z_size); std::cout << std::endl;
#endif

	std::copy(Zv, Zv + z_size, observation_model->Zv.begin());

#ifdef TRACE
	std::cout << "TRANSFORMED VECTOR: Zv="; print_vector(observation_model->Zv, z_size); std::cout << std::endl;
#endif
#ifdef ASSERT
	assert_transformed_vector(observation_model->Zv, Zv);
#endif
#ifdef TRACE
	std::cout << "END FUNCTION CALL: set_Zv" << std::endl;
#endif
}

void set_Hx(BF::Linrz_uncorrelated_observe_model* observation_model, double* Hx, std::size_t x_size, std::size_t z_size)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: set_Hx(observation_model, Hx, x_size=" << x_size << ", z_size=" << z_size << ")" << std::endl;
	std::cout << "INPUT MATRIX: Hx="; print_matrix(Hx, z_size, x_size); std::cout << std::endl;
#endif

	std::copy(Hx, Hx + z_size*x_size, observation_model->Hx.data().begin());

#ifdef TRACE
	std::cout << "TRANSFORMED MATRIX: Hx="; print_matrix(observation_model->Hx, z_size, x_size); std::cout << std::endl;
#endif
#ifdef ASSERT
	assert_transformed_matrix(observation_model->Hx, Hx);
#endif
#ifdef TRACE
	std::cout << "END FUNCTION CALL: set_Hx" << std::endl;
#endif
}

BF::Covariance_scheme* create_covariance_scheme(std::size_t x_size)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: create_covariance_scheme(x_size=" << x_size << ")" << std::endl;
#endif

	BF::Covariance_scheme* scheme = 0;
	try {
		scheme = new BF::Covariance_scheme(x_size);
	}
	catch(const std::exception& e)
	{
		last_error = e.what();
	}

#ifdef TRACE
	std::cout << "END FUNCTION CALL: create_covariance_scheme" << std::endl;
#endif
	return scheme;
}

void dispose_covariance_scheme(BF::Covariance_scheme* scheme)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: dispose_covariance_scheme(scheme)" << std::endl;
#endif
	delete scheme;
#ifdef TRACE
	std::cout << "END FUNCTION CALL: dispose_covariance_scheme" << std::endl;
#endif
}

int init_kalman(BF::Covariance_scheme* scheme, double* x_0, double* X_0, std::size_t x_size)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: init_kalman(scheme, x_0, X_0, x_size=" << x_size << ")" << std::endl;
	std::cout << "INPUT VECTOR: x_0="; print_vector(x_0, x_size); std::cout << std::endl;
	std::cout << "INPUT MATRIX: X_0="; print_matrix(X_0, x_size, x_size); std::cout << std::endl;
#endif

	FM::Vec x0_Vec(x_size);
	FM::Matrix X0_Mat(x_size, x_size);

	std::copy(x_0, x_0 + x_size, x0_Vec.begin());
	std::copy(X_0, X_0 + x_size*x_size, X0_Mat.data().begin());

#ifdef TRACE
	std::cout << "TRANSFORMED VECTOR: x_0="; print_vector(x0_Vec, x_size); std::cout << std::endl;
	std::cout << "TRANSFORMED MATRIX: X_0="; print_matrix(X0_Mat, x_size, x_size); std::cout << std::endl;
#endif
#ifdef ASSERT
	assert_transformed_vector(x0_Vec, x_0);
	assert_transformed_matrix(X0_Mat, X_0);
#endif

	FM::SymMatrix X0_Mat_sym(X0_Mat);

	try
	{
		scheme->init_kalman(x0_Vec, X0_Mat_sym);
	}
	catch(const std::exception& e) {
		last_error = e.what();
		return ERROR;
	}

#ifdef TRACE
	std::cout << "END FUNCTION CALL: init_kalman" << std::endl;
#endif
	return OK;
}

int predict(BF::Covariance_scheme* scheme, BF::Linrz_predict_model* predict_model)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: predict(scheme, predict_model)" << std::endl;
#endif

	try
	{
		scheme->predict(*predict_model);
	}
	catch(const std::exception& e)
	{
		last_error = e.what();
		return ERROR;
	}

#ifdef TRACE
	std::cout << "END FUNCTION CALL: predict" << std::endl;
#endif
	return OK;
}

int observe(BF::Covariance_scheme* scheme, BF::Linrz_uncorrelated_observe_model* observe_model, double* z, std::size_t z_size)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: observe(scheme, observe_model, z, z_size=" << z_size << ")" << std::endl;
	std::cout << "INPUT VECTOR: z="; print_vector(z, z_size); std::cout << std::endl;
#endif

	FM::Vec zVec(z_size);
	std::copy(z, z + z_size, zVec.begin());

#ifdef TRACE
	std::cout << "TRANSFORMED VECTOR: z="; print_vector(zVec, z_size); std::cout << std::endl;
#endif
#ifdef ASSERT
	assert_transformed_vector(zVec, z);
#endif

	try
	{
		scheme->observe(*observe_model, zVec);
	}
	catch(const std::exception& e)
	{
		last_error = e.what();
		return ERROR;
	}

#ifdef TRACE
	std::cout << "END FUNCTION CALL: observe" << std::endl;
#endif
	return OK;
}

int update(BF::Covariance_scheme* scheme)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: observe(scheme)" << std::endl;
#endif

	try
	{
		scheme->update();
	}
	catch(const std::exception& e)
	{
		last_error = e.what();
		return ERROR;
	}

#ifdef TRACE
	std::cout << "END FUNCTION CALL: update" << std::endl;
#endif
	return OK;
}

void get_x(BF::Covariance_scheme* scheme, double* x)
{
#ifdef TRACE
	std::cout << "FUNCTION CALL: get_x(scheme)" << std::endl;
#endif

	FM::Vec xVec = scheme->x;
	std::copy(xVec.begin(), xVec.end(), x);

#ifdef TRACE
	std::cout << "OUTPUT VECTOR: x="; print_vector(xVec, xVec.size()); std::cout << std::endl;
	std::cout << "TRANSFOMRED VECTOR: x="; print_vector(x, xVec.size()); std::cout << std::endl;
#endif
#ifdef ASSERT
	assert_transformed_vector(xVec, x);
#endif
#ifdef TRACE
	std::cout << "END FUNCTION CALL: get_x" << std::endl;
#endif

}

const char* get_last_error()
{
	const char* temp = last_error;
	last_error = 0;
	return temp;
}


