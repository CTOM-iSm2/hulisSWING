package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.Bond;


public class BondEvent extends HuckelEvent {

	public BondEvent(Bond bond, Object oldValue,
			Object newValue) {
		super(bond, oldValue, newValue);
	}

	@Override
	public Bond getSource() {
		return (Bond) super.getSource();
	}


}
