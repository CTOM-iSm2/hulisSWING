package org.ctom.util.maths.minimizer;

import Jama.Matrix;


public interface MultiVariableFunction {
	
	/*
	 * A Multivariable Function takes getDimension() variables and returns a value getValue. 
	 */

	/*
	 * returns the number of variables the function depends on 
	 */
	public int getDimension();
	
	/*
	 * returns the value of the function for input v.
	 * v is a one dimensional matrix of dimension getDimension.
	 */
	public double getValue(Matrix v);


}