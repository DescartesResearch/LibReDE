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
package tools.descartes.librede.ipopt.java;

import static tools.descartes.librede.linalg.LinAlg.empty;
import static tools.descartes.librede.linalg.LinAlg.matrix;
import static tools.descartes.librede.linalg.LinAlg.mean;
import static tools.descartes.librede.linalg.LinAlg.norm2;
import static tools.descartes.librede.linalg.LinAlg.transpose;
import static tools.descartes.librede.linalg.LinAlg.vertcat;
import static tools.descartes.librede.linalg.LinAlg.zeros;
import static tools.descartes.librede.nativehelper.NativeHelper.nativeVector;
import static tools.descartes.librede.nativehelper.NativeHelper.toNative;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;

import tools.descartes.librede.algorithm.AbstractEstimationAlgorithm;
import tools.descartes.librede.exceptions.EstimationException;
import tools.descartes.librede.exceptions.InitializationException;
import tools.descartes.librede.ipopt.java.backend.Eval_F_CB;
import tools.descartes.librede.ipopt.java.backend.Eval_G_CB;
import tools.descartes.librede.ipopt.java.backend.Eval_Grad_F_CB;
import tools.descartes.librede.ipopt.java.backend.Eval_H_CB;
import tools.descartes.librede.ipopt.java.backend.Eval_Jac_G_CB;
import tools.descartes.librede.ipopt.java.backend.IpoptLibrary;
import tools.descartes.librede.ipopt.java.backend.IpoptOptionKeyword;
import tools.descartes.librede.ipopt.java.backend.IpoptOptionValue;
import tools.descartes.librede.linalg.Matrix;
import tools.descartes.librede.linalg.Vector;
import tools.descartes.librede.models.diff.HessianMatrixBuilder;
import tools.descartes.librede.models.diff.JacobiMatrixBuilder;
import tools.descartes.librede.models.observation.IObservationModel;
import tools.descartes.librede.models.observation.functions.IOutputFunction;
import tools.descartes.librede.models.state.IStateModel;
import tools.descartes.librede.models.state.StateVariable;
import tools.descartes.librede.models.state.constraints.ILinearStateConstraint;
import tools.descartes.librede.models.state.constraints.IStateConstraint;
import tools.descartes.librede.models.state.constraints.StateBoundsConstraint;
import tools.descartes.librede.nativehelper.NativeHelper;
import tools.descartes.librede.registry.Component;
import tools.descartes.librede.registry.ParameterDefinition;

@Component(displayName="Non-linear Constrained Optimization")
public class RecursiveOptimization extends AbstractEstimationAlgorithm {
	
	@ParameterDefinition(name = "SolutionTolerance", label = "Solution Tolerance", defaultValue = "1e-7")
	private double solutionTolerance = 1e-7;
	
	@ParameterDefinition(name = "UpperBoundsInfValue", label = "Upper Bounds Infinity Value", defaultValue = "1e19")
	private double upperBoundsInfValue = 1e19;
	
	@ParameterDefinition(name = "LowerBoundsInfValue", label = "Lower Bounds Infinity Value", defaultValue = "-1e19")
	private double lowerBoundsInfValue = -1e19;

	// C-style; start counting of rows and column indices at 0
	private final static int IPOPT_INDEX_STYLE = 0;
	
	/* Number of variables of the optimization problem. */
	protected int stateSize = -1;
	/* Number of linear and nonlinear constraints. This does not include the bounds. */
	private int constraintCount = -1;
	
	/*
	 * Lists of all constraints returnd by the state model. The different
	 * types of constraints are managed in separate lists so that they can
	 * be treated differently during the optimization
	 */
	private List<IStateConstraint> nonlinearConstraints = new ArrayList<IStateConstraint>();
	private List<ILinearStateConstraint> linearConstraints = new ArrayList<ILinearStateConstraint>();
	private List<StateBoundsConstraint> boundsConstraints = new ArrayList<StateBoundsConstraint>();
	
