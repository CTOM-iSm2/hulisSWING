package org.ctom.hulis.huckel;

import java.io.Serializable;
import java.util.Comparator;


public abstract class Orbitale extends ObjectLaTeX implements Normalisable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8542623846236386428L;

	private Energy energy;

	public Orbitale(Energy e) {
		super();
		this.energy=e;
	}

	/**
	 * @return the energy
	 */
	public Energy getEnergy() {
		return this.energy;
	}

	public static void main(String[] args) {
		String stro = new Orbitale(null) {
			
			public double computeNorm() {
				// TODO Auto-generated method stub
				return 0;
			}
		}.toLaTeX();
		System.out.print(stro);
	}

	public class OrbitalComparator implements Comparator {

		public OrbitalComparator() {
			super();
		}

		public int compare(Object o1, Object o2) {
			if (!(o1 instanceof Orbitale))
				throw new ClassCastException();
			if (!(o2 instanceof Orbitale))
				throw new ClassCastException();
			Orbitale som1 = (Orbitale) o1;
			Orbitale som2 = (Orbitale) o2;
			
			/*order by increasing energy*/
			return (som1.getEnergy()).compareTo(som2.getEnergy());
		}

	}
}