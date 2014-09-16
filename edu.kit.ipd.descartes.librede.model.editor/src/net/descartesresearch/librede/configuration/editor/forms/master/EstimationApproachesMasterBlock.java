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
package net.descartesresearch.librede.configuration.editor.forms.master;

import java.util.HashSet;
import java.util.Set;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.EstimationApproachConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.ValidatorConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.details.ParametersDetailsPage;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;

import edu.kit.ipd.descartes.librede.approaches.IEstimationApproach;
import edu.kit.ipd.descartes.librede.estimation.validation.Validator;
import edu.kit.ipd.descartes.librede.factory.Registry;

public class EstimationApproachesMasterBlock extends AbstractMasterBlock
		implements ISelectionChangedListener, IDetailsPageProvider {

	private Table tableApproaches;
	private CheckboxTableViewer tableApproachesViewer;

	private EMFDataBindingContext masterBindingContext = new EMFDataBindingContext();

	/**
	 * Create the master details block.
	 */
	public EstimationApproachesMasterBlock(AdapterFactoryEditingDomain domain,
			LibredeConfiguration model) {
		super(domain, model);
	}

	/**
	 * Register the pages.
	 * 
	 * @param part
	 */
	@Override
	protected void registerPages(DetailsPart part) {
		part.setPageProvider(this);
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Estimation Approaches";
	}

	@Override
	protected Control createItemsList(Composite composite) {
		tableApproachesViewer = CheckboxTableViewer.newCheckList(composite,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		tableApproaches = tableApproachesViewer.getTable();
		tableApproaches.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		toolkit.paintBordersFor(tableApproaches);

		tableApproachesViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		tableApproachesViewer
				.setContentProvider(new ObservableListContentProvider());

		// We need to add the existing validators in the configuration first, so that the test for
		// equality in the checked table binding works correctly. EMF always does an equality check on
		// the object instance.
		IObservableList approaches = new WritableList();
		Set<Class<?>> existingApproaches = new HashSet<Class<?>>();
		for (EstimationApproachConfiguration v : model.getEstimation().getApproaches()) {
			approaches.add(v);
			existingApproaches.add(v.getType());
		}
		for (Class<?> cl : Registry.INSTANCE
				.getImplementationClasses(IEstimationApproach.class)) {
			if (!existingApproaches.contains(cl)) {
				EstimationApproachConfiguration a = ConfigurationFactory.eINSTANCE.createEstimationApproachConfiguration();
				a.setType(cl);
				approaches.add(a);
			}
		}
		tableApproachesViewer.setInput(approaches);
		tableApproachesViewer.addSelectionChangedListener(this);
		
		// Binding: All checked approaches are added to the model instance
		masterBindingContext
				.bindSet(
						ViewerProperties.checkedElements(
								EstimationApproachConfiguration.class).observe(
								tableApproachesViewer),
						EMFEditProperties
								.set(domain,
										FeaturePath
												.fromList(
														ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__ESTIMATION,
														ConfigurationPackage.Literals.ESTIMATION_SPECIFICATION__APPROACHES))
								.observe(model));

		return tableApproaches;
	}

	@Override
	public Object getPageKey(Object object) {
		if (object instanceof EstimationApproachConfiguration) {
			return ((EstimationApproachConfiguration)object).getType();
		}
		return null;
	}

	@Override
	public IDetailsPage getPage(Object key) {
		if (key instanceof Class<?>) {
			return new ParametersDetailsPage(page, 
					domain, 
					"Estimation Approach Configuration", 
					ConfigurationPackage.Literals.ESTIMATION_APPROACH_CONFIGURATION,
					(Class<?>) key, 
					ConfigurationPackage.Literals.ESTIMATION_APPROACH_CONFIGURATION__PARAMETERS);
		}
		return null;
	}
}
