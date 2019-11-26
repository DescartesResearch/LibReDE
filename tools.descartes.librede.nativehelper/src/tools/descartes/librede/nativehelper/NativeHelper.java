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
package tools.descartes.librede.nativehelper;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.MatrixFunction;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.linalg.VectorFunction;
import tools.descartes.librede.linalg.backend.colt.ColtMatrixFactory;

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
