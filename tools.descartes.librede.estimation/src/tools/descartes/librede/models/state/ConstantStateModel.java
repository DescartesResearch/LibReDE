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
import java.util.List;

import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.Matrix;
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
	
	private final int stateSize;
	private final List<Resource> resources;
	private final List<Service> services;
	private final List<C> constraints;
	private final Vector initialState;
	private final List<IDifferentiableFunction> derivatives = new ArrayList<IDifferentiableFunction>();
	
	private ConstantStateModel(List<Resource> resources, List<Service> services, List<C> constraints, Vector initialState) {
		this.stateSize = resources.size();
		this.resources = Collections.unmodifiableList(resources);
		this.services = Collections.unmodifiableList(services);
		this.constraints = Collections.unmodifiableList(constraints);
		
		if (initialState == null) {
			throw new IllegalArgumentException("Initial state must not be null.");
		}
		if (initialState.rows() != stateSize) {
			throw new IllegalArgumentException("Size of initial state vector must be equal to the state size.");
		}		
		this.initialState = initialState;
	
		for (int i = 0; i < stateSize; i++) {
			derivatives.add(new ConstantFunction(stateSize, i));
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
	public Resource getResource(int stateIdx) {
		return resources.get(stateIdx);
	}

	@Override
	public Service getService(int stateIdx) {
		return services.get(stateIdx);
	}

}
