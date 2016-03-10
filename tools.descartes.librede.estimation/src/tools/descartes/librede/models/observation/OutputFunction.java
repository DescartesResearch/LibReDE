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
package tools.descartes.librede.models.observation;

import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.AbstractDependencyTarget;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.observation.queueingmodel.FixedValue;
import tools.descartes.librede.models.observation.queueingmodel.ModelEquation;
import tools.descartes.librede.models.variables.OutputVariable;

public class OutputFunction extends AbstractDependencyTarget {
	
	private final FixedValue observedOutput;
	private final ModelEquation calculatedOutput;
	
	public OutputFunction(FixedValue observedOutput, ModelEquation calculatedOutput) {
		this.observedOutput = observedOutput;
		this.calculatedOutput = calculatedOutput;
		addDataDependencies(this.observedOutput);
		addDataDependencies(this.calculatedOutput);
	}
	
	public boolean hasData() {
		return observedOutput.hasData() && calculatedOutput.hasData();
	}
	
	public double getObservedOutput() {
		return observedOutput.getConstantValue();
	}
	
	public OutputVariable getCalculatedOutput(State state) {
		return new OutputVariable(state, calculatedOutput.getValue(state));
	}
	
	public double getFactor() {
		return calculatedOutput.getConstantValue();
	}
	
	public Vector getIndependentVariables() {
		return calculatedOutput.getFactors();
	}
	
	public boolean isLinear() {
		return calculatedOutput.isLinear();
	}
	
	public boolean isConstant() {
		return calculatedOutput.isConstant();
	}

}
