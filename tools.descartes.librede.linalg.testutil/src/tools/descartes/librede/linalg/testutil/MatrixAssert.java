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
package tools.descartes.librede.linalg.testutil;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.data.Offset;

import tools.descartes.librede.linalg.Matrix;

public class MatrixAssert extends AbstractAssert<MatrixAssert, Matrix> {

	protected MatrixAssert(Matrix actual) {
		super(actual, MatrixAssert.class);
	}
	
	public MatrixAssert isEqualTo(Matrix expected, Offset<Double> delta) {
		Assertions.assertThat(actual.rows())
			.overridingErrorMessage("Expected matrix row count to be <%d> but was <%d>", expected.rows(), actual.rows())
			.isEqualTo(expected.rows());
		Assertions.assertThat(actual.columns())
			.overridingErrorMessage("Expected matrix column count to be <%d> but was <%d>", expected.columns(), actual.columns())
			.isEqualTo(expected.columns());
		for (int i = 0; i < actual.rows(); i++) {
			for (int j = 0; j < actual.columns(); j++) {
				Assertions.assertThat(actual.get(i, j))
					.overridingErrorMessage("Expected matrix(%d, %d) to be <%f>, but was <%f>", i, j,expected.get(i, j), actual.get(i, j))
					.isEqualTo(expected.get(i,j), delta);
			}
		}
		return this;
	}

	public static MatrixAssert assertThat(Matrix actual) {
		return new MatrixAssert(actual);
	}
}
