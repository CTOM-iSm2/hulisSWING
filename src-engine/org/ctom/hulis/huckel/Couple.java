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

public abstract class Couple implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6703305405208198930L;
	
	private Object obj1; // first object coupled
	private Object obj2; // second object coupled
	/**
	 * @return the obj1
	 */
	public Object getObj1() {
		return this.obj1;
	}

	/**
	 * @return the obj2
	 */
	public Object getObj2() {
		return this.obj2;
	}

	/**
	 * @param obj1 the obj1 to set
	 */
	public void setObj1(Object obj1) {
		this.obj1 = obj1;
	}

	/**
	 * @param obj2 the obj2 to set
	 */
	public void setObj2(Object obj2) {
		this.obj2 = obj2;
	}


	/**
	 * Constructor by copy
	 * @param couple
	 */
	public Couple(Couple couple) {
		this.obj1 = couple.getObj1();
		this.obj2 = couple.getObj2();
	}

	/**
	 * Constructor : creates a couple between two Huckel atoms
	 * @param a1 : first Huckel atom
	 * @param a2 : second Huckel atom
	 * @throws CoupleException : if atoms are the same or null
	 */
	public Couple(Object obj1, Object obj2) throws CoupleException {
		if ((obj1 == null) || (obj2 == null))
			throw new CoupleException("object null is forbidden for couple");
		if ((obj1 == obj2))
			throw new CoupleException("objects to couple must be different");
	}

		
	/**
	 * This function tests if the both atoms are contained in the couple
	 * @param a1 : first atom to test
	 * @param a2 : second atom to test
	 * @return true if the couple contains the both atom.
	 */
	public abstract boolean contains(Object obj1, Object obj2);
	
	/**
	 * This function tests if two couples are equals 
	 * @param c : another couple
	 * @return true if the both atoms in the couple are equals to the both atoms in the other couple
	 */
	public abstract boolean equals(Object o);

	

	
	/**
	 * This function returns a string representation of the couple
	 * @return string
	 */
	@Override
	public String toString() {
		return "(" + this.getObj1() + ", " + this.getObj2() + ")";
	}

}