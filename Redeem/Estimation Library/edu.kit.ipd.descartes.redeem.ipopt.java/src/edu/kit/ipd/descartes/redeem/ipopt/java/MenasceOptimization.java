package edu.kit.ipd.descartes.redeem.ipopt.java;

import static edu.kit.ipd.descartes.linalg.LinAlg.*;

import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;

import edu.kit.ipd.descartes.linalg.Matrix;
import edu.kit.ipd.descartes.linalg.Vector;
import edu.kit.ipd.descartes.linalg.VectorFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.algorithm.IEstimationAlgorithm;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.HessianMatrixBuilder;
import edu.kit.ipd.descartes.redeem.estimation.models.diff.JacobiMatrixBuilder;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.IObservationModel;
import edu.kit.ipd.descartes.redeem.estimation.models.observation.functions.IOutputFunction;
import edu.kit.ipd.descartes.redeem.estimation.models.state.ConstantStateModel;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.INonLinearConstraint;
import edu.kit.ipd.descartes.redeem.estimation.models.state.constraints.StateBoundsConstraint;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_F_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_G_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_Grad_F_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_H_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_Jac_G_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.IpoptLibrary;
import edu.kit.ipd.descartes.redeem.nativehelper.NativeDoubleStorage;
import edu.kit.ipd.descartes.redeem.nativehelper.NativeHelper;

public class MenasceOptimization implements IEstimationAlgorithm<ConstantStateModel<INonLinearConstraint>, IObservationModel<IOutputFunction, Vector>> {	
	
	private final static int BYTE_SIZE_DOUBLE = 8;
	
	private final static String OPTION_TOL_NAME = "tol";
	private final static double OPTION_TOL_VALUE = 1e-7;
	private final static String OPTION_LNP_LOWER_BOUND_INF_NAME = "nlp_lower_bound_inf";
	private final static double OPTION_LNP_LOWER_BOUND_INF_VALUE = -1e-19;
	private final static String OPTION_LNP_UPPER_BOUND_INF_NAME = "nlp_upper_bound_inf";
	private final static double OPTION_LNP_UPPER_BOUND_INF_VALUE = 1e-19;
	
	
	private ConstantStateModel<INonLinearConstraint> stateModel;
	private IObservationModel<IOutputFunction, Vector> observationModel;
	
	private int stateSize;
	private int constraintCount;
	
	private List<INonLinearConstraint> g_constr;
	
