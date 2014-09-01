package net.descartesresearch.librede.configuration.editor.forms.master;

import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public abstract class AbstractMasterBlockWithButtons extends AbstractMasterBlock {

	public AbstractMasterBlockWithButtons(
			AbstractEstimationConfigurationFormPage page, EditingDomain domain,
			LibredeConfiguration model) {
		super(page, domain, model);
	}
	
	@Override
	protected void createButtons(Composite parent) {
		Button btnAdd = toolkit.createButton(parent, "Add...", SWT.NONE);
		GridData gd_btnAdd = new GridData();
		gd_btnAdd.widthHint = 90;
		gd_btnAdd.verticalAlignment = SWT.TOP;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleAdd();
			}			
		});
		
		Button btnRemove = toolkit.createButton(parent, "Remove", SWT.NONE);
		GridData gd_btnRemove = new GridData();
		gd_btnRemove.widthHint = 90;
		gd_btnRemove.verticalAlignment = SWT.TOP;
		btnRemove.setLayoutData(gd_btnRemove);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				handleRemove();
			}			
		});
		
	}
	
	protected abstract void handleAdd();
	
	protected abstract void handleRemove();

}
