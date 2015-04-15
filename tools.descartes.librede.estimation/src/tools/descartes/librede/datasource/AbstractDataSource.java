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

import java.util.LinkedList;
import java.util.List;

/**
 * Abstract class containing helper functionality for all types of data sources.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 */
public abstract class AbstractDataSource implements IDataSource {
	
	private List<IDataSourceListener> listeners = new LinkedList<IDataSourceListener>();
	
	@Override
	public void addListener(IDataSourceListener listener) {
		listeners.add(listener);		
	}
	
	@Override
	public void removeListener(IDataSourceListener listener) {
		listeners.remove(listener);		
	}

	protected void notifyListeners(TraceEvent e) {
		for (IDataSourceListener listener : listeners) {
			listener.dataAvailable(this, e);
		}
	}
}
