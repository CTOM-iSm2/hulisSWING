/**
 * 
 */
package org.ctom.hulis.util.geometry;

import java.util.ArrayList;
import java.util.List;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.GeometryException;
import org.ctom.hulis.huckel.exception.HuckelAtomException;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Point3D;
import org.ctom.util.maths.Rotation;
import org.ctom.util.maths.VecteurCTOM;

/**
 * @author macbook
 *
 */
/**
 * @author macbook
 *
 */
public class VirtualMethyl {

	private List <Atom> lstAtomsCH3;
	private List <Bond> lstBondsCH3;
	private Atom substituteME;
	private Atom aXClone ; // atom lie a ME
	
	/**
	 * @param c
	 * @param h1
	 * @param h2
	 * @param h3
	 * @param me : substituted methyl element by CH3
	 * @throws GeometryException
	 */
	public VirtualMethyl(Atom c, Atom h1, Atom h2, Atom h3, Atom me) throws GeometryException {
		this.lstAtomsCH3 = new ArrayList<Atom>(4);
		this.lstAtomsCH3.add(c);
		this.lstAtomsCH3.add(h1);
		this.lstAtomsCH3.add(h2);
		this.lstAtomsCH3.add(h3);
		this.substituteME = me;
		this.checkListAtoms(this.lstAtomsCH3, this.substituteME);
		this.generateBonds();
		this.aXClone = null;
		Atom meNeighbourg = this.findMeNeighbourg(this.substituteME);
		if (meNeighbourg != null)
			this.aXClone = (Atom) meNeighbourg.clone();
	}
	
	/**
	 * 
	 * @param lstAtoms : must contains C, H, H, H elements and the substituted ME 
	 * @throws GeometryException
	 */
	public VirtualMethyl(List<Atom> lstAtomsCH3, Atom me) throws GeometryException {
		checkListAtoms(lstAtomsCH3, me);
		this.lstAtomsCH3 = lstAtomsCH3;
		this.substituteME = me;
		this.generateBonds();
		Atom meNeighbourg = this.findMeNeighbourg(this.substituteME);
		if (meNeighbourg != null)
			this.aXClone = (Atom) meNeighbourg.clone();
	}

	/**
	 * 
	 */
	private Atom findMeNeighbourg(Atom me) {
		
		List<Bond> bonds = me.getBonds();
		if (bonds != null)
			return bonds.get(0).getAtomLinkedWith(me);
		else
			return null;

		
	}

	/**
	 * 
	 * @param lstAtoms
	 * @throws GeometryException
	 */
	private void checkListAtoms(List<Atom> lstAtoms, Atom me) throws GeometryException {
		if (lstAtoms.size() != 4)
			throw new GeometryException("methyl must be a group of 4 elements + the subistuted methyl ME");
		
		Atom c = lstAtoms.get(0);
		Atom h1 = lstAtoms.get(1);
		Atom h2 = lstAtoms.get(2);
		Atom h3 = lstAtoms.get(3);
		if ((c.getElement() != PeriodicTable.Element.C)||(h1.getElement() != PeriodicTable.Element.H)||
		(h2.getElement() != PeriodicTable.Element.H)||(h3.getElement() != PeriodicTable.Element.H)|| (me.getElement() != PeriodicTable.Element.Me))
			throw new GeometryException("methyl must be a group of C, H, H, H elements, and the subsitued element ME, in this order");
		
		if (me.getSuperSymbol() != c.getSuperSymbol() )
			throw new GeometryException("C and ME must have the same supersymbol");

	}
	
