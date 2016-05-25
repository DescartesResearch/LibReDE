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

/**
 * The interface classes need to implement to be able to listen for trace events
 * from a data source.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public interface IDataSourceListener {

	/**
	 * Called when new data is available in a data source.
	 * 
	 * @param source
	 *            the originating data source
	 * @param e
	 *            more information on the event
	 */
	public void dataAvailable(IDataSource source, TraceEvent e);

	/**
	 * Called when a new trace is added to a data source.
	 * 
	 * @param source
	 *            the originating data source
	 * @param key
	 *            a TraceKey object containing information on the new trace
	 */
	public void keyAdded(IDataSource source, TraceKey key);

	/**
	 * Called when a trace is removed from a data source.
	 * 
	 * @param source
	 *            the originating data source
	 * @param key
	 *            a TraceKey object containing information on the removed trace
	 */
	public void keyRemoved(IDataSource source, TraceKey key);

}
