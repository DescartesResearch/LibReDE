package net.descartesresearch.librede.configuration.editor.forms.details;

import net.descartesresearch.librede.configuration.ExporterConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;

import org.eclipse.emf.edit.domain.EditingDomain;

public class OutputDetailsPage extends AbstractParametersDetailsPage {

	public OutputDetailsPage(AbstractEstimationConfigurationFormPage page,
			EditingDomain domain, LibredeConfiguration model,
			Class<?> contentType) {
		super(page, domain, model, contentType);
	}

	@Override
	protected Object getParameterList(Object element) {
		if (element instanceof ExporterConfiguration) {
			return ((ExporterConfiguration)element).getParameters();
		}
		return null;
	}

	@Override
	protected String getDetailsSectionTitle() {
		return "Exporter Configuration";
	}

}
