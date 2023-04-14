package org.ctom.hulis.huckel;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.exception.BlocDelocalizedException;
import org.ctom.hulis.huckel.listeners.IBlocDelocalizedListener;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;


/****
 * This class describe a delocalized bloc in a structure
 * @author CTOM
 *
 */
public class BlocDelocalized extends HuckelBlock implements Serializable {
	
	private static final long serialVersionUID = 2866139443601131754L;
	
	Molecule moleculeParent; // structure wich owns the bloc
	protected final List<IBlocDelocalizedListener> listeners;

	/**
	 * Constructs a bloc
	 */
	public BlocDelocalized () {
		super();
		this.moleculeParent=null;
		listeners = new ArrayList<IBlocDelocalizedListener> ();

	}
	
	/**
	 * 
	 * @param huckelObjects
	 * @param nElectrons
	 * @throws BlocDelocalizedException
	 */
	public BlocDelocalized(List<IHuckelObject> huckelObjects, int nElectrons) throws BlocDelocalizedException {
		this();
		for (IHuckelObject o : huckelObjects) {
			this.addHuckelObject(o);
		}
		this.setNbElectron(nElectrons);
	}
	
	/**
	 * 
	 * @param bloc
	 * @param target
	 */
	private BlocDelocalized(BlocDelocalized bloc, StructureLocalized target) {
		// TODO Ce n'est pas propre : on ne devrat pas accerder au membres de la classe parent ici
		this.lstHuckelObject = new ArrayList<IHuckelObject>();
		listeners = new ArrayList<IBlocDelocalizedListener> ();
		this.moleculeParent = null;
		for (IHuckelObject o : bloc.getListHuckelObject()) {
			IHuckelObject otarget=target.getHuckelObjectByIndex(o.getClass(), o.getIndex());
			this.lstHuckelObject.add(otarget);
			otarget.setBlocDelocalizedParent(this);
		}
		this.nbElectrons=bloc.nbElectrons;	
	}
	


	/**
	 * @param target :
	 * @return
	 */
	public BlocDelocalized getCopy(StructureLocalized target) {
		return new BlocDelocalized(this, target);
	}


	
	/**
	 * Add a huckel bond or atom in the bloc
	 * 
	 * If a bond is added in a block, the both linked atom are added too. If one of the two atoms
	 * owns to the bloc, or the bond already owns to the bloc, then an exception is thrown
	 * 
	 * @param o
	 * @throws BlocDelocalizedException 
	 */
	public void addHuckelObject(IHuckelObject o) throws BlocDelocalizedException {
		// si l'objet a ajoute est une liaison alors ajoute au bloc les atomes lies, exception si un des atomes appartient
		// deja a une bloc
		if (o==null)
			throw new BlocDelocalizedException("cannot add a null object in a block");
		if (o.getBlocDelocalizedParent() != null)
			throw new BlocDelocalizedException(o + " is already in a block");
	
		if (o instanceof HuckelBond) {
			HuckelBond b = (HuckelBond) o;
			if (b.getBondType()>1)
				throw new BlocDelocalizedException("b " + b + " has more than 1 electron");
			HuckelAtom a1 = (HuckelAtom) b.getAtom1();
			HuckelAtom a2 = (HuckelAtom) b.getAtom2();
			
			if (a1.getNbElectrons() > 0) 
				throw new BlocDelocalizedException("cannot create bloc on " + a1 + " : " + a1.getNbElectrons() +  " electrons instead of 0");
			else if (a1.countDBondAtom() > 0)
				throw new BlocDelocalizedException("cannot create bloc on " + a1 + " : " + a1.countDBondAtom() +  " : double bonds detected");
			else if (a1.getBlocDelocalizedParent() == null) {
				lstHuckelObject.add(a1);
				a1.setBlocDelocalizedParent(this);
			}
			else if (a1.getBlocDelocalizedParent() != this)
				throw new BlocDelocalizedException("atom " + a1 + " is already in a block");
			
			if (a2.getNbElectrons() > 0) 
				throw new BlocDelocalizedException("cannot create bloc on " + a2 + " : " + a2.getNbElectrons() +  " electrons instead of 0");
			else if (a2.countDBondAtom() > 0)
				throw new BlocDelocalizedException("cannot create bloc on " + a2 + " : " + a2.countDBondAtom() +  " : double bonds detected");
			if (a2.getBlocDelocalizedParent() == null) {
				lstHuckelObject.add(a2);
				a2.setBlocDelocalizedParent(this);
			}
			else if (a2.getBlocDelocalizedParent() != this)
				throw new BlocDelocalizedException("atom " + a2 + " is already in a block");
		
					
		}
		else {
			// si atome
			if (o.getNbElectrons() > 0)
				throw new BlocDelocalizedException("cannot create bloc on " + o + " : " + o.getNbElectrons() +  " electrons instead of 0" );
			else if (((Atom) o).countDBondAtom() > 0)
				throw new BlocDelocalizedException("cannot create bloc on " + o + " : " + ((Atom) o).countDBondAtom() + " double bonds detected");
		}
		
		lstHuckelObject.add(o);
		o.setBlocDelocalizedParent(this);
								
		this.nbElectrons= this.getSumAtomsNbElecPi();	

		
		this.fireHuckelObjectAdded(this, null, o);
	
	}
	
	
	
