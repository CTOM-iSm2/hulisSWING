package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.structures.Structure;


public class StructureEvent extends MoleculeEvent {

	public StructureEvent(Structure structure,  Object oldValue,
			Object newValue) {
		super(structure, oldValue, newValue);
	}
	
	@Override
	public Structure getSource() {
		return (Structure) super.getSource();
	}


}
