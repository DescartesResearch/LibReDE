package net.descartesresearch.librede.configuration.editor.forms.details;

import net.descartesresearch.librede.configuration.DataSourceConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;

import org.eclipse.emf.edit.domain.EditingDomain;

public class DataProviderDetailsPage extends AbstractParametersDetailsPage {
	
	/**
	 * Create the details page.
	 */
	public DataProviderDetailsPage(AbstractEstimationConfigurationFormPage page, EditingDomain domain, LibredeConfiguration model, Class<?> inputType) {
		super(page, domain, model,  inputType);
	}

	@Override
	protected String getDetailsSectionTitle() {
		return "Data Provider Configuration";
	}

	@Override
	protected Object getParameterList(Object element) {
		if (element instanceof DataSourceConfiguration) {
			return ((DataSourceConfiguration)element).getParameters();
		}
		return null;
	}
}