	/*
	 * Callback functions which are called from native code of IPOPT during optimization
	 * in order to evaluate the objective function and the constraint functions (including
	 * first and second order derivatives).
	 * 
	 * ATTENTION: Due to technical limitations of JNA, each callback function must
	 * be implemented by a separate class. It does not work to bundle them in a single class.
	 * Furthermore, the classes need to be static so that they cannot access the fields of the
	 * enclosing class. Therefore the class OptimizationState encapsulates the internal
	 * optimization state that is shared between the callbacks.
	 */
	private F evalf = new F(); /* objective function f(x): R^n --> R */
	private G evalg = new G(); /* constraint functions g(x): R^n --> R^m */
	private GradF evalgradf = new GradF(); /* first derivatives of the objective function */
	private JacG evaljacg = new JacG(); /* Jacobi matrix of the constraint functions */
	private H evalh = new H(); /* Hessian matrix of the Lagrangian */
	
	/* Number of nonzeros in the Jacobian of the constraints */
	private int nele_jac;
	/* Number of nonzeros in the Hessian of the Lagrangian (lower or upper triangual part only) */
	private int nele_hess;	
	
	/*
	 * Pointers to native memory used to exchange data with the native IPOPT library
	 */
	private Pointer x_L; /* double[stateSize] : x_L <= x <= x_U */
	private Pointer x_U; /* double[stateSize] */
	private Pointer g_L; /* double[constraintCount] : g_L <= g(x) <= g_U */
	private Pointer g_U; /* double[constraintCount] */
	private Pointer x; /* double[stateSize] : initial and solution vector */
	private DoubleByReference objRef; /* current objective value of optimization */

	// Caches the current state vector
	protected Vector current;
	
	//protected Vector error;
	
	// Caches the difference between observed and calculated response time
	protected double obj;
	
	protected Matrix lagrange;
	
	// Caches the first derivative of the observation model
	protected Vector objGrad;
	
	private Matrix estimationBuffer;
	
	// Flag indicating whether this is the first iteration.
	private boolean initialized = false;
	
	private Vector error;
	private Matrix jacobi ;
	private Matrix jacobiConstr;
	
	private Pointer nlp;
	
	protected Vector solve() {
		setOptimizationBounds();
		
		setOptimizationConstraints();
		
		if (nlp == null) {
			nlp = IpoptLibrary.INSTANCE.IpOpt_CreateIpoptProblem(stateSize, x_L, x_U, constraintCount, g_L, g_U, nele_jac, nele_hess,
	                IPOPT_INDEX_STYLE, evalf, evalg, evalgradf,
	                evaljacg, evalh);
			
			setOptimizationOptions(nlp);
		}			
		
		/* solve the problem */
		int status = IpoptLibrary.INSTANCE.IpOpt_IpoptSolve(nlp, x, Pointer.NULL, objRef, Pointer.NULL, Pointer.NULL, Pointer.NULL, Pointer.NULL);
		
		Vector estimate = null;		
		if (status == IpoptLibrary.IP_SOLVE_SUCCEEDED || status == IpoptLibrary.IP_ACCEPTABLE_LEVEL) {
			estimate = nativeVector(stateSize, x);		
		} else if (status == IpoptLibrary.IP_ITERATION_EXCEEDED || status == IpoptLibrary.IP_CPUTIME_EXCEEDED) {
			estimate = nativeVector(stateSize, x);
			System.out.println("\n\nWARNING: Inaccurate estimate.");
		} else {
			estimate = zeros(stateSize);
			System.out.println("\n\nERROR OCCURRED DURING IPOPT OPTIMIZATION: " + status);
		}

		return estimate;
	}
	
	protected void setState(Vector state) {
		copy(state, x);
	}
	
	private void copy(Vector v, Pointer target) {
		for (int i = 0; i < v.rows(); i++) {
			NativeHelper.setDoubleArray(target, i, v.get(i));
		}
	}

	private void initStateConstraints(List<? extends IStateConstraint> constraints) {
		nonlinearConstraints.clear();
		linearConstraints.clear();
		boundsConstraints.clear();
		
		for (IStateConstraint c : constraints) {
			if (c instanceof StateBoundsConstraint) {
				boundsConstraints.add((StateBoundsConstraint)c);
			} else if (c instanceof ILinearStateConstraint) {
				linearConstraints.add((ILinearStateConstraint)c);
			} else {
				nonlinearConstraints.add(c);
			}
		}
	}
	
