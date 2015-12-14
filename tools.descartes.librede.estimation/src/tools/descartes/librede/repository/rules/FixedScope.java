package tools.descartes.librede.repository.rules;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import tools.descartes.librede.configuration.ModelEntity;

public class FixedScope extends DependencyScope {
	
	private final Set<ModelEntity> scope;
	
	FixedScope(Collection<? extends ModelEntity> entities) {
		scope = new HashSet<>(entities);
	}

	@Override
	public Set<? extends ModelEntity> getScopeSet(ModelEntity base) {
		return scope;
	}

	@Override
	public Set<? extends ModelEntity> getNotificationSet(ModelEntity base) {
		return Collections.singleton(base);
	}

}
