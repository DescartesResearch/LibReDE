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
package tools.descartes.librede.models.state.constraints;

import java.util.Collections;
import java.util.List;

import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.variables.Variable;
import tools.descartes.librede.repository.rules.DataDependency;

public class ConstantStateBoundsConstraint implements IStateBoundsConstraint {

	// the state variable to which the bounds apply to
	private final ResourceDemand variable;
	private final double lower;
	private final double upper;
	private IStateModel<? extends IStateConstraint> stateModel;
	
	public ConstantStateBoundsConstraint(ResourceDemand demand, double lowerBound, double upperBound) {
		this.variable = demand;
		this.lower = lowerBound;
		this.upper = upperBound;
	}
	
	public ResourceDemand getStateVariable() {
		return this.variable;
	}

	@Override
	public Variable getValue(State state) {
		if (stateModel == null) {
			throw new IllegalStateException();
		}
		return state.getVariable(variable.getResource(), variable.getService());
	}

	@Override
	public double getLowerBound() {
		return lower;
	}

	@Override
	public double getUpperBound() {
		return upper;
	}

	@Override
	public void setStateModel(IStateModel<? extends IStateConstraint> model) {
		this.stateModel = model;
	}

	@Override
	public List<DataDependency<?>> getDataDependencies() {
		return Collections.emptyList();
	}

}
