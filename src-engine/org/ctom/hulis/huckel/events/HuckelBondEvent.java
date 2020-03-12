package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.HuckelBond;


public class HuckelBondEvent extends BondEvent {

	public HuckelBondEvent(HuckelBond huchelBond,  Object oldValue,
			Object newValue) {
		super(huchelBond, oldValue, newValue);
	}

	@Override
	public HuckelBond getSource() {
		return (HuckelBond) super.getSource();
	}
	


}
