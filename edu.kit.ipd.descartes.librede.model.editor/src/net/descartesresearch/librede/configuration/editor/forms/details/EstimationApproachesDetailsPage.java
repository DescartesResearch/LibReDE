package net.descartesresearch.librede.configuration.editor.forms.details;

import net.descartesresearch.librede.configuration.EstimationApproachConfiguration;
import net.descartesresearch.librede.configuration.LibredeConfiguration;
import net.descartesresearch.librede.configuration.editor.forms.AbstractEstimationConfigurationFormPage;

import org.eclipse.emf.edit.domain.EditingDomain;

public class EstimationApproachesDetailsPage extends
		AbstractParametersDetailsPage {

	public EstimationApproachesDetailsPage(
			AbstractEstimationConfigurationFormPage page, EditingDomain domain,
			LibredeConfiguration model, Class<?> contentType) {
		super(page, domain, model, contentType);
	}

	@Override
	protected String getDetailsSectionTitle() {
		return "Estimation Approach Configuration";
	}

	@Override
	protected Object getParameterList(Object element) {
		if (element instanceof EstimationApproachConfiguration) {
			return ((EstimationApproachConfiguration)element).getParameters();
		}
		return null;
	}
}
