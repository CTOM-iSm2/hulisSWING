/**
 * 
 */
package org.ctom.util.common;

/**
 * @author macbook
 *
 */
public class Arrays {
	public static double[] arrayCopy(double array[]) {
		if (array == null) return null;
		double[] arraycopy = new double[array.length];
		for (int i = 0; i < array.length; i++)
			arraycopy[i] = array[i];
		return arraycopy;
	}

	public static double[][] arrayCopy(double array[][]) {
		if (array == null) return null;
		if (array.length == 0) return new double[0][0];
		double[][] arraycopy = new double[array.length][array[0].length];
		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[i].length; j++)
				arraycopy[i][j] = array[i][j];
		return arraycopy;
	}

	public static int[] arrayCopy(int array[]) {
		if (array == null) return null;
		int[] arraycopy = new int[array.length];
		for (int i = 0; i < array.length; i++)
			arraycopy[i] = array[i];
		return arraycopy;
	}

	public static int[][] arrayCopy(int array[][]) {
		if (array == null) return null;
		if (array.length == 0) return new int[0][0];
		int[][] arraycopy = new int[array.length][array[0].length];
		for (int i = 0; i < array.length; i++)
			for (int j = 0; j < array[i].length; j++)
				arraycopy[i][j] = array[i][j];
		return arraycopy;
	}

	public static boolean areEquals(int array1[][], int array2[][]) {
		if ((array1 == null) && (array2 != null)) return false;
		if ((array1 != null) && (array2 == null)) return false;

		if (array1.length != array2.length) return false;
		if (array1[0].length != array2[0].length) return false;

		for (int i = 0; i < array1.length; i++)
			for (int j = 0; j < array1[i].length; j++)
				if (array1[i][j] != array2[i][j])
					return false;
		return true;
	}

	
	/**
	 * convert double[] to Double[]
	 * @param array
	 * @return
	 */
	public static Double[] toDoubleArray(double[]array) {
		Double[] res = new Double[array.length]; 
		for (int i=0 ; i < array.length ; i++) {
			res[i] = array[i];
		}
		return res;
	}
	
	/**
	 * convert Double[] to double[]
	 * @param array
	 * @return
	 */
	public static double[] toDoublePrimitiveArray(Double[]array) {
		double[] res = new double[array.length]; 
		for (int i=0 ; i < array.length ; i++) {
			res[i] = array[i];
		}
		return res;
	}


}