	private void setOptimizationConstraints() {
		if (constraintCount > 0) {		
			/* set the values of the constraint bounds to default value */			
			int idx = 0;
			for (IStateConstraint c : linearConstraints) {
				NativeHelper.setDoubleArray(g_L, idx, c.getLowerBound());
				NativeHelper.setDoubleArray(g_U, idx, c.getUpperBound());
				idx++;
			}			
			for (IStateConstraint c : nonlinearConstraints) {
				NativeHelper.setDoubleArray(g_L, idx, c.getLowerBound());
				NativeHelper.setDoubleArray(g_U, idx, c.getUpperBound());
				idx++;
			}
		}
	}

	private void setOptimizationBounds() {
		/* set the values for the variable bounds to default value */
		for (int i = 0; i < stateSize; i++) {
			NativeHelper.setDoubleArray(x_L, i, 0);
			NativeHelper.setDoubleArray(x_U, i, upperBoundsInfValue);
		}
		
		for (StateBoundsConstraint c : boundsConstraints) {
			StateVariable var = c.getStateVariable();
			NativeHelper.setDoubleArray(x_L, getStateModel().getStateVariableIndex(var.getResource(), var.getService()), c.getLowerBound());
			NativeHelper.setDoubleArray(x_U, getStateModel().getStateVariableIndex(var.getResource(), var.getService()), c.getUpperBound());
		}
	}
	
	private void allocateNativeMemory() {
		/* allocate space for the variable bounds */
		x_L = NativeHelper.allocateDoubleArray(stateSize);
		x_U = NativeHelper.allocateDoubleArray(stateSize);
		
		if (constraintCount > 0) {		
			/* allocate space for the constraint bounds */
			g_L = NativeHelper.allocateDoubleArray(constraintCount);
			g_U = NativeHelper.allocateDoubleArray(constraintCount);
		} else {
			g_L = Pointer.NULL;
			g_U = Pointer.NULL;
		}
		
		/* allocate space for the initial point and solution vector */
	    x = NativeHelper.allocateDoubleArray(stateSize);

		objRef = new DoubleByReference(); // objective value
	}

