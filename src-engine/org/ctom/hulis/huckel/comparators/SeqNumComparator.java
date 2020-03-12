package org.ctom.hulis.huckel.comparators;

import java.util.Comparator;

import org.ctom.hulis.huckel.HuckelAtom;

public class SeqNumComparator implements Comparator<HuckelAtom> {
	
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare (HuckelAtom a1, HuckelAtom a2){
		return new Integer(a1.getSeqNum()).compareTo(a2.getSeqNum());
	}

}
