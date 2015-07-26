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

import org.apache.log4j.Logger;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.repository.IMonitoringRepository;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Dimension;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.Unit;

public class DeriveDiffHandler<D extends Dimension> extends BaseDerivationHandler<D> {
	
	private static final Logger log = Logger.getLogger(DeriveDiffHandler.class);
	
	public DeriveDiffHandler(Metric<D> delegatedMetric) {
		super(delegatedMetric, Aggregation.CUMULATIVE_SUM);
	}

	@Override
	public TimeSeries derive(IMonitoringRepository repository, Metric<D> metric,
			Unit<D> unit, ModelEntity entity, Aggregation aggregation, Quantity<Time> start,
			Quantity<Time> end) {
		log.trace("Derive sum from cumulative sum of " + metric.getName());
		if (aggregation == Aggregation.SUM) {
			TimeSeries cumSum = repository.select(metric, unit, entity, Aggregation.CUMULATIVE_SUM, start, end);
			if (!cumSum.isEmpty()) {
				return cumSum.diff();
			} else {
				log.trace("Could not find required traces. Skip derivation of sum.");
				return TimeSeries.EMPTY;
			}
		}
		throw new IllegalArgumentException("Unexpected aggregation: " + aggregation);
	}
}