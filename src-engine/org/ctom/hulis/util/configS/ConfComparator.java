package org.ctom.hulis.util.configS;

import java.util.Comparator;

public class ConfComparator implements Comparator {

	public int compare(Object o1, Object o2) {
		// HuckelIO.PrintIf("Je m'en sert...");
		Conf conf1 = (Conf) o1;
		Conf conf2 = (Conf) o2;
		/* to get it in the "natural" order i.e. with more db first */
		return (conf2).compareTo(conf1);
	}

}