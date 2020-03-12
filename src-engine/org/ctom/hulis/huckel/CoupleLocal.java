package org.ctom.hulis.huckel;

import java.io.Serializable;

import org.ctom.hulis.huckel.exception.CoupleException;

/**
 * This class describe a couple between two Huckel atoms
 * 
 * @author Nicolas Goudard
 * 
 * @author Nicolas Goudard, Yannick Carissan
 * @version 1.0
 * @since JDK 1.5
 */

public class CoupleLocal extends Couple implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7357221910006124200L;

	/**
	 * Constructor by copy
	 * @param couple
	 */
	public CoupleLocal(CoupleLocal couple) {
		super(couple);
	}

	/**
	 * Constructor : creates a couple between two Huckel atoms
	 * @param a1 : first Huckel atom
	 * @param a2 : second Huckel atom
	 * @throws CoupleException :if atoms are the same or null
	 */
	public CoupleLocal(HuckelAtom a1, HuckelAtom a2) throws CoupleException {
		super((Object) a1,(Object) a2);
		if ((a1.getNbRadR() != 1) || (a2.getNbRadR() != 1))
			throw new CoupleException("number of rad R is incorrect for couple");
		this.setObj1(a1);
		this.setObj2(a2);
	}

	/**
	 * This method returns a copy of this couple
	 * @return a copy of this couple
	 */
	@Override
	public Object clone() {
		return new CoupleLocal(this);
	}
	
	/**
	 * This function tests if the both atoms are contained in the couple
	 * @param a1 : first atom to test
	 * @param a2 : second atom to test
	 * @return true if the couple contains the both atom.
	 */
	public boolean contains(Object obj1, Object obj2) {
		if (!(obj1 instanceof HuckelAtom)) return false;
		if (!(obj2 instanceof HuckelAtom)) return false;
		HuckelAtom Atom1 = (HuckelAtom) this.getObj1();
		HuckelAtom Atom2 = (HuckelAtom) this.getObj2();
		HuckelAtom Atom3 = (HuckelAtom) obj1;
		HuckelAtom Atom4 = (HuckelAtom) obj1;
		if (((Atom1 == Atom3) && (Atom2 == Atom4))
				|| ((Atom1 == Atom4) && (Atom2 == Atom3)))
			return true;
		else
			return false;
	}

	/**
	 * This function tests if two couples are equals 
	 * @param c : another couple
	 * @return true if the both atoms in the couple are equals to the both atoms in the other couple
	 */
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Couple))
			throw new ClassCastException();

		Couple c = (Couple) o;
		if (!(c instanceof CoupleLocal)) return false;
		CoupleLocal cLocal = (CoupleLocal) c;
		return ((getHuckelAtom1().equals(cLocal.getHuckelAtom1()) && getHuckelAtom2().equals(
				cLocal.getHuckelAtom2())) || (getHuckelAtom1().equals(cLocal.getHuckelAtom2()) && getHuckelAtom2()
				.equals(cLocal.getHuckelAtom1())));
	}

	private volatile int hashCode = 0;
	 
	@Override
    public int hashCode () {
        final int multiplier = 23;
        if (hashCode == 0) {
            int code = 133;
            code = multiplier * code + this.getHuckelAtom1().hashCode();
            code = multiplier * code +this.getHuckelAtom2().hashCode();
            hashCode = code;
        }
        return hashCode;
	
	}
	
	/**
	 * This function returns the first atom in the couple
	 * @return an atom
	 */
	public HuckelAtom getHuckelAtom1() {
		return (HuckelAtom)this.getObj1();
	}

	/**
	 * This function returns the second atom in the couple
	 * @return an atom
	 */
	public HuckelAtom getHuckelAtom2() {
		return (HuckelAtom)this.getObj2();
	}

	/**
	 * This function returns a string representation of the couple
	 * @return string
	 */
	@Override
	public String toString() {
		return "(" + this.getHuckelAtom1() + ", " + this.getHuckelAtom2() + ")";
	}

}