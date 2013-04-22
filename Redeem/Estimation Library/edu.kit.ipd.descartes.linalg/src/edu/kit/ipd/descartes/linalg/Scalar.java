package edu.kit.ipd.descartes.linalg;

import edu.kit.ipd.descartes.linalg.backend.MatrixImplementation;
import edu.kit.ipd.descartes.linalg.backend.VectorImplementation;
import edu.kit.ipd.descartes.linalg.storage.DoubleStorage;

public class Scalar extends Vector {
	
	public static final Scalar ZERO = new Scalar(0);
	public static final Scalar ONE = new Scalar(1);
	
	public Scalar(double value) {
		super(new ScalarImplementation(value));
	}
	
	Scalar(ScalarImplementation delegate) {
		super(delegate);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	<M extends Matrix> M newInstance(MatrixImplementation delegate) {
		return (M) new Scalar((ScalarImplementation)delegate);
	}
	
	public double getValue() {
		return ((ScalarImplementation)delegate).value;
	}
	
	@Override
	public boolean isScalar() {
		return true;
	}
	
	@Override
	public boolean isVector() {
		return false;
	}

	private static class ScalarImplementation implements VectorImplementation {
		
		public double value;

		public ScalarImplementation(double value) {
			this.value = value;
		}
		
		@Override
		public ScalarImplementation abs() {
			return new ScalarImplementation(Math.abs(value));
		}

		@Override
		public double sum() {
			return value;
		}

		@Override
		public double norm1() {
			return Math.abs(value);
		}

		@Override
		public double norm2() {
			return value*value;
		}

		@Override
		public ScalarImplementation transpose() {
			return this;
		}

		@Override
		public MatrixImplementation appendRows(MatrixImplementation a) {
			if (a.columns() != 1) {
				throw new IllegalArgumentException("Number of columns must be equal.");
			}
			double[] vector = new double[a.rows() + 1];
			vector[0] = value;
			System.arraycopy(a.toArray1D(), 0, vector, 1,  a.rows());
			return LinAlg.FACTORY.createVector(vector);
		}

		@Override
		public MatrixImplementation appendColumns(MatrixImplementation a) {
			if (a.rows() != 1) {
				throw new IllegalArgumentException("Number of rows must be equal.");
			}
			double[][] matrix = new double[1][a.columns()];
			matrix[0][0] = value;
			System.arraycopy(a.toArray1D(), 0, matrix[0], 1, a.columns());
			return LinAlg.FACTORY.createMatrix(matrix);
		}

		@Override
		public double get(int row, int col) {
			if (row > 0 || col > 0) {
				throw new IndexOutOfBoundsException();
			}
			return value;
		}

		@Override
		public int rows() {
			return 1;
		}

		@Override
		public int columns() {
			return 1;
		}

		@Override
		public VectorImplementation row(int row) {
			if (row != 0) {
				throw new IndexOutOfBoundsException();
			}
			return this;
		}

		@Override
		public ScalarImplementation column(int column) {
			if (column != 0) {
				throw new IndexOutOfBoundsException();
			}
			return this;
		}

		@Override
		public ScalarImplementation plus(double a) {
			return new ScalarImplementation(value + a);
		}

		@Override
		public ScalarImplementation minus(double a) {
			return new ScalarImplementation(value - a);
		}
		
		@Override
		public ScalarImplementation times(double a) {
			return new ScalarImplementation(value * a);
		}

		@Override
		public MatrixImplementation plus(MatrixImplementation a) {
			return new ScalarImplementation(value + ((ScalarImplementation)a).value);
		}
		
		@Override
		public MatrixImplementation arrayMultipliedBy(MatrixImplementation a) {
			return new ScalarImplementation(value * ((ScalarImplementation)a).value);
		}

		@Override
		public MatrixImplementation minus(MatrixImplementation a) {
			return new ScalarImplementation(value - ((ScalarImplementation)a).value);
		}

		@Override
		public MatrixImplementation multipliedBy(MatrixImplementation a) {
			return a.times(value);
		}

		@Override
		public double[] toArray1D() {
			return new double[] { value };
		}

		@Override
		public double[][] toArray2D() {
			return new double[][] {{ value }};
		}

		@Override
		public void toDoubleStorage(DoubleStorage storage) {
			storage.write(new double[] { value });			
		}

		@Override
		public double get(int row) {
			if (row > 0) {
				throw new IndexOutOfBoundsException();
			}
			return value;
		}

		@Override
		public VectorImplementation slice(Range range) {
			if (range.getStart() != 0 && range.getEnd() != 1) {
				throw new IndexOutOfBoundsException();
			}
			return this;
		}

		@Override
		public double dot(VectorImplementation b) {
			if (!(b instanceof ScalarImplementation)) {
				throw new IllegalArgumentException("Dimensions of operands do not match.");
			}		
			return value * ((ScalarImplementation)b).value;
		}
		
	}
}
