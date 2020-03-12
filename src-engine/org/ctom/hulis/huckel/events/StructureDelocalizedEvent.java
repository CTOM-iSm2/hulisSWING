package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.structures.StructureDelocalized;


public class StructureDelocalizedEvent extends StructureEvent {

	public StructureDelocalizedEvent(StructureDelocalized structureDelocalized,  Object oldValue,
			Object newValue) {
		super(structureDelocalized, oldValue, newValue);
	}


	@Override
	public StructureDelocalized getSource() {
		return (StructureDelocalized) super.getSource();
	}

}
