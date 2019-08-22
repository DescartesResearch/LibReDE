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
package tools.descartes.librede.validation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.observation.equations.ResponseTimeEquation;
import tools.descartes.librede.models.observation.equations.ResponseTimeValue;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.InvocationGraph;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.rules.DataDependency;

@Component(displayName = "Weighted Response Time Validator")
public class WeightedResponseTimeValidator extends ResponseTimeValidator {

	private static final Logger log = Logger.getLogger(WeightedResponseTimeValidator.class);

	public void predict(Vector state) {
		State x = new State(super.getStateModel(), state);
		super.getStateModel().step(x);
		double[] relErr = new double[super.getRespEq().size()];
		double[] real = new double[super.getRespEq().size()];
		double[] actual = new double[super.getRespEq().size()];
		for (int i = 0; i < super.getRespEq().size(); i++) {
			real[i] = super.getRespObservation().get(i).getConstantValue();
			if (Double.isNaN(real[i])) {
				// replace NaN with MAX_VALUE
				real[i] = Double.MAX_VALUE;
			}
			actual[i] = respEq.get(i).getValue(x).getValue();
			if (Double.isNaN(actual[i])) {
				// replace NaN with MAX_VALUE
				actual[i] = Double.MAX_VALUE;
			}
			if (real[i] != 0) {
				// to avoid dividing by zero resulting in NaN
				relErr[i] = Math.abs(actual[i] - real[i]) / real[i];
			} else {
				relErr[i] = Math.abs(actual[i] - real[i]);
				throw new IllegalArgumentException("Computed error was NaN, through division by zero!");
			}
			if (Double.isNaN(relErr[i]) || Double.isNaN(actual[i]) || Double.isNaN(real[i])) {
				log.error("Computed error was NaN!");
			}
		}
		allErrors.addRow(relErr);
		predictedRespTimes.addRow(actual);
		observedRespTimes.addRow(real);
	}
}
