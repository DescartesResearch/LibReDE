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
package edu.kit.ipd.descartes.librede.estimation.models.observation.functions;

import java.util.List;

import edu.kit.ipd.descartes.librede.estimation.repository.Query;
import edu.kit.ipd.descartes.librede.estimation.workload.IModelEntity;
import edu.kit.ipd.descartes.librede.estimation.workload.Resource;
import edu.kit.ipd.descartes.librede.estimation.workload.Service;
import edu.kit.ipd.descartes.librede.estimation.workload.WorkloadDescription;

public abstract class AbstractOutputFunction implements IOutputFunction {
	
	private WorkloadDescription system;
	private List<Resource> selectedResources;
	private List<Service> selectedClasses;

	protected AbstractOutputFunction(WorkloadDescription system, List<Resource> selectedResources,
			List<Service> selectedClasses) {		
		if (system == null || selectedResources == null || selectedClasses == null) {
			throw new NullPointerException();
		}
		if (selectedResources.size() < 1 || selectedClasses.size() < 1) {
			throw new IllegalArgumentException();
		}
		
		this.system = system;
		this.selectedResources = selectedResources;
		this.selectedClasses = selectedClasses;
	}

	public WorkloadDescription getSystem() {
		return system;
	}

	public List<Resource> getSelectedResources() {
		return selectedResources;
	}

	public List<Service> getSelectedWorkloadClasses() {
		return selectedClasses;
	}	
	
	protected boolean checkQueryPrecondition(Query<?> query, List<String> messages) {
		if (!query.hasData()) {
			StringBuilder msg = new StringBuilder("DATA PRECONDITION: ");
			msg.append("metric = ").append(query.getMetric().toString()).append(" ");
			msg.append("entities = { ");
			for(IModelEntity entity : query.getEntities()) {
				msg.append(entity.getName()).append(" ");
			}
			msg.append(" } ");
			messages.add(msg.toString());
			return false;
		}
		return true;
	}
}
