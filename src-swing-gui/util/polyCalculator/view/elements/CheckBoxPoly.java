package util.polyCalculator.view.elements;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ctom.hulis.polynomes.Polynomial;

public class CheckBoxPoly implements ChangeListener {

	private Polynomial pf;
	private CheckBoxPolyListener listener;

	/**
	 * An object to connect easily a polynomial and a chekcbox
	 * @param pf
	 * @param cb
	 */
	public CheckBoxPoly(Polynomial pf , JCheckBox cb) {
		setPoly(pf);
		cb.addChangeListener(this);
	}

	public void setListener(CheckBoxPolyListener l) {
		listener=l;
	}

	public void setPoly(Polynomial pf) {
		this.pf=pf;
	}

	public void stateChanged(ChangeEvent arg0) {
		listener.switchPlot();
	}

}
