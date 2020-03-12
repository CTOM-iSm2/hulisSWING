/**
 * 
 * This class describes a connection that not exists in the Molecule
 * A X-ME exists but not a X-CH3 because CH3 is virtual 
 * 
 */
package org.ctom.hulis.util.geometry;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.HuckelBondException;

/**
 * @author macbook
 *
 */
public class VirtualBond extends Bond {

	int bondType;
	private Atom substitutedME;
	
	/**
	 * @param neighbour
	 * @param c
	 * @param substitutedME
	 * @param bondType
	 * @throws BondException
	 * @throws HuckelBondException
	 */
	public VirtualBond(Atom neighbour, Atom c, Atom substitutedME, int bondType)
			throws BondException, HuckelBondException {
		super(neighbour, c);

		if (c.getElement() != PeriodicTable.Element.C)
			throw new BondException("atom type C expected in parameter");

		if (substitutedME.getElement() != PeriodicTable.Element.Me)
			throw new BondException("substitutedME is not a ME");

		if (substitutedME.getSuperSymbol() != c.getSuperSymbol())
			throw new BondException("substitutedME and C have not the same super symbol");
		
		this.substitutedME =substitutedME;
		this.bondType = bondType;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getBondType() {
		return this.bondType;
	}

	
	public Atom getSubistutuedME() {
		return this.substitutedME;
	}
	

	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Bond#checkCoherence(org.ctom.hulis.huckel.Atom, org.ctom.hulis.huckel.Atom)
	 */
	@Override
	protected void checkCoherence(Atom atom1, Atom atom2) throws BondException
	{
		/*
		 * A la difference de la methode parente, cette methode heritee
		 * ne verifie pas que les deux atomes appartiennent a la meme molecule
		 * puisque le groupe VirtualMethyl est "virtuel"
		 */
		if ((atom1 == null) || (atom2 == null))
			throw new BondException("atom must be intialized");
		if ((atom1 == atom2))
			throw new BondException("atom must be different");
		if ((atom1 == null) || (atom2 == null))
			throw new BondException("atom must be in a molecule");
	
	}
		
	
	public void setBondType(int bondType) {
		this.bondType = bondType;
	}
	
}
