package org.ctom.hulis.huckel.structures;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.CoupleLocal;
import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.IHuckelObject;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.MonoExcitation;
import org.ctom.hulis.huckel.OrbitaleMoleculaire;
import org.ctom.hulis.huckel.Spin;
import org.ctom.hulis.huckel.SpinOrbitaleMoleculaire;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.MoleculeAtomNullException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.exception.NoElectronException;
import org.ctom.hulis.huckel.exception.NoSOMAvailableException;
import org.ctom.hulis.huckel.exception.OrbitaleMoleculaireException;
import org.ctom.hulis.huckel.exception.StructureLocalizedException;
import org.ctom.hulis.huckel.exception.UnavailableSOMException;
import org.ctom.hulis.huckel.listeners.IMoleculeValueListener;
import org.ctom.hulis.huckel.listeners.IStructureLocalizedListener;
import org.ctom.hulis.huckel.structures.StructureLocalized.CoupleOMs;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Maths;
import org.ctom.util.maths.Rotation;

import Jama.Matrix;
//import org.json.simple.JSONObject;

/**
 * @author macbook
 *
 */
public class StructureLocalized extends Structure implements Serializable 
 {


	/**
	 * 
	 */
	private static final long serialVersionUID = 3507848539073531741L;

	public static enum CreationMethod {AUTO_GENERATED, CREATED_MANUALLY};
	public static enum Error {ERROR_INCORRECT_NB_PI_ELEC,ERROR_ALREADY_EXIST,ERROR_TOO_MUCH_SINGLE_ELECTRONS,ERROR_CORRECT_SPIN_IMPOSSIBLE,NOT_ERROR};
	

	
	private CoupleLocal couple;

	private Error error;
	protected StructureLocalized identicalStruct;
	private CreationMethod methodCreation;
	private HashMap<String, Double> weights;
	
	private ArrayList<CoupleOMs> lstCouplesOMs;

	
	
	class CoupleOMs{
		int om1;
		int om2;
	}


	/**
	 * 
	 */
	public StructureLocalized() {
		super();
		this.lstCouplesOMs = new ArrayList<CoupleOMs>();
		this.weights = new HashMap<String, Double>();
			
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#addAtom(org.ctom.hulis.huckel.Atom)
	 */
	@Override
	public void addAtom(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		throw new MoleculeCoherenceException ("Cannot add an atom in a localized structure. It must have the same configuration as the delocalized structure");
	}
	
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#addBond(org.ctom.hulis.huckel.Bond)
	 */
	@Override
	public void addBond(Bond b) throws  MoleculeCoherenceException, HuckelBondException, BondException, MoleculeBondExistsException, MoleculeTooManyNeighboursException {
		throw new MoleculeCoherenceException ("Cannot add an bond in a localized structure. It must have the same configuration as the delocalized structure");
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#removeAtom(org.ctom.hulis.huckel.Atom)
	 */
	@Override
	public void removeAtom(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		throw new MoleculeCoherenceException ("Cannot remove an atom in a localized structure. It must have the same configuration as the delocalized structure");
		
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#removeBond(org.ctom.hulis.huckel.Bond)
	 */
	@Override
	public void removeBond(Bond l) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		throw new MoleculeCoherenceException ("Cannot remove a bond in a localized structure. It must have the same configuration as the delocalized structure");
		
	}
		
		
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#replaceAtom(org.ctom.hulis.huckel.Atom, org.ctom.hulis.huckel.Atom)
	 */
	@Override
	public void replaceAtom(Atom old, Atom newAtom) throws HuckelBondException, BondException, MoleculeAtomNullException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException{
		throw new MoleculeCoherenceException ("Cannot replace an atom in a localized structure. It must have the same configuration as the delocalized structure");

	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#removeMonoExcitation(org.ctom.hulis.huckel.MonoExcitation)
	 */
	@Override
	public void removeMonoExcitation (MonoExcitation monoExcitation){
		throw new RuntimeException("Cannot remove excitation in a localized structure. It must have the same configuration as the delocalized structure");
	}
	
	
	@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		throw new RuntimeException("Cannot trigger a monoexcitation in a localized structure. It must have the same configuration as the delocalized structure");
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#rotate(org.ctom.util.maths.Rotation)
	 */
	@Override
	public void rotate(Rotation mRot) {
		throw new RuntimeException("Cannot rotate a localized structure. It must have the same configuration as the delocalized structure");

	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#move(double, double, double)
	 */
	@Override
	public void move(double dx, double dy, double dz) {
		// TODO : decommenter et reprogrammer Controller.Center par rapport a une marge dans la vue et ne pas appeler move
		throw new RuntimeException("Cannot move a localized structure. It must have the same configuration as the delocalized structure");
		
	}
	
	
	/**
	 * Cannot reset sequence number on a localized structure because the sekeleton must be the same as a delocalized structure
	 */
	@Override 
	public void resetSeqNumListHuckelAtomToZero() {
		try {
			throw new MoleculeCoherenceException("Cannot reset sequence number on a localized structure because the sekeleton must be the same as a delocalized structure");
		}
		catch (MoleculeCoherenceException e ) {
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#setAutoSeqNumListHuckelAtom()
	 */
	@Override
	public void setAutoSeqNumListHuckelAtom() {
		try {
			throw new MoleculeCoherenceException("Cannot reset sequence number on a localized structure because the sekeleton must be the same as a delocalized structure");
		}
		catch (MoleculeCoherenceException e ) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#countSingleElectronsOnAtoms()
	 */
	@Override
	public int countSingleElectronsOnAtoms() {
		int nb = super.countSingleElectronsOnAtoms();
	
		// si il y a un couplage alors on enleve 2 electrons celibataires
		if ( ((StructureLocalized)this).aUnCouplage() ) {
			nb-=2;
		}
	
		//fin cas du couplage
		return nb;
	}

	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#setNbElecCharge(int)
	 */
	@Override
	public void setNbElecCharge(int nbElecCharge) throws MoleculeCoherenceException  {		
		throw new MoleculeCoherenceException ("Cannot change eletrons of a localized structure. It must have the same configuration as the delocalized structure");
	}
	
	/**
	 * @param structure
	 */
	public StructureLocalized(StructureDelocalized structure) {
		super(structure);
		this.weights = new HashMap<String, Double>();
		this.methodCreation = StructureLocalized.CreationMethod.CREATED_MANUALLY;
		this.error = StructureLocalized.Error.NOT_ERROR;
		this.couple = null;
		this.lstCouplesOMs = new ArrayList<CoupleOMs>();
		updateCharges();
		if (this.isEnabledFlyCalculate())
			calculate();
	}

	
	/**
	 * @param structure
	 * @throws CoupleException
	 */
	public StructureLocalized(StructureLocalized structure) throws CoupleException {
		this(structure, structure.isCloneResultsCacheWhenCloneStructure()) ;
	}
	
	/**
	 * @param structure
	 * @param cloneStructureResults
	 * @throws CoupleException
	 */
	public StructureLocalized(StructureLocalized structure, boolean cloneStructureResults) throws CoupleException {
		super(structure);
		this.weights = new HashMap<String, Double>();
		for (Map.Entry<String, Double> entry : structure.weights.entrySet())
			this.weights.put(entry.getKey(), entry.getValue());
		
		if (cloneStructureResults)
			this.copyArrays(structure);
		this.methodCreation = StructureLocalized.CreationMethod.CREATED_MANUALLY;
		this.error = structure.error;
		if (structure.couple != null) {
			this.couple = new CoupleLocal(
						(HuckelAtom) getAtomByIndex(structure.couple
								.getHuckelAtom1().getIndex()),
								(HuckelAtom) getAtomByIndex(structure.couple
										.getHuckelAtom2().getIndex()));
			
		}
		this.lstCouplesOMs = new ArrayList<CoupleOMs>();

		// updateCharges();
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#appliqueCouplage()
	 */
	@Override
	protected void appliqueCouplage() throws NoSOMAvailableException,
	UnavailableSOMException, OrbitaleMoleculaireException {
		int i = getCouplage()[0];
		this.getSpinOM(i).switchSpin();
	}

	/**
	 * @param a1
	 * @param a2
	 * @return
	 */
	public boolean areCoupled(HuckelAtom a1, HuckelAtom a2) {
		if (this.couple == null)
			return false;
		return this.couple.contains(a1, a2);

	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#aUnCouplage()
	 */
	@Override
	public boolean aUnCouplage() {
		return (getCouple() != null);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#calculate()
	 */
	@Override
	public void calculate() {
		this.weights.clear();
		super.calculate();		
	}

	
	
	
	

	/**
	 * This function returns the number of Pi electrons in the molecule
	 * 
	 * @return Sum of electronic distribution
	 */
	@Override
	protected int calcSumOfPiElec() {
		int nbElecPi = 0;

		for (IHuckelObject o : this.getHuckelObjects()) {
			nbElecPi += o.getNbRadR();
		}
		for (BlocDelocalized bloc : this.getListBlocDelocalized()) {
			nbElecPi += bloc.getNbElectron();
		}

		return nbElecPi;

	}
	
	
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#calcDistributionElecArray()
	 */
	@Override
	protected int[] calcDistributionElecArray() {
		
		lstCouplesOMs.clear();
		int[] repartitionElec = new int[countHuckelAtoms()];
		// IMoleculeComponent[] locations = new
		// IMoleculeComponent[countHuckelAtoms()];
	
		double[][] orbitales = getOrbitalsCoeffArray();
		// intitialize the number that we must put on radical R and double bonds
		int nbElecADistribuer = getNbElecCharge() + getSumAtomsNbElecPi();
	
		boolean trouve;
		int a1SeqNum=0;
		int a2SeqNum=0;
		double valRetenue;
		double valTmp;
		int idxRetenu = 0;
		int i;
		int j;
	
		// intialize table of repartition of elec pi
		for (i = 0; i < repartitionElec.length; i++)
			repartitionElec[i] = 0;
	
		
		//>>>>>> BLOCS : on commencer par repartir les electrons des blocs
		
		// association bloc a une orbitale, la liste est triee selon le niveau d'energie. Le tableau d'object prend 
		// trois valeurs : l'orbitale, l'énergie de celle-ci et le le bloc auquel elle appartient
		// cela permet de faire un comparateur par energie
		ArrayList<Object[]> blocOrbitals = new ArrayList <Object[]>();
		
		
		
		double threshold = 0.0;
	
		
		/* pour chaque orbitale on regarde si un atome est dans le bloc (vrai si le coeff > seuil). si c'est le cas, l'orbitale appartient au bloc de l'atome
		et donc on lui met un electron
		*/ 
		for (j = 0; j < orbitales.length; j++) {
			
			// parcourir les orbitales  associe un bloc a une orbitale
			for (HuckelAtom atom : this.getHuckelAtoms()) {
				a1SeqNum= atom.getSeqNum() - 1	;
				double coeff = orbitales[a1SeqNum][j];
				BlocDelocalized bloc = atom.getBlocDelocalizedParent();
				if ((bloc != null) && Math.abs(Maths.round(coeff, StructureDelocalized.PRECISION)) >  threshold) {
					Object[]  value = new Object[] {j, energiesArray[j], atom.getBlocDelocalizedParent()};
					blocOrbitals.add(value);
					System.out.println("orbitale " + j +  " de coeff " +  orbitales[a1SeqNum][j] + " contient l'atome " + atom + " donc elle appartient au bloc de " + bloc.getNbElectron() + " electrons" );
					System.out.println("size =  " + blocOrbitals.size()); 
					break;
				}
				
			} // fin for HuckelAtoms
	
		} // fin j orbitales
	
		
		// on trie le tableau des orbitales de bloc par energie 
		Collections.sort(blocOrbitals, new Comparator(){
			public int compare(Object o1, Object o2) {
				double e1 = (Double) ((Object[]) o1)[1];
				double e2 = (Double) ((Object[]) o2)[1];
				return -((Double) e1).compareTo((Double) (e2)); // signe - car beta est negatif, donc on trie dans l'ordre inverse
			}
		});
	
		System.out.println("Les blocs occupent  " +  blocOrbitals.size() + " orbitales");		
		
		// pour chaque bloc on va repartir les electrons sur chacune des orbitales lui appartenant
		for (BlocDelocalized bloc : this.getListBlocDelocalized()) {
			int blocElectrons =  bloc.getNbElectron();
			// on recupere le numero orbiales du bloc courant qu'on met dans une liste
			ArrayList<Integer> orbitalesIndices = new ArrayList<Integer>();
			for (Object[] blocOrbital : blocOrbitals) {
				if (bloc == blocOrbital[2]) {
					int orbitaleIndice = (int) blocOrbital[0];
					orbitalesIndices.add(orbitaleIndice); 
				}		
			}
			// on parcourt chaque orbitale du bloc et on repartit les electrons
			int deb = 0;
			int fin = 0;
			double[] energies = getOrbitalsEnergies();
			System.out.println("on doit repartir " + blocElectrons + " electrons sur " + orbitalesIndices.size() + " orbitales " );
			// on parcourt les orbitales tant qu'il y a des electrons a distribuer avec deux curseurs (deb et end)
			while ((deb < orbitalesIndices.size()) && (blocElectrons > 0)) {
				fin = deb;
	
				// on deplace le curseur de fin sur la derniere om de meme energie que le curseur de debut
				while ((fin < orbitalesIndices.size()) && (Maths.round(energies[orbitalesIndices.get(deb)],
						StructureDelocalized.PRECISION) == Maths.round(
								energies[orbitalesIndices.get(fin)], StructureDelocalized.PRECISION))) {
					fin++;
				}
	
				// on repartit equitablemeent deux electrons sur chaque om entre les curseurs de debut et de fin 
				for (int nb = 0; nb < 2; nb++) {
					i = deb;
					while ((i < fin) && (blocElectrons > 0)) {
						repartitionElec[orbitalesIndices.get(i)]++;
						System.out.println("on place 1 electron sur l'orbitale numero " +  orbitalesIndices.get(i) );
						blocElectrons--;
						nbElecADistribuer --;
						i++;
					}
				}
			/*Si fin = (deb+1) alors
				CoupleOMs[0]=orbitalesIndices.get(deb);
				CoupleOMs[1]=orbitalesIndices.get(fin);
	
			>>	Si fin > deb+1 alors probleme : plus que 2 electrons celibataires.
				*/
				// deb+2 car fin est toujours un indice au dessus de deb
				if ((fin <= orbitalesIndices.size()) && (orbitalesIndices.get(fin-1) == (orbitalesIndices.get(deb) + 1))) {
					System.out.println ("OUI !!");
					CoupleOMs coupleOMs = new CoupleOMs();
					coupleOMs.om1=orbitalesIndices.get(deb);
					coupleOMs.om2=orbitalesIndices.get(fin-1);
					lstCouplesOMs.add(coupleOMs);
				}
	
				
				deb = fin;
	
			} // fin while
	
		} // fin parcours bloc
	
		
		
		//>>>>>> Parcourir les atomes Huckel et les liaisons Huckel hors block
		// >>>>>> distribuer les electrons sur les orbitales correspondantes
		for (IHuckelObject o : this.getHuckelObjects()) {
	
			
			if (o.getNbElectrons() > 0 && o.getBlocDelocalizedParent() == null) {
				if (o instanceof HuckelBond) {
					a1SeqNum = ((HuckelBond) o).getAtom1().getSeqNum() - 1;
					a2SeqNum = ((HuckelBond) o).getAtom2().getSeqNum() - 1;
				}
				else if (o instanceof HuckelAtom){
					a1SeqNum = ((HuckelAtom) o).getSeqNum() - 1;
					a2SeqNum = a1SeqNum;
					
				}
				
				trouve = false;
				valRetenue =orbitales[a1SeqNum][0]
					                             * orbitales[a2SeqNum][0];
				// prend l'energie la plus basse qui soit libre 
				for (j = 0; j < orbitales.length; j++) {
					valTmp = orbitales[a1SeqNum][j]
					                             * orbitales[a2SeqNum][j];
					if ((valTmp >= valRetenue) && (repartitionElec[j] == 0)) {
						valRetenue = valTmp;
						idxRetenu = j;
						trouve = true;
					}
				}
	
				if (trouve) {
					repartitionElec[idxRetenu] =o.getNbElectrons();
					nbElecADistribuer -= o.getNbElectrons();
				}// if trouve
	
			} // if
			
		} // for
	
	
	
		return repartitionElec;
	}


	/**
	 * @param cloneResults
	 * @return
	 * @throws CoupleException 
	 */
	public Object clone(boolean cloneResults){
		try {
			return new StructureLocalized (this, cloneResults);
		} catch (CoupleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#clone()
	 */
	@Override
	public Object clone() {
		try {
			return new StructureLocalized(this);
		} catch (CoupleException e) {
			return null;
		}
	}

	/**
	 * @param iAtom
	 * @return
	 * @throws StructureLocalizedException
	 */
	private int convertSeqnumSpinOM(int iAtom) throws StructureLocalizedException {
		int iSOM = -1;
		double tol = 0.95;
		LOOP_OVER_SOMs: for (int jSOM = 0; jSOM < getnSO(); jSOM++) {
			OrbitaleMoleculaire OM = null;
			try {
				OM = getSpinOM(jSOM);
			} catch (NoSOMAvailableException e) {
				HuckelIO.warning(this.getClass().getName(),
						"convertSeqnumSpinOM", e.getMessage(), e);
			} catch (UnavailableSOMException e) {
				HuckelIO.warning(this.getClass().getName(),
						"convertSeqnumSpinOM", e.getMessage(), e);
			}
			LOOP_OVER_AO: for (int i = 0; i < OM.getSize(); i++) {
				double coeff = OM.getCoeff(i);
				if (coeff > tol) {
					if ((i) == iAtom) {
						iSOM = jSOM;
						break LOOP_OVER_SOMs;
					}
				}
			}
		}
		if (iSOM == -1)
			throw new StructureLocalizedException("No OM found for atom number " + iAtom);
		return iSOM;
	}

	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#clear(boolean)
	 */
	@Override
	public void clear(boolean notify) {
		this.couple = null;
		super.clear(notify);
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#delete(boolean)
	 */
	@Override
	public void delete(boolean notify) {
		this.couple = null;
		super.delete(notify);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
	
		if (!(o instanceof StructureLocalized))
			return false;
		
		StructureLocalized s = (StructureLocalized) o;
		

		if (((s.getCouple() != null) && (this.getCouple() == null))
				|| ((s.getCouple() == null) && (this.getCouple() != null)))
			return false;
		if ((s.getCouple() == null) && (getCouple() == null))
			return super.equals(s);
		else
			return super.equals(s) && s.getCouple().equals(getCouple());
	}


	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#hashCode()
	 */
	@Override
    public int hashCode () {
        final int multiplier = 23;
        if (hashCode == 0) {
            int code = 133;
            if (this.getCouple() != null)
            	code = multiplier * code + this.getCouple().hashCode();
           	code = multiplier * code +  super.hashCode();
            hashCode = code;
        }
        return hashCode;
	
	}
	
	/**
	 * 
	 */
	protected void autoRemovecouple() {
		if (this.couple != null) {
			if ((this.couple.getHuckelAtom1() == null)
					|| (this.couple.getHuckelAtom2() == null))
				removeCouple();
			else if ((this.couple.getHuckelAtom1().getNbRadR() != 1)
					|| (this.couple.getHuckelAtom2().getNbRadR() != 1))
				removeCouple();
		}
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#fireHuckelAtomRadRChanged(org.ctom.hulis.huckel.events.HuckelAtomEvent)
	 */
	@Override
	protected void fireHuckelAtomRadRChanged(HuckelAtomEvent e) {
		autoRemovecouple();
		super.fireHuckelAtomRadRChanged(e);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#fireMoleculeAtomRemoved(org.ctom.hulis.huckel.Molecule, java.lang.Object, java.lang.Object)
	 */
	@Override
	protected void fireMoleculeAtomRemoved(Molecule source, Object oldValue,
			Object newValue) {
		autoRemovecouple();
		super.fireMoleculeAtomRemoved(source, oldValue, newValue);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#getCouplage()
	 */
	@Override
	protected int[] getCouplage() {
		int[] couple_atoms = new int[2];
		int[] couple_OMs = new int[2];
		// get which atoms were coupled
		couple_atoms[0] = getCouple().getHuckelAtom1().getSeqNum() - 1;
		couple_atoms[1] = getCouple().getHuckelAtom2().getSeqNum() - 1;
		// transforms into orbitals
		try {
			couple_OMs[0] = convertSeqnumSpinOM(couple_atoms[0]);
			couple_OMs[1] = convertSeqnumSpinOM(couple_atoms[1]);
		} catch (StructureLocalizedException e) {
			HuckelIO.warning(this.getClass().getName(), "getCouplage", e
					.getMessage(), e);
		}
		return couple_OMs;
	}

	/**
	 * @return
	 */
	public CoupleLocal getCouple() {
		return this.couple;

	}

	/**
	 * @return
	 */
	public Error getError() {
		return this.error;
	}

	/**
	 * @return
	 */
	public StructureLocalized getIdenticalStruct() {
		return this.identicalStruct;
	}

	@Override
	protected Matrix getMatrixHamiltonian() {
		int x;
		int y;
		double hxy;
		int nbAtomes = countHuckelAtoms();

		if (nbAtomes == 0)
			return null;

		Matrix hamiltonien = new Matrix(nbAtomes, nbAtomes);

		for (HuckelAtom a : this.getHuckelAtoms()) {
			if (a.getSeqNum() == 0)
				return null;

			if (a.getSeqNum() > 0)
				hamiltonien.set(a.getSeqNum() - 1, a.getSeqNum() - 1, a
						.getHx());
		}
		for (HuckelBond l : this.getHuckelBonds()) {
			x = l.getAtom1().getSeqNum() - 1;
			y = l.getAtom2().getSeqNum() - 1;
			hxy = l.getHxy();
			if ((x >= 0) && (y >= 0)) {
				if (l.getBondType() > 1
						|| (l.getBlocDelocalizedParent() != null)) {
					hamiltonien.set(x, y, hxy);
					hamiltonien.set(y, x, hxy);
				} else {
					hamiltonien.set(x, y, 0.0);
					hamiltonien.set(y, x, 0.0);
				}
			}
		}

		return hamiltonien;
	}

	public CreationMethod getMethodCreation() {
		return this.methodCreation;
	}

	/**
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean isWeightExists(String methodName) {
		if (this.weights.get(methodName) == null)
			return false;
		return true;
	}
	
	/**
	 * 
	 * @param methodName
	 * @return
	 */
	public double getWeight(String methodName) {
		if (this.weights.get(methodName) == null)
			return 0.0;
		return this.weights.get(methodName);
	}
	
	/**
	 * 
	 */
	public void removeCouple() {
		if (this.couple == null)
			return;
		String old = this.couple.toString();
		this.couple = null;
		fireStructureLocalizedCoupleRemoved(this, old, null);

	}

	/**
	 * 
	 * @param couple
	 */
	public void setCouple(CoupleLocal couple) {
		if (this.couple != null)
			if (couple.equals(this.couple))
				return;

		String old;
		if (this.couple != null)
			old = this.couple.toString();
		else
			old = null;
		this.couple = couple;

		if (old != this.couple.toString())
			fireStructureLocalizedCoupleChanged(this, old, couple);

	}

	/**
	 * 
	 * @param error
	 */
	public void setError(Error error) {
		this.error = error;
		// this.fireMoleculeChanged("error", old, this.error);

	}

	/**
	 * 
	 * @param s
	 */
	public void setIdenticalStruct(StructureLocalized s) {
		this.identicalStruct = s;
	}

	// yannick modif

	/**
	 * @param methodCreation
	 */
	public void setMethodCreation(CreationMethod methodCreation) {
		this.methodCreation = methodCreation;
	}

	/**
	 * @param methodName
	 * @param weight
	 */
	public void setWeight(String methodName, double weight) {
		double old;
		if (this.weights.get(methodName) == null)
			old = 0.0;
		else
			old = this.weights.get(methodName);
		this.weights.put(methodName, weight);
		if (weight != old)
			fireStructureLocalizedWeightChanged(this, old, weight);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#updateCharges()
	 */
	@Override
	protected void updateCharges() {
		for (HuckelAtom a : getHuckelAtoms()) {
			// deb test calcul auto de charge
			int nbElecPi = a.getNbElecPi();
			int nrad = a.getNbRadR();
			int nDB = a.countDBondAtom();
			int tDB = a.countTBondAtom();
			int blocs = a.countBondAtomInBloc();
			blocs = 0;
			int newCharge = (nbElecPi - nrad - nDB - blocs);
			a.setCharge(newCharge);

			/*
			 * HuckelIO.PrintIf("Nouvelle charge atome :"+this.getSeqNum());
			 * HuckelIO.PrintIf("nbElecPi = "+ nbElecPi);
			 * HuckelIO.PrintIf("nradical = "+nrad); HuckelIO.PrintIf("Nbre
			 * DB = "+nDB); HuckelIO.PrintIf("New Charge = "+newCharge);
			 */
			// fin test calcul auto de charge
		}

	}

	/**
	 * @throws CoupleException
	 */
	public void autoCouplage() throws CoupleException {
		if (this.countSingleElectronsOnAtoms() > 1 && (this.couple == null)) {
			// on prend 2 radicaux
			int i = 0;
			HuckelAtom[] tab = new HuckelAtom[2];
			for (HuckelAtom a : getHuckelAtoms()) {
				if (a.getNbRadR() == 1)
					tab[i++] = a;
				if (i > 1)
					break; // on a remplit le tableau
			}
			// on les couple
			
		
			// assertion : tab ne peut pas etre null
			this.couple = new CoupleLocal(tab[0], tab[1]);
		}
	}

	/**
	 * @param source
	 * @param oldValue
	 * @param newValue
	 */
	protected void fireStructureLocalizedCoupleRemoved(StructureLocalized source,
			Object oldValue, Object newValue) {
		if (!isEnabledNotify()) {
			HuckelIO
			.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		StructureLocalizedEvent event = null;

		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new StructureLocalizedEvent(source, oldValue, newValue);
			((IStructureLocalizedListener) listener)
			.structureLocalizedCoupleRemoved(event);
		}
	}

	/**
	 * @param source
	 * @param oldValue
	 * @param newValue
	 */
	protected void fireStructureLocalizedWeightChanged(StructureLocalized source,
			Object oldValue, Object newValue) {
		if (!isEnabledNotify()) {
			HuckelIO
			.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		StructureLocalizedEvent event = null;

		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new StructureLocalizedEvent(source, oldValue, newValue);
			((IStructureLocalizedListener) listener)
			.structureLocalizedWeightChanged(event);
		}
	}

	/**
	 * @param source
	 * @param oldValue
	 * @param newValue
	 */
	protected void fireStructureLocalizedCoupleChanged(StructureLocalized source,
			Object oldValue, Object newValue) {
		if (!isEnabledNotify()) {
			HuckelIO
			.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		StructureLocalizedEvent event = null;

		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new StructureLocalizedEvent(source, oldValue, newValue);
			((IStructureLocalizedListener) listener)
			.structureLocalizedCoupleChanged(event);
		}
	}

	/**
	 * @param listener
	 */
	public void addListener(IStructureLocalizedListener listener) {
		super.addListener(listener);
	}

	/**
	 * @param listener
	 */
	public void removeListener(IStructureLocalizedListener listener) {
		super.removeListener(listener);
	}

	/**
	 * calculate and check if has too much electrons
	 * @return
	 */
	public boolean hasTooManyElectrons() {
		return (this.countSingleElectronsOnAtoms()>2);
	}
	
	/**
	 * calculate and check if the number of pi electrons is correct. if mesomery is null then return true
	 * @return
	 */
	public boolean hasCorrectNbPiElec() {
		Mesomery mesomery = this.getMesomeryParent();
		if (mesomery == null)
			return true;
		
		
		int correctNbPiElec = mesomery.getDelocalizedStructure().getSumOfPiElec();
		return (this.getSumOfPiElec() == correctNbPiElec);
		
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.structures.Structure#calculateOverlapHLP()
	 */
	@Override
	public void calculateOverlapHLP() {
		if (this.hasCorrectNbPiElec() && !this.hasTooManyElectrons())
			super.calculateOverlapHLP();
	}



	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.structures.Structure#generateSpinOM()
	 */
	@Override 
	protected void generateSpinOM() throws NoElectronException, NoSOMAvailableException, UnavailableSOMException {
		super.generateSpinOM();
		System.out.println("GENERATE SPIN OM @Override");
		for (int i = 0; i < this.countSpinOM() ; i++){
			SpinOrbitaleMoleculaire som = this.getSpinOM(i);
			System.out.println(i + " " + som.getSpin() + " " + som.getPartner().getSpin());
			
		}
		System.out.println("FIN GENERATE SPIN OM @Override"  + lstCouplesOMs.size());
		
		for (CoupleOMs coupleOM : lstCouplesOMs) {
			try {
				SpinOrbitaleMoleculaire spinOM1 = this.getSpinOM(2 * coupleOM.om1);
				SpinOrbitaleMoleculaire spinOM2 = this.getSpinOM(2 * coupleOM.om2);
				System.out.println(coupleOM.om1);
				System.out.println(coupleOM.om2);
				System.out.println(spinOM1.getSpin());
				System.out.println(spinOM1.getPartner().getSpin());
				System.out.println(spinOM2.getSpin());
				System.out.println(spinOM2.getPartner().getSpin());
				if (((spinOM1.getSpin() == Spin.ALPHA) && (spinOM1.getPartner().getSpin() == Spin.NOSPIN_BETA))
					&& ((spinOM2.getSpin() == Spin.ALPHA) && (spinOM2.getPartner().getSpin() == Spin.NOSPIN_BETA)))
					spinOM2.switchSpin();
			} catch (OrbitaleMoleculaireException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
