package org.ctom.hulis.huckel.listeners;


import java.util.EventListener;

import org.ctom.hulis.huckel.events.HuckelBondEvent;

/**
 * This interface is implemented by classes that listen huckel bond events
 * @author ngoudard
 *
 */
public interface IHuckelBondListener extends EventListener, IBondListener {

	/**
	 * This method is executed when the bond has changed
	 * @param event
	 */
	public void huckelBondHxyChanged(HuckelBondEvent e);

	public void huckelBondBondTypeChanged(HuckelBondEvent e);


}
