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

@Component(displayName = "Response Time Validator")
public class ResponseTimeValidator implements IValidator {

	private static final Logger log = Logger.getLogger(ResponseTimeValidator.class);

	private List<ModelEntity> services;
	private List<ResponseTimeValue> respObservation;
	private List<ResponseTimeEquation> respEq;
	private MatrixBuilder allErrors;
	private MatrixBuilder predictedRespTimes;
	private MatrixBuilder observedRespTimes;
	private ConstantStateModel<Unconstrained> stateModel;
	private final List<DataDependency<?>> dependencies = new ArrayList<>();

	@Override
	public void initialize(WorkloadDescription workload, IRepositoryCursor cursor) {
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		Set<Service> services = new HashSet<Service>();
		for (Resource res : workload.getResources()) {
			for (ResourceDemand demand : res.getDemands()) {
				builder.addVariable(demand);
				services.add(demand.getService());
			}
		}
		builder.setInvocationGraph(new InvocationGraph(new ArrayList<>(services), cursor, 1));
		this.stateModel = builder.build();

		this.respObservation = new ArrayList<>();
		this.respEq = new ArrayList<ResponseTimeEquation>();
		this.services = new ArrayList<ModelEntity>();
		for (Service srv : stateModel.getUserServices()) {
			if (srv.getIncomingCalls().isEmpty()) {
				ResponseTimeValue rtValue = new ResponseTimeValue(stateModel, cursor, srv, 0);
				ResponseTimeEquation rt = new ResponseTimeEquation(stateModel, cursor, srv, false, 0);
				dependencies.addAll(rt.getDataDependencies());
				dependencies.addAll(rtValue.getDataDependencies());
				respEq.add(rt);
				respObservation.add(rtValue);
				this.services.add(srv);
			}
		}
		allErrors = MatrixBuilder.create(this.services.size());
		predictedRespTimes = MatrixBuilder.create(this.services.size());
		observedRespTimes = MatrixBuilder.create(this.services.size());
	}

	@Override
	public IStateModel<?> getStateModel() {
		return stateModel;
	}

	public void predict(Vector state) {
		State x = new State(stateModel, state);
		stateModel.step(x);
		double[] relErr = new double[respEq.size()];
		double[] real = new double[respEq.size()];
		double[] actual = new double[respEq.size()];
		for (int i = 0; i < respEq.size(); i++) {
			real[i] = respObservation.get(i).getConstantValue();
			actual[i] = respEq.get(i).getValue(x).getValue();
			if (Double.isNaN(actual[i]) || Double.isNaN(real[i])) {
				log.error("Computed error was NaN!");
				relErr[i] = Double.MAX_VALUE;
			} else {
				relErr[i] = getRelativeError(real[i], actual[i], i);
				if (Double.isNaN(relErr[i])) {
					throw new IllegalArgumentException("Computed relative error was NaN for unknown reason.");
				}
			}
		}
		allErrors.addRow(relErr);
		predictedRespTimes.addRow(actual);
		observedRespTimes.addRow(real);
	}

	protected double getRelativeError(double real, double actual, int index) {
		if (real != 0) {
			// to avoid dividing by zero resulting in NaN
			double relErr = Math.abs(actual - real) / real;
			return relErr;
		} else {
			throw new IllegalArgumentException("Computed error was NaN, through division by zero!");
		}
	}

	public Vector getPredictionError() {
		return checkedMean(allErrors.toMatrix());
	}

	@Override
	public List<ModelEntity> getModelEntities() {
		return services;
	}

	@Override
	public Vector getObservedValues() {
		return checkedMean(observedRespTimes.toMatrix());
	}

	@Override
	public Vector getPredictedValues() {
		return checkedMean(predictedRespTimes.toMatrix());
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

	protected List<ModelEntity> getServices() {
		return services;
	}

	protected void setServices(List<ModelEntity> services) {
		this.services = services;
	}

	protected List<ResponseTimeValue> getRespObservation() {
		return respObservation;
	}

	protected void setRespObservation(List<ResponseTimeValue> respObservation) {
		this.respObservation = respObservation;
	}

	protected List<ResponseTimeEquation> getRespEq() {
		return respEq;
	}

	protected void setRespEq(List<ResponseTimeEquation> respEq) {
		this.respEq = respEq;
	}

	protected MatrixBuilder getAllErrors() {
		return allErrors;
	}

	protected void setAllErrors(MatrixBuilder allErrors) {
		this.allErrors = allErrors;
	}

	protected MatrixBuilder getPredictedRespTimes() {
		return predictedRespTimes;
	}

	protected void setPredictedRespTimes(MatrixBuilder predictedRespTimes) {
		this.predictedRespTimes = predictedRespTimes;
	}

	protected MatrixBuilder getObservedRespTimes() {
		return observedRespTimes;
	}

	protected void setObservedRespTimes(MatrixBuilder observedRespTimes) {
		this.observedRespTimes = observedRespTimes;
	}

	protected List<DataDependency<?>> getDependencies() {
		return dependencies;
	}

	protected void setStateModel(ConstantStateModel<Unconstrained> stateModel) {
		this.stateModel = stateModel;
	}
}
