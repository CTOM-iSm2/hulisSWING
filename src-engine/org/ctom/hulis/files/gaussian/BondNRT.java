package org.ctom.hulis.files.gaussian;


public class BondNRT implements Comparable {
	int noAtom1;
	int noAtom2;
	String type;

	protected BondNRT(int noAtom1, int noAtom2, String type) {
		this.noAtom1 = noAtom1;
		this.noAtom2 = noAtom2;
		this.type = type;
	}

	public int compareTo(Object o) {
		if (!(o instanceof BondNRT))
			throw new ClassCastException();
		BondNRT bondNRT = (BondNRT) o;
		if (!bondNRT.type.equals(this.type))
			return new String(this.type).compareTo(bondNRT.type);
		else if (bondNRT.noAtom1 != this.noAtom1)
			return new Integer(this.noAtom1).compareTo(bondNRT.noAtom1);
		else
			return new Integer(this.noAtom2).compareTo(bondNRT.noAtom2);
	}

	@Override
	public String toString() {
		return this.type + " " + this.noAtom1 + " " + this.noAtom2;
	}

}
