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

import tools.descartes.librede.models.State;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.variables.ConstraintVariable;
import tools.descartes.librede.repository.rules.DataDependency;

public final class Unconstrained implements IStateConstraint {
	
	private Unconstrained() {
		throw new AssertionError("Class Unconstrained cannot be instantiated.");
	}

	@Override
	public double getLowerBound() {
		throw new UnsupportedOperationException();
	}

	@Override
	public double getUpperBound() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ConstraintVariable getValue(State state) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setStateModel(IStateModel<? extends IStateConstraint> model) {
		// Do nothing		
	}

	@Override
	public List<DataDependency<?>> getDataDependencies() {
		return Collections.emptyList();
	}
}
