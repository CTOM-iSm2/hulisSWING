package org.ctom.hulis.util;

/**
 * VectorssHuckel : This class sorts arrays : it is useful because the Eigen
 * decomposition functions in the package Jama matrix don't return the values in
 * the direction in which we want to display them
 * 
 * @author Nicolas GOUDARD
 * @version 1.0
 * @since
 * 
 */

public class VectorssHuckel {

	/**
	 * sort : this function sort a double [] vector and it returns a new vector
	 * 
	 * @param vector
	 *            to sort
	 * @return double []
	 */

	public static double[] sort(double[] vector) {
		double[] vecteurEnOrdre = new double[vector.length];

		for (int i = 0; i < vector.length; i++)
			vecteurEnOrdre[i] = vector[vector.length - 1 - i];

		return vecteurEnOrdre;
	}

	/**
	 * sort : this function sort a double [][] matrix and it returns a new
	 * matrix
	 * 
	 * @param matrix
	 *            to  sort
	 * @return double [][]
	 */
	public static double[][] sort(double[][] matrix) {
		double[][] matriceEnOrdre = new double[matrix.length][matrix.length];
		int i;
		int j;
		for (i = 0; i < matrix.length; i++) {
			for (j = 0; j < matrix[i].length; j++) {
				matriceEnOrdre[i][j] = matrix[i][matrix[i].length - 1 - j];
			}
		}
		return matriceEnOrdre;
	}

}
