package org.ctom.hulis.huckel.listeners;


import java.util.EventListener;

import org.ctom.hulis.huckel.events.GeometryEvent;

/**
 * This interface is implemented by classes that listen atom events
 * @author ngoudard
 *
 */
public interface IAtomGeometryListener extends EventListener {

	public void atomLocationChanged(GeometryEvent event);
}
