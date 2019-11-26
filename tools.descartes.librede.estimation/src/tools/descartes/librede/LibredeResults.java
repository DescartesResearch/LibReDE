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
package tools.descartes.librede;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import tools.descartes.librede.approach.IEstimationApproach;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;
import tools.descartes.librede.validation.IValidator;

/**
 * This class describes the results of an execution of Librede. It stores
 * estimates for each approach and their corresponding errors.
 * 
 * @author Simon Spinner
 *
 */
public class LibredeResults {

	private final Map<Class<? extends IEstimationApproach>, ApproachResult> approachResults = new HashMap<>();
	private final int numFolds;
	private final List<Class<? extends IEstimationApproach>> selectedApproaches = new ArrayList<>();

	public LibredeResults(int numApproaches, int numFolds) {
		this.numFolds = numFolds;
	}

	public void addEstimates(Class<? extends IEstimationApproach> approach, int fold, ResultTable estimates) {
		ApproachResult appRes = approachResults.get(approach);
		if (appRes == null) {
			appRes = new ApproachResult(approach, numFolds);
		}
		appRes.addEstimate(fold, estimates);
		approachResults.put(approach, appRes);
	}

	public void clear() {
		approachResults.clear();
	}

	public void setSelectedApproaches(List<Class<? extends IEstimationApproach>> newSelection) {
		selectedApproaches.clear();
		selectedApproaches.addAll(newSelection);
	}

	public List<Class<? extends IEstimationApproach>> getSelectedApproaches() {
		return selectedApproaches;
	}

	public ResultTable getEstimates(Class<? extends IEstimationApproach> approach, int fold) {
		ApproachResult appRes = approachResults.get(approach);
		ResultTable result = appRes.getResultOfFold(fold);
		return result;
	}

	public int getNumberOfFolds() {
		return numFolds;
	}

	public Set<Class<? extends IEstimationApproach>> getApproaches() {
		return approachResults.keySet();
	}

	public ApproachResult getApproachResults(Class<? extends IEstimationApproach> approach) {
		return approachResults.get(approach);
	}

	public Map<Class<? extends IEstimationApproach>, Matrix> getAllEstimates() {
		Map<Class<? extends IEstimationApproach>, Matrix> allApproachResults = new HashMap<>();
		for (Class<? extends IEstimationApproach> approach : getApproaches()) {
			allApproachResults.put(approach, approachResults.get(approach).getMeanEstimates());
		}
		return allApproachResults;
	}

	// fetches all validated entities of all ApproachResults
	// adds validator -> entity if
	// validator is not part of the validatedEntities Map
	// entity is not part of the validatedEntities Map
	// validator -> entity is not part of the validatedEntities Map
	public Map<Class<? extends IValidator>, List<ModelEntity>> getValidatedEntities() {
		Map<Class<? extends IValidator>, List<ModelEntity>> validatedEntities = new HashMap<Class<? extends IValidator>, List<ModelEntity>>();
		for (ApproachResult appRes : approachResults.values()) {
			// Class<? extends IValidator>, List<ModelEntity>
			Map<Class<? extends IValidator>, List<ModelEntity>> validEntities = appRes.getValidatedEntities();
			for (Class<? extends IValidator> vali : validEntities.keySet()) {
				if (!validatedEntities.containsKey(vali) || validatedEntities.containsValue(validEntities.get(vali))
						|| !validEntities.get(vali).equals(validatedEntities.get(vali))) {
					validatedEntities.put(vali, validEntities.get(vali));
				}
			}
		}
		return validatedEntities;

	}

