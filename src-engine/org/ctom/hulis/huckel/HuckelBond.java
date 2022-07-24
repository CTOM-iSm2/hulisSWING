package org.ctom.hulis.huckel;

import java.io.Serializable;

import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.listeners.IBondListener;
import org.ctom.hulis.huckel.listeners.IHuckelBondListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;

/**
 * This class describes a Huckel bond
 * @author ngoudard
 *
 */
public class HuckelBond extends Bond implements IHuckelObject, Cloneable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2118885093223851933L;

	public static final int MAX_TYPE_BOND = 2; // Maximal bond type : 1 : single, 2 : double bond

	/**
	 * Interation table between Huckel atoms. each value is the hXY value, Huckel parameter 
	 * WARNING : ATOMS MUST BE INDEXED LIKE IN PeriodicTable entry list
	 * @see  
	 */
	public static double[][] tblHxy = {
			/*       C B N1 N2 O1 O2 F Si P1 P2 S1 S2 Cl ME X0 X1 X2 */
			/* C */{ 1.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00,
					0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* B */{ 0.73, 0.87, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00,
					0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* N1 */{ 1.02, 0.66, 1.09, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00,
					0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* N2 */{ 0.89, 0.53, 0.99, 0.98, 0.00, 0.00, 0.00, 0.00, 0.00,
					0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* O1 */{ 1.06, 0.60, 1.14, 1.13, 1.26, 0.00, 0.00, 0.00, 0.00,
					0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* O2 */{ 0.66, 0.35, 0.80, 0.89, 1.02, 0.95, 0.00, 0.00, 0.00,
					0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* F */{ 0.52, 0.26, 0.65, 0.77, 0.92, 0.94, 1.04, 0.00, 0.00,
					0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* Si */{ 0.75, 0.57, 0.72, 0.43, 0.65, 0.24, 0.17, 0.64, 0.00,
					0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* P1 */{ 0.77, 0.53, 0.78, 0.55, 0.75, 0.31, 0.21, 0.62, 0.63,
					0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* P2 */{ 0.76, 0.54, 0.81, 0.64, 0.82, 0.39, 0.22, 0.52, 0.58,
					0.62, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* S1 */{ 0.81, 0.51, 0.83, 0.68, 0.84, 0.43, 0.28, 0.61, 0.65,
					0.65, 0.66, 0.00, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* S2 */{ 0.69, 0.44, 0.78, 0.73, 0.85, 0.54, 0.32, 0.40, 0.48,
					0.60, 0.58, 0.63, 0.00, 0.00, 0.00, 0.00, 0.00 },
			/* Cl */{ 0.62, 0.41, 0.77, 0.80, 0.88, 0.70, 0.51, 0.34, 0.35,
					0.55, 0.52, 0.59, 0.68, 0.00, 0.00, 0.00, 0.00 },
			/* ME */{ 0.707, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00,
					1.00, 1.00, 1.00, 1.00, 1.00, 0.00, 0.00, 0.00 },
			/* X0 */{ 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00,
					1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 0.00, 0.00 },
			/* X1 */{ 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00,
					1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 0.00 },
			/* X2 */{ 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00,
					1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00, 1.00 } };

	/**
	 * This function returns the hXY value (Huckel bond parameter) relative to the atoms entries specified 
	 * in parameter
	 * 
	 * @param first entry
	 * @param second entry
	 * @return double
	 * @throws HuckelBondException if a parameter is null
	 */
	public static double getHxy(PeriodicTable.Entry entry1,
			PeriodicTable.Entry entry2) throws HuckelBondException {
		if ((entry1 == null) || (entry2 == null))
			throw new HuckelBondException("atom must be initalized");
		HuckelBond.initHxy();
		int index1 = HuckelBond.getIndex(entry1);
		int index2 = HuckelBond.getIndex(entry2);
		return HuckelBond.tblHxy[index1][index2];
	}

	/**
	 * This function returns the index of an periodic table entry to use it in the hXY table
	 * @param entry
	 * @return index
	 */
	private static int getIndex(PeriodicTable.Entry entry) {
		int i = 0;
		for (PeriodicTable.Entry a : PeriodicTable.Entry.values()) {
			if (a == entry)
				break;
			i++;
		}
		return i;
	}

	/**
	 * This method creates the hXY table symetry 
	 */
	private static void initHxy() {
		int i;
		int j;

		for (i = 0; i < HuckelBond.tblHxy.length; i++) {
			for (j = 0; j < HuckelBond.tblHxy[i].length; j++) {
				if (HuckelBond.tblHxy[i][j] == 0.00) {
					HuckelBond.tblHxy[i][j] = HuckelBond.tblHxy[j][i];
				}
			}
		}

	}

	/* Calculated value */
	protected double bondOrder; // bond order calculated
	
	/* type of bond setby the user */
	protected int bondType; // type of the bond : possible values 1=single 2=double 3=triple
	public BlocDelocalized blocDelocalizedParent; // null if not in a delocalized bloc
	
	
	protected double hxy; // hXY : org.ctom.hulis.huckel parameter automatically set when the bond is constructed

	
	/**
	 * This constructor create a bond between two atoms and automatically sets the org.ctom.hulis.huckel parameter to it
	 * @param atom1 : first atom to link
	 * @param atom2 : second atom to link
	 * @throws HuckelBondException if atoms are null, or point on the same object, or are not in the same molecule
	 * 
	 */
	public HuckelBond(HuckelAtom atom1, HuckelAtom atom2) throws BondException, HuckelBondException {
		super(atom1, atom2);
		this.bondType = 1;
		this.hxy = 0.0;
		if ((atom1.getEntry() != null) &&  (atom2.getEntry() != null))
			this.hxy = HuckelBond.getHxy(atom1.getEntry(), atom2.getEntry());
		this.blocDelocalizedParent = null;
	}

	/**
	 * This constructor create a bond between two atoms with a specified bond type and sets
	 * automatically the org.ctom.hulis.huckel parameter to it
	 * @param atom1 : first atom to link
	 * @param atom2 : second atom to link
	 * @param bond type : 1 = single, 2 = double bond
	 * @throws HuckelBondException if atoms are null, or point on the same object, or are not in the same molecule
	 * 
	 */
	public HuckelBond(HuckelAtom atom1, HuckelAtom atom2, int bondType)
			throws BondException, HuckelBondException {
		super(atom1, atom2);

		this.bondType = bondType;
		this.hxy = 0.0;
		if ((atom1.getEntry() != null) &&  (atom2.getEntry() != null))
			this.hxy = HuckelBond.getHxy(atom1.getEntry(), atom2.getEntry());
		this.blocDelocalizedParent = null;
		checkCoherence();
	}

	/**
	 * Copy Constructor
	 * 
	 * @param bond : bond to copy
	 * @throws HuckelBondException
	 */
	public HuckelBond(HuckelBond bond) {
		super(bond);
		this.bondOrder = bond.bondOrder;
		this.bondType = bond.bondType;
		this.hxy = bond.hxy;
		this.blocDelocalizedParent = null;
	}
	

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Bond#clone()
	 */
	public Object clone() {
		return new HuckelBond(this);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Bond#hashCode()
	 */
	@Override
    public int hashCode () {
        final int multiplier = 23;
        if (hashCode == 0) {
            int code = 133;
            code = multiplier * code + super.hashCode();
            code = multiplier * code +  this.bondType;
            code = multiplier * code + new Double(this.hxy).hashCode() ;
            hashCode = code;
        }
        return hashCode;
	
	}

	/**
	 * This function tests if this bond is equal to another
	 * @return true or false
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof HuckelBond))
			return false;
		HuckelBond hB = (HuckelBond)o;
		return (super.equals(hB) && (hB.bondType == this.bondType) && (hB.hxy == this.hxy));

	}


	
	/**
	 * 
	 * @throws HuckelBondException
	 */
	public void checkCoherence() throws HuckelBondException {
		
		if ((bondType < 1) || (bondType > HuckelBond.MAX_TYPE_BOND))
			throw new HuckelBondException("set unavailable valye type of bond : " + bondType + " for " + this);

		else if ((bondType > 1 ) && (this.blocDelocalizedParent != null) || (this.getAtom1().getBlocDelocalizedParent()!= null) || (this.getAtom2().getBlocDelocalizedParent()!= null) ) 
			throw new HuckelBondException("Cannot change bond type : the bond " + this + " owns to a  delocalized block");

	}
	/**
	 * 
	 */
	public BlocDelocalized getBlocDelocalizedParent() {
		return blocDelocalizedParent;
	}

	/**
	 * 
	 */
	public void setBlocDelocalizedParent(BlocDelocalized blocDelocalizedParent) {
		this.blocDelocalizedParent = blocDelocalizedParent;
	}


	/**
	 * This function returns the first linked Huckel atom
	 * @return org.ctom.hulis.huckel atom 
	 */
	@Override
	public HuckelAtom getAtom1() {
		return (HuckelAtom) this.atom1;
	}

	/**
	 * This function returns the second linked Huckel atom 
	 * @return org.ctom.hulis.huckel atom 
	 */
	@Override
	public HuckelAtom getAtom2() {
		return (HuckelAtom) this.atom2;
	}

	/**
	 * 
	 * @return the calculated bond order
	 */
	public double getBondOrder() {
		return this.bondOrder;
	}

	/**
	 * @return the bond type
	 */

	public int getBondType() {
		return this.bondType;
	}



	public int getNbRadR(){
		if ( getBondType() == 2) return 2;
		else return 0;
	}

	/**
	 * 
	 * @return the calculated bond order
	 */
	public double getDensity() {
		return this.bondOrder;
	}

	/**
	 * This function returns hXY, Huckel parameter
	 * 
	 * @return double
	 */

	public double getHxy() {
		return this.hxy;
	}

	/**
	 * This function returns the name of org.ctom.hulis.huckel parameter
	 */
	public String getNameParamHuckel() {
		return "hXY";
	}

	/**
	 * This function returns the name of the Huckel parameter in an HTML format.
	 * It is used in tooltip text
	 * @return string
	 */
	public String getNameParamHuckelHTML() {
		return "h<sub>XY</sub>";
	}
	
	/**
	 * This function returns the name of the Huckel parameter
	 * @return string
	 */
	public double getParamHuckel() {
		return this.hxy;
	}

	/**
	 * This function replace an atom by another
	 * @param atom to replace
	 * @param new atom
	 * @throws BondException 
	 */
	@Override
	protected void replaceAtom(Atom old, Atom newAtom) throws BondException {
		super.replaceAtom(old, newAtom);
		if ((old instanceof HuckelAtom) && (newAtom instanceof HuckelAtom) && (this.atom1 != null) && (this.atom2!= null) && (this.atom1.getEntry() != null) && (this.atom2.getEntry() != null))
			this.hxy = HuckelBond.getHxy(((HuckelAtom)this.atom1).getEntry(), ((HuckelAtom)this.atom2).getEntry());
	}

	/**
	 * This method sets the bond order (electron density) 
	 * @param bondOrder
	 */
	public void setBondOrder(double bondOrder) {
		this.bondOrder = bondOrder;
	}

	/**
	 * This method sets the type of the bond
	 * 
	 * @param bondType : bond type : 1 (single) or 2 (double)
	 * @throws HuckelBondException : if incorrect type of bond (negative or zero or greater than MAX_TYPE_BOND)
	 *            or if the bond owns to a delocalized block or id the bond owns to a delocalized structure
	 */
	public void setBondType(int bondType) throws HuckelBondException {
		if (this.moleculeParentContainer instanceof StructureDelocalized)
			throw new HuckelBondException("Cannot change bond type delocalized structure");

		
		if ((bondType < 1) || (bondType > HuckelBond.MAX_TYPE_BOND))
			throw new HuckelBondException("incorrect type of bond");

		int old = getBondType();
		do_setBondType(bondType);
		
		checkCoherence();
		
		if (old != this.bondType)
			this.fireBondTypeChanged(this, old,
					getBondType());

	}
	
	
	protected void do_setBondType(int bondType){
		this.bondType = bondType;
		this.moleculeParentContainer.updateCharges();


	}

	/**
	 * This method sets a hXY value
	 * 
	 * @param hXY value to set
	 * @throws HuckelBondException : if incorrect value
	 */
	public void setHxy(double hxy) throws HuckelBondException {
		if (this.moleculeParentContainer instanceof StructureLocalized) 
			throw new HuckelBondException("Cannot set Hxy on a localized structure");
		 if  ( (this.moleculeParentContainer instanceof Structure) && ( ((Structure) this.moleculeParentContainer).getMesomeryParent() != null) && (((Structure) this.moleculeParentContainer).getMesomeryParent() .countLocalizedStructs() >0))
				throw new  HuckelBondException("Cannot set Hxy. There are localized structures in the mesomery");
	
		if (hxy == this.hxy)
			return;

		double old = this.getHxy();
		this.hxy = hxy;
		if (this.hxy != old)
			this.fireHxyChanged(this, old,
					this.getHxy());

	}

	/**
	 * This function returns a string representation of this Huckel bond
	 * @return string
	 */
	@Override
	public String toString() {
		String res = "";
		res += getAtom1().toString();
		if (getBondType() == 1)
			res += "-";
		else if (getBondType() == 2)
			res += "=";
		else
			res += "-(*" + getBondType() + ")-";
		res += getAtom2().toString();
		return res;
	}

	protected void fireHxyChanged(HuckelBond source, Object oldValue, Object newValue) {
		HuckelBondEvent event = null;
		
		for (IBondListener listener : this.getListeners()) {
			if (event == null)
				event = new HuckelBondEvent(source, oldValue, newValue);
			((IHuckelBondListener)listener).huckelBondHxyChanged(event);
		}	
	}

	protected void fireBondTypeChanged(HuckelBond source, Object oldValue, Object newValue) {
		HuckelBondEvent event = null;
		for (IBondListener listener : this.getListeners()) {
			if (event == null)
				event = new HuckelBondEvent(source, oldValue, newValue);
			((IHuckelBondListener)listener).huckelBondBondTypeChanged(event);
		}	
	}

	public void addListener(IHuckelBondListener listener) {
		super.addListener(listener);
	}

	public void removeListener(IHuckelBondListener listener) {
		super.removeListener(listener);
	}

	@Override
	public int getNbElectrons() {
		return this.getNbRadR();
	}


}
