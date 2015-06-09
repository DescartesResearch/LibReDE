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
package tools.descartes.librede.models.observation.functions;

import static tools.descartes.librede.linalg.LinAlg.vector;
import static tools.descartes.librede.linalg.LinAlg.zeros;

import org.apache.commons.math3.analysis.differentiation.DSCompiler;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableFunction;

import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.diff.IDifferentiableFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public abstract class AbstractLinearOutputFunction extends AbstractOutputFunction implements ILinearOutputFunction, IDifferentiableFunction, MultivariateDifferentiableFunction {
	
	protected AbstractLinearOutputFunction(IStateModel<? extends IStateConstraint> stateModel, int historicInterval) {
		super(stateModel, historicInterval);
	}
	
	@Override
	public double getCalculatedOutput(Vector state) {
		return getIndependentVariables().dot(state);
	}

	@Override
	public Vector getFirstDerivatives(Vector state) {
		return getIndependentVariables();
	}

	@Override
	public Matrix getSecondDerivatives(Vector state) {
		return zeros(state.rows(), state.rows());
	}
	
	@Override
	public double value(double[] x) {
		return getCalculatedOutput(vector(x));
	}
	
	@Override
	public DerivativeStructure value(DerivativeStructure[] x) {
		if (x.length > 0) {
			// Important: linear combination in MathArray (used by DerivativeStructure) seems to be buggy in version 3.2
			// Therefore we calculate it manually (this implementation is optimized for speed not accuracy)
			DSCompiler c = DSCompiler.getCompiler(x.length, x[0].getOrder());
			Vector factors = getIndependentVariables();
			double[] derivatives = new double[c.getSize()];
			int[] orders = new int[x.length];
			double value = 0.0;
			for (int i = 0; i < x.length; i++) {
				orders[i]++;
				value += x[i].getValue() * factors.get(i);
				derivatives[c.getPartialDerivativeIndex(orders)] = factors.get(i);
				orders[i]--;
			}
			derivatives[0] = value;
			return new DerivativeStructure(x.length, x[0].getOrder(), derivatives);
		}
		throw new IllegalArgumentException();
	}
}
