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
package tools.descartes.librede.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import tools.descartes.librede.repository.Query;
import tools.descartes.librede.repository.rules.DataDependency;
import tools.descartes.librede.repository.rules.DependencyScope;
import tools.descartes.librede.repository.rules.IDependencyTarget;

/**
 * Provides a default implementation of {@link IDependencyTarget}.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 *
 */
public class AbstractDependencyTarget implements IDependencyTarget {

	private final List<DataDependency<?>> dataDependencies = new ArrayList<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see tools.descartes.librede.repository.rules.IDependencyTarget#
	 * getDataDependencies()
	 */
	@Override
	public List<DataDependency<?>> getDataDependencies() {
		return Collections.unmodifiableList(dataDependencies);
	}

	/**
	 * Copies the dependencies of another {@code IDependencyTarget} to this one
	 * 
	 * @param target
	 *            the other {@code IDependencyTarget} object
	 */
	protected void addDataDependencies(IDependencyTarget target) {
		dataDependencies.addAll(target.getDataDependencies());
	}

	/**
	 * Adds a data dependency based on the data accessed by a query.
	 * 
	 * @param query
	 *            the query object
	 */
	protected void addDataDependency(Query<?, ?> query) {
		dataDependencies.add(new DataDependency<>(query.getMetric(), query.getAggregation(),
				DependencyScope.fixedScope(query.getEntities())));
	}
}
