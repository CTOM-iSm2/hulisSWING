package org.ctom.hulis.huckel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ctom.hulis.huckel.events.GeometryEvent;
import org.ctom.hulis.huckel.exception.AtomException;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.GeometryException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.listeners.IAtomGeometryListener;
import org.ctom.hulis.huckel.listeners.IAtomValueListener;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.util.geometry.CovalentRadii;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Point3D;
import org.ctom.util.maths.Rotation;
import org.ctom.util.maths.VecteurCTOM;


/**
 * This class describe a basic Huckel atom
 * 
 * @author Nicolas Goudard
 * @version 1.0
 * @since JDK 1.5
 */

public class Atom implements IMoleculeComponent, Cloneable, Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 2359193961861225653L;
	
	/* members initialized by the factory PeriodicTable */
	protected String sigle; // sigle of the atom
	public PeriodicTable.Element element; // element 
	PeriodicTable.Entry entry; // entry in periodic table : format <Element + number of pi electron> (example : C, O1, O2)
	protected String name; // name of the atom 
	protected int nbElecVal; // count of valence electrons
	protected int nbNeighboursMax; // maximal connexions authorized to this atom
	protected double valence; // valence of org.ctom.hulis.huckel (see the parameters in the journal of chemistry)

	/* members calculated after modification : ****/
	public int charge; // charge of an atom 


	/* members used by Geom class ****/
	private ArrayList<Integer> connect = null; // list of atoms connected to it (used in Geom class)
	private int superSymbol; // sequence number used in Geom classs

	/* members used to get the atom in the parent molecule ***/
	public Molecule moleculeParentContainer; // Molecule that contains this atom
	protected int index; // sequence number of an atom in the molecule

	/* members used to localize atoms in the space ***/
	Point3D p2D;
	Point3D pGeom3D; // this variable is set by the geometry file reader
	                // and can be modified if the atoms is moved by setLoation
	
	protected final ArrayList<IAtomValueListener> atomValuelisteners;
	protected final ArrayList<IAtomGeometryListener> geometryListeners;
	private int atomicNumber;




	/**
	 * Copy constructor. This method does not copy the molecule parent
	 * 
	 * @param Atom : a : atom to copy
	 *            
	 */

	public Atom(Atom a) {
		this.atomicNumber = a.atomicNumber;
		this.element = a.element;
		this.entry = a.entry;
		this.sigle = a.sigle;
		this.name = a.name;
		this.nbElecVal = a.nbElecVal;
		this.valence = a.valence;
		this.index = a.index;
		this.charge = a.charge;
		this.p2D = null;
		if (a.p2D != null)
			this.p2D = (Point3D) a.p2D.clone();
		this.pGeom3D = null;
		if (a.pGeom3D != null)
			this.pGeom3D = (Point3D) a.pGeom3D.clone();
		this.nbNeighboursMax = a.nbNeighboursMax;
		this.superSymbol = a.superSymbol;
		if (a.connect != null) {
			this.connect = new ArrayList<Integer>();
			for (Integer i : a.connect)
				this.connect.add(i);
		}
		this.atomValuelisteners = new ArrayList<IAtomValueListener>();
		this.geometryListeners = new ArrayList<IAtomGeometryListener>();
		// this.moleculeParentContainer= null;
	}

	protected void fireAtomLocationChanged(Atom source, Object oldValue, Object newValue) {
		GeometryEvent event = null;
		for (IAtomGeometryListener listener :  this.getGeometryListeners()) {
			if (event == null)
				event = new GeometryEvent(source, oldValue, newValue);
			((IAtomGeometryListener) listener).atomLocationChanged(event);
		}	
	}
	/**
	 * This constructor creates an atom object
	 * 
	 * @param atomicNumber : atomic number of the atom (Z)
	 * @param entry : entry in periodic table : format <Element + number of pi electron> (example : C, O1, O2)
	 * @param element : element
	 * @param sigle : sigle of the atom (element in string format)
	 * @param name : name of the atom
	 * @param nbElecVal : number of valence electrons
	 * @param nbNeighboursMax : number of authorized connexions to this atom
	 * 
	 */
	public Atom(int atomicNumber, PeriodicTable.Entry entry, PeriodicTable.Element element, String sigle, String name,
			int nbElecVal, int nbNeighboursMax) {
		this.atomicNumber = atomicNumber;
		this.entry = entry;
		this.element = element;
		this.sigle = sigle;
		this.name = name;
		this.nbElecVal = nbElecVal;
		this.nbNeighboursMax = nbNeighboursMax;
		this.index = 0;
		this.charge = 0;
		this.moleculeParentContainer = null;
		this.superSymbol = 0;
		this.atomValuelisteners = new ArrayList<IAtomValueListener>();
		this.geometryListeners = new ArrayList<IAtomGeometryListener>();
		this.p2D= null;
		this.pGeom3D = null;
	}

	
	/**
	 * Rotate the atom around the origin by a rotation matrix
	 * @param mRot : rotation matrix
	 */
	public void rotate (Rotation mRot)  {		
		this.p2D.rotateEquals (mRot);
		this.setLocationNotNotify(p2D);
	}
	

	/**
	 * Rotate  the atom around a point by a rotation matrix
	 * @param mRot : rotation matrix
	 */
	public void rotate (Point3D around, Rotation mRot)  {		
		this.p2D = this.p2D.rotate(around, mRot);
		this.setLocationNotNotify(p2D);
	}
	
	/**
	 * 
	 * @param vector
	 */
	public void translate (VecteurCTOM v)  {		
		this.p2D.translateEquals(v);
		this.setLocationNotNotify(this.p2D);
	}
	
	/**
	 * 
	 * @param other
	 * @return
	 */
	public double getDistance(Atom other) {
		return this.p2D.getDistance(other.p2D);	
	}
	
	/**
	 * This method adds a charge on an atom. 
	 * @param : charge to add .
	 * For example : if the charge ot the atom is 2- and the parameter is 1, the charge will be 1-
	 */
	protected void addCharge(int charge) {
		setCharge(this.charge + charge);
	}

	/**
	 * This method adds an atom. This method is used in Geom class
	 * @param i : supersymbol of to the atom to connect with this atom
	 */
	public void addConnect(int i) {
		if (i==0) {
			try {
				throw new GeometryException("atom.addConnect(i) : cannot connect atom because supersymbol parameter i==0. Atoms supersymbols must be greater than zero.");
			}
			catch (GeometryException e) {
				e.printStackTrace();
				return;
			}
		}
		checkConnect();
		this.connect.add(i);
	}

	/**
	 * This method checks if there are connexions on this atom. If there are not connexions
	 * it creates an empty list.
	 */
	public void checkConnect() {
		if (this.connect == null)
			this.connect = new ArrayList<Integer>();
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return new Atom(this);
	}

	/**
	 * This function counts the number of double bonds that contain this atom
	 * 
	 * @return the number of double bonds that contain this atom
	 */

	// deb Yannick
	public int countDBondAtom() {
		int nDB = 0;
		Iterator it = this.moleculeParentContainer.getHuckelBonds().iterator();
		while (it.hasNext()) {
			HuckelBond bond = (HuckelBond) it.next();
			if (bond.getBondType() == 2) {
				if ((bond.getAtom1() == this) || (bond.getAtom2() == this)) {
					nDB++;
				}
			}
		}
		return nDB;
	}

	// fin Yannick

	/**
	 * This function counts the number of atoms linked to this atom
	 * @return the count of atoms linked to this atom
	 */
	public int countNeighbours() {
		if (getMoleculeContainer() == null)
			return 0;

		return getMoleculeContainer().countNeighbours(this);
	}


	/**
	 * This function counts the number of atoms, exluding hydrogen atoms, linked to this atom
	 * @return the count of atoms linked to this atom, excluding hydrogen atoms
	 */
	public int countNeighboursExcludingH() {
		if (getMoleculeContainer() == null)
			return 0;

		return getMoleculeContainer().countNeighboursExcludingH(this);
	}

	
	/**
	 * This function returns the list of atoms linked with this atom.
	 * If the atom is not in a molecule it returns null 
	 * @return
	 */
	public List<Atom> getNeighbours() {
		if (getMoleculeContainer() == null)
			return null;

		return getMoleculeContainer().getNeighbours(this);

	}
	
	/**
	 * This function counts the number of simple bonds that contain this atom
	 * 
	 * @return the number of single bonds that contain this atom
	 */

	public int countSBondAtom() {
		int nSB = 0;
		Iterator it = this.moleculeParentContainer.getBonds().iterator();
		while (it.hasNext()) {
			Bond b = (Bond) it.next();
			;
			if (b instanceof HuckelBond) {
				HuckelBond bond = (HuckelBond) b;
				if (bond.getBondType() == 1) {
					if ((bond.getAtom1() == this) || (bond.getAtom2() == this)) {
						nSB++;
					}
				}
			} else if ((b.getAtom1() == this) || (b.getAtom2() == this)) {
				nSB++;
			}
		}
		return nSB;
	}

	/**
	 * This function counts the number of triple bonds around this atom
	 * 
	 * @return the number of triple bonds that contain this atom
	 */
	public int countTBondAtom() {
		int nTB = 0;
		Iterator it = this.moleculeParentContainer.getHuckelBonds().iterator();
		while (it.hasNext()) {
			HuckelBond bond = (HuckelBond) it.next();
			if (bond.getBondType() == 3) {
				if ((bond.getAtom1() == this) || (bond.getAtom2() == this)) {
					nTB++;
				}
			}
		}
		return nTB;
	}

	/**
	 * This methods displays the atom properties in the standard output
	 */
	public void debug() {
		String res = "";

		res += "ATOM" + "\n";
		if (getMoleculeContainer() != null)
			res += "molecule hascode = " + +getMoleculeContainer().hashCode()
			+ "\n";
		else
			res += "molecule : null !!!\n";
		res += "index : " + getIndex() + "\n";
		res += "type :  " + getSigle() + "\n";
		;
		res += "name : " + getName() + "\n";
		res += "charge : " + getCharge() + "\n";
		res += "nbElecVal :" + getNbElecVal() + "\n";
		res += "valence :  " + getValence() + "\n";
		res += "max neighbourgs :  " + getNbNeighboursMax() + "\n";
		res += "location : ";
		res += "x= " + getX() + ", y= " + getY() + "\n";

		HuckelIO.PrintIf(res);
	}

	/**
	 * This method removes this atom in the molecule and the bonds that contain it
	 * @throws MoleculeBondExistsException 
	 * @throws MoleculeCoherenceException 
	 * @throws MoleculeTooManyNeighboursException 
	 * @throws BondException 
	 * @throws HuckelBondException 
	 * @throws Exception 
	 */
	public void remove() throws MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException, HuckelBondException, BondException  {

		if (getMoleculeContainer() != null)
			getMoleculeContainer().removeAtom(this);

	}

	/**
	 * This function compares this atom to another
	 * 
	 * @param a : atom to compare
	 * @return true if the both atoms have got the same properties : index, element, sigle, name, valence of org.ctom.hulis.huckel, 
	 * number of valence electrons, charge
	 *  else it returns false
	 */

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof Atom))
			throw new ClassCastException();

		Atom a = (Atom) o;
		/*
		 * if (! (o instanceof Atom)) return false; Atom a = (Atom ) o;
		 */
		return ((a.atomicNumber == this.atomicNumber) && (a.index == this.index) && (a.getElement()== this.getElement()) && (a.sigle == this.sigle)
				&& (a.name == this.name) && (a.valence == this.valence)
				&& (a.nbElecVal == this.nbElecVal) && (a.charge == this.charge));

	}

	protected volatile int hashCode = 0;

	@Override
	public int hashCode () {
		final int multiplier = 23;
		if (hashCode == 0) {
			int code = 133;
			if (this.connect != null)
				code = multiplier * code + this.connect.hashCode();
			code = multiplier * code + this.atomicNumber;
			code = multiplier * code + this.index;
			if (this.getElement() != null)
				code = multiplier * code + this.getElement().hashCode();
			code = multiplier * code + this.sigle.hashCode();
			code = multiplier * code + this.name.hashCode();
			code = multiplier * code + new  Double (this.valence).hashCode() ;
			code = multiplier * code + this.nbElecVal;
			code = multiplier * code + this.charge;
			hashCode = code;
		}
		return hashCode;

	}
	/**
	 * This function gets bonds that include this atom
	 * list of bonds
	 */
	 public List<Bond> getBonds() {
		 Molecule molecule = this.getMoleculeContainer();
		 if (molecule == null) 
			 return null;
		 
		 List<Bond> lstBond = new ArrayList<Bond> ();
		 for (Bond b : molecule.getBonds()) {
			 if (b.contains(this)) 
				 lstBond.add(b);
		 }
		 return lstBond;
	 }

	 /**
	  * This function returns the charge of this atom
	  * 
	  * @return the charge of this atom
	  */

	 public int getCharge() {
		 return this.charge;
	 }

	 /**
	  * This function gets the list of the atoms connected with it.
	  * @return the list of atoms linked with this atom
	  */
	 public ArrayList<Integer> getConnect() {
		 checkConnect();
		 return this.connect;
	 }

	 /**
	  * Deprecated : Use getLocation instead.
	  * This function returns an array with the coordinates of this atom
	  * @return an array of double
	  */
	 @Deprecated
	 public double[] getCoord3D() {
		 double[] coord = new double[3];
		 coord[0] = getX();
		 coord[1] = getY();
		 coord[2] = getZ();
		 return coord;
	 }

	 /**
	  * This function returns the constant covalent radius of this atom corresponding to its element
	  * If the eleme is null, returns the covalent radius of atom C 
	  * @return the constant covalent radius corresponding to the element of the atom, or the covalent radius of atom C  if the element is null
	  */
	 public double getCovalentRadius() {
		 double radius;
		 if (getElement() == null)
			 return CovalentRadii.C.getCovRad();
		 radius = CovalentRadii.valueOf(getElement().toString()).getCovRad();
		 return radius;
	 }

	 /**
	  * This function returns the distance 3D between this atom and another, using a ratio
	  * @param that
	  * @param scale
	  * @return
	  */

	 public double getDist3D(Atom that, double scale) {
		 double dist = 0;
		 dist += Math.pow((getX() - that.getX()) / scale, 2.0);
		 dist += Math.pow((getY() - that.getY()) / scale, 2.0);
		 dist += Math.pow((getZ() - that.getZ()) / scale, 2.0);
		 dist = Math.sqrt(dist);
		 return dist;
	 }

	 /**
	  * This function returns the element of the atom
	  * @return the element of this atom : C, O ,etc...
	  */

	 public PeriodicTable.Element getElement() {
		 return this.element;
	 }

	 /**
	  * This function return the entry of this atom in the periodic table
	  * @return entry
	  * @see PeriodicTable
	  */
	 public PeriodicTable.Entry getEntry() {
		 return this.entry;
	 }

	 /**
	  * This function returns the index (sequence number) of the atom in the molecule.
	  * @returns the sequence number of the atom in the molecule
	  * 
	  */

	 public int getIndex() {
		 return this.index;
	 }



	 /**
	  * This function gets the molecule that contains this atom
	  * 
	  * @return the molecule that contains this 
	  */

	 public Molecule getMoleculeContainer() {
		 return this.moleculeParentContainer;
	 }

	 /**
	  * This function returns the name of this atom
	  * 
	  * @return the name of this atom
	  */
	 public String getName() {
		 return this.name;
	 }

	 /**
	  * This function returns the number of valence electrons of this atom
	  * 
	  * @return the number of valence electron of the atom. It is a constant determined by the type (element)
	  * of the atom. 
	  * @see PeriodicTable
	  */
	 public int getNbElecVal() {
		 return this.nbElecVal;
	 }

	 /**
	  * This function returns the number of maximum connections of this atom
	  * 
	  * @return the number of connections of the atom. 
	  * of the atom. 
	  * @see PeriodicTable
	  */
	 public int getNbNeighboursMax() {
		 return this.nbNeighboursMax;
	 }

	 
	 /**
	  * This method sets the number of maximum connections of this atom
	  * @param nbNeighboursMax
	  */
	 public void setNbNeighboursMax(int nbNeighboursMax) {
		  this.nbNeighboursMax = nbNeighboursMax;
	 }


	 /**
	  * This function returns the symbol of this atom
	  * 
	  * @return the sigle of this atom
	  */
	 public String getSigle() {
		 return this.sigle;
	 }

	 /**
	  * This function returns the index of this atom in a "geom" object
	  * 
	  * @return the supersymbol
	  * @see Geom
	  */
	 public int getSuperSymbol() {
		 return this.superSymbol;
	 }

	 /**
	  * This function returns the valence huckel of this atom
	  * 
	  * @return the valence of org.ctom.hulis.huckel
	  */
	 public double getValence() {
		 return this.valence;
	 }

	 /**
	  * This function returns the abscissa of this atom
	  * 
	  * @return returns the coordinate x
	  */
	 public double getX() {

		 return this.p2D.getX();
	 }

	 /**
	  * This function returns the ordinate of this atom
	  * 
	  * @return returns the coordinate y
	  */
	 public double getY() {

		 return this.p2D.getY();
	 }

	 /**
	  * This function returns the coordinate z of this atom
	  * 
	  * @return returns the coordinate z
	  */
	 public double getZ() {
		 return this.p2D.getZ();
	 }

	 // yannick

	 // yannick
	 /**
	  *  This method prints this atom into the standard output
	  *  
	  */
	 public void print() {
		 System.out.printf("%s", getSigle());
	 }

	 // yannick


	 /**
	  * This method sets the charge to this atom
	  * 
	  * @param charge to set
	  */
	 public void setCharge(int charge) {
		 this.charge = charge;
	 }

	 /**
	  * This method sets the element of this atom
	  * 
	  * @param element to set
	  */
	 protected void setElement(PeriodicTable.Element element) {
		 this.element = element;
	 }

	 /**
	  * This method sets the index to this atom
	  * 
	  * @param index to set
	  */

	 protected void setIndex(int index) {
		 this.index = index;
	 }

	 /**
	  * This method sets the coordinates x,y,z to an atom
	  * This method changes the coordinate of the geom3d atom location
	  * 
	  * @param coordinates Point3D(x,y,z) to set
	  */
	 public void setLocation(Point3D p) {
		 if ((this.p2D != null) && (this.moleculeParentContainer instanceof StructureLocalized)) 
				try {
					throw new AtomException("Cannot change atom location on a localized structure");
			
				}
				catch (AtomException e) {
					e.printStackTrace();
					return;
				}
		 
		 if  ( (this.moleculeParentContainer instanceof Structure) && ( ((Structure) this.moleculeParentContainer).getMesomeryParent() != null) && (((Structure) this.moleculeParentContainer).getMesomeryParent() .countLocalizedStructs() >0))
				throw new RuntimeException("Cannot move the atom. There are localized structures in the mesomery");
		 
		 Point3D old = null;
		 if (this.p2D != null)
			 old = (Point3D) this.p2D.clone();
		 setLocationNotNotify(p);
		 if (!p.equals( old))
			 this.fireAtomLocationChanged(this, old, p);

	 }

	 /**
	  * 
	  * @param p
	  */
	 protected void setLocationNotNotify(Point3D p) {
	
		 this.p2D = p;
		 this.pGeom3D = (Point3D) this.p2D.clone();
	 }
	 
	 /**
	  * 
	  * This method changes the coordinate of the geom3d atom location
	  * @return
	  */
	 public Point3D setLocationGeom3D(Point3D pXYZ){
		 return this.pGeom3D = pXYZ;
	 }
	 
	 /**
	  * 
	  */
	 public Point3D getLocation(){
		 return this.p2D;
	 }
	 
	 /**
	  * 
	  * @return
	  */
	 public Point3D getLocationGeom(){
		 return this.pGeom3D;
	 }

	 /**
	  * Sets the molecule that owns to this atom
	  * 
	  * @param molecule
	  */

	 protected void setMoleculeContainer(Molecule moleculeParentContainer) {
		 this.moleculeParentContainer = moleculeParentContainer;
	 }

	 /**
	  * This method sets a name to an atom
	  * 
	  * @param name to set
	  */
	 protected void setName(String name) {
		 this.name = name;
	 }

	 /**
	  * This method sets the number of valence electrons to an atom
	  * 
	  * @param nbValElec : number of valence eletrons to set
	  * @throws AtomException
	  *             if the number is incorrect
	  */
	 public void setNbElecVal(int nbValElec) throws AtomException {
		 if (nbValElec < 0)
			 throw new AtomException(
					 "number of Pi electrons must be positive or null");

		 this.nbElecVal = nbValElec;
	 }

	 // yannick

	 /**
	  * This method sets a sigle to an atom
	  * 
	  * @param sigle
	  */
	 public void setSigle(String atomType) {
		 this.sigle = atomType;
	 }

	 /**
	  * This method sets the supersymbol, index in a geom object
	  * 
	  * @param supersymbol
	  * @see Geom
	  * 
	  */

	 public void setSuperSymbol(int nombre) {
		 this.superSymbol = nombre;

	 }

	 /**
	  * This method sets the org.ctom.hulis.huckel valence
	  * 
	  * @param org.ctom.hulis.huckel valence
	  * @throws AtomException
	  *             if the valence is negative
	  */
	 protected void setValence(double valence) throws AtomException {
		 if (valence < 0)
			 throw new AtomException("valence must be positive or null");

		 this.valence = valence;
	 }

	 /**
	  * This method sets the coordinate x 
	  * @param x
	  */
	 public void setX(double x) {
		if ((this.p2D != null) && (this.moleculeParentContainer instanceof StructureLocalized)) 
			try {
				throw new AtomException("Cannot change x on a localized structure");
		
			}
			catch (AtomException e) {
				e.printStackTrace();
				return;
			}
				
		 if  ( (this.moleculeParentContainer instanceof Structure) && ( ((Structure) this.moleculeParentContainer).getMesomeryParent() != null) && (((Structure) this.moleculeParentContainer).getMesomeryParent() .countLocalizedStructs() >0))
				throw new RuntimeException("Cannot move the atom. There are localized structures in the mesomery");
	
		 Point3D old = null;
		 if (this.p2D != null)
			 old = (Point3D) this.p2D.clone();
		 this.setXNotNotify(x);
		 if (!p2D.equals(old))
			 this.fireAtomLocationChanged(this, old, p2D);

	 }

	 public void setXNotNotify(double x) {
		 if (this.p2D == null) 
			 this.p2D = new Point3D(x, 0.0, 0.0);
		
		 this.p2D.setX(x);
		 
		 if (this.pGeom3D == null) 
			 this.pGeom3D = new Point3D(x, 0.0, 0.0);
		
		 this.pGeom3D.setX(x);
	 }

	 /**
	  * This method sets the coordinate y
	  * @param y
	  * 
	  */
	 public void setY(double y) {
		if ((this.p2D != null) && (this.moleculeParentContainer instanceof StructureLocalized)) 
			try {
				throw new AtomException("Cannot change y on a localized structure");
		
			}
			catch (AtomException e) {
				e.printStackTrace();
				return;
			}
		 if  ( (this.moleculeParentContainer instanceof Structure) && ( ((Structure) this.moleculeParentContainer).getMesomeryParent() != null) && (((Structure) this.moleculeParentContainer).getMesomeryParent() .countLocalizedStructs() >0))
				throw new RuntimeException("Cannot move the atom. There are localized structures in the mesomery");

		 Point3D old = null;
		 if (this.p2D != null)
			 old = (Point3D) this.p2D.clone();
		 this.setYNotNotify(y);
		 if (!p2D.equals(old))
			 this.fireAtomLocationChanged(this, old, p2D);

	 }

	 public void setYNotNotify(double y) {
		 if (this.p2D == null)
			 this.p2D = new Point3D(0.0, y, 0.0);
		 this.p2D.setY(y);
		 
		 if (this.pGeom3D == null)
			 this.pGeom3D = new Point3D(0.0, y, 0.0);
		 this.pGeom3D.setY(y);
	 }

	 /**
	  * This method sets the coordinate z
	  * @param z
	  * 
	  */
	 public void setZ(double z) {
		 if ((this.p2D != null) && (this.moleculeParentContainer instanceof StructureLocalized)) 
				try {
					throw new AtomException("Cannot change z on a localized structure");
			
				}
				catch (AtomException e) {
					e.printStackTrace();
					return;
				}
		 if  ( (this.moleculeParentContainer instanceof Structure) && ( ((Structure) this.moleculeParentContainer).getMesomeryParent() != null) && (((Structure) this.moleculeParentContainer).getMesomeryParent() .countLocalizedStructs() >0))
				throw new RuntimeException("Cannot move the atom. There are localized structures in the mesomery");
	 
		 Point3D old = null;
		 if (this.p2D != null)
			 old = (Point3D) this.p2D.clone();
		 this.setZNotNotify(z);
		 if (!p2D.equals(old))
			 this.fireAtomLocationChanged(this, old, p2D);

	 }

	 protected void setZNotNotify(double z) {
		 if (this.p2D == null)
			 this.p2D = new Point3D(0.0, 0.0, z);
		 this.p2D.setZ(z);
		 
		 
		 if (this.pGeom3D == null)
			 this.pGeom3D = new Point3D(0.0, 0.0, z);
		 this.pGeom3D.setZ(z);
	 }

	 /**
	  * This function returns the string representation of the atom
	  * 
	  * @return String
	  */
	 @Override
	 public String toString() {
		 return getSigle();
	 }

	 public void addListener(IAtomValueListener listener) {
		 this.atomValuelisteners.add(listener);
	 }

	 public void removeListener(IAtomValueListener listener) {
		 this.atomValuelisteners.remove(listener);
	 }

	 public void addListener(IAtomGeometryListener listener) {
		 this.geometryListeners.add(listener);
	 }

	 public void removeListener(IAtomGeometryListener listener) {
		 this.geometryListeners.remove(listener);
	 }

	
	
	protected ArrayList<IAtomGeometryListener> getGeometryListeners() {
		return (ArrayList<IAtomGeometryListener> ) this.geometryListeners.clone();
	}
	
	protected ArrayList<IAtomValueListener> getValueListeners() {
		return (ArrayList<IAtomValueListener> ) this.atomValuelisteners.clone();
	}
}
