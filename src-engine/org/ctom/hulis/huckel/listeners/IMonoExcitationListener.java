package org.ctom.hulis.huckel.listeners;

import java.util.EventListener;

import org.ctom.hulis.huckel.events.MonoExcitationEvent;


/**
 * This interface is used by classes that listen monoexcitation event
 * @author Y. Carissan
 *
 */
public interface IMonoExcitationListener extends EventListener {

	/**
	 * This method is executed when a monoexcitation is triggered
	 * @param huckel event
	 */
	public void monoExcitationTriggered(MonoExcitationEvent e);

}
