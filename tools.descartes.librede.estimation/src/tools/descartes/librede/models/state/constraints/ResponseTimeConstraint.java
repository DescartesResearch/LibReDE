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
package tools.descartes.librede.models.state.constraints;

import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.models.AbstractDependencyTarget;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.observation.equations.ResponseTimeValue;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.variables.Variable;
import tools.descartes.librede.repository.IRepositoryCursor;

public class ResponseTimeConstraint extends AbstractDependencyTarget implements IStateBoundsConstraint {

	private final Service cls_r;
	
	private ResponseTimeValue responseTime;
	
	private final ResourceDemand variable;
	
	private final IRepositoryCursor cursor;
	
	private final int historicInterval;
	
	public ResponseTimeConstraint(Service systemService, ResourceDemand demand, IRepositoryCursor cursor, int historicInterval) {
		this.cls_r = systemService;
		this.variable = demand;
		this.cursor = cursor;
		this.historicInterval = historicInterval;
	}
	
	@Override
	public double getLowerBound() {
		return 0;
	}

	@Override
	public double getUpperBound() {
		return responseTime.getConstantValue();
	}

	@Override
	public Variable getValue(State state) {
		return state.getVariable(variable.getResource(), variable.getService());
	}

	@Override
	public void setStateModel(IStateModel<? extends IStateConstraint> model) {
		responseTime = new ResponseTimeValue(model, cursor, cls_r, historicInterval);
	}

	@Override
	public ResourceDemand getStateVariable() {
		return variable;
	}

}
