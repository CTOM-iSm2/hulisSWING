package org.ctom.hulis.huckel.adapters;

import org.ctom.hulis.huckel.MonoExcitation;
import org.ctom.hulis.huckel.events.Event;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener;
import org.ctom.hulis.huckel.listeners.IStructureListener;
import org.ctom.hulis.huckel.listeners.IStructureLocalizedListener;

public  class StructureAdapter extends MoleculeAdapter implements IStructureListener, IStructureDelocalizedListener, IStructureLocalizedListener  {
	


	
	@Override
	public void retrieveMolecule(Event e){
		super.retrieveMolecule(e);
		if (e.getSource() instanceof MonoExcitation) {
			this.molecule = ((MonoExcitation)  e.getSource()).getStructure();
		}
		
	}


	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IMonoExcitationListener#monoExcitationTriggered(org.ctom.hulis.huckel.events.MonoExcitationEvent)
	 */
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		this.whatHasChanged="monoexcitation triggered";
		this.retrieveMolecule(e);
		this.valueHasChanged(e);
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureLocalizedListener#structureLocalizedWeightChanged(org.ctom.hulis.huckel.events.StructureLocalizedEvent)
	 */
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		this.whatHasChanged="weight changed";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureLocalizedListener#structureLocalizedCoupleChanged(org.ctom.hulis.huckel.events.StructureLocalizedEvent)
	 */
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		this.whatHasChanged="couple changed";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);			
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureLocalizedListener#structureLocalizedCoupleRemoved(org.ctom.hulis.huckel.events.StructureLocalizedEvent)
	 */
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {
		this.whatHasChanged="couple removed";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);	
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener#structureDelocalizedSpinChanged(org.ctom.hulis.huckel.events.StructureDelocalizedEvent)
	 */
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {
		this.whatHasChanged="spin changed";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);	
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.listeners.IStructureListener#structureNameChanged(org.ctom.hulis.huckel.events.StructureEvent)
	 */
	public void structureNameChanged(StructureEvent event) {
		this.whatHasChanged="name changed";
		this.retrieveMolecule(event);
		this.valueHasChanged(event);	
	}



}
