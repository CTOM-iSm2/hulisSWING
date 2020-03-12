package org.ctom.hulis.huckel.methods;

import java.io.Serializable;
import java.util.ArrayList;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.HLPException;
import org.ctom.hulis.huckel.exception.HLPOverlapException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.NoElectronException;
import org.ctom.hulis.huckel.exception.NoSOMAvailableException;
import org.ctom.hulis.huckel.exception.NullOvlMatrixException;
import org.ctom.hulis.huckel.exception.OrbitaleMoleculaireException;
import org.ctom.hulis.huckel.exception.ProjectionException;
import org.ctom.hulis.huckel.exception.RedundanceException;
import org.ctom.hulis.huckel.exception.UnavailableSOMException;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.huckel.warning.IMethodWarning;
import org.ctom.hulis.util.Projection;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.common.Arrays;

import Jama.Matrix;

/**
 * This class describes the HLP method calculation for the mesomery :
 * hamiltonian, Ci, weights
 * 
 * @author Y. Carissan
 */
public class HLPMethod implements Cloneable, IMethod, IMethodHasOptimizableMatrix, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9057058480708583811L;
	private double[] ciCoeff; // Ci vector
	private double[] ciWeights; // weights array
	private transient Mesomery mesomery; // mesomery that implements HLP method
	final private String name = "HLP";
	private double[][] overlapTab; // overlap matrix kept to improve data access
	private double tau; // tau

	IMethodException error;
	private ArrayList<IMethodWarning> warnings = new ArrayList<IMethodWarning>();

	/**
	 * Constructor
	 */
	public HLPMethod() {
		this.ciCoeff = new double[0];
		this.ciWeights = new double[0];
		this.error = null;
	}

	/**
	 * Constructor by copy
	 */
	public HLPMethod(HLPMethod method) {
		super();
		this.tau = method.tau;
		this.mesomery = method.getMesomery();
		this.ciCoeff = Arrays.arrayCopy(method.ciCoeff);
		this.ciWeights = Arrays.arrayCopy(method.ciWeights);
		this.overlapTab = Arrays.arrayCopy(method.overlapTab);
		this.error = method.error;
	}

	/**
	 * This method calculates HLP coefficients, weights and tau
	 * @throws  
	 */
	public void calculate() throws IMethodException {
		int i;
		this.ciCoeff = new double[0];
		this.ciWeights = new double[0];
		this.error = null;

		int nbSelLocalizedStructs = this.mesomery.countSelectedLocalizedStructs();

		double[][] OvlTabAll;

		if (this.mesomery.isStopConsumingProccess())
			return;
		try {
			OvlTabAll = this.getOverlapTabAll();
		} catch (OrbitaleMoleculaireException e) {
			this.error = new HLPException(e);
			throw error;
		} catch (NoElectronException e) {
			this.error = new HLPException(e);
			throw error;
		} catch (NullOvlMatrixException e) {
			this.error = new HLPException(e);
			throw error;
		} // i+1 pour sauter Phitot|Phitot
		catch (HLPOverlapException e) {
			this.error = new HLPException(e);
			throw error;
		}
		catch (Exception e) {
			this.error = new HLPException(e);
			throw error;
		}
		//on recupere le vecteur des recouvrements entre les Phi_i et Phi_tot
		Matrix OvlStot = new Matrix(nbSelLocalizedStructs,1);
		for (i = 0; i < nbSelLocalizedStructs; i++) {
			OvlStot.set(i, 0, OvlTabAll[0][i + 1]);
		}
		
/*		for (i = 0; i < nbSelLocalizedStructs; i++) {
			if (this.mesomery.isStopConsumingProccess())
				return;
			try {
				OvlStot.set(i, 0, this.getOverlapTabAll()[0][i + 1]);
			} catch (OrbitaleMoleculaireException e) {
				this.error = new HLPException(e);
				throw error;
			} catch (NoElectronException e) {
				this.error = new HLPException(e);
				throw error;
			} catch (NullOvlMatrixException e) {
				this.error = new HLPException(e);
				throw error;
			} // i+1 pour sauter Phitot|Phitot
			catch (HLPOverlapException e) {
				this.error = new HLPException(e);
				throw error;
			}
			catch (Exception e) {
				this.error = new HLPException(e);
				throw error;
			}
		}
*/
		Projection proj = null;

		//on resoud le probleme S x C = S_tot


		try {
			//	JOptionPane.showMessageDialog(null, "calcul projection");
			proj = new Projection(new Matrix(this.getOverlapTabNoPhitot()), OvlStot);
			HuckelIO.PrintVector(proj.getCoeffs(),"Coefficients de la projection");
			HuckelIO.PrintVector(proj.getWeights(),"Poids de la projection");
			HuckelIO.PrintVector(proj.getNormalizedWeights(),"Poids normes de la projection");
			HuckelIO.PrintIfln(String.format("Norme de la projection %f",proj.getNorm()));
			
		} catch (NoElectronException e1) {
			this.error = new RedundanceException();
			throw error;
		} catch (NullOvlMatrixException e1) {
			this.error = new RedundanceException();
			throw error;
		} catch (ProjectionException e1) {
			this.error = new RedundanceException();
			throw error;
		} catch (OrbitaleMoleculaireException e1) {
			this.error = new RedundanceException();
			throw error;
		} catch (HLPOverlapException e1) {
			this.error = new RedundanceException();
			throw error;
		}


		this.ciCoeff = proj.getNormalizedCoeffs().clone();
		this.ciWeights = proj.getNormalizedWeights().clone();
/*		this.tau = proj.getNorm(); 04/02/2015 : changement de definition pour tau.
 * yc pas daccord
 */
		this.tau = proj.getNewTau() ;


		HuckelIO.unsetBuffer();

		//on repercute les poids vers les structures de la mesomerie
		StructureLocalized struct;
		for (i = 0; i < nbSelLocalizedStructs; i++) {
			struct = this.mesomery.getSelectedLocalizedStruct(i);
			struct.setWeight(this.getName(), this.getWeights()[i]);
		}

	} // Calculate HLP

	/**
	 * This method returns a copy of the HLCI method
	 * 
	 * @return HLP method
	 */
	@Override
	public Object clone() {
		try {
			super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return new HLPMethod(this);
	}

	/**
	 * This function gets the calculated Ci vector
	 * 
	 * @return a double vector
	 */
	public double[] getCI() {
		return this.ciCoeff;
	}

	/**
	 * This function return the HLP hamiltonian
	 * 
	 * @return a double array
	 */
	public double[][] getHamiltonian() {
		return null;
	}

	/**
	 * This function returns the method name
	 * 
	 * @return string
	 */
	public String getName() {
		return this.name ;
	}

	/**
	 * This method returns the overlap matrix as a double[][] array used in the
	 * mesomery
	 * 
	 * @return Overlap Matrix as double[][]
	 * @throws OrbitaleMoleculaireException 
	 * @throws HLPOverlapException 
	 */
	public double[][] getOverlapTabAll() throws NoElectronException, NullOvlMatrixException, OrbitaleMoleculaireException, HLPOverlapException {
		if (this.overlapTab == null) {
			try {
				this.computeOverlapTab();
			} catch (NullOvlMatrixException e) {
				HuckelIO.error(this.getClass().getName(), "getOverlapTabAll", e.getMessage(), e);
				throw e;
			}
		}
		return this.overlapTab;
	}

	/**
	 * This method returns the overlap matrix as a double[][] array used in the
	 * mesomery
	 * 
	 * @return Overlap Matrix as double[][]
	 * @throws NullOvlMatrixException 
	 * @throws OrbitaleMoleculaireException 
	 * @throws HLPOverlapException 
	 */
	public double[][] getOverlapTabNoPhitot() throws NoElectronException, NullOvlMatrixException, OrbitaleMoleculaireException, HLPOverlapException {
		double[][] sAll = this.getOverlapTabAll();
		int size = sAll.length;
		double[][] sSmall = new double[size - 1][size - 1];

		for (int i = 1; i < size; i++) {
			for (int j = 1; j < size; j++) {
				sSmall[i - 1][j - 1] = sAll[i][j];
			}
		}

		return sSmall;
	}

	/**
	 * This function returns tau value
	 * 
	 * @return double
	 */
	public double getTau() {
		return this.tau;
	}

	/**
	 * This function returns weigths (in %) calculated for all the localized
	 * structures
	 * 
	 * @return an array that contains the weights
	 */
	public double[] getWeights() {
		return this.ciWeights;
	}

	/**
	 * This function tests if this method implements a hamiltonian
	 * 
	 * @return true if a hamitionian calculation is implemented
	 */
	public boolean isImplementedHamiltonian() {
		return false;
	}

	/**
	 * This function tests if this method implements a tau calculation
	 * 
	 * @return true if a tau calculation is implemented
	 */

	public boolean isImplementedTau() {
		return true;
	}

	/**
	 * This method sets the mesomery that implements this method
	 * 
	 * @param mesomery
	 *            to set
	 */
	public void setMesomery(Mesomery mesomery) {
		this.mesomery = mesomery;
	}

	/**
	 * Computes the overlap between structure i and j of the mesomery object
	 * 
	 * @param i
	 *            : idx of structure in the mesomery
	 * @param j
	 *            : idx of structure in the mesomery
	 * @return (double) overlap between Phi_i and Phi_j
	 * @throws NoSOMAvailableException 
	 * @throws NullOvlMatrixException
	 * @throws NoElectronException 
	 * @throws UnavailableSOMException 
	 * @throws OrbitaleMoleculaireException 
	 * @throws HLPOverlapException 
	 * @throws Exception
	 */
	public double computeOverlap(int i, int j) throws NoElectronException, NoSOMAvailableException, NullOvlMatrixException, UnavailableSOMException, OrbitaleMoleculaireException, HLPOverlapException {
		// if (i==j) return 1;
		Structure Phi1;
		Structure Phi2;

		// version 1.10 du fichier mauvaise
		// remise en place de la ligne
		//		this.mesomery.getDelocalizedStructure().setTotalSpin();
		if (i == -1) {
			Phi1 = this.mesomery.getDelocalizedStructure();
		} else {
			Phi1 = this.mesomery.getSelectedLocalizedStruct(i);
		}
		if (j == -1) {
			Phi2 = this.mesomery.getDelocalizedStructure();
		} else {
			Phi2 = this.mesomery.getSelectedLocalizedStruct(j);
		}
		double tol = 1e-6;
		double norm1 = Math.sqrt(Structure.overlap(Phi1, Phi1));
		//		if (Math.abs(norm1-1.0)>tol) HuckelIO.abandon(this.getClass(), "Phi1 non normalisee");
		double norm2 = Math.sqrt(Structure.overlap(Phi2, Phi2));
		//		if (Math.abs(norm2-1.0)>tol) HuckelIO.abandon(this.getClass(), "Phi2 non normalisee");

		if (norm1 * norm2 < tol) {
			throw new ArithmeticException(norm1 + " " + norm2 + " "
					+ "Stop before division by 0");
		}
		double ovl = Structure.overlap(Phi1, Phi2);
		return ovl / (norm1 * norm2);
	}
	
	

	private Mesomery getMesomery() {
		return this.mesomery;
	}

	/**
	 * This method computes the overlap matrix as a double[][] array used in the
	 * mesomery
	 * 
	 * @throws NullOvlMatrixException
	 * @throws OrbitaleMoleculaireException 
	 * @throws HLPOverlapException 
	 */
	protected void computeOverlapTab() throws NullOvlMatrixException,
	NoElectronException, OrbitaleMoleculaireException, HLPOverlapException {
		int nbStruc = this.mesomery.countSelectedLocalizedStructs();
		this.overlapTab = null;
		try {
			this.overlapTab = new double[nbStruc + 1][nbStruc + 1];
			for (int i = -1; i < nbStruc; i++) {
				for (int j = i; j < nbStruc; j++) {
					this.overlapTab[j + 1][i + 1] = this.overlapTab[i + 1][j + 1] = this
							.computeOverlap(i, j);
				}
			}
		} catch (NoElectronException e) {
			HuckelIO.warning(this.getClass().getName(), "computeOverlapTab", e
					.getMessage(), e);
			this.overlapTab = null;
			throw new NullOvlMatrixException();
		} catch (NoSOMAvailableException e) {
			HuckelIO.warning(this.getClass().getName(), "computeOverlapTab", e
					.getMessage(), e);
			this.overlapTab = null;
			throw new NullOvlMatrixException();
		} catch (UnavailableSOMException e) {
			HuckelIO.warning(this.getClass().getName(), "computeOverlapTab", e
					.getMessage(), e);
			this.overlapTab = null;
			throw new NullOvlMatrixException();
		}
	}

	/* 	protected void computeOverlapTab() throws NullOvlMatrixException,
	NoElectronException, OrbitaleMoleculaireException, HLPOverlapException {
		int nbStruc = this.mesomery.countSelectedLocalizedStructs();
		this.overlapTab = null;
		try {
			this.overlapTab = new double[nbStruc + 1][nbStruc + 1];
			ArrayList<Double> overlapTab0 = new ArrayList<Double>();
			for (int i = -1; i < nbStruc; i++) {
				overlapTab0.add(this.computeOverlap(i, -1));
			}
			Collections.sort(overlapTab0, new Comparator<Double>() {
		        public int compare(Double arg0, Double arg1) {
					return new Double(Math.abs(arg1)).compareTo( new Double (Math.abs(arg0)));
				}
		    });
			for (int k= 0 ; k < overlapTab0.size()-1; k++)
				this.overlapTab[k][0]=this.overlapTab[0][k]=overlapTab0.get(k);
			
			for (int i = 0; i < nbStruc; i++) {
				for (int j = i; j < nbStruc; j++) {
					this.overlapTab[j + 1][i + 1] = this.overlapTab[i + 1][j + 1] = this
							.computeOverlap(i, j);
				}
			}
		} catch (NoElectronException e) {
			HuckelIO.warning(this.getClass().getName(), "computeOverlapTab", e
					.getMessage(), e);
			this.overlapTab = null;
			throw new NullOvlMatrixException();
		} catch (NoSOMAvailableException e) {
			HuckelIO.warning(this.getClass().getName(), "computeOverlapTab", e
					.getMessage(), e);
			this.overlapTab = null;
			throw new NullOvlMatrixException();
		} catch (UnavailableSOMException e) {
			HuckelIO.warning(this.getClass().getName(), "computeOverlapTab", e
					.getMessage(), e);
			this.overlapTab = null;
			throw new NullOvlMatrixException();
		}
	}*/
	 
	
	/*
	 * (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethod#getPriority()
	 */
	public int getPriority() {
		return 1;
	}

	public IMethodException getError() {
		// TODO Auto-generated method stub
		return this.error;
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethod#newInstance()
	 */
	public IMethod newInstance() {
		return new HLPMethod();
	}

	/**
	 * 
	 * @param i : column
	 * @param j : row
	 * @param n : value to set
	 * @return
	 * @throws NoElectronException
	 * @throws NoSOMAvailableException
	 * @throws NullOvlMatrixException
	 * @throws UnavailableSOMException
	 * @throws OrbitaleMoleculaireException
	 * @throws HLPOverlapException
	 */
	protected void updateOverlapTab(int i, int j, double n) throws NoElectronException, NoSOMAvailableException, NullOvlMatrixException, UnavailableSOMException, OrbitaleMoleculaireException, HLPOverlapException {
		this.getOverlapTabAll()[i][j] = n;
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOverlap#removeInOverlapMatrix(int)
	 */
	public void removeInOptimizableMatrix(int i) {
		try {
			this.computeOverlapTab();
		} catch (NullOvlMatrixException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
			e.printStackTrace();
		} catch (NoElectronException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		} catch (OrbitaleMoleculaireException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		} catch (HLPOverlapException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		}
				
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOptimizableMatrixp#computeOptimizablepMatrix(int)
	 */
	public void computeOptimizableMatrix(int i) {
		try {
			this.computeOverlapTab();
		} catch (NullOvlMatrixException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
			e.printStackTrace();
		} catch (NoElectronException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		} catch (OrbitaleMoleculaireException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		} catch (HLPOverlapException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		}
				
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOptimizableMatrix#extendOptimizableMatrix()
	 */
	public void extendOptimizableMatrix() {
		try {
			this.computeOverlapTab();
		} catch (NullOvlMatrixException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
			e.printStackTrace();
		} catch (NoElectronException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		} catch (OrbitaleMoleculaireException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		} catch (HLPOverlapException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOptimizableMatrix#computeAllOptimizableMatrix()
	 */
	public void computeAllOptimizableMatrix() {
		try {
			this.computeOverlapTab();
		} catch (NullOvlMatrixException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
			e.printStackTrace();
		} catch (NoElectronException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		} catch (OrbitaleMoleculaireException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		} catch (HLPOverlapException e) {
			HuckelIO.warning(this.getClass().getName(), "extendOverlapMatrix", e
					.getMessage(), e);
		}
				
	}
	
	
	
	
	/*JAMAIS UTILISE MAIS A UTILISER SEUELEMNT QUAND CE SERA  DEBUGGUE ET TESTE PAR  removeInOverlapMatrix
	 * 
	 * @param i
	 */
	private void removeOverlapAllHLP(int i){
			
		if (i==-1)
			return;

		int ligToCopy;
		int colToCopy;		
		int nbStruct =mesomery.countSelectedLocalizedStructs();
		double overlapTab[][] = new double[nbStruct-1][nbStruct-1];
		double[][] hlpOverTabAll = null;
		try {
			hlpOverTabAll = this.getOverlapTabAll();
			
			for (int lig = 0; lig < nbStruct; lig ++) {
				for (int col = 0; col < nbStruct; col ++) {
					System.out.print(hlpOverTabAll[col][lig]+" ");
				}
				System.out.println();
			}
			System.out.println();
			System.out.println();
		
		} catch (NoElectronException e) {
			HuckelIO.warning(this.getClass().getName(), "removeOverlapAllHLP", e
					.getMessage(), e);
			e.printStackTrace();
		} catch (NullOvlMatrixException e) {
			HuckelIO.warning(this.getClass().getName(), "removeOverlapAllHLP", e
					.getMessage(), e);
			e.printStackTrace();
		} catch (OrbitaleMoleculaireException e) {
			HuckelIO.warning(this.getClass().getName(), "removeOverlapAllHLP", e
					.getMessage(), e);
			e.printStackTrace();
		} catch (HLPOverlapException e) {
			HuckelIO.warning(this.getClass().getName(), "removeOverlapAllHLP", e
					.getMessage(), e);
			e.printStackTrace();
		}
		if (hlpOverTabAll == null)
			return;
		for (int lig = 0; lig < nbStruct; lig ++) {
			for (int col = 0; col < nbStruct; col ++) {
				if (col < i)
					colToCopy = col;
				else
					colToCopy = col-1;

				if (lig < i)
					ligToCopy = lig;
				else
					ligToCopy = lig-1;

				overlapTab[col][lig] = hlpOverTabAll [colToCopy][ligToCopy];
				System.out.print(overlapTab[col][lig]+" ");
			}
			System.out.println();
		}
		this.overlapTab  = overlapTab;
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOptimizableMatrix#checkOptimizableMatrix()
	 */
	public boolean checkOptimizableMatrix() {
		return true;
	}

	public ArrayList<IMethodWarning> getWarnings() {
		return warnings;
	}



}