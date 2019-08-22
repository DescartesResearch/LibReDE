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
import java.util.List;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.WorkloadDescription;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.IRepositoryCursor;
import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.QueryBuilder;
import tools.descartes.librede.units.RequestRate;

@Component(displayName = "Weighted Response Time Validator")
public class WeightedResponseTimeValidator extends ResponseTimeValidator {

	private Query<Vector, RequestRate> throughputQuery;

	@Override
	public void initialize(WorkloadDescription workload, IRepositoryCursor cursor) {
		super.initialize(workload, cursor);
		// initialize throughput Query
		List<Service> services = new ArrayList<Service>();
		for (ModelEntity service : getServices()) {
			if (service instanceof Service) {
				services.add((Service) service);
			}
		}
		throughputQuery = QueryBuilder.select(StandardMetrics.THROUGHPUT).in(RequestRate.REQ_PER_SECOND)
				.forServices(services).average().using(cursor);
	}

	@Override
	protected double getRelativeError(double real, double actual, int index) {

		Vector throughputs = throughputQuery.get(0);
		double totaltp = 0;
		for (int i = 0; i < throughputs.rows(); i++) {
			totaltp += throughputs.get(i);
		}
		if (real != 0) {
			// to avoid dividing by zero resulting in NaN
			double error = Math.abs(actual - real) / real;
			double relErr = (throughputs.get(index) / totaltp) * error;
			return relErr;
		} else {
			throw new IllegalArgumentException("Computed error was NaN, through division by zero!");
		}
	}
}
