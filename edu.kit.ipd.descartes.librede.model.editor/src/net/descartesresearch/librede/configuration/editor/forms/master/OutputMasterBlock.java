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

import java.util.Iterator;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.ExporterConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.details.ParametersDetailsPage;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IDetailsPageProvider;
import org.eclipse.ui.forms.IManagedForm;

import edu.kit.ipd.descartes.librede.export.IExporter;
import edu.kit.ipd.descartes.librede.factory.Registry;

public class OutputMasterBlock extends AbstractMasterBlockWithButtons implements IDetailsPageProvider {
	
	private Table tableExporters;
	private TableViewer tableExportersViewer;

	public OutputMasterBlock(AdapterFactoryEditingDomain domain, LibredeConfiguration model) {
		super(domain, model);
	}

	@Override
	protected void handleAdd() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(page.getSite().getShell(), new LabelProvider() {
			@Override
			public String getText(Object element) {
				return Registry.INSTANCE.getDisplayName((Class<?>)element);
			}
		});
		dialog.setElements(Registry.INSTANCE.getImplementationClasses(IExporter.class).toArray());
		dialog.setAllowDuplicates(false);
		dialog.setMultipleSelection(false);
		dialog.setTitle("Exporter Types");
		dialog.setMessage("Select a type for the new exporter:");
		dialog.create();
		if (dialog.open() == Window.OK) {
			addExporters(dialog.getResult());
		}	
	}
	
	private void addExporters(Object[] results) {
		for (Object r : results) {
			ExporterConfiguration source = ConfigurationFactory.eINSTANCE.createExporterConfiguration();
			source.setName("New Exporter");
			source.setType((Class<?>)r);
			
			Command cmd = AddCommand.create(domain, model.getOutput(), ConfigurationPackage.Literals.OUTPUT_SPECIFICATION__EXPORTERS, source);
			domain.getCommandStack().execute(cmd);
		}
	}

	@Override
	protected void handleRemove() {
		IStructuredSelection selection = (IStructuredSelection)tableExportersViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while(iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof ExporterConfiguration) {
				domain.getCommandStack().execute(RemoveCommand.create(domain, o));
			}
		}		
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Exporters";
	}

	@Override
	protected Control createItemsList(Composite parent) {
		tableExportersViewer = new TableViewer(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tableExporters = tableExportersViewer.getTable();
		
		tableExportersViewer.setContentProvider(new AdapterFactoryContentProvider(page.getAdapterFactory()));
		tableExportersViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		tableExportersViewer.setInput(model.getOutput());
		tableExportersViewer.addSelectionChangedListener(this);
		
		return tableExporters;
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.setPageProvider(this);
	}

	@Override
	public Object getPageKey(Object object) {
		if (object instanceof ExporterConfiguration) {
			return ((ExporterConfiguration)object).getType();
		}
		return null;
	}

	@Override
	public IDetailsPage getPage(Object key) {
		return new ParametersDetailsPage(page, 
				domain, 
				"Exporter Configuration", 
				ConfigurationPackage.Literals.EXPORTER_CONFIGURATION,
				(Class<?>)key, 
				ConfigurationPackage.Literals.EXPORTER_CONFIGURATION__PARAMETERS);
	}

}
