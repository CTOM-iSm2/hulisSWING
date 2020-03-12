/**
 * 
 */
package org.ctom.hulis.huckel.adapters;

import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MesomeryEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.listeners.IMesomeryListener;

/**
 * @author macbook
 *
 */
public class MesomeryAdapter implements IMesomeryListener {

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener#structureDelocalizedSpinChanged(org.ctom.hulis.huckel.events.StructureDelocalizedEvent)
	 */
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureListener#structureNameChanged(org.ctom.hulis.huckel.events.StructureEvent)
	 */
	public void structureNameChanged(StructureEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeDeleted(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeDeleted(MoleculeEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeAtomAdded(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeAtomAdded(MoleculeEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeAtomRemoved(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeAtomRemoved(MoleculeEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeAtomReplaced(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeAtomReplaced(MoleculeEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeBondAdded(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeBondAdded(MoleculeEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeBondRemoved(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeBondRemoved(MoleculeEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeChargeChanged(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeChargeChanged(MoleculeEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeListHuckelAtomSeqNumAutoSetted(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeListHuckelAtomSeqNumResetted(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeBlocDelocalizedAdded(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeBlocDelocalizedAdded(MoleculeEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMoleculeValueListener#moleculeBlocDelocalizedRemoved(org.ctom.hulis.huckel.events.MoleculeEvent)
	 */
	public void moleculeBlocDelocalizedRemoved(MoleculeEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IHuckelAtomListener#huckelAtomHxChanged(org.ctom.hulis.huckel.events.HuckelAtomEvent)
	 */
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IHuckelAtomListener#huckelAtomSeqNumChanged(org.ctom.hulis.huckel.events.HuckelAtomEvent)
	 */
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IHuckelAtomListener#huckelAtomRadRChanged(org.ctom.hulis.huckel.events.HuckelAtomEvent)
	 */
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IHuckelBondListener#huckelBondHxyChanged(org.ctom.hulis.huckel.events.HuckelBondEvent)
	 */
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IHuckelBondListener#huckelBondBondTypeChanged(org.ctom.hulis.huckel.events.HuckelBondEvent)
	 */
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IBlocDelocalizedListener#blocDelocalizedHuckelObjectAdded(org.ctom.hulis.huckel.events.BlocDelocalizedEvent)
	 */
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IBlocDelocalizedListener#blocDelocalizedHuckelObjectRemoved(org.ctom.hulis.huckel.events.BlocDelocalizedEvent)
	 */
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IBlocDelocalizedListener#blocDelocalizedNbElectronsChanged(org.ctom.hulis.huckel.events.BlocDelocalizedEvent)
	 */
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMonoExcitationListener#monoExcitationTriggered(org.ctom.hulis.huckel.events.MonoExcitationEvent)
	 */
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureLocalizedListener#structureLocalizedWeightChanged(org.ctom.hulis.huckel.events.StructureLocalizedEvent)
	 */
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureLocalizedListener#structureLocalizedCoupleChanged(org.ctom.hulis.huckel.events.StructureLocalizedEvent)
	 */
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureLocalizedListener#structureLocalizedCoupleRemoved(org.ctom.hulis.huckel.events.StructureLocalizedEvent)
	 */
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMesomeryListener#mesomeryListWeightCalculated(org.ctom.hulis.huckel.events.MesomeryEvent)
	 */
	public void mesomeryListWeightCalculated(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMesomeryListener#mesomeryStructuresSelectionTresholdChanged(org.ctom.hulis.huckel.events.MesomeryEvent)
	 */
	public void mesomeryStructuresSelectionTresholdChanged(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMesomeryListener#mesomeryStructureAdded(org.ctom.hulis.huckel.events.MesomeryEvent)
	 */
	public void mesomeryStructureAdded(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMesomeryListener#mesomeryStructureRemoved(org.ctom.hulis.huckel.events.MesomeryEvent)
	 */
	public void mesomeryStructureRemoved(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMesomeryListener#mesomeryMethodAdded(org.ctom.hulis.huckel.events.MesomeryEvent)
	 */
	public void mesomeryMethodAdded(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMesomeryListener#mesomeryMethodRemoved(org.ctom.hulis.huckel.events.MesomeryEvent)
	 */
	public void mesomeryMethodRemoved(MesomeryEvent e) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMesomeryListener#mesomeryExceptionOccured(org.ctom.hulis.huckel.events.MesomeryEvent, java.lang.Exception)
	 */
	public void mesomeryExceptionOccured(MesomeryEvent e, Exception exception) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMesomeryListener#mesomeryStartComputeWeights(org.ctom.hulis.huckel.events.MesomeryEvent)
	 */
	public void mesomeryStartComputeWeights(MesomeryEvent event) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMesomeryListener#mesomeryStructuresSelectorChanged(org.ctom.hulis.huckel.events.MesomeryEvent)
	 */
	public void mesomeryStructuresSelectorChanged(MesomeryEvent event) {
		// TODO Auto-generated method stub
		
	}

	

}
