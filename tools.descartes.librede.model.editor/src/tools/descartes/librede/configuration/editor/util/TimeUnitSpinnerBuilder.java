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
package tools.descartes.librede.configuration.editor.util;

import java.util.Date;

import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.widgets.FormToolkit;

import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public class TimeUnitSpinnerBuilder {
	
	public static Composite createComposite(FormToolkit toolkit, Composite parent) {
		Composite spinnerEditorComposite = toolkit.createComposite(parent,
				SWT.NONE);
		spinnerEditorComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		toolkit.paintBordersFor(spinnerEditorComposite);
		GridLayout gl_spinnerEditorComposite = new GridLayout(2, false);
		gl_spinnerEditorComposite.marginHeight = 0;
		gl_spinnerEditorComposite.marginWidth = 0;
		spinnerEditorComposite.setLayout(gl_spinnerEditorComposite);
		return spinnerEditorComposite;
	}
	
	public static Spinner createSpinnerControl(FormToolkit toolkit, Composite parent) {
		Spinner spnValue = new Spinner(parent, SWT.BORDER);
		toolkit.adapt(spnValue);
		toolkit.paintBordersFor(spnValue);
		spnValue.setMaximum(Integer.MAX_VALUE); // Important: Default is
														// 100. Much too low.
		spnValue.setMinimum(0);
		spnValue.setLayoutData(new GridData(GridData.FILL_BOTH));
		return spnValue;
	}
	
	public static ComboViewer createTimeUnitControl(FormToolkit toolkit, AdapterFactory adapterFactory, Composite parent) {
		final ComboViewer comboUnitViewer = new ComboViewer(parent,
				SWT.READ_ONLY);
		Combo comboUnit = comboUnitViewer.getCombo();
		toolkit.adapt(comboUnit);
		toolkit.paintBordersFor(comboUnit);
		comboUnitViewer.setContentProvider(new ObservableListContentProvider());
		comboUnitViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		comboUnitViewer.setInput(new WritableList(Time.INSTANCE.getUnits(), Unit.class));
		comboUnitViewer.setSelection(new StructuredSelection(
				Time.INSTANCE.getBaseUnit()));
		return comboUnitViewer;
	}
	
	public static EMFUpdateValueStrategy createModelToTargetConverter() {
		EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy(EMFUpdateValueStrategy.POLICY_UPDATE);
		strategy.setConverter(new Converter(Double.class, Integer.class) {
			@Override
			public Object convert(Object fromObject) {
				return ((Double) fromObject).intValue();
			}
		});
		return strategy;
	}
	
	public static EMFUpdateValueStrategy createTargetToModelConverter() {
		EMFUpdateValueStrategy strategy = new EMFUpdateValueStrategy(EMFUpdateValueStrategy.POLICY_UPDATE);
		strategy.setConverter(new Converter(Integer.class, Double.class) {
			@Override
			public Object convert(Object fromObject) {
				return ((Integer) fromObject).doubleValue();
			}
		});
		return strategy;
	}
}
