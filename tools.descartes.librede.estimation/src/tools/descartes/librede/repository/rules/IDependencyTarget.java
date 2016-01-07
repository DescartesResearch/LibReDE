package tools.descartes.librede.repository.rules;

import java.util.List;

public interface IDependencyTarget {
	
	public List<? extends DataDependency<?>> getDataDependencies();

}
