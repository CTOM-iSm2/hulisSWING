package org.ctom.hulis.huckel.listeners;


import java.util.EventListener;

import org.ctom.hulis.huckel.events.MoleculeEvent;

/**
 * This interface is implemented by classes that listen molecule events
 * @author ngoudard
 *
 */
public interface IMoleculeValueListener extends EventListener, IHuckelAtomListener, IHuckelBondListener, IBlocDelocalizedListener, IMoleculeListener {

	/**
	 * This method is executed when the molecule is deleted
	 * @param event
	 */
	public void moleculeDeleted(MoleculeEvent e);

	
	public void moleculeAtomAdded(MoleculeEvent e);

	public void moleculeAtomRemoved(MoleculeEvent e);

	public void moleculeAtomReplaced(MoleculeEvent e);


	public void moleculeBondAdded(MoleculeEvent e);

	public void moleculeBondRemoved(MoleculeEvent e);


	
	public void moleculeChargeChanged(MoleculeEvent event);
	
	public void  moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event);

	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event);

	public void moleculeBlocDelocalizedAdded(MoleculeEvent event);
	
	public void moleculeBlocDelocalizedRemoved(MoleculeEvent event);

}
