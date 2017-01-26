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
package tools.descartes.librede.validation;

import java.util.ArrayList;
import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.ResourceDemand;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.State;
import tools.descartes.librede.models.observation.equations.UtilizationLawEquation;
import tools.descartes.librede.models.observation.equations.UtilizationValue;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.rules.DataDependency;

@Component(displayName = "Utilization Law Validator (Relative)")
public class UtilizationValidator implements IValidator {
	
	private final List<DataDependency<?>> dependencies = new ArrayList<>();
	private List<ModelEntity> resources;
	private List<UtilizationValue> utilObservation;
	private List<UtilizationLawEquation> utilLaw;
	private MatrixBuilder allErrors;
	private MatrixBuilder predictedUtilization;
	private MatrixBuilder observedUtilization;
	private ConstantStateModel<Unconstrained> stateModel;
	
	@Override
	public void initialize(WorkloadDescription workload, IRepositoryCursor cursor) {
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		for (Resource res : workload.getResources()) {
			for (ResourceDemand demand : res.getDemands()) {
				builder.addVariable(demand);
			}
		}
		stateModel = builder.build(); 
		
		this.utilObservation = new ArrayList<>();
		this.utilLaw = new ArrayList<UtilizationLawEquation>();
		this.resources = new ArrayList<ModelEntity>();
		for (Resource res : stateModel.getResources()) {
			if (res.getSchedulingStrategy() != SchedulingStrategy.IS) {
				UtilizationValue util = new UtilizationValue(stateModel, cursor, res, 0);
				UtilizationLawEquation law = new UtilizationLawEquation(stateModel, cursor, res, 0);
				dependencies.addAll(law.getDataDependencies());
				dependencies.addAll(util.getDataDependencies());
				utilLaw.add(law);
				utilObservation.add(util);
				resources.add(res);
			}
		}
		allErrors = MatrixBuilder.create(resources.size());
		predictedUtilization = MatrixBuilder.create(resources.size());
		observedUtilization = MatrixBuilder.create(resources.size());
	}
	
	@Override
	public IStateModel<?> getStateModel() {
		return stateModel;
	}
	
	@Override
	public void predict(Vector state) {
		State x = new State(stateModel, state);
		double[] relErr = new double[utilLaw.size()];
		double[] actualUtil = new double[utilLaw.size()];
		double[] realUtil = new double[utilLaw.size()];
		for (int i = 0; i < utilLaw.size(); i++) {
			realUtil[i] = utilObservation.get(i).getConstantValue();
			if (Double.isNaN(realUtil[i])) {
				// replace NaN with MAX_VALUE
				realUtil[i] = Double.MAX_VALUE;
			}
			actualUtil[i] = utilLaw.get(i).getValue(x).getValue();
			if (Double.isNaN(actualUtil[i])) {
				// replace NaN with MAX_VALUE
				actualUtil[i] = Double.MAX_VALUE;
			}
			relErr[i] = returnErrorValue(actualUtil[i], realUtil[i]);
		}
		allErrors.addRow(relErr);
		predictedUtilization.addRow(actualUtil);
		observedUtilization.addRow(realUtil);
	}
	
	/**
	 * Returns the required error value (relative or absolute)
	 * 
	 * @param actual
	 *            The actual utilization
	 * @param real
	 *            The real utilization
	 * @return The error
	 */
	protected double returnErrorValue(double actual, double real) {
		if (real != 0) {
			// to avoid dividing by zero resulting in NaN
			return Math.abs(actual - real) / real;
		} else {
			return Math.abs(actual - real);
		}
	}

	
	@Override
	public Vector getPredictionError() {
		return checkedMean(allErrors.toMatrix());
	}
	
	public Vector getPredictedValues() {
		return checkedMean(predictedUtilization.toMatrix());
	}
	
	public Vector getObservedValues() {
		return checkedMean(observedUtilization.toMatrix());
	}
	
	@Override
	public List<ModelEntity> getModelEntities() {
		return resources;
	}
	
	private Vector checkedMean(Matrix matrix) {
		if (matrix.isEmpty()) {
			return LinAlg.empty();
		}
		return LinAlg.mean(matrix);
	}
	
	@Override
	public List<DataDependency<?>> getDataDependencies() {
		return dependencies;
	}

}
