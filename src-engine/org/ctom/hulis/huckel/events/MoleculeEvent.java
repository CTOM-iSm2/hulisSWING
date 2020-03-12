package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.Molecule;


public class MoleculeEvent extends HuckelEvent {

	public MoleculeEvent(Molecule molecule,  Object oldValue,
			Object newValue) {
		super(molecule, oldValue, newValue);
	}

	@Override
	public Molecule getSource() {
		return (Molecule) super.getSource();
	}


}
