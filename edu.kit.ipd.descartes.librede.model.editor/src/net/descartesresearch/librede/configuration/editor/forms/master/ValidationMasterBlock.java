package net.descartesresearch.librede.configuration.editor.forms.master;

import java.util.HashSet;
import java.util.Set;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;

import edu.kit.ipd.descartes.librede.estimation.validation.Validator;
import edu.kit.ipd.descartes.librede.factory.Registry;

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
		Set<Class<?>> existingValidators = new HashSet<Class<?>>();
		for (ValidatorConfiguration v : model.getValidation().getValidators()) {
			validators.add(v);
			existingValidators.add(v.getType());
		}
		for (Class<?> cl : Registry.INSTANCE
				.getImplementationClasses(Validator.class)) {
			if (!existingValidators.contains(cl)) {
				ValidatorConfiguration a = ConfigurationFactory.eINSTANCE
						.createValidatorConfiguration();
				a.setType(cl);
				validators.add(a);
			}
		}
		tableValidatorsViewer.setInput(validators);
		
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
				(Class<?>)key, 
				ConfigurationPackage.Literals.VALIDATOR_CONFIGURATION__PARAMETERS);
	}

}
