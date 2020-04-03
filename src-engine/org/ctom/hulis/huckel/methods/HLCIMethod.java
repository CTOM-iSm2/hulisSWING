package org.ctom.hulis.huckel.methods;

import java.io.Serializable;
import java.util.ArrayList;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.HLCIException;
import org.ctom.hulis.huckel.exception.HLCIValBnException;
import org.ctom.hulis.huckel.exception.HLPException;
import org.ctom.hulis.huckel.exception.HLPOverlapException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.NoElectronException;
import org.ctom.hulis.huckel.exception.NullOvlMatrixException;
import org.ctom.hulis.huckel.exception.OrbitaleMoleculaireException;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.huckel.warning.IMethodWarning;
import org.ctom.hulis.huckel.warning.IMethodWarning.HulisWarningType;
import org.ctom.hulis.util.VectorssHuckel;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.common.Arrays;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;

/**
 * This class describes the HLCI method calculation for the mesomery : hamiltonian, Ci, weights 
 * @author ngoudard
 *
 */
public class HLCIMethod implements IMethod, Cloneable, IMethodHasOptimizableMatrix, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4116421317538357713L;
	private double[][] coeffB; // matrix B coeff
	private double[] ciHLCI;  // CI vector
	private double[][] hamiltonian; // Hamiltonian
	private Mesomery mesomery;   // mesomery that implements HLCI method
	private double[] weightsHLCIArray; // weights
	double tau;
	boolean isImplementedTau ;

	IMethodException error;
	private ArrayList<IMethodWarning> warnings = new ArrayList<IMethodWarning>();

	/**
	 * This function calculates and the Bn value of this mesomery
	 * @return Bn Value
	 * @throws HLCIValBnException 
	 */

	public double calculateValBn() throws HLCIValBnException {
		// in other cases (more than one localized form) :
		int i, j;
		int nbSelLocalizedStructs = mesomery.countSelectedLocalizedStructs();

		StructureLocalized s = null;
		double delocalizedEnergy = mesomery.getDelocalizedStructure()
				.getTotalEnergy().getBeta();
		double deltaEi = 0.0;
		Matrix matC = new Matrix(nbSelLocalizedStructs, nbSelLocalizedStructs);

		// calculating the first matrix
		s = null;
		for (i = 0; i < nbSelLocalizedStructs; i++) {
			s = mesomery.getSelectedLocalizedStruct(i);
			for (j = 0; j < nbSelLocalizedStructs; j++) {
				if (i == j)
					matC.set(i, j, 1);
				else {
					deltaEi = s.getTotalEnergy().getBeta() - delocalizedEnergy;
					if (deltaEi == 0) {
						throw new HLCIValBnException("divide by zero");
					}
					try {
						matC.set(i, j, this.getCoeffB()[i][j]  / deltaEi);
					} catch (Exception e) {
						throw new HLCIValBnException("fail to initialize matrix");

					}
				}
			}

		}

		// eigen values decomposition
		EigenvalueDecomposition eig = null;
		try {
			eig = new EigenvalueDecomposition(matC);
		} catch (Exception e) {
			throw new HLCIValBnException("EigenvalueDecomposition error");
		}

		double[] eigAn = eig.getRealEigenvalues();

		// calculating Bn

		double n = 0.0;
		int nbPositive = 0;
		double valBn = 0.0;

		// returns exception if too many positive Bn.
		for (i = 0; i < nbSelLocalizedStructs; i++) {
			n = -(1 / (eigAn[i] - 1));
			if (n > 0) {
				valBn = n;
				nbPositive++;
				if (nbPositive > 1) {
					throw new HLCIValBnException("to much positive Bn");
				}
			}
		}

		return valBn;
	} //calculate valBn



	/**
	 * Constructor
	 */
	public HLCIMethod() {
		this.ciHLCI = new double[0];
		this.weightsHLCIArray = new double[0];
		this.hamiltonian = new double[0][0];
		this.tau = 0.0;
		this.isImplementedTau = false;
		this.error = null;
		this.coeffB = null;
	}

	/**
	 * Constructor by copy. copy hamiltonian, Ci, weights 
	 * @param method to copy
	 */
	public HLCIMethod(HLCIMethod method) {
		this.mesomery = method.mesomery;
		this.ciHLCI = Arrays.arrayCopy(method.ciHLCI);
		this.weightsHLCIArray = Arrays.arrayCopy(method.weightsHLCIArray);
		this.hamiltonian = Arrays.arrayCopy(method.hamiltonian);
		this.tau = method.tau;
		this.isImplementedTau = method.isImplementedTau;
		this.error = method.error;
		if (this.coeffB != null)
			this.coeffB = Arrays.arrayCopy(method.coeffB);

	}

	/**
	 * This method generate a matrix (coeff B) used in the weights calculation
	 */

	private  void generateCoeffB() {
		int nbStruc = mesomery.countSelectedLocalizedStructs();
		if (nbStruc == 0) {
			this.coeffB = new double[0][0];
			return;
		}
		int i;
		int j;
		double[][] coeffB = new double[nbStruc][nbStruc];
		for (i = 0; i < nbStruc; i++) {
			for (j = 0; j < nbStruc; j++) {
				coeffB[i][j] = 1.0;
			}
		}

		this.coeffB = coeffB;
	}

	/**
	 * 
	 * @return
	 */
	public synchronized double[][] getCoeffB() {
		if (this.coeffB == null)
			return new double[][] {};
		;
			return this.coeffB.clone();
	}

	/**
	 * This method calculates HLCI hamiltonian, Ci, weights
	 */
	public void calculate() throws IMethodException {		
		this.ciHLCI = new double[0];
		this.weightsHLCIArray = new double[0];
		this.hamiltonian = new double[0][0];
		this.tau = 0.0;
		this.isImplementedTau = false;
		this.error = null;

		// weight = 100 % if there is only one localized form
		if (this.mesomery.countSelectedLocalizedStructs() == 1) {
			StructureLocalized struct = this.mesomery.getSelectedLocalizedStruct(0);
			struct.setWeight(getName(), 1.0);

			this.weightsHLCIArray = new double[1];
			this.weightsHLCIArray[0] = 1.0;
			this.ciHLCI = new double[1];
			this.ciHLCI[0] = 1.0;
			try {
				this.tau = this.computeTau();
			} catch (NullOvlMatrixException e) {
				this.error = new HLCIException(e);;
				throw error;
			} catch (NoElectronException e) {
				this.error = new HLCIException(e);;
				throw error;
			} catch (OrbitaleMoleculaireException e) {
				this.error = new HLCIException(e);;
				throw error;
			} catch (HLPOverlapException e) {
				this.error = new HLCIException(e);;
				throw error;
			}
			catch (Exception e) {
				this.error = new HLPException(e);
				throw error;
			}
			return;
		}

		this.weightsHLCIArray = new double[this.mesomery
		                                   .countSelectedLocalizedStructs()];
		double[][] eigV = new double[0][0];
		int idxHighestEnergy = 0;

		generateCoeffB();
		if (this.coeffB.length == 0)
			return;

		// in other cases (more than one localized form) :
		int i,j;

		double valBn = 0.0;
		try {
			valBn = this.calculateValBn();
		} catch (HLCIValBnException e) {
			this.error = new HLCIException (e);
			throw error;
		}
		StructureLocalized s = null;
		Matrix matC;
		int nbSelLocalizedStructs = this.mesomery
				.countSelectedLocalizedStructs();

		// returns exception if no one negative Bn
		if (valBn == 0.0) {
			this.error = new HLCIException("no negative Bn");
			throw error;
		}

		// calculating the second matrix
		double deltaEi = 0.0;

		try {
			matC = new Matrix(nbSelLocalizedStructs, nbSelLocalizedStructs);
		} catch (Exception e1) {
			this.error = new HLCIException("Out of memory");
			throw error;
		}

		s = null;
		for (i = 0; i < nbSelLocalizedStructs; i++) {
			if (this.mesomery.isStopConsumingProccess())
				return;
			s = this.mesomery.getSelectedLocalizedStruct(i);
			for (j = 0; j < nbSelLocalizedStructs; j++) {
				if (this.mesomery.isStopConsumingProccess())
					return;
				if (i == j) {
					deltaEi = s.getTotalEnergy().getBeta(); // -
					// delocalizedEnergy;
					matC.set(i, j, deltaEi);
				} else
					matC.set(i, j, this.getCoeffB()[i][j] * valBn);
			}
		}

		this.hamiltonian = matC.getArray();
		EigenvalueDecomposition eig = null;
		// Eigen Values decomposition
		try {
			eig = new EigenvalueDecomposition(matC); // Exception if wrong
			// matrix
		} catch (Exception e) {
			this.error = new HLCIException("EigenvalueDecomposition error");
			throw error;
		}

		// get the smallest energy

		double[] eigValues = VectorssHuckel.sort(eig.getRealEigenvalues());

		idxHighestEnergy = 0;

		for (i = 0; i < eigValues.length; i++) {

			if (eigValues[i] > eigValues[idxHighestEnergy]) {
				idxHighestEnergy = i;
			}
		}


		if (this.mesomery.isStopConsumingProccess())
			return;

		// calculate PhiTot : get the vector corresponding to the highest energy

		eigV = VectorssHuckel.sort(eig.getV().getArray());

		this.ciHLCI = new double[nbSelLocalizedStructs];
		for (i = 0; i < nbSelLocalizedStructs; i++)
			this.ciHLCI[i] = (eigV[i][idxHighestEnergy]);

		for (i = 0; i < nbSelLocalizedStructs; i++) {
			StructureLocalized struct = this.mesomery.getSelectedLocalizedStruct(i);
			this.weightsHLCIArray[i] = (eigV[i][idxHighestEnergy] * eigV[i][idxHighestEnergy]);
			struct.setWeight(getName(), this.weightsHLCIArray[i]);
		}


		try {
			this.tau = computeTau();
		} catch (NullOvlMatrixException e) {
			this.error = new HLCIException(e);
			throw error;
		} catch (NoElectronException e) {
			this.error = new HLCIException(e);
			throw error;
		} catch (OrbitaleMoleculaireException e) {
			this.error = new HLCIException(e);
			throw error;
		} catch (HLPOverlapException e) {
			this.error = new HLCIException(e);
			throw error;
		}
	}
	/**
	 * calculate tau
	 * @return
	 * @throws NullOvlMatrixException 
	 * @throws NoElectronException 
	 * @throws OrbitaleMoleculaireException 
	 * @throws HLPOverlapException 
	 */
	public double computeTau () throws NullOvlMatrixException, NoElectronException, OrbitaleMoleculaireException, HLPOverlapException {
		double tau = 0.0;

		HLPMethod hlpMethod = (HLPMethod) this.mesomery.getMethod("HLP");
		if (hlpMethod == null) {
			this.isImplementedTau = false;
			return 0.0;
		}

		this.isImplementedTau = true;

		double[][] hlpOverlap = hlpMethod.getOverlapTabAll();
		
//		int n=hlpOverlap[0].length;
//		for (int i=1; i<n; i++) {
//			for (int j=1; j<n; j++) {
//				if (i==j) {
//					hlpOverlap[i][j]=1;
//				} else {
//					hlpOverlap[i][j]=0;
//				}
//			}
//		}


		adjustOvlForHLCI(hlpOverlap);
		warnForNegativeOverlap(hlpOverlap);

		double[] ciHLCITemp = new double[ciHLCI.length];
		HuckelIO.PrintIfln("CALCUL DE TAU HLCI ");


		double som = 0.0;

		/*
		 * Dans les boucles suivantes la matrice recouvrement a une ligne et une colonne
		 * de plus que le tableau de coef n'a d'element car la ligne 0 est reservée pour la 
		 * structure delocalisee
		 */
		for (int iStructLoc = 1 ; iStructLoc<  hlpOverlap.length; iStructLoc++ ){
			for(int jStructLoc = 1; jStructLoc< hlpOverlap[0].length ; jStructLoc++) {
				som +=  ciHLCI[iStructLoc-1] * ciHLCI[jStructLoc-1] * hlpOverlap[iStructLoc] [jStructLoc];
			}
		}
		double sqrSom =  Math.sqrt(som);
		for (int iStructLoc = 0 ; iStructLoc<ciHLCI.length; iStructLoc++)	{
			ciHLCITemp[iStructLoc] = ciHLCI[iStructLoc]  / sqrSom;
			HuckelIO.PrintIfln ("coeff renormés" + iStructLoc + " - " + ciHLCITemp[iStructLoc] + " = " + ciHLCI[iStructLoc] + "/" +  sqrSom );
		}

		som = 0.0;
		for (int iStructLoc = 1; iStructLoc < hlpOverlap[0].length; iStructLoc++) {
			som += ciHLCITemp[iStructLoc-1] * hlpOverlap[iStructLoc] [0];	
			HuckelIO.PrintIfln( "overlap " + iStructLoc + " "+ciHLCITemp[iStructLoc-1] + "*" + hlpOverlap[iStructLoc] [0]);
		}

		tau = som;


		return tau;
	}

	private void warnForNegativeOverlap(double[][] hlpOverlap) {
		int size = hlpOverlap[0].length;
		for (int i =0; i<size; i++) {
			for (int j=0; j< size; j++) {
				if (hlpOverlap[i][j] < 0) {
					this.warnings.add(new IMethodWarning(this,"A negative term appears in the overlap matrix.\nHL-CI method might give unreliable results.\n", HulisWarningType.UNAPPROPRIATE_METHOD));
					return;
				}
			}
		}
	}

	/**
	 * Modification du calcul de tau dans HLCI suite a notre discussion de fin janvier 2015 :
	 * Si une structure localisee recouvre negativement avec la structure
	 * de reference et que ce signe est du a un couplage, alors on prend
	 * la valeur absolue de ce recouvrement. Ainsi, que que soit l'ordre
	 * dans lequel on couples les radicaux, tau est toujours maximal.
	 * 
	 * OFF : modification abandonnée au proifit d'un message dans la fenetre resultats.
	 * 
	 * @param hlpOverlap
	 */
	private void adjustOvlForHLCI(double[][] hlpOverlap) {
		return;
		/*		double[] ovl0 = hlpOverlap[0];
		for (int i=1; i<ovl0.length; i++) {
			if (ovl0[i]<0) {
				StructureLocalized struct = this.mesomery.getSelectedLocalizedStruct(i-1);
				if (struct.aUnCouplage()) {
					hlpOverlap[0][i] *= -1;
					hlpOverlap[i][0] *= -1;
				}
			}
		}
		 */
	}



	/**
	 * This function returns a copy of the HLCI method
	 * @return a copy of hamiltonian, Ci, weights 
	 */
	@Override
	public Object clone() {
		try {
			super.clone();
		}
		 catch (CloneNotSupportedException e) {
			return null;
		}
		return new HLCIMethod(this);
	}

	/**
	 * This function gets the calculated Ci vector
	 * @return a double vector
	 */
	public double[] getCI() {
		//NG : incompatible GWT
		double [] ciHLCIclone = new double [ciHLCI.length];
		System.arraycopy(ciHLCI, 0, ciHLCIclone, 0, ciHLCI.length);
		return ciHLCIclone;
		//return this.ciHLCI.clone();
	}

	/**
	 * This function tests if this method implements a hamiltonian
	 * @return true if a hamitionian calculation is implemented
	 */
	public boolean isImplementedHamiltonian(){
		return true;
	}

	/**
	 * This function return the HLCI hamiltonian
	 * @return a double array
	 */
	public double[][] getHamiltonian() {
		return this.hamiltonian;
	}

	/**
	 * This function returns the method name
	 * @return string
	 */
	public String getName() {
		return "HLCI";
	}


	/**
	 * This function returns tau value
	 * @return double
	 */
	public double getTau() {
		if (!this.isImplementedTau())
			throw new ArithmeticException();
		return this.tau;
	}

	/**
	 * This function returns weigths (in %) calculated for all the localized
	 * structures
	 * 
	 * @return an array that contains the weights
	 */
	public double[] getWeights(){
		return weightsHLCIArray.clone();

	}

	/**
	 * This function tests if this method implements a tau calculation
	 * @return true if a tau calculation is implemented
	 */

	public boolean isImplementedTau() {
		return this.isImplementedTau;
	}

	/**
	 * This method sets the mesomery that implements this method
	 * @param mesomery to set
	 */

	public void setMesomery(Mesomery mesomery) {
		this.mesomery = mesomery;
	}

	public int getPriority() {
		return 2;
	}



	public IMethodException getError() {
		// TODO Auto-generated method stub
		return this.error;
	}



	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethod#newInstance()
	 */
	public IMethod newInstance() {
		return new HLCIMethod();
	}



	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOptimizableMatrix#removeInOptimizableMatrix(int)
	 */
	public void removeInOptimizableMatrix(int i) {
		this.generateCoeffB();

	}



	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOptimizableMatrix#computeOptimizableMatrix(int)
	 */
	public void computeOptimizableMatrix(int i) {
		this.generateCoeffB();

	}



	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOptimizableMatrix#extendOptimizablepMatrix()
	 */
	public void extendOptimizableMatrix() {
		this.generateCoeffB();

	}



	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOptimizableMatrix#computeAllOptimizableMatrix()
	 */
	public void computeAllOptimizableMatrix() {
		this.generateCoeffB();

	}



	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethodHasOptimizableMatrix#checkOptimizableMatrix()
	 */
	public boolean checkOptimizableMatrix() {
		int i;
		int j;

		int nbSelLocalizedStructs = mesomery.countSelectedLocalizedStructs();
		// tests the coeffB structure : exception if wrong

		if (this.coeffB == null)
			this.generateCoeffB();

		for (i = 0; i < nbSelLocalizedStructs; i++) {
			if (this.coeffB[i][i] != 1.0) {
				this.error =  new HLCIException(
						"incorrect matrix of coeff B : diagonal must be equal to 1");
				return false;
			}
		}

		for (i = 0; i < nbSelLocalizedStructs; i++) {
			for (j = 0; j < nbSelLocalizedStructs; j++) {
				if (this.coeffB[i][j] != this.coeffB[i][j]) {
					this.error = new HLCIException("matrix must be symetric");
					return false;

				}

			}
		}
		return true;
	}

	public ArrayList<IMethodWarning> getWarnings() {
		return warnings;
	}

}
