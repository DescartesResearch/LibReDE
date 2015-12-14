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
