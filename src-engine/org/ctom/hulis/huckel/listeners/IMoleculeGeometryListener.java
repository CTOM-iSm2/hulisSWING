package org.ctom.hulis.huckel.listeners;

import org.ctom.hulis.huckel.events.GeometryEvent;

public interface IMoleculeGeometryListener extends IAtomGeometryListener, IMoleculeListener{

	public void moleculeMoved(GeometryEvent e);

	public void moleculeRotated(GeometryEvent e);

}
