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
package tools.descartes.librede.linalg.backend;

import tools.descartes.librede.linalg.Matrix;

public abstract class AbstractMatrix implements Matrix {
	
	@Override
	public boolean isVector() {
		return false;
	}
	
	@Override
	public boolean isScalar() {
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	protected void checkOperandsInnerDimensions(Matrix m) {
		if (columns() != m.rows()) {
			throw new IllegalArgumentException("Inner dimensions of operands must be equal.");
		}
	}
	
	protected void checkOperandsSameSize(Matrix m) {
		if (m.columns() != this.columns() || m.rows() != this.rows()) {
			throw new IllegalArgumentException("Both operands must have the same size.");
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		for (int i = 0; i < rows(); i++) {
			builder.append("[");
			for (int j = 0; j < columns(); j++) {
				if (j > 0) {
					builder.append("; ");
				}
				builder.append(get(i, j));
			}
			builder.append("]");
		}
		builder.append("]");
		return builder.toString();
	}
}
