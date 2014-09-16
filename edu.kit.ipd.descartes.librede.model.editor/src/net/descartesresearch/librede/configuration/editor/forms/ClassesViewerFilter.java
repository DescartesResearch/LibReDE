package net.descartesresearch.librede.configuration.editor.forms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ClassesViewerFilter extends ViewerFilter {
	
	private Class<?> cl;
	
	public ClassesViewerFilter(Class<?> cl) {
		this.cl = cl;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return cl.isAssignableFrom(element.getClass());
	}

}
