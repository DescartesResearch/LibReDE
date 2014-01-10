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
package edu.kit.ipd.descartes.librede.bayesplusplus.backend;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * Helper class defining the native methods for accessing the Bayes++ library.
 * 
 * @author Simon Spinner (simon.spinner@kit.edu)
 * @version 1.0
 */
public final class BayesPlusPlusLibrary {

	static {
		// Register the native methods in this class with the shared library
		Native.register("BayesPlusPlus");
	}

	/**
	 * Return code if an operation failed.
	 */
	public static final int ERROR = -1;
	
	/**
	 * Return code if an operation succeeded.
	 */
	public static final int OK = 0;

	/**
	 * Creates a new linearized predict model. See Bayes++ documentation for
	 * more information.
	 * 
	 * @param x_size
	 *            the size of the state vector
	 * @param q_size
	 *            the size of the state covariance matrix
	 * @param ffunc
	 *            a reference to a <code>Callback</code> object that provides
	 *            the next state
	 * @return a <code>Pointer</code> to the native object or null if an error
	 *         occurred
	 * @see <a href="http://bayesclasses.sourceforge.net/ClassDocumentation/html/
	 *      classBayesian__filter_1_1Linrz__predict__model
	 *      .html#a8be6802abc71a157598c1102d6270703">http://bayesclasses.sourceforge.net/</a>
	 */
	public static native Pointer create_linrz_predict_model(int x_size, int q_size, Callback ffunc);

	/**
	 * Destroys the native object of the linearized predict model.
	 * 
	 * @param model
	 *            a <code>Pointer</code> to the native object
	 * @see #create_linrz_predict_model(int, int, Callback)
	 */
	public static native void dispose_linrz_predict_model(Pointer model);

	/**
	 * Sets the state noise covariance vector of the predict model.
	 * 
	 * <p>
	 * Assuming the state equation x(k|k-1) = f(x(k-1|k-1)) + G(k)w(k), the
	 * state noise vector q(k) describes the covariance of w(k).
	 * </p>
	 * 
	 * @param predict_model
	 *            a <code>Pointer</code> to a linearized predict model
	 * @param q
	 *            a <code>Pointer</code> to a double array containing the state
	 *            noise covariance vector
	 * @param x_size
	 *            the size of the state vector
	 */
	public static native void set_q(Pointer predict_model, Pointer q, int x_size);

	/**
	 * Sets the state noise coupling matrix of the predict model.
	 * 
	 * <p>
	 * Assuming the state equation x(k|k-1) = f(x(k-1|k-1)) + G(k)w(k).
	 * </p>
	 * 
	 * @param predict_model
	 *            a <code>Pointer</code> to a linearized predict model
	 * @param G
	 *            a <code>Pointer</code> to a double array containing the state
	 *            noise coupling matrix
	 * @param x_size
	 *            the size of the state vector
	 */
	public static native void set_G(Pointer predict_model, Pointer G, int x_size);

	/**
	 * Sets the Jacobian of the functional part of f(x).
	 * 
	 * <p>
	 * Assuming the state equation x(k|k-1) = f(x(k-1|k-1)) + G(k)w(k).
	 * </p>
	 * 
	 * @param predict_model
	 *            a <code>Pointer</code> to a linearized predict model
	 * @param Fx
	 *            a <code>Pointer</code> to a double array containing the Jacobi
	 *            matrix of f(x)
	 * @param x_size
	 *            the size of the state vector
	 */
	public static native void set_Fx(Pointer predict_model, Pointer Fx, int x_size);

	/**
	 * Creates a new linearized uncorrelated observe model. See Bayes++
	 * documentation for more information.
	 * 
	 * @param x_size
	 *            the size of the state vector
	 * @param z_size
	 *            the size of the observation vector
	 * @param hfunc
	 *            a reference to a <code>Callback</code> object that calculates
	 *            the next observation
	 * @return a <code>Pointer</code> to the native object or null if an error
	 *         occurred
	 * @see <a href="http://bayesclasses.sourceforge.net/ClassDocumentation/html/
	 *      classBayesian__filter_1_1Linrz__uncorrelated__observe__model.html">http://bayesclasses.sourceforge.net/</a>
	 */
	public static native Pointer create_linrz_uncorrelated_observe_model(int x_size, int z_size, Callback hfunc);

	/**
	 * Destroys the native object of the linearized uncorrelated observe model.
	 * 
	 * @param model
	 *            a <code>Pointer</code> to the native object.
	 * @see #create_linrz_uncorrelated_observe_model(int, int, Callback)
	 */
	public static native void dispose_linrz_uncorrelated_observe_model(Pointer model);

	/**
	 * Sets the observe noise vector.
	 * 
	 * @param observation_model
	 *            a <code>Pointer</code> to the native object corresponding to
	 *            the observation model.
	 * @param Zv
	 *            a <code>Pointer</code> to a double array containing the
	 *            observe noise vector.
	 * @param z_size
	 *            the size of the output vector
	 */
	public static native void set_Zv(Pointer observation_model, Pointer Zv, int z_size);

