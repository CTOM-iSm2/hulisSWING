package org.ctom.hulis.huckel.structures;

import java.io.Serializable;
import java.util.ArrayList;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.IDebuggable;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.MonoExcitation;
import org.ctom.hulis.huckel.NatOrbitaleMoleculaire;
import org.ctom.hulis.huckel.Spin;
import org.ctom.hulis.huckel.SpinOrbitaleMoleculaire;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.HLPOverlapException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.HulisException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.exception.NoElectronException;
import org.ctom.hulis.huckel.exception.NoSOMAvailableException;
import org.ctom.hulis.huckel.exception.NullOvlMatrixException;
import org.ctom.hulis.huckel.exception.OrbitaleMoleculaireException;
import org.ctom.hulis.huckel.exception.UnavailableSOMException;
import org.ctom.hulis.huckel.listeners.IMoleculeValueListener;
import org.ctom.hulis.huckel.listeners.IMonoExcitationListener;
import org.ctom.hulis.huckel.listeners.IStructureListener;
import org.ctom.hulis.huckel.methods.HLPMethod;
import org.ctom.hulis.huckel.spectrum.DensityOfStates;
import org.ctom.hulis.huckel.structures.symmetry.Symmetry;
import org.ctom.hulis.polynomes.CharacteristicPolynomial;
import org.ctom.hulis.util.VectorssHuckel;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.common.Arrays;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;



