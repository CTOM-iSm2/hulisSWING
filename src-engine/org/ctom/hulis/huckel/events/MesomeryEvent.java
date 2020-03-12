package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.Mesomery;

public class MesomeryEvent extends HuckelEvent {

	public MesomeryEvent(Mesomery mesomery, Object oldValue,
			Object newValue) {
		super(mesomery, oldValue, newValue);
	}

	@Override
	public Mesomery getSource() {
		return (Mesomery) super.getSource();
	}
}
