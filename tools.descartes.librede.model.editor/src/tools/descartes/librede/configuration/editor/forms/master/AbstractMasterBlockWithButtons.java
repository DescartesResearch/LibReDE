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

import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import tools.descartes.librede.configuration.LibredeConfiguration;

public abstract class AbstractMasterBlockWithButtons extends AbstractMasterBlock {

	public AbstractMasterBlockWithButtons(AdapterFactoryEditingDomain domain,
			LibredeConfiguration model) {
		super(domain, model);
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
