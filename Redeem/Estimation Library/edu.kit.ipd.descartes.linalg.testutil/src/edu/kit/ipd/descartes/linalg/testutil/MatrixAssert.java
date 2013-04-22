package edu.kit.ipd.descartes.linalg.testutil;

import org.fest.assertions.api.AbstractAssert;
import org.fest.assertions.api.Assertions;
import org.fest.assertions.data.Offset;

import edu.kit.ipd.descartes.linalg.Matrix;

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
