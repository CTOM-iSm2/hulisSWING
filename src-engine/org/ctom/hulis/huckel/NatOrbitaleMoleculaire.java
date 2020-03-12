package org.ctom.hulis.huckel;

import java.io.Serializable;


public class NatOrbitaleMoleculaire extends OrbitaleMoleculaire implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9009698140794732497L;
	
	double occ=-1;

	public double getOcc() {
		return occ;
	}

	public void setOcc(double occ) {
		this.occ = occ;
	}

	public NatOrbitaleMoleculaire(double[] coeffs) {
		super(null, coeffs);
	}
	
	public String toHtml() {
	//	DecimalFormat format = new DecimalFormat("#.###");
	//    String output = format.format(this.getOcc());
	//	return output;
		return this.getOcc() + "";
	}
	
}
