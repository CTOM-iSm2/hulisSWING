/**
 * 
 */
package org.ctom.util.format;

/**
 * @author macbook
 *
 */
public class DefaultNumberFormater implements NumberFormater {

	/* (non-Javadoc)
	 * @see org.ctom.util.format.NumberFormater#formatNumber(double)
	 */
	//@Override
	public String formatNumber(double number) {
		return Double.toString(number);
		
	}

}
