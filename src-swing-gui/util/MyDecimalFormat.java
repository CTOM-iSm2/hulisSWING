package util;

import java.text.DecimalFormat;

/**
 * 
 * @author macbook
 *
 */
public class MyDecimalFormat extends DecimalFormat{

	
	
	/**
	 * format a number to format #.#### 
	 * @param precision
	 */
	public MyDecimalFormat(int precision) {
		this(precision, "0");
	}
	
	/**
	 * format a number to format #.#### or 0.0000
	 * @param precision
	 * @param formatToRepeat : 0 or # (use # if somewhere else is set)
	 */
	public MyDecimalFormat(int precision, String formatToRepeat) {
		String format ="#";
		if (formatToRepeat.equals("#") || formatToRepeat.equals("0") )		
		format=	formatToRepeat;
		if (precision > 0)
			format = format + ".";
		for (int i =0; i<precision; i++) 
			format += formatToRepeat;
		this.applyPattern(format);	
	}
	
	
	
	
	/**
	 * 
	 * @param decimalformat
	 */
	public MyDecimalFormat(String format) {
		super(format);
	}

	/**
	 * Format the number in parameter, and if it is zero, delete the minus symbol
	 * ex: -0.000 -> 0.000 
	 * @param n
	 * @return
	 */
	public String formatZero(double n) {
		String s = this.format(n);

		if (isZero(s) && (s.substring(0, 1).equals("-")))
			s = s.substring(1, s.length());

		// f (s.substring(0, 1) !="-")
		// return " " +s;

		return s;

	}

	private boolean isZero(String s) {
		for (int i = 0; i < s.length(); i++) {
			if ((s.charAt(i) != '-') && (s.charAt(i) != '.')
					&& (s.charAt(i) != ','))
				if (s.charAt(i) != '0')
					return false;
		}

		return true;
	}
}
