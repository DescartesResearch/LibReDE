package net.descartesresearch.librede.configuration.editor.forms.details;

import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.ValidatorConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;

import org.eclipse.emf.edit.domain.EditingDomain;

public class ValidationDetailsPage extends AbstractParametersDetailsPage {

	public ValidationDetailsPage(AbstractEstimationConfigurationFormPage page,
			EditingDomain domain, LibredeConfiguration model,
			Class<?> contentType) {
		super(page, domain, model, contentType);
	}

	@Override
	protected Object getParameterList(Object element) {
		if (element instanceof ValidatorConfiguration) {
			return ((ValidatorConfiguration)element).getParameters();
		}
		return null;
	}

	@Override
	protected String getDetailsSectionTitle() {
		return "Validator Configuration";
	}
}
