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
package tools.descartes.librede.repository.handlers;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.configuration.Resource;
import tools.descartes.librede.configuration.SchedulingStrategy;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public class DeriveQueueLengthHandler extends BaseDerivationHandler<RequestCount> {
	
	@Override
	public double aggregate(IMonitoringRepository repository, Metric<RequestCount> metric, Unit<RequestCount> unit,
			ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
		if (!(entity instanceof Resource)) {
			throw new IllegalArgumentException();
		}
		if (Loggers.DERIVATION_HANDLER_LOG.isTraceEnabled()) {
			Loggers.DERIVATION_HANDLER_LOG.trace("Derive average queue-length from utilization.");
		}
		if (aggregation == Aggregation.AVERAGE) {
			double util = repository.aggregate(StandardMetrics.UTILIZATION, Ratio.NONE, entity, aggregation, start, end);
			if (((Resource)entity).getSchedulingStrategy() == SchedulingStrategy.IS) {
				// average queue length and utilization are equal
				return unit.convertFrom(util, RequestCount.REQUESTS);
			} else {
				// ATTENTION: The equation Q_i = \frac{U_i}{1 - U_i} is only valid
				// for PS or single-class FCFS
				return unit.convertFrom(util / (1 - util), RequestCount.REQUESTS);
			}
		}
		throw new IllegalArgumentException("Unexpected aggregation: " + aggregation);
	}

}
