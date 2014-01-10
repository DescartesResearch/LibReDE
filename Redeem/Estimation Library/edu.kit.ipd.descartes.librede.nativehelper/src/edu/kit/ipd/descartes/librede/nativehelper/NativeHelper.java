package edu.kit.ipd.descartes.librede.nativehelper;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import edu.kit.ipd.descartes.linalg.LinAlg;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.MatrixFunction;
import edu.kit.ipd.descartes.linalg.Scalar;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.linalg.backend.colt.ColtMatrix;
import edu.kit.ipd.descartes.linalg.backend.colt.ColtMatrixFactory;
import edu.kit.ipd.descartes.linalg.backend.colt.ColtVector;

public class NativeHelper {
	
	private static ColtMatrixFactory FACTORY = new ColtMatrixFactory();
	
	public static final int DOUBLE_BYTE_SIZE = 8;
	
	public static Pointer allocateDoubleArray(int size) {
		return new Memory(size * DOUBLE_BYTE_SIZE);
	}
	
	public static void setDoubleArray(Pointer array, int idx, double value) {
		array.setDouble(idx * DOUBLE_BYTE_SIZE, value);
	}
	
	public static Matrix nativeMatrix(final int rows, final int columns, final Pointer elements) {
		return LinAlg.matrix(rows, columns, new MatrixFunction() {			
			@Override
			public double cell(int row, int column) {
				return elements.getDouble((row * columns + column) * DOUBLE_BYTE_SIZE);
			}
		});
	}
	
	public static Vector nativeVector(final int rows, final Pointer elements) {
		return LinAlg.vector(rows, new VectorFunction() {			
			@Override
			public double cell(int row) {
				return elements.getDouble(row * DOUBLE_BYTE_SIZE);
			}
		});
	}
	
	public static void toNative(Pointer memory, Matrix matrix) {
		int rows = matrix.rows();
		int columns = matrix.columns();
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				memory.setDouble((i * columns + j) * DOUBLE_BYTE_SIZE, matrix.get(i, j));
			}
		}
	}
	
	public static void toNative(Pointer memory, Vector vector) {
		int rows = vector.rows();
		for (int i = 0; i < rows; i++) {
			memory.setDouble(i* DOUBLE_BYTE_SIZE, vector.get(i));
		}
	}

}
