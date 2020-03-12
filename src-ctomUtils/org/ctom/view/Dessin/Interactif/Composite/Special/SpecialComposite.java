package org.ctom.view.Dessin.Interactif.Composite.Special;

import java.util.HashSet;

import org.ctom.view.Dessin.Dessin;
import org.ctom.view.Dessin.Interactif.Composite.DessinComposite;


public abstract class SpecialComposite extends DessinComposite {
	
	HashSet<Dessin> todraw = new HashSet<Dessin>();

	/* (non-Javadoc)
	 * @see org.ctom.view.Dessin.Interactif.Composite.DessinComposite#getToDraw()
	 */
	@Override
	protected HashSet<Dessin> getToDraw() {
		return todraw;
	}

}