	// returns a Map approach -> Matrix
	// the Matrix contains of 2 rows one row for each validator
	// the values are meanErrors of every fold per entity
	public Map<Class<? extends IEstimationApproach>, Matrix> getValidationErrors() {
		Map<Class<? extends IEstimationApproach>, Matrix> validationErrors = new HashMap<>();
		// iterate over all ApproachResults (entities)
		for (ApproachResult appRes : approachResults.values()) {
			MatrixBuilder valiErrorsBuilder = MatrixBuilder.create(appRes.getResult().length);
			double[] valiErrorsResp = new double[appRes.getResult().length];
			double[] valiErrorsUtil = new double[appRes.getResult().length];
			// iterate over all folds
			for (int j = 0; j < appRes.getResult().length; j++) {
				ResultTable appResFold = appRes.getResultOfFold(j);
				Set<Class<? extends IValidator>> appResFoldValis = appResFold.getValidators();
				// iterate over all validators
				for (Class<? extends IValidator> vali : appResFoldValis) {
					Vector errors = appResFold.getValidationErrors(vali);
					double meanErrorsFold = 0.0;
					for (int i = 0; i < errors.rows(); i++) {
						meanErrorsFold += errors.get(i);
					}
					meanErrorsFold = meanErrorsFold / appRes.getResult().length;
					if (vali.getName().equals("tools.descartes.librede.validation.ResponseTimeValidator")) {
						valiErrorsResp[j] = meanErrorsFold;
					} else {
						valiErrorsUtil[j] = meanErrorsFold;
					}
				}
			}

			// add values of array to a vector to add it to the matrix
			VectorBuilder valiErrorsRespVect = VectorBuilder.create(appRes.getResult().length);
			VectorBuilder valiErrorsUtilVect = VectorBuilder.create(appRes.getResult().length);
			for (int i = 0; i < valiErrorsUtil.length; i++) {
				valiErrorsRespVect.add(valiErrorsResp[i]);
				valiErrorsUtilVect.add(valiErrorsUtil[i]);
			}
			valiErrorsBuilder.addRow(valiErrorsRespVect.toVector());

			valiErrorsBuilder.addRow(valiErrorsUtilVect.toVector());

			validationErrors.put(appRes.getApproach(), valiErrorsBuilder.toMatrix());
		}
		return validationErrors;

	}

	public double getApproachUtilizationError(Class<? extends IEstimationApproach> approach) {
		ApproachResult appRes = approachResults.get(approach);
		return appRes.getUtilizationError();
	}

	public double getApproachResponseTimeError(Class<? extends IEstimationApproach> approach) {
		ApproachResult appRes = approachResults.get(approach);
		return appRes.getResponseTimeError();
	}

	// returns a Map approach -> Matrix
	// the Matrix contains of 2 rows, one row for each validator
	// the values are meanPredictions of every fold per entity
	public Map<Class<? extends IEstimationApproach>, Matrix> getValidationPredictions() {
		Map<Class<? extends IEstimationApproach>, Matrix> validationPredictions = new HashMap<>();
		// iterate over alll ApproachResults (every entity)
		for (ApproachResult appRes : approachResults.values()) {
			MatrixBuilder valiPredBuilder = MatrixBuilder.create(appRes.getResult().length);
			double[] valiPredResp = new double[appRes.getResult().length];
			double[] valiPredUtil = new double[appRes.getResult().length];
			// iterate over folds
			for (int i = 0; i < appRes.getResult().length; i++) {
				ResultTable appResFold = appRes.getResultOfFold(i);
				Set<Class<? extends IValidator>> appResFoldValis = appResFold.getValidators();
				// iterate over validators
				for (Class<? extends IValidator> vali : appResFoldValis) {
					Vector preds = appResFold.getValidationPredictions(vali);
					double meanPredsFold = 0.0;
					for (int j = 0; j < preds.rows(); j++) {
						meanPredsFold += preds.get(j);
					}
					meanPredsFold = meanPredsFold / appRes.getResult().length;
					if (vali.getName().equals("tools.descartes.librede.validation.ResponseTimeValidator")) {
						valiPredResp[i] = meanPredsFold;
					} else {
						valiPredUtil[i] = meanPredsFold;
					}
				}
			}

			// add values of array to a vector to add it to the matrix
			VectorBuilder valiPredRespVect = VectorBuilder.create(appRes.getResult().length);
			VectorBuilder valiPredUtilVect = VectorBuilder.create(appRes.getResult().length);
			for (int i = 0; i < valiPredUtil.length; i++) {
				valiPredRespVect.add(valiPredResp[i]);
				valiPredUtilVect.add(valiPredUtil[i]);
			}
			valiPredBuilder.addRow(valiPredRespVect.toVector());
			valiPredBuilder.addRow(valiPredUtilVect.toVector());

			validationPredictions.put(appRes.getApproach(), valiPredBuilder.toMatrix());
		}
		return validationPredictions;
	}

}
