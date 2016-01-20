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
package tools.descartes.librede.models;

import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.vector;

import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.models.state.IStateModel;

public class State {
	
	private final DerivativeStructure[] state;
	private final int order;
	private final IStateModel<?> stateModel;
	
	public State(IStateModel<?> stateModel, Vector state) {
		this(stateModel, state, 0);
	}
	
	public State(IStateModel<?> stateModel, Vector state, int order) {
		this.stateModel = stateModel;
		this.state = new DerivativeStructure[state.rows()];
		this.order = order;
		for (int i = 0; i < this.state.length; i++) {
			this.state[i] = new DerivativeStructure(this.state.length, order, state.get(i));
		}
	}
	
	public Matrix getStateJacobiMatrix() {
		final int[] orders = new int[state.length];
		Matrix jacobi = matrix(state.length, state.length, new MatrixFunction() {			
			@Override
			public double cell(int row, int column) {
				orders[column] = 1;
				double value = state[row].getPartialDerivative(orders);
				orders[column] = 0;
				return value;
			}
		});
		return jacobi;
	}
	
	public Vector getVector() {
		Vector vec = vector(state.length, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return state[row].getValue();
			}
		});
		return vec;
	}
}
