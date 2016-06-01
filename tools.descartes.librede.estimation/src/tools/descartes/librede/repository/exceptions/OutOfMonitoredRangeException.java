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
package tools.descartes.librede.repository.exceptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import tools.descartes.librede.configuration.ModelEntity;
import tools.descartes.librede.metrics.Aggregation;
import tools.descartes.librede.metrics.Metric;
import tools.descartes.librede.units.Quantity;
import tools.descartes.librede.units.Time;

/**
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class OutOfMonitoredRangeException extends MonitoringRepositoryException {

	private static final long serialVersionUID = 8959366514292578488L;
	
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	
	private final Quantity<Time> requestedStartTime;
	private final Quantity<Time> requestedEndTime;
	private final Quantity<Time> actualStartTime;
	private final Quantity<Time> actualEndTime;
	
	public OutOfMonitoredRangeException(Metric<?> metric, Aggregation aggregation, ModelEntity entity, 
			Quantity<Time> requestedStartTime, Quantity<Time> requestedEndTime,
			Quantity<Time> actualStartTime, Quantity<Time> actualEndTime) {
		super("No monitoring data available for the requested time frame.", metric, aggregation, entity);
		this.requestedStartTime = requestedStartTime;
		this.requestedEndTime = requestedEndTime;
		this.actualStartTime = actualStartTime;
		this.actualEndTime = actualEndTime;
	}
	
	public Quantity<Time> getRequestedStartTime() {
		return requestedStartTime;
	}
	
	public Quantity<Time> getRequestedEndTime() {
		return requestedEndTime;
	}
	
	public Quantity<Time> getActualStartTime() {
		return actualStartTime;
	}
	
	public Quantity<Time> getActualEndTime() {
		return actualEndTime;
	}
	
	@Override
	public String getMessage() {
		return super.getMessage() + " " + printDebugInformation();
	}
	
	private String printDebugInformation() {
		StringBuilder msg = new StringBuilder();
		msg.append("{");
		msg.append("entity=").append(getEntity()).append(", ");
		msg.append("metric=").append(getMetric()).append(", ");
		msg.append("aggregation=").append(getAggregation()).append(", ");
		msg.append("requested=[");
		appendTimestamp(msg, requestedStartTime);
		msg.append(", ");
		appendTimestamp(msg, requestedEndTime);
		msg.append("], ");
		msg.append("actual=[");
		appendTimestamp(msg, actualStartTime);
		msg.append(", ");
		appendTimestamp(msg, actualEndTime);
		msg.append("]");
		msg.append("}");
		return msg.toString();
	}
	
	private void appendTimestamp(StringBuilder builder, Quantity<Time> timestamp) {
		builder.append(dateFormat.format(new Date((long)timestamp.getValue(Time.MILLISECONDS))));
	}

}
