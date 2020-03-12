package org.ctom.hulis.huckel;

import java.io.Serializable;



public class OrbitaleAtomique extends Orbitale implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3710447215023881088L;

	Atom atom;
	
	/**
	 * @return the atom
	 */
	public Atom getAtom() {
		return this.atom;
	}

	/**
	 * @return the atom
	 */
	public HuckelAtom getHuckelAtom() {
		if (this.atom instanceof HuckelAtom) return (HuckelAtom)this.atom;
		return null;
	}

	public OrbitaleAtomique(Atom a, Energy e) {
		super(e);
		this.atom=a;
	}

	public double computeNorm() {
		return 0;
	}

	public static void main(String[] args) {
		OrbitaleAtomique oa = new OrbitaleAtomique((Atom)null, (Energy)null);
		System.out.print(oa.toLaTeX());
	}

}