	@Override
	public Vector estimate() {	
		
		stateSize = stateModel.getStateSize(); // number of variables
		
		g_constr = filterGeneralConstraints(stateModel.getConstraints());
		
		List<StateBoundsConstraint> x_bounds = filterStateBoundsConstraints(stateModel.getConstraints());		
		
		constraintCount = g_constr.size(); // number of constraints g(x)
		
		
		Memory x_L; // lower bounds on x
		Memory x_U; // upper bounds on x
		Memory g_L; // lower bounds on g
		Memory g_U; // upper bounds on g
		
		
		/* allocate space for the variable bounds */
		x_L = new Memory(BYTE_SIZE_DOUBLE * stateSize);
		x_U = new Memory(BYTE_SIZE_DOUBLE * stateSize);
		
		/* set the values for the variable bounds to default value */
		for (int i = 0; i < stateSize; i++) {
			x_L.setDouble(i * BYTE_SIZE_DOUBLE, OPTION_LNP_LOWER_BOUND_INF_VALUE);
			x_U.setDouble(i * BYTE_SIZE_DOUBLE, OPTION_LNP_UPPER_BOUND_INF_VALUE);
		}
		
		for (StateBoundsConstraint c : x_bounds) {
			x_L.setDouble(c.getStateVariable() * BYTE_SIZE_DOUBLE, c.getLowerBound());
			x_U.setDouble(c.getStateVariable() * BYTE_SIZE_DOUBLE, c.getUpperBound());
		}
		
		/* allocate space for the constraint bounds */
		g_L = new Memory(BYTE_SIZE_DOUBLE * constraintCount);
		g_U = new Memory(BYTE_SIZE_DOUBLE * constraintCount);
				  
		/* set the values of the constraint bounds to default value */
		for (int i = 0; i < constraintCount; i++) {
			g_L.setDouble(i * BYTE_SIZE_DOUBLE, g_constr.get(i).getLowerBound());
			g_U.setDouble(i * BYTE_SIZE_DOUBLE, g_constr.get(i).getUpperBound());
		}
		
		/* Number of nonzeros in the Jacobian of the constraints */
		int nele_jac = 0;
		/* Number of nonzeros in the Hessian of the Lagrangian (lower or upper triangual part only) */
		int nele_hess = 0;
		/* indexing style for matrices */
		int index_style = 0; // C-style; start counting of rows and column indices at 0
		
		Pointer nlp;             /* IpoptProblem */
		
		OptimizationProblemEvaluator evalCallbacks = new OptimizationProblemEvaluator();
		
		nlp = IpoptLibrary.INSTANCE.CreateIpoptProblem(stateSize, x_L, x_U, constraintCount, g_L, g_U, nele_jac, nele_hess,
                index_style, evalCallbacks, evalCallbacks, evalCallbacks,
                evalCallbacks, evalCallbacks);
		
		/* We can free the memory now - the values for the bounds have been copied internally in CreateIpoptProblem */
	    x_L = null;
	    x_U = null;
	    g_L = null;
	    g_U = null;

	    /* Set some options.  Note the following ones are only examples,
	     they might not be suitable for your problem. */
	    IpoptLibrary.INSTANCE.AddIpoptNumOption(nlp, OPTION_TOL_NAME, OPTION_TOL_VALUE);
	    IpoptLibrary.INSTANCE.AddIpoptNumOption(nlp, OPTION_LNP_LOWER_BOUND_INF_NAME, OPTION_LNP_LOWER_BOUND_INF_VALUE);
	    IpoptLibrary.INSTANCE.AddIpoptNumOption(nlp, OPTION_LNP_UPPER_BOUND_INF_NAME, OPTION_LNP_UPPER_BOUND_INF_VALUE);
		
		/* allocate space for the initial point and set the values */
		Memory x = new Memory(BYTE_SIZE_DOUBLE * stateSize); /* starting point and solution vector */
		copy(stateModel.getInitialState(), x);

		/* allocate space to store the bound multipliers at the solution */
		Memory mult_g = new Memory(BYTE_SIZE_DOUBLE * constraintCount); // constraint multipliers at the solution
		Memory mult_x_L = new Memory(BYTE_SIZE_DOUBLE * stateSize); // lower bound multipliers at the solution
		Memory mult_x_U = new Memory(BYTE_SIZE_DOUBLE * stateSize); // upper bound multipliers at the solution

		DoubleByReference obj = new DoubleByReference(); // objective value
		
		/* solve the problem */
		int status = IpoptLibrary.INSTANCE.IpoptSolve(nlp, x, null, obj, mult_g, mult_x_L, mult_x_U, null);
		
		if (status == IpoptLibrary.IP_SOLVE_SUCCEEDED) {
			System.out.println("\n\nSolution of the primal variables, x");
			for (int i=0; i < stateSize; i++) {
				System.out.printf("x[%d] = %e\n", i, x.getDouble(i * BYTE_SIZE_DOUBLE));
			}

			System.out.println("\n\nSolution of the ccnstraint multipliers, lambda");
			for (int i=0; i < constraintCount; i++) {
				System.out.printf("lambda[%d] = %e\n", i, mult_g.getDouble(i * BYTE_SIZE_DOUBLE));
			}
			System.out.println("\n\nSolution of the bound multipliers, z_L and z_U");
			for (int i=0; i < stateSize; i++) {
				System.out.printf("z_L[%d] = %e\n", i, mult_x_L.getDouble(i * BYTE_SIZE_DOUBLE));
			}
			for (int i=0; i < stateSize; i++) {
				System.out.printf("z_U[%d] = %e\n", i, mult_x_U.getDouble(i * BYTE_SIZE_DOUBLE));
			}

			System.out.println("\n\nObjective value");
			System.out.printf("f(x*) = %e\n", obj.getValue());
		}
		else {
			System.out.println("\n\nERROR OCCURRED DURING IPOPT OPTIMIZATION.");
		}
		  
		/* free allocated memory */
		IpoptLibrary.INSTANCE.FreeIpoptProblem(nlp);

		x = null;
		mult_g = null;
		mult_x_L = null;
		mult_x_U = null;
		
		return null;
	}
	
	private List<INonLinearConstraint> filterGeneralConstraints(List<INonLinearConstraint> constraints) {
		List<INonLinearConstraint> filtered = new ArrayList<>();
		for (INonLinearConstraint c : constraints) {
			if (!(c instanceof StateBoundsConstraint)) {
				filtered.add(c);
			}
		}		
		return filtered;		
	}
	
	private List<StateBoundsConstraint> filterStateBoundsConstraints(List<INonLinearConstraint> constraints) {
		List<StateBoundsConstraint> filtered = new ArrayList<>();
		for (INonLinearConstraint c : constraints) {
			if (c instanceof StateBoundsConstraint) {
				filtered.add((StateBoundsConstraint) c);
			}
		}		
		return filtered;		
	}
	
