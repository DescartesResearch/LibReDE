package edu.kit.ipd.descartes.linalg;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.data.Offset;

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
