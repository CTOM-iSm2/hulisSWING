package org.ctom.hulis.huckel.events;

import org.ctom.hulis.huckel.BlocDelocalized;


public class BlocDelocalizedEvent extends HuckelEvent {

	public BlocDelocalizedEvent(BlocDelocalized blocDelocalized,  Object oldValue,
			Object newValue) {
		super(blocDelocalized, oldValue, newValue);
	}

	@Override
	public BlocDelocalized getSource() {
		return (BlocDelocalized) super.getSource();
	}


}
