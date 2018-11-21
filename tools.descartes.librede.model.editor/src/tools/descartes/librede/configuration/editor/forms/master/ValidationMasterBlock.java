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
package tools.descartes.librede.configuration.editor.forms.master;

import java.util.HashSet;
import java.util.Set;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.ValidatorConfiguration;
import tools.descartes.librede.configuration.editor.forms.details.ParametersDetailsPage;
import tools.descartes.librede.registry.Registry;
import tools.descartes.librede.validation.IValidator;

public class ValidationMasterBlock extends AbstractMasterBlock implements IDetailsPageProvider {
	
	private Table tableValidators;
	private CheckboxTableViewer tableValidatorsViewer;

	private EMFDataBindingContext masterBindingContext = new EMFDataBindingContext();

	public ValidationMasterBlock(AdapterFactoryEditingDomain domain, LibredeConfiguration model) {
		super(domain, model);
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Validators";
	}

	@Override
	protected Control createItemsList(Composite parent) {
		tableValidatorsViewer = CheckboxTableViewer.newCheckList(parent,
				SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
		tableValidators = tableValidatorsViewer.getTable();
		tableValidators.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1));
		toolkit.paintBordersFor(tableValidators);

		tableValidatorsViewer
				.setContentProvider(new ObservableListContentProvider());
		tableValidatorsViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));

		// We need to add the existing validators in the configuration first, so that the test for
		// equality in the checked table binding works correctly. EMF always does an equality check on
		// the object instance.
		IObservableList validators = new WritableList();
		Set<String> existingValidators = new HashSet<String>();
		for (ValidatorConfiguration v : model.getValidation().getValidators()) {
			validators.add(v);
			existingValidators.add(v.getType());
		}
		for (String instance : Registry.INSTANCE
				.getInstances(IValidator.class)) {
			if (!existingValidators.contains(instance)) {
				ValidatorConfiguration a = ConfigurationFactory.eINSTANCE
						.createValidatorConfiguration();
				a.setType(instance);
				validators.add(a);
			}
		}
		tableValidatorsViewer.setInput(validators);
		tableValidatorsViewer.addSelectionChangedListener(this);
		
		registerViewer(tableValidatorsViewer);
		
		// Binding: All checked validators are added to the model instance
		masterBindingContext
				.bindSet(
						ViewerProperties.checkedElements(
								ValidatorConfiguration.class).observe(
										tableValidatorsViewer),
						EMFEditProperties
								.set(domain,
										FeaturePath
												.fromList(
														ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__VALIDATION,
														ConfigurationPackage.Literals.VALIDATION_SPECIFICATION__VALIDATORS))
								.observe(model));

		return tableValidators;
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.setPageProvider(this);
	}

	@Override
	public Object getPageKey(Object object) {
		if (object instanceof ValidatorConfiguration) {
			return ((ValidatorConfiguration)object).getType();
		}
		return null;
	}

	@Override
	public IDetailsPage getPage(Object key) {
		return new ParametersDetailsPage(page, 
				domain, 
				"Validator Configuration", 
				ConfigurationPackage.Literals.VALIDATOR_CONFIGURATION,
				(String)key, 
				ConfigurationPackage.Literals.VALIDATOR_CONFIGURATION__PARAMETERS);
	}

}
