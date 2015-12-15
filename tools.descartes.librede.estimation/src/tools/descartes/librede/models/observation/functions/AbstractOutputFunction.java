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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.rules.Rule;
import tools.descartes.librede.repository.rules.DataDependency;
import tools.descartes.librede.units.Time;

public abstract class AbstractOutputFunction implements IOutputFunction {
	
	private List<DataDependency<?>> dataDependencies = new ArrayList<>();
	private final IStateModel<? extends IStateConstraint> stateModel;
	protected final int historicInterval;

	protected AbstractOutputFunction(IStateModel<? extends IStateConstraint> stateModel, int historicInterval) {		
		if (stateModel == null) {
			throw new NullPointerException();
		}
		this.historicInterval = historicInterval;
		this.stateModel = stateModel;
	}

	public IStateModel<? extends IStateConstraint> getStateModel() {
		return stateModel;
	}
	
	protected abstract void initDataDependencies();
	
	protected void addDataDependency(Query<?,?> query) {
	}
	
	protected boolean checkQueryPrecondition(Query<?,?> query, List<String> messages) {
		if (!query.isExecutable()) {
			StringBuilder msg = new StringBuilder("FAILED DATA PRECONDITION: ");
			msg.append("metric = ").append(query.getMetric().toString()).append(" ");
			msg.append("entities = { ");
			for(ModelEntity entity : query.getEntities()) {
				msg.append(entity.getName()).append(" ");
			}
			msg.append(" } ");
			messages.add(msg.toString());
			return false;
		}
		return true;
	}
}
