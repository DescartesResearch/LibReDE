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

import tools.descartes.librede.linalg.Vector;

public class VectorAssert extends AbstractAssert<VectorAssert, Vector> {
	
	protected VectorAssert(Vector actual) {
		super(actual, VectorAssert.class);
	}

	public VectorAssert isEqualTo(Vector expected, Offset<Double> delta) {
		Assertions.assertThat(actual.rows())
			.overridingErrorMessage("Expected vector row count to be <%s> but was <%s>", expected.rows(), actual.rows())
			.isEqualTo(expected.rows());
		for (int i = 0; i < actual.rows(); i++) {			
			Assertions.assertThat(actual.get(i))
				.overridingErrorMessage("Expected vector to be <%s> but was <%s>", expected.toString(), actual.toString())
				.isEqualTo(expected.get(i), delta);
		}
		return this;
	}

	public static VectorAssert assertThat(Vector actual) {
		return new VectorAssert(actual);
	}
	
}
