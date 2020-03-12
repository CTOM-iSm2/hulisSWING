package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.HuckelAtom;


public class HuckelAtomEvent extends AtomEvent {

	public HuckelAtomEvent(HuckelAtom huckelAtom,  Object oldValue,
			Object newValue) {
		super(huckelAtom, oldValue, newValue);
	}

	@Override
	public HuckelAtom getSource() {
		return (HuckelAtom) super.getSource();
	}


}
