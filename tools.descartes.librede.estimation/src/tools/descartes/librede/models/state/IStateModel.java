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
package tools.descartes.librede.models.state;

import java.util.List;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Indices;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.diff.IDifferentiableFunction;
import tools.descartes.librede.models.state.constraints.IStateConstraint;

public interface IStateModel<C extends IStateConstraint> {
	
	// Information about the structure of the state model
	
	int getStateSize();
	
	List<Resource> getResources();
	
	List<Service> getAllServices();
	
	List<Service> getBackgroundServices();
	
	List<Service> getUserServices();
	
	boolean containsStateVariable(Resource res, Service service);
	
	int getStateVariableIndex(Resource res, Service service);	
	
	Indices getStateVariableIndices(Resource res);
	
	ResourceDemand getResourceDemand(int stateVariableIdx);
	
	List<C> getConstraints();
	
	Vector step(Vector currentState);
	
	List<IDifferentiableFunction> getStateDerivatives();
	
	Vector getInitialState();

}
