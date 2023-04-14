package org.ctom.hulis.huckel;

import java.io.Serializable;
import java.util.ArrayList;

import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.listeners.IBondListener;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Point3D;




/**
 * Bond : this class describe a bond between two atoms
 * 
 * @author Nicolas Goudard
 * 
 * @author Nicolas Goudard, Yannick Carissan
 * @version 1.0
 * @since JDK 1.5
 */

public class Bond implements IMoleculeComponent, Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3015290635133264373L;
	
	protected Atom atom1; // first atom in the bond
	protected Atom atom2; // second atom in the bond
	protected int index; // sequence number of the bond in the molecule
	protected Molecule moleculeParentContainer; // molecule that contains the bond
	protected final ArrayList<IBondListener> listeners;

	

	
	/**
	 * This constructor creates a bond between two atoms (in parameter)
	 * @param atom1 : first atom
	 * @param atom2 : second atom
	 * @throws BondException if atoms are null, or point on the same object, or are not in the same molecule
	 */
	public Bond(Atom atom1, Atom atom2) throws BondException {
		checkCoherence(atom1, atom2);
			
		this.atom1 = atom1;
		this.atom2 = atom2;
		this.listeners = new ArrayList<IBondListener>();

	}
	
	/**
	 * 
	 * @param atom1
	 * @param atom2
	 * @throws BondException 
	 */
	protected void checkCoherence(Atom atom1, Atom atom2) throws BondException
	{
		if ((atom1 == null) || (atom2 == null))
			throw new BondException("atom must be intialized");
		if ((atom1 == atom2))
			throw new BondException("atom must be different");
		if ((atom1 == null) || (atom2 == null))
			throw new BondException("atom must be in a molecule");
		if (atom1.getMoleculeContainer() != atom2.getMoleculeContainer())
			throw new BondException("atoms are not in the same molecule");
	
	}
	
	
	
	/**
	 * Copy Constructor
	 * 
	 * @param bond to copy. This method does not copy the molecule parent
	 */
	public Bond(Bond bond) {
		this.atom1 = bond.getAtom1();
		this.atom2 = bond.getAtom2();
		this.setIndex (bond.getIndex());
		this.listeners = new ArrayList<IBondListener>();
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return new Bond(this);

	}

	/**
	 * This function tests if the bond contains the atom specified in parameter
	 * 
	 * @param a :
	 *            atom to be tested
	 * @return boolean
	 */
	public boolean contains(Atom a) {
		return ((this.atom1 == a) || (this.atom2 == a));
	}

	/**
	 * This function tests if the atoms are linked
	 * 
	 * @param atom1 : first atom
	 * @param atom2 : second atom
	 * @return true if the atoms are linked
	 */
	public boolean contains(Atom atom1, Atom atom2) {
		if ((atom1 == null) || (atom2 == null))
			return false;

		return ((this.atom1 == atom1) && (this.atom2 == atom2)) ||  ((this.atom1 == atom2) && (this.atom2 == atom1));
	}

	/**
	 * This function tests if a bond contains an atom of the periodic element specified in
	 * parameter
	 * 
	 * @param a :
	 *            periodic element to test
	 * @return true if the first or the second atom in the bond is of the specified element
	 */

	public boolean contains(PeriodicTable.Element element) {
		return ((this.atom1.getElement() == element) || (this.atom2
				.getElement() == element));
	}

	/**
	 *  This method displays the bond properties in the standard output
	 */
	public void debug() {
		String res = "";

		res += "BOND : \n";
		if (getMoleculeContainer() != null)
			res += "molecule hascode = " + +getMoleculeContainer().hashCode()
			+ "\n";
		res += "seqNum : " + this.index + "\n";
		res += "atom1 :     " + this.atom1.toString() + "\n";
		;
		res += "atom2 :     " + this.atom2.toString() + "\n";
		// res += "type " + bondType ;
		HuckelIO.PrintIf(res);
	}

	/**
	 * This method deletes the bond in the molecule
	 * @throws MoleculeBondExistsException 
	 * @throws MoleculeCoherenceException 
	 * @throws MoleculeTooManyNeighboursException 
	 * @throws BondException 
	 * @throws HuckelBondException 
	 */
	public void remove() throws MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException, HuckelBondException, BondException{
	
		if (getMoleculeContainer() != null)
			getMoleculeContainer().removeBond(this);

	}

	/**
	 * This function compares this bond to another
	 * 
	 * @param b : bond to compare
	 * @return true if the bond has the same properties : index, atoms are equals
	 *  else it returns false
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Bond))
			throw new ClassCastException();

		Bond b = (Bond) o;
		/*
		 * if (! (o instanceof Bond)) return false; Bond b = (Bond) o;
		 */
		return ( /*(b.index == this.index)
				&&*/ ( (b.atom1.equals(this.atom1) && b.atom2.equals(this.atom2)) || 
				(b.atom1.equals(this.atom2) && b.atom2.equals(this.atom1)) )
				);

	}

	protected volatile int hashCode = 0;
	 
	@Override
    public int hashCode () {
        final int multiplier = 23;
        if (hashCode == 0) {
            int code = 133;
            code = multiplier * code + this.index;
            code = multiplier * code + this.atom1.hashCode();
            code = multiplier * code +this.atom2.hashCode();
            hashCode = code;
        }
        return hashCode;
	
	}
	
	
	/**
	 * Thus function return the atom in the molecule that is of the element specified in the parameter.
	 * if the both atoms are of the same element, the function returns the first atom
	 * @param element
	 * @return first atom corresponding to the specified element else returns null
	 */
	public Atom getAtom(PeriodicTable.Element element) {
		if (this.atom1.getElement() == element)
			return this.atom1;
		if (this.atom2.getElement() == element)
			return this.atom2;
		return null;
	}

	/**
	 * This function returns the first atom in the bond
	 * 
	 * @return Atom object
	 */

	public Atom getAtom1() {
		return this.atom1;
	}

	/**
	 * This function returns the second atom in the bond
	 * 
	 * @return Atom object
	 */

	public Atom getAtom2() {
		return this.atom2;
	}

	/**
	 * This function returns the atom linked to the atom specified in parameter
	 * For example in a bond A-B , getAtomLinkedWith(A) returns the atom B
	 * 
	 * @return Atom object
	 */
	public Atom getAtomLinkedWith(Atom a) {
		if (this.atom1 == a)
			return this.atom2;
		if (this.atom2 == a)
			return this.atom1;
		return null;
	}

	/**
	 * This function returns the sequence number of an Bond object in a Molecule
	 * object
	 * 
	 * @return int
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * This function gets the distance between the center of atoms
	 */
	
	public Double getLength() {
		return this.getAtom1().getDistance(getAtom2());
	}


	/**
	 * This function returns the molecule that contains this bond
	 * 
	 * @return molecule
	 */
	public Molecule getMoleculeContainer() {
		return this.moleculeParentContainer;
	}
	
	/**
	 * This function returns the center coordinates of the bond between the both atoms that
	 * compose it
	 * 
	 * @return Point3D : x, y, zcoordinates
	 */

	
	public Point3D getLocation() {
		return new Point3D(getX(), getY(), getZ());
	}

	
	public ArrayList<IBondListener> getListeners() {
		
		return ( ArrayList<IBondListener>)this.listeners.clone();
	}
	
	
	/**
	 * This function returns the abscisse center of the bond between the both atoms
	 * that compose it
	 * 
	 * @return x coordinate
	 */

	public double getX() {
		return (this.atom1.getX() + this.atom2.getX()) / 2;
	}


	/**
	 * This function returns the ordinate center of the bond between the both atoms
	 * that compose it
	 * 
	 * @return y coordinate
	 */

	public double getY() {
		return  (this.atom1.getY() + this.atom2.getY()  ) / 2;
	}

	/**
	 * This function returns the z coordinate center of the bond between the both atoms
	 * that compose it
	 * 
	 * @return y coordinate
	 */

	public double getZ() {
		return  (this.atom1.getZ() + this.atom2.getZ()  ) / 2;
	}

	
	/**
	 * This method replace an atom by another
	 * @param old : atom to replace
	 * @param newAtom : new atom
	 * @throws BondException if atom to replace is not in the molecule and atoms in parameter are not in the same molecule
	 */
	protected void replaceAtom(Atom old, Atom newAtom) throws BondException {

		if (old.getMoleculeContainer() != newAtom.getMoleculeContainer())
			throw new BondException("both atoms must be in the same molecule");
		if (old.getMoleculeContainer() != getMoleculeContainer())
			throw new BondException(
			"atom must be in the same molecule as he bond");

		if (getAtom1() == old)
			this.atom1 = newAtom;
		else if (getAtom2() == old)
			this.atom2 = newAtom;
		else
			throw new BondException("atom does not exist");

	}

	/**
	 * This method changes the sequence number of the bond in the molecule
	 * 
	 * @param index : new index
	 */

	protected void setIndex(int index) {
		this.index = index;
	}

	/**
	 * This method sets the molecule that contain this bond
	 * @param moleculeParentContainer : molecule parent
	 */
	protected void setMoleculeContainer(Molecule moleculeParentContainer) {
		this.moleculeParentContainer = moleculeParentContainer;
	}

	/**
	 * This function returns a string representation of this bond
	 * @return string
	 */
	@Override
	public String toString() {
		String res = "";
		res += getAtom1().toString() + "-";
		res += getAtom2().toString();
		return res;
	}

	public void addListener(IBondListener listener) {
		this.listeners.add(listener);
	}
	public void removeListener(IBondListener listener) {
		this.listeners.remove(listener);
	}

	public double getOptimalValue() {
		return this.atom1.getCovalentRadius() + this.atom2.getCovalentRadius();
	}
	
	

}