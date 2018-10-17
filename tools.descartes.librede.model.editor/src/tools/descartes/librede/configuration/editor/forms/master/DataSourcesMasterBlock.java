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
package tools.descartes.librede.configuration.editor.forms.master;

import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
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

import tools.descartes.librede.configuration.ConfigurationFactory;
import tools.descartes.librede.configuration.ConfigurationPackage;
import tools.descartes.librede.configuration.DataSourceConfiguration;
import tools.descartes.librede.configuration.InputSpecification;
import tools.descartes.librede.configuration.LibredeConfiguration;
import tools.descartes.librede.configuration.editor.forms.ClassesViewerFilter;
import tools.descartes.librede.configuration.editor.forms.details.ParametersDetailsPage;
import tools.descartes.librede.datasource.IDataSource;
import tools.descartes.librede.registry.Registry;

public class DataSourcesMasterBlock extends AbstractMasterBlockWithButtons implements ISelectionChangedListener, IDetailsPageProvider {
	
	private Table tableSources;
	private TableViewer tableSourcesViewer;

	/**
	 * Create the master details block.
	 */
	public DataSourcesMasterBlock(AdapterFactoryEditingDomain domain, LibredeConfiguration model) {
		super(domain, model);
	}

	/**
	 * Register the pages.
	 * @param part
	 */
	@Override
	protected void registerPages(DetailsPart part) {
		part.setPageProvider(this);
	}

	@Override
	protected String getMasterSectionTitle() {
		return "All Data Sources";
	}

	@Override
	protected Control createItemsList(Composite composite) {
		tableSourcesViewer = new TableViewer(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tableSources = tableSourcesViewer.getTable();
		
		tableSourcesViewer.setContentProvider(new AdapterFactoryContentProvider(page.getAdapterFactory()));
		tableSourcesViewer.setLabelProvider(new AdapterFactoryLabelProvider(page.getAdapterFactory()));
		
		tableSourcesViewer.setInput(model.getInput());
		tableSourcesViewer.addFilter(new ClassesViewerFilter(InputSpecification.class, DataSourceConfiguration.class));
		tableSourcesViewer.addSelectionChangedListener(this);
		
		registerViewer(tableSourcesViewer);
		
		return tableSources;
	}

	@Override
	protected void handleAdd() {
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(page.getSite().getShell(), new LabelProvider() {
			@Override
			public String getText(Object element) {
				return Registry.INSTANCE.getDisplayName(Registry.INSTANCE.getInstanceClass((String)element));
			}
		});
		dialog.setElements(Registry.INSTANCE.getInstances(IDataSource.class).toArray());
		dialog.setAllowDuplicates(false);
		dialog.setMultipleSelection(false);
		dialog.setTitle("Data Source Types");
		dialog.setMessage("Select a type for the new data source:");
		dialog.create();
		if (dialog.open() == Window.OK) {
			addDataProviders(dialog.getResult());
		}		
	}

	private void addDataProviders(Object[] results) {
		for (Object r : results) {
			DataSourceConfiguration source = ConfigurationFactory.eINSTANCE.createDataSourceConfiguration();
			source.setName("New Data Source");
			source.setType((String)r);
			
			Command cmd = AddCommand.create(domain, model.getInput(), ConfigurationPackage.Literals.INPUT_SPECIFICATION__DATA_SOURCES, source);
			domain.getCommandStack().execute(cmd);
		}
	}

	@Override
	protected void handleRemove() {
		IStructuredSelection selection = (IStructuredSelection)tableSourcesViewer.getSelection();
		Iterator<?> iterator = selection.iterator();
		while(iterator.hasNext()) {
			Object o = iterator.next();
			if (o instanceof DataSourceConfiguration) {
				domain.getCommandStack().execute(RemoveCommand.create(domain, o));
			}
		}		
	}

	@Override
	public Object getPageKey(Object object) {
		if (object instanceof DataSourceConfiguration) {
			return ((DataSourceConfiguration)object).getType();
		}
		return null;
	}

	@Override
	public IDetailsPage getPage(Object key) {
		if (key instanceof String) {
			return new ParametersDetailsPage(page, 
					domain, 
					"Data Source Configuration", 
					ConfigurationPackage.Literals.DATA_SOURCE_CONFIGURATION,
					(String)key, 
					ConfigurationPackage.Literals.DATA_SOURCE_CONFIGURATION__PARAMETERS);
		}
		return null;
	}
}
