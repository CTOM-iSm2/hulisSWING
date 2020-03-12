package org.ctom.hulis.huckel.structures.symmetry;

import java.util.ArrayList;
import java.util.HashMap;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.structures.Structure;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;


public class Symmetry {
	
	private Structure structure;
	private HashMap<String, ArrayList<Integer>> symmetryTableAtoms=null;
	private HashMap<String, ArrayList<Bond>> symmetryTableBonds=null;
	
	public Symmetry(Structure struct) {
		this.structure = struct;
	}
	
	public HashMap<String, ArrayList<Integer>> getUniqueAtoms() {
		Matrix H = new Matrix(structure.getHamiltonianArray());
		EigenvalueDecomposition evd;
		int n = H.getColumnDimension();
		symmetryTableAtoms = new HashMap<String, ArrayList<Integer>>();
		for (int i=0; i<n; i++) {
			H.set(i, i, 1);
			evd = new EigenvalueDecomposition(H);
			double[] eigV = evd.getRealEigenvalues();
			double sum = 0;
			for (int j=0; j<n/2; j++) {
				sum+=eigV[j];
			}
			String str = Double.toString(sum);
			str = str.substring(0, Math.min(str.length(), 12));
			ArrayList<Integer> list = symmetryTableAtoms.get(str);
			if (list==null) {
				list = new ArrayList<Integer>();
				list.add(i);
				symmetryTableAtoms.put(str, list);
			} else {
				list.add(i);
			}
			H.set(i, i, 0);
		}
		return symmetryTableAtoms;
	}
	
	public int getTotalNumberOfGroups() {
		if (symmetryTableAtoms==null) getUniqueAtoms();
		return symmetryTableAtoms.keySet().size();
	}
	
	public int getAtomGroupNumber(Atom atom) {
		if (!(atom instanceof HuckelAtom)) return 0;
		int num=0;
		if (symmetryTableAtoms==null) getUniqueAtoms();
		for (String str : symmetryTableAtoms.keySet()) {
			num++;
			for (int iat : symmetryTableAtoms.get(str)) {
				if (iat==((HuckelAtom)atom).getSeqNum()-1) {
					return num;
				};
			}
		}
		return 0;
	}

	public HashMap<String, ArrayList<Bond>> getUniqueBonds() {
 		Matrix H = new Matrix(structure.getHamiltonianArray());
		EigenvalueDecomposition evd;
		int n = H.getColumnDimension();
		symmetryTableBonds = new HashMap<String, ArrayList<Bond>>();
		for (Bond b : structure.getHuckelBonds()) {
			int iat=((HuckelAtom)b.getAtom1()).getSeqNum()-1;
			int jat=((HuckelAtom)b.getAtom2()).getSeqNum()-1;
			H.set(iat, jat, 2);
			H.set(jat, iat, 2);
			evd = new EigenvalueDecomposition(H);
			double[] eigV = evd.getRealEigenvalues();
			double sum = 0;
			for (int j=0; j<n/2; j++) {
				sum+=eigV[j];
			}
			String str = Double.toString(sum);
			str = str.substring(0, 12);
			ArrayList<Bond> list = symmetryTableBonds.get(str);
			if (list==null) {
				list = new ArrayList<Bond>();
				list.add(b);
				symmetryTableBonds.put(str, list);
			} else {
				list.add(b);
			}
			H.set(iat, jat, 1);
			H.set(jat, iat, 1);
		}
		return symmetryTableBonds;
	}

}
