package util;

import java.text.DecimalFormat;

import org.ctom.util.format.NumberFormater;

public class SwingNumberFormater implements NumberFormater {
	protected DecimalFormat decimalFormat;
	
	
	/**
	 * return double to string or NaN or Inf string
	 * if the result is double, add the suffix
	 * @param n
	 * @param form
	 * @param suffix
	 * @return
	 */
	public static String doubleToString(double n, DecimalFormat form, String suffix) {
		if (Double.isNaN(n))
			return  "NaN";
		else 
		if (Double.isInfinite(n))
			return  "Inf";
		else 
			return form.format(n)+ suffix;
		
		
	}
	
	/**
	 * return double to string or NaN or Inf string
	 * @param n
	 * @param form
	 * @return
	 */
	public static String doubleToString(double n, DecimalFormat form) {
		return doubleToString(n, form, "");
		
		
	}
	
	/**
	 * 
	 * @param decimalformat
	 */
	public SwingNumberFormater(DecimalFormat decimalformat) {
		this.decimalFormat = decimalformat;
	}

	
	/* (non-Javadoc)
	 * @see org.ctom.util.format.NumberFormater#formatNumber(double)
	 */
	public String formatNumber(double number) {
		return decimalFormat.format(number);

	}

	
	public DecimalFormat getDecimalFormat() {
		return decimalFormat;
	}
	

}
