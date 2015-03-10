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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Range;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.diff.IDifferentiableFunction;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.models.state.initial.IStateInitializer;
import tools.descartes.librede.models.state.initial.PredefinedStateInitializer;

public class ConstantStateModel<C extends IStateConstraint> implements IStateModel<C> {
	
	public static class Builder<C extends IStateConstraint> {
		// The set is automatically sorted by resource names and service names
		private Set<StateVariable> stateVariables = new TreeSet<StateVariable>();
		private List<C> constraints = new ArrayList<C>();
		private IStateInitializer stateInitializer;
		
		public void addVariable(Resource resource, Service service) {
			stateVariables.add(new StateVariable(resource, service));
		}
		
		public void setStateInitializer(IStateInitializer stateInitializer) {
			this.stateInitializer = stateInitializer;
		}
		
		public void addConstraint(C constraint) {
			if (constraint == null) {
				throw new IllegalArgumentException("Constraint must not be null.");
			}
			constraints.add(constraint);
		}
		
		public ConstantStateModel<C> build() {
			if (stateInitializer == null) {
				stateInitializer = new PredefinedStateInitializer(zeros(stateVariables.size()));
			}
			ConstantStateModel<C> model = new ConstantStateModel<C>(new ArrayList<StateVariable>(stateVariables), constraints, stateInitializer);
			for (IStateConstraint c : model.getConstraints()) {
				c.setStateModel(model);
			}
			return model;
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
	
	private final int stateSize;
	private final List<Service> services;
	private final List<Resource> resources;
	private final Map<Resource, Integer> resourcesToIdx;
	private final Map<Service, Integer> servicesToIdx;
	private final List<StateVariable> variables;
	private final int[][] stateVarIdx;
	private final List<Range> resourceRanges;
	private final List<C> constraints;
	private final IStateInitializer stateInitializer;
	private final List<IDifferentiableFunction> derivatives = new ArrayList<IDifferentiableFunction>();
	
	private ConstantStateModel(List<StateVariable> variables, List<C> constraints, IStateInitializer stateInitializer) {
		this.stateSize = variables.size();	
		this.constraints = Collections.unmodifiableList(constraints);
		this.variables = Collections.unmodifiableList(variables);
		
		// Determine all resources and services contained in this state model
		resources = new ArrayList<Resource>();
		services = new ArrayList<Service>();
		resourcesToIdx = new HashMap<Resource, Integer>();
		servicesToIdx = new HashMap<Service, Integer>();
		for (StateVariable v : variables) {
			if (!resourcesToIdx.containsKey(v.getResource())) {
				resources.add(v.getResource());
				resourcesToIdx.put(v.getResource(), resources.size() - 1);
			}
			if (!servicesToIdx.containsKey(v.getService())) {
				services.add(v.getService());
				servicesToIdx.put(v.getService(), services.size() - 1);
			}
		}
		
		// Determine a mapping between the 2-dimensional state vector and the resources and services
		stateVarIdx = new int[resourcesToIdx.size()][servicesToIdx.size()];
		// -1 means that this resource / service combination is not a state variable 
		// (e.g. if a service does not visit all resources)
		for (int i = 0; i < stateVarIdx.length; i++) {
			Arrays.fill(stateVarIdx[i], -1);
		}		
		int varIdx = 0;
		for (StateVariable var : variables) {
			int r = resourcesToIdx.get(var.getResource());
			int s = servicesToIdx.get(var.getService());
			stateVarIdx[r][s] = varIdx;
			varIdx++;
		}
		
		// determine the start and end indexes of all resources
		resourceRanges = new ArrayList<Range>(resourcesToIdx.size());
		int offset = 0;
		for (int r = 0; r < stateVarIdx.length; r++) {
			int newOffset = offset;
			for (int s = 0; s < stateVarIdx[r].length; s++) {
				if (stateVarIdx[r][s] >= 0) {
					newOffset++;
				}
			}
			resourceRanges.add(LinAlg.range(offset, newOffset));
			offset = newOffset;
		}

		// initialize state of model		
		if (stateInitializer == null) {
			throw new IllegalArgumentException("State initializer must not be null.");
		}
		this.stateInitializer = stateInitializer;
	
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
	public Vector getNextState(Vector state) {
		return state;
	}
	
	@Override
	public Range getStateVariableIndexRange(Resource res) {
		Integer resIdx = resourcesToIdx.get(res);
		if (resIdx == null) {
			throw new NoSuchElementException("Resource is not contained in state model.");
		}
		return resourceRanges.get(resIdx);
	}
	
	@Override
	public int getStateVariableIndex(Resource res, Service service) {
		Integer resIdx = resourcesToIdx.get(res);
		Integer serviceIdx = servicesToIdx.get(service);
		if (resIdx == null || serviceIdx == null) {
			throw new NoSuchElementException("There is no defined state variable for this resource/service combination.");
		}
		int idx = stateVarIdx[resIdx][serviceIdx];
		if (idx < 0) {
			throw new NoSuchElementException("There is no defined state variable for this resource/service combination.");
		}
		return idx;
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
		Vector initialState = stateInitializer.getInitialValue();
		if (!initialState.isEmpty() && initialState.rows() != stateSize) {
			throw new IllegalStateException("Size of initial state vector must be equal to the state size.");
		}
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
		return variables.get(stateVariableIdx).getResource();
	}

	@Override
	public Service getService(int stateVariableIdx) {
		return variables.get(stateVariableIdx).getService();
	}


}
