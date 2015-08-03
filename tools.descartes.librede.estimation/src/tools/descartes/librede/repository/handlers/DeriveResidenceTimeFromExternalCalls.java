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
package tools.descartes.librede.repository.handlers;

import tools.descartes.librede.configuration.ExternalCall;
import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Service;
import tools.descartes.librede.configuration.Task;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public class DeriveResidenceTimeFromExternalCalls extends BaseDerivationHandler<Time> {

	@Override
	public double aggregate(IMonitoringRepository repository, Metric<Time> metric, Unit<Time> unit, ModelEntity entity,
			Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {		
		if (!(entity instanceof Service)) {
			throw new IllegalArgumentException();
		}
		Service base = (Service)entity;		
		double rt = repository.aggregate(StandardMetrics.RESPONSE_TIME, unit, entity, Aggregation.AVERAGE, start, end);
		for (Task t : base.getTasks()) {
			if (t instanceof ExternalCall) {
				double visitCount = repository.aggregate(StandardMetrics.VISITS, RequestCount.REQUESTS, t, Aggregation.AVERAGE, start, end);
				double callTime = repository.aggregate(StandardMetrics.RESPONSE_TIME, unit, ((ExternalCall)t).getCalledService(), Aggregation.AVERAGE, start, end);
				rt -= (visitCount * callTime);
			}
		}
		return rt;
	}
}
