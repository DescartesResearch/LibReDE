/*
 * bayesFltWrappers.hpp
 *
 *
 */

#ifndef _DESCARTES_STATEMODEL_H_
#define _DESCARTES_STATEMODEL_H_

#include <algorithm>
#include <iterator>
#include <BayesFilter/bayesFlt.hpp>


namespace BF = Bayesian_filter;
namespace FM = Bayesian_filter_matrix;

typedef const double* (*H_callback)(const double*);
typedef const double* (*F_callback)(const double*);

class Linrz_predict_model_java_wrapper : public BF::Linrz_predict_model {
private:
	F_callback ffunc;
	mutable FM::Vec x_cur;

public:
	Linrz_predict_model_java_wrapper(std::size_t x_size, std::size_t q_size, F_callback ffunc) :
			BF::Linrz_predict_model(x_size, q_size),
			ffunc(ffunc),
			x_cur(x_size)
	{
	}

	/* Inherited state change expression */
	const FM::Vec& f(const FM::Vec& x) const
	{
		const double* xArr = x.data().begin();

		const double* xNextArr = ffunc(xArr);

		std::copy(xNextArr, xNextArr + x_cur.size(), x_cur.begin());
		return x_cur;
	}

};


class Linrz_uncorrelated_observe_model_java_wrapper : public BF::Linrz_uncorrelated_observe_model {
private:
	H_callback hfunc;
	mutable FM::Vec z_cur;
public:
	Linrz_uncorrelated_observe_model_java_wrapper(std::size_t x_size, std::size_t z_size, H_callback hfunc) :
			BF::Linrz_uncorrelated_observe_model(x_size, z_size),
			hfunc(hfunc),
			z_cur(z_size)
	{
	}

	/* Implemented state transition function (not used) */
	const FM::Vec& h(const FM::Vec& x) const
	{
		const double* xArr = x.data().begin();

		const double* outArr = hfunc(xArr);

		std::copy(outArr, outArr + z_cur.size(), z_cur.begin());
		return z_cur;
	}

};
#endif
