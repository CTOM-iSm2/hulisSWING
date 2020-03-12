package org.ctom.hulis.huckel.listeners;


import java.util.EventListener;

import org.ctom.hulis.huckel.events.StructureEvent;

/**
 * This interface is implemented by classes that listen molecule events
 * @author ngoudard
 *
 */
public interface IStructureListener extends EventListener, IMoleculeValueListener, IMonoExcitationListener{

	public void structureNameChanged(StructureEvent event);
	

}
