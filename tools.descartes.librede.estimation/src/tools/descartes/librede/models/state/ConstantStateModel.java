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

import static tools.descartes.librede.linalg.LinAlg.zeros;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Range;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.diff.IDifferentiableFunction;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.models.state.constraints.Unconstrained;

public class ConstantStateModel<C extends IStateConstraint> implements IStateModel<C> {
	
	public static class Builder<C extends IStateConstraint> {
		private List<Resource> resources = new ArrayList<>();
		private List<Service> services = new ArrayList<>();
		private List<C> constraints = new ArrayList<C>();
		private Vector initialState;
		
		public void addVariable(Resource resource, Service service) {
			resources.add(resource);
			services.add(service);
		}
		
		public void setInitialState(Vector initialState) {
			this.initialState = initialState;
		}
		
		public void addConstraint(C constraint) {
			if (constraint == null) {
				throw new IllegalArgumentException("Constraint must not be null.");
			}
			constraints.add(constraint);
		}
		
		public ConstantStateModel<C> build() {
			return new ConstantStateModel<C>(resources, services, constraints, initialState);
		}
	}
	
	private class ConstantFunction implements IDifferentiableFunction {
		
		private final Vector firstDev;
		private final Vector secondDev;
		
		public ConstantFunction(int stateSize, int varIdx) {
			firstDev = zeros(stateSize).set(varIdx, 1.0);
			secondDev = zeros(stateSize);
		}

		@Override
		public Vector getFirstDerivatives(Vector x) {
			return firstDev;
		}

		@Override
		public Matrix getSecondDerivatives(Vector x) {
			return secondDev;
		}
		
	}
	
	private final int resourceStride;
	private final int stateSize;
	private final List<Resource> resources;
	private final List<Service> services;
	private final Map<Resource, Integer> resourcesToIndex;
	private final Map<Service, Integer> servicesToIndex;
	private final List<C> constraints;
	private final Vector initialState;
	private final List<IDifferentiableFunction> derivatives = new ArrayList<IDifferentiableFunction>();
	private Vector currentState;
	
	private ConstantStateModel(List<Resource> resources, List<Service> services, List<C> constraints, Vector initialState) {
		this.stateSize = resources.size() * services.size();
		this.resourceStride = services.size();		
		this.resources = Collections.unmodifiableList(resources);
		this.services = Collections.unmodifiableList(services);
		this.constraints = Collections.unmodifiableList(constraints);
		
		// Create mapping between model entities and state variables index
		this.resourcesToIndex = new HashMap<Resource, Integer>(resources.size());
		int i = 0;
		for (Resource r : resources) {
			this.resourcesToIndex.put(r, i);
			i++;
		}
		this.servicesToIndex = new HashMap<Service, Integer>(services.size());
		i = 0;
		for (Service s : services) {
			this.servicesToIndex.put(s, i);
			i++;
		}

		// initialize state of model		
		if (initialState == null) {
			throw new IllegalArgumentException("Initial state must not be null.");
		}
		if (initialState.rows() != stateSize) {
			throw new IllegalArgumentException("Size of initial state vector must be equal to the state size.");
		}		
		this.initialState = initialState;
		this.currentState = initialState;
	
		for (int a = 0; a < stateSize; a++) {
			derivatives.add(new ConstantFunction(stateSize, a));
		}
	}
	
	public static Builder<Unconstrained> unconstrainedModelBuilder() {
		return new Builder<Unconstrained>();
	}
	
	public static Builder<IStateConstraint> constrainedModelBuilder() {
		return new Builder<IStateConstraint>();
	}

	@Override
	public int getStateSize() {
		return stateSize;
	}

	@Override
	public Vector getNextState() {
		return currentState;
	}
	
	@Override
	public Vector getCurrentState() {
		return currentState;
	}
	
	@Override
	public Range getStateVariableIndexRange(Resource res) {
		Integer resIdx = resourcesToIndex.get(res);
		if (resIdx == null) {
			throw new NoSuchElementException("Resource is not contained in state model.");
		}
		return LinAlg.range(resIdx * resourceStride, (resIdx + 1) * resourceStride);
	}
	
	@Override
	public int getStateVariableIndex(Resource res, Service service) {
		Integer resIdx = resourcesToIndex.get(res);
		Integer serviceIdx = servicesToIndex.get(service);
		if (resIdx == null || serviceIdx == null) {
			throw new NoSuchElementException("Service or resource is not contained in state model.");
		}
		return resIdx * resourceStride + serviceIdx;
	}	

	@Override
	public void setCurrentState(Vector state) {
		this.currentState = state;
	}

	@Override
	public List<C> getConstraints() {
		return Collections.unmodifiableList(constraints);
	}

	@Override
	public List<IDifferentiableFunction> getStateDerivatives() {
		return derivatives;
	}
	
	@Override
	public Vector getInitialState() {
		return initialState;
	}

	@Override
	public List<Resource> getResources() {
		return resources;
	}

	@Override
	public List<Service> getServices() {
		return services;
	}

	@Override
	public Resource getResource(int stateVariableIdx) {
		return resources.get(stateVariableIdx / resourceStride);
	}

	@Override
	public Service getService(int stateVariableIdx) {
		return services.get(stateVariableIdx % resourceStride);
	}


}