public abstract class Structure extends Molecule implements IDebuggable, Cloneable , Serializable, 
IMonoExcitationListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3059111279902006401L;
	
	protected int index; // index in the mesomery
	//	protected static int TotalSpin;
	protected double[][] bondOrdersArray;

	protected double[] chargeDensitiesArray;

	protected double[] distributionElecPiArray;

	protected double[] energiesArray;
	private SpinOrbitaleMoleculaire[] SOM = null;
	private double[][] hamiltonianArray;
	private boolean isEnabledFlyCalculate;
	//protected IMoleculeComponent[] locationsElecPi;
	protected Mesomery mesomeryParent;
	private ArrayList<MonoExcitation> monoExcitations;
	private String name;
	protected double[][] orbitalsCoeffArray;
	protected double sumOfChargeDensities;
	protected int sumOfDistributedElec;
	protected int sumOfPiElec;
	protected Energy totalEnergy;
	private double overlapHLP;
	private int indexSelection;
	private DensityOfStates dos;
	private Symmetry symmetry;
	private SpinOrbitaleMoleculaire[] occSOMs=null;
	private boolean isCloneResultsCacheWhenCloneStructure;
	private ArrayList<NatOrbitaleMoleculaire> naturalOrbitals=new ArrayList<NatOrbitaleMoleculaire>();


	/**
	 * 
	 */
	public Structure() {
		super();
		this.index = 0;
		this.indexSelection = 0;
		this.isCloneResultsCacheWhenCloneStructure= true;
		this.mesomeryParent = null;
		setName("");
		this.totalEnergy = new Energy(0.0, 0.0);
		this.isEnabledFlyCalculate = true;
		this.monoExcitations = new ArrayList<MonoExcitation>();
		this.overlapHLP = 0.0;
	}


	/**
	 * @param structure
	 */
	public Structure(Structure structure) {
		super(structure);

		setName(structure.getName());
		this.index = structure.index;
		this.indexSelection = structure.indexSelection;
		this.isCloneResultsCacheWhenCloneStructure = structure.isCloneResultsCacheWhenCloneStructure;
		this.mesomeryParent = structure.mesomeryParent;
		this.isEnabledFlyCalculate = structure.isEnabledFlyCalculate;
		this.totalEnergy = (Energy) structure.getTotalEnergy().clone();
		this.overlapHLP = structure.getOverlapHLP();
		this.monoExcitations = (ArrayList<MonoExcitation>) structure.monoExcitations
				.clone();


		// copie les Spins OM et met a jour le partenaire avec l'objet copie
		int l=0;
		if (structure.SOM != null)
			l = structure.SOM.length;
		this.SOM = new SpinOrbitaleMoleculaire[l];

		for (int i=0 ; i< l ; i++){
			this.SOM[i] = (SpinOrbitaleMoleculaire) structure.getSOM()[i].clone();
		}
		for (int i=0 ; i< l ; i++) {
			SpinOrbitaleMoleculaire som = structure.getSOM()[i];
			SpinOrbitaleMoleculaire.setPartner(this.getSpinOMByIndex(som.getIndex()), 
					this.getSpinOMByIndex(som.getPartner().getIndex()) );

		}

	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#hashCode()
	 */
	@Override
	public int hashCode () {
		final int multiplier = 23;
		if (hashCode == 0) {
			int code = 133;
			code = multiplier * code + this.getIndex();
			code = multiplier * code + this.getName().hashCode();
			code = multiplier * code +  super.hashCode();
			hashCode = code;
		}
		return hashCode;

	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#equals(java.lang.Object)
	 */
	@Override 
	public boolean equals(Object o) {
		if (!super.equals(o))
			return false;
		
		if (!(o instanceof Structure))
			return false;

		Structure s = (Structure) o;

		if (this.getIndex() != s.getIndex())
			return false;

		if (!this.getName().equals(s.getName()))
			return false;

		return true;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return(this.countAtoms() == 0);
	}

	/**
	 * 
	 * @return
	 */
	public boolean hasDemiElectrons() {
		for (int i=0; i<this.countSpinOM();i++) {
			SpinOrbitaleMoleculaire som;
			try {
				som = this.getSpinOM(i);
				if (som.getSpin()==Spin.DEMI_ELECTRON) 
					return true;
			} catch (NoSOMAvailableException | UnavailableSOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} 

		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getOverlapHLP() {
		return this.overlapHLP;
	}

	/**
	 * 
	 * @param structure
	 */
	public void copyArrays(Structure structure) {
		if (!areCorrectAtomsSeqNum() || countAtoms() == 0) {
			this.clearDataCache();

			return;
		}

		this.hamiltonianArray = Arrays.arrayCopy(structure.hamiltonianArray);
		this.energiesArray = Arrays.arrayCopy(structure.energiesArray);
		this.orbitalsCoeffArray = Arrays.arrayCopy(structure.orbitalsCoeffArray);
		this.distributionElecPiArray = Arrays.arrayCopy(structure.distributionElecPiArray);
		this.chargeDensitiesArray =Arrays.arrayCopy(structure.chargeDensitiesArray);
		this.sumOfChargeDensities  = structure.sumOfChargeDensities;
		this.sumOfDistributedElec  = structure.sumOfDistributedElec;
		this.sumOfPiElec  = structure.sumOfPiElec;
		this.bondOrdersArray = Arrays.arrayCopy(structure.bondOrdersArray);
		//		this.SOM = org.ctom.hulis.util.Arrays.arrayCopy(structure.SOM);

		//		if (this.isActiveFlyCalculate()){
		//			if (this.SOM != null)
		//				for (SpinOrbitaleMoleculaire iSOM : this.SOM) {
		//					iSOM = null;
		//				}
		//			try {
		//				this.generateSpinOM();
		//			} catch (NoElectronException e) {
		//				e.printStackTrace();
		//				HuckelIO.abandon(this.getClass(),"Error in copy of structure.");
		//			}
		//		}
	}

	/**
	 * 
	 */
	private void clearDataCache() {
		this.hamiltonianArray = null;
		this.energiesArray = null;
		this.orbitalsCoeffArray = null;
		this.chargeDensitiesArray = null;
		this.sumOfChargeDensities = 0;
		this.distributionElecPiArray = null;
		this.sumOfDistributedElec = 0;
		this.sumOfPiElec = 0;
		this.bondOrdersArray = null;
		this.SOM = null;
		bondOrdersArray = new double[0][0];
		chargeDensitiesArray = new double[0];
		distributionElecPiArray = new double[0];
		energiesArray= new double[0];
		hamiltonianArray= new double[0][0];
		monoExcitations.clear();
		orbitalsCoeffArray= new double[0][0] ;
		occSOMs=null;
	}

	/**
	 * applies the coupling
	 * 
	 * @author yannick
	 * @throws UnavailableSOMException 
	 * @throws NoSOMAvailableException 
	 * @throws OrbitaleMoleculaireException 
	 */
	protected abstract void appliqueCouplage() throws NoSOMAvailableException, UnavailableSOMException, OrbitaleMoleculaireException;

	/**
	 * @return true if there's a coupling in the structure
	 * @author yannick
	 */
	public abstract boolean aUnCouplage();

	/**
	 * This function returns the bond order of the molecule
	 * 
	 * @return double[][]
	 */
	protected abstract double[][] calcBondOrdersArray();
	


	/**
	 *  This function returns the charges of the molecule
	 * @return
	 */
	abstract protected double[] calcChargeDensitiesArray();
	
	

	/**
	 * This function returns the electronic distribution in the molecule : it
	 * depends on the netCharge and the calculate method DELOCALIZED_METHOD :
	 * two electrons per energy. only one per orbital if the two orbitals are
	 * the same energy LOCALIZED_METHOD : depends of the bond orders
	 * 
	 * @return int [] : electronic distribution sorted by orbital
	 */

	abstract protected double[] calcDistributionElecArray();

	/**
	 * This function returns the total charge of the molecule
	 * 
	 * @return double
	 */

	protected double calcSumOfChargesDensities() {
		double chargeTotale = 0.0;
		double[] tblCharge = chargeDensitiesArray;

		for (int i = 0; i < tblCharge.length; i++) {
			chargeTotale += tblCharge[i];
		}

		return chargeTotale;

	}

	/**
	 * This function returns the sum of the distributed electrons
	 * 
	 * @return Sum of electronic distribution on orbitals
	 */
	protected int calcSumofDistributionElec() {
		double[] repartitionElec = distributionElecPiArray;
		int nbElecPi = 0;

		for (int i = 0; i < repartitionElec.length; i++) {
			nbElecPi += repartitionElec[i];
		}

		return nbElecPi;
	}

	/**
	 * This function returns the number of Pi electrons in the molecule
	 * 
	 * @return for a delocalized struture : sum = Sum of default atom pi electrons + electronic charge of molecule
	 * for a localized structure sum = sum of electrons localized on atoms and electrons on bonds
	 */
	abstract protected int calcSumOfPiElec();

	/**
	 * This method calculates the Hamiltonian Matrix of the molecule
	 * 
	 */
	public void calculate() {
		if (!areCorrectAtomsSeqNum() || countAtoms() == 0) {
			this.clearDataCache();

			return;
		}

		Matrix hamiltonien = getMatrixHamiltonian();
		if (hamiltonien == null)
			return;

		this.hamiltonianArray = hamiltonien.getArray();
		this.energiesArray = calcOrbitalsEnergies();
		this.orbitalsCoeffArray = calcOrbitalsCoefficients(); 
		HuckelIO.PrintMatrix(new Matrix(hamiltonianArray), "hamiltonianArray calculé");
		HuckelIO.PrintMatrix(new Matrix(orbitalsCoeffArray), "orbitalsCoeffArray calculé");

		this.distributionElecPiArray = calcDistributionElecArray();
		this.chargeDensitiesArray = calcChargeDensitiesArray();
		this.sumOfChargeDensities = calcSumOfChargesDensities();
		this.sumOfDistributedElec = calcSumofDistributionElec();
		this.sumOfPiElec = calcSumOfPiElec();
		this.bondOrdersArray = calcBondOrdersArray();
		this.totalEnergy.set(getTotalEnergyAlpha(), getTotalEnergyBeta());
		this.computeNaturalOrbitals();


		try {
			generateSpinOM();
		} catch (NoElectronException e) {
			e.printStackTrace();
			HuckelIO.error(this.getClass().getName(), "calculate", "NoElectronException thrown");
		} catch (NoSOMAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnavailableSOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		calculateOverlapHLP();// a placer imperativement apres generateSpinOM()
	}


	protected abstract double[][] calcOrbitalsCoefficients();



	/**
	 * 
	 * @return
	 */
	protected abstract double[] calcOrbitalsEnergies() ;




	/* a appeler imperativement apres generateSpinOM()
	 * 
	 */
	public void calculateOverlapHLP() {
		if (this.getMesomeryParent() != null) 
			try {
				HLPMethod hlpMethod = new HLPMethod();
				hlpMethod.setMesomery(this.getMesomeryParent());
				// recouvrement entre psitot et this
				if (this.getIndex() < 1) 
					return;
				if (this.getIndex()==1) {
					this.overlapHLP=1; // cas structure delocalizee
					return;
				}
				this.overlapHLP =  hlpMethod.computeOverlap(-1, this.getIndex()-2);
				System.out.println("Psi" + this.getName() +  " - overlap =" + this.overlapHLP);
			} catch (NoElectronException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NoSOMAvailableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (NullOvlMatrixException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnavailableSOMException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (OrbitaleMoleculaireException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (HLPOverlapException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}  catch (Exception e1) {
				// TODO Auto-generated catch block
				HuckelIO.warning(this.getClass().getName(), "calculateOverlapHLP", "cannot compute overlap PSITOT|PSI" + this.getName() + " : index PSITOT = -1 et index PSI" + this.getName() + " = " + ( this.getIndex() -2 ));
				e1.printStackTrace();
			} 
	}




	@Override
	public void debug() {
		HuckelIO.PrintIf("***** Structure ****** ");
		HuckelIO.PrintIf("name : " + this.name);
		super.debug();

		HuckelIO.PrintIf("***** STATS *****");

		HuckelIO.PrintIf("Hamiltonian : ");

		if (this.hamiltonianArray == null) {
			HuckelIO.PrintIf("NULL :");
			return;
		}

		for (int i = 0; i < this.hamiltonianArray.length; i++) {
			for (int j = 0; j < this.hamiltonianArray[i].length; j++) {
				HuckelIO.PrintIf(this.hamiltonianArray[i][j] + " ");
			}
			HuckelIO.PrintIf("\n ");

		}

		HuckelIO.PrintIf("\n ");
		HuckelIO.PrintIf("Energies : ");

		for (int i = 0; i < this.energiesArray.length; i++) {
			HuckelIO.PrintIf(this.energiesArray[i] + " ");
		}
		HuckelIO.PrintIf("\n ");

		HuckelIO.PrintIf("\n ");
		HuckelIO.PrintIf("Orbitals : ");
		for (int i = 0; i < this.orbitalsCoeffArray.length; i++) {
			for (int j = 0; j < this.orbitalsCoeffArray[i].length; j++) {
				HuckelIO.PrintIf(this.orbitalsCoeffArray[i][j] + " ");
			}
			HuckelIO.PrintIf("\n ");

		}

		HuckelIO.PrintIf("\n ");
		HuckelIO.PrintIf("Distribution Elec Pi : ");

		for (int i = 0; i < this.distributionElecPiArray.length; i++) {
			HuckelIO.PrintIf(this.distributionElecPiArray[i] + " ");
		}
		HuckelIO.PrintIf("\n ");

		HuckelIO.PrintIf("\n ");
		System.out
		.println("sum of Pi electrons : " + this.sumOfDistributedElec);

		HuckelIO.PrintIf("total Energy : " + this.totalEnergy.getAlpha()
				+ "alpha" + this.totalEnergy.getBeta() + "beta");

		HuckelIO.PrintIf("\n ");
		HuckelIO.PrintIf("Bond Order matrix : ");
		for (int i = 0; i < this.bondOrdersArray.length; i++) {
			for (int j = 0; j < this.bondOrdersArray[i].length; j++) {
				HuckelIO.PrintIf(this.bondOrdersArray[i][j] + " ");
			}
			HuckelIO.PrintIf("\n ");

		}

		HuckelIO.PrintIf("\n ");
		HuckelIO.PrintIf("Charge Densities: ");

		for (int i = 0; i < this.chargeDensitiesArray.length; i++) {
			HuckelIO.PrintIf(this.chargeDensitiesArray[i]);
		}

		HuckelIO.PrintIf("\n ");
		HuckelIO.PrintIf("sum of Pi charge densities : "
				+ this.sumOfChargeDensities);

	}

	@Override
	public void clear(boolean notify) {
		stopFlyCalculate();
		this.clearDataCache();
		super.clear(notify);
		startFlyCalculate();
	}

	@Override
	public void delete(boolean notify) {
		stopFlyCalculate();
		this.clearDataCache();
		super.delete(notify);
		this.SOM = null;
	}




	/**
	 * @return the index of the structure in the mesomery
	 */
	public int getIndex() {
		return this.index;
	}

	/**
	 * 
	 * @return the index in the selected structures in the mesomery
	 */
	public int getIndexSelection() {
		return this.indexSelection;
	}

	/**
	 * @param the index of the structure in the mesomery
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * 
	 * @param indexSelection
	 */
	public void setIndexSelection(int indexSelection) {
		this.indexSelection = indexSelection;
	}



	/**
	 * generates the molecular spin orbital set in the SOM array
	 * 
	 * @return false if it could not be done (nelec=0?)
	 * @author yannick
	 * @throws UnavailableSOMException 
	 * @throws NoSOMAvailableException 
	 * @throws TooManyExcitationsException 
	 * @throws Exception
	 */
	protected void generateSpinOM() throws NoElectronException, NoSOMAvailableException, UnavailableSOMException{
	
		double[] electronDistrib = this.getDistributionElecPiArray();
	
		if (electronDistrib == null)
			return;
		occSOMs=null;
		//il y a deux fois plus de SOM que d'atomes (alpha/beta)
		int nAt=this.countHuckelAtoms();
		int nSOM=2*nAt;
		this.SOM = new SpinOrbitaleMoleculaire[nSOM];
		int iSOM = 0;
		int idx=0;
		boolean oneSingleElectron=false;
		for (int i = 0; i < electronDistrib.length; i++) {
			SpinOrbitaleMoleculaire somA=null;
			SpinOrbitaleMoleculaire somB=null;
			if (electronDistrib[i]% 1 ==  0) { // if ajoute par NG pour gerer les demi electrons dans le ELSE
				switch ((int) electronDistrib[i]) {
					case 2:
						somA=new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
								Spin.ALPHA);
						somA.setIndex(idx);
						idx++;
						somB=new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
								Spin.BETA);
						somB.setIndex(idx);
						idx++;
						break;
					case 1:
						if (( ! oneSingleElectron)||(this instanceof StructureLocalized)) {
							somA=new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
									Spin.ALPHA);
							somA.setIndex(idx);
							idx++;
							somB=new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
									Spin.NOSPIN_BETA);
							somB.setIndex(idx);
							idx++;
							oneSingleElectron=true;
						} else {
							somA=new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
									Spin.NOSPIN_ALPHA);
							somA.setIndex(idx);
							idx++;
							somB=new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
									Spin.BETA);
							somB.setIndex(idx);
							idx++;
						}
						break;
					default:
						somA = new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
								Spin.NOSPIN_ALPHA);
						somA.setIndex(idx);
						idx++;
						somB = new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
								Spin.NOSPIN_BETA);
						somB.setIndex(idx);
						idx++;
						break;
				}
		
			} else {
				// ELSE ajoute PAR NG pour gere les demi electrons
				somA = new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
						Spin.DEMI_ELECTRON);
				somA.setIndex(idx);
				idx++;
				somB = new SpinOrbitaleMoleculaire(new Energy(1.0,this.energiesArray[i]), getOM(i),
						Spin.NO_DEMI_ELECTRON);
				somB.setIndex(idx);
				idx++;
				// FIN NG
			}	
			// DEBUT NG
			somA.setNbElectrons(electronDistrib[i]);
			somB.setNbElectrons(0);
			somA.setIndexTableOrbitale(i);
			somB.setIndexTableOrbitale(i);
			// FIN NG
			SpinOrbitaleMoleculaire.setPartner(somA,somB);
			getSOM()[iSOM++] = somA;
			getSOM()[iSOM++] = somB;
		}
		this.getOccSOM();
	}

	public int countSpinOM() {
		SpinOrbitaleMoleculaire[] som = this.getSOM();
		if (som == null) return 0;
		return som.length;
	}

	/**
	 * This function returns the bond order matrix of the molecule. rows and
	 * colmuns are sorted by orbital
	 * 
	 * @return double [][]
	 */
	public double[][] getBondOrdersArray() {
		return this.bondOrdersArray;
	}

	/**
	 * This function returns an array containing the charges densties of the
	 * orbital sorted by orbital
	 * 
	 * @return double []
	 */

	public double[] getChargeDensitiesArray() {
		return this.chargeDensitiesArray;
	}

	/**
	 * @return array of size 2 which contains atom numbers of the radicals which
	 *         are coupled
	 * @author yannick
	 */
	protected abstract int[] getCouplage();

	/**
	 * This function returns the distribution of Elec Pi in the molecule, sorted
	 * by orbital
	 * 
	 * @return double []
	 */

	public double[] getDistributionElecPiArray() {
		return this.distributionElecPiArray;
	}

	/*
	 * @Deprecated probably wrong, at least weird 
	 */
	@Deprecated
	public Matrix getDmat() throws HulisException {
		int nSO = getnSO();
		int nOA = getnOA();
		Matrix Dmat = new Matrix(nOA, nOA);

		generateSpinOM();
		for (int mu = 0; mu < nOA; mu++) {
			for (int nu = 0; nu < nOA; nu++) {
				double somme = 0.;
				for (int iSO = 0; iSO < nSO; iSO++) {
					somme += getSpinOM(iSO).getCoeff(mu)
							* getSpinOM(iSO).getCoeff(nu);
				}
				Dmat.set(mu, nu, somme);
			}
		}
		return Dmat;
	}

	public Matrix getRestrictedDensityMatrix() {
		Matrix d = new Matrix(this.getBondOrdersArray());
		return d;
	}

	/**
	 * This function returns an array containing the beta coefficient of the
	 * energies of the molecule
	 * 
	 * @return double []
	 */

	public double[] getEnergiesArray() {
		return this.energiesArray;
	}

	/**
	 * This function returns the hamiltonian matrix of the molecule
	 * 
	 * @return double [][]
	 */
	public double[][] getHamiltonianArray() {
		return this.hamiltonianArray;
	}

	/**
	 * This function returns the Matrix of the hamiltonian
	 * 
	 * @return Matrix
	 */

	abstract protected Matrix getMatrixHamiltonian();

	/**
	 * @return mesomeryParent
	 */
	public Mesomery getMesomeryParent() {
		return this.mesomeryParent;
	}

	/**
	 * returns an array of the recorder monoexcitations
	 * 
	 * @author yannick
	 */
	public ArrayList<MonoExcitation> getMonoExcitations() {
		return this.monoExcitations;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return number of atomic orbiatls
	 * @author yannick
	 */
	private int getnOA() {
		return countHuckelAtoms();
	}

	/**
	 * @return number of moleuclar spin orbitals
	 * @author yannick
	 */
	protected int getnSO() {
		if (this.SOM == null)
			return 0;
		return this.SOM.length;
	}

	/**
	 * @return number of moleuclar spin orbitals
	 * @author yannick
	 * @throws UnavailableSOMException 
	 * @throws NoSOMAvailableException 
	 */
	protected int getnOccSO() throws NoSOMAvailableException, UnavailableSOMException {
		return this.getOccSOM().length;		
	}

	/**
	 * @param iOM
	 * @return coefficients of the molecular orbital of index iOM
	 * @author yannick
	 */
	private double[] getOM(int iOM) {
		double[][] coeffOM = getOrbitalsCoefficients();
		int size = countHuckelAtoms();
		double[] OM = new double[size];
		// this strange order is due to the sorting done after the calculation
		// of the eigenvectors of the hamiltonian in calculate.
		for (int iOA = 0; iOA < size; iOA++) {
			OM[iOA] = coeffOM[iOA][iOM];
		}
		return OM;
	}

	public ArrayList<SpinOrbitaleMoleculaire> getSOMofSpin(Spin spin) {
		ArrayList<SpinOrbitaleMoleculaire> res=new ArrayList<SpinOrbitaleMoleculaire>();
		for (SpinOrbitaleMoleculaire som : this.getSOM()) {
			if (som.getSpin() == spin) res.add(som);
		}
		return res;
	}

	public SpinOrbitaleMoleculaire[] getOccSOM() throws NoSOMAvailableException, UnavailableSOMException {
		if (occSOMs==null) {
			ArrayList<SpinOrbitaleMoleculaire> list = new ArrayList<SpinOrbitaleMoleculaire>();
			for (int i=0; i<this.countSpinOM();i++) {
				SpinOrbitaleMoleculaire som = this.getSpinOM(i);
				switch (som.getSpin()) {
				case ALPHA:
				case BETA:
					list.add(som);
					break;
				case DEMI_ELECTRON : // AJOUT NG
					list.add(som);
					break;
				}
			}
			occSOMs = new SpinOrbitaleMoleculaire[list.size()];
			list.toArray(occSOMs);
		}
		return occSOMs;
	}

	public SpinOrbitaleMoleculaire[] getVirtSOM() throws NoSOMAvailableException, UnavailableSOMException {
		ArrayList<SpinOrbitaleMoleculaire> list = new ArrayList<SpinOrbitaleMoleculaire>();
		for (int i=0; i<this.countSpinOM();i++) {
			SpinOrbitaleMoleculaire som = this.getSpinOM(i);
			switch (som.getSpin()) {
			case NOSPIN_ALPHA:
			case NOSPIN_BETA:
				list.add(som);
				break;
			case NO_DEMI_ELECTRON : // AJOUT NG		
				list.add(som);
				break;
			}
		}
		SpinOrbitaleMoleculaire[] a =new SpinOrbitaleMoleculaire[list.size()];
		list.toArray(a);
		return a;
	}

	/**
	 * @return
	 */
	public final SpinOrbitaleMoleculaire[] getSOM() {
		return this.SOM;
	}
	/**
	 * This function returns a matrix containing the coefficients of the
	 * orbitals column sorded by energy, and rows sorted by orbital
	 * 
	 * @return double [][]
	 */

	public double[][] getOrbitalsCoeffArray() {
		return this.orbitalsCoeffArray;
	}

	/**
	 * This function returns the coefficients of the orbitals of the molecule
	 * 
	 * @return double [][]
	 * @deprecated use getOrbitalsCoeffArray instead
	 */
	protected double[][] getOrbitalsCoefficients() {
		return this.getOrbitalsCoeffArray();
	}

	/**
	 * This function returns the energies of the orbitals of the molecule
	 * 
	 * @return double []
	 */
	protected double[] getOrbitalsEnergies() {
		return this.energiesArray;
	}

	/**
	 * @param iSOM
	 * @return the iSOM'th spin molecular orbital
	 * @throws NoSOMAvailableException
	 * @throws UnavailableSOMException
	 */
	public SpinOrbitaleMoleculaire getSpinOM(int iSOM)
			throws NoSOMAvailableException, UnavailableSOMException {
		if (getSOM() == null) 
			throw new NoSOMAvailableException();

		int size=getSOM().length;

		if (iSOM >= size) 
			throw new UnavailableSOMException(iSOM, size);

		return getSOM()[iSOM];
	}


	public SpinOrbitaleMoleculaire getSpinOMByIndex(int index) {
		for (int i = 0 ; i < this.getSOM().length; i++ ) {
			SpinOrbitaleMoleculaire som =  getSOM()[i];
			if (som.getIndex()==index)
				return som;
		}
		return null;
	}


	public SpinOrbitaleMoleculaire getSpinOM(SpinOrbitaleMoleculaire phi1)
			throws NoSOMAvailableException, UnavailableSOMException {
		int size=getSOM().length;
		if (getSOM() == null) {
			throw new NoSOMAvailableException();
		}
		for (int iSOM=0; iSOM<size; iSOM++)  {
			SpinOrbitaleMoleculaire phi2=getSOM()[iSOM];
			if (phi1.overlap(phi2, this.getTopology()) > 0.999) return phi2; 
		}
		return null;
	}

	/**
	 * This function returns the sum of the charges densties
	 * 
	 * @see getChargeDensitiesArray ()
	 * @return double
	 */

	public double getSumOFChargeDensities() {
		return this.sumOfChargeDensities;
	}

	/**
	 * This function returns the sum of the Pi electrons distributed in the
	 * molecule
	 * 
	 * @see getDistributionElecPiArray ()
	 * @return sum of electrons on orbitals
	 */

	public int getSumOfDistributedElec() {
		return this.sumOfDistributedElec;
	}


	/**
	 *  @return for a delocalized structure : sum = Sum of default atom pi electrons + electronic charge of molecule
	 * for a localized structure : sum of electrons localized on atoms and electrons on bonds
	 */
	public int getSumOfPiElec() {
		return this.sumOfPiElec;
	}

	/**
	 * This function returns the total energy of the molecule of the molecule
	 * 
	 * @return an Energy object
	 */

	public Energy getTotalEnergy() {
		return this.totalEnergy;
	}

	/**
	 * This function returns the Alpha value of the total energy of a molecule
	 * 
	 * @return double
	 */

	protected double getTotalEnergyAlpha() {
		return getSumOfDistributedElec();

	}

	/**
	 * This function returns the Beta value of the total energy of a molecule
	 * 
	 * @return double
	 */

	protected double getTotalEnergyBeta() {

		double energieTotale = 0;
		double[] repartitionElec = distributionElecPiArray;

		for (int i = 0; i < repartitionElec.length; i++) {
			energieTotale += this.energiesArray[i] * repartitionElec[i];
		}

		return energieTotale;
	}

	/**
	 * @return total spin of the structure (sum of individual spins)
	 * @author yannick
	 */
	public int getTotalSpin() {
		if (getSOM() == null)
			try {
				generateSpinOM();
			} catch (NoElectronException e) {
				HuckelIO.warning(this.getClass().getName(), "getTotalSpin", e
						.getMessage(), e);
			} catch (NoSOMAvailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnavailableSOMException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		int TotSpin = 0;
		try {
			for (SpinOrbitaleMoleculaire SOM : getOccSOM()) {
				TotSpin += SOM.getSpin().getval() + 0;
			}
		} catch (NoSOMAvailableException e) {
			e.printStackTrace();
			HuckelIO.error(this.getClass().toString(), "getTotalSpin", "SOM not generated ? ");
		} catch (UnavailableSOMException e) {
			e.printStackTrace();
			HuckelIO.error(this.getClass().toString(), "getTotalSpin", "SOM not generated ? ");
		}
		return TotSpin;
	}


	/**
	 * 
	 * @param monoExcitation
	 */
	public void removeMonoExcitation (MonoExcitation monoExcitation){
		this.monoExcitations.remove(monoExcitation);
		if (this.isEnabledFlyCalculate())
			this.calculate();
		this.fireMonoExcitationTriggered(new MonoExcitationEvent(monoExcitation, null, null ));
	}



	/**
	 * prints the spin orbitals
	 * 
	 * @author yannick
	 */
	public void printSOMs() {
		if (!HuckelIO.isverbose())
			return;
		int nOA = getnOA();
		int nSO = getnSO();
		System.out.printf("%s spinorbitales nOA=%s\n", nSO, nOA);
		for (int iSO = 0; iSO < nSO; iSO++) {
			System.out.printf("%4s_%-3d", "OM", iSO + 1);
		}
		HuckelIO.PrintIf("\n");
		for (int iSO = 0; iSO < nSO; iSO++) {
			Spin spin=Spin.NOSPIN_ALPHA;
			try {
				spin = getSpinOM(iSO).getSpin();
			}catch(NullPointerException e) {
				HuckelIO.PrintIf("getSpinOM(iSO) is null\n" );
			} catch (NoSOMAvailableException e) {
				// TODO Auto-generated catch block
				HuckelIO.warning(this.getClass().getName(), "printSOMs", e
						.getMessage(), e);
			} catch (UnavailableSOMException e) {
				// TODO Auto-generated catch block
				HuckelIO.warning(this.getClass().getName(), "printSOMs", e
						.getMessage(), e);
			}
			System.out.printf("%7s ", spin.toString());
		}
		HuckelIO.PrintIf("\n");
		for (int iOA = 0; iOA < nOA; iOA++) {
			for (int iSO = 0; iSO < nSO; iSO++) {
				double cOM;
				try {
					cOM = getSpinOM(iSO).getCoeff(iOA);
					System.out.printf("%8.4f", cOM);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					HuckelIO.warning(this.getClass().getName(), "printSOMs", e
							.getMessage(), e);

				}
			}
			HuckelIO.PrintIf("\n");
		}
	}

	@Override
	public void do_removeAtom(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		// supprime les excitations sur la structure
		resetMonoExcitations();
		resetSOM();
		super.do_removeAtom(a);
	}

	@Override

	public void setNbElecCharge(int nbElecCharge) throws MoleculeCoherenceException  {
		this.resetMonoExcitations();
		resetSOM();
		super.setNbElecCharge(nbElecCharge);


	}


	/**
	 * empty the list of monoexcitations
	 * 
	 * @author yannick
	 */
	public void resetMonoExcitations() {
		this.monoExcitations.clear();
	}

	// yannick

	/**
	 * cleans the spin molecular orbitals
	 * 
	 * @author yannick
	 */
	public void resetSOM() {
		this.SOM = null;
		this.occSOMs=null;
		try {
			generateSpinOM();
		} catch (NoElectronException e) {
			HuckelIO.warning(this.getClass().getName(), "resetSOM", e
					.getMessage(), e);
		} catch (NoSOMAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnavailableSOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param mesomeryParent
	 *            mesomeryParent a definir
	 */
	public void setMesomeryParent(Mesomery mesomeryParent) {
		this.mesomeryParent = mesomeryParent;
	}

	public void setName(String name) {

		String old = this.name;
		this.name = name;
		if (old != this.name)
			fireStructureNameChanged(this, old, this.name);

	}
	
	public boolean isEnabledFlyCalculate() {
		return this.isEnabledFlyCalculate;
	}
	
	public void setEnabledFlyCalculate(boolean isActiveFlyCalculate) {
		this.isEnabledFlyCalculate = isActiveFlyCalculate;
	}

	public void startFlyCalculate() {
		this.isEnabledFlyCalculate = true;
	}

	public void stopFlyCalculate() {
		this.isEnabledFlyCalculate = false;
	}
	/**
	 * exchanges the spins of spin moleuclar orbitals iSOM and jSOM
	 * 
	 * @param iSOM
	 * @param jSOM
	 * @author yannick
	 */
	protected void switchSpinOM(int iSOM, int jSOM) {
		try {
			getSpinOM(iSOM).switchSpin();
			getSpinOM(jSOM).switchSpin();
		} catch (Exception e) {
			HuckelIO.warning(this.getClass().getName(), "switchSpinOM", e
					.getMessage(), e);
		}
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#toString()
	 */
	@Override
	public String toString() {
		String res = this.name + " " + super.toString();
		return res;

	}

	/**
	 * 
	 * @param i
	 * @return
	 * @throws NoSOMAvailableException
	 * @throws UnavailableSOMException
	 */
	public SpinOrbitaleMoleculaire getOccSpinOM(int i) throws NoSOMAvailableException, UnavailableSOMException {
		return this.getOccSOM()[i];
	}

	/**
	 * 
	 * @param spin
	 * @return
	 * @throws NoSOMAvailableException
	 * @throws UnavailableSOMException
	 */
	public SpinOrbitaleMoleculaire getHOMO(Spin spin) throws NoSOMAvailableException, UnavailableSOMException {
		SpinOrbitaleMoleculaire[] list = this.getOccSOM();
		Energy eMax=Energy.MIN(list[0]);
		SpinOrbitaleMoleculaire som = null;
		for (int i=0; i<list.length; i++) {
			SpinOrbitaleMoleculaire tmp = list[i];
			if (tmp.getSpin()==spin) {
				Energy e = tmp.getEnergy();
				if (e.compareTo(eMax)==-1) {
					eMax=e;
					som = list[i];
				}
			}
		}
		return som;
	}

	/**
	 * 
	 * @param spin
	 * @return
	 * @throws NoSOMAvailableException
	 * @throws HulisException
	 */
	public SpinOrbitaleMoleculaire getLUMO(Spin spin) throws NoSOMAvailableException, HulisException {
		SpinOrbitaleMoleculaire[] list = this.getVirtSOM();
		Energy eMin=Energy.MAX(list[0]);
		SpinOrbitaleMoleculaire som = null;
		for (int i=0; i<list.length; i++) {
			SpinOrbitaleMoleculaire tmp = list[i];
			if (tmp.getSpin()==spin) {
				Energy e = tmp.getEnergy();
				if (e.compareTo(eMin)== 1) {
					eMin=e;
					som = list[i];
				}
			}
		}
		return som;
	}

	public CharacteristicPolynomial getCharacteristicPolynomial() {
		CharacteristicPolynomial poly=null;
		try {
			poly=new CharacteristicPolynomial(new Matrix(this.getHamiltonianArray()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
		return poly;
	}

	/**
	 * This function computes the overlap between two structures
	 * 
	 * @param Phi1
	 * @param Phi2
	 * @return overlap between structure Phi1 and Phi2
	 * @author yannick
	 * @throws UnavailableSOMException
	 * @throws NoSOMAvailableException
	 * @throws NoElectronException
	 * @throws OrbitaleMoleculaireException 
	 * @throws HLPOverlapException 
	 */
	public static double overlap(Structure struct1, Structure struct2)
			throws NoElectronException, NoSOMAvailableException,
			UnavailableSOMException, OrbitaleMoleculaireException, HLPOverlapException {

		Structure Phi1 = struct1;
		Structure Phi2 = null;
		if (struct1 == struct2) {
			Phi2 = (Structure) struct2.clone(true);

		} else {
			Phi2 = struct2;
		}
		HuckelIO.PrintIfln(" <  " + Phi1.getName() + " | " + Phi2.getName()
				+ " > \n");
		Phi1.printSOMs();
		Phi2.printSOMs();
		
		double ovl;

		int size = Phi1.getnOccSO();
		if (Phi2.getnOccSO()!=size) {
			return 0.0;
		}


		/*
		 * ici il faut tester le spin de la structure. Il y a deux cas : _sans
		 * couplage : appel normal _avec couplage : deux appels, puis
		 * combinaison
		 */

		boolean phi1AUnCouplage = Phi1.aUnCouplage();
		boolean phi2AUnCouplage = Phi2.aUnCouplage();

		/*	if (HuckelIO.isverbose()) {
				System.out.printf("Phi1 a un couplage=%s\n", phi1AUnCouplage);
				System.out.printf("Phi2 a un couplage=%s\n", phi2AUnCouplage);
			}*/
		int iSOM;
		int jSOM;

		// final double norm = 1 / Math.sqrt(2.0);
		//  19/05/2010		double norm = 1;
		double norm = 1 / Math.sqrt(2.0);
		if (phi1AUnCouplage && phi2AUnCouplage) { /* coupling in 1 and 2 */
			/*
			 * in this part we compute the overlap between 2 structures which
			 * have a coupling. Phi1 couples e in OM a and b (upper case is for
			 * BETA spin) Phi2 couples e in OM c and d
			 */
			Phi1.resetSOM(); // create the som of phi1 with all e UP
			// state a b | c d
			Phi1.appliqueCouplage();// get the coupling and flip the first
			// electron coupled
			// state A b | c d
			Phi2.resetSOM(); // create the som of phi1 with all e UP
			Phi2.appliqueCouplage();// get the coupling and flip the first
			// electron coupled
			// state A b | C d
			//	System.out.printf("%s\n", "Initial OMs");
			Phi1.printSOMs();
			Phi2.printSOMs();
			Matrix ovlMat_11_21 = new Matrix(size, size);
			// first component of phi1 and phi2
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					SpinOrbitaleMoleculaire SOM1 = Phi1.getOccSpinOM(i);
					SpinOrbitaleMoleculaire SOM2 = Phi2.getOccSpinOM(j);
					ovlMat_11_21.set(i, j, SOM1.overlap(SOM2, Phi1
							.getTopology()));
				}
			}

			HuckelIO.PrintIfln("Matrice 11 21");
			HuckelIO.PrintMatrix(ovlMat_11_21);
			double det_11_21 = ovlMat_11_21.det();
			ovlMat_11_21 = null;

			// first component of phi1 second component of phi2
			Matrix ovlMat_11_22 = new Matrix(size, size);
			iSOM = Phi2.getCouplage()[0];
			jSOM = Phi2.getCouplage()[1];
			Phi2.switchSpinOM(iSOM, jSOM);
			Phi1.printSOMs();
			Phi2.printSOMs();

			// switch spins of c and d
			// state A b | c D
			if (HuckelIO.isverbose()) {
				//System.out.printf("%s\n", "Phi1 init Phi2 modified");
				Phi1.printSOMs();
				Phi2.printSOMs();
			}
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					SpinOrbitaleMoleculaire SOM1 = Phi1.getOccSpinOM(i);
					SpinOrbitaleMoleculaire SOM2 = Phi2.getOccSpinOM(j);
					ovlMat_11_22.set(i, j, SOM1.overlap(SOM2, Phi1
							.getTopology()));
				}
			}

			if (HuckelIO.isverbose()) {
				HuckelIO.PrintIfln("Matrice 11 22");
				HuckelIO.PrintMatrix(ovlMat_11_22);
			}
			double det_11_22 = ovlMat_11_22.det();
			ovlMat_11_22 = null;

			// second component of phi1 first component of phi2
			Matrix ovlMat_12_21 = new Matrix(size, size);
			iSOM = Phi2.getCouplage()[0];
			jSOM = Phi2.getCouplage()[1];
			Phi2.switchSpinOM(iSOM, jSOM);
			iSOM = Phi1.getCouplage()[0];
			jSOM = Phi1.getCouplage()[1];
			Phi1.switchSpinOM(iSOM, jSOM);
			// switch spins of a and b and c and d
			// state a B | C d
			/*	if (HuckelIO.isverbose()) {
					System.out.printf("%s\n", "Phi1 modified Phi2 init");
					Phi1.printSOMs();
					Phi2.printSOMs();
				}*/
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					SpinOrbitaleMoleculaire SOM1 = Phi1.getOccSpinOM(i);
					SpinOrbitaleMoleculaire SOM2 = Phi2.getOccSpinOM(j);
					ovlMat_12_21.set(i, j, SOM1.overlap(SOM2, Phi1
							.getTopology()));
				}
			}

			if (HuckelIO.isverbose()) {
				HuckelIO.PrintIfln("Matrice 12 21");
				HuckelIO.PrintMatrix(ovlMat_12_21);
			}
			double det_12_21 = ovlMat_12_21.det();
			ovlMat_12_21 = null;

			// second component of phi1 second component of phi2
			Matrix ovlMat_12_22 = new Matrix(size, size);
			iSOM = Phi2.getCouplage()[0];
			jSOM = Phi2.getCouplage()[1];
			Phi2.switchSpinOM(iSOM, jSOM);
			/*if (HuckelIO.isverbose()) {
					// switch spins of c and d
					// state a B | C d
					System.out.printf("%s\n", "Phi1 modified Phi2 modified");
					Phi1.printSOMs();
					Phi2.printSOMs();
				}*/
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					SpinOrbitaleMoleculaire SOM1 = Phi1.getOccSpinOM(i);
					SpinOrbitaleMoleculaire SOM2 = Phi2.getOccSpinOM(j);
					ovlMat_12_22.set(i, j, SOM1.overlap(SOM2, Phi1
							.getTopology()));
				}
			}

			if (HuckelIO.isverbose()) {
				HuckelIO.PrintIfln("Matrice 12 22");
				HuckelIO.PrintMatrix(ovlMat_12_22);
			}
			double det_12_22 = ovlMat_12_22.det();
			ovlMat_12_22 = null;

			// Phi1 : a b => aB-Ab
			// Phi2 : c d => cD-Cd
			// (aB-Ab)(cD-Cd) = aBcD + AbCd - AbcD - aBCd
			// equiv 12_22 + 11_21 - 11_22 - 12_21
			ovl = det_11_21 - det_11_22 - det_12_21 + det_12_22;
			ovl *= Math.pow(norm, 2);

		} else if (phi1AUnCouplage) { /* coupling in 1 only */
			Phi1.resetSOM();
			Phi1.appliqueCouplage();
			/*	if (HuckelIO.isverbose()) {
					System.out.printf("%s\n", "Phi1 init");
					Phi1.printSOMs();
					Phi2.printSOMs();
				}*/
			// first component of phi1
			Matrix ovlMat_11 = new Matrix(size, size);
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					SpinOrbitaleMoleculaire SOM1 = Phi1.getOccSpinOM(i);
					SpinOrbitaleMoleculaire SOM2 = Phi2.getOccSpinOM(j);
					ovlMat_11.set(i, j, SOM1.overlap(SOM2, Phi1.getTopology()));
				}
			}

			if (HuckelIO.isverbose()) {
				HuckelIO.PrintIfln("Matrice 11");
				HuckelIO.PrintMatrix(ovlMat_11);
			}
			double det_11 = ovlMat_11.det();
			ovlMat_11 = null;

			// second component of phi1
			Matrix ovlMat_12 = new Matrix(size, size);
			iSOM = Phi1.getCouplage()[0];
			jSOM = Phi1.getCouplage()[1];
			Phi1.switchSpinOM(iSOM, jSOM);
			/*if (HuckelIO.isverbose()) {
					System.out.printf("%s\n", "Phi1 modified");
					Phi1.printSOMs();
					Phi2.printSOMs();
				}*/
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					SpinOrbitaleMoleculaire SOM1 = Phi1.getOccSpinOM(i);
					SpinOrbitaleMoleculaire SOM2 = Phi2.getOccSpinOM(j);
					ovlMat_12.set(i, j, SOM1.overlap(SOM2, Phi1.getTopology()));
				}
			}

			if (HuckelIO.isverbose()) {
				HuckelIO.PrintIfln("Matrice 12");
				HuckelIO.PrintMatrix(ovlMat_12);
			}
			double det_12 = ovlMat_12.det();
			ovlMat_12 = null;

			// Phi1 : a b
			// Ab - aB
			// equiv 11 - 12
			ovl = det_11 - det_12;
			ovl *= norm;

		} else if (phi2AUnCouplage) { /* coupling in 2 only */
			Phi2.resetSOM();
			Phi2.appliqueCouplage();
			/*if (HuckelIO.isverbose()) {
					System.out.printf("%s\n", "Phi2 init");
					Phi1.printSOMs();
					Phi2.printSOMs();
				}*/
			// first component of phi2
			Matrix ovlMat_21 = new Matrix(size, size);
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					SpinOrbitaleMoleculaire SOM1 = Phi1.getOccSpinOM(i);
					SpinOrbitaleMoleculaire SOM2 = Phi2.getOccSpinOM(j);
					ovlMat_21.set(i, j, SOM1.overlap(SOM2, Phi1.getTopology()));
				}
			}

			if (HuckelIO.isverbose()) {
				HuckelIO.PrintIfln("Matrice 21");
				HuckelIO.PrintMatrix(ovlMat_21);
			}
			double det_21 = ovlMat_21.det();
			ovlMat_21 = null;

			// second component of phi2
			Matrix ovlMat_22 = new Matrix(size, size);
			iSOM = Phi2.getCouplage()[0];
			jSOM = Phi2.getCouplage()[1];
			Phi2.switchSpinOM(iSOM, jSOM);
			if (HuckelIO.isverbose()) {
				//		System.out.printf("%s\n", "Phi2 modified");
				Phi1.printSOMs();
				Phi2.printSOMs();
			}
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					SpinOrbitaleMoleculaire SOM1 = Phi1.getOccSpinOM(i);
					SpinOrbitaleMoleculaire SOM2 = Phi2.getOccSpinOM(j);
					ovlMat_22.set(i, j, SOM1.overlap(SOM2, Phi1.getTopology()));
				}
			}
			HuckelIO.PrintIfln("Matrice 22");
			HuckelIO.PrintMatrix(ovlMat_22);
			double det_22 = ovlMat_22.det();
			ovlMat_22 = null;

			// Phi2 : c d
			// Cd - cD
			// equiv 21 - 22
			ovl = det_21 - det_22;
			ovl *= norm;

		} else { /* no coupling */
			//			size=Phi1.getSOMofSpin(Spin.ALPHA).size();
			Matrix ovlMat = new Matrix(size, size);
			/*if (HuckelIO.isverbose()) {
					System.out.printf("%s\n", "Orbitals");
					Phi1.printSOMs();
					Phi2.printSOMs();
				}*/
			//TEST
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					//					SpinOrbitaleMoleculaire SOM1 = Phi1.getSOMofSpin(Spin.ALPHA).get(i);
					//					SpinOrbitaleMoleculaire SOM2 = Phi2.getSOMofSpin(Spin.ALPHA).get(j);
					SpinOrbitaleMoleculaire SOM1 = Phi1.getOccSpinOM(i);
					SpinOrbitaleMoleculaire SOM2 = Phi2.getOccSpinOM(j);
					ovlMat.set(i, j, SOM1.overlap(SOM2, Phi1.getTopology()));
				}
			}

			HuckelIO.PrintIfln("Matrice 00");
			HuckelIO.PrintMatrix(ovlMat);
			ovl = ovlMat.det();
			//TEST
		}
		/*if (HuckelIO.isverbose()) {
				HuckelIO.PrintIf(" < " + Phi1.getName() + " | " + Phi2.getName()
						+ " >");			
				System.out.printf("Overlap = %10.5f\n", ovl);
			}*/
		return ovl;
	}



	/**
	 * @param b
	 * @return
	 */
	public abstract Object clone(boolean cloneResults) ;


	/**
	 * @return true if clone the results are cloned when the structure is cloned
	 */
	public boolean isCloneResultsCacheWhenCloneStructure() {
		return isCloneResultsCacheWhenCloneStructure;
	}

	/**
	 * if the parameter is set tot true, the results in cache are cloned when the structure is cloned
	 * @param isCloneResultsCacheWhenCloneStructure the parameter to set 
	 */
	/**
	 * @param isCloneResultsCacheWhenCloneStructure
	 */
	public void setCloneResultsCacheWhenCloneStructure(
			boolean isCloneResultsCacheWhenCloneStructure) {
		this.isCloneResultsCacheWhenCloneStructure = isCloneResultsCacheWhenCloneStructure;

	}

	/**
	 * 
	 */
	private void computeNaturalOrbitals() {
		Matrix D = getRestrictedDensityMatrix();
		EigenvalueDecomposition evd = new EigenvalueDecomposition(D);
		double[][] natOrb = evd.getV().getArrayCopy();
		double[] natOcc = evd.getRealEigenvalues();
		int nNO=natOcc.length;
		naturalOrbitals = new ArrayList<NatOrbitaleMoleculaire>();
		for (int i=0; i<nNO; i++) {
			double[] OM = new double[nNO];
			for (int iOA = 0; iOA < nNO; iOA++) {
				OM[iOA] = natOrb[iOA][i];
			}
			NatOrbitaleMoleculaire no = new NatOrbitaleMoleculaire(OM);
			no.setOcc(natOcc[i]);
			naturalOrbitals.add(no);
		}
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<NatOrbitaleMoleculaire> getNaturalOrbitals(){
		return naturalOrbitals;
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public NatOrbitaleMoleculaire getNatOM(int i) {
		return naturalOrbitals.get(i);
	}

	/**
	 * 
	 * @return
	 */
	public Symmetry getSymmetry() {
		if (symmetry==null) return new Symmetry(this);
		return symmetry;
	}


	/**
	 * 
	 * @param listener
	 */
	public void addListener(IStructureListener listener) {
		super.addListener(listener);
	}


	/**
	 * 
	 * @param listener
	 */
	public void removeListener(IStructureListener listener) {
		super.removeListener(listener);
	}


	/**
	 * stores a monoexcitation whenever triggered in the current structure
	 * 
	 * @param e :
	 *            Huckel event sent
	 * @author yannick
	 */
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		MonoExcitation ex = e.getSource();
		if (this != ex.getStructure())
			return;
		boolean found = false;
		for (MonoExcitation m : this.getMonoExcitations() ) 
			if (m == ex)
				found = true;

		MonoExcitation excopy = (MonoExcitation) ex.clone();

		if (!found) {
			this.monoExcitations.add(excopy);
			this.occSOMs=null;
		}

		fireMonoExcitationTriggered(new MonoExcitationEvent(excopy, null, null));
	}

	
	protected void fireMonoExcitationTriggered(MonoExcitationEvent e){

		if (this.isEnabledFlyCalculate())
			calculate();

		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		for (IMoleculeValueListener listener : this.getValueListeners()) {

			((IStructureListener)listener).monoExcitationTriggered(e);
		}	
	}

	/**
	 */
	protected void fireStructureNameChanged(Structure source, Object oldValue, Object newValue) {
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		StructureEvent event = null;

		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new StructureEvent(source, oldValue, newValue);
			if (listener instanceof IStructureListener) {
				((IStructureListener)listener).structureNameChanged(event);
			}
		}	
	}
	
	

	@Override
	protected void fireHuckelAtomHxChanged(HuckelAtomEvent e){
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireHuckelAtomHxChanged(e);
	}

	@Override
	protected void fireHuckelAtomRadRChanged(HuckelAtomEvent e){
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireHuckelAtomRadRChanged(e);
	}

	@Override
	protected void fireHuckelAtomSeqNumChanged(HuckelAtomEvent e){
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireHuckelAtomSeqNumChanged(e);
	}

	@Override
	protected void fireHuckelBondBondTypeChanged(HuckelBondEvent e) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireHuckelBondBondTypeChanged(e);
	}

	@Override
	protected void fireHuckelBondHxyChanged(HuckelBondEvent e) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireHuckelBondHxyChanged(e);
	}

	@Override
	protected void fireMoleculeAtomAdded(Molecule source, Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireMoleculeAtomAdded(source, oldValue, newValue);
	}
	@Override
	protected void fireMoleculeAtomRemoved(Molecule source, Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireMoleculeAtomRemoved(source, oldValue, newValue);
	}

	@Override
	protected void fireMoleculeAtomReplaced(Molecule source, Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireMoleculeAtomReplaced(source, oldValue, newValue);
	}

	@Override
	protected void fireMoleculeBondAdded(Molecule source, Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireMoleculeBondAdded(source, oldValue, newValue);
	}

	@Override
	protected void fireMoleculeBondRemoved(Molecule source, Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireMoleculeBondRemoved(source, oldValue, newValue);
	}


	@Override
	protected void fireMoleculeChargeChanged(Molecule source, Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireMoleculeChargeChanged(source, oldValue, newValue);
	}


	@Override
	protected void fireMoleculeListHuckelAtomSeqNumAutoSetted(Molecule source, Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireMoleculeListHuckelAtomSeqNumAutoSetted(source, oldValue, newValue);
	}

	@Override
	protected void fireMoleculeListHuckelAtomSeqNumReset(Molecule source, Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		super.fireMoleculeListHuckelAtomSeqNumReset(source, oldValue, newValue);
	}

	static public void main(String args[]) {
		double [][] hamiltonien={
				{0,0,0,1,0,0,0,0,1,0,0,0},
				{0,0,1,0,0,0,0,1,0,0,0,0},
				{0,1,0,1,0,0,0,0,0,0,0,0},
				{1,0,1,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0.5,0,0,0,0,0},
				{0,0,0,0,0,0.5,0,0,0,0,0,0},
				{0,1,0,0,0,0,0,0,1,0,0,0},
				{1,0,0,0,0,0,0,1,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0,0,1},
				{0,0,0,0,0,0,0,0,0,0,1,0}};
		EigenvalueDecomposition eig = new EigenvalueDecomposition(new Matrix (hamiltonien));
		HuckelIO.PrintMatrix(new Matrix(hamiltonien), "hamiltonien");
		HuckelIO.PrintMatrix(new Matrix(eig.getV().getArray()), "orbitales (eigenvalues)");
		HuckelIO.PrintMatrix(new Matrix(VectorssHuckel.sort(eig.getV().getArray())), "orbitales reordonnees");
		}


	


}
