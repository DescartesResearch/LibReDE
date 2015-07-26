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
import tools.descartes.librede.repository.UnitConverter;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Ratio;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public class DeriveUtilizationHandler extends BaseDerivationHandler<Ratio> {
	
	private static final Logger log = Loggers.DERIVATION_HANDLER_LOG;
	
	public DeriveUtilizationHandler() {
		super(Arrays.asList(StandardMetrics.BUSY_TIME, StandardMetrics.IDLE_TIME), Arrays.asList(Aggregation.SUM, Aggregation.SUM));
	}

	@Override
	public TimeSeries derive(IMonitoringRepository repository, Metric<Ratio> metric,
			Unit<Ratio> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start,
			Quantity<Time> end) {
		if (log.isTraceEnabled()) {
			log.trace("Derive average utilization from busy time and idle time.");
		}
		if (aggregation == Aggregation.AVERAGE) {
			TimeSeries busy = repository.select(StandardMetrics.BUSY_TIME, Time.SECONDS, entity, Aggregation.SUM, start, end);
			TimeSeries idle = repository.select(StandardMetrics.IDLE_TIME, Time.SECONDS, entity, Aggregation.SUM, start, end);
			if (!busy.isEmpty() && !idle.isEmpty()) {
				// TODO: check that timestamps of idle and busy are equal
				TimeSeries util = new TimeSeries(idle.getTime(), busy.getData().arrayDividedBy(busy.getData().plus(idle.getData())));
				util.setStartTime(start.getValue(Time.SECONDS));
				util.setEndTime(end.getValue(Time.SECONDS));
				return UnitConverter.convertTo(util, Ratio.NONE, unit);
			} else {
				if (log.isTraceEnabled()) {
					log.trace("Could not find required busy time and/or idle time traces. Skip derivation of average utilization.");
				}
				return TimeSeries.EMPTY;
			}
		}
		throw new IllegalArgumentException("Unexpected aggregation: " + aggregation);
	}
}