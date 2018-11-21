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
package tools.descartes.librede.repository.rules;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;

import tools.descartes.librede.configuration.ModelEntity;

public class DynamicScope extends DependencyScope {
	private class ReferencePath {
		private final EReference[] path;
		
		public ReferencePath(EReference...path) {
			this.path = path;
		}
		
		public EReference[] getElements() {
			return path;
		}
	}
	
	private boolean includeRoot;
	private final List<ReferencePath> includedPaths = new LinkedList<>();
	private final List<ReferencePath> includedOppositePaths = new LinkedList<>();
	
	DynamicScope(boolean includeRoot) {
		this.includeRoot = includeRoot;
	}
	
	public DynamicScope skipRoot() {
		includeRoot = false;
		return this;
	}
	
	public DynamicScope include(EReference...path) {		
		includedPaths.add(new ReferencePath(path));
		ReferencePath oppositePath = getOppositePath(path);
		if (oppositePath != null) {
			includedOppositePaths.add(oppositePath);
		}
		return this;
	}
	
	public DynamicScope include(EReference[] path, EReference[] oppositePath) {
		includedPaths.add(new ReferencePath(path));
		includedOppositePaths.add(new ReferencePath(oppositePath));
		return this;
	}
	
	public Set<? extends ModelEntity> getScopeSet(ModelEntity base) {
		Set<ModelEntity> selectedEntities = new HashSet<>();
		if (includeRoot) {
			selectedEntities.add(base);
		}
		for (ReferencePath currentPath : includedPaths) {
			resolve(base, currentPath.getElements(), 0, selectedEntities);
		}
		return selectedEntities;
	}
	
	public Set<? extends ModelEntity> getNotificationSet(ModelEntity base) {
		Set<ModelEntity> selectedEntities = new HashSet<>();
		if (includeRoot) {
			selectedEntities.add(base);
		}
		for (ReferencePath currentPath : includedOppositePaths) {
			resolve(base, currentPath.getElements(), 0, selectedEntities);
		}
		return selectedEntities;
	}
	
	private ReferencePath getOppositePath(EReference...path) {
		EReference[] oppositeRefs = new EReference[path.length];
		for (int pathIdx = path.length - 1, oppositeIdx = 0; pathIdx >= 0; pathIdx--, oppositeIdx++) {
			EReference opposite = path[pathIdx].getEOpposite();
			if (opposite != null) {
				oppositeRefs[oppositeIdx] = opposite;
			} else {
				return null;
			}
		}
		return new ReferencePath(oppositeRefs);
	}
	
	@SuppressWarnings("unchecked")
	private void resolve(ModelEntity current, EReference[] path, int pathIdx, Set<ModelEntity> selectedEntities) {
		EStructuralFeature currentFeature = path[pathIdx];
		if (currentFeature.getEContainingClass().isSuperTypeOf(current.eClass())) {
			Object value = current.eGet(currentFeature);
			if (value != null) {
				pathIdx++;
				if (pathIdx >= path.length) {
					if (currentFeature.isMany()) {
						selectedEntities.addAll((List<ModelEntity>)value);
					} else {
						selectedEntities.add((ModelEntity)value);
					}
				} else {
					if (currentFeature.isMany()) {
						List<?> children = (List<?>)value;
						for (Object child : children) {
							resolve((ModelEntity) child, path, pathIdx, selectedEntities);
						}
					} else {
						resolve((ModelEntity) value, path, pathIdx, selectedEntities);
					}
				}
			}
		}
	}
}
