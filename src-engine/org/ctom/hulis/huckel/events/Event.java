package org.ctom.hulis.huckel.events;

/**
 * This class describes an event
 * @author ngoudard
 *
 */
public class Event  {

	private Object newValue; // value after the event
	private Object oldValue; // value before the event
	private Object source; // source of the event : molecule, bond or atom

	/**
	 * Constructs an event 
	 * @param source :  source of the event : molecule, bond or atom
	 * @param oldValue : value before the event
	 * @param newValue : value after the event
	 */
	public Event(Object source, Object oldValue,
			Object newValue) {
		this.source = source;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	/**
	 * 
	 * @return value after the event
	 */
	public Object getNewValue() {
		return this.newValue;
	}

	/**
	 * @return value before the event
	 */
	public Object getOldValue() {
		return this.oldValue;
	}

	/**
	 * 
	 * @return event source
	 */
	public Object getSource() {
		return this.source;
	}

	
}
