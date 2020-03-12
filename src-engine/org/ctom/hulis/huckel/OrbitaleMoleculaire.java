package org.ctom.hulis.huckel;

import java.io.Serializable;




public class OrbitaleMoleculaire extends Orbitale implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3617696344037923987L;
	
	private double[] LCAO;
	private int size=-1;

	public OrbitaleMoleculaire(Energy e, double[] coeffs) {
		super(e);
		size=coeffs.length;
		this.LCAO = new double[size];
		for (int i=0; i<size; i++) {
			this.LCAO[i] = coeffs[i];
		}
	}

	public double[] getCoeffs() {
		return LCAO;
	}

	public double getCoeff(int i) {
		return LCAO[i];
	}

	public int getSize() {
		return size;
	}

	/**
	 * @param that : the molecular spin orbital to overlap with
	 * @param topo_ : matrix of atomic orbitals overlap
	 * @return overlap of the current object with the input spin orbital
	 */
	public double overlap(OrbitaleMoleculaire that, int[][] topo_) {

		double ovl = 0;
		for (int i = 0; i < size; i++) {
			ovl += LCAO[i] * that.LCAO[i];
		}
		return ovl;
	}

	// overlap for huckel with overlap matrix modified
	//
	//	public double overlap(OrbitaleMoleculaire that, int[][] topo_) {
	//
	//		double ovl = 0;
	//		double value = 0;
	//		final int topo_size = topo_.length;
	//		double[][] topo = new double[topo_size][topo_size];
	//		for (int i = 0; i < topo_size; i++) {
	//			for (int j = 0; j < topo_size; j++) {
	//				topo[i][j] = topo_[i][j];
	//			}
	//		}
	//		Matrix Smat = new Matrix(topo);
	//		Matrix Ident = Matrix.identity(topo_size, topo_size);
	//		Smat = Smat.times(value);
	//		Smat = Smat.plus(Ident);
	//
	//		for (int i = 0; i < size; i++) {
	//			for (int j = 0; j < size; j++) {
	//				ovl += getCoeffs()[i] * that.getCoeffs()[j] * Smat.get(i, j);
	//			}
	//		}
	//		return ovl;
	//	}

	public boolean equals(OrbitaleMoleculaire that) {
		return this.LCAO==that.LCAO;

	}

	public double computeNorm() {
		//TODO : ajouter un argument par defaut (matrice identite)
		return (Double)null;
	}

	public static void main(String[] args) {
		OrbitaleMoleculaire o = new OrbitaleMoleculaire(null, null);
		System.out.print(o.toLaTeX());
	}

	public String toHtml() {
		return "TO OVERRIDE";
	}

}