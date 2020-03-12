package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.structures.StructureLocalized;


public class StructureLocalizedEvent extends StructureEvent {

	public StructureLocalizedEvent(StructureLocalized structureLocalized,  Object oldValue,
			Object newValue) {
		super(structureLocalized, oldValue, newValue);
	}


	@Override
	public StructureLocalized getSource() {
		return (StructureLocalized) super.getSource();
	}

}