	private void setOptimizationOptions(Pointer nlp) {
		IpoptLibrary.INSTANCE.IpOpt_AddIpoptIntOption(nlp, IpoptOptionKeyword.PRINT_LEVEL.toNativeString(),1);
		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, IpoptOptionKeyword.MU_STRATEGY.toNativeString(), 
				IpoptOptionValue.ADAPTIVE.toNativeString());
//		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, IpoptOptionKeyword.CHECK_DERIVATIVES_FOR_NANINF.toNativeString(), 
//				IpoptOptionValue.YES.toNativeString());
//		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, "output_file", "ipopt.out");
//		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, IpoptOptionKeyword.DERIVATIVE_TEST.toNativeString(), 
//				IpoptOptionValue.SECOND_ORDER.toNativeString());
//		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, IpoptOptionKeyword.DERIVATIVE_TEST_PRINT_ALL.toNativeString(), 
//				IpoptOptionValue.YES.toNativeString());
	    IpoptLibrary.INSTANCE.IpOpt_AddIpoptNumOption(nlp, IpoptOptionKeyword.TOL.toNativeString(), solutionTolerance);
	    IpoptLibrary.INSTANCE.IpOpt_AddIpoptNumOption(nlp, IpoptOptionKeyword.NLP_LOWER_BOUND_INF.toNativeString(), lowerBoundsInfValue);
	    IpoptLibrary.INSTANCE.IpOpt_AddIpoptNumOption(nlp, IpoptOptionKeyword.NLP_UPPER_BOUND_INF.toNativeString(), upperBoundsInfValue);
	    IpoptLibrary.INSTANCE.IpOpt_AddIpoptIntOption(nlp, IpoptOptionKeyword.MAX_ITER.toNativeString(), 300);
	}
	
	
	/* (non-Javadoc)
	 * @see tools.descartes.librede.models.algorithm.IEstimationAlgorithm#initialize(tools.descartes.librede.models.state.IStateModel, tools.descartes.librede.models.observation.IObservationModel, int)
	 */
	@Override
	public void initialize(IStateModel<?> stateModel,
			IObservationModel<?, ?> observationModel, int estimationWindow) throws InitializationException {
		super.initialize(stateModel, observationModel, estimationWindow);
		initStateConstraints(stateModel.getConstraints());
		
		this.stateSize = stateModel.getStateSize(); // number of variables
		this.constraintCount = nonlinearConstraints.size() + linearConstraints.size(); // number of constraints g(x)
		
		allocateNativeMemory();
		
		// Determine number of zeros in jacobi an hessian matrices.
		nele_jac = stateSize * constraintCount;
		nele_hess = 0;	
		// NOTE: lower or upper triangular part only
		for (int i = 1; i <= stateSize; i++) {
			nele_hess += i;
		}
		estimationBuffer = matrix(estimationWindow, stateSize, Double.NaN);
	}
	

	/* (non-Javadoc)
	 * @see tools.descartes.librede.algorithm.IEstimationAlgorithm#update()
	 */
	@Override
	public void update() {
		if (stateSize < 0) {
			throw new IllegalStateException("Method initialize() must be called before calling estimate().");
		}		
		
		if (!initialized) {
			// Set initial state
			Vector initialState = getStateModel().getInitialState();
			if (!initialState.isEmpty()) {
				setState(initialState);
				initialized = true;
			}
		}
		
		if (initialized) {
			Vector estimate = solve();
			
			estimationBuffer = estimationBuffer.circshift(1).setRow(0, estimate);		
		}
	}
	
	@Override
	public Vector estimate() throws EstimationException {
		return mean(estimationBuffer, 0);
	}

	public void updateObjectiveFunction() {
		IObservationModel<?, ?> observationModel = getObservationModel();
	
		Vector o_real = observationModel.getObservedOutput();
		Vector o_calc = observationModel.getCalculatedOutput(current);
			
		// The absolute error between the calculated output based on the current state and
		// the observed output.
		error = o_real.minus(o_calc);
		// Calculate Jacobi matrix
		jacobi = JacobiMatrixBuilder.calculateOfObservationModel(observationModel, current);				
		// obj = sum((h_real - h_calc(x)) .^ 2)
		obj = norm2(error);
		// objGrad = sum(-2 * (h_real - h_calc(x)) * h_calc'(x))				
		objGrad = (Vector) transpose(jacobi).multipliedBy(error).times(-2.0);
		
//		System.out.println(current + "; " + error + "; " + obj + "; " + objGrad);
	}
	
	public void updateJacobiOfConstraints() {
		jacobiConstr = empty();
		if (linearConstraints.size() > 0) {
			jacobiConstr = JacobiMatrixBuilder.calculateOfConstraints(linearConstraints, current);
		}		
		if (nonlinearConstraints.size() > 0) {
			Matrix res = JacobiMatrixBuilder.calculateOfConstraints(nonlinearConstraints, current);
			jacobiConstr = vertcat(jacobiConstr, res);
		}
	}

	public void updateLagrangeMatrix(double obj_factor, double[] lambda) {	
		lagrange = zeros(stateSize, stateSize);
		
		int outputIdx = 0;

		for (IOutputFunction function : getObservationModel()) {					
			Matrix dev2 = HessianMatrixBuilder.calculateOfOutputFunction(function, current);
			
			Matrix u = dev2.times(error.get(outputIdx));					
			
			Vector jacobiRow = jacobi.row(outputIdx);
			Matrix v = u.minus(jacobiRow.multipliedBy(transpose(jacobiRow)));
			
			lagrange = lagrange.plus(v.times(-2 * obj_factor));
			
			outputIdx++;
		}

		// add portion for constraints
		for (int i = linearConstraints.size(); i < lambda.length; i++) {
			Matrix dev2 = HessianMatrixBuilder.calculateOfConstraint(nonlinearConstraints.get(i), current);
			
			lagrange = lagrange.plus(dev2.times(lambda[i]));
		}
	}

	@Override
	public void destroy() {
		if (nlp != null) {
			/* free allocated memory */
			IpoptLibrary.INSTANCE.IpOpt_FreeIpoptProblem(nlp);
			nlp = null;
		}
		g_L = null;
		g_U = null;
		x = null;
		x_L = null;
		x_U = null;
	}
	
	private class F implements Eval_F_CB {
		@Override
		public boolean eval_f(int n, Pointer x, boolean new_x,
				Pointer obj_value, Pointer user_data) {
			if (new_x) {
				current = nativeVector(stateSize, x);
				updateObjectiveFunction();
			}
			
			obj_value.setDouble(0, obj);
			
			return true;
		}
	}
	
	private class GradF implements Eval_Grad_F_CB {
		@Override
		public boolean eval_grad_f(int n, Pointer x, boolean new_x, Pointer grad_f, Pointer user_data) {
			if (new_x) {
				current = nativeVector(stateSize, x);
				updateObjectiveFunction();
			}
			toNative(grad_f, objGrad);			
			return true;
		}
	}
	
	private class G implements Eval_G_CB {
		@Override
		public boolean eval_g(int n, Pointer x, boolean new_x, int m, Pointer g, Pointer user_data) {
			if (new_x) {
				current = nativeVector(stateSize, x);
				updateObjectiveFunction();
			}
			
			int i = 0;
			for (ILinearStateConstraint c : linearConstraints) {
				g.setDouble(i, c.getValue(current));
				i++;
			}
			for (IStateConstraint c : nonlinearConstraints) {
				g.setDouble(i, c.getValue(current));
				i++;
			}
			
			return true;
		}
	}
	
	private class JacG implements Eval_Jac_G_CB {
		@Override
		public boolean eval_jac_g(int n, Pointer x, boolean new_x, int m, int nele_jac, Pointer iRow, Pointer jCol,
				Pointer values, Pointer user_data) {
			try {
				if (new_x) {
					current = nativeVector(stateSize, x);
					updateObjectiveFunction();
				}
				
			
				if (values == Pointer.NULL) {
					/* return the structure of the jacobian */
	
					/* this particular jacobian is dense */
					int[] iRowArr = new int[n * m];
					int[] jColArr = new int[n * m];
	
					for (int i = 0; i < m; i++) {
						for (int j = 0; j < n; j++) {
							iRowArr[i * m + j] = i;
							jColArr[i * m + j] = j;
						}
					}
	
					iRow.write(0, iRowArr, 0, iRowArr.length);
					jCol.write(0, jColArr, 0, jColArr.length);
				} else {
					updateJacobiOfConstraints();
					if (jacobiConstr != null) {
						toNative(values, jacobiConstr);
					}
				}
	
				} catch(Throwable e) {
					e.printStackTrace();
					return false;
				}
				return true;
		}
	}
	
	private class H implements Eval_H_CB {
		@Override
		public boolean eval_h(int n, Pointer x, boolean new_x, double obj_factor, int m, Pointer lambda,
				boolean new_lambda, int nele_hess, Pointer iRow, Pointer jCol, Pointer values, Pointer user_data) {		
			if (new_x) {
				current = nativeVector(stateSize, x);
				updateObjectiveFunction();
			}

			int arraySize = 0;				
			for (int i = 1; i <= n; i++) {
				arraySize += i;
			}

			if (values == Pointer.NULL) {
				/* return the structure. This is a symmetric matrix, fill the lower left
				 * triangle only. */
				
				int[] iRowArr = new int[arraySize];
				int[] jColArr = new int[arraySize];

				/* the hessian for this problem is actually dense */
				int idx = 0;
				for (int row = 0; row < n; row++) {
					for (int col = 0; col <= row; col++) {
						iRowArr[idx] = row;
						jColArr[idx] = col;
						idx++;
					}
				}
				iRow.write(0, iRowArr, 0, iRowArr.length);
				jCol.write(0, jColArr, 0, jColArr.length);
			}
			else {
				/* return the values. This is a symmetric matrix, fill the lower left
				 * triangle only */
				
				if (new_lambda) {
					double[] lambdaArr = lambda.getDoubleArray(0, m);
					updateLagrangeMatrix(obj_factor, lambdaArr);
				}

				double[] valuesArr = new double[arraySize];				
				int idx = 0;
				for (int row = 0; row < n; row++) {
					for (int col = 0; col <= row; col++) {
						valuesArr[idx] = lagrange.get(row, col);
						idx++;
					}
				}
				values.write(0, valuesArr, 0, valuesArr.length);
			}

			return true;
		}
	}
}
