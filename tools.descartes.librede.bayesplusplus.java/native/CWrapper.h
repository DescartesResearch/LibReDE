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
 *  Created on: 05.04.2011
 *      Author: Simon Spinner
 */

#ifndef CWRAPPER_H_
#define CWRAPPER_H_

#include <BayesFilter/bayesFlt.hpp>
#include <BayesFilter/covFlt.hpp>
#include "bayesFltWrappers.hpp"

namespace BF = Bayesian_filter;

extern "C" {

BF::Linrz_predict_model* create_linrz_predict_model(std::size_t x_size, std::size_t q_size, H_callback hfunc);

void dispose_linrz_predict_model(BF::Linrz_predict_model* model);

void set_q(BF::Linrz_predict_model* predict_model, double* q, std::size_t x_size);

void set_G(BF::Linrz_predict_model* predict_model, double* G, std::size_t x_size);

void set_Fx(BF::Linrz_predict_model* predict_model, double* Fx, std::size_t x_size);

BF::Linrz_uncorrelated_observe_model* create_linrz_uncorrelated_observe_model(std::size_t x_size, std::size_t z_size, F_callback ffunc);

void dispose_linrz_uncorrelated_observe_model(BF::Linrz_uncorrelated_observe_model* model);

void set_Zv(BF::Linrz_uncorrelated_observe_model* observation_model, double* Zv, std::size_t z_size);

void set_Hx(BF::Linrz_uncorrelated_observe_model* observation_model, double* Hx, std::size_t x_size, std::size_t z_size);

BF::Covariance_scheme* create_covariance_scheme(std::size_t x_size);

void dispose_covariance_scheme(BF::Covariance_scheme* scheme);

int init_kalman(BF::Covariance_scheme* scheme, double* x_0, double* X_0, std::size_t x_size);

int predict(BF::Covariance_scheme* scheme, BF::Linrz_predict_model* predict_model);

int observe(BF::Covariance_scheme* scheme, BF::Linrz_uncorrelated_observe_model* observe_model, double* z, std::size_t z_size);

int update(BF::Covariance_scheme* scheme);

void get_x(BF::Covariance_scheme* scheme, double* x);

void set_x(BF::Covariance_scheme* scheme, double* x, std::size_t x_size);

void get_X(BF::Covariance_scheme* scheme, double* X);

void set_X(BF::Covariance_scheme* scheme, double* X, std::size_t x_size);

const char* get_last_error();

}

#endif /* CWRAPPER_H_ */
