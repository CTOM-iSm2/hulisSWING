package org.ctom.hulis.huckel.comparators;



import java.util.Comparator;

import org.ctom.hulis.huckel.methods.IMethod;

public class MethodNameComparator implements Comparator {

	public MethodNameComparator() {
		super();
	}

	public int compare(Object o1, Object o2) {
		if (!(o1 instanceof IMethod))
			throw new ClassCastException();
		if (!(o2 instanceof IMethod))
			throw new ClassCastException();
		IMethod m1 = (IMethod) o1;
		IMethod m2 = (IMethod) o2;
		/* to get it in the "natural" order i.e. with more db first */
		return (m1.getName()).compareTo(m2.getName());
	}

}