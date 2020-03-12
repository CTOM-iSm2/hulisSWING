package org.ctom.hulis.huckel.listeners;


import java.util.EventListener;

import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;

/**
 * This interface is implemented by classes that listen bloc events
 * @author ngoudard
 *
 */
public interface IBlocDelocalizedListener extends EventListener {

	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e);

	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e);

	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e);

}
