package org.ctom.hulis.huckel.listeners;


import java.util.EventListener;

import org.ctom.hulis.huckel.events.StructureLocalizedEvent;

/**
 * This interface is implemented by classes that listen molecule events
 * @author ngoudard
 *
 */
public interface IStructureLocalizedListener extends EventListener, IStructureListener{

	public void structureLocalizedWeightChanged(StructureLocalizedEvent event);

	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event);

	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event);


}
