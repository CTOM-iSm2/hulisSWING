/**
 * 
 */
package org.ctom.hulis.files;


/**
 * This interface must be implemented to be used in Writexyz class
 *
 */
public interface Writer {
	/**
	 * implement this method to print in US format
	 * @param format
	 * @param symbol
	 * @param x
	 * @param y
	 * @param z
	 */
	public void  printfUS( String format, String symbol, double x, double y, double z) ;

	/**
	 * @param format
	 * @param i
	 */
	public void printf(String format, int i);

	/**
	 * @param i
	 */
	public void println(int i);

	/**
	 * @param string
	 */
	public void println(String string);

	/**
	 * 
	 */
	public void println();

	/**
	 * @param string
	 */
	public void print(String string);
	
}
