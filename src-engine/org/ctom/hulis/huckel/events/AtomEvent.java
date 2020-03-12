package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.Atom;


public class AtomEvent extends HuckelEvent {

	public AtomEvent(Atom atom, Object oldValue,
			Object newValue) {
		super(atom, oldValue, newValue);
	}


	@Override
	public Atom getSource() {
		return (Atom) super.getSource();
	}


}
