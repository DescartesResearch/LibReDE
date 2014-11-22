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
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.functions.ResponseTimeEquation;
import tools.descartes.librede.models.state.ConstantStateModel;
import tools.descartes.librede.models.state.ConstantStateModel.Builder;
import tools.descartes.librede.models.state.constraints.Unconstrained;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;

@Component(displayName = "Response Time Validator")
public class ResponseTimeValidator implements IValidator {
	
	private List<ModelEntity> services;
	private List<ResponseTimeEquation> respEq;
	private MatrixBuilder allErrors;
	
	@Override
	public void initialize(WorkloadDescription workload,
			IRepositoryCursor cursor) {
		Builder<Unconstrained> builder = ConstantStateModel.unconstrainedModelBuilder();
		for (Resource res : workload.getResources()) {
			for (Service serv : workload.getServices()) {
				builder.addVariable(res, serv);
			}
		}
		ConstantStateModel<Unconstrained> stateModel = builder.build(); 
		
		this.respEq = new ArrayList<ResponseTimeEquation>();
		this.services = new ArrayList<ModelEntity>();
		for (Service srv : stateModel.getServices()) {
			respEq.add(new ResponseTimeEquation(stateModel, cursor, srv));
			services.add(srv);
		}
		allErrors = new MatrixBuilder(stateModel.getServices().size());		
	}
	
	public void predict(Vector state) {
		double[] relErr = new double[respEq.size()];
		int i = 0;
		for (ResponseTimeEquation cur : respEq) {
			double real = cur.getObservedOutput();
			double actual = cur.getCalculatedOutput(state);
			relErr[i] = Math.abs(actual - real) / real;
			i++;
		}
		allErrors.addRow(relErr);		
	}
	
	public Vector getPredictionError() {
		return LinAlg.mean(allErrors.toMatrix(), 0);
	}
	
	@Override
	public List<ModelEntity> getModelEntities() {
		return services;
	}
}
