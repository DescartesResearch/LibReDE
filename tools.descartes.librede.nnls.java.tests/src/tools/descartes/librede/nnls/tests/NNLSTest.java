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
package tools.descartes.librede.nnls.tests;

import org.fest.assertions.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tools.descartes.librede.linalg.LinAlg;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.nnls.LeastSquaresRegression;

/**
 * 
 * @author Mehran Saliminia
 * Test Case for Non-Negative Least-Squares algorithm
 *
 */

public class NNLSTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void polnomialDegree5Test() {		
		Matrix E=this.getMatrixE(5, 5);
		//degree 5
		Polynomial pol=new Polynomial(5);
		//generates random coefficients
		pol.generateRandomCoeffs();
		LeastSquaresRegression reg=new LeastSquaresRegression();		
		Vector F=LinAlg.vector(pol.getValues(5));
		Vector coef=LinAlg.vector(pol.getCoef());
		
		Vector result=reg.nnls(E,F );
		VectorAssert.assertThat(result).isEqualTo(coef , Assertions.offset(1e-9));			
	}

	@Test
	public void polnomialDegree4Test() {		
		Matrix E=this.getMatrixE(4, 4);
		//degree 4 
		Polynomial pol=new Polynomial(4);
		//generates random coefficients
		pol.generateRandomCoeffs();
		LeastSquaresRegression reg=new LeastSquaresRegression();		
		Vector F= LinAlg.vector(pol.getValues(4));
		Vector coef=LinAlg.vector(pol.getCoef());
		
		Vector result=reg.nnls(E,F );
		VectorAssert.assertThat(result).isEqualTo(coef , Assertions.offset(1e-9));			
	}
	
	@Test
	public void polnomialDegree3Test() {		
		Matrix E=this.getMatrixE(4, 3);
		//degree 3 
		Polynomial pol=new Polynomial(3);
		//generates random coefficients
		pol.generateRandomCoeffs();
		LeastSquaresRegression reg=new LeastSquaresRegression();		
		Vector F=LinAlg.vector(pol.getValues(4));
		Vector coef=LinAlg.vector(pol.getCoef());
		
		Vector result=reg.nnls(E,F );
		VectorAssert.assertThat(result).isEqualTo(coef , Assertions.offset(1e-9));			
	}
	
	public Matrix getMatrixE(int rows, int columns)
	{
		double[][] e=new double[rows][columns];
		for (int i = 0; i < columns; ++i)
			for (int j = 1; j < rows + 1; ++j) {
				e[j-1][i] = Math.pow(j, i);
			}		
		Matrix matrix=LinAlg.matrix(e);
		return matrix;		
	}
}
