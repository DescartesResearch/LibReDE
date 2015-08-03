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

import java.util.Arrays;

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.metrics.StandardMetrics;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.RequestRate;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public class ThroughputWeightedAggregationHandler<D extends Dimension> extends BaseDerivationHandler<D> {

	private static final Logger log = Loggers.AGGREGATION_HANDLER_LOG;
	
	public ThroughputWeightedAggregationHandler(Metric<?> delegatedMetric) {
		super(Arrays.asList(delegatedMetric, StandardMetrics.THROUGHPUT), Arrays.asList(Aggregation.AVERAGE, Aggregation.AVERAGE));
	}

	@Override
	public double aggregate(IMonitoringRepository repository, Metric<D> metric, Unit<D> unit,
			ModelEntity entity, Aggregation aggregation, Quantity<Time> start, Quantity<Time> end) {
		if (aggregation == Aggregation.AVERAGE) {
			if (log.isTraceEnabled()) {
				log.trace("Calculate throughput-weighted " + aggregation.getLiteral() + " of " + metric.getName() + " for entity " + entity + ".");
			}
			TimeSeries ts = repository.select(metric, unit, entity, Aggregation.AVERAGE, start, end);
			TimeSeries weights = repository.select(StandardMetrics.THROUGHPUT, RequestRate.REQ_PER_SECOND, entity, Aggregation.AVERAGE, start, end);
			if (weights.isEmpty()) {
				throw new IllegalStateException();
			}
			return ts.timeWeightedMean(0, weights);
		}
		throw new IllegalArgumentException();
	}
}