	/**
	 * 
	 */
	private void generateBonds() {
		this.lstBondsCH3 = new ArrayList<Bond>(3);
		try {
			this.lstBondsCH3.add(new Bond(this.getC(), this.getH1()));
			this.lstBondsCH3.add(new Bond(this.getC(), this.getH2()));
			this.lstBondsCH3.add(new Bond(this.getC(), this.getH3()));
		} catch (BondException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @return
	 */
	public Atom getC() {
		Atom a = this.lstAtomsCH3.get(0);
		try {
			if (a.getElement() != PeriodicTable.Element.C)
				throw new GeometryException("This atom should be a C element but it is a " + a.getElement());
		} catch (GeometryException e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * @return
	 */
	public Atom getH1() {
		Atom a = this.lstAtomsCH3.get(1);
		try {
			if (a.getElement() != PeriodicTable.Element.H)
				throw new GeometryException("This atom should be a H element but it is a " + a.getElement());
		} catch (GeometryException e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * @return
	 */
	public Atom getH2() {
		Atom a = this.lstAtomsCH3.get(2);
		try {
			if (a.getElement() != PeriodicTable.Element.H)
				throw new GeometryException("This atom should be a H element but it is a " + a.getElement());
		} catch (GeometryException e) {
			e.printStackTrace();
		}
		return a;
	}

	/**
	 * @return
	 */
	public Atom getH3() {
		Atom a = this.lstAtomsCH3.get(3);
		try {
			if (a.getElement() != PeriodicTable.Element.H)
				throw new GeometryException("This atom should be a H element but it is a " + a.getElement());
		} catch (GeometryException e) {
			e.printStackTrace();
		}
		return a;
	}
	
	
	/**
	 * @return
	 */
	public Atom getSubstituteME() {
		Atom a = this.substituteME;
		try {
			if (a.getElement() != PeriodicTable.Element.Me)
				throw new GeometryException("This atom should be a ME element but it is a " + a.getElement());
		} catch (GeometryException e) {
			e.printStackTrace();
		}
		return a;
	}
	
	/**
	 * @return
	 */
	public List<Atom> getCH3() {
		try {
			this.checkListAtoms(this.lstAtomsCH3, this.substituteME);
		} catch (GeometryException e) {
			e.printStackTrace();
		}
		return this.lstAtomsCH3;
	}
	
	/**
	 * @return
	 */
	public List<Bond> getBonds() {
		return this.lstBondsCH3;
	}
	
	/**
	 * 
	 */
	public void generateConnectivity(){
		this.getC().addConnect(this.getH1().getSuperSymbol());
		this.getC().addConnect(this.getH2().getSuperSymbol());
		this.getC().addConnect(this.getH2().getSuperSymbol());
		this.getH1().addConnect(this.getC().getSuperSymbol());
		this.getH2().addConnect(this.getC().getSuperSymbol());
		this.getH3().addConnect(this.getC().getSuperSymbol());
		List <Atom> lstMeNeighbours  = this.getSubstituteME().getNeighbours();
		if (lstMeNeighbours.size() >0) {
			Atom linkedAtom = lstMeNeighbours.get(0); // Le methyl n'st lie qu'a un seul atome
			this.getSubstituteME().addConnect(linkedAtom.getSuperSymbol());		
			linkedAtom.addConnect(this.getSubstituteME().getSuperSymbol());		
		}
	}
	
	
	/**
	 * 
	 * update number of radical R on C atom from substituted ME
	 * update locations of C atom from substituted ME atom
	 * update locations of H atoms
	 * 
	 */
	public void autoUpdate (){
		Atom c = this.getC();
		Atom h1 = this.getH1();
		Atom h2 = this.getH2();
		Atom h3 = this.getH3();
		Atom me = this.getSubstituteME();

		// affecte le nombre de RadR de Me sur C
		if ((me instanceof HuckelAtom) && (c instanceof HuckelAtom)) {
			try {
				((HuckelAtom) c).setRadR(((HuckelAtom) me).countRadR());
			} catch (HuckelAtomException e) {
				e.printStackTrace();
			}
		}
		
		boolean meHasMoved = false;
		// affecte les coordonnes hulis et geomxyz de Me sur C si Me a ete deplace. 
		if (! me.getLocation().equals(c.getLocation()) ) {
			HuckelIO.PrintIfln("virtualMethyl.autoupdate : change coordinate of C because ME has been moved");
			c.setLocation((Point3D) me.getLocation().clone());
			if (c.getLocationGeom3D()==null)
				c.setLocationGeom3D((Point3D) me.getLocationGeom3D().clone()); 
			meHasMoved = true;
		}
		
		// verifie si le voisin de me a ete deplace depuis la derniere fois, 
		boolean xHasMoved = false;
		Atom aX =this.findMeNeighbourg(me);
		if ( aX != null && this.aXClone != null && !(aX.getLocation().equals(this.aXClone.getLocation()))){
			HuckelIO.PrintIfln("virtualMethyl.autoupdate : X has been moved");
			this.aXClone = (Atom) aX.clone();
			xHasMoved = true;
		}
		else if ((this.aXClone == null) && (aX != null)) {
			this.aXClone = (Atom) aX.clone();		
		}
	
		
		
		//replacer les hydrogenes selon le vecteur X-ME (X etant l'atome rattache a ME) :
		//   - si les coordonnes h n'ont pas ete affectes (nouvau methyl), leur affecter des nouvelles coordonnes
		//   - si X et ME n'ont pas ete deplaces, ne rien faire
		//   - si X ou ME ont ete deplace, replacer les atomes d'hydrogene
		
		if (xHasMoved || meHasMoved || (h1.getLocation() == null) || (h2.getLocation() == null) || (h3.getLocation() == null)) {
			HuckelIO.PrintIfln("virtualMethyl.autoupdate : updateLocationsH()");
			
				
			// 1ere atom 1Am de ME a 110deg de X ds le plan du dessin (x, y)
			// 1H : Rot(110, At(0,0,1), Me)
			// 2H : Rot (120, H1, XME)
			// 3H : Rot(240, H1, XME)
			double lgHBond =  Molecule.LENGTH_H_BOND;		
	
			Atom x = me.getBonds().get(0).getAtomLinkedWith(me); // recupere l'atome lie X avec Me 
			VecteurCTOM vXMe = new VecteurCTOM (x.getLocation(), me.getLocation());
			h1.setLocation(x.getLocation().rotateAndRenorm(me.getLocation(), Rotation.getRotationZ(Math.toRadians(110)), lgHBond));
			h2.setLocation(h1.getLocation().rotateAndRenorm(me.getLocation(), Rotation.getRotation3D(vXMe, Math.toRadians(120)), lgHBond));
			h3.setLocation(h1.getLocation().rotateAndRenorm(me.getLocation(), Rotation.getRotation3D(vXMe, Math.toRadians(240)), lgHBond));
			if (h1.getLocationGeom3D()==null)
				h1.setLocationGeom3D((Point3D) h1.getLocation().clone());
			if (h2.getLocationGeom3D()==null)
				h2.setLocationGeom3D((Point3D) h2.getLocation().clone());
			if (h3.getLocationGeom3D()==null)
				h3.setLocationGeom3D((Point3D) h3.getLocation().clone());
		
		}
	}
	
	/**
	 * @param me
	 * @return true if the supersymbol of C element of CH3 equals to supersymbol of ME given in parameter 
	 */
	public boolean isSubsituteOf(Atom me) {
		if (me.getElement() != PeriodicTable.Element.Me)
			return false;
		return (this.getC().getSuperSymbol() == me.getSuperSymbol());
	}
	
	/**
	 * 
	 * @param me
	 * @return
	 * @throws GeometryException 
	 */
	public void setSubsituteOf(Atom me) throws GeometryException {
		checkListAtoms(this.lstAtomsCH3, me);
		this.substituteME = me;
	}
	
}
