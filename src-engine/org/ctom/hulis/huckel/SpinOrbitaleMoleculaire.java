package org.ctom.hulis.huckel;

import java.io.Serializable;

import org.ctom.hulis.huckel.exception.OrbitaleMoleculaireException;
import org.ctom.hulis.huckel.exception.SpinOrbitaleMoleculaireException;


public class SpinOrbitaleMoleculaire extends OrbitaleMoleculaire implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7473959347477473706L;
	
	private Spin spin = Spin.NOSPIN_ALPHA;
	private SpinOrbitaleMoleculaire partner=null;
	
	private int index=-1;

	/**
	 * @param spin2 
	 * @param ds 
	 * @param arrayList 
	 * @param coeff : molecular orbital coefficients
	 * @param spin : spin of the orbital
	 */
	public SpinOrbitaleMoleculaire(Energy e, double[] coeff, Spin spin) {
		super(e, coeff);
		String val = spin.toString();
		setSpin(val);
	}
	
	/**
	 * Copy constructor
	 * @param that SpinOrbitaleMoleculaire to copy
	 */
	public SpinOrbitaleMoleculaire(SpinOrbitaleMoleculaire that) {
		this(that.getEnergy(), that.getCoeffs(), that.getSpin());
		this.partner=that.partner;
		this.index = that.index;
	}
	

	/**
	 * This function returns a copy of this spin OM
	 * @return 
	 **/
	// @Override
	public Object clone() {
		return new SpinOrbitaleMoleculaire(this);
	}
	
	
	public boolean equals(SpinOrbitaleMoleculaire that) {
		if (this.spin != that.spin)
			return false;
		return super.equals(that);
	}

	public Spin getSpin() {
		return this.spin;
	}

	private void setSpin(String spinStr) {
		this.spin = Spin.valueOf(spinStr);
	}

	public void switchSpin() throws OrbitaleMoleculaireException {
		switch (this.spin) {
			case ALPHA:
				this.setSpin(Spin.BETA.toString());
				break;
			case BETA:
				this.setSpin(Spin.ALPHA.toString());				
				break;
			default:
				throw new OrbitaleMoleculaireException (this.getClass().toString() + "Cannot switch spin of nospin");
		}
	}
	
	public double overlap(SpinOrbitaleMoleculaire that, int[][] topo) {
		Spin a=this.getSpin();
		Spin b=that.getSpin();
//		if (a==b) {HuckelIO.PrintIf("ok");}
		if (getSpin() != that.getSpin())
			return 0;
		double rc=super.overlap(that, topo);
		return rc;
	}
	
	public static void main(String[] args) {
		SpinOrbitaleMoleculaire o = new SpinOrbitaleMoleculaire(null);
		System.out.print(o.toLaTeX());
	}

	public String toHtml() {
		return "\u025B<sub>" + "</sub> = " + this.getEnergy().toString();
	}
	
	/**
	 * @param somA
	 * @param somB 
	 */
	public static void setPartner(SpinOrbitaleMoleculaire somA, SpinOrbitaleMoleculaire somB) {
		somA.partner = somB;
		somB.partner = somA;
	}

	/**
	 * @return the partner
	 */
	public SpinOrbitaleMoleculaire getPartner() {
		return partner;
	}

	public void removeElectron() throws SpinOrbitaleMoleculaireException {
		switch (this.getSpin()) {
			case ALPHA:
				this.setSpin(Spin.NOSPIN_ALPHA.toString());
				break;
			case BETA:
				this.setSpin(Spin.NOSPIN_BETA.toString());
				break;
			default:
				throw new SpinOrbitaleMoleculaireException("cannot remove electron from empty orbital");
		}
	}

	public void addElectron() throws SpinOrbitaleMoleculaireException {
		switch (this.getSpin()) {
			case NOSPIN_ALPHA:
				this.setSpin(Spin.ALPHA.toString());
				break;
			case NOSPIN_BETA:
				this.setSpin(Spin.BETA.toString());
				break;
			default:
				throw new SpinOrbitaleMoleculaireException("cannot add electron to an occupied orbital");
		}
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

}