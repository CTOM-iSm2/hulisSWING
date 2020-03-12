package util.polyCalculator;

import org.ctom.hulis.polynomes.Polynomial;
import org.ctom.hulis.util.math.Complex;

public interface PolyCalculatorListener {
	
	/**
	 * triggered when the result function was modified
	 * @param pf
	 */
	public void resultModified(Polynomial pf, Complex[] roots);
	
	/**
	 * triggered when the TRE was modified
	 * @param nElec
	 */
	public void treModified(int nElec, double tre);

}
