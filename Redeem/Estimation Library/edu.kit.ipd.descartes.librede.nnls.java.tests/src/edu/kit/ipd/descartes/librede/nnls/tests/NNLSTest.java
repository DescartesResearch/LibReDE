package edu.kit.ipd.descartes.librede.nnls.tests;

import org.fest.assertions.api.Assertions;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.kit.ipd.descartes.librede.nnls.LeastSquaresRegression;
import edu.kit.ipd.descartes.linalg.LinAlg;
import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;

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
