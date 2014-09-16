package net.descartesresearch.librede.configuration.editor.forms;

import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.master.AbstractMasterBlock;
import net.descartesresearch.librede.configuration.presentation.ConfigurationEditor;

import org.eclipse.emf.databinding.EMFDataBindingContext;
import org.eclipse.emf.databinding.FeaturePath;
import org.eclipse.emf.databinding.edit.EMFEditProperties;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class ValidationFormPage extends MasterDetailsFormPage {
	
	private Button btnCrossValidation;
	private Spinner spnFolds;
	private EMFDataBindingContext bindingContext = new EMFDataBindingContext();

	public ValidationFormPage(ConfigurationEditor editor, String id,
			String title, String icon,
			AdapterFactoryEditingDomain editingDomain,
			LibredeConfiguration model, AbstractMasterBlock masterBlock) {
		super(editor, id, title, icon, editingDomain, model, masterBlock);
	}
	
	@Override
	protected void createFormContentBeginning(FormToolkit toolkit,
			Composite parent) {
		super.createFormContentBeginning(toolkit, parent);
		createCrossValidationSection(toolkit, parent);
	}
	
	private void createCrossValidationSection(FormToolkit toolkit, Composite body) {
		Section sctnCrossValidation = toolkit.createSection(body, Section.TWISTIE | Section.TITLE_BAR);
		GridData gd_sctnCrossValidation = new GridData(GridData.FILL_HORIZONTAL);
		gd_sctnCrossValidation.horizontalSpan = 2;
		sctnCrossValidation.setLayoutData(gd_sctnCrossValidation);
		sctnCrossValidation.setText("Cross-Validation Settings");
		sctnCrossValidation.setExpanded(true);
		
		Composite client = toolkit.createComposite(sctnCrossValidation, SWT.NONE);
		sctnCrossValidation.setClient(client);
		toolkit.paintBordersFor(client);
		client.setLayout(new GridLayout(2, false));
		
		btnCrossValidation = toolkit.createButton(client, "Run k-Fold Cross-Validation", SWT.CHECK);
		GridData gd_btnCrossValidation = new GridData();
		gd_btnCrossValidation.horizontalSpan = 2;
		btnCrossValidation.setLayoutData(gd_btnCrossValidation);
		
		toolkit.createLabel(client, "Number of Folds k:");
		spnFolds = new Spinner(client, SWT.BORDER);
		spnFolds.setMinimum(2);
		spnFolds.setMaximum(Integer.MAX_VALUE);
		toolkit.paintBordersFor(spnFolds);
		
		bindingContext.bindValue(WidgetProperties.selection().observe(btnCrossValidation), 
				EMFEditProperties.value(
						getEditingDomain(), 
						FeaturePath.fromList(
								ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__VALIDATION,
								ConfigurationPackage.Literals.VALIDATION_SPECIFICATION__VALIDATE_ESTIMATES
						)).observe(getModel()));
		bindingContext.bindValue(WidgetProperties.selection().observe(spnFolds), 
				EMFEditProperties.value(
						getEditingDomain(),
						FeaturePath.fromList(
								ConfigurationPackage.Literals.LIBREDE_CONFIGURATION__VALIDATION,
								ConfigurationPackage.Literals.VALIDATION_SPECIFICATION__VALIDATION_FOLDS
						)).observe(getModel()));
	}

}
