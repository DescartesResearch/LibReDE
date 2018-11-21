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
/**
 * This package contains the interface {@link tools.descartes.librede.algorithm.IEstimationAlgorithm} describing an estimation algorithm. 
 * An estimation algorithm  * is used to solve an estimation problem given the current monitoring data. The estimation problem is
 * provided as a combination of a state model ({@link tools.descartes.librede.models.state.IStateModel}) and 
 * an observation model ({@link tools.descartes.librede.models.observation.IObservationModel}). Furthermore it
 * contains basic solution algorithms implementing that interface.
 */
package tools.descartes.librede.algorithm;
