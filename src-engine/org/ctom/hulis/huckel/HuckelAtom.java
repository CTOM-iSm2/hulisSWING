package org.ctom.hulis.huckel;

import java.io.Serializable;

import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.exception.HuckelAtomException;
import org.ctom.hulis.huckel.listeners.IAtomValueListener;
import org.ctom.hulis.huckel.listeners.IHuckelAtomListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;


public class HuckelAtom extends Atom implements IHuckelObject, Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 655377641393938248L;

	public static final int MAX_RAD_R = 2; // maximal number of radical R

	/* members initialized by the factory PeriodicTable */
	protected double hx; // hx : org.ctom.hulis.huckel parameter
	protected int nbElecPi; // number of pi electrons
	protected int nbElecSigma; // number of sigma electrons
	
	/* members used in the parent molecule  */
	protected int seqNum; // sequence number in the org.ctom.hulis.huckel matrix

	/* members set by the user */
	protected int nbRadR; // count of Radical R

	/* calculated values */
	protected double density; // electrons density

	private BlocDelocalized blocDelocalizedParent;

	/**
	 * Constructor by copy 
	 * @param a : org.ctom.hulis.huckel atom to copy 
	 */
	public HuckelAtom(HuckelAtom a) {
		super(a);
		this.density = a.density;
		this.hx = a.hx;
		this.nbRadR = a.nbRadR;
		this.seqNum = a.seqNum;
		this.nbElecPi = a.nbElecPi;
		this.nbElecSigma = a.nbElecSigma;
	}

	/**
	 * Constructor : create a Huckel atom
	 * @param atomicNumber : atomic number of the atom (Z)
	 * @param entry : entry in periodic table : format <Element + number of pi electron> (example : C, O1, O2)
	 * @param element : element : C, O, ...
	 * @param type : atom sigle
	 * @param name : atom name 
	 * @param nbElecSigma : number of sigma electrons
	 * @param nbElecPi : number of pi electrons
	 * @param nbElecVal : number of valence electrons
	 * @param hx : org.ctom.hulis.huckel interaction parameter
	 * @param nbNeighbours : maximal connexions count
	 */
	public HuckelAtom(int atomicNumber, PeriodicTable.Entry entry, PeriodicTable.Element element,
			String type, String name, int nbElecSigma, int nbElecPi,
			int nbElecVal, double hx, int nbNeighbours) {
		super(atomicNumber, entry, element, type, name, nbElecVal, nbNeighbours);

		this.hx = hx;
		this.nbElecSigma = nbElecSigma;
		this.nbElecPi = nbElecPi;
		this.nbRadR = 0;
	}

	
	protected void fireHxChanged(HuckelAtom source, Object oldValue, Object newValue) {
		HuckelAtomEvent event = null;
		for (IAtomValueListener listener :  this.getValueListeners()) {
			if (event == null)
				event = new HuckelAtomEvent(source, oldValue, newValue);
			((IHuckelAtomListener) listener).huckelAtomHxChanged(event);
		}	
	}

	protected void fireSeqNumChanged(HuckelAtom source, Object oldValue, Object newValue) {
		HuckelAtomEvent event = null;
		
		for (IAtomValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new HuckelAtomEvent(source, oldValue, newValue);
			((IHuckelAtomListener)listener).huckelAtomSeqNumChanged(event);
		}	
	}

	protected void fireRadRChanged(HuckelAtom source, Object oldValue, Object newValue) {
		HuckelAtomEvent event = null;
		for (IAtomValueListener listener : this.getValueListeners()) {
	
			if (event == null)
				event = new HuckelAtomEvent(source, oldValue, newValue);
			((IHuckelAtomListener)listener).huckelAtomRadRChanged(event);
		}	
	}
	
	/**
	 * This method sets automatically a sequence number to an atom if it is
	 * equal to zero. To do this, it gets the highest sequence number in the
	 * molecule and it adds 1
	 * 
	 */
	public void autoSeqNum() {

		int old = getSeqNum();

		if (this.seqNum == 0)
			this.seqNum = getMoleculeContainer().getMaxAtomSeqNum() + 1;
		if (old != this.seqNum)
			this.fireSeqNumChanged(this, old,
					getSeqNum());

	}

	/**
	 * This method returns a copy of this Huckel atom
	 * @return a new Huckel atom
	 */
	@Override
	public Object clone() {
		return new HuckelAtom(this);
	}

	/**
	 * This function returns the number of pi electron set by the user on this org.ctom.hulis.huckel atom
	 * 
	 * @return int
	 */

	public int countRadR() {
		return this.nbRadR;
	}

	/**
	 * This function compares this Huckel atom to another
	 * 
	 * @param a : atom to compare
	 * @return true if the both Huckel atoms have got the same properties : index, element, sigle, name, valence of org.ctom.hulis.huckel, 
	 * number of valence electrons, charge, org.ctom.hulis.huckel parameter, number of pi electrons
	 *  else it returns false
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof HuckelAtom))
			return false;
		HuckelAtom hA = (HuckelAtom) o;
		return super.equals(hA) && (hA.nbRadR == this.nbRadR)
				&& (hA.getSeqNum() == getSeqNum()) && (hA.getHx() == this.hx)
				&& (hA.nbElecPi == this.nbElecPi);
	}

	
	@Override
    public int hashCode () {
        final int multiplier = 23;
        if (hashCode == 0) {
            int code = 133;
            code = multiplier * code + super.hashCode();
            code = multiplier * code +  this.nbRadR;
            code = multiplier * code +  this.seqNum;
            code = multiplier * code +  new Double(this.hx).hashCode();
            code = multiplier * code +  this.nbElecPi;
            hashCode = code;
        }
        return hashCode;
	
	}
	
	
	/**
	 * This function gets the electron density calculated on this Huckel atom
	 * @return double
	 */
	public double getDensity() {
		return this.density;
	}


	/**
	 * This function returns the hx value (Huckel parameter) of the Huckel atom
	 * 
	 * @return double
	 */

	public double getHx() {
		return this.hx;
	}

	/**
	 * This function returns the name of the Huckel parameter
	 * @return string
	 */
	public String getNameParamHuckel() {
		return "hX";
	}

	/**
	 * This function returns the name of the Huckel parameter in an HTML format.
	 * It is used in tooltip text
	 * @return string
	 */
	public String getNameParamHuckelHTML() {
		return "h<sub>X</sub>";
	}

	/**
	 * This function returns the number of pi electrons of this atom
	 * 
	 * @return int
	 */

	public int getNbElecPi() {
		return this.nbElecPi;
	}

	
	/**
	 * This method sets the number of pi electrons of this atom
	 * 
	 * @param nbElecPi
	 */

	public void setNbElecPi(int nbElecPi) {
		this.nbElecPi = nbElecPi;
	}

	/**
	 * This function returns the number of Sigma electrons of this atom
	 * 
	 * @return int
	 */

	public int getNbElecSigma() {
		return this.nbElecSigma;
	}

	/**
	 * This function returns the number of  electrons set by the user on this atom (radical)
	 * 
	 * @return int
	 */

	public int getNbRadR() {
		return this.nbRadR;
	}

	/**
	 * This function returns the Huckel parameter value
	 */
	public double getParamHuckel() {
		return this.hx;
	}

	/**
	 * This function returns the sequence number of the org.ctom.hulis.huckel atom in the molecule
	 * 
	 * @return int
	 */
	public int getSeqNum() {
		return this.seqNum;
	}

	/**
	 * This method sets the density of electrons on the atom 
	 * 
	 * @param electron density
	 */
	public void setDensity(double density) {
		this.density = density;
	}

	/**
	 * this method sets the hx value (Huckel parameter) to an atom
	 * 
	 * @param hx : double, Huckel parameter
	 */

	public void setHx(double hx) {
		if (this.moleculeParentContainer instanceof StructureLocalized) 
		try {
			throw new HuckelAtomException("Cannot set Hx on a localized structure");
	
		}
		catch (HuckelAtomException e) {
			e.printStackTrace();
		}
		
		 if  ( (this.moleculeParentContainer instanceof Structure) && ( ((Structure) this.moleculeParentContainer).getMesomeryParent() != null) && (((Structure) this.moleculeParentContainer).getMesomeryParent() .countLocalizedStructs() >0))
		 try {
				throw new  HuckelAtomException("Cannot set Hx. There are localized structures in the mesomery");
		
			}
			catch (HuckelAtomException e) {
				e.printStackTrace();
			}
		 
		double old = getHx();
		this.hx = hx;
		if (this.hx != old)
			this.fireHxChanged(this, old, getHx());
	}

	/**
	 * This method sets a number pi electrons to this atom
	 * 
	 * @param int : number of electrons to set
	 * @throws HuckelAtomException if negative value or greater than MAX_RAD_R constant or if the atom owns to a delocalized bloc
	 * or if the atom owns to a delocalized bloc 
	 */

	public void setRadR(int nbRadR) throws HuckelAtomException {
		if (this.moleculeParentContainer instanceof StructureDelocalized)
			throw new HuckelAtomException("Cannot put electrons on an atom in a delocalized structure");
	
		if ((nbRadR < 0) || (nbRadR > MAX_RAD_R))
			throw new HuckelAtomException(
					"incorrect number of radical R : cannot put : " + nbRadR);
		if (this.blocDelocalizedParent != null)
			throw new HuckelAtomException(
					"Cannot change the number of electrons : this atom owns to a delocalized block");
		
		int old = getNbRadR();
		do_setRadR(nbRadR);
		if (this.nbRadR != old  )
			this.fireRadRChanged(this, old, getNbRadR());

	}
	
	public void do_setRadR(int nbRadR) {
		this.nbRadR = nbRadR;
		if (this.moleculeParentContainer != null)
			this.moleculeParentContainer.updateCharges();
	
	}
	

	/**
	 * This method sets the sequence number to the Huckel atom in the molecule
	 * 
	 * @param seqNum : sequence number to set
	 * @throws HuckelAtomException : if sequence number is negative
	 */
	public void setSeqNum(int seqNum) throws HuckelAtomException {
		if (this.moleculeParentContainer instanceof StructureLocalized) 
		try {
			throw new HuckelAtomException("Cannot number atom on a localized structure");
	
		}
		catch (HuckelAtomException e) {
			e.printStackTrace();
		}
		
		
		 if  ( (this.moleculeParentContainer instanceof Structure) && ( ((Structure) this.moleculeParentContainer).getMesomeryParent() != null) && (((Structure) this.moleculeParentContainer).getMesomeryParent() .countLocalizedStructs() >0))
				throw new RuntimeException("Cannot number the atom. There are localized structures in the mesomery");

		
		if (seqNum < 0)
			throw new HuckelAtomException("incorrect sequence number");
		int old = getSeqNum();
		this.seqNum = seqNum;
		if (this.seqNum != old )
			this.fireSeqNumChanged(this, old,
					getSeqNum());
	}

	/**
	 * This function returns a string representation of this Huckel atom
	 * @return string
	 */
	@Override
	public String toString() {
		return getSeqNum() + " " + getSigle();
	}

	/**
	 *  This function counts the bond around the atom that are in a bloc
	 */
	
	public int countBondAtomInBloc() {
		int nDB = 0;
		for (HuckelBond bond : this.moleculeParentContainer.getHuckelBonds()) {
			if (bond.getBlocDelocalizedParent() != null) {
				if ((bond.getAtom1() == this) || (bond.getAtom2() == this)) 
					nDB++;
			}			
		}
		return nDB;
	}

//	@Override
	public BlocDelocalized getBlocDelocalizedParent() {
		return this.blocDelocalizedParent;
	}

//	@Override
	public void setBlocDelocalizedParent(BlocDelocalized blocDelocalized) {
		this.blocDelocalizedParent = blocDelocalized;
		
	}

	/**
	 * 	
	 * @param listener
	 */
	public void addListener(IHuckelAtomListener listener) {
		super.addListener(listener);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void removeListener(IHuckelAtomListener listener) {
		super.removeListener(listener);
	}

	@Override
	public int getNbElectrons() {
		return this.getNbRadR();
	}
}
