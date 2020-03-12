package util.polyCalculator.view.main;

import java.text.DecimalFormat;

import org.ctom.hulis.util.math.Complex;
import org.ctom.hulis.util.math.Complex.ComplexFormater;

public class ComplexFormat implements ComplexFormater {
	DecimalFormat decimalFormat;
	
	/**
	 * @param decimalFormat
	 */
	public ComplexFormat(DecimalFormat decimalFormat ) {
		this.decimalFormat = decimalFormat;
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.util.math.Complex.ComplexFormater#formatNumber(double)
	 */
	public String formatNumber(double number) {
		return decimalFormat.format(number);
	}
	
	/**
	 * @param complex
	 * @return
	 */
	public String format(Complex complex) {
		return Complex.format(complex, this);
	}

}