	private void copy(Vector v, Memory target) {
		for (int i = 0; i < v.rows(); i++) {
			target.setDouble(i * BYTE_SIZE_DOUBLE, v.get(i));
		}
	}
	
	private class OptimizationProblemEvaluator implements Eval_F_CB, Eval_Grad_F_CB, Eval_G_CB, Eval_Jac_G_CB, Eval_H_CB {
		
		// Caches the current state vector
		private Vector state;
		
		// Caches the difference between observed and calculated response time
		private Vector respDiff;
		
		// Caches the first derivative of the observation model
		private Vector respDiffGrad;

		@Override
		public boolean eval_h(int n, Pointer x, boolean new_x,
				double obj_factor, int m, Pointer lambda, boolean new_lambda,
				int nele_hess, Pointer iRow, Pointer jCol, Pointer values,
				Pointer user_data) {
			updateInternalState(x, new_x);
			
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
				for (int row = 0; row < 4; row++) {
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


				double[] valuesArr = new double[10];

				Matrix lagrange = zeros(n, n);

				for (IOutputFunction function : observationModel) {
					Matrix dev2 = HessianMatrixBuilder.calculateOfOutputFunction(function, state);
					
					Matrix u = dev2.arrayMultipliedBy(repmat(transpose(respDiff), dev2.rows(), 1));
					
					Matrix v = u.minus(repmat(transpose(respDiffGrad.arrayMultipliedBy(respDiffGrad)), u.rows(), 1));
					
					lagrange = lagrange.plus(v.times(obj_factor));
				}


				// add portion for constraints

				for (int i = 0; i < m; i++) {
					Matrix dev2 = HessianMatrixBuilder.calculateOfConstraint(stateModel.getConstraints().get(i), state);
					
					lagrange = lagrange.plus(dev2.times(lambdaArr[i]));
				}
				
				int idx = 0;
				for (int row = 0; row < 4; row++) {
					for (int col = 0; col <= row; col++) {
						valuesArr[idx] = lagrange.get(row, col);
						idx++;
					}
				}
				values.write(0, valuesArr, 0, valuesArr.length);
			}

			return true;
		}

		@Override
		public boolean eval_jac_g(int n, Pointer x, boolean new_x, int m,
				int nele_jac, Pointer iRow, Pointer jCol, Pointer values,
				Pointer user_data) {
			updateInternalState(x, new_x);
			
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
				Matrix jacobi = JacobiMatrixBuilder.calculateOfConstraints(g_constr, state);
				jacobi.toDoubleStorage(new NativeDoubleStorage(values));
			}

			return true;
		}

		@Override
		public boolean eval_g(int n, Pointer x, boolean new_x, int m,
				Pointer g, Pointer user_data) {
			updateInternalState(x, new_x);
			
			Vector gVec = vector(m, new VectorFunction() {
				@Override
				public double cell(int row) {
					return g_constr.get(row).getValue(state);
				}				  
			});

			gVec.toDoubleStorage(new NativeDoubleStorage(g));
			
			return true;
		}

		@Override
		public boolean eval_grad_f(int n, Pointer x, boolean new_x,
				Pointer grad_f, Pointer user_data) {
			updateInternalState(x, new_x);
			
			respDiffGrad.toDoubleStorage(new NativeDoubleStorage(grad_f));
			
			return true;
		}

		@Override
		public boolean eval_f(int n, Pointer x, boolean new_x,
				Pointer obj_value, Pointer user_data) {
			updateInternalState(x, new_x);
			
			obj_value.setDouble(0, norm2(respDiff));
			
			return true;
		}
		
		private void updateInternalState(Pointer x, boolean new_x) {
			if (new_x) {
				state = vector(stateSize, new NativeDoubleStorage(x));
				
				Vector r_real = observationModel.getObservedOutput();
				Vector r_calc = observationModel.getCalculatedOutput(state);
				respDiff = r_real.minus(r_calc);
				
				Matrix jacobi = JacobiMatrixBuilder.calculateOfObservationModel(observationModel, state);
				respDiffGrad = (Vector)jacobi.multipliedBy(respDiff).times(-2.0);
			}
		}
		
		
		
	}

	@Override
	public void initialize(ConstantStateModel<INonLinearConstraint> stateModel,
			IObservationModel<IOutputFunction, Vector> observationModel) {
		this.stateModel = stateModel;
		this.observationModel = observationModel;
		
	}

	@Override
	public void destroy() {
		
	}
	
}
