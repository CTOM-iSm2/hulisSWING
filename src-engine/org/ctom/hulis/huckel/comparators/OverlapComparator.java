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
public class OverlapComparator implements Comparator {


	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		Structure s1 = (Structure) o1;
		Structure s2 = (Structure) o2;
		return new Double(Math.abs(s2.getOverlapHLP())).compareTo(new Double(Math.abs(s1.getOverlapHLP()))); // attention :pour overlap on prend l'ordre decroissant
		
	}

}
