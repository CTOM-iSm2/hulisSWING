package org.ctom.hulis.huckel.events;

/**
 * This class describes a Huckel event
 * @author ngoudard
 *
 */
public class HuckelEvent extends Event {


	/**
	 * Constructs an event 
	 * @param source :  source of the event : molecule, bond or atom
	 * @param oldValue : value before the event
	 * @param newValue : value after the event
	 */
	public HuckelEvent(Object source, Object oldValue,
			Object newValue) {
		super (source, oldValue, newValue);
	}

	
	
}
