package edu.kit.ipd.descartes.redeem.ipopt.java;

import static edu.kit.ipd.descartes.linalg.LinAlg.norm2;
import static edu.kit.ipd.descartes.linalg.LinAlg.transpose;
import static edu.kit.ipd.descartes.linalg.LinAlg.vector;
import static edu.kit.ipd.descartes.linalg.LinAlg.vertcat;
import static edu.kit.ipd.descartes.linalg.LinAlg.zeros;
import static edu.kit.ipd.descartes.redeem.nativehelper.NativeHelper.*;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.redeem.estimation.algorithm.IEstimationAlgorithm;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.HessianMatrixBuilder;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.JacobiMatrixBuilder;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.ILinearStateConstraint;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.IStateConstraint;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.StateBoundsConstraint;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_F_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_G_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_Grad_F_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_H_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_Jac_G_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.IpoptLibrary;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.IpoptOptionKeyword;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.IpoptOptionValue;
import edu.kit.ipd.descartes.redeem.nativehelper.NativeHelper;

public class MenasceOptimization implements IEstimationAlgorithm<ConstantStateModel<? extends IStateConstraint>, IObservationModel<IOutputFunction, Vector>> {	

	private final static double OPTION_TOL_VALUE = 1e-7;
	private final static double OPTION_LNP_LOWER_BOUND_INF_VALUE = -1e+19;
	private final static double OPTION_LNP_UPPER_BOUND_INF_VALUE = 1e+19;
	
	// C-style; start counting of rows and column indices at 0
	private final static int IPOPT_INDEX_STYLE = 0;
	
	/* Number of variables of the optimization problem. */
	private int stateSize = -1;
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
	private OptimizationState sharedState = new OptimizationState();
	private F evalf = new F(sharedState); /* objective function f(x): R^n --> R */
	private G evalg = new G(sharedState); /* constraint functions g(x): R^n --> R^m */
	private GradF evalgradf = new GradF(sharedState); /* first derivatives of the objective function */
	private JacG evaljacg = new JacG(sharedState); /* Jacobi matrix of the constraint functions */
	private H evalh = new H(sharedState); /* Hessian matrix of the Lagrangian */
	
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
	private DoubleByReference obj; /* current objective value of optimization */
	
			
	
	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.algorithm.IEstimationAlgorithm#initialize(edu.kit.ipd.descartes.redeem.estimation.models.state.IStateModel, edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel)
	 */
	@Override
	public void initialize(ConstantStateModel<? extends IStateConstraint> stateModel,
			IObservationModel<IOutputFunction, Vector> observationModel) {
		
		initStateConstraints(stateModel.getConstraints());
		
		this.stateSize = stateModel.getStateSize(); // number of variables
		this.constraintCount = nonlinearConstraints.size() + linearConstraints.size(); // number of constraints g(x)
		
		initOptimizationState(observationModel);
		
		allocateNativeMemory();
		
		// Set initial state
		copy(stateModel.getInitialState(), x);	
		
		// Determine number of zeros in jacobi an hessian matrices.
		nele_jac = stateSize * constraintCount;
		nele_hess = 0;	
		// NOTE: lower or upper triangular part only
		for (int i = 1; i <= stateSize; i++) {
			nele_hess += i;
		}
	}
	

	/* (non-Javadoc)
	 * @see edu.kit.ipd.descartes.redeem.estimation.models.algorithm.IEstimationAlgorithm#estimate()
	 */
	@Override
	public Vector estimate() {
		if (stateSize < 0) {
			throw new IllegalStateException("Method initialize() must be called before calling estimate().");
		}		
		
		setOptimizationBounds();
		
		setOptimizationConstraints();
		
		Pointer nlp = IpoptLibrary.INSTANCE.IpOpt_CreateIpoptProblem(stateSize, x_L, x_U, constraintCount, g_L, g_U, nele_jac, nele_hess,
                IPOPT_INDEX_STYLE, evalf, evalg, evalgradf,
                evaljacg, evalh);
		
		setOptimizationOptions(nlp);	
		
		/* solve the problem */
		int status = IpoptLibrary.INSTANCE.IpOpt_IpoptSolve(nlp, x, Pointer.NULL, obj, Pointer.NULL, Pointer.NULL, Pointer.NULL, Pointer.NULL);
		
		Vector estimate = zeros(stateSize);		
		if (status == IpoptLibrary.IP_SOLVE_SUCCEEDED) {
			estimate = nativeVector(stateSize, x);		
		} else {
			System.out.println("\n\nERROR OCCURRED DURING IPOPT OPTIMIZATION: " + status);
		}
		  
		/* free allocated memory */
		IpoptLibrary.INSTANCE.IpOpt_FreeIpoptProblem(nlp);

		return estimate;
	}
	
