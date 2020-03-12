package util.polyCalculator;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import org.ctom.hulis.polynomes.Polynomial;
import org.ctom.hulis.polynomes.PolynomialSolver;
import org.ctom.hulis.util.math.Complex;

import util.io.SwingIO;
import util.polyCalculator.view.elements.PolyType;
import util.polyCalculator.view.main.PolyCalculatorView;
import util.polyCalculator.view.main.PolyCalculatorViewListener;

public class PolyCalculator implements PolyCalculatorViewListener {

	private ArrayList<Polynomial> listPoly=new ArrayList<Polynomial>();
	private Hashtable<Polynomial, Double> coeffs = new Hashtable<Polynomial, Double>();
	private PolyCalculatorView gui;
	private ArrayList<PolyCalculatorListener> listener = new ArrayList<PolyCalculatorListener>();
	private int nelec=0;

	/**
	 * Constructor
	 */
	public PolyCalculator(int nelec) {
		this.gui=new PolyCalculatorView();
		this.gui.addlistener(this);
		this.addListener(gui);
		this.setNelec(nelec);
	}

	/**
	 * add a PolyCalculatorListener to the list of listener of the element
	 * @param l
	 */
	private void addListener(PolyCalculatorListener l) {
		this.listener.add(l);
	}


	/**
	 * Adds a polynomial function to the calculator
	 * @param c
	 * @param pf
	 */
	public void add(double c, Polynomial polynomial) {
		Polynomial pf = normalizePoly(polynomial);
		listPoly.add(pf);
		coeffs.put(pf,c);
		addPolyToGui(pf,PolyType.NORMAL);
		updateResult();
	}

	/**
	 * @param polynomial
	 * @return
	 */
	private Polynomial normalizePoly(Polynomial polynomial) {
		double aMax = polynomial.getCoefficients()[polynomial.getDegree()];
		Polynomial pf = polynomial.multiply(new Polynomial(new double[] {1/aMax}));
		return pf;
	}

	/**
	 * Removes a polynomial function of the calculator
	 */
	public void remove(Polynomial pf) {
		listPoly.remove(pf);
		coeffs.remove(pf);
		updateResult();
	}

	/**
	 * Sends a polynomial to the gui
	 * @param pf
	 */
	private void addPolyToGui(Polynomial pf, PolyType pt) {
		gui.add(pf, PolyCalculator.getRoots(pf), pt);
	}

	/**
	 * Removes a polynomial from the gui
	 * param pf
	 */
	private void removePolyFromGui(Polynomial pf) {
		gui.remove(pf);
	}

	/**
	 * Get the roots of a polynomial function
	 * if the roots couls not be found return null
	 * @param pf
	 * @return the roots or null
	 */
	public static Complex[] getRoots(Polynomial pf) {
		PolynomialSolver solver = new PolynomialSolver(pf);
		Object[] array=null;
		try {
			array = solver.getRoots().toArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Complex[] roots = new Complex[array.length];
		for (int i=0; i<array.length; i++) {
			Complex z = (Complex)array[i];
			roots[i] = new Complex(z);
		}
		return sortRoots(roots);
	}

	private static Complex[] sortRoots(Complex[] roots) {
		int n=roots.length;
		double[] realParts = new double[n];
		int i=0;
		for (Complex z : roots) {
			if (z.getImag()!=0) {
				SwingIO.PrintIf("At least one root is complex : no ordering possible");
				return roots;
			}
			realParts[i++] = z.getReal();
		}
		Arrays.sort(realParts);
		for (i=0; i< realParts.length; i++) {
			roots[i] = new Complex(realParts[n-1-i],0.0);
		}
		return roots;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PolyCalculator pc = new PolyCalculator(4);
		pc.add(1.0, new Polynomial(new double[] {1.0, 0.0, 1.0}));
		pc.add(1.0, new Polynomial(new double[] {2.0, -1.0, 1.0, 0.0, 1.0}));
	}

	public void CoeffModifiedinGUI(Polynomial pf, double coeff) {
		coeffs.remove(pf);
		coeffs.put(pf, coeff);
		updateResult();
		return;
	}

	/**
	 * update and return the result function
	 * @return
	 */
	private void updateResult() {
		if (this.listPoly.size()==0) return;
		Polynomial res=new Polynomial(new double[] {0.0});
		for (Polynomial pf : this.listPoly) {
			res=res.add(pf.multiply(new Polynomial(new double[] {coeffs.get(pf)})));
		}
		res=normalizePoly(res);
		Complex[] roots = PolyCalculator.getRoots(res);
		for (PolyCalculatorListener l : this.listener ) {
			l.resultModified(res, roots);
		}
		double tre=computeTRE(roots);
		for (PolyCalculatorListener l : this.listener) {
			l.treModified(nelec, tre);
		}
	}

	private boolean areAllReal(Complex[] roots) {
		for (Complex r : roots) {
			if (r.getImag()!=0.0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param roots
	 * @return
	 */
	private double computeTRE(Complex[] roots) {
		if (!areAllReal(roots)) {
			return Double.NaN;
		}
		double tre = 0.0;
		int nPair = nelec/2;
		int nSing = nelec%2;
		int i;
		for (i=0; i < nPair; i++) {
			tre+=2*roots[i].getReal();
		}
		tre+=nSing*roots[nPair].getReal();
		return tre;
	}

	/**
	 * @return the nelec
	 */
	public int getNelec() {
		return nelec;
	}

	/**
	 * @param nelec the nelec to set
	 */
	private void setNelec(int nelec) {
		this.nelec = nelec;
		if (this.listPoly.size()>0) {
			this.updateResult();
		} 
	}

	public void removePoly(Polynomial pf) {
		this.remove(pf);
	}

}
