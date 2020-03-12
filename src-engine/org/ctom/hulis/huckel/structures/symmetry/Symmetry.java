package org.ctom.hulis.huckel.structures.symmetry;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
		return getUniqueAtoms_DHR();
	}

	//	@Deprecated
	//	private HashMap<String, ArrayList<Integer>> getUniqueAtoms_YC() {
	//		Matrix H = new Matrix(structure.getHamiltonianArray());
	//		EigenvalueDecomposition evd;
	//		int n = H.getColumnDimension();
	//		symmetryTableAtoms = new HashMap<String, ArrayList<Integer>>();
	//		for (int i=0; i<n; i++) {
	//			H.set(i, i, 1);
	//			evd = new EigenvalueDecomposition(H);
	//			double[] eigV = evd.getRealEigenvalues();
	//			double sum = 0;
	//			for (int j=0; j<n/2; j++) {
	//				sum+=eigV[j];
	//			}
	//			String str = Double.toString(sum);
	//			str = str.substring(0, Math.min(str.length(), 12));
	//			ArrayList<Integer> list = symmetryTableAtoms.get(str);
	//			if (list==null) {
	//				list = new ArrayList<Integer>();
	//				list.add(i);
	//				symmetryTableAtoms.put(str, list);
	//			} else {
	//				list.add(i);
	//			}
	//			H.set(i, i, 0);
	//		}
	//		return symmetryTableAtoms;
	//	}


	private HashMap<String, ArrayList<Integer>> getUniqueAtoms_DHR() {
		Matrix H = new Matrix(structure.getHamiltonianArray());
		EigenvalueDecomposition evd;
		symmetryTableAtoms = new HashMap<String, ArrayList<Integer>>();
		evd = new EigenvalueDecomposition(H);
		//		double[] eigVect = evd.getV().getArray()[0];
		int n = H.getColumnDimension();
		double[] eigVect = evd.getV().getMatrix(0, n-1, n-1, n-1).transpose().getArray()[0];
		for (int i=0; i<eigVect.length; i++) {
			double sum = eigVect[i];
			String str = new BigDecimal(sum).setScale(12, BigDecimal.ROUND_HALF_UP).toPlainString();
			//			String str = Double.toString(sum);
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

	public String getAtomGroupKey(Atom atom) {
		if (!(atom instanceof HuckelAtom)) return null;
		if (symmetryTableAtoms==null) getUniqueAtoms();
		for (String str : symmetryTableAtoms.keySet()) {
			for (int iat : symmetryTableAtoms.get(str)) {
				if (iat==((HuckelAtom)atom).getSeqNum()-1) {
					return str;
				};
			}
		}
		return null;
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

	public ArrayList<Integer> getGroupOfAtom(Atom atom) {
		String atomGroupKey = getAtomGroupKey(atom);
		ArrayList<Integer> list = symmetryTableAtoms.get(atomGroupKey);
		return list;
	}

	public int getAtomGroupNumber(Atom atom) {
		if (symmetryTableAtoms == null) {
			getUniqueAtoms_DHR();
		}
		Set<String> keys = symmetryTableAtoms.keySet();
		String key = this.getAtomGroupKey(atom);
		Iterator<String> it = keys.iterator();
		int num=0;
		while (it.hasNext()) {
			String str = (String) it.next();
			if (str==key) {
				return num;
			}
			num++;
		}
		return num;
	}

}
