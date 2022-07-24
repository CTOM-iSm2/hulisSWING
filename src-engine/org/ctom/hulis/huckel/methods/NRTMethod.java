package org.ctom.hulis.huckel.methods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.huckel.warning.IMethodWarning;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.common.Arrays;

import Jama.Matrix;
import flanagan.math.Minimisation;
public class NRTMethod implements Cloneable, IMethod, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6363018023821850481L;
	private static final double THRS = 1E-8;
	private double[] ciCoeff; // Ci vector
	private double[] ciWeights; // weights array
	private Mesomery mesomery; // mesomery that implements HLP method
	final private String name = "NRT";
	private Fonction NRTfonction= new Fonction();
	private double delta0;
	private NRTComputeMode Mode = NRTComputeMode.NONE;
	enum NRTComputeMode {
		NONE, DELTA0, NRT;
	}


	Matrix matTrue;
	Matrix[] matLoc;
	double[] weights;
	int nMatLoc;
	int nBas;

	IMethodException error;

	/**
	 * Constructor by copy
	 */
	public NRTMethod(NRTMethod method) {
		super();
		this.mesomery = method.getMesomery();
		this.ciCoeff = Arrays.arrayCopy(method.ciCoeff);
		this.ciWeights = Arrays.arrayCopy(method.ciWeights);
		this.error = method.error;
	}

	public NRTMethod() {
		this.ciCoeff = new double[0];
		this.ciWeights = new double[0];
		this.error = null;
	}

	public void calculate() throws IMethodException {

		//NG : ajout condition car bug quand on oubre un fichier 
		if (this.mesomery.countLocalizedStructs() ==0)
			return;

		/*		try {
			nBas = this.mesomery.getDelocalizedStructure().getnOccSO();
		} catch (NoSOMAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnavailableSOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 */
		List<StructureLocalized> selectedLocalizedStructures = this.mesomery.getSelectedLocalizedStructures();
		nMatLoc = selectedLocalizedStructures.size();
		matLoc = new Matrix[nMatLoc];

		matTrue = this.mesomery.getDelocalizedStructure().getRestrictedDensityMatrix();
		nBas = matTrue.getColumnDimension();
		HuckelIO.PrintIf("Matrice densite delocalisee:");
		matTrue.print(10, 5);
		for (int i = 0; i<nMatLoc; i++) {
			matLoc[i] = this.mesomery.getLocalizedStruct(i).getRestrictedDensityMatrix();
			HuckelIO.PrintIf("Matrice loc "+i);
			matLoc[i].print(10, 5);
		}
		calculateDelta0();
		calculateNRT();

	}

	/**
	 * Computes the NRT weights
	 */
	private void calculateNRT() {
		this.setMode(NRTComputeMode.NRT);
		Minimisation minimisation = new Minimisation();		
		Fonction fonction = new Fonction();
		this.NRTfonction=fonction;

		fonction.setSystem(nBas, nMatLoc, matTrue, matLoc);
		double[] tab=new double[nMatLoc];
		double[] steps = new double[this.nMatLoc];
		int[] pIndices = new int[nMatLoc];
		int[] plusOrMinus = new int[nMatLoc];
		for (int i=0; i<this.nMatLoc; i++) {
			pIndices[i]=i;
			steps[i] = 0.001;
			plusOrMinus[i]=1;
			if (i==0) { 
				tab[0]=1.0/nMatLoc;
				steps[i]=-0.1;
			} else {
				tab[i]=1.0/nMatLoc;
				steps[i]=0.1;
			}
			minimisation.addConstraint(i, 1, 1);   //w<1
			minimisation.addConstraint(i, -1, 0);  //w>0
		}
		minimisation.addConstraint(pIndices, plusOrMinus, 1, 1); //sum w<1
		minimisation.setNmax(10000);
		minimisation.nelderMead(fonction, tab,steps,THRS);
		this.weights = minimisation.getParamValues();
		//		fonction.normalise(this.weights);

		/*TODO
		 *  Vraiment un truc a virer
		 *  NG : petite correction : on ne pend que les structure selectionnees
		 *  NG : A supprimer donc : reimplementer getWeight() dans le fichier StructureLocalized, avec qqchose du genre
		 *  return if this.getMesomeryParent()!=null return this.getMesomeryParent().getMethod(name).getWeight(this.getIndex()) else return 0.0
		 *  et tester si pas effet de bord
		 *  
		 */
		//on repercute les poids vers les structures de la mesomerie
		StructureLocalized struct;
		int n = this.mesomery.countSelectedLocalizedStructs();
		for (int i = 0; i < n; i++) {
			struct = this.mesomery.getSelectedLocalizedStruct(i);
			struct.setWeight(this.getName(), this.getWeights()[i]);
		}
		this.setMode(NRTComputeMode.NONE);
	}

	private void setMode(NRTComputeMode mode) {
		this.Mode = mode;
	}

	/**
	 * Computes the weight for each structure alone
	 * and keep the highest called delta0
	 */
	private double calculateDelta0() {

		this.setMode(NRTComputeMode.DELTA0);
		Minimisation minimisation;		
		Fonction fonction;
		double omega = Double.MIN_VALUE;
		int iLoc = -1;
		for (int i=0; i<matLoc.length; i++) {
			minimisation = new Minimisation();		
			fonction = new Fonction();
			Matrix[] rhoLoc = new Matrix[1];
			rhoLoc[0] = matLoc[i];
			fonction.setSystem(nBas, 1, matTrue, rhoLoc);
			double[] tab=new double[1];
			double[] steps = new double[1];
			steps[0] = 0.001;
			tab[0]=1.0;
			minimisation.addConstraint(0, 1, 1);   //w<1
			minimisation.addConstraint(0, -1, 0);  //w>0
			minimisation.setNmax(10000);
			minimisation.nelderMead(fonction, tab,steps,THRS);
			double w = minimisation.getParamValues()[0];
			if (w>omega) {
				iLoc=i;
				omega=w;
			}
		}
		Matrix mat = matLoc[iLoc].minus(this.matTrue);
		//		mat.times(omega);
		mat=mat.times(mat);
		delta0 = Math.sqrt(mat.trace());
		this.setMode(NRTComputeMode.NONE);
		return delta0;
	}

	public int getPriority() {
		return 0;
	}

	public double[] getCI() {
		return null;
	}

	public double[][] getHamiltonian() {
		return null;
	}

	public boolean isImplementedHamiltonian() {
		return false;
	}

	public String getName() {
		return this.name;
	}

	public double getTau() {
		return 1-this.NRTfonction.getResidual();
	}

	public double[] getWeights() {
		return this.weights;
	}

	public boolean isImplementedTau() {
		return true;
	}

	public void setMesomery(Mesomery mesomery) {
		this.mesomery = mesomery;
	}

	public IMethodException getError() {
		// TODO Auto-generated method stub
		return null;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return new NRTMethod(this);
	}

	private Mesomery getMesomery() {
		return this.mesomery;
	}

	private class Fonction implements flanagan.math.MinimisationFunction, Serializable {

		int nBas;
		int nMatLoc;
		Matrix matTrue;
		Matrix[] matLoc;
		private double residual = Double.NaN;

		public double getResidual() {
			return residual;
		}

		public double function(double[] vec) {
			//		normalise(vec);
			Matrix somme=new Matrix(nBas,nBas,0);
			if (HuckelIO.isverbose()) {
				//				System.out.printf("vec :");
			}		
			for (int iMat=0; iMat<nMatLoc; iMat++) {
				//somme = ...+ w_i * D_i + ...
				somme.plusEquals(matLoc[iMat].times(vec[iMat]));
				if (HuckelIO.isverbose()) {
					System.out.printf("%f ", vec[iMat]);
				}			
			}
			Matrix mat = somme.minus(this.matTrue);
			mat=mat.times(mat);
			//						residual = Math.sqrt(mat.trace());
			//						residual = Math.sqrt(mat.trace())/this.matTrue.trace();
			//			residual = Math.abs(somme.normF()-this.matTrue.normF())/this.matTrue.normF();
			//			residual = Math.abs(somme.normF()-this.matTrue.normF())/this.matTrue.normF();
			residual = somme.minus(this.matTrue).normF()/this.matTrue.trace();
			//			EigenvalueDecomposition evd = new EigenvalueDecomposition(mat);
			//			double[] vp = evd.getRealEigenvalues();
			//			residual = 0;
			//			for (double a : vp) {
			//				residual+=a;
			//			}
			//			residual /= this.matTrue.trace();
			if (HuckelIO.isverbose()) {
				//				System.out.printf("\nsomme %f ref %f ", somme.trace(), this.matTrue
				//						.trace());
				System.out.printf("Normes somme %f ref %f residu:%f\n", somme.normF(),
						this.matTrue.normF(), residual);
			}		
			return getResidual(); 
		}

		public void setSystem(int nBas, int nMatLoc, Matrix matTrue,Matrix[] matLoc) {
			this.nBas=nBas;
			this.nMatLoc=nMatLoc;
			this.matTrue=matTrue;
			this.matLoc=matLoc;
		}

	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IMethod#newInstance()
	 */
	public IMethod newInstance() {
		return new NRTMethod();
	}

	public ArrayList<IMethodWarning> getWarnings() {
		// TODO Auto-generated method stub
		return null;
	}

}
