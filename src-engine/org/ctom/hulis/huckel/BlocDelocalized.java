package org.ctom.hulis.huckel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.exception.BlocDelocalizedException;
import org.ctom.hulis.huckel.listeners.IBlocDelocalizedListener;
import org.ctom.hulis.huckel.structures.StructureDelocalized;


/****
 * This class describe a delocalized bloc in a structure
 * @author CTOM
 *
 */
public class BlocDelocalized implements Serializable {
	ArrayList<IHuckelObject> lstHuckelObject; // objects org.ctom.hulis.huckel in the bloc 
	int nbElectrons; // number of electrons of the bloc
	Molecule moleculeParent; // structure wich owns the bloc
	protected final List<IBlocDelocalizedListener> listeners;
	
	protected void do_addHuckelObject(IHuckelObject o){
		lstHuckelObject.add(o);
		o.setBlocDelocalizedParent(this);

	}

	
	protected void do_removeHuckelObject(IHuckelObject o){
		lstHuckelObject.remove(o);
		o.setBlocDelocalizedParent(null);

	}

	
	/**
	 * Constructs a bloc
	 */
	public BlocDelocalized () {
		this.lstHuckelObject = new ArrayList<IHuckelObject>();
		this.nbElectrons = 0;
		listeners = new ArrayList<IBlocDelocalizedListener> ();
	}


	/**
	 * Adds an org.ctom.hulis.huckel bond or atom in the bloc
	 * 
	 * If a bond is added, the boh linked atom are added. If one of the two atoms
	 * owns to the bond, an exception is thrown
	 * 
	 * @param o
	 * @throws BlocDelocalizedException 
	 */
	public void addHuckelObject(IHuckelObject o) throws BlocDelocalizedException {
		Mesomery mesomery;
		if ((this.getMoleculeParent() instanceof StructureDelocalized)
			&& ( ( mesomery = ((StructureDelocalized) this.getMoleculeParent() ).getMesomeryParent())!= null)
			&& mesomery.countLocalizedStructs()>0) 
			throw new BlocDelocalizedException ("cannot add an object to the bloc because there are localized structures in the mesomery");

		
		if (o.getBlocDelocalizedParent() != null)
			throw new BlocDelocalizedException ("this bond or this atom already own to a bloc");
		
		if (o.getNbRadR() > 0)
			throw new BlocDelocalizedException ("Cannot add to a bloc : this bond or this atom has got electrons" );
		
		// si l'objet a ajoute est une liaison alors ajoute au bloc les atomes lies, exception si un des atomes appartient
		// deja a une bloc
		if (o instanceof HuckelBond) {
			HuckelBond b = (HuckelBond) o;
			HuckelAtom a1 = (HuckelAtom) b.getAtom1();
			HuckelAtom a2 = (HuckelAtom) b.getAtom2();
			if  ( (a1.getBlocDelocalizedParent() != null && a1.getBlocDelocalizedParent() != this)||
					(a2.getBlocDelocalizedParent() != null && a2.getBlocDelocalizedParent() != this) )
				throw new BlocDelocalizedException ("Cannot add the bond " + b.toString() + " in the bloc because one of the two linked atoms owns to another bloc");
			
			if (a1.getBlocDelocalizedParent() == null) 
				do_addHuckelObject(a1);
				

			if (a2.getBlocDelocalizedParent() == null)
				do_addHuckelObject(a2);
		
		}
		
		do_addHuckelObject(o);
		
		this.fireHuckelObjectAdded(this, null, o);

	}
	
	/**
	 * 
	 * @return number of objects of the bloc
	 */
	public int countHuckelObjects() {
		return lstHuckelObject.size();
	}
	