	/**
	 * Remove object of the bloc
	 * @param o
	 */
	public void removeHuckelObject(IHuckelObject o) {
		do_removeHuckelObject(o);
		this.nbElectrons= this.getSumAtomsNbElecPi();	
		this.fireHuckelObjectRemoved(this, o, null);

	}
	
	/**
	 * 
	 * @param o
	 */
	protected void do_removeHuckelObject(IHuckelObject o){
		// si l'objet a supprimer est une liaison alors supprime du bloc les atomes lies, exception si un des atomes appartient
		// deja a une bloc
		// si on enleve un atome d'un bloc, il faut enlever du meme bloc les liaisons adjacentes
		if (o instanceof Atom) {
			Atom a = (Atom) o;
			for (Bond b : a.getBonds()) {
				if (b instanceof HuckelBond) {
					HuckelBond hb = (HuckelBond) b;
					for (IHuckelObject huckelObject: this.getListHuckelObject() ) {
						if (huckelObject == hb) {
							lstHuckelObject.remove(hb);
							hb.setBlocDelocalizedParent(null);
						}
					}
				}
			}
		}		
	    else if (o instanceof HuckelBond) {
			HuckelBond b = (HuckelBond) o;
			HuckelAtom a1 = (HuckelAtom) b.getAtom1();
			HuckelAtom a2 = (HuckelAtom) b.getAtom2();
			
			// supprume les atomes afferents à la liaison du bloc s'ils ne sont pas liés a un auautre atome du bloc
			List<HuckelBond> lstHuckelBonds=this.getListHuckelBond();
			Iterator<HuckelBond> it = lstHuckelBonds.iterator();
			boolean found=false;
			while (it.hasNext() && !found) {
				HuckelBond current= it.next();
				if  ((current != b) && b.contains(a1)  )
					found = true;
				
			}
			if (!found) {
				lstHuckelObject.remove(a1);
				a1.setBlocDelocalizedParent(null);
			}
			
			found = false;
			while (it.hasNext() && !found) {
				HuckelBond current= it.next();
				if  ((current != b) && b.contains(a2)  )
					found = true;
				
			}
			if (!found) {
				lstHuckelObject.remove(a2);
				a2.setBlocDelocalizedParent(null);
			}
	

		}
		
		lstHuckelObject.remove(o);
		o.setBlocDelocalizedParent(null);		
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
		
		super.setNbElectron(nbElectrons);
		
		if (old != this.nbElectrons)
			this.fireNbElectronsChanged(this, old, this.nbElectrons);

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
	}
	
	
	/**
	 * empties the list of object of the bloc
	 * TODO a verifier
	 */
	public void empty () {
		for (IHuckelObject huckelObject : this.lstHuckelObject)
			huckelObject.setBlocDelocalizedParent(null);
		this.lstHuckelObject.clear();		
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

	
	

}
