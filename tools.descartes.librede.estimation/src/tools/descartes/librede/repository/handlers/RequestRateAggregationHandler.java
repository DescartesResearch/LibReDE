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

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.RequestCount;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public class RequestRateAggregationHandler extends BaseDerivationHandler<RequestRate> {
	
	private static final Logger log = Loggers.DERIVATION_HANDLER_LOG;
	
	private Metric<RequestCount> countMetric;
	
	public RequestRateAggregationHandler(Metric<RequestCount> countMetric) {
		this.countMetric = countMetric;
	}

	@Override
	public double aggregate(IMonitoringRepository repository, Metric<RequestRate> metric, Unit<RequestRate> unit,
			ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
		if (aggregation == Aggregation.AVERAGE) {
			if (log.isTraceEnabled()) {
				log.trace("Calculate requrest rate " + aggregation.getLiteral() + " of " + metric.getName() + " for entity " + entity + ".");
			}
			double sum = repository.aggregate(countMetric, RequestCount.REQUESTS,
					entity, Aggregation.SUM, start, end);
			return unit.convertFrom(sum / (end.minus(start).getValue(Time.SECONDS)), RequestRate.REQ_PER_SECOND);
		}
		throw new IllegalArgumentException();
	}		
}