	/**
	 * Remove object of the bloc
	 * @param o
	 */
	public void removeHuckelObject(IHuckelObject o) {
		Mesomery mesomery;
		if ((this.getMoleculeParent() instanceof StructureDelocalized)
			&& ( ( mesomery = ((StructureDelocalized) this.getMoleculeParent() ).getMesomeryParent())!= null)
			&& mesomery.countLocalizedStructs()>0) 
			throw new RuntimeException ("cannot remove an object to the bloc because there are localized structures in the mesomery");

		
		// si on enleve un atome d'un bloc, il faut enlever du meme bloc les liaisons adjacentes
		if (o instanceof Atom) {
			Atom a = (Atom) o;
			for (Bond b : a.getBonds()) {
				if (b instanceof HuckelBond) {
					HuckelBond hb = (HuckelBond) b;
					if (hb.getBlocDelocalizedParent() == this)
						do_removeHuckelObject(hb);
				}
			}
		}
		
		do_removeHuckelObject(o);

		this.fireHuckelObjectRemoved(this, o, null);

	}

	
	/**
	 * Sets the number of electon in the bloc (charge)
	 * @param nbElectrons
	 */
	
	public void setNbElectron(int nbElectrons) {
		Mesomery mesomery;
		if ((this.getMoleculeParent() instanceof StructureDelocalized)
			&& ( ( mesomery = ((StructureDelocalized) this.getMoleculeParent() ).getMesomeryParent())!= null)
			&& mesomery.countLocalizedStructs()>0) 
			throw new RuntimeException ("cannot change electrons of the bloc there are localized structures in the mesomery");


		int old = this.getNbElectron();
		
		this.nbElectrons = nbElectrons;
		
		if (old != this.nbElectrons)
			this.fireNbElectronsChanged(this, old, this.nbElectrons);

	}

	/**
	 * number of electrons in the bloc
	 * @return
	 */
	public int getNbElectron() {
		return this.nbElectrons ;
		
	}
	
	/**
	 * tests if the bloc contains the org.ctom.hulis.huckel object in parameter
	 * @param o
	 * @return
	 */
	public boolean containsHuckelObject(IHuckelObject o) {
		return lstHuckelObject.contains(o);
		
	}
	
	/***
	 * destroy bloc contents
	 */
	public void clear () {
		Mesomery mesomery;
		if ((this.getMoleculeParent() instanceof StructureDelocalized)
			&& ( ( mesomery = ((StructureDelocalized) this.getMoleculeParent() ).getMesomeryParent())!= null)
			&& mesomery.countLocalizedStructs()>0) 
			throw new RuntimeException ("cannot clear the bloc because there are localized structures in the mesomery");

		
		for (IHuckelObject huckelObject : this.lstHuckelObject)
			huckelObject.setBlocDelocalizedParent(null);
		this.lstHuckelObject.clear();
		this.moleculeParent = null;
		this.lstHuckelObject = null;
		
	}

	/**
	 * getSumAtomNbElecPi : this function returns the sum of number of Pi
	 * electrons of the atoms of the bloc
	 * 
	 * @return double : sum of Pi electron of each atom in the bloc
	 */
	public int getSumAtomsNbElecPi() {
		int nbElecPi = 0;
			
		for (IHuckelObject o : this.lstHuckelObject) {
			if (o instanceof HuckelAtom)
			nbElecPi += ((HuckelAtom) o).getNbElecPi();
		}

		return nbElecPi;
	}
	
	/**
	 * empties the list of object of the bloc
	 */
	public void empty () {
		for (IHuckelObject huckelObject : this.lstHuckelObject)
			huckelObject.setBlocDelocalizedParent(null);
		this.lstHuckelObject.clear();		
	}
	
	/**
	 * attach the bloc to a structure
	 * @param moleculeParent
	 */
	public void setMoleculeParent (Molecule moleculeParent) {
		this.moleculeParent = moleculeParent;
		
	}

	/**
	 * 
	 * @return the structure attached to the bloc
	 */
	public Molecule getMoleculeParent () {
		return this.moleculeParent;
		
	}
	
	protected void fireHuckelObjectAdded(BlocDelocalized source, Object oldValue, Object newValue) {
		BlocDelocalizedEvent event = null;
		for (IBlocDelocalizedListener listener : this.listeners) {
	
			if (event == null)
				event = new BlocDelocalizedEvent(source, oldValue, newValue);
					listener.blocDelocalizedHuckelObjectAdded(event);
		}	
	}

	protected void fireHuckelObjectRemoved(BlocDelocalized source, Object oldValue, Object newValue) {
		BlocDelocalizedEvent event = null;
		for (IBlocDelocalizedListener listener :  this.listeners) {
	
			if (event == null)
				event = new BlocDelocalizedEvent(source, oldValue, newValue);
					listener.blocDelocalizedHuckelObjectRemoved(event);
		}	
	}

	protected void fireNbElectronsChanged(BlocDelocalized source, Object oldValue, Object newValue) {
		BlocDelocalizedEvent event = null;
		for (IBlocDelocalizedListener listener : this.listeners) {
	
			if (event == null)
				event = new BlocDelocalizedEvent(source, oldValue, newValue);
					listener.blocDelocalizedNbElectronsChanged(event);
		}	
	}

	
	public void addListener(IBlocDelocalizedListener listener) {
		this.listeners.add(listener);
	}

	public void removeListener(IBlocDelocalizedListener listener) {
		this.listeners.remove(listener);
	}

	public List<IHuckelObject> getListHuckelObject() {
		return lstHuckelObject;
	}
	
	public List<HuckelAtom> getListHuckelAtom() {
		List<HuckelAtom> lstHuckelAtom = new ArrayList<HuckelAtom>(); 
		for (IHuckelObject o : this.getListHuckelObject() )
			if (o instanceof HuckelAtom)
				lstHuckelAtom.add((HuckelAtom)o);
			
			return lstHuckelAtom;
	}

	public List<HuckelBond> getListHuckelBond() {
		List<HuckelBond> lstHuckelBond = new ArrayList<HuckelBond>(); 
		for (IHuckelObject o : this.getListHuckelObject() )
			if (o instanceof HuckelBond)
				lstHuckelBond.add((HuckelBond)o);
			
			return lstHuckelBond;
	}

	/**
	 * Compare two blocs
	 * @param bloc
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (!(o instanceof BlocDelocalized))
			throw new ClassCastException();
		
		BlocDelocalized bloc = (BlocDelocalized) o;
		
		// deux blocs sont egaux s'ils possedent  memes atomes
		List<HuckelAtom>  lstHuckelAtom = getListHuckelAtom();
		List<HuckelAtom>  lstHuckelAtomB = bloc.getListHuckelAtom();
		
		List<HuckelBond>  lstHuckelBond = getListHuckelBond();
		List<HuckelBond>  lstHuckelBondB = bloc.getListHuckelBond();

		if (lstHuckelAtom.size() != lstHuckelAtomB.size())
				return false;
		
		if (lstHuckelBond.size() != lstHuckelBondB.size())
			return false;
	
		for (HuckelAtom a : lstHuckelAtom ) {
			boolean ok = false;
			for (HuckelAtom a2 : lstHuckelAtomB )
				if (a.equals(a2) ){
					ok = true;
					break;
				}
			if (!ok)
				return false;
		}

		
		for (HuckelBond b : lstHuckelBond ) {
			boolean ok = false;
			for (HuckelBond b2 : lstHuckelBondB )
				if (b.equals(b2)) {
					ok = true;
					break;
				}
			if (!ok)
				return false;
		}

		
		return true;
	} // equals
	
	private volatile int hashCode = 0;

	@Override
    public int hashCode () {
        final int multiplier = 23;
        if (hashCode == 0) {
            int code = 133;
            if (this.lstHuckelObject != null)
            	code = multiplier * code +  this.lstHuckelObject.hashCode();
    	   hashCode = code;
        }
        return hashCode;
	
	}

}
