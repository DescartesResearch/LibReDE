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
package tools.descartes.librede.datasource;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Abstract class containing helper functionality for all types of data sources.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 */
public abstract class AbstractDataSource implements IDataSource {
	
	private static final Logger log = Loggers.DATASOURCE_LOG;
	
	private String name = "<UNNAMED>";
	private Set<IDataSourceListener> listeners = new HashSet<IDataSourceListener>();
	
	@Override
	public void addListener(IDataSourceListener listener) {	
		if(listeners.add(listener)) {
			if (log.isDebugEnabled()) {
				log.debug("New listener added to data source " + name + ": " + listener);
			}
		}
	}
	
	@Override
	public void removeListener(IDataSourceListener listener) {
		if (listeners.remove(listener)) {
			if (log.isDebugEnabled()) {
				log.debug("Listener removed from data source " + name + ": " + listener);
			}
		}
	}

	protected void notifyListeners(TraceEvent e) {
		for (IDataSourceListener listener : listeners) {
			listener.dataAvailable(this, e);
		}
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void setName(String name) {
		this.name = name;		
	}
}
