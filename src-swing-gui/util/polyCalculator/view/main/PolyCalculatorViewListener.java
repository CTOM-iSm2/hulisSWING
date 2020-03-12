package util.polyCalculator.view.main;

import org.ctom.hulis.polynomes.Polynomial;

public interface PolyCalculatorViewListener {
	
	
	/**
	 * triggered when the coefficient was modified in the gui
	 * @param pf
	 * @param coeff
	 */
	public void CoeffModifiedinGUI (Polynomial pf, double coeff);

	public void removePoly(Polynomial pf);

}
