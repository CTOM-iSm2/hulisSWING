package org.ctom.hulis.huckel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.util.VectorssHuckel;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Maths;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * 
 * @author nicolasgoudard
 *
 */
public abstract class HuckelBlock implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6648719278422253428L;	
	ArrayList<IHuckelObject> lstHuckelObject; // objects org.ctom.hulis.huckel in the bloc 
	int nbElectrons; // number of electrons of the bloc
	
	/**
	 * 
	 */
	 public HuckelBlock() {
		this.lstHuckelObject = new ArrayList<IHuckelObject>();
		this.nbElectrons = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(lstHuckelObject, nbElectrons);
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HuckelBlock other = (HuckelBlock) obj;
		
		// deux blocs sont egaux s'ils possedent  memes objets
		List<IHuckelObject>  lstHuckelObjectsA = this.getListHuckelObject();
		List<IHuckelObject>  lstHuckelObjectsB = other.getListHuckelObject();
		
		if (lstHuckelObjectsA.size() != lstHuckelObjectsB.size())
				return false;
		
		for (IHuckelObject a : lstHuckelObjectsA ) {
			boolean ok = false;
			for (IHuckelObject a2 : lstHuckelObjectsB )
				if (a.equals(a2) ){
					ok = true;
					break;
				}
			if (!ok)
				return false;
		}

		return nbElectrons == other.nbElectrons;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return this.getListHuckelObject().isEmpty();
	}

	/**
	 * number of electrons in the bloc
	 * @return
	 */
	public int getNbElectron() {
		return this.nbElectrons ;
		
	}
	
	/**
	 * 
	 * @param nbElectrons
	 */
	public void setNbElectron(int nbElectrons) {
		this.nbElectrons=nbElectrons;
		
	}
	/**
	 *   table correspondance pour creer une matrice hamiltonien : huckelatom <-> colonne matrice 
	 *   afin d'avoir les atomes triés par numero d'ordre et leur faire correspondre un numero de colonne dans la matrice
	 */
	public Map<Integer, HuckelAtom> generateTableCorrespondanceMatrixIndexToAtom() {
	
	    Comparator<HuckelAtom> comparator = new Comparator<HuckelAtom>() {
	     
			@Override
			public int compare(HuckelAtom o1, HuckelAtom o2) {
				// TODO Auto-generated method stub
				return o1.getSeqNum() - o2.getSeqNum();
			}
	    };
	    List<HuckelAtom> lstHuckelAtoms = this.getListHuckelAtom();
	    Collections.sort(lstHuckelAtoms, comparator);
	    
		Map<Integer, HuckelAtom> tableCorrespondance= new HashMap<Integer, HuckelAtom>();
		for (int i = 0; i < lstHuckelAtoms.size() ; i++ ) {
			tableCorrespondance.put(i, lstHuckelAtoms.get(i));
	    }
	    return tableCorrespondance;
	};
	
	
	/**
	 *   table correspondance pour creer une matrice hamiltonien : huckelatom <-> colonne matrice 
	 *   afin d'avoir les atomes triés par numero d'ordre et leur faire correspondre un numero de colonne dans la matrice
	 */
	public Map<HuckelAtom, Integer> generateTableCorrespondanceAtomToMatrixIndex() {
	
	    Comparator<HuckelAtom> comparator = new Comparator<HuckelAtom>() {
	     
			@Override
			public int compare(HuckelAtom o1, HuckelAtom o2) {
				// TODO Auto-generated method stub
				return o1.getSeqNum() - o2.getSeqNum();
			}
	    };
	    List<HuckelAtom> lstHuckelAtoms = this.getListHuckelAtom();
	    Collections.sort(lstHuckelAtoms, comparator);
		Map<HuckelAtom, Integer> tableCorrespondance= new HashMap<HuckelAtom, Integer>();
		for (int i = 0; i < lstHuckelAtoms.size() ; i++ ) {
			tableCorrespondance.put(lstHuckelAtoms.get(i), i);
	    }
	    return tableCorrespondance;
	};
	
	
	/**
	 * 
	 * @return
	 */
	public double[] calcDistributionElecArray() {
		
		int nbHuckelAtoms=this.getListHuckelAtom().size();
	
		double[] repartitionElec = new double[nbHuckelAtoms];
		// intialize table of repartition of elec pi
		for (int i = 0; i < repartitionElec.length; i++)
			repartitionElec[i] = 0;
	
		int nbElecArepartir=this.getNbElectron();
		double[]energies=this.calcOrbitalsEnergies();
		
		int  i=0;
		int  fin=0;
		int d= 0;// nombre de dege,eres 
		while (nbElecArepartir>0) {
			// trouver les orbitales degenerees
			d=1;
			while ( ( (i+d) < energies.length) && (Maths.round(energies[i],
					StructureDelocalized.PRECISION) == Maths.round(
							energies[i+d], StructureDelocalized.PRECISION))) {
				d++;
			}
			fin=i+d-1;
			if (nbElecArepartir>=(2*d)) {
				for (int current=i; current<fin+1; current++) {
					repartitionElec[current] = 2;
				}
				nbElecArepartir-=(2*d);
			}
			else if (nbElecArepartir < (2*d) ) {
				for (int current=i; current<fin+1; current++) {
					repartitionElec[current] = (double) nbElecArepartir/  (double) d;
				}
				nbElecArepartir=0;
			}
			i=fin+1;
		}
	
		HuckelIO.PrintVector(repartitionElec, "distrib elec arrat petit bloc");

		return repartitionElec;
	}
	/**
	 * 
	 * @return
	 */
	public Matrix calcMatrixHamiltonian() {
		
	    Map<HuckelAtom, Integer> tableCorrespondance = generateTableCorrespondanceAtomToMatrixIndex();
		
		int x;
		int y;
		double hxy;
		int nbAtomes = getListHuckelAtom().size();
	
		Matrix hamiltonien = new Matrix(nbAtomes, nbAtomes);
	
		for (HuckelAtom a : this.getListHuckelAtom()) {
			if (a.getSeqNum() <= 0)
				return null;
	
			hamiltonien.set(tableCorrespondance.get(a), tableCorrespondance.get(a), a
						.getHx());
		}
		for (HuckelBond l : this.getListHuckelBond()) {
			x = tableCorrespondance.get(l.getAtom1());
			y = tableCorrespondance.get(l.getAtom2());
			hxy = l.getHxy();
			if ((x >= 0) && (y >= 0)) {
				hamiltonien.set(x, y, hxy);
				hamiltonien.set(y, x, hxy);
			}
		}
		
		HuckelIO.PrintMatrix(hamiltonien, "hamiltonien petit bloc");
		
		return hamiltonien;
	}
	/**
	 * 
	 * @return
	 */
	public double[][] calcOrbitalsCoeffArray() {
		Matrix hamiltionianBlock  = calcMatrixHamiltonian();
		EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecomposition(hamiltionianBlock);
		HuckelIO.PrintMatrix(new Matrix(VectorssHuckel.sort(eigenvalueDecomposition.getV().getArray())), "orbitales petit bloc");
		return VectorssHuckel.sort(eigenvalueDecomposition.getV().getArray());
		
	}
	/**
	 * 
	 * @return
	 */
	public double[] calcOrbitalsEnergies() {
		Matrix hamiltionianBlock  = calcMatrixHamiltonian();
		EigenvalueDecomposition eigenvalueDecomposition = new EigenvalueDecomposition(hamiltionianBlock);
		HuckelIO.PrintVector(VectorssHuckel.sort(eigenvalueDecomposition.getRealEigenvalues()), "calcOrbitalsEnergies petit bloc");
		return VectorssHuckel.sort(eigenvalueDecomposition.getRealEigenvalues());
		
	}
	
	
	
	
	/**
	 * This function returns the charges of the molecule
	 * 
	 * @return double []
	 */
	public double[] calcChargeDensitiesArray() {
	    Map<Integer, HuckelAtom> tableCorrespondance = generateTableCorrespondanceMatrixIndexToAtom();
		
		double[] tblCharges = new double[getListHuckelAtom().size()];
		double somme = 0.0;
		double Ci = 0.0;
		double[] repartitionElec = calcDistributionElecArray();
		double [][] orbitalsCoeffsArray=calcOrbitalsCoeffArray();
		int i;
		int j;
		HuckelAtom a;

		for (i = 0; i < orbitalsCoeffsArray.length; i++) {
			somme = 0;
			for (j = 0; j < orbitalsCoeffsArray[i].length; j++) {
				Ci = orbitalsCoeffsArray[i][j];
				somme += (Ci * Ci) * repartitionElec[j];
			}
			a = tableCorrespondance.get(i);
			tblCharges[i] = a.getNbElecPi() - somme;
			a.setDensity(tblCharges[i]);
		}


		return tblCharges;
	}
	
	/**
	 * 
	 * @return
	 */
	public double[][] calcBondOrdersArray() {
		Map<HuckelAtom, Integer> tableCorrespondance = generateTableCorrespondanceAtomToMatrixIndex();
		double somme = 0.0;
		double CA = 0.0;
		double CB = 0.0;
		double[] repartitionElec = calcDistributionElecArray();
		double [][] orbitalsCoeffsArray=calcOrbitalsCoeffArray();
		double[][] ordresLiaison = new double[orbitalsCoeffsArray.length][orbitalsCoeffsArray.length];
		int i;
		int j;
		int l;

		for (i = 0; i < ordresLiaison.length; i++) {
			for (j = 0; j < ordresLiaison[i].length; j++) {
				somme = 0;
				for (l = 0; l < orbitalsCoeffsArray.length; l++) {
					CA = orbitalsCoeffsArray[i][l];
					CB = orbitalsCoeffsArray[j][l];
					somme += (CA * CB) * repartitionElec[l];
				}
				ordresLiaison[i][j] = somme;
			}
		}

		for (HuckelBond b : getListHuckelBond()) {
			b.setBondOrder(ordresLiaison[tableCorrespondance.get(b.getAtom1())][tableCorrespondance.get(b.getAtom2())]);
		}

		return ordresLiaison;
	}
	/**
	 * 
	 * @return
	 */
	public List<HuckelAtom> getListHuckelAtom() {
		List<HuckelAtom> lstHuckelAtom = new ArrayList<HuckelAtom>(); 
		for (IHuckelObject o : this.getListHuckelObject() )
			if (o instanceof HuckelAtom)
				lstHuckelAtom.add((HuckelAtom)o);
			
			return lstHuckelAtom;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<HuckelBond> getListHuckelBond() {
		List<HuckelBond> lstHuckelBond = new ArrayList<HuckelBond>(); 
		for (IHuckelObject o : this.getListHuckelObject() )
			if (o instanceof HuckelBond)
				lstHuckelBond.add((HuckelBond)o);
			
			return lstHuckelBond;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<IHuckelObject> getListHuckelObject() {
		return (List<IHuckelObject>) lstHuckelObject.clone();
	}
	

	/**
	 * tests if the bloc contains the org.ctom.hulis.huckel object in parameter
	 * @param o
	 * @return
	 */
	public boolean containsHuckelObject(IHuckelObject o) {
		return this.getListHuckelObject().contains(o);
		
	}
	
	/**
	 * 
	 * @return number of objects of the bloc
	 */
	public int countHuckelObjects() {
		return this.getListHuckelObject().size();
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		 List<HuckelBond> bonds=this.getListHuckelBond();
		 int nbonds = bonds.size();
		 int nAtomsAlone = 0;
		 int nLinkedAtoms = 0;
		 for (HuckelAtom atom : this.getListHuckelAtom()) {
			 boolean found=false;
			 Iterator<HuckelBond> it= bonds.iterator();
			 while (it.hasNext() && !found) {
				 HuckelBond b = it.next();
				 if (b.contains(atom))
					 found=true;
			 }
			 if (found) nLinkedAtoms ++ ; else nAtomsAlone++;
		 }
		 
		 this.getListHuckelAtom().size();
		 return "block : " + nbonds + " bonds, " + 
		 nLinkedAtoms + " linked atoms, " + 
		 nAtomsAlone + " atoms not linked, " +
		 this.getNbElectron() + " electrons"; 

	}


	
	
}
