package util.polyCalculator.view.elements;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.ctom.hulis.polynomes.Polynomial;
import org.ctom.hulis.util.math.Complex;

import util.polyCalculator.view.main.PolyCalculatorView;

public class PolyView extends JPanel implements CoeffPolySpinnerListener, CheckBoxPolyListener, PolyTextFieldListener {

	public static final int ROOT_WIDTH = 500;
	public static final int POLY_WIDTH = 300;
	public static final int STANDARD_HEIGHT = 60;
	private Polynomial pf;
	double coeff=1.0;
	private PolyTextField textPoly;
	private JTextField textRoots;
	private CheckBoxPoly checkBoxPoly;
	private JCheckBox cb;
	private Container affichage_coeff;
	private Box box;
	private ArrayList<PolyViewListener> listener = new ArrayList<PolyViewListener>();

	/**
	 * a composite object which contains all infos about a polynomial
	 * @param pf
	 * @param pt
	 */
	public PolyView(Polynomial pf, Complex[] roots, PolyType pt) {
		box = Box.createHorizontalBox();
		textPoly=new PolyTextField(pf);
		textPoly.addlistener(this);
		textRoots=new JTextField();
		textRoots.setEditable(false);
		cb = new JCheckBox();
		checkBoxPoly=new CheckBoxPoly(pf, cb);
		checkBoxPoly.setListener(this);
		setPoly(pf, roots);
		JSpinner sp = new JSpinner(new SpinnerNumberModel(coeff, -99., 99., 0.1));
		Dimension dim = sp.getPreferredSize();
		int h=dim.height;
		int w=dim.width;
		Dimension dim1 = new Dimension(w, STANDARD_HEIGHT);
		switch (pt) {
			case NORMAL:
				affichage_coeff = sp;
				sp.setPreferredSize(dim1);
				sp.setMaximumSize(dim1);
				sp.setMinimumSize(dim1);
				new CoeffPolySpinner(pf, sp).setListener(this);
				break;
			case RESULT:
				affichage_coeff = new JTextField("Total");
				affichage_coeff.setPreferredSize(dim1);
				((JTextField)affichage_coeff).setEditable(false);
				affichage_coeff.setMaximumSize(dim1);
				affichage_coeff.setMinimumSize(dim1);
			default:
				break;
		}

		box.add(affichage_coeff);
		Dimension dimPoly = new Dimension(POLY_WIDTH, STANDARD_HEIGHT);
		JScrollPane jsp;
		jsp = new JScrollPane();
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		jsp.setMaximumSize(dimPoly);
		jsp.setMinimumSize(dimPoly);
		jsp.setPreferredSize(dimPoly);
		jsp.setViewportView(textPoly);
		box.add(jsp);
		Dimension dimRoot = new Dimension(ROOT_WIDTH, STANDARD_HEIGHT);
		jsp = new JScrollPane();
		jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		jsp.setMaximumSize(dimRoot);
		jsp.setMinimumSize(dimRoot);
		jsp.setPreferredSize(dimRoot);
		jsp.setViewportView(textRoots);
		box.add(jsp);
		box.add(cb);
		box.add(Box.createHorizontalGlue());
		this.setLayout(new GridLayout(1, 1));
		this.add(box);
	}

	public void addlistener(PolyViewListener l) {
		listener.add(l);
	}

	public void setPoly(Polynomial pf, Complex[] roots) {
		this.pf=pf;
		this.textPoly.setPf(pf);
		String str="";
		for (Complex r : roots) {
			str+=PolyCalculatorView.complexFormat.format(r)+" ; ";
		}
		textRoots.setText(str);
	}

	public void switchPlot() {
		for (PolyViewListener l : listener) {
			l.switchPlot(this.pf);
		}
	}

	public void CoeffModified(double coeff) {
		this.coeff=coeff;
		for (PolyViewListener l : listener) {
			l.CoeffModified(this.pf, coeff);
		}
	}

	public Polynomial getPolynomial() {
		return pf;
	}

	public void removePoly(Polynomial pf) {
		for (PolyViewListener pvl : this.listener) {
			pvl.removePoly(pf);
		}
	}

}

