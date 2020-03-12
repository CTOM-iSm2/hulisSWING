/**
 * 
 */
package org.ctom.util.maths;

/**
 * @author macbook
 *
 */

public class Maths {
	public static double round(double n, int precision) {
		/*double a = n;
		// Arrondir la valeur a 10^-n
		double b = (int) ((a + 5 * Math.pow(10, -(precision + 1))) * Math.pow(
				10, precision));
		double c = b / Math.pow(10, precision);
		return c;*/
			double p = Math.pow(10.0, precision);
			return Math.floor((n*p)+0.5) / p;
		}


	/**
	 * return the minimmal value a or b. If a or b == NaN, return the values that not equals to NaN
	 * @param a
	 * @param b
	 * @return a or b and never NaN
	 */
	public static double minNotNaN (double a, double b){
		if (Double.isNaN(a)) 
			return b;
		else if (Double.isNaN(b))  {
			return a;
		}
		else 
			return Math.min(a, b);
		
	}

	/**
	 * return the maximal value a or b. If a or b == NaN, return the values that not equals to NaN
	 * @param a
	 * @param b
	 * @return a or b and never NaN
	 */
	public static double maxNotNaN (double a, double b){
		if (Double.isNaN(a)) 
			return b;
		else if (Double.isNaN(b)) 
			return a;
		else 
			return Math.max(a, b);
		
	}
	
	/**
	 * transform the values from an array in positive values
	 * @param array
	 * @return new array : all values of the array in parameter are positive values
	 */
	public static double[] abs(double[]array) {
		double[] res = new double[array.length]; 
		for (int i=0 ; i < array.length ; i++) {
			res[i] = Math.abs(array[i]);
		}
		return res;
	}
	

}
