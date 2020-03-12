package org.ctom.hulis.huckel.listeners;


import java.util.EventListener;

import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;

/**
 * This interface is implemented by classes that listen structure deloclaized events
 * @author ngoudard
 *
 */
public interface IStructureDelocalizedListener extends EventListener, IStructureListener{

	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event);

	

}
