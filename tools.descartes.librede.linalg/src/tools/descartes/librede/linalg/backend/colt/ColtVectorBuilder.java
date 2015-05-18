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
package tools.descartes.librede.linalg.backend.colt;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorBuilder;

public class ColtVectorBuilder extends VectorBuilder {
	
	private DoubleMatrix1D values;
	private int last;
	private int capacity;
	
	public ColtVectorBuilder(int capacity) {
		this.capacity = capacity;
		values = new DenseDoubleMatrix1D(capacity);
		last = 0;
	}
	
	@Override
	public void add(double value) {
		if (last == capacity) {
			grow();
		}
		values.set(last, value);
		last++;
	}
	
	@Override
	public Vector toVector() {
		if (last < capacity) {
			return new ColtVector(values.viewPart(0, last));
		}
		return new ColtVector(values);
	}
	
	private void grow() {
		capacity = capacity * 2;
		DoubleMatrix1D newValues = new DenseDoubleMatrix1D(capacity);
		newValues.viewPart(0, values.size()).assign(values);
		values = newValues;
	}
	

}
