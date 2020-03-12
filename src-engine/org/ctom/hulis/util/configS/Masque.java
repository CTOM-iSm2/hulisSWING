package org.ctom.hulis.util.configS;

import java.util.ArrayList;


public class Masque {

	public static void main(String[] args) {
		Masque mymasque = new Masque(4);
		mymasque.set(0, -1);
		//System.out.format("Test1\n");
		mymasque.print();
		while (mymasque.getNext()) {
			mymasque.print();
		}

		//System.out.format("\nTest2\n");
		mymasque.empty();
		mymasque.getList(1, 0);

	}

	private boolean hasNext = true;

	private int[] mask;

	public Masque(int size) {
		this.mask = new int[size];
		empty();
	}

	public Masque add(Masque that) {
		Masque res = new Masque(length());
		for (int ielem = 0; ielem < length(); ielem++) {
			int value = get(ielem) + that.get(ielem);
			res.set(ielem, value);
		}
		return res;
	}

	public void addToElem(int ielem, int value) {
		set(ielem, get(ielem) + value);
	}

	public void empty() {
		for (int ielem = 0; ielem < length(); ielem++) {
			set(ielem, 0);
		}
	}

	protected void erase() {
		this.mask = null;
		this.hasNext = true;
	}

	public int get(int ielem) {
		return this.mask[ielem];
	}

	public ArrayList<Masque> getList(int charge, int nsep) {
		ArrayList<Masque> List = new ArrayList<Masque>();
		int plus = nsep;
		int minus = -nsep;
		if (charge > 0) {
			plus += charge;
		} else {
			minus += charge;
		}
		if ((charge==0) && (nsep==0)) {
			List.add(new Masque(this.length()));
		} else {
			for (int iplus = 1; iplus <= plus + 1; iplus++) {
				for (int iminus = -1; iminus >= minus - 1; iminus--) {
					if (iplus + iminus == charge) {
						for (int iat = 0; iat < length(); iat++) {
							for (int jat = 0; jat < length(); jat++) {
								Masque newmask = new Masque(length());
								newmask.addToElem(iat, -iplus);
								newmask.addToElem(jat, -iminus);
								List.add(newmask);
							}
						}
					}
				}
			}
		}
		for (Masque mask : List) {
			mask.print();
		}
		return List;
	}

	public boolean getNext() {
		for (int ielem = 0; ielem < length() - 1; ielem++) {
			if (get(ielem) != 0) {
				if (get(ielem + 1) == 0) {
					set(ielem + 1, get(ielem));
					set(ielem, 0);
					return true;
				}
				if (ielem == length() - 1)
					this.hasNext = false;
			}
		}
		return false;
	}

	public boolean hasNext() {
		return this.hasNext;
	}

	public int length() {
		return this.mask.length;
	}

	public void print() {
		for (int ielem = 0; ielem < length(); ielem++) {
			//if (HuckelIO.isverbose())
			//System.out.format("%5d", get(ielem));
		}
		//System.out.format("\n");
	}

	public Masque set(int ielem, int value) {
		this.mask[ielem] = value;
		return this;
	}
}