	private void initOptimizationState(IObservationModel<IOutputFunction, Vector> observationModel) {
		sharedState.observationModel = observationModel;
		sharedState.nonlinearConstraints = nonlinearConstraints;
		sharedState.linearConstraints = linearConstraints;
		sharedState.stateSize = stateSize;			
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

		obj = new DoubleByReference(); // objective value
	}

	private void setOptimizationOptions(Pointer nlp) {
		IpoptLibrary.INSTANCE.IpOpt_AddIpoptIntOption(nlp, IpoptOptionKeyword.PRINT_LEVEL.toNativeString(), 1);
		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, IpoptOptionKeyword.MU_STRATEGY.toNativeString(), 
				IpoptOptionValue.ADAPTIVE.toNativeString());
//		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, IpoptOptionKeyword.CHECK_DERIVATIVES_FOR_NANINF.toNativeString(), 
//				IpoptOptionValue.YES.toNativeString());
//		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, "output_file", "ipopt.out");
//		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, IpoptOptionKeyword.DERIVATIVE_TEST.toNativeString(), 
//				IpoptOptionValue.SECOND_ORDER.toNativeString());
//		IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, IpoptOptionKeyword.DERIVATIVE_TEST_PRINT_ALL.toNativeString(), 
//				IpoptOptionValue.YES.toNativeString());
	    IpoptLibrary.INSTANCE.IpOpt_AddIpoptNumOption(nlp, IpoptOptionKeyword.TOL.toNativeString(), OPTION_TOL_VALUE);
	    IpoptLibrary.INSTANCE.IpOpt_AddIpoptNumOption(nlp, IpoptOptionKeyword.NLP_LOWER_BOUND_INF.toNativeString(), OPTION_LNP_LOWER_BOUND_INF_VALUE);
	    IpoptLibrary.INSTANCE.IpOpt_AddIpoptNumOption(nlp, IpoptOptionKeyword.NLP_UPPER_BOUND_INF.toNativeString(), OPTION_LNP_UPPER_BOUND_INF_VALUE);		
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
			NativeHelper.setDoubleArray(x_U, i, OPTION_LNP_UPPER_BOUND_INF_VALUE);
		}
		
		for (StateBoundsConstraint c : boundsConstraints) {
			NativeHelper.setDoubleArray(x_L, c.getStateVariable(), c.getLowerBound());
			NativeHelper.setDoubleArray(x_U, c.getStateVariable(), c.getUpperBound());
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
	
	private void copy(Vector v, Pointer target) {
		for (int i = 0; i < v.rows(); i++) {
			NativeHelper.setDoubleArray(target, i, v.get(i));
		}
	}
	
	private static class F implements Eval_F_CB {
		
		private OptimizationState state;
		
		public F(OptimizationState state) {
			super();
			this.state = state;
		}
		
		@Override
		public boolean eval_f(int n, Pointer x, boolean new_x,
				Pointer obj_value, Pointer user_data) {
			
			state.update(x, new_x);
			
			obj_value.setDouble(0, state.obj);
			
			return true;
		}
	}
	
	private static class GradF implements Eval_Grad_F_CB {
		
		private OptimizationState state;
		
		public GradF(OptimizationState state) {
			super();
			this.state = state;
		}
		
		@Override
		public boolean eval_grad_f(int n, Pointer x, boolean new_x, Pointer grad_f, Pointer user_data) {
			
			state.update(x, new_x);
			
			toNative(grad_f, state.objGrad);
			
			return true;
		}
	}
	
	private static class G implements Eval_G_CB {
		
		private OptimizationState state;
		
		public G(OptimizationState state) {
			super();
			this.state = state;
		}

		@Override
		public boolean eval_g(int n, Pointer x, boolean new_x, int m, Pointer g, Pointer user_data) {
			
			state.update(x, new_x);
			
			int i = 0;
			for (ILinearStateConstraint c : state.linearConstraints) {
				g.setDouble(i, c.getValue(state.current));
				i++;
			}
			for (IStateConstraint c : state.nonlinearConstraints) {
				g.setDouble(i, c.getValue(state.current));
				i++;
			}
			
			return true;
		}
	}
	
	private static class JacG implements Eval_Jac_G_CB {
		
		private OptimizationState state;

		public JacG(OptimizationState state) {
			super();
			this.state = state;
		}

		@Override
		public boolean eval_jac_g(int n, Pointer x, boolean new_x, int m, int nele_jac, Pointer iRow, Pointer jCol,
				Pointer values, Pointer user_data) {
			try {
			
			state.update(x, new_x);
			
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
				/* return the values of the jacobian of the constraints */
				Matrix jacobi = null;
				
				if (state.linearConstraints.size() > 0) {
					jacobi = JacobiMatrixBuilder.calculateOfConstraints(state.linearConstraints, state.current);
				}
				if (state.nonlinearConstraints.size() > 0) {
					Matrix res = JacobiMatrixBuilder.calculateOfConstraints(state.nonlinearConstraints, state.current);
					if (jacobi == null) {
						jacobi = res;
					} else {
						jacobi = vertcat(jacobi, res);
					}
				}
				if (jacobi != null) {
					toNative(values, jacobi);
				}
			}

			} catch(Throwable e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}
	}
	
	private static class H implements Eval_H_CB {
		
		private OptimizationState state;

		public H(OptimizationState state) {
			super();
			this.state = state;
		}

		@Override
		public boolean eval_h(int n, Pointer x, boolean new_x, double obj_factor, int m, Pointer lambda,
				boolean new_lambda, int nele_hess, Pointer iRow, Pointer jCol, Pointer values, Pointer user_data) {
			
			state.update(x, new_x);
			
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
				double[] lambdaArr = lambda.getDoubleArray(0, m);


				double[] valuesArr = new double[arraySize];

				Matrix lagrange = zeros(n, n);
				
				int outputIdx = 0;
				for (IOutputFunction function : state.observationModel) {					
					Matrix dev2 = HessianMatrixBuilder.calculateOfOutputFunction(function, state.current);
					
					Matrix u = dev2.times(state.error.get(outputIdx));
					
					
					Vector jacobiRow = state.jacobi.row(outputIdx);
					Matrix v = u.minus(jacobiRow.multipliedBy(transpose(jacobiRow)));
					
					lagrange = lagrange.plus(v.times(-2 * obj_factor));
					
					outputIdx++;
				}


				// add portion for constraints

				for (int i = state.linearConstraints.size(); i < m; i++) {
					Matrix dev2 = HessianMatrixBuilder.calculateOfConstraint(state.nonlinearConstraints.get(i), state.current);
					
					lagrange = lagrange.plus(dev2.times(lambdaArr[i]));
				}
				
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
	
	private static class OptimizationState {
		
		public int stateSize;
		
		public IObservationModel<IOutputFunction, Vector> observationModel;
		
		public List<IStateConstraint> nonlinearConstraints;
		public List<ILinearStateConstraint> linearConstraints;
		
		// Caches the current state vector
		public Vector current;
		
		public Vector error;
		
		// Caches the difference between observed and calculated response time
		public double obj;
		
		public Matrix jacobi;
		
		// Caches the first derivative of the observation model
		public Vector objGrad;

		public void update(Pointer x, boolean new_x) {
			if (new_x) {
				current = nativeVector(stateSize, x);
			
				Vector o_real = observationModel.getObservedOutput();
				Vector o_calc = observationModel.getCalculatedOutput(current);
				error = o_real.minus(o_calc);			
				
				obj = norm2(error);
							
				jacobi = JacobiMatrixBuilder.calculateOfObservationModel(observationModel, current);
				
				objGrad = (Vector)transpose(jacobi).multipliedBy(error).times(-2.0);
			}
		}		
	}

	@Override
	public void destroy() {
		
	}
	
}
