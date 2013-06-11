package edu.kit.ipd.descartes.redeem.ipopt.java;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.DoubleByReference;

import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_F_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_G_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_Grad_F_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_H_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Eval_Jac_G_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.Intermediate_CB;
import edu.kit.ipd.descartes.redeem.ipopt.java.backend.IpoptLibrary;

public class Example {
	
	public static void main(String[] args) {
		
		System.loadLibrary("IpOpt");
		
		 int n=-1;                          /* number of variables */
		  int m=-1;                          /* number of constraints */
		  Memory x_L;                  /* lower bounds on x */
		  Memory x_U;                  /* upper bounds on x */
		  Memory g_L;                  /* lower bounds on g */
		  Memory g_U;                  /* upper bounds on g */
		  Pointer nlp;             /* IpoptProblem */
		  int status; /* Solve return code */
		  Memory x;                    /* starting point and solution vector */
		  Memory mult_g;               /* constraint multipliers
		             at the solution */
		  Memory mult_x_L;             /* lower bound multipliers
		             at the solution */
		  Memory mult_x_U;             /* upper bound multipliers
		             at the solution */
		  DoubleByReference obj = new DoubleByReference();                          /* objective value */
		  int i;                             /* generic counter */

		  /* Number of nonzeros in the Jacobian of the constraints */
		  int nele_jac = 8;
		  /* Number of nonzeros in the Hessian of the Lagrangian (lower or
		     upper triangual part only) */
		  int nele_hess = 10;
		  /* indexing style for matrices */
		  int index_style = 0; /* C-style; start counting of rows and column
		             indices at 0 */

		  /* our user data for the function evalutions. */
		  Memory user_data;

		  /* set the number of variables and allocate space for the bounds */
		  n=4;
		  x_L = new Memory(8*n);
		  x_U = new Memory(8*n);
		  /* set the values for the variable bounds */
		  for (i=0; i<n; i++) {
			  x_L.setDouble(i*8, 1.0);
			  x_U.setDouble(i*8, 5.0);
		  }

		  /* set the number of constraints and allocate space for the bounds */
		  m=2;
		  g_L = new Memory(8*m);
		  g_U = new Memory(8*m);
				  
		  /* set the values of the constraint bounds */
		  g_L.setDouble(0, 25);
		  g_U.setDouble(0, 2e19);
		  g_L.setDouble(1*8, 40);
		  g_U.setDouble(1*8, 40);

		  /* create the IpoptProblem */
		  nlp = IpoptLibrary.INSTANCE.IpOpt_CreateIpoptProblem(n, x_L, x_U, m, g_L, g_U, nele_jac, nele_hess,
		                           index_style, new Eval_f(), new Eval_g(), new Eval_grad_f(),
		                           new Eval_jac_g(), new Eval_h());

		  /* We can free the memory now - the values for the bounds have been
		     copied internally in CreateIpoptProblem */
		  x_L = null;
		  x_U = null;
		  g_L = null;
		  g_U = null;

		  /* Set some options.  Note the following ones are only examples,
		     they might not be suitable for your problem. */
		  IpoptLibrary.INSTANCE.IpOpt_AddIpoptNumOption(nlp, "tol", 1e-7);
		  IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, "mu_strategy", "adaptive");
		  IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, "output_file", "ipopt.out");

		  /* allocate space for the initial point and set the values */
		  x = new Memory(8*n);
		  x.setDouble(0, 1.0);
		  x.setDouble(1*8, 5.0);
		  x.setDouble(2*8, 5.0);
		  x.setDouble(3*8, 1.0);

		  /* allocate space to store the bound multipliers at the solution */
		  mult_g = new Memory(8*m);
		  mult_x_L = new Memory(8*n);
		  mult_x_U = new Memory(8*n);

		  /* Initialize the user data */
		  user_data = new Memory(16);
		  user_data.setDouble(0, 0.0);
		  user_data.setDouble(8, 0.0);

		  /* Set the callback method for intermediate user-control.  This is
		   * not required, just gives you some intermediate control in case
		   * you need it. */
		  /* SetIntermediateCallback(nlp, intermediate_cb); */

		  /* solve the problem */
		  status = IpoptLibrary.INSTANCE.IpOpt_IpoptSolve(nlp, x, null, obj, mult_g, mult_x_L, mult_x_U, user_data);

		  if (status == IpoptLibrary.IP_SOLVE_SUCCEEDED) {
		    System.out.println("\n\nSolution of the primal variables, x");
		    for (i=0; i<n; i++) {
		    	System.out.printf("x[%d] = %e\n", i, x.getDouble(i*8));
		    }

		    System.out.println("\n\nSolution of the ccnstraint multipliers, lambda");
		    for (i=0; i<m; i++) {
		    	System.out.printf("lambda[%d] = %e\n", i, mult_g.getDouble(i*8));
		    }
		    System.out.println("\n\nSolution of the bound multipliers, z_L and z_U");
		    for (i=0; i<n; i++) {
		    	System.out.printf("z_L[%d] = %e\n", i, mult_x_L.getDouble(i*8));
		    }
		    for (i=0; i<n; i++) {
		    	System.out.printf("z_U[%d] = %e\n", i, mult_x_U.getDouble(i*8));
		    }

		    System.out.println("\n\nObjective value");
		    System.out.printf("f(x*) = %e\n", obj.getValue());
		  }
		  else {
		    System.out.println("\n\nERROR OCCURRED DURING IPOPT OPTIMIZATION.");
		  }

		  /* Now we are going to solve this problem again, but with slightly
		     modified constraints.  We change the constraint offset of the
		     first constraint a bit, and resolve the problem using the warm
		     start option. */
		  user_data.setDouble(0, 0.2);

		  if (status == IpoptLibrary.IP_SOLVE_SUCCEEDED) {
		    /* Now resolve with a warmstart. */
			  IpoptLibrary.INSTANCE.IpOpt_AddIpoptStrOption(nlp, "warm_start_init_point", "yes");
		    /* The following option reduce the automatic modification of the
		       starting point done my Ipopt. */
			  IpoptLibrary.INSTANCE.IpOpt_AddIpoptNumOption(nlp, "bound_push", 1e-5);
			  IpoptLibrary.INSTANCE.IpOpt_AddIpoptNumOption(nlp, "bound_frac", 1e-5);
		    status = IpoptLibrary.INSTANCE.IpOpt_IpoptSolve(nlp, x, null, obj, mult_g, mult_x_L, mult_x_U, user_data);

		    if (status == IpoptLibrary.IP_SOLVE_SUCCEEDED) {
		      System.out.println("\n\nSolution of the primal variables, x");
		      for (i=0; i<n; i++) {
		    	  System.out.printf("x[%d] = %e\n", i, x.getDouble(i*8));
		      }

		      System.out.println("\n\nSolution of the ccnstraint multipliers, lambda");
		      for (i=0; i<m; i++) {
		    	  System.out.printf("lambda[%d] = %e\n", i, mult_g.getDouble(i*8));
		      }
		      System.out.println("\n\nSolution of the bound multipliers, z_L and z_U");
		      for (i=0; i<n; i++) {
		    	  System.out.printf("z_L[%d] = %e\n", i, mult_x_L.getDouble(i*8));
		      }
		      for (i=0; i<n; i++) {
		    	  System.out.printf("z_U[%d] = %e\n", i, mult_x_U.getDouble(i*8));
		      }

		      System.out.println("\n\nObjective value");
		      System.out.printf("f(x*) = %e\n", obj.getValue());
		    }
		    else {
		      System.out.println("\n\nERROR OCCURRED DURING IPOPT OPTIMIZATION WITH WARM START.");
		    }
		  }

		  /* free allocated memory */
		  IpoptLibrary.INSTANCE.IpOpt_FreeIpoptProblem(nlp);

		  x = null;
		  mult_g = null;
		  mult_x_L = null;
		  mult_x_U = null;
	}
	
	
	public static class Eval_f implements Eval_F_CB {
		@Override
		public boolean eval_f(int n, Pointer x, boolean new_x,
				Pointer obj_value, Pointer user_data) {
			 assert n == 4;
			 
			 double[] xArr = x.getDoubleArray(0, 4);

			 obj_value.setDouble(0, xArr[0] * xArr[3] * (xArr[0] + xArr[1] + xArr[2]) + xArr[2]);

			 return true;
		}
	}
	
	public static class Eval_grad_f implements Eval_Grad_F_CB {
		@Override
		public boolean eval_grad_f(int n, Pointer x, boolean new_x, Pointer grad_f,
				Pointer user_data) {
			assert n == 4;
			
			double[] xArr = x.getDoubleArray(0, 4);
			double[] grad_fArr = new double[4];

		  grad_fArr[0] = xArr[0] * xArr[3] + xArr[3] * (xArr[0] + xArr[1] + xArr[2]);
		  grad_fArr[1] = xArr[0] * xArr[3];
		  grad_fArr[2] = xArr[0] * xArr[3] + 1;
		  grad_fArr[3] = xArr[0] * (xArr[0] + xArr[1] + xArr[2]);
		  
		  grad_f.write(0, grad_fArr, 0, grad_fArr.length);

		  return true;
		}
	}
	
	public static class Eval_g implements Eval_G_CB {
		@Override
		public boolean eval_g(int n, Pointer x, boolean new_x, int m,
				Pointer g, Pointer user_data) {

			  assert n == 4;
			  assert m == 2;
			  
			  double[] xArr = x.getDoubleArray(0, 4);
			  double[] gArr = new double[2];

			  gArr[0] = xArr[0] * xArr[1] * xArr[2] * xArr[3] + user_data.getDouble(0);
			  gArr[1] = xArr[0]*xArr[0] + xArr[1]*xArr[1] + xArr[2]*xArr[2] + xArr[3]*xArr[3] + user_data.getDouble(8);
			  
			  g.write(0, gArr, 0, gArr.length);

			  return true;
		}
	}

	public static class Eval_jac_g implements Eval_Jac_G_CB {
		@Override
		public boolean eval_jac_g(int n, Pointer x, boolean new_x, int m,
				int nele_jac, Pointer iRow, Pointer jCol, Pointer values,
				Pointer user_data) {
			if (values == Pointer.NULL) {
			    /* return the structure of the jacobian */

			    /* this particular jacobian is dense */
				int[] iRowArr = new int[8];
				int[] jColArr = new int[8];
				
				iRowArr[0] = 0;
				jColArr[0] = 0;
			    iRowArr[1] = 0;
			    jColArr[1] = 1;
			    iRowArr[2] = 0;
			    jColArr[2] = 2;
			    iRowArr[3] = 0;
			    jColArr[3] = 3;
			    iRowArr[4] = 1;
			    jColArr[4] = 0;
			    iRowArr[5] = 1;
			    jColArr[5] = 1;
			    iRowArr[6] = 1;
			    jColArr[6] = 2;
			    iRowArr[7] = 1;
			    jColArr[7] = 3;
			    
			    iRow.write(0, iRowArr, 0, iRowArr.length);
			    jCol.write(0, jColArr, 0, jColArr.length);
			  }
			  else {
			    /* return the values of the jacobian of the constraints */
				  
				  double[] xArr = x.getDoubleArray(0, 4);
				  double[] valuesArr = new double[8];

			    valuesArr[0] = xArr[1]*xArr[2]*xArr[3]; /* 0,0 */
			    valuesArr[1] = xArr[0]*xArr[2]*xArr[3]; /* 0,1 */
			    valuesArr[2] = xArr[0]*xArr[1]*xArr[3]; /* 0,2 */
			    valuesArr[3] = xArr[0]*xArr[1]*xArr[2]; /* 0,3 */

			    valuesArr[4] = 2*xArr[0];         /* 1,0 */
			    valuesArr[5] = 2*xArr[1];         /* 1,1 */
			    valuesArr[6] = 2*xArr[2];         /* 1,2 */
			    valuesArr[7] = 2*xArr[3];         /* 1,3 */
			    
			    values.write(0, valuesArr, 0, valuesArr.length);
			  }

			  return true;
		}
	}

	public static class Eval_h implements Eval_H_CB {
		@Override
		public boolean eval_h(int n, Pointer x, boolean new_x,
				double obj_factor, int m, Pointer lambda, boolean new_lambda,
				int nele_hess, Pointer iRow, Pointer jCol, Pointer values,
				Pointer user_data) {
			int idx = 0; /* nonzero element counter */
			  int row = 0; /* row counter for loop */
			  int col = 0; /* col counter for loop */
			  if (values == Pointer.NULL) {
			    /* return the structure. This is a symmetric matrix, fill the lower left
			     * triangle only. */
				  
					int[] iRowArr = new int[10];
					int[] jColArr = new int[10];

			    /* the hessian for this problem is actually dense */
			    idx=0;
			    for (row = 0; row < 4; row++) {
			      for (col = 0; col <= row; col++) {
			        iRowArr[idx] = row;
			        jColArr[idx] = col;
			        idx++;
			      }
			    }
			    iRow.write(0, iRowArr, 0, iRowArr.length);
			    jCol.write(0, jColArr, 0, jColArr.length);
			    assert(idx == nele_hess);
			  }
			  else {
			    /* return the values. This is a symmetric matrix, fill the lower left
			     * triangle only */
				  
				  double[] xArr = x.getDoubleArray(0, 4);
				  double[] lambdaArr = lambda.getDoubleArray(0, 2);
				  double[] valuesArr = new double[10];

			    /* fill the objective portion */
				  valuesArr[0] = obj_factor * (2*xArr[3]);               /* 0,0 */

				  valuesArr[1] = obj_factor * (xArr[3]);                 /* 1,0 */
				  valuesArr[2] = 0;                                   /* 1,1 */

				  valuesArr[3] = obj_factor * (xArr[3]);                 /* 2,0 */
				  valuesArr[4] = 0;                                   /* 2,1 */
				  valuesArr[5] = 0;                                   /* 2,2 */

				  valuesArr[6] = obj_factor * (2*xArr[0] + xArr[1] + xArr[2]); /* 3,0 */
				  valuesArr[7] = obj_factor * (xArr[0]);                 /* 3,1 */
				  valuesArr[8] = obj_factor * (xArr[0]);                 /* 3,2 */
				  valuesArr[9] = 0;                                   /* 3,3 */


			    /* add the portion for the first constraint */
				  valuesArr[1] += lambdaArr[0] * (xArr[2] * xArr[3]);          /* 1,0 */

				  valuesArr[3] += lambdaArr[0] * (xArr[1] * xArr[3]);          /* 2,0 */
				  valuesArr[4] += lambdaArr[0] * (xArr[0] * xArr[3]);          /* 2,1 */

				  valuesArr[6] += lambdaArr[0] * (xArr[1] * xArr[2]);          /* 3,0 */
				  valuesArr[7] += lambdaArr[0] * (xArr[0] * xArr[2]);          /* 3,1 */
				  valuesArr[8] += lambdaArr[0] * (xArr[0] * xArr[1]);          /* 3,2 */

			    /* add the portion for the second constraint */
				  valuesArr[0] += lambdaArr[1] * 2;                      /* 0,0 */

				  valuesArr[2] += lambdaArr[1] * 2;                      /* 1,1 */

				  valuesArr[5] += lambdaArr[1] * 2;                      /* 2,2 */

				  valuesArr[9] += lambdaArr[1] * 2;                      /* 3,3 */
				  
				  values.write(0, valuesArr, 0, valuesArr.length);
			  }

			  return true;
		}
	}

	public static class Intermediate implements Intermediate_CB {
		@Override
		public boolean execute(int alg_mod, int iter_count, double obj_value,
				double inf_pr, double inf_du, double mu, double d_norm,
				double regularization_size, double alpha_du, double alpha_pr,
				int ls_trials, Pointer user_data) {
				System.out.printf("Testing intermediate callback in iteration %d\n", iter_count);
			  if (inf_pr < 1e-4) return false;

			  return true;
		}
	}



	

}
