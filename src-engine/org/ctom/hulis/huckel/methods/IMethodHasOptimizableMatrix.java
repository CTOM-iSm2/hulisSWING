/**
 * 
 */
package org.ctom.hulis.huckel.methods;

/**
 * @author macbook
 *
 */
public interface IMethodHasOptimizableMatrix {
	/**
	 * remove the column and the line i in the overlap matrix
	 * @param i
	 */
	public void removeInOptimizableMatrix(int i);

	/**
	 * compute the column and the line i in the overlap matrix
	 * @param i
	 */
	public void computeOptimizableMatrix(int i);

	
	/**
	 * add a column and a line  in the overlap matrix
	 * @param i
	 */
	public void extendOptimizableMatrix();

	
	/**
	 * compute overlap matrix
	 */
	public void computeAllOptimizableMatrix();
	
	/**
	 * @return true if the overlapMatrix is ok
	 */
	public boolean checkOptimizableMatrix();

}
