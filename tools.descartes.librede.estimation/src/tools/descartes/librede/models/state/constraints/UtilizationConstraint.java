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

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.models.AbstractDependencyTarget;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.observation.equations.UtilizationLawEquation;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.variables.ConstraintVariable;
import tools.descartes.librede.models.variables.Variable;
import tools.descartes.librede.repository.IRepositoryCursor;

public class UtilizationConstraint extends AbstractDependencyTarget implements ILinearStateConstraint {

	private final Resource res_i;
	
	private final IRepositoryCursor cursor;
	
	private final int historicInterval;
	
	private UtilizationLawEquation utilLaw;
	
	public UtilizationConstraint(Resource resource, IRepositoryCursor cursor) {
		this(resource, cursor, 0);
	}
	
	public UtilizationConstraint(Resource resource, IRepositoryCursor cursor, int historicInterval) {
		this.res_i = resource;
		this.cursor = cursor;
		this.historicInterval = historicInterval;
	}
	
	@Override
	public double getLowerBound() {
		return 0;
	}

	@Override
	public double getUpperBound() {
		return 1;
	}

	@Override
	public Variable getValue(State state) {
		if (utilLaw == null) {
			throw new IllegalStateException();
		}
		return new ConstraintVariable(state, utilLaw.getValue(state));
	}

	@Override
	public void setStateModel(IStateModel<? extends IStateConstraint> model) {
		this.utilLaw = new UtilizationLawEquation(model, cursor, res_i, historicInterval);
		addDataDependencies(this.utilLaw);
	}

}
