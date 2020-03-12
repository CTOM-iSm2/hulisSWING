/**
 * 
 */
package org.ctom.hulis.util.geometry;

import java.util.Comparator;

import org.ctom.hulis.huckel.Atom;

public class SuperSymbolComparator implements Comparator<Atom> {
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Atom o1, Atom o2) {
		return new Integer(o1.getSuperSymbol()).compareTo(new Integer (o2.getSuperSymbol()));
	}
	
}
