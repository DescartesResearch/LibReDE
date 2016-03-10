package tools.descartes.librede.models.observation.queueingmodel;

import org.apache.commons.math3.analysis.differentiation.DSCompiler;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public abstract class LinearModelEquation extends ModelEquation {
	
	protected LinearModelEquation(IStateModel<? extends IStateConstraint> stateModel, int historicInterval) {
		super(stateModel, historicInterval);
	}

	public abstract Vector getFactors();
	
	@Override
	public DerivativeStructure getValue(State state) {
		if (state.getStateSize() > 0) {
			DerivativeStructure[] x = state.getDerivativeStructure();
			// Important: linear combination in MathArray (used by DerivativeStructure) seems to be buggy in version 3.2
			// Therefore we calculate it manually (this implementation is optimized for speed not accuracy)
			DSCompiler c = DSCompiler.getCompiler(x.length, x[0].getOrder());
			Vector factors = getFactors();
			double[] derivatives = new double[c.getSize()];
			int[] orders = new int[x.length];
			double value = 0.0;
			boolean derive = c.getOrder() > 0;
			for (int i = 0; i < x.length; i++) {
				orders[i]++;
				value += x[i].getValue() * factors.get(i);
				if (derive) {
					derivatives[c.getPartialDerivativeIndex(orders)] = factors.get(i);
				}
				orders[i]--;
			}
			derivatives[0] = value;
			return new DerivativeStructure(state.getStateSize(), state.getDerivationOrder(), derivatives);
		}
		throw new IllegalArgumentException();
	}

}
