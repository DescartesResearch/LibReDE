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
package edu.kit.ipd.descartes.librede.estimation.workload;

import java.util.UUID;

/**
 * Represents a computing resource (e.g., CPU, hard disk, etc.) in a system.
 * 
 * @author Simon Spinner (simon.spinner@uni-wuerzburg.de)
 * @version 1.0
 */
public class Resource implements IModelEntity {

	private final UUID id;
	private final String name;
	private int parallelServers;

	/**
	 * Creates a new instance
	 * 
	 * @param name
	 */
	public Resource(String name) {
		this(name, 1);
	}

	/**
	 * @param name
	 * @param parallelServers
	 * @throws IllegalArgumentException
	 *             if parallelServers <= 0
	 */
	public Resource(String name, int parallelServers) {
		if (parallelServers <= 0) {
			throw new IllegalArgumentException();
		}

		this.id = UUID.randomUUID();
		this.name = name;
		this.parallelServers = parallelServers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.ipd.descartes.librede.estimation.workload.IModelEntity#getId()
	 */
	public UUID getId() {
		return id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.kit.ipd.descartes.librede.estimation.workload.IModelEntity#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the number of servers available to process requests in parallel.
	 */
	public int getNumberOfParallelServers() {
		return parallelServers;
	}

	/**
	 * Sets the number of parallel server for this resource.
	 * 
	 * @param parallelServers
	 */
	public void setNumberOfParallelServers(int parallelServers) {
		if (parallelServers <= 0) {
			throw new IllegalArgumentException();
		}
		this.parallelServers = parallelServers;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Resource other = (Resource) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Resource: " + name;
	}
}