/**
 * 
 */
package org.ctom.hulis.files.gaussian;


class LonePairNRT implements Comparable {
	int count;
	int noAtom;

	protected LonePairNRT(int noAtom, int count) {
		this.noAtom = noAtom;
		this.count = count;
	}

	public int compareTo(Object o) {
		if (!(o instanceof LonePairNRT))
			throw new ClassCastException();
		LonePairNRT l = (LonePairNRT) o;
		return new Integer(this.noAtom).compareTo(l.noAtom);

	}

	@Override
	public String toString() {
		return this.noAtom + " " + this.count;
	}
}