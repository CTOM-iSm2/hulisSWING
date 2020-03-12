package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.MonoExcitation;


public class MonoExcitationEvent extends HuckelEvent {

	public MonoExcitationEvent(MonoExcitation monoExcitation,  Object oldValue,
			Object newValue) {
		super(monoExcitation, oldValue, newValue);
	}

	@Override
	public MonoExcitation getSource() {
		return (MonoExcitation) super.getSource();
	}
	

}
