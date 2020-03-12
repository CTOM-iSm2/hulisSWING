package org.ctom.hulis.huckel.adapters;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.Event;
import org.ctom.hulis.huckel.events.GeometryEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.HuckelEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.listeners.IMoleculeGeometryListener;
import org.ctom.hulis.huckel.listeners.IMoleculeValueListener;

public abstract class MoleculeAdapter implements IMoleculeValueListener,  IMoleculeGeometryListener  {
	
	Molecule molecule;
	String whatHasChanged;
	
	/**
	 * get the molecule changed
	 * @return
	 */
	public Molecule getMolecule() {
		return this.molecule;
	}
	
	public String getWhatHasChanged() {
		return this.whatHasChanged;
	}
	
	/**
	 * You must implement this method to get what has changed
	 * @param e
	 */
	public void valueHasChanged(HuckelEvent e){
	}

	/**
	 * You must implement this method to get the geometry events
	 * @param e
	 */
	public void geometryHasChanged(GeometryEvent event) {
	}
	
	/**
	 * 
	 * @param e
	 */
	public void retrieveMolecule(Event e) {
		if (e.getSource() instanceof Molecule)
			this.molecule = (Molecule) e.getSource();
		else if (e.getSource() instanceof Atom) 
			this.molecule = ((Atom) e.getSource()).getMoleculeContainer();
		else if (e.getSource() instanceof Bond)
			this.molecule = ((Bond) e.getSource()).getMoleculeContainer();
		else if (e.getSource() instanceof BlocDelocalized)		
			this.molecule = ((BlocDelocalized) e.getSource()).getMoleculeParent();
		else 
			this.molecule = null;
		
	}
		
	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		this.whatHasChanged="hx changed";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		this.whatHasChanged="seqnum changed";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		this.whatHasChanged="nb radr changed";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		this.whatHasChanged="hxy changed";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		this.whatHasChanged="bond type changed";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		this.whatHasChanged="bloc object added";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		this.whatHasChanged="bloc object removed";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		this.whatHasChanged="bloc nbelec changed";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void moleculeDeleted(MoleculeEvent e) {
		this.whatHasChanged="molecule deleted";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void moleculeAtomAdded(MoleculeEvent e) {
		this.whatHasChanged="molecule atom added";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		this.whatHasChanged="molecule atom removed";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {
		this.whatHasChanged="molecule atom replaced";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		this.whatHasChanged="molecule bond added";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		this.whatHasChanged="molecule bond removed";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
	}

	//@Override
	public void moleculeChargeChanged(MoleculeEvent event) {
		this.whatHasChanged="charge changed";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);
	}

	//@Override
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {
		this.whatHasChanged="seqnum auto setted";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);
	}

	//@Override
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {
		this.whatHasChanged="seqnum resetted";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);
	}

	//@Override
	public void moleculeBlocDelocalizedAdded(MoleculeEvent event) {
		this.whatHasChanged="bloc added";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);
	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(MoleculeEvent event) {
		this.whatHasChanged="bloc removed";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);		
	}
	
	//@Override
	public void atomLocationChanged(GeometryEvent event){
		this.whatHasChanged="atom location changed";
		this.retrieveMolecule(event);
		this.geometryHasChanged(event);
	}

	
	//@Override
	public void moleculeMoved(GeometryEvent e) {
		this.whatHasChanged="molecule moved";
		this.retrieveMolecule(e);
		this.geometryHasChanged(e);
	}

	//@Override
	public void moleculeRotated(GeometryEvent e) {
		this.whatHasChanged="molecule rotated";
		this.retrieveMolecule(e);
		this.geometryHasChanged(e);
	}



}
