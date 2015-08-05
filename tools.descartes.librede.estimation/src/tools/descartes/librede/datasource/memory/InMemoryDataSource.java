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
package tools.descartes.librede.datasource.memory;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import tools.descartes.librede.configuration.TraceConfiguration;
import tools.descartes.librede.configuration.TraceToEntityMapping;
import tools.descartes.librede.datasource.AbstractDataSource;
import tools.descartes.librede.datasource.TraceEvent;
import tools.descartes.librede.datasource.TraceKey;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.repository.TimeSeries;
import tools.descartes.librede.units.Time;
import tools.descartes.librede.units.UnitsFactory;

@Component(displayName = "In-Memory Data Source")
public class InMemoryDataSource extends AbstractDataSource {
	
	private class Channel {
		public TimeSeries data = TimeSeries.EMPTY;
		public final Map<TraceKey, Integer> keys = new HashMap<>();
	}
	
	private Map<String, Channel> channels = new HashMap<>();
	private boolean loaded = false;
	
	public void append(String identifier, TimeSeries series) {
		synchronized(channels) {
			Channel curChannel = getChannel(identifier);
			if (loaded) {
				// The data source is already loaded, i.e. we directly notify the listeners of new data
				// no need to save it temporarily
				notifyNewData(curChannel, series);
			} else {
				// If the data source is not yet loaded, we save the data for later
				curChannel.data = curChannel.data.append(series);
			}
		}
	}

	@Override
	public List<TraceKey> addTrace(TraceConfiguration trace) throws IOException {
		List<TraceKey> keys = new LinkedList<TraceKey>();
		synchronized(channels) {
			Channel curChannel = getChannel(trace.getLocation());
			for (TraceToEntityMapping mapping : trace.getMappings()) {
				TraceKey k = new TraceKey(trace.getMetric(), trace.getUnit(), trace.getInterval(),
						mapping.getEntity(), trace.getAggregation(), mapping.getFilters());
				curChannel.keys.put(k, mapping.getTraceColumn() - 1);
				keys.add(k);
			}
			return keys;
		}
	}

	@Override
	public void load() throws IOException {
		if (loaded) {
			throw new IllegalStateException("The datasource is already loaded.");
		}
		// Send out all pending data
		synchronized(channels) {
			for (Channel curChannel : channels.values()) {
				if (!curChannel.data.isEmpty()) {
					notifyNewData(curChannel, curChannel.data);
					curChannel.data = TimeSeries.EMPTY;
				}
			}
			loaded = true;
		}
	}

	@Override
	public void close() throws IOException {
		channels.clear();		
	}
	
	private Channel getChannel(String location) {
		Channel curChannel = channels.get(location);
		if (curChannel == null) {
			curChannel = new Channel();
			channels.put(location, curChannel);
		}
		return curChannel;
	}
	
	private void notifyNewData(Channel channel, TimeSeries newData) {
		for (TraceKey key : channel.keys.keySet()) {
			TimeSeries curData = new TimeSeries(newData.getTime(), newData.getData(channel.keys.get(key)));
			curData.setStartTime(newData.getStartTime());
			curData.setEndTime(newData.getEndTime());
			TraceEvent event = new TraceEvent(key, curData, UnitsFactory.eINSTANCE.createQuantity(curData.getEndTime(), Time.SECONDS));
			notifyListeners(event);
		}
	}

}
