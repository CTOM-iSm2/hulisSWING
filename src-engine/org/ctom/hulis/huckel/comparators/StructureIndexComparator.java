/**
 * 
 */
package org.ctom.hulis.huckel.comparators;

import java.util.Comparator;

import org.ctom.hulis.huckel.structures.Structure;

/**
 * @author macbook
 *
 */
public class StructureIndexComparator implements Comparator<Structure> {
	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare (Structure s1, Structure s2){
		return new Integer(s1.getIndex()).compareTo(s2.getIndex());
	}
}
