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
package tools.descartes.librede;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.validation.IValidator;

public class ApproachResult {

	private Class<? extends IEstimationApproach> approach;
	private ResultTable[] result;

	public ApproachResult(Class<? extends IEstimationApproach> approach, int numFolds) {
		this.approach = approach;
		this.result = new ResultTable[numFolds];
	}

	public Class<? extends IEstimationApproach> getApproach() {
		return approach;
	}

	public ResultTable[] getResult() {
		return result;
	}

	public ResultTable getResultOfFold(int fold) {
		return result[fold];
	}

	public void addEstimate(int idx, ResultTable estimates) {
		this.result[idx] = estimates;
	}

	// returns a Vector which contains the mean estimate of every entity
	public Vector getMeanEstimates() {
		MatrixBuilder lastEstimates = MatrixBuilder.create(result[0].getStateVariables().length);
		// for all folds do
		for (int i = 0; i < result.length; i++) {
			lastEstimates.addRow(result[i].getLastEstimates());
		}
		Vector meanEstimates = LinAlg.mean(lastEstimates.toMatrix());
		return meanEstimates;
	}

	// returns a Map which maps a validator to a Vector
	// the Vector contains the means of validation errors for
	// every entitity
	public Map<Class<? extends IValidator>, Vector> getValidationErrors() {
		Map<Class<? extends IValidator>, Vector> meanErrors = new HashMap<Class<? extends IValidator>, Vector>();
		for (Class<? extends IValidator> validator : result[0].getValidators()) {
			MatrixBuilder errorsBuilder = null;
			// for all folds do
			for (int i = 0; i < result.length; i++) {
				ResultTable curFold = result[i];
				Vector curErr = curFold.getValidationErrors(validator);
				if (errorsBuilder == null) {
					errorsBuilder = MatrixBuilder.create(curErr.rows());
				}
				errorsBuilder.addRow(curErr);
			}

			Matrix errors = errorsBuilder.toMatrix();
			if (!errors.isEmpty()) {
				Vector curMeanErr = LinAlg.mean(errors);
				if (!meanErrors.containsKey(validator)) {
					meanErrors.put(validator, curMeanErr);
				}
			}
		}
		return meanErrors;
	}

	// returns a Map which maps a validator to a vector
	// the vector contains the means of validation predictions for
	// every entitity
	public Map<Class<? extends IValidator>, Vector> getValidationPredictions() {
		Map<Class<? extends IValidator>, Vector> meanPredictions = new HashMap<Class<? extends IValidator>, Vector>();
		for (Class<? extends IValidator> validator : result[0].getValidators()) {
			MatrixBuilder predictionsBuilder = null;
			for (int i = 0; i < result.length; i++) {
				ResultTable curFold = result[i];
				Vector curPred = curFold.getValidationPredictions(validator);
				if (predictionsBuilder == null) {
					predictionsBuilder = MatrixBuilder.create(curPred.rows());
				}
				predictionsBuilder.addRow(curPred);
			}

			Matrix predictions = predictionsBuilder.toMatrix();
			if (!predictions.isEmpty()) {
				Vector curMeanPred = LinAlg.mean(predictions);
				if (!meanPredictions.containsKey(validator)) {
					meanPredictions.put(validator, curMeanPred);
				}
			}
		}
		return meanPredictions;

	}

	// returns a Map of all validated entities for this approach
	public Map<Class<? extends IValidator>, List<ModelEntity>> getValidatedEntities() {
		Map<Class<? extends IValidator>, List<ModelEntity>> validatedEntities = new HashMap<Class<? extends IValidator>, List<ModelEntity>>();

		for (Class<? extends IValidator> validator : result[0].getValidators()) {
			for (int i = 0; i < result.length; i++) {
				ResultTable curFold = result[i];
				if (!validatedEntities.containsValue(curFold.getValidatedEntities(validator))) {
					validatedEntities.put(validator, curFold.getValidatedEntities(validator));
				}
			}
		}
		return validatedEntities;
	}

	public double getUtilizationError() {
		Map<Class<? extends IValidator>, Vector> errorMap = getValidationErrors();
		Set<Class<? extends IValidator>> valis = result[0].getValidators();
		Vector utilError = null;
		for (Class<? extends IValidator> vali : valis) {
			if (vali.getName().equals("Utilization Law Validator")) {
				utilError = errorMap.get(vali);
			}
		}

		if (utilError != null) {
			double errorSum = 0.0;
			for (int i = 0; i < utilError.columns(); i++) {
				errorSum += utilError.get(i);
			}
			return errorSum / utilError.columns();
		}

		return 0;
	}

	public double getResponseTimeError() {
		Map<Class<? extends IValidator>, Vector> errorMap = getValidationErrors();
		Set<Class<? extends IValidator>> valis = result[0].getValidators();
		Vector respTimeError = null;
		for (Class<? extends IValidator> vali : valis) {
			if (vali.getName().equals("Response Time Validator")) {
				respTimeError = errorMap.get(vali);
			}
		}

		if (respTimeError != null) {
			double errorSum = 0.0;
			for (int i = 0; i < respTimeError.columns(); i++) {
				errorSum += respTimeError.get(i);
			}
			return errorSum / respTimeError.columns();
		}

		return 0;
	}

}
