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

import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.MatrixBuilder;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.observation.functions.ResponseTimeEquation;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.workload.WorkloadDescription;

@Component(displayName = "Response Time Validator")
public class ResponseTimeValidator implements IValidator {
	
	private List<ResponseTimeEquation> respEq;
	private MatrixBuilder allErrors;
	
	public ResponseTimeValidator(WorkloadDescription workload, IRepositoryCursor cursor) {
		this.respEq = new ArrayList<ResponseTimeEquation>();
		for (Service srv : workload.getServices()) {
			respEq.add(new ResponseTimeEquation(workload, cursor, srv, workload.getResources()));
		}
		allErrors = new MatrixBuilder(workload.getServices().size());
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

}