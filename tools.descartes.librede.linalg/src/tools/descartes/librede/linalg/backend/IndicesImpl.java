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
package tools.descartes.librede.linalg.backend;

import java.util.Arrays;

import tools.descartes.librede.linalg.Indices;
import tools.descartes.librede.linalg.VectorFunction;

public class IndicesImpl extends Indices {
	
	protected final int[] indices;
	
	public IndicesImpl(int... indices) {
		this.indices = indices;
	}
	
	public IndicesImpl(int length, VectorFunction init) {
		indices = new int[length];
		for (int i = 0; i < length; i++) {
			indices[i] = (int) init.cell(i);
		}
	}
	
	public int[] getIndices() {
		return indices;
	}
	
	@Override
	public int length() {
		return indices.length;
	}

	@Override
	public int get(int idx) {
		return indices[idx];
	}
	
	@Override
	public boolean isContinuous() {
		return false;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(indices);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IndicesImpl other = (IndicesImpl) obj;
		if (!Arrays.equals(indices, other.indices))
			return false;
		return true;
	}



}
