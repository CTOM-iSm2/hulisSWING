package org.ctom.hulis.huckel.listeners;


import java.util.EventListener;

import org.ctom.hulis.huckel.events.HuckelAtomEvent;

/**
 * This interface is implemented by classes that listen atom events
 * @author ngoudard
 *
 */
public interface IHuckelAtomListener extends EventListener, IAtomValueListener {

	public void huckelAtomHxChanged(HuckelAtomEvent e);

	public void huckelAtomSeqNumChanged(HuckelAtomEvent e);

	public void huckelAtomRadRChanged(HuckelAtomEvent e);

}
