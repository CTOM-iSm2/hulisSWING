package org.ctom.hulis.huckel;

public class PeriodicTable {

	public enum Element {
		C, B, N, O, F, Si, P, S, Cl, Me, H
	};

	public enum Entry {
		C, B, N1, N2, O1, O2, F, Si, P1, P2, S1, S2, Cl, Me, H
	};

	/**
	 * constructs and return a new simple atom or huckel atom depends on the Entry enum specified in parameter;
	 * @param entry
	 * @return a new atom object
	 */
	public static Atom getNewAtom(Entry entry) {
		/*
		 * atomic number entry, element, type, name, nbElecSigma, nbElecPi, nbElecVal, 
		 * hx, nbNeighboursMax
		 */
		switch (entry) {
		case C:
			return new HuckelAtom(6, entry, Element.C, "C", "Carbone", 0, 1, 4,0.00, 3);
		case B:
			return new HuckelAtom(5, entry, Element.B, "B", "Boron", 0, 0, 3,
					-0.45, 3);
		case N1:
			return new HuckelAtom(7, entry, Element.N, "N1", "Nitrogen 1", 2, 1, 5,
					0.51, 2);
		case N2:
			return new HuckelAtom(7, entry, Element.N, "N2", "Nitrogen 2", 0, 2, 5,
					1.37, 3);
		case O1:
			return new HuckelAtom(8, entry, Element.O, "O1", "Oxygen 1", 4, 1, 6,
					0.97, 1);
		case O2:
			return new HuckelAtom(8, entry, Element.O, "O2", "Oxygen 2", 2, 2, 6,
					2.09, 2);
		case F:
			return new HuckelAtom(9, entry, Element.F, "F", "Fluorine", 4, 2, 7,
					2.71, 1);
		case Si:
			return new HuckelAtom(14, entry, Element.Si, "Si", "Silicon ", 0, 1,
					4, 0.00, 3);
		case P1:
			return new HuckelAtom(15, entry, Element.P, "P1", "Phosphorus 1", 2, 1,
					5, 0.19, 2);
		case P2:
			return new HuckelAtom(15, entry, Element.P, "P2", "Phosphorus 2", 0, 2,
					5, 0.75, 3);
		case S1:
			return new HuckelAtom(16, entry, Element.S, "S1", "Sulfur 1", 4, 1, 6,
					0.46, 1);
		case S2:
			return new HuckelAtom(16, entry, Element.S, "S2", "Sulfur 2", 2, 2, 6,
					1.11, 2);
		case Cl:
			return new HuckelAtom(17, entry, Element.Cl, "Cl", "Chlorine", 2, 2, 7,
					1.48, 1);
		case Me:
			return new HuckelAtom(6, entry, Element.Me, "CH3", "CH3", 0, 2, 2,
					2.00, 1);

			// case X :
			// return new HuckelAtom (element, "X0", "Libre 0", 0, 1, 0.00, 1);
			// // ?
		case H:
			/* element, sigle, name, nbElecVal, nbNeighboursMax */
			return new Atom(1, entry, Element.H, "H", "Hydrogen", 1, 1);
		default:
			return null;
		}
	}
	
	/**
	 * Return a huckel table entry depends of the element and the number of neighbours of element
	 * @param element
	 * @param neighbours
	 * @return huckel table entry
	 */
	public static Entry  getElementToEntry (Element element, int neighbours){

			switch (element) {
			case N:
				if (neighbours == 3)
					return PeriodicTable.Entry.N2;
				else
					return PeriodicTable.Entry.N1;

			case O:
				if (neighbours == 2)
					return PeriodicTable.Entry.O2;
				else
					return PeriodicTable.Entry.O1;
			case P:
				if (neighbours == 3)
					return PeriodicTable.Entry.P2;
				else
					return PeriodicTable.Entry.P1;
			case S:
				if (neighbours == 2)
					return PeriodicTable.Entry.S2;
				else
					return PeriodicTable.Entry.S1;
			default:
				return PeriodicTable.Entry.valueOf(element.toString());
			}

	
	}
	
	/**
	 * return the element enum according to the atomic number specified in parameter
	 * @param atomicNumber
	 * @return element according to the atomic number
	 */
	public static Element getElementByAtomicNumber(int atomicNumber) {
		switch (atomicNumber){
		case 1: return Element.H;
		case 5: return Element.B;
		case 6: return Element.C;
		case 7: return Element.N;
		case 8: return Element.O;
		case 9:  return Element.F;
		case 14:  return Element.Si;
		case 15:  return Element.P;
		case 16:  return Element.S;
		case 17:  return Element.Cl;
		default : return null;
		}
	}
	
}