	/**
	 * Sets the Jacobi matrix of h(x).
	 * 
	 * <p>
	 * Assuming the observation equation zp(k) = h(x(k-1|k-1)).
	 * </p>
	 * 
	 * @param observation_model
	 *            a <code>Pointer</code> to the native object corresponding to
	 *            the observation model.
	 * @param Hx
	 *            a <code>Pointer</code> to a double array containing the Jacobi
	 *            matrix of h(x).
	 * @param x_size
	 *            the size of the state vector
	 * @param z_size
	 *            the size of the output vector
	 */
	public static native void set_Hx(Pointer observation_model, Pointer Hx, int x_size, int z_size);

	/**
	 * Creates a new covariance scheme. See Bayes++ documentation for more
	 * information.
	 * 
	 * @param x_size
	 *            the size of the state vector
	 * @return a <code>Pointer</code> to the native object or null if an error
	 *         occurred
	 * @see <a href="http://bayesclasses.sourceforge.net/ClassDocumentation/html/
	 *      classBayesian__filter_1_1Covariance__scheme.html">http://bayesclasses.sourceforge.net/</a>
	 */
	public static native Pointer create_covariance_scheme(int x_size);

	/**
	 * Destroys the native object of the covariance scheme.
	 * 
	 * @param scheme
	 *            a <code>Pointer</code> to the native object.
	 * @see #create_covariance_scheme(int)
	 */
	public static native void dispose_covariance_scheme(Pointer scheme);

	/**
	 * Initializes the kalman filter before it can be used for state estimation.
	 * 
	 * <p>
	 * This function must be called once before any other functions such as
	 * {@link #predict(Pointer, Pointer)},
	 * {@link #observe(Pointer, Pointer, Pointer, int)}, or
	 * {@link #update(Pointer)} are called.
	 * </p>
	 * 
	 * @param scheme
	 *            a <code>Pointer</code> to the the native object corresponding
	 *            to the covariance scheme.
	 * @param x_0
	 *            a <code>Pointer</code> to a double array containing the vector
	 *            of initial estimates.
	 * @param X_0
	 *            a <code>Pointer</code> to a double array containing the
	 *            symmetrical matrix of the state covariance.
	 * @param x_size
	 *            the size of the state vector
	 * @return {@link #OK} if the operation succeeded, {@link #ERROR} otherwise.
	 */
	public static native int init_kalman(Pointer scheme, Pointer x_0, Pointer X_0, int x_size);

	/**
	 * Calculates the new state prediction.
	 * 
	 * <p>
	 * This function is usually called before
	 * {@link #observe(Pointer, Pointer, Pointer, int)} and
	 * {@link #update(Pointer)}.
	 * </p>
	 * 
	 * @param scheme
	 *            a <code>Pointer</code> to the the native object corresponding
	 *            to the covariance scheme.
	 * @param predict_model
	 *            a <code>Pointer</code> to the the native object corresponding
	 *            to the predict model.
	 * @return {@link #OK} if the operation succeeded, {@link #ERROR} otherwise.
	 */
	public static native int predict(Pointer scheme, Pointer predict_model);

	/**
	 * Calculates the observation for the new state prediction and compares it
	 * to the given real observation.
	 * 
	 * <p>
	 * This function is usually called after {@link #predict(Pointer, Pointer)}
	 * and before {@link #update(Pointer)}.
	 * </p>
	 * 
	 * @param scheme
	 *            a <code>Pointer</code> to the the native object corresponding
	 *            to the covariance scheme.
	 * @param observe_model
	 *            a <code>Pointer</code> to the the native object corresponding
	 *            to the observe model.
	 * @param z
	 *            a <code>Pointer</code> to a double array containing the real
	 *            observation vector
	 * @param z_size
	 *            the size of the output vector
	 * @return {@link #OK} if the operation succeeded, {@link #ERROR} otherwise.
	 */
	public static native int observe(Pointer scheme, Pointer observe_model, Pointer z, int z_size);

	/**
	 * Updates the internal state of the kalman filter.
	 * 
	 * <p>
	 * This function is usually called after {@link #predict(Pointer, Pointer)}
	 * and {@link #observe(Pointer, Pointer, Pointer, int)}.
	 * </p>
	 * 
	 * @param scheme
	 *            a <code>Pointer</code> to the the native object corresponding
	 *            to the covariance scheme.
	 * @return {@link #OK} if the operation succeeded, {@link #ERROR} otherwise.
	 */
	public static native int update(Pointer scheme);

	/**
	 * Returns the current state estimate.
	 * 
	 * @param scheme
	 *            a <code>Pointer</code> to the the native object corresponding
	 *            to the covariance scheme.
	 * @param x
	 *            a <code>Pointer</code> to a double array in which the state
	 *            estimate will be stored by the method.
	 */
	public static native void get_x(Pointer scheme, Pointer x);

	/**
	 * Gets the message of the last exception thrown in the native code.
	 * 
	 * @return a String containing the error message
	 */
	public static native String get_last_error();

}
