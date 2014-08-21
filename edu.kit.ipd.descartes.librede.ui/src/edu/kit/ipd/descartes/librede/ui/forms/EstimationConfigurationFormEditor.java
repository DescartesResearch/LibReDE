package edu.kit.ipd.descartes.librede.ui.forms;

import java.util.HashMap;

import net.descartesresearch.librede.configuration.ConfigurationFactory;
import net.descartesresearch.librede.configuration.ConfigurationPackage;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.TimeSeries;
import net.descartesresearch.librede.configuration.provider.ConfigurationItemProviderAdapterFactory;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.ReflectiveItemProviderAdapterFactory;
import org.eclipse.emf.edit.provider.resource.ResourceItemProviderAdapterFactory;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;

public class EstimationConfigurationFormEditor extends FormEditor implements IEditingDomainProvider {

	public static final String ID = "edu.kit.ipd.descartes.librede.ui.forms.EstimationConfigurationFormEditor"; //$NON-NLS-1$

	protected LibredeConfiguration model;
	
	protected ComposedAdapterFactory adapterFactory;
	protected AdapterFactoryEditingDomain editingDomain;
	
	public EstimationConfigurationFormEditor() {
		this.model = ConfigurationFactory.eINSTANCE.createLibredeConfiguration();
		initModelStructure();
		initializeEditingDomain();
	}

	@Override
	protected void addPages() {
		AbstractEstimationConfigurationFormPage workload = new WorkloadDescriptionFormPage(this, "workloadmodel", "Workload Description", editingDomain, model);
		DataProvidersFormPage dataProviders = new DataProvidersFormPage(this, "dataproviders", "Data Providers", editingDomain, model);
		MeasurementDataFormPage measurements = new MeasurementDataFormPage(this, "measurements", "Measurements", editingDomain, model);
		EstimationApproachesFormPage approaches = new EstimationApproachesFormPage(this, "approaches", "Estimation Approaches");
		OutputFormPage output = new OutputFormPage(this, "output", "Output");
		try {
			addPage(workload);
			addPage(dataProviders);
			addPage(measurements);
			addPage(approaches);
			addPage(output);
		} catch (PartInitException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return true;
	}
	
	@Override
	public boolean isDirty() {
		return ((BasicCommandStack)editingDomain.getCommandStack()).isSaveNeeded();
	}
	
	public AdapterFactoryEditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	private void initModelStructure() {
		this.model.setWorkloadDescription(ConfigurationFactory.eINSTANCE.createWorkloadDescription());
		this.model.setInput(ConfigurationFactory.eINSTANCE.createInputSpecification());
		TimeSeries s1 = ConfigurationFactory.eINSTANCE.createTimeSeries();
		s1.setMetric("UTILIZATION");
		this.model.getInput().getObservations().add(s1);
	}
	
	protected void initializeEditingDomain() {
		// Create an adapter factory that yields item providers.
		adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);

		adapterFactory.addAdapterFactory(new ResourceItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ConfigurationItemProviderAdapterFactory());
		adapterFactory.addAdapterFactory(new ReflectiveItemProviderAdapterFactory());
		
		// Create the command stack that will notify this editor as commands are executed.
		BasicCommandStack commandStack = new BasicCommandStack();
		
		// Create the editing domain with a special command stack.
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack, new HashMap<org.eclipse.emf.ecore.resource.Resource, Boolean>());
	}

}
