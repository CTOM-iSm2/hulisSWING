package util.polyCalculator.view.elements;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ctom.hulis.polynomes.Polynomial;

public class CoeffPolySpinner implements ChangeListener {

	private CoeffPolySpinnerListener listener;
	
	/**
	 * an object to treat easily a polynomial and a spinner
	 * @param pf
	 * @param sp
	 */
	public CoeffPolySpinner(Polynomial pf , JSpinner sp) {
		sp.addChangeListener(this);
	}
	
	public void setListener(CoeffPolySpinnerListener l) {
		listener=l;
	}

	public void stateChanged(ChangeEvent arg0) {
		double val=(Double)((JSpinner)(arg0.getSource())).getValue();
		listener.CoeffModified(val);
	}

}
