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
package tools.descartes.librede.configuration.editor.forms;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.edit.provider.DelegatingWrapperItemProvider;
import org.eclipse.emf.edit.provider.ItemProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

public class ClassesViewerFilter extends ViewerFilter {
	
	private Class<?> cl;
	private Class<?> parentCl;
	
	public ClassesViewerFilter(Class<?> parentCl, Class<?> cl) {
		this.cl = cl;
		this.parentCl = parentCl;
	}

	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		/*
		 * If an element has a DelegatingWrapperItemProvider we need to first get the right object.
		 */
		Object unwrapped = parentElement;
		if (parentElement instanceof DelegatingWrapperItemProvider) {
			unwrapped = ((DelegatingWrapperItemProvider)parentElement).getValue();
		}		
		
		if (parentCl.isAssignableFrom(unwrapped.getClass())) {
			if (element instanceof DelegatingWrapperItemProvider) {
				return cl.isAssignableFrom(((DelegatingWrapperItemProvider)element).getValue().getClass());
			}
			return cl.isAssignableFrom(element.getClass());
		}
		return true;
	}

}
