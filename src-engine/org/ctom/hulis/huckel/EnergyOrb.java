package org.ctom.hulis.huckel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ctom.hulis.huckel.structures.Structure;

/**
 * This class describe a level energy of the structure and the orbitals on each Huckel atom relative to this energy level
 * @author ngoudard
 * @see Structure
 *
 */
public class EnergyOrb implements Comparable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5082805417736492209L;

	/**
	 * This class describe an orbital around a Huckel atom
	 * @author ngoudard
	 *
	 */
	public class Orbital {
		protected HuckelAtom a; // atom relative to the orbital
		protected double coeff; // orbital coefficient (radius)
		protected EnergyOrb energyOrb; // orbital energy.

		/**
		 * Constructor
		 * @param energyOrb : orbital energy
		 * @param a : atom 
		 * @param coeff : orbital radius
		 */
		public Orbital(EnergyOrb energyOrb, HuckelAtom a, double coeff) {
			this.energyOrb = energyOrb;
			this.a = a;
			this.coeff = coeff;
		}

		/** This function gets the orbital coefficient 
		 * 
		 * @return double
		 */
		public double getCoeff() {
			return this.coeff;
		}

		/** This function gets the atom
		 *
		 * @return
		 */
		public HuckelAtom getHuckelAtom() {
			return this.a;
		}

	} // Orbital class

	public static final String EPSILON_SYMBOL = "\u025B"; // energy symbol constant
	protected int index; // index of the level energy
	protected int nbElecPi; // number  of pi electron on the energy level
	protected ArrayList<Orbital> orbitals; // list of orbital relative to this energy level
	protected Energy value;
	
	/**
	 * Constructor
	 * @param value : energy level
	 * @param nbElecPi : number of pi electron on this energy level
	 */
	public EnergyOrb(Energy value, int nbElecPi) {
		this.orbitals = new ArrayList<Orbital>();
		this.value = value;
		this.nbElecPi = nbElecPi;
	}

	/**
	 * This method clears the list of orbitals for this energy level
	 */
	public void clear() {

		if (this.orbitals != null)
			for (Orbital orb : this.orbitals) {
				// orbitals.remove(orb);
				orb = null;
			}

		this.orbitals.clear();
	}

	/**
	 * This function compares an energy level to another by the energy
	 * @param another energy level
	 * @return -1, 0 or 1
	 */
	public int compareTo(Object o) {
		if (!(o instanceof EnergyOrb))
			throw new ClassCastException();
		return this.value.compareTo(((EnergyOrb) o).value);

	}
	
	/**
	 * This methods create an orbital for this energy level
	 * @param a : atom relative to the orbital
	 * @param coeff : double, orbital coefficient (radius)
	 */
	public void createOrbital(HuckelAtom a, double coeff) {
		this.orbitals.add(new Orbital(this, a, coeff));
	}

	/**
	 * returns the index of the energy level
	 * @return int
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * returns the number of pi electrons on this energy level
	 * @return int
	 */
	public int getNbElecPi() {
		return this.nbElecPi;
	}

	/**
	 * returns the orbital to the Huckel atom specified in parameter
	 * @param a : Huckel atom
	 * @return : orbital relative to the atom specified in parameter
	 */
	public Orbital getOrbital(HuckelAtom a) {
		Orbital oFound = null;
		for (Orbital o : this.orbitals) {
			if (o.getHuckelAtom() == a) {
				oFound = o;
			}
		}

		return oFound;
	}

	
	/** 
	 * returns the orbital list relative to this energy level
	 * @return orbital list
	 */
	public List<Orbital> getOrbitals() {
		return this.orbitals;
	}

	/**
	 * Return the energy value relative to this energy level
	 * @return energy
	 */
	public Energy getValue() {
		return this.value;
	}

	/** 
	 * This method returns the index of this energy level
	 * @param index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * This function returns a HTML representation of this energy level.
	 * It is used in tooltip text
	 * @return string
	 */
	public String toHtml() {
		return "\u025B<sub>" + this.index + "</sub> = " + this.value.toString();
	}

	/**
	* This function returns a string representation of this energy level.
	**/
	@Override
	public String toString() {
		return "\u025B" + this.index + " = " + this.value.toString();
	}

}
