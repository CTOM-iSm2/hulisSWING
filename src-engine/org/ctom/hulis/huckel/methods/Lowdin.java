package org.ctom.hulis.huckel.methods;

import org.ctom.hulis.huckel.Input;
import org.ctom.hulis.huckel.exception.LowdinException;
import org.ctom.hulis.util.io.HuckelIO;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;


public class Lowdin extends Input {

	private Matrix Hamiltonien;

	private Matrix hamiltonien_modifie;

	private Matrix matE;

	private String Nom;

	private Matrix Overlap;

	private int size;

	private Matrix sMinusHalf;

	private EigenvalueDecomposition solution;

	private Matrix sPlusHalf;

	private double[] valeursPropresI;

	private double[] valeursPropresR;

	private Matrix vecteursPropresHinitial;

	private Matrix vecteursPropresHmodifie;

	private Matrix vecteursPropresOverlap;

	public Lowdin(Matrix MatriceH, Matrix MatriceS, String nom)
			throws LowdinException {

		this.Hamiltonien = MatriceH;
		this.size = this.Hamiltonien.getColumnDimension();
		if (Input.overrideS_UNIT) {
			this.Overlap = MatriceS;
		} else {
			this.Overlap = Matrix.identity(this.size, this.size);
		}
		HuckelIO.PrintIf("size S:" + this.size + " H: "
				+ this.Hamiltonien.getColumnDimension() + "\n");
		this.Nom = nom;
		solve();

	}

	/**
	 * @return the hamiltonien
	 */
	public Matrix getHamiltonien() {
		return this.Hamiltonien;
	}

	/**
	 * @return the hamiltonien_modifie
	 */
	public Matrix getHamiltonien_modifie() {
		return this.hamiltonien_modifie;
	}

	/**
	 * @return the matE
	 */
	public Matrix getMatE() {
		return this.matE;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return this.Nom;
	}

	/**
	 * @return the overlap
	 */
	public Matrix getOverlap() {
		return this.Overlap;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return this.size;
	}

	/**
	 * @return the sMinusHalf
	 */
	public Matrix getSMinusHalf() {
		return this.sMinusHalf;
	}

	/**
	 * @return the solution
	 */
	public EigenvalueDecomposition getSolution() {
		return this.solution;
	}

	/**
	 * @return the sPlusHalf
	 */
	public Matrix getSPlusHalf() {
		return this.sPlusHalf;
	}

	/**
	 * @return the valeursPropresI
	 */
	public double[] getValeursPropresI() {
		return this.valeursPropresI;
	}

	/**
	 * @return the valeursPropresR
	 */
	public double[] getValeursPropresR() {
		return this.valeursPropresR;
	}

	/**
	 * @return the vecteursPropresHinitial
	 */
	public Matrix getVecteursPropresHinitial() {
		return this.vecteursPropresHinitial;
	}

	/**
	 * @return the vecteursPropresHmodifie
	 */
	public Matrix getVecteursPropresHmodifie() {
		return this.vecteursPropresHmodifie;
	}

	/**
	 * @return the vecteursPropresOverlap
	 */
	public Matrix getVecteursPropresOverlap() {
		return this.vecteursPropresOverlap;
	}

	private void solve() throws LowdinException {

		this.solution = new EigenvalueDecomposition(this.Overlap);
		this.valeursPropresR = this.solution.getRealEigenvalues();
		this.valeursPropresI = this.solution.getImagEigenvalues();
		this.vecteursPropresOverlap = this.solution.getV();
		for (int iVal = 0; iVal < this.size; iVal++) {
			if (this.valeursPropresR[iVal] == 0) {
				throw new LowdinException("Redundancy in the basis!!");
			}
		}

		// calcul de S^+0.5 et S^-0.5
		this.sPlusHalf = new Matrix(this.size, this.size);
		this.sMinusHalf = new Matrix(this.size, this.size);
		for (int i = 0; i < this.size; i++) {
			this.sPlusHalf.set(i, i, Math.pow(this.valeursPropresR[i], +0.5));
			this.sMinusHalf.set(i, i, Math.pow(this.valeursPropresR[i], -0.5));
		}
		this.sPlusHalf = this.vecteursPropresOverlap.times(this.sPlusHalf
				.times(this.vecteursPropresOverlap.transpose()));
		this.sMinusHalf = this.vecteursPropresOverlap.times(this.sMinusHalf
				.times(this.vecteursPropresOverlap.transpose()));
		Matrix tmp = this.Hamiltonien.times(this.sMinusHalf);
		this.hamiltonien_modifie = this.sMinusHalf.times(tmp);
		this.solution = new EigenvalueDecomposition(this.hamiltonien_modifie);
		this.valeursPropresR = this.solution.getRealEigenvalues();
		this.valeursPropresI = this.solution.getImagEigenvalues();
		this.vecteursPropresHmodifie = this.solution.getV();
		this.vecteursPropresHinitial = this.sMinusHalf
				.times(this.vecteursPropresHmodifie);
		this.matE = new Matrix(this.size, this.size);
		for (int i = 0; i < this.size; i++) {
			this.matE.set(i, i, this.valeursPropresR[i]);
		}
	}

}
