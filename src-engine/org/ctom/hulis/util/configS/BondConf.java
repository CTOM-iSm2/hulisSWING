package org.ctom.hulis.util.configS;

public class BondConf {

	private int Atom1;
	private int Atom2;

	protected BondConf(int iat, int jat) {
		this.Atom1 = iat;
		this.Atom2 = jat;
	}

	protected int getAtom1() {
		return this.Atom1;
	}

	protected int getAtom2() {
		return this.Atom2;
	}

	protected void setAtom1(int iat) {
		this.Atom1 = iat;
	}

	protected void setAtom2(int iat) {
		this.Atom2 = iat;
	}

}
