package org.ctom.hulis.huckel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.ctom.hulis.huckel.PeriodicTable.Entry;
import org.ctom.hulis.huckel.comparators.SeqNumComparator;
import org.ctom.hulis.huckel.events.GeometryEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.GeometryException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.MoleculeAtomNullException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeNotSupportedComponentException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.listeners.IAtomGeometryListener;
import org.ctom.hulis.huckel.listeners.IAtomValueListener;
import org.ctom.hulis.huckel.listeners.IHuckelAtomListener;
import org.ctom.hulis.huckel.listeners.IHuckelBondListener;
import org.ctom.hulis.huckel.listeners.IMoleculeGeometryListener;
import org.ctom.hulis.huckel.listeners.IMoleculeListener;
import org.ctom.hulis.huckel.listeners.IMoleculeValueListener;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.util.geometry.CovalentRadii;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.geometry.optimizer.Geom2DOptimizer;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Point3D;
import org.ctom.util.maths.Rotation;
import org.ctom.util.maths.VecteurCTOM;



/**
 * Molecule : This class describes a molecule object
 * 
 * @see : Atom
 * @see : Bond
 * @author Nicolas Goudard, Yannick Carissan
 * @version 1.0
 * @since JDK 1.5
 * 
 */

public class Molecule implements Cloneable, IDebuggable, IHuckelBondListener, IAtomGeometryListener, IHuckelAtomListener, Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2403133786018141877L;
	
	public final static double ATOM_ZONE = 12.0;
	public final static double deuxPI = Math.PI * 2;
	public final static double LENGTH_H_BOND = (CovalentRadii.C.getCovRad()+CovalentRadii.H.getCovRad()) * Geometry.SCALE; // 1 
	private boolean autoCorrectH;
	private boolean enabledNotify;
	protected  Geometry geometry;

	protected  final ArrayList<IMoleculeGeometryListener> geometryListeners;
	protected  final ArrayList<IMoleculeValueListener> valueListeners;
	private ArrayList<Atom> lstAtoms; // atoms which compose a molecule

	private ArrayList<Bond> lstBonds; // Bonds that compose a molecule
	
	private int nbElecCharge; // number of electron (charge of the molecule)
	protected volatile int hashCode = 0;
	private int[][] topology=null;


	/**
	 * Constructor : this method creates an object Molecule
	 */
	public Molecule() {
		this.lstAtoms = new ArrayList<Atom>();
		this.lstBonds = new ArrayList<Bond>();
		this.nbElecCharge = 0;
		this.valueListeners = new ArrayList<IMoleculeValueListener>();
		this.geometryListeners = new ArrayList<IMoleculeGeometryListener>();
		this.enabledNotify = true;
		this.autoCorrectH = true;
		this.geometry = null;
	}
	
	/**
	 * Copy constructor
	 * 
	 * @param molecule
	 *            to copy
	 */

	public Molecule(Molecule molecule) {
		Atom aClone;
		Bond lClone;

		this.nbElecCharge = molecule.getNbElecCharge();
		this.lstAtoms = new ArrayList<Atom>();
		this.lstBonds = new ArrayList<Bond>();
		this.valueListeners = new ArrayList<IMoleculeValueListener>();
		this.geometryListeners = new ArrayList<IMoleculeGeometryListener>();
		this.enabledNotify = molecule.enabledNotify;
		this.autoCorrectH = molecule.autoCorrectH;
		this.geometry=null;
		
		// copier les atomes
		for (Atom a : molecule.getAtoms()) {
			aClone = (Atom) a.clone();
			aClone.setMoleculeContainer(this);
			aClone.addListener((IAtomValueListener)this);
			aClone.addListener((IAtomGeometryListener)this);
			this.lstAtoms.add(aClone);
		}

		// copier les liaisons
		for (Bond l : molecule.getBonds()) {	
			lClone = (Bond) l.clone();
			lClone.setMoleculeContainer(this);
			lClone.addListener(this);
			lClone.atom1 = this.getAtomByIndex(l.getAtom1().getIndex());
			lClone.atom2 =  this.getAtomByIndex(l.getAtom2().getIndex());
			this.lstBonds.add(lClone);


		}

		this.updateTopology();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return new Molecule(this);

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode () {
		final int multiplier = 23;
		if (hashCode == 0) {
			int code = 133;
			code = multiplier * code +  this.getNbElecCharge();
			if (this.lstAtoms != null)
				code = multiplier * code +  this.lstAtoms.hashCode();
			if (this.lstBonds != null)
				code = multiplier * code +  this.lstBonds.hashCode();

			hashCode = code;
		}
		return hashCode;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object  o) {
		if (this == o)
			return true;

		if (!(o instanceof Molecule))
			return false;

		return this.geometryEquals((Molecule) o);
	}
	
	
	/**
	 * 
	 * @param molecule
	 */
	public boolean geometryEquals(Molecule molecule) {
		if (getNbElecCharge() != molecule.getNbElecCharge())
			return false;
		if (molecule.countAtoms() != countAtoms())
			return false;
		if (molecule.countBonds() != countBonds())
			return false;
		for (Atom a : getAtoms())
			if (!a.equals(molecule.getAtomByIndex(a.getIndex())))
				return false;
		for (Bond b : getBonds())
			if (!b.equals(molecule.getBondByIndex(b.getIndex())))
				return false;
		return true;
	}


	/**
	 * 
	 */
	public void notifyValueChanged() {
		this.fireMoleculeAtomAdded(this, null, null);
	}
	
	/**
	 * 
	 */
	public void notifyGeometryChanged() {
		this.fireAtomLocationChanged(new GeometryEvent(this, null, null));
	}
	
	
	/**
	 * 
	 */
	public void optimizeGeometry() {
		if (this instanceof StructureLocalized) 
			try {
				throw new MoleculeCoherenceException("Cannot optimize a localized structure");
		
			}
			catch (MoleculeCoherenceException e) {
				e.printStackTrace();
				return;
			}
		Geom2DOptimizer opt = new Geom2DOptimizer(this);
		opt.setOptimize(true);
		opt.setVerbose(false);
		this.fireMoleculeAtomAdded(this, this, null);
		opt.setOptimize(false);

	}

	



	@Deprecated
	/*
	 * jamais utilis�e?
	 */
	public void add(IMoleculeComponent component) throws HuckelBondException, BondException, MoleculeNotSupportedComponentException, MoleculeCoherenceException, MoleculeTooManyNeighboursException, MoleculeBondExistsException {
		if (component instanceof Atom) {
			addAtom((Atom) component);
		} else if (component instanceof Bond) {
			addBond((Bond) component);
		} else
			throw new MoleculeNotSupportedComponentException("moleculecomponent not supported by this class");

	}

	/**
	 * create a new atom in the moleculeAbstract
	 * 
	 * @param atom :
	 *            atom
	 * @throws BondException 
	 * @throws HuckelBondException 
	 * @throws MoleculeBondExistsException 
	 * @throws MoleculeCoherenceException 
	 * @throws MoleculeTooManyNeighboursException 
	 * 
	 */

	public void addAtom(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException{
		addAtomNotNotify(a);
		updateCharges();
		updateTopology();
		this.fireMoleculeAtomAdded(this, null, a);
	}

	protected void addAtomNotNotify(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		// a = new Atom (atomType, this, this.getMaxAtomSeqNum() + 1 , p);
		a.setMoleculeContainer(this);
		a.addListener((IAtomValueListener)this);
		a.addListener((IAtomGeometryListener)this);
		a.setIndex(getMaxAtomIndex() + 1);

		if (a instanceof HuckelAtom) {
			HuckelAtom a2 = (HuckelAtom) a;
			a2.seqNum = getMaxAtomSeqNum() + 1;
		}
		this.lstAtoms.add(a);
		if (a.getElement() != PeriodicTable.Element.H) {
			if (isAutoCorrectH()) {
				correctHBondAround(a);
				reindexAtoms();
				reindexBonds();
			}

		}

	}

	/**
	 * this method creates a bond between two atoms
	 * 
	 * @param b
	 * @throws MoleculeCoherenceException
	 * @throws BondException 
	 * @throws HuckelBondException 
	 * @throws MoleculeBondExistsException 
	 * @throws MoleculeTooManyNeighboursException 
	 */
	public void addBond(Bond b) throws MoleculeCoherenceException, HuckelBondException, BondException, MoleculeBondExistsException, MoleculeTooManyNeighboursException {

		if (isNbNeighboursMaxReachedExcludingH(b.getAtom1()))
			throw new MoleculeTooManyNeighboursException("too many neighbours");

		if (isNbNeighboursMaxReachedExcludingH(b.getAtom2()))
			throw new MoleculeTooManyNeighboursException("too many neighbours");


		addBondNotNotify(b);
		if (isAutoCorrectH())
			correctHBondRelativeTo(b);
		updateCharges();
		updateTopology();
		this.fireMoleculeBondAdded(this, null, b);
	}

	protected void addBondNotNotify(Bond b) throws MoleculeCoherenceException, MoleculeBondExistsException {
		if (!areLinked(b.getAtom1(), b.getAtom2())) {
			if ((b.getAtom1().getMoleculeContainer() != this)
					|| (b.getAtom2().getMoleculeContainer() != this))
				throw new MoleculeCoherenceException(
						"the bond to add must be composed of atoms that belong to the molecule");
			b.setMoleculeContainer(this);
			b.addListener(this);
			this.lstBonds.add(b);
			reindexBonds();
		} else {
			throw new MoleculeBondExistsException("atoms already linked");
		}
	}

	/**
	 * this method adds electrons in the molecule for changing the net charge
	 * 
	 * @param nbElec :
	 *            number of electrons to be added
	 * @throws MoleculeCoherenceException 
	 *             if too much electron
	 */
	public void addNbElecCharge(int nbElec) throws MoleculeCoherenceException  {
		setNbElecCharge(this.nbElecCharge + nbElec);
	}

	// begin yannick
	/**
	 * 
	 * @param iat
	 * @param jat
	 * @return
	 */
	public Bond areAdjacentAtoms(int iat, int jat) {
		for (HuckelBond bond : getHuckelBonds()) {
			int at1 = bond.getAtom1().getIndex();
			int at2 = bond.getAtom2().getIndex();
			if ((iat == at1 && jat == at2) || (iat == at2 && jat == at1)) {
				return bond;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param ibond
	 * @param jbond
	 * @return
	 */
	protected boolean areAdjacentBonds(int ibond, int jbond) {
		boolean rc = false;
		if (getHuckelBond(ibond).getAtom1().getSeqNum() == getHuckelBond(jbond)
				.getAtom1().getSeqNum()
				&& getHuckelBond(ibond).getAtom2().getSeqNum() == getHuckelBond(
						jbond).getAtom2().getSeqNum()) {
			// if the bonds are the same they are NOT adjacent
			return false;
		}
		rc = (rc || getHuckelBond(ibond).getAtom1().getSeqNum() == getHuckelBond(
				jbond).getAtom1().getSeqNum());
		rc = (rc || getHuckelBond(ibond).getAtom1().getSeqNum() == getHuckelBond(
				jbond).getAtom2().getSeqNum());
		rc = (rc || getHuckelBond(ibond).getAtom2().getSeqNum() == getHuckelBond(
				jbond).getAtom1().getSeqNum());
		rc = (rc || getHuckelBond(ibond).getAtom2().getSeqNum() == getHuckelBond(
				jbond).getAtom2().getSeqNum());
		return rc;
	}

	/**
	 * 
	 * @return
	 */
	public boolean areCorrectAtomsSeqNum() {
		for (HuckelAtom a : getHuckelAtoms()) {
			if (a.getSeqNum() == 0)
				return false;
		}
		return true;
	}

	/**
	 * this method tests if two atoms are linked
	 * 
	 * @param atom1 :
	 *            fist atom
	 * @param atom2 :
	 *            second atom
	 * @return boolean
	 */
	public boolean areLinked(Atom atom1, Atom atom2) {
		if ((atom1 == null) || (atom2 == null))
			return false;

		for (Bond l : this.lstBonds) {
			if (l.contains(atom1, atom2))
				return true;
		}

		return false;
	}

	/**
	 * 
	 * @param ibond
	 * @param jbond
	 * @return
	 */
	protected boolean areSecondNeighbour(int ibond, int jbond) {
		boolean rc = false;
		for (int kbond = 0; kbond < countBonds(); kbond++) {
			if (kbond != ibond && kbond != jbond) {
				rc = (areAdjacentBonds(ibond, kbond) && areAdjacentBonds(kbond,
						jbond));
				if (rc)
					break;
			}
		}
		return rc;
	}

	/**
	 * 
	 * @param ibond
	 * @param type
	 * @return
	 * @throws MoleculeCoherenceException
	 * @throws HuckelBondException
	 */
	public boolean changeBondType(int ibond, int type) throws  MoleculeCoherenceException, HuckelBondException {
		int ibondtype;
		if (this.getBond(ibond) instanceof HuckelBond)
			ibondtype = ((HuckelBond) this.getBond(ibond)).getBondType();
		else
			throw new MoleculeCoherenceException("the bond " + ibond
					+ " is not a HuckelBond");

		if (type == ibondtype) {
			return false;
		}

		setBondType(ibond, type);

		return true;
	}

	/**
	 * 
	 * @param iat
	 * @param jat
	 * @param type
	 * @return
	 * @throws HuckelBondException
	 * @throws MoleculeCoherenceException
	 */
	public int changeBondType(int iat, int jat, int type) throws HuckelBondException, MoleculeCoherenceException {

		int ibond = -1;

		for (HuckelBond bond : this.getHuckelBonds()) {
			if (((iat == bond.getAtom1().getSeqNum()) && (jat == bond
					.getAtom2().getSeqNum()))
					|| ((jat == bond.getAtom1().getSeqNum()) && (iat == bond
					.getAtom2().getSeqNum()))) {
				ibond = bond.getIndex() - 1;
				break;
			}
		}

		if (!changeBondType(ibond, type))
			return 1;

		return 0;
	}

	/**
	 * 
	 * @param path
	 * @return
	 */
	public boolean checkPath(ArrayList<HuckelBond> path) {
		boolean rc = true;
		for (HuckelBond currbond : path) {
			if (currbond.getAtom1().getNbElecPi()
					- currbond.getAtom1().getCharge() < 0) {
				return true;
			}
			if (currbond.getAtom2().getNbElecPi()
					- currbond.getAtom2().getCharge() < 0) {
				return true;
			}
		}
		return rc;
	}

	/**
	 * 
	 */
	protected void correctElecCharge() {
		int nbPiElec = getSumAtomsNbElecPi();
		int maxElecPossible = 2 * countHuckelAtoms() ;	

		if ((nbPiElec + getNbElecCharge()) > maxElecPossible)
			this.nbElecCharge = maxElecPossible - nbPiElec;

		if ((nbPiElec + getNbElecCharge()) < 0)
			this.nbElecCharge = nbPiElec - maxElecPossible;

	}

	public void correctHBonds() {
		for (HuckelAtom a : this.getHuckelAtoms()) {
			try {
				correctHBondAround(a);
				reindexAtoms();
				reindexBonds();
			} catch (HuckelBondException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BondException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MoleculeTooManyNeighboursException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MoleculeCoherenceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MoleculeBondExistsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	private boolean contains(Atom atom, double x, double y, double tolerance) {
		
		// Normalize the coordinates compared to the ellipse
		// having a center at 0,0 and a radius of 1.
		double width = tolerance;
		double normx = (x - atom.getX() ) / width ;
		
		double height = tolerance;
		double normy = (y - atom.getY()) / height ;
		return (normx * normx + normy * normy) < 1;
	}
	
	
	private Point3D getLocationHOnBisector(Atom a, Bond reference, double angle ) {
		double oppositeAngle = (deuxPI - angle) / 2;
		
		Point3D pRepere = reference.getAtomLinkedWith(a).getLocation();
		Point3D p = pRepere.rotateAndRenorm(a.getLocation(), Rotation.getRotationZ(oppositeAngle), LENGTH_H_BOND);
		
		return p;
	}
	
	
	public boolean isOccupiedZoneByAtom (Point3D p ){


		for (Atom atom  : this.getAtoms() ) {
			if (this.contains(atom, p.getX(), p.getY(), ATOM_ZONE))
				return true;
		}
		return false;
		
	}
	
	protected void correctHBondAround(Atom a) throws  HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {

		// cette fonction est utilise dans le cas ou on a remplace un atome par
		// un autre ou qu'on a lie un atome
		// Dans ce cas on doit corriger le nombre de liaison hydrogene de
		// maniere jusqu'au nombre de
		// maximum du nouvel atome ou des atomes lies : cela consite a ajouter les liaisons H manquantes ou enlever
		// des liaisons H en trop. Une exception est renvoyee si le nombre de
		// voisins non H est superieur au nombre de voisin maximum


		ArrayList<Bond> lstHBonds = new ArrayList<Bond>();
		ArrayList<Bond> lstNoHBonds = new ArrayList<Bond>();
		int nbHManquants = 0;
		Atom h;
		// debut parcourir les liaisons.
		// Si la liaison contient un H et l'atome a, ajouter liaisons dans
		// liste de liaison H
		// Si la liaison contient un atome <> H et l'atome a, incrementer nombre
		// de liaison non H
		// Si le nombre de liaison non H > nombre de voisins max : exception
		// trop de voisins
		// fin parcourir les liaisons
		for (Bond b : this.lstBonds) {
			if (b.contains(PeriodicTable.Element.H) && b.contains(a))
				lstHBonds.add(b);
			if ((!b.contains(PeriodicTable.Element.H)) && b.contains(a)) {
				lstNoHBonds.add(b);
			}
			if (lstNoHBonds.size() > a.getNbNeighboursMax())
				throw new MoleculeTooManyNeighboursException("too many neighbours");
		}
		nbHManquants = a.getNbNeighboursMax() - lstNoHBonds.size();

		// on supprime les atomes h ainsi que les liaisons h existants autour de l'atome dont on doit corriger le voisinage
		for (Bond b : lstHBonds) {
			h = b.getAtom(PeriodicTable.Element.H);
			this.lstAtoms.remove(h);
			h = null;
			do_removeBond(b); //
		}

		//  s'l manque des H autour de l'atome
		if (nbHManquants > 0) {

			// calcul de longueur liaison de reference
			
			double angle, greatestAngle;;
			Bond brepere;
			Point3D pRepere;
			// si l'atome a plus d'un voisin non H
			if (lstNoHBonds.size() > 1) {
				/*
				 * On veut trouver le plus grand angle forme par 3 atomes pour
				 * positionner un atome d'hydrogene sur la mediatrice. Soit trois
				 * points : C l'atome qu'on doit "entourer" d'hydrogenes, soit O
				 * l'atome Huckel lie a C dont on se sert de reference pour
				 * calculer l'angle avec les autres lisaisons. Soit D de coordonnees de
				 * l'atome Huckel lie a C dont on compare l'angle par rapport a
				 * O.
				 */
				Bond nouv = null;
				
				ArrayList<Bond> lstBonds = (ArrayList<Bond>) lstNoHBonds.clone();
				
				for (int i = 0; i < nbHManquants; i++) {
					angle = 0;
					greatestAngle = -999;
					brepere = lstBonds.get(0);
					VecteurCTOM vCO, vCD;
					 
					// on calcule le plus grand angle entre chaque liaison autour de l'atome. le plus grand angle est form� par 2 liaison, on en prend une comme repere
		
					for (Bond b1 : lstBonds) {
						vCO = new VecteurCTOM (a.getLocation(), b1.getAtomLinkedWith(a).getLocation());


						for (Bond b2 : lstBonds) {
							if ((b1 != b2)  ) {
								vCD = new VecteurCTOM(a.getLocation(),  b2.getAtomLinkedWith(a).getLocation());
								// angle  = sens de l'angle * angle vectoriel
								angle = Math.signum(vCO.getCrossProduct(vCD).getZ()) * vCO.getAngle(vCD);
								if ((angle > greatestAngle)  && !isOccupiedZoneByAtom(getLocationHOnBisector(a, b2, angle)) ) {
									greatestAngle = angle;
									brepere = b2;
									
								}
							}

						} // for b2
					} // for b1
					// on calcule la moitie l'angle oppose par rapport au plus grand angle pour avoir la position de l'hydrogene

					Point3D p = getLocationHOnBisector (a, brepere, greatestAngle);
					nouv = createBondNotNotify(a, createAtomNotNotify(
							PeriodicTable.Entry.H, p));
					
					lstBonds.add(nouv);
				} // for nombre de H manquants

				lstBonds.clear();
				lstBonds = null;
				// sinon : l'atome a 1 seul voisin non H ou zero
			} else {
				// si l'atome a 1 seul voisin non H
				if (lstNoHBonds.size() == 1) {
					// on se repere par rapport a l'unique liaison non H
					pRepere = lstNoHBonds.get(0).getLocation();
					
					// sinon, si l'atome est seul : pas de voisin non H
				} else /* (lstNoHBonds.size() == 0) */{
					pRepere = new Point3D (a.getX() + LENGTH_H_BOND, a.getY(), a.getZ());
		
				}
				// on place les atom h manquant autour de l'atome, qui est seul, selon des angles egaux
				for (int i = 1; i <= nbHManquants; i++) {
					// si l'atome est un O2 ou S2 alors il faut qu'il soit coude, on ajoute 120degres
					if ((a.getEntry() == Entry.O2) || (a.getEntry() == Entry.S2) || (a.getEntry() == Entry.N1) || (a.getEntry() == Entry.P1) )
						angle = deuxPI / (a.getNbNeighboursMax() * i + 1);	
					else 
						angle = deuxPI / a.getNbNeighboursMax() * i;						

					Point3D p = pRepere.rotateAndRenorm(a.getLocation(), Rotation.getRotationZ(angle), LENGTH_H_BOND); 
					createBondNotNotify(a, createAtomNotNotify(PeriodicTable.Entry.H, p));
					

				} // for
			} // else (lstNoHBonds.size() <= 1)
		} // if nombre de manquants > 0
		lstNoHBonds.clear();
		lstNoHBonds = null;
		lstHBonds.clear();
		lstHBonds = null;
	}

	/**
	 * 
	 * @param b
	 * @throws HuckelBondException
	 * @throws BondException
	 * @throws MoleculeTooManyNeighboursException
	 * @throws MoleculeCoherenceException
	 * @throws MoleculeBondExistsException
	 */
	protected void correctHBondRelativeTo(Bond b) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		if (b.getAtom1().getElement() != PeriodicTable.Element.H)
			correctHBondAround(b.getAtom1());
		if (b.getAtom2().getElement() != PeriodicTable.Element.H)
			correctHBondAround(b.getAtom2());
		reindexAtoms();
		reindexBonds();
	}

	/**
	 * this function returns the number of atoms in the molecule
	 * 
	 * @return int
	 */
	public int countAtoms() {
		if (this.lstAtoms == null)
			return 0;
		return this.lstAtoms.size();
	}

	/**
	 * this function returns the number of bonds in the molecule
	 * 
	 * @return int
	 */
	public int countBonds() {
		if (this.lstBonds == null)
			return 0;
		return this.lstBonds.size();
	}
	
	
	/**
	 * 
	 * @param iAtom
	 * @return
	 */
	protected int countDBaroundAtom(int iAtom) {
		int nDB = 0;
		Iterator itb = getBonds().iterator();
		HuckelBond l;
		while (itb.hasNext()) {
			l = (HuckelBond) itb.next();
			if (l.getBondType() == 2) {
				if ((l.getAtom1().getSeqNum() == iAtom)
						|| (l.getAtom2().getSeqNum() == iAtom)) {
					nDB++;
				}
			}
		}
		return nDB;
	}

	/**
	 * 
	 * @return
	 */
	public int countHuckelAtoms() {
		return this.getHuckelAtoms().size();
	}

	/**
	 * 
	 * @return
	 */
	public int countHuckelBonds() {
		return this.getHuckelBonds().size();
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public int countNeighbours(Atom a) {

		int nbNeighbours = 0;
		for (Bond b : this.lstBonds) {
			if (b.contains(a))
				nbNeighbours++;
		}
		return nbNeighbours;
	}
	/**
	 * 
	 * @param a
	 * @return
	 */
	public int countNeighbourHs(Atom a) {

		int nbNeighbourHs = 0;
		for (Bond b : this.lstBonds) {
			if (b.contains(a) && b.contains(PeriodicTable.Element.H))
			nbNeighbourHs++;
		}
		// correct for H neighbours
		if (a.getElement() ==PeriodicTable.Element.H)
			{nbNeighbourHs--;}
		return nbNeighbourHs;
	}

	
	
	/**
	 * This function returns the list of atoms linked with atom given in parameter 
	 * @param a
	 * @return
	 */
	public List<Atom> getNeighbours(Atom a) {
		List<Atom> lstAtoms = new ArrayList<Atom>();
		Atom neighbour;
		for (Bond b : this.lstBonds) {
			neighbour = b.getAtomLinkedWith(a); 
			if (neighbour != null)
				lstAtoms.add(neighbour);
				
		}
		return lstAtoms;
	}

	/**
	 * 
	 * @param a
	 * @return
	 */
	public int countNeighboursExcludingH(Atom a) {

		int nbNeighbours = 0;
		nbNeighbours = this.countNeighbours(a)-this.countNeighbourHs(a);
		return nbNeighbours;
	}

	/**
	 * this function count number of atoms that have a single electron (atomes mono occup�es)
	 * 
	 * @return int
	 */
	public int countSingleElectronsOnAtoms() {
		int nb = 0;
		for (HuckelAtom a : this.getHuckelAtoms()) {
			if (a.countRadR()==1)
				nb++;
		}

		return nb;
	}

	/**
	 * 
	 * @param entry
	 * @param p
	 * @return
	 * @throws HuckelBondException
	 * @throws BondException
	 * @throws MoleculeTooManyNeighboursException
	 * @throws MoleculeCoherenceException
	 * @throws MoleculeBondExistsException
	 */
	protected Atom createAtomNotNotify(PeriodicTable.Entry entry, Point3D p) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		Atom a = do_createNewAtom(entry, p);
		addAtomNotNotify(a);
		return a;
	}

	/**
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 * @throws HuckelBondException
	 * @throws BondException
	 * @throws MoleculeTooManyNeighboursException
	 * @throws MoleculeCoherenceException
	 * @throws MoleculeBondExistsException
	 */
	protected Bond createBondNotNotify(Atom a1, Atom a2) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		if (isNbNeighboursMaxReachedExcludingH(a1))
			throw new MoleculeTooManyNeighboursException("too many neighbours");
		if (isNbNeighboursMaxReachedExcludingH(a2))
			throw new MoleculeTooManyNeighboursException("too many neighbours");

		Bond b;
		if ((a1 instanceof HuckelAtom) && (a2 instanceof HuckelAtom))
			b = new HuckelBond((HuckelAtom)a1, (HuckelAtom)a2, 1);
		else
			b = new Bond(a1, a2);

		addBondNotNotify(b);
		return b;
	}
	

	/**
	 * 
	 * @throws GeometryException
	 */
	public void createGeometry() throws GeometryException {
		if (this.geometry != null) {
			try {
				throw new GeometryException(
						"The geometry object already exists in molecule");
			} catch (Exception e) {
				HuckelIO.warning(this.getClass().getName(), "createGeometry", e
						.getMessage(), e);
			}
		}
		HuckelIO.PrintIfln("CREATE GEOMETRY");
		this.geometry = new Geometry(this);
		return;
	}

	// yannick
	/**
	 * 
	 * @param geom
	 * @throws HuckelBondException
	 * @throws BondException
	 * @throws MoleculeTooManyNeighboursException
	 * @throws MoleculeCoherenceException
	 * @throws MoleculeBondExistsException
	 */
	public void createMolecule(Geometry geom) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {

		Atom atomCree = null;
		Bond bondCree = null;

		List<Atom> geom2d =  geom.getGeom();
		for (Atom iatom : geom2d ) {
			if (((iatom.getElement() != PeriodicTable.Element.H) && isAutoCorrectH())
					|| !isAutoCorrectH()) {
				atomCree = createAtomNotNotify(Geometry.atomToEntry(iatom),
						iatom.getLocation());
				atomCree.setLocationGeom3D((Point3D) iatom.getLocationGeom().clone());
				atomCree.setSuperSymbol(iatom.getSuperSymbol());
			}

		}

		for (Atom iatom : geom2d) {
			for (int i : iatom.getConnect()) {
				if (i > iatom.getSuperSymbol()) {
					try {
						Atom a1 = getAtomBySuperSymbol(iatom.getSuperSymbol());
						Atom a2 = getAtomBySuperSymbol(i);

						if (((a1 != null) && (a2 != null))
								&& ((((a1.getElement() == PeriodicTable.Element.H) || (a2
										.getElement() == PeriodicTable.Element.H)) && !isAutoCorrectH())
										|| isAutoCorrectH()) ) {
							bondCree = createBondNotNotify(a1, a2);
							if (isAutoCorrectH()) 
								correctHBondRelativeTo(bondCree);

						} else
							createBondNotNotify(a1, a2);

					} catch (Exception e) {
						HuckelIO.warning(this.getClass().getName(),
								"createMolecule", e.getMessage(), e);
					}
				}
			}
		}
		updateTopology();

	}

	/**
	 * 
	 * @param entry
	 * @param p
	 * @return
	 * @throws HuckelBondException
	 * @throws BondException
	 * @throws MoleculeTooManyNeighboursException
	 * @throws MoleculeCoherenceException
	 * @throws MoleculeBondExistsException
	 */
	public Atom createNewAtom(PeriodicTable.Entry entry, Point3D p) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		Atom a = do_createNewAtom(entry, p);
		addAtom(a);
		return a;
	}

	/**
	 * 
	 * @param a1
	 * @param a2
	 * @return
	 * @throws BondException
	 * @throws MoleculeTooManyNeighboursException
	 * @throws MoleculeCoherenceException 
	 * @throws MoleculeBondExistsException 
	 */
	public Bond createNewBond(Atom a1, Atom a2) throws BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {

		Bond b;

		if ((a1 instanceof HuckelAtom) && (a2 instanceof HuckelAtom))
			b = new HuckelBond((HuckelAtom)a1, (HuckelAtom)a2, 1);
		else
			b = new Bond(a1, a2);

		addBond(b);
		return b;
	}

	// begin yannick


	private Atom do_createNewAtom(PeriodicTable.Entry entry, Point3D p) {
		Atom a;
		a = PeriodicTable.getNewAtom(entry);
		a.setLocationNotNotify(p);
		return a;
	}

	public void debug() {

		HuckelIO.PrintIf("Molecule : ");
		HuckelIO.PrintIf("hascode = " + +hashCode());
		HuckelIO.PrintIf("number electrons charge : " + this.nbElecCharge);

		HuckelIO.PrintIf("*** Atoms ***");

		for (Atom a : this.lstAtoms)
			a.debug();

		HuckelIO.PrintIf("*** Bonds ***");

		for (Bond b : this.lstBonds) {
			b.debug();
		}

	}

	/**
	 * clear all atoms, bonds and bonds in the molecule
	 * and objects in the molecule
	 * @param notify
	 * 
	 */
	public void clear(boolean notify) {
		for (Atom a : lstAtoms){
			a.removeListener((IAtomValueListener)this);
			a.removeListener((IAtomGeometryListener)this);
			a = null;
		}

		for (Bond b : lstBonds){
			b.removeListener(this);
			b = null;
		}
		
		this.lstAtoms.clear();		
		this.lstBonds.clear();
		this.nbElecCharge = 0;
		if (notify)
			fireMoleculeDeleted(this, this, null);
	}

	
	/**
	 * clear all atoms, bonds and bonds in the molecule
	 * and objects in the molecule
	 * 
	 */
	public void clear() {
		clear(true);
	}
	// end yannick

	/**
	 * clear and set to null
	 * @param notify
	 */
	public void delete(boolean notify){
		this.clear(notify);
		this.lstAtoms=null;
		this.lstBonds=null;
	}
	
	/**
	 * clear and set to null
	 * @param notify
	 */
	public void delete(){
		this.delete(true);
	}
	
	@Deprecated
	protected ArrayList<HuckelBond> FindPath(int atomDebut, int atomFin) {
		ArrayList<HuckelBond> lstpath = new ArrayList<HuckelBond>();
		ArrayList<HuckelBond> lstbonds = new ArrayList<HuckelBond>();

		boolean chercher = true;
		boolean recommencer = true;
		boolean verbose = false;
		HuckelBond current = null;
		int idxAtomActif = 0;
		int niter = 0;
		int MaxIter = 100;

		while (chercher) {
			niter++;
			if (niter >= MaxIter) {
				HuckelIO.PrintIf("Trop d'iteration\n");
				return null;
			}
			if (recommencer) {
				// je cree la liste des liaisons
				lstbonds.clear();
				for (int ibond = 0; ibond < countHuckelBonds(); ibond++) {
					lstbonds.add(getHuckelBond(ibond));
				}

				// je melange la liste et j'initialise le chemin
				Collections.shuffle(lstbonds);

				lstpath.clear();

				// je recherche une liaisons qui implique l'atome de debut
				// je la prends comme la premiere liaison en debut de chemin
				// et je vire ce que je viens de prendre de la liste
				for (HuckelBond scanbond : lstbonds) {
					if (scanbond.getAtom1().getSeqNum() == atomDebut) {
						current = scanbond;
						idxAtomActif = scanbond.getAtom2().getSeqNum();
						// Si la liaison contient les atomes de debut et de fin
						// j'ai deja termine
						if (idxAtomActif == atomFin) {
							lstpath.add(current);
							return lstpath;
						}
						break;
					} else if (scanbond.getAtom2().getSeqNum() == atomDebut) {
						current = scanbond;
						idxAtomActif = scanbond.getAtom1().getSeqNum();
						// Si la liaison contient les atomes de debut et de fin
						// j'ai deja termine
						if (idxAtomActif == atomFin) {
							lstpath.add(current);
							return lstpath;
						}
						break;
					}
				}
				lstpath.add(current);
				lstbonds.remove(current);
				recommencer = false;
				// System.out.print("actif : "+idxAtomActif+"\n");
			}
			// je cherche une liaison adjacente a la precedente
			// et je la mets a la suite du chemin
			// en n'oubliant pas de la supprimer des liaisons dispo
			boolean found = false;
			for (HuckelBond scanbond : lstbonds) {
				if (scanbond.getAtom1().getSeqNum() == idxAtomActif) {
					current = scanbond;
					idxAtomActif = scanbond.getAtom2().getSeqNum();
					found = true;
					break;
				} else if (scanbond.getAtom2().getSeqNum() == idxAtomActif) {
					current = scanbond;
					idxAtomActif = scanbond.getAtom1().getSeqNum();
					found = true;
					break;
				}
			}
			if (found == false) {
				if (verbose) {
					System.out.print("!!! NOT FOUND !!!");
				}
			}
			lstpath.add(current);
			lstbonds.remove(current);

			// je dois verifier si
			// 1 je suis arrive a l'atome de fin
			if (current.getAtom1().getSeqNum() == atomFin
					|| current.getAtom2().getSeqNum() == atomFin) {
				chercher = false;
				HuckelIO.PrintIf("je suis au bout : ");
				for (int ibd = 0; ibd < lstpath.size(); ibd++) {
					HuckelIO.PrintIf("bond : "
							+ lstpath.get(ibd).getAtom1().getSeqNum() + " - "
							+ lstpath.get(ibd).getAtom2().getSeqNum() + " | ");
				}
				HuckelIO.PrintIf("\n");
			} else if (current.getAtom1().getSeqNum() == atomDebut
					|| current.getAtom2().getSeqNum() == atomDebut) {
				// 2 je suis revenu au debut auquel cas je recommence tout
				recommencer = true;
				if (verbose) {
					HuckelIO.PrintIf("je suis de retour, je recommence\n");
				}
				;
			} else {
				// 3 je suis dans une impasse auquel cas je recommence tout
				boolean impasse = true;
				for (HuckelBond bond : lstbonds) {
					if (bond.getAtom1().getSeqNum() == idxAtomActif
							|| bond.getAtom2().getSeqNum() == idxAtomActif) {
						impasse = false;
						break;
					}
				}
				if (impasse) {
					recommencer = true;
					if (verbose) {
						HuckelIO.PrintIf("impasse\n");
					}
					;
				}
			}
		}
		return lstpath;
	}
	/**
	 * this function returns the atom at the specified sequence number in the
	 * molecule
	 * 
	 * @return Atom
	 */
	public Atom getAtomByIndex(int index) {
		if ((index < 1) || (index > countAtoms())) {
			return null;
		}

		for (Atom a : this.lstAtoms) {
			if (a.getIndex() == index)
				return a;
		}

		return null;
	}


	public Atom getAtomBySeqNum(int seqNum) {
		if ((seqNum < 1) || (seqNum > countHuckelAtoms())) {
			return null;
		}

		for (HuckelAtom a : this.getHuckelAtoms()) {
			if (a.getSeqNum() == seqNum)
				return a;
		}

		return null;
	}

	public Atom getAtomBySuperSymbol(int seqNum) {
		if (seqNum < 1) {
			return null;
		}

		for (Atom a : this.lstAtoms) {
			if (a.getSuperSymbol() == seqNum)
				return a;
		}

		return null;
	}

	public IHuckelObject getHuckelObjectByIndex(Class classe, int index) {
		List <IHuckelObject>lstHuckelObjects  = this.getHuckelObjects();

		if (index < 1)  
			return null;

		for (IHuckelObject o : lstHuckelObjects) {
			if (o.getIndex() == index && (o.getClass()== classe))
				return o;
		}

		return null;
	}



	protected int getAtomConnectivity(int iAtom) {
		int nConn = 0;
		Iterator itb = getHuckelBonds().iterator();
		HuckelBond l;
		while (itb.hasNext()) {
			l = (HuckelBond) itb.next();
			if ((l.getAtom1().getSeqNum() == iAtom)
					|| (l.getAtom2().getSeqNum() == iAtom)) {
				nConn++;
			}
		}
		return nConn;
	}

	public int getAtomIdx(Atom atom) {
		int idx = 0;
		for (int i = 0; i < countAtoms(); i++) {
			if (this.lstAtoms.get(i) == atom) {
				idx = i;
				break;
			}
		}
		return idx;
	}

	public int getHuckelObjectIdx(IHuckelObject o) {
		List<IHuckelObject> lstHuckelObject = this.getHuckelObjects();
		int idx = 0;
		for (int i = 0; i <lstHuckelObject.size(); i++) {
			if (lstHuckelObject.get(i) == o) {
				idx = i;
				break;
			}
		}
		return idx;
	}


	/**
	 * This function return atoms which commposed the molecule
	 * 
	 * @return a list of the atoms
	 */
	public List<Atom> getAtoms() {

		return this.lstAtoms;
	}

	/**
	 * This function returns the bond corresponding to both atoms
	 * 
	 * @param atom1 :
	 *            fist atom
	 * @param atom2 :
	 *            second atom
	 * @return Bond : the found bond or null if atoms are not linked
	 */

	public Bond getBond(Atom a1, Atom a2) {

		if ((a1 == null) || (a1 == null))
			return null;

		for (Bond l : this.lstBonds) {
			if (l.contains(a1, a2))
				return l;
		}

		return null;
	}

	/**
	 * This function returns the bond at the specified coordinates in
	 * the molecule
	 * 
	 * @return Bond
	 */
	public Bond getBond(int idxBond) {
		return this.lstBonds.get(idxBond);
	}

	/**
	 * This function returns the bond at the specified sequence number in the
	 * molecule
	 * 
	 * @return bond
	 */
	public Bond getBondByIndex(int index) {
		if ((index < 1) || (index > countBonds())) {
			return null;
		}

		for (Bond b : this.lstBonds) {
			if (b.getIndex() == index)
				return b;
		}

		return null;
	}

	protected int getBondIdx(Bond current) {
		int idx = 0;
		for (int ibond = 0; ibond < countBonds(); ibond++) {
			if (getBond(ibond) == current) {
				idx = ibond;
				break;
			}
		}
		return idx;
	}

	// end yannick

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ctom.hulis.huckel.IMolecule#getBonds()
	 */
	public List<Bond> getBonds() {

		return this.lstBonds;
	}

	/**
	 * this function returns the co-ordinates of the center of the molecule
	 * 
	 * @return Point3D
	 */
	public Point3D getCenter() {
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;

		double nbAtomes = countAtoms();

		if (nbAtomes ==0)
			return new Point3D(0.0, 0.0, 0.0);

		for (Atom a : this.lstAtoms) {
			x = x + a.getX();
			y = y + a.getY();
			z = z + a.getZ();
		}

		return new Point3D(x / nbAtomes, y / nbAtomes, z /nbAtomes);


	}

	/**
	 * @return
	 * @throws GeometryException
	 */
	public Geometry getGeometry() throws GeometryException {
		if (this.geometry == null) {
			HuckelIO.PrintIfln("NO GEOM FOUND");
			createGeometry();
		}
		else {
			HuckelIO.PrintIfln("GEOM EXISTS");
			this.geometry.synchronize(this);
		}
		return this.geometry;
	}

	/**
	 * this function returns the atom at the specified sequence number in the
	 * molecule
	 * 
	 * @return Atom
	 */
	public HuckelAtom getHuckelAtomBySeqNum(int seqNum) {
		if ((seqNum < 1) || (seqNum > countHuckelAtoms())) {
			return null;
		}

		for (HuckelAtom a : this.getHuckelAtoms()) {
			if (a.getSeqNum() == seqNum)
				return a;
		}

		return null;
	}

	/**
	 * This function return the list og org.ctom.hulis.huckel atoms of the molecule
	 * 
	 * @return a list of the atoms
	 */
	public List<HuckelAtom> getHuckelAtoms() {
		List<HuckelAtom> lstHuckelAtoms = new ArrayList<HuckelAtom>();
		for (Atom a : this.getAtoms())
			if (a instanceof HuckelAtom)
				lstHuckelAtoms.add((HuckelAtom) a);
		return lstHuckelAtoms;
	}

	/**
	 * 
	 * @return
	 */
	public List<IHuckelObject> getHuckelObjects() {
		List<IHuckelObject> list = new ArrayList<IHuckelObject> ();
		list.addAll(this.getHuckelAtoms());
		list.addAll(this.getHuckelBonds());

		return list;
	}

	/**
	 * 
	 * @param idxBond
	 * @return
	 */
	public HuckelBond getHuckelBond(int idxBond) {
		HuckelBond l;
		l = this.getHuckelBonds().get(idxBond);
		return l;
	}

	/**
	 * 
	 * @return
	 */
	public List<HuckelBond> getHuckelBonds() {
		List<HuckelBond> lstHuckelBonds = new ArrayList<HuckelBond>();
		for (Bond b : this.getBonds())
			if (b instanceof HuckelBond)
				lstHuckelBonds.add((HuckelBond) b);
		return lstHuckelBonds;
	}

	/**
	 * 
	 * @return
	 */
	protected int getMaxAtomIndex() {
		int numOrdreMax = 0;
		int numOrdreCourant = 0;

		for (Atom a : this.lstAtoms) {
			numOrdreCourant = a.getIndex();

			if (numOrdreCourant > numOrdreMax) {
				numOrdreMax = numOrdreCourant;
			}
		}

		return numOrdreMax;
	}

	/**
	 *  get the maximal sequence number among the atoms
	 * 
	 * @return int
	 */

	public int getMaxAtomSeqNum() {
		int numOrdreMax = 0;
		int numOrdreCourant = 0;

		for (HuckelAtom a : this.getHuckelAtoms()) {
			numOrdreCourant = a.getSeqNum();

			if (numOrdreCourant > numOrdreMax) {
				numOrdreMax = numOrdreCourant;
			}
		}

		return numOrdreMax;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxAtomSuperSymbol() {
		int superSymbolMax = 0;
		int superSymbolCurrent = 0;

		for (Atom a : this.getAtoms()) {
			superSymbolCurrent = a.getSuperSymbol();

			if (superSymbolCurrent > superSymbolMax) {
				superSymbolMax = superSymbolCurrent;
			}
		}

		return superSymbolMax;
	}

	/**
	 *  get the maximal sequence number among the bonds
	 * 
	 * @return int
	 */

	protected int getMaxBondIndex() {
		int numOrdreMax = 0;
		int numOrdreCourant = 0;

		for (Bond b : this.lstBonds) {
			numOrdreCourant = b.getIndex();

			if (numOrdreCourant > numOrdreMax) {
				numOrdreMax = numOrdreCourant;
			}
		}

		return numOrdreMax;
	}

	/**
	 * this function returns the charge of the molecule (in number of electrons)
	 * 
	 * @return int
	 */
	public int getNbElecCharge() {
		return this.nbElecCharge;
	}

	/**
	 * getSumAtomNbElecPi : this function returns the sum of number of Pi
	 * electrons of the atoms of the molecule
	 * 
	 * @return double : sum of Pi electron of each atom in the molecule
	 */
	public int getSumAtomsNbElecPi() {
		int nbElecPi = 0;
		HuckelAtom a;
		Iterator i = getHuckelAtoms().iterator();
		while (i.hasNext()) {
			a = (HuckelAtom) i.next();
			nbElecPi += a.getNbElecPi();
		}

		return nbElecPi;
	}

	// addition 21 juin 2007
	public int[][] getTopology() {
		return this.topology;
	}
	
	public void updateTopology() {
		if (!this.areCorrectAtomsSeqNum())
			return; 
		
		int nAtoms = countHuckelAtoms();
		this.topology = new int[nAtoms][nAtoms];
//ne devrait pas etre necessaire YC
//		for (int i = 0; i < nAtoms; i++) {
//			for (int j = 0; j < nAtoms; j++) {
//				this.topology[i][j] = 0;
//			}
//		}
		for (HuckelBond bond : this.getHuckelBonds()) {
			int i = bond.getAtom1().getSeqNum() - 1;
			int j = bond.getAtom2().getSeqNum() - 1;
			this.topology[i][j] = 1;
			this.topology[j][i] = 1;
		}
	}


	/**
	 * 
	 * @return
	 */
	public double getWidth() {
		return Math.abs(this.getXMax() - this.getXMin());
	}

	/**
	 * 
	 * @return
	 */
	public double getHeight() {
		return Math.abs(this.getYMax() - this.getYMin());
	}


	/**
	 * this function returns the largest X-coordinate among the atoms
	 * 
	 * @return double
	 */
	public double getXMax() {

		double x = this.lstAtoms.get(0).getX();

		for (Atom a : this.lstAtoms) {
			if (a.getX() > x)
				x = a.getX();

		}

		return x;
	}

	/**
	 * this function returns the smallest X-coordinate among the atoms
	 * 
	 * @return double
	 */
	public double getXMin() {

		double x = this.lstAtoms.get(0).getX();

		for (Atom a : this.lstAtoms) {
			if (a.getX() < x)
				x = a.getX();
		}

		return x;
	}

	/**
	 * this function returns the largest Y-coordinate among the atoms
	 * 
	 * @return double
	 */
	public double getYMax() {
		double y = this.lstAtoms.get(0).getY();

		for (Atom a : this.lstAtoms) {
			if (a.getY() > y)
				y = a.getY();

		}

		return y;
	}

	/**
	 * this function returns the smallest Y-coordinate among the atoms
	 * 
	 * @return double
	 */
	public double getYMin() {
		double y = this.lstAtoms.get(0).getY();

		for (Atom a : this.lstAtoms) {
			if (a.getY() < y)
				y = a.getY();
		}

		return y;
	}

	public int[] getZpi() {
		int[] Zpi = new int[countHuckelAtoms()];
		for (int iat = 0; iat < countHuckelAtoms(); iat++) {
			Zpi[iat] = getHuckelAtomBySeqNum(iat + 1).getNbElecPi();
		}
		return Zpi;
	}

	// FIN addition 21 juin 2007

	public boolean isAutoCorrectH() {
		return this.autoCorrectH;
	}

	public boolean isEnabledNotify() {
		return this.enabledNotify;
	}

	public boolean isNbNeighboursMaxReachedExcludingH(Atom a) {
		int nbNoHBondAround = 0;
		for (Bond b : this.lstBonds) {
			if ((a.getElement() == PeriodicTable.Element.H) && b.contains(a))
				return true;
			if ((!b.contains(PeriodicTable.Element.H)) && b.contains(a))
				nbNoHBondAround++;
			if (nbNoHBondAround == a.getNbNeighboursMax())
				return true;
		}
		return false;
	}

	/**
	 * this methode move the molecule to the specified direction
	 * 
	 * @param dx :
	 *            direction on the x-axis
	 * @param dy :
	 *            direction on the y-axis
	 * @param dz :
	 *            direction on the z-axis
	 *            
	 */
	public void move(double dx, double dy, double dz) {

		for (Atom a : this.lstAtoms) {
			Point3D p = a.getLocation();
			a.setXNotNotify(p.getX() + dx);
			a.setYNotNotify(p.getY() + dy);
			a.setZNotNotify(p.getZ() + dz);
		}

		this.fireMoleculeMoved(this, null, null);
	}


	/*
	 * 
	 */
	protected void reindexAtoms() {
		// change the index numbers of the atoms
		int index = 1;
		for (Atom n : this.lstAtoms) {
			// if (n.getIndex() > a.getIndex()) {
			// n.index--;
			// }
			n.setIndex(index);
			index++;
		} // for (n : lstAtoms)
	}

	/*
	 * 
	 */
	protected void reindexBonds() {
		int index = 1;
		for (Bond i : this.lstBonds) {
			i.setIndex(index);
			index++;

		}


	}

	/**
	 * this method removes the bonds that were linked of the removed atom
	 * specified in parameter
	 * 
	 * @param atom :
	 *            removed atom
	 * @throws BondException 
	 * @throws HuckelBondException 
	 * @throws MoleculeTooManyNeighboursException 
	 * @throws MoleculeCoherenceException 
	 * @throws MoleculeBondExistsException 
	 */
	public void removeAtom(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		// impossible de supprimer un H si autocrrection de H
		if (this.isAutoCorrectH() && a.getElement() == PeriodicTable.Element.H)
			throw new MoleculeCoherenceException("cannot remove H atom when autoCorectH molecule option in true" );
		Atom old = a;
		do_removeAtom(a);
		updateCharges();
		updateTopology();
		correctElecCharge();
		this.fireMoleculeAtomRemoved(this, old, null);
	}


	protected void do_removeAtom(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {

		ArrayList<Atom> lstAtomHtoCorrect = new ArrayList<Atom>();
		Atom h;
		/*
		 * supprimer les liaisons autour de l'atome X a supprimer et les
		 * atomes H (si liaison X-H) Pour cela : rescencer les liaisons a
		 * supprimer, rescencer les atomes H a supprimer, rescencer les atomes
		 * autour desquels il faut ajouter des H
		 */
		Bond b = null;
		Iterator<Bond> iterator = this.lstBonds.iterator();
		while (iterator.hasNext()) {
			b= iterator.next();
			if (b.contains(a)) { /*
			 * si liaison qui contient l'atome a
			 * supprimer
			 */
				h = b.getAtom(PeriodicTable.Element.H); // recupere l'atome H lie a celui a supprimer ou null
				if ((h != null) && isAutoCorrectH()) { /* Si liaison avec un H et si on est 
				en mode auto-correction des H, supprimer les H  */
					this.lstAtoms.remove(h); 
					h = null;
				} else { /*
				 * Si l'atome lie n'est pas un H : marquer l'atome lie a comme
				 * etant un atome ayant des H a corriger
				 */
					lstAtomHtoCorrect.add(b.getAtomLinkedWith(a));

				} /* fin if */

				//suprime la liaison  contient l'atome qu'on supprime
				iterator.remove();

			} // fin if b contient a

		} // fin tant que boucle pas finie

		
		// si l'atome supprime est un atome huckel, alors baisser le numero de sequence
		// des atomes qui on un num de sequence superieur a celui supprime
		if (a instanceof HuckelAtom) {
			HuckelAtom a2 = (HuckelAtom) a;
			for (HuckelAtom n : this.getHuckelAtoms()) {
				if (n.getSeqNum() > a2.getSeqNum())
					n.seqNum--;
			} // for HuckelAtom
		} // if a instanceof HuckelAtom

		this.lstAtoms.remove(a);

		// corriger les hydrogenes autour des atomees marques
		if (isAutoCorrectH())
			for (Atom a2 : lstAtomHtoCorrect) {
				correctHBondAround(a2);
			}

		reindexAtoms();
		reindexBonds();



	}

	/**
	 * this method removes a specified bond in the basicMolecule
	 * 
	 * @param l :
	 *            removed bond
	 * @throws BondException 
	 * @throws HuckelBondException 
	 * @throws MoleculeTooManyNeighboursException 
	 * @throws MoleculeCoherenceException 
	 * @throws MoleculeBondExistsException 
	 */
	public void removeBond(Bond l) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		Bond old = l;
		do_removeBond(l);
		Atom a1 = l.getAtom1();
		Atom a2 = l.getAtom2();
		if (isAutoCorrectH()) {
			correctHBondAround(a1);
			correctHBondAround(a2);
		}
		reindexAtoms();
		reindexBonds();
		updateCharges();
		updateTopology();
		this.fireMoleculeBondRemoved(this, old, null);
		// l = null;
	}

	protected void do_removeBond (Bond b)  {
		this.lstBonds.remove(b);

	}

	/**
	 * this method changes an atom by antoher and it updates bonds.
	 * 
	 * @param a :
	 *            modified atom
	 * @param sigle :
	 *            type to be set
	 * @throws MoleculeTooManyNeighboursException 
	 * @throws MoleculeAtomNullException 
	 * @throws BondException 
	 * @throws HuckelBondException 
	 * @throws MoleculeCoherenceException 
	 * @throws MoleculeBondExistsException 
	 */

	public void replaceAtom(Atom old, Atom newAtom) throws HuckelBondException, BondException, MoleculeAtomNullException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException{
		do_replaceAtom(old, newAtom) ;
		this.fireMoleculeAtomReplaced(this, old, newAtom);

		old = null;
	} // replace atom

	
	/**
	 * 
	 */
	protected void updateCharges() {
		
	}
	/**
	 * 
	 * @param old
	 * @param newAtom
	 * @throws HuckelBondException
	 * @throws BondException
	 * @throws MoleculeAtomNullException 
	 * @throws MoleculeTooManyNeighboursException 
	 * @throws MoleculeCoherenceException 
	 * @throws MoleculeBondExistsException 
	 */
	protected  void do_replaceAtom(Atom old, Atom newAtom) throws  HuckelBondException, BondException, MoleculeAtomNullException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException{
		HuckelAtom huckelAtom;
		newAtom.setMoleculeContainer(this);
		newAtom.addListener((IAtomValueListener)this);
		newAtom.addListener((IAtomGeometryListener)this);
		Point3D oldXYZLocation = (Point3D) old.getLocationGeom().clone();
		newAtom.setLocationNotNotify(old.getLocation());
		newAtom.setLocationGeom3D(oldXYZLocation);
		newAtom.setIndex( old.getIndex());

		if ((newAtom == null) || (old == null))
			throw new MoleculeAtomNullException("atom must be initialized");

		int nbNotHBondAroundOld = 0;
		// compte le nombre de liaisons H autour de l'atome a remplacer, et
		// erreur si trop de voisin par rapport au nombre de voisins max du
		// nouvel atome
		for (Bond b : this.lstBonds) {
			if ((!b.contains(PeriodicTable.Element.H)) && b.contains(old))
				nbNotHBondAroundOld++;
			if (nbNotHBondAroundOld > newAtom.getNbNeighboursMax())
				throw new MoleculeTooManyNeighboursException("too many neighbours");
		}


		// si le nouvel atom est org.ctom.hulis.huckel et pas l'ancien, 
		// remplacer les liaisons non Huckel devant etre Huckel par des liaisons de org.ctom.hulis.huckel
		// sinon remplacer simplement l'atome par le nouveau dans les liaisons non org.ctom.hulis.huckel 
		HuckelBond hl;
		for ( int i= 0; i<  this.lstBonds.size(); i++ ) {
			Bond l = this.lstBonds.get(i);
			if (l.contains(old)) {
				if (!(old instanceof HuckelAtom)
						&& (newAtom instanceof HuckelAtom)) {
					hl = new HuckelBond((HuckelAtom)newAtom, (HuckelAtom) (l.getAtomLinkedWith(old)));
					hl.setMoleculeContainer(this);
					hl.addListener(this);
					l.removeListener(this);
					l = null;
					this.lstBonds.set(i, hl);
				} else { // si old est org.ctom.hulis.huckel et new non org.ctom.hulis.huckel ou old non org.ctom.hulis.huckel et new org.ctom.hulis.huckel 
					// -> liaison non org.ctom.hulis.huckel
					l.replaceAtom(old, newAtom);
				}
			}
		}

		// si on remplace un atome non org.ctom.hulis.huckel par un atome de org.ctom.hulis.huckel , lui ajouter un numero de sequence
		// sinon recuperer le num de sequence de celui remplace
		if (!(old instanceof HuckelAtom)&& (newAtom instanceof HuckelAtom)) {
			huckelAtom = (HuckelAtom) newAtom;
			huckelAtom.seqNum = getMaxAtomSeqNum() + 1;
		}
		else if ((old instanceof HuckelAtom)&& (newAtom instanceof HuckelAtom)) {
			huckelAtom = (HuckelAtom) newAtom;
			huckelAtom.seqNum = ((HuckelAtom) old).seqNum;

		}

		// remplacer l'ancien atome par le nouveau dans la liste des atomes
		for (int i = 0; i < this.lstAtoms.size(); i++) {
			if (this.lstAtoms.get(i) == old) {
				this.lstAtoms.set(i, newAtom);
				break;
			}
		} // fin remplacer ancien atome par nouveau dans la liste des atomes

		// corriger les hydrogene autour du nouvel atome et reindexer les composants
		if (isAutoCorrectH())
			correctHBondAround(newAtom);
		reindexAtoms();
		reindexBonds();
		updateCharges();
		updateTopology();
		
		correctElecCharge();

		old.removeListener((IAtomValueListener)this);
		old.removeListener((IAtomGeometryListener)this);
	}


	/**
	 * this method automatically puts all the sequence numbers of the atoms to
	 * zero
	 * 
	 * 
	 */
	public void resetSeqNumListHuckelAtomToZero() {

		for (HuckelAtom a : this.getHuckelAtoms()) {
			a.seqNum = 0;
		}
		this.fireMoleculeListHuckelAtomSeqNumReset(this, null, null);

	}

	/**
	 * this method rotates the molecule around its center
	 * 
	 * @param angle :
	 */

	public void rotate(Rotation mRot) {

		Point3D pCentre = getCenter();

		for (Atom a : this.lstAtoms) {
			Point3D oldLocationXYZ = (Point3D) a.getLocationGeom().clone();		
			a.rotate(pCentre, mRot);
			a.setLocationGeom3D(oldLocationXYZ);
		}
		
		this.fireMoleculeRotated(this, 0.0, mRot);

	}

	/**
	 * this method create automatically a sequence number for each atom in the
	 * molecule
	 * 
	 * 
	 */
	public void setAutoSeqNumListHuckelAtom() {
		int seqn = 1;

		resetSeqNumListHuckelAtomToZero();
		for (HuckelAtom a : this.getHuckelAtoms()) {
			a.seqNum = seqn;
			seqn++;
		}

		this.fireMoleculeListHuckelAtomSeqNumAutoSetted(this, null, null);


	}

	public void setAutoCorrectH(boolean autoCorrectH) {
		this.autoCorrectH = autoCorrectH;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.ctom.hulis.huckel.IMolecule#setBondType(int, int)
	 */
	public void setBondType(int idx, int bondType) throws MoleculeCoherenceException, HuckelBondException {
		if ((bondType < 1) || (bondType > HuckelBond.MAX_TYPE_BOND)) {
			throw new MoleculeCoherenceException("incorrect bond type");
		}
		if (idx < 0 || idx > countBonds())
			throw new MoleculeCoherenceException("wrong bond index");
		// int oldValue = bondType;

		Bond l = getBond(idx);
		if (!(l instanceof HuckelBond))
			throw new MoleculeCoherenceException("the bond" + idx + " is not a HuckelBond");
		HuckelBond l2 = (HuckelBond) l;
		l2.do_setBondType(bondType);

	}

	public void setEnabledNotify(boolean enabled) {
		this.enabledNotify = enabled;
	}

	
	public void setCharge (int charge) throws MoleculeCoherenceException {
		this.setNbElecCharge(-charge);
	}

	public int getCharge () {
		return -this.nbElecCharge;
	}



	/**
	 * this method sets electrons in the molecule for changing the net charge
	 * 
	 * @param nbElec :
	 *            number of electrons to add
	 * @throws MoleculeCoherenceException 
	 */
	public void setNbElecCharge(int nbElecCharge) throws MoleculeCoherenceException  {
		int nbElectTotal = nbElecCharge + getSumAtomsNbElecPi();
		int nbMaxElecPossible = (countHuckelAtoms() * 2);
		int oldValue = this.nbElecCharge;

		if ((nbElectTotal <= nbMaxElecPossible) && (nbElectTotal >= 0)) {
			this.nbElecCharge = nbElecCharge;

		} else if ((nbElectTotal > nbMaxElecPossible) || (nbElecCharge < 0)) {
			throw new MoleculeCoherenceException(
					"incorrect number of electrons (out of bounds)");
		}
		if (this.nbElecCharge != oldValue)
			fireMoleculeChargeChanged(this, oldValue,
					nbElecCharge);

	}

	protected void switchBondTypes(int ibond, int jbond) {
		int ibondtype, jbondtype;
		if (this.getBond(ibond) instanceof HuckelBond)
			ibondtype = ((HuckelBond) this.getBond(ibond)).getBondType();
		else
			return;

		if (this.getBond(jbond) instanceof HuckelBond)
			jbondtype = ((HuckelBond) this.getBond(jbond)).getBondType();
		else
			return;

		if (ibondtype == jbondtype) {
			return;
		}

		int tmpType = ibondtype;

		try {
			changeBondType(ibond, jbondtype);
			changeBondType(jbond, tmpType);
		} catch (Exception e) {
			HuckelIO.warning(this.getClass().getName(), "switchBondTypes", e
					.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		String res = "elec charge : " + this.nbElecCharge;

		return res;

	}

	
	public OrbitaleAtomique[] getOAs() {
		List<HuckelAtom> list = this.getHuckelAtoms();
		Collections.sort(list, new SeqNumComparator());
		int size=list.size();
		OrbitaleAtomique[] OAs = new OrbitaleAtomique[size];
		int n=0;
		for (HuckelAtom a : list) {
			OAs[n]=new OrbitaleAtomique(a ,new Energy(a.getHx(),0.0 ));
			n++;
		}
		return OAs;
	}


	/**
	 * 
	 * @param listener
	 */
	public void addListener(IMoleculeValueListener listener) {
		this.valueListeners.add((IMoleculeValueListener) listener);
	}
	
	/**
	 * 
	 * @param listener
	 */
	public void addListener(IMoleculeGeometryListener listener) {
		this.geometryListeners.add((IMoleculeGeometryListener) listener);

	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		// TODO Auto-generated method stub
		this.fireHuckelBondBondTypeChanged(e);

	}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		// TODO Auto-generated method stub
		this.fireHuckelBondHxyChanged(e);

	}

	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub
		this.fireHuckelAtomHxChanged(e);

	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub
		this.fireHuckelAtomRadRChanged(e);


	}





	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub

		this.fireHuckelAtomSeqNumChanged(e);

	}

	//@Override
	public void atomLocationChanged(GeometryEvent event) {
		this.fireAtomLocationChanged(event);

	}

	public ArrayList<IMoleculeValueListener> getValueListeners() {
		return (ArrayList<IMoleculeValueListener> ) this.valueListeners.clone();
	}

	public ArrayList<IMoleculeGeometryListener> getGeometryListeners() {
		return (ArrayList<IMoleculeGeometryListener> ) this.geometryListeners.clone();
	}

	/**
	 * 
	 * @param e
	 */
	protected void fireHuckelAtomHxChanged(HuckelAtomEvent e) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		for (IMoleculeValueListener listener : this.getValueListeners()) {
	
			listener.huckelAtomHxChanged(e);
		}	
	}
	
	protected void fireHuckelAtomSeqNumChanged(HuckelAtomEvent e) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		for (IMoleculeValueListener listener :  this.getValueListeners()) {
	
			listener.huckelAtomSeqNumChanged(e);
		}	
	}
	
	protected void fireAtomLocationChanged(GeometryEvent e) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		for (IMoleculeGeometryListener listener :  this.getGeometryListeners()) {
	
			listener.atomLocationChanged(e);
		}	
	}
	
	protected void fireHuckelAtomRadRChanged(HuckelAtomEvent e) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
	
		for (IMoleculeValueListener listener :  this.getValueListeners()) {
	
			listener.huckelAtomRadRChanged(e);
		}	
	}
	
	protected void fireHuckelBondHxyChanged(HuckelBondEvent e) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
		for (IMoleculeValueListener listener :this.getValueListeners()) {
	
			listener.huckelBondHxyChanged(e);
		}	
	}
	
	
	protected void fireHuckelBondBondTypeChanged(HuckelBondEvent e) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		for (IMoleculeValueListener listener : this.getValueListeners()) {
	
			listener.huckelBondBondTypeChanged(e);
		}	
	}
	
	
	
	protected void fireMoleculeMoved(Molecule source, 
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("FireMoleculeChanged : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		GeometryEvent event = null;
	
		for (IMoleculeGeometryListener listener : this.getGeometryListeners()) {
			if (event == null)
				event = new GeometryEvent(source, oldValue, newValue);
			listener.moleculeMoved(event);
		}	
	}
	
	protected void fireMoleculeRotated(Molecule source, 
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		GeometryEvent event = null;
	
		for (IMoleculeGeometryListener listener : this.getGeometryListeners()) {
			if (event == null)
				event = new GeometryEvent(source, oldValue, newValue);
			listener.moleculeRotated(event);
		}	
	}
	
	
	protected void fireMoleculeAtomRemoved(Molecule source, 
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		MoleculeEvent event = null;
	
		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeAtomRemoved(event);
		}	
	}
	
	protected void fireMoleculeBondAdded(Molecule source,
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		MoleculeEvent event = null;
	
		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeBondAdded(event);
		}	
	}
	
	
	protected void fireMoleculeDeleted(Molecule source, Object oldValue, Object newValue) {
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false\n");
			return;
		}
	
		MoleculeEvent event = null;
		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeDeleted(event);
		}
	}
	
	
	/**
	 * 
	 * @param listener
	 */
	public void addListener(IMoleculeListener listener) {
		if ( listener instanceof IMoleculeGeometryListener)
			this.geometryListeners.add((IMoleculeGeometryListener) listener);
		if ( listener instanceof IMoleculeValueListener)
			this.valueListeners.add((IMoleculeValueListener) listener);
	}
	
	
	public void removeListener(IMoleculeListener listener) {
		if ( listener instanceof IMoleculeGeometryListener)
			this.geometryListeners.remove((IMoleculeGeometryListener) listener);
		if ( listener instanceof IMoleculeValueListener)
			this.valueListeners.remove((IMoleculeValueListener) listener);
	}
	
	public void removeListener(IMoleculeValueListener listener) {
		this.valueListeners.remove(listener);
	}
	
	public void removeListener(IMoleculeGeometryListener listener) {
		this.geometryListeners.remove(listener);
	}
	
	public void removeAllListeners() {
		this.valueListeners.clear();
		this.geometryListeners.clear();
	}
	
	protected void fireMoleculeBondRemoved(Molecule source,
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		MoleculeEvent event = null;
	
		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeBondRemoved(event);
		}	
	}
	
	protected void fireMoleculeAtomReplaced(Molecule source,
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		MoleculeEvent event = null;
	
		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeAtomReplaced(event);
		}	
	}
	
	protected void fireMoleculeChargeChanged(Molecule source, 
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		MoleculeEvent event = null;
		ArrayList<IMoleculeValueListener> lst = this.getValueListeners();
		for (IMoleculeValueListener listener : lst) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeChargeChanged(event);
		}	
	}
	
	protected void fireMoleculeListHuckelAtomSeqNumAutoSetted(Molecule source, 
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		MoleculeEvent event = null;
	
		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeListHuckelAtomSeqNumAutoSetted(event);
		}	
	}
	
	protected void fireMoleculeListHuckelAtomSeqNumReset(Molecule source, 
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		MoleculeEvent event = null;
	
		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeListHuckelAtomSeqNumReset(event);
		}	
	}
	
	protected void fireMoleculeAtomAdded(Molecule source, 
			Object oldValue, Object newValue) {
	
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}
	
		MoleculeEvent event = null;
	
		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeAtomAdded(event);
		}	
	}
}
