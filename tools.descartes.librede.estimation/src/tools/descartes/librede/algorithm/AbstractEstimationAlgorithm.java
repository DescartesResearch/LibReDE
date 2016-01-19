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
package tools.descartes.librede.algorithm;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.rules.DataDependency;
import tools.descartes.librede.repository.rules.IRuleActivationHandler;
import tools.descartes.librede.repository.rules.Rule;

/**
 * This abstract class provides standard implementations for some of the methods in {@link IEstimationAlgorithm}.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public abstract class AbstractEstimationAlgorithm implements IEstimationAlgorithm, IRuleActivationHandler {
	
	private static final Logger log = Logger.getLogger(AbstractEstimationAlgorithm.class);
	
	private IStateModel<?> stateModel;
	private IObservationModel<?, ?> observationModel;
	private IRepositoryCursor cursor;
	private Rule activationRule = new Rule();
	private boolean activated = true;
	
	@Override
	public void initialize(IStateModel<?> stateModel, IObservationModel<?, ?> observationModel,
			IRepositoryCursor cursor, int estimationWindow) throws InitializationException {
		this.stateModel = stateModel;
		this.observationModel = observationModel;
		this.cursor = cursor;
		activationRule.addDependencies(stateModel.getDataDependencies());
		activationRule.addDependencies(observationModel.getDataDependencies());
		activationRule.setActivationHandler(this);
		cursor.getRepository().addRule(activationRule);
	}

	@Override
	public IStateModel<?> getStateModel() {
		return stateModel;
	}

	@Override
	public IObservationModel<?, ?> getObservationModel() {
		return observationModel;
	}

	@Override
	public void destroy() {
		cursor.getRepository().removeRule(activationRule);
	}
	
	@Override
	public boolean isApplicable() {
		return activated;
	}
	
	@Override
	public void activateRule(IMonitoringRepository repository, Rule.Status rule, ModelEntity entity) {
		if (!activated) {
			log.info("Activated algorithm: " + stateModel);
			activated = true;
		}
	}
	
	@Override
	public void deactivateRule(IMonitoringRepository repository, Rule.Status rule, ModelEntity entity) {
		if (activated) {
			StringBuilder message = new StringBuilder("Deactivated algorithm: ");
			message.append(stateModel.toString()).append("\n");			
			for (DataDependency<?>.Status depStatus : rule.getDependenciesStatus()) {
				if (!depStatus.isResolved()) {
					message.append("    Missing dependency: ");
					message.append(depStatus.getDependency().getMetric().getName());
					message.append("(").append(depStatus.getDependency().getAggregation()).append(")");
					message.append(" for entities ");
					boolean first = true;
					for (ModelEntity missing : depStatus.getMissingEntities()) {
						if (first) {
							first = false;
						} else {
							message.append(", ");
						}
						message.append(missing.getName());
					}
					message.append("\n");
				}
			}
			log.info(message);
			activated = false;
		}
	}
}
