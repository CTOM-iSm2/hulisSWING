package util.polyCalculator.view.elements;

import org.ctom.hulis.polynomes.Polynomial;

public interface PolyViewListener {

	/**
	 * triggered when the coeff was modified
	 * @param pf
	 * @param coeff
	 */
	public void CoeffModified(Polynomial pf, double coeff);
	
	/**
	 * triggered when the checkbox was checked/unchecked
	 * @param pf
	 */
	public void switchPlot(Polynomial pf);
	
	/**
	 * triggered when removing a polynomial from the list
	 */
	public void removePoly(Polynomial pf);

}
