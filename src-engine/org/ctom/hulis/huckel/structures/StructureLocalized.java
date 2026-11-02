package org.ctom.hulis.huckel.structures;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.CoupleLocal;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBlock;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.IHuckelObject;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.MonoBlock;
import org.ctom.hulis.huckel.MonoExcitation;
import org.ctom.hulis.huckel.OrbitaleMoleculaire;
import org.ctom.hulis.huckel.Spin;
import org.ctom.hulis.huckel.SpinOrbitaleMoleculaire;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.HulisException;
import org.ctom.hulis.huckel.exception.MoleculeAtomNullException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.exception.NoElectronException;
import org.ctom.hulis.huckel.exception.NoSOMAvailableException;
import org.ctom.hulis.huckel.exception.OrbitaleMoleculaireException;
import org.ctom.hulis.huckel.exception.StructureLocalizedException;
import org.ctom.hulis.huckel.exception.UnavailableSOMException;
import org.ctom.hulis.huckel.listeners.IBlocDelocalizedListener;
import org.ctom.hulis.huckel.listeners.IMoleculeValueListener;
import org.ctom.hulis.huckel.listeners.IStructureLocalizedListener;
import org.ctom.hulis.prefs.Preferences;
import org.ctom.hulis.prefs.Preferences.COUPLAGE_TYPE;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Maths;
import org.ctom.util.maths.Rotation;

import Jama.Matrix;
//import org.json.simple.JSONObject;

/**
 * @author macbook
 *
 */
public class StructureLocalized extends Structure implements IBlocDelocalizedListener, Cloneable, Serializable 
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

	private ArrayList<BlocDelocalized> lstBlocDelocalized; // 

	private int[] block_Couple_OMs;

	
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
		this.lstBlocDelocalized = new ArrayList<BlocDelocalized>();
		this.weights = new HashMap<String, Double>();
			
	}

	
	/**
	 * @param structure
	 */
	public StructureLocalized(StructureLocalized structure)  {
		this(structure, structure.isCloneResultsCacheWhenCloneStructure()) ;
	}
	
	/**
	 * TODO A SUPPRIMER OU A RENOMMER : ce n'est pas la fonction clone standard
	 * @param cloneResults
	 * @return
	 */
	public Object clone(boolean cloneResults){
		return new StructureLocalized (this, cloneResults);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#clone()
	 */
	@Override
	public Object clone() {
		return new StructureLocalized(this);
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
            
            if (this.block_Couple_OMs != null)
            	code = multiplier * code + this.block_Couple_OMs.hashCode();
            
			if (this.lstBlocDelocalized != null)
				code = multiplier * code +  this.lstBlocDelocalized.hashCode();
           	code = multiplier * code +  super.hashCode();
            hashCode = code;
        }
        return hashCode;
	
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (! super.equals(o))
			return false;
			
		if (!(o instanceof StructureLocalized))
			return false;
			
		return true;

	}

	

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Molecule#geometryEquals(org.ctom.hulis.huckel.Molecule)
	 */
	@Override
	public boolean geometryEquals(Molecule molecule){
		if (!(molecule instanceof StructureLocalized))
			return false;
		
		StructureLocalized s = (StructureLocalized) molecule;
		
		if (Preferences.COUPLAGE_TYPE_DEFAULT.equals(COUPLAGE_TYPE.ATOMS)) {
			if (((s.getCouple() != null) && (this.getCouple() == null))
					|| ((s.getCouple() == null) && (this.getCouple() != null)))
				return false;
			else if ((s.getCouple() != null) && (this.getCouple() != null) && !s.getCouple().equals(this.getCouple()) )
				return false;
		}

		if (this.countBlocsDelocalized() != s.countBlocsDelocalized())
			return false;


		// cherche un bloc identique
		if (this.countBlocsDelocalized() != s.countBlocsDelocalized())
			return false;
		for (BlocDelocalized bloc : this.getListBlocDelocalized()) {
			boolean ok = false;
			for (BlocDelocalized sbloc : s.getListBlocDelocalized())
				if (bloc.equals(sbloc) ){
					ok = true;
					break;
				}

			if (!ok)
				return false;
		}
		return false;
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
		this.lstBlocDelocalized = new ArrayList<BlocDelocalized>();
		this.weights = new HashMap<String, Double>();
		this.methodCreation = StructureLocalized.CreationMethod.CREATED_MANUALLY;
		this.error = StructureLocalized.Error.NOT_ERROR;
		this.couple = null;
		this.block_Couple_OMs=null;
		this.lstCouplesOMs = new ArrayList<CoupleOMs>();
		updateCharges();
		if (this.isEnabledFlyCalculate())
			calculate();
	}


	/**
	 * @param structure
	 * @param cloneStructureResults
	 */
	public StructureLocalized(StructureLocalized structure, boolean cloneStructureResults) {
		super(structure);
		this.weights = new HashMap<String, Double>();
		for (Map.Entry<String, Double> entry : structure.weights.entrySet())
			this.weights.put(entry.getKey(), entry.getValue());
		
		if (cloneStructureResults)
			this.copyArrays(structure);
		this.methodCreation = StructureLocalized.CreationMethod.CREATED_MANUALLY;
		this.error = structure.error;
		
		if (structure.couple != null)
			this.couple = structure.couple.getCopy(this);
		else
			this.couple=null;
		
		if (structure.block_Couple_OMs != null) {
			this.block_Couple_OMs=structure.block_Couple_OMs.clone();
		}
		
		this.lstCouplesOMs = new ArrayList<CoupleOMs>();
		// copier blocs
		this.lstBlocDelocalized = new ArrayList<BlocDelocalized>();
		BlocDelocalized blocClone;
		for (BlocDelocalized bloc : structure.getListBlocDelocalized()){
			System.out.println ("ICI_SH"+this.getHuckelBlocks().get(index).toString());
			blocClone = bloc.getCopy(this); 
			blocClone.setMoleculeParent(this);
			blocClone.addListener(this);
			this.lstBlocDelocalized.add(blocClone);
		}
		// updateCharges();
	}
	
	protected void checkRemoveHuckelObjectInBloc (IHuckelObject o){
		BlocDelocalized bloc;
		for (int i = this.lstBlocDelocalized.size() - 1; i>=0; i-- ){
			bloc = this.lstBlocDelocalized.get(i);
			if (o.getBlocDelocalizedParent() == bloc){
				bloc.removeHuckelObject(o);
				if (bloc.countHuckelObjects() == 0) {
					bloc.clear();
					do_removeBlocDelocalized(bloc);
					bloc = null;
				}	

			}
		}
	}
	
	@Override
	public void do_removeAtom(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		super.do_removeAtom(a);
		if (a instanceof HuckelAtom)
			checkRemoveHuckelObjectInBloc ((IHuckelObject) a);
	}
	
	@Override
	protected void do_removeBond (Bond b)  {
		super.do_removeBond(b);
		if (b instanceof IHuckelObject)
			checkRemoveHuckelObjectInBloc((IHuckelObject) b);
	}

	@Override
	protected  void do_replaceAtom(Atom old, Atom newAtom) throws HuckelBondException, BondException, MoleculeAtomNullException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException{
		super.do_replaceAtom(old, newAtom);
		// trop tot		resetSOM();
		if(old instanceof HuckelAtom)
			checkRemoveHuckelObjectInBloc ((IHuckelObject) old);
	}


	public ArrayList<BlocDelocalized> getListBlocDelocalized() {
		return (ArrayList<BlocDelocalized>) this.lstBlocDelocalized.clone();
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

	public void addBlocDelocalized(BlocDelocalized blocDelocalized) {
		this.lstBlocDelocalized.add(blocDelocalized);
		blocDelocalized.setMoleculeParent(this);
		blocDelocalized.addListener(this);
		if (this.isEnabledFlyCalculate())
			calculate();
		fireBlocDelocalizedAdded(this, null, blocDelocalized);

	}
		
	
	public void removeBlocDelocalized(BlocDelocalized blocDelocalized) {
		do_removeBlocDelocalized(blocDelocalized);
		if (this.isEnabledFlyCalculate())
			calculate();		
		fireBlocDelocalizedRemoved(this, blocDelocalized, 0);
	}
	
	protected void do_removeBlocDelocalized(BlocDelocalized blocDelocalized) {
		this.lstBlocDelocalized.remove(blocDelocalized);
		blocDelocalized.setMoleculeParent(null);
		blocDelocalized.removeListener(this);
	}
	
	/**
	 * 
	 * @return the number of delocalized blocs
	 */
	public int countBlocsDelocalized() {
		if (this.lstBlocDelocalized == null)
			return 0;
		return this.lstBlocDelocalized.size();
	}

	
	public void clear() {
		for (BlocDelocalized bl : lstBlocDelocalized){
			bl.clear();
			bl.removeListener(this);
			bl = null;
		}
		this.lstBlocDelocalized.clear();
		this.setCouple(null);
		this.block_Couple_OMs=null;
		super.clear();
				

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
		switch (Preferences.COUPLAGE_TYPE_DEFAULT) {
		case ORBITALS : 
			return (this.block_Couple_OMs != null);
	
		case ATOMS :
			return (getCouple() != null);
		default :
			return false;
		}
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
		this.block_Couple_OMs=null;
		super.clear(notify);
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#delete(boolean)
	 */
	@Override
	public void delete(boolean notify) {
		this.couple = null;
		this.block_Couple_OMs=null;
		this.lstBlocDelocalized=null;
		super.delete(notify);
	}
	
	/**
	 * 
	 */
	protected void autoRemovecouple() {
		switch (Preferences.COUPLAGE_TYPE_DEFAULT) {
		case ORBITALS : 
			this.block_Couple_OMs=null;
		case ATOMS : 
			if (this.couple != null) {
				if ((this.couple.getHuckelAtom1() == null)
						|| (this.couple.getHuckelAtom2() == null))
					removeCouple();
				else if ((this.couple.getHuckelAtom1().getNbRadR() != 1)
						|| (this.couple.getHuckelAtom2().getNbRadR() != 1))
					removeCouple();
			}
			break;
		default : 
			break;
		}
		
	}


	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#getCouplage()
	 */
	@Override
	protected int[] getCouplage() {
		switch (Preferences.COUPLAGE_TYPE_DEFAULT) {
		case ORBITALS : 
			if (this.block_Couple_OMs != null) {
				return this.block_Couple_OMs;
			}
			else
				return new int[2];
		
		case ATOMS :
		
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
		default :
			return new int[2];
		}
		
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
	
	
	
	/**
	 * @return tous les blocks de huckels calculables
	 * il s'agit des blocs délocalizés non vides et des blocs individuels formés par les doubles liaisons, les atomes à 0, 1, ou 2 electrons
	 * les resultats de huckel de la structure sont consolidations des resultats individuels
	 */
	public List<HuckelBlock> getHuckelBlocks() {
		List<HuckelBlock> list = new ArrayList<HuckelBlock> ();
		
		for (BlocDelocalized b :  this.getListBlocDelocalized()) {
			if (!b.isEmpty())
				list.add(b);
		}
				
		for (IHuckelObject huckelObject : this.getHuckelObjects()) {
			if ( (huckelObject.getBlocDelocalizedParent()==null) && ((huckelObject instanceof Atom) ||
				(huckelObject instanceof HuckelBond) && (huckelObject.getNbElectrons() > 1)) ) {
					try {
						list.add(new MonoBlock (huckelObject ) );
					} catch (HulisException e) {
						e.printStackTrace();
					}
					if (HuckelIO.isverbose())
					{System.out.println ("add mono block " + huckelObject);}
			}
				
		}
		return list;
	}

	/**
	 * @return
	 */
	protected Matrix calcMatrixHamiltonian() {
		int x;
		int y;
		int nbAtomes = countHuckelAtoms();

		if (nbAtomes == 0)
			return null;

		Matrix hamiltonien = new Matrix(nbAtomes, nbAtomes);
		
		// on parcourt les petits blocs et on transpose  les resultats de huckels de ceux-ci dans la grande matrice de la structure
		List<HuckelBlock> lstBlocks=this.getHuckelBlocks();
		for (HuckelBlock bloc :  lstBlocks) {
			Matrix matrixBloc = bloc.calcMatrixHamiltonian();
			Map<Integer, HuckelAtom> tableCorrespondance =  bloc.generateTableCorrespondanceMatrixIndexToAtom();
			for (int i=0; i< matrixBloc.getColumnDimension(); i++) {
				for (int j=0; j< matrixBloc.getRowDimension(); j++) {
					x =tableCorrespondance.get(i).getSeqNum() -1 ;
					y = tableCorrespondance.get(j).getSeqNum()-1;
					double huckelParam = matrixBloc.get(i, j);
					if ((x >= 0) && (y >= 0) && (x<hamiltonien.getColumnDimension()) && (y<hamiltonien.getRowDimension()) ) {
						hamiltonien.set(x, y, huckelParam);
					}
				}	
			}
		}
		
		return hamiltonien;
	}

	

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.structures.Structure#calcOrbitalsCoefficients()
	 */
	protected double[][] calcOrbitalsCoefficients() {
		
		int countHuckelAtoms=this.countHuckelAtoms();
		double [][] orbitalsCoeffs=new double[countHuckelAtoms][countHuckelAtoms];
		
		List<HuckelBlock> lstBlocks=this.getHuckelBlocks();
		for (HuckelBlock bloc :  lstBlocks) {
			double [][] orbitalsCoeffsBlock = bloc.calcOrbitalsCoeffArray();
			Map<Integer, HuckelAtom> tableCorrespondance =  bloc.generateTableCorrespondanceMatrixIndexToAtom();
			for (int i=0; i< orbitalsCoeffsBlock.length; i++) {
				for (int j=0; j< orbitalsCoeffsBlock[0].length; j++) {
					int x =tableCorrespondance.get(i).getSeqNum() -1 ;
					int y = tableCorrespondance.get(j).getSeqNum()-1;
					double coeff = orbitalsCoeffsBlock[i][j];
					if ((x >= 0) && (y >= 0) && (x<orbitalsCoeffs.length) && (y<orbitalsCoeffs[0].length)) {
						orbitalsCoeffs[x][y] =coeff;
					}
				}	
			}
		}
		
	
		return orbitalsCoeffs;
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.structures.Structure#calcOrbitalsEnergies()
	 */
	@Override
	protected  double[] calcOrbitalsEnergies() {
		double[] orbitalEnergies = new double[countHuckelAtoms()];
		
		List<HuckelBlock> lstBlocks=this.getHuckelBlocks();
		for (HuckelBlock bloc :  lstBlocks) {
			Map<Integer, HuckelAtom> tableCorrespondance =  bloc.generateTableCorrespondanceMatrixIndexToAtom();
			double[] orbitalEnergiesBlock= bloc.calcOrbitalsEnergies();
			for (int i=0; i< orbitalEnergiesBlock.length; i++) {
				int index =tableCorrespondance.get(i).getSeqNum()-1;
				if ((index>=0) && (index < orbitalEnergies.length))
					orbitalEnergies[index]=orbitalEnergiesBlock[i];
			}
		}
		
		return orbitalEnergies;
		
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.structures.Structure#getMatrixHamiltonian()
	 */
	@Override
	protected Matrix getMatrixHamiltonian() {
		return this.calcMatrixHamiltonian();
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.structures.Structure#calcDistributionElecArray()
	 */
	@Override
	protected double[] calcDistributionElecArray() {
		double[] repartitionElec = new double[countHuckelAtoms()];
			
		List<HuckelBlock> lstBlocks=this.getHuckelBlocks();
		for (HuckelBlock bloc :  lstBlocks) {
			Map<Integer, HuckelAtom> tableCorrespondance =  bloc.generateTableCorrespondanceMatrixIndexToAtom();
			double[] repartitionElecBlock= bloc.calcDistributionElecArray();
			for (int i=0; i< repartitionElecBlock.length; i++) {
				int index =tableCorrespondance.get(i).getSeqNum()-1;
				if ((index>=0) && (index < repartitionElec.length))
					repartitionElec[index]=repartitionElecBlock[i];
			}
		}
			
		
		return repartitionElec;
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.structures.Structure#calcOrbitalsCoefficients()
	 */
	protected double[][] calcBondOrdersArray() {
		
		int countHuckelAtoms=this.countHuckelAtoms();
		double [][] ordresLiaison=new double[countHuckelAtoms][countHuckelAtoms];
		
		List<HuckelBlock> lstBlocks=this.getHuckelBlocks();
		for (HuckelBlock bloc :  lstBlocks) {
			double [][] ordresLiaisonBlock = bloc.calcBondOrdersArray();
			Map<Integer, HuckelAtom> tableCorrespondance =  bloc.generateTableCorrespondanceMatrixIndexToAtom();
			for (int i=0; i< ordresLiaisonBlock.length; i++) {
				for (int j=0; j< ordresLiaisonBlock[0].length; j++) {
					int x =tableCorrespondance.get(i).getSeqNum() -1 ;
					int y = tableCorrespondance.get(j).getSeqNum()-1;
					double bondOrder = ordresLiaisonBlock[i][j];
					if ((x >= 0) && (y >= 0) && (x<ordresLiaison.length) && (y<ordresLiaison[0].length)) {
						ordresLiaison[x][y] =bondOrder;
					}
				}	
			}
		}	
		return ordresLiaison;
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.structures.Structure#calcChargeDensitiesArray()
	 */
	@Override
	protected double[] calcChargeDensitiesArray() {
		double[] chargesDensityArray = new double[countHuckelAtoms()];
		
		List<HuckelBlock> lstBlocks=this.getHuckelBlocks();
		for (HuckelBlock bloc :  lstBlocks) {
			Map<Integer, HuckelAtom> tableCorrespondance =  bloc.generateTableCorrespondanceMatrixIndexToAtom();
			double[] orbitalEnergiesBlock= bloc.calcChargeDensitiesArray();
			for (int i=0; i< orbitalEnergiesBlock.length; i++) {
				int index =tableCorrespondance.get(i).getSeqNum()-1;
				if ((index>=0) && (index < chargesDensityArray.length))
					chargesDensityArray[index]=orbitalEnergiesBlock[i];
			}
		}
		
		return chargesDensityArray;
		
	}
	
	
	/**
	 * 
	 * @return
	 */
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
		this.block_Couple_OMs=null;
		this.couple = null;
		fireStructureLocalizedCoupleRemoved(this, old, null);

	}

	/**
	 * 
	 * @param couple
	 */
	public void setCouple(CoupleLocal couple) {
		if ((this.couple != null) && (couple.equals(this.couple)))
			return;

		String old;
		if (this.couple != null)
			old = this.couple.toString();
		else
			old = null;
		this.couple = couple;
		
		if (Preferences.COUPLAGE_TYPE_DEFAULT.equals(Preferences.COUPLAGE_TYPE.ORBITALS)) {
			this.block_Couple_OMs=new int[2];
			try {
				this.block_Couple_OMs[0] = convertSeqnumSpinOM(couple.getHuckelAtom1().getSeqNum()-1);
				this.block_Couple_OMs[1] = convertSeqnumSpinOM(couple.getHuckelAtom2().getSeqNum()-1);		
			} catch (StructureLocalizedException e) {
				e.printStackTrace();
			}
		}

		
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
	 * 
	 */
	public HuckelAtom[] guessCouple() {
		if (super.countSingleElectronsOnAtoms() > 1 ) {
			// on prend 2 radicaux
			int i = 0;
			HuckelAtom[] tab = new HuckelAtom[2];
			for (HuckelAtom a : getHuckelAtoms()) {
				if (a.getNbRadR() == 1)
					tab[i++] = a;
				if (i > 1)
					break; // on a remplit le tableau
			}
			return tab;
		}
		return null;
							
	}
	
	/**
	 * @throws CoupleException
	 */
	public void autoCouplage() throws CoupleException {
		HuckelAtom[] tab;
		if ((this.couple == null) && ((tab= this.guessCouple()) != null) ) {
			
			// assertion : tab ne peut pas etre null
			this.couple = new CoupleLocal(tab[0], tab[1]);
		}
	}
	
	
	/**
	 * NG
	 * couplage entre SOM
	 */
	public void autoCouplageSOM() {
		
		this.block_Couple_OMs=null;


		double[]ni=this.distributionElecPiArray;
		boolean trouve=false;
				
		for (int i=0; i < ni.length; i++) {

			if (ni[i]==1 && !trouve) {
				trouve=true;
				this.block_Couple_OMs=new int[2];
				try {
					block_Couple_OMs[0] = this.getSpinOM(2 * i).getIndex();
				} catch (NoSOMAvailableException | UnavailableSOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			else if (ni[i]==1 && trouve) {
				try {
					SpinOrbitaleMoleculaire spinOM = this.getSpinOM(2 * i);
					if ((spinOM.getSpin() == Spin.ALPHA) && (spinOM.getPartner().getSpin() == Spin.NOSPIN_BETA)) {
						spinOM.switchSpin();
						block_Couple_OMs[1] =spinOM.getIndex();

					}
				} catch (OrbitaleMoleculaireException | NoSOMAvailableException | UnavailableSOMException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			
		}
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
		if (HuckelIO.isverbose()) {
		System.out.print("GENERATE SPIN OM @Override");
		}
		int itest=0;
		for (int i = 0; i < this.countSpinOM() ; i++){
			SpinOrbitaleMoleculaire som = this.getSpinOM(i);
	if (HuckelIO.isverbose()) {
		{if (itest == 0 ) {itest=1; System.out.println("Configuration (ajouter 1 au numero des OM): ");}}
		if (( som.getSpin() == Spin.ALPHA) && (som.getPartner().getSpin() == Spin.BETA))             {System.out.print(" "+som.getIndex()+ "_"+som.getPartner().getIndex()+"    "); }
		else if (( som.getSpin() == Spin.BETA) && (som.getPartner().getSpin() == Spin.ALPHA))        {System.out.print("_"+som.getIndex()+" "+som.getPartner().getIndex()+"    ");} 
		else if (( som.getSpin() == Spin.NOSPIN_BETA) && (som.getPartner().getSpin() == Spin.ALPHA)) {System.out.print(" "+" ^"+som.getPartner().getIndex()+"    ");} 
		else if (( som.getSpin() == Spin.ALPHA) && (som.getPartner().getSpin() == Spin.NOSPIN_BETA)) {System.out.print(" ^"+som.getIndex()+" "+"    ");} 
		else if (( som.getSpin() == Spin.BETA) && (som.getPartner().getSpin() == Spin.NOSPIN_ALPHA)) {System.out.print("_"+""  +som.getIndex()+"    ");} 
		else if (( som.getSpin() == Spin.NOSPIN_ALPHA) && (som.getPartner().getSpin() == Spin.BETA)) {System.out.print("_"+""  +som.getPartner().getIndex()+ "    ");} 
	}
		//	System.out.println(i + " " + som.getSpin() + " " + som.getPartner().getSpin());
			
		}
		if (HuckelIO.isverbose()) {
		System.out.println();
		System.out.println("FIN GENERATE SPIN OM @Overrid:"  + lstCouplesOMs.size()+ lstCouplesOMs);/* SH MODIF*/
		}
		for (CoupleOMs coupleOM : lstCouplesOMs) {
			try {
				SpinOrbitaleMoleculaire spinOM1 = this.getSpinOM(2 * coupleOM.om1);
				SpinOrbitaleMoleculaire spinOM2 = this.getSpinOM(2 * coupleOM.om2);
			if (HuckelIO.isverbose()) {
				System.out.println(coupleOM.om1);
				System.out.println(coupleOM.om2);
				System.out.println(spinOM1.getSpin());
				System.out.println(spinOM1.getPartner().getSpin());
				System.out.println(spinOM2.getSpin());
				System.out.println(spinOM2.getPartner().getSpin());
			}
				if (((spinOM1.getSpin() == Spin.ALPHA) && (spinOM1.getPartner().getSpin() == Spin.NOSPIN_BETA))
					&& ((spinOM2.getSpin() == Spin.ALPHA) && (spinOM2.getPartner().getSpin() == Spin.NOSPIN_BETA)))
					spinOM2.switchSpin();
			} catch (OrbitaleMoleculaireException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.Structure#calcDistributionElecArray()
	 */
	protected int[] calcDistributionElecArrayOLD() {
		
		lstCouplesOMs.clear();
		int[] repartitionElec = new int[countHuckelAtoms()];
		// IMoleculeComponent[] locations = new
		// IMoleculeComponent[countHuckelAtoms()];
	
		double[][] orbitales = getOrbitalsCoeffArray();
		// intitialize the number that we must put on radical R and double bonds
	//	int nbElecADistribuer = getNbElecCharge() + getSumAtomsNbElecPi();
	
		boolean trouve;
		int indexOrbitale1=0;
		int indexOrbitale2=0;
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
				indexOrbitale1 = atom.getSeqNum() - 1	;
				double coeff = orbitales[indexOrbitale1][j];
				BlocDelocalized bloc = atom.getBlocDelocalizedParent();
				if ((bloc != null) && Math.abs(Maths.round(coeff, StructureDelocalized.PRECISION)) >  threshold) {
					Object[]  value = new Object[] {j, energiesArray[j], atom.getBlocDelocalizedParent()};
					blocOrbitals.add(value);
					System.out.println("orbitale " + j +  " de coeff " +  orbitales[indexOrbitale1][j] + " contient l'atome " + atom + " donc elle appartient au bloc de " + bloc.getNbElectron() + " electrons" );
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
					//	nbElecADistribuer --;
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
					indexOrbitale1 = ((HuckelBond) o).getAtom1().getSeqNum() - 1;
					indexOrbitale2 = ((HuckelBond) o).getAtom2().getSeqNum() - 1;
				}
				else if (o instanceof HuckelAtom){
					indexOrbitale1 = ((HuckelAtom) o).getSeqNum() - 1;
					indexOrbitale2 = indexOrbitale1;
					
				}
				
				trouve = false;
				valRetenue =(orbitales[indexOrbitale1][0]
					                             * orbitales[indexOrbitale2][0]);
				// prend l'energie la plus basse qui soit libre 
				for (j = 0; j < orbitales.length; j++) {
					valTmp = (orbitales[indexOrbitale1][j]
					                             * orbitales[indexOrbitale2][j]);
					if ((valTmp >= valRetenue) && (repartitionElec[j] == 0)) {
						valRetenue = valTmp;
						idxRetenu = j;
						trouve = true;
					}
				}
	
				if (trouve) {
					repartitionElec[idxRetenu] =o.getNbElectrons();
		//			nbElecADistribuer -= o.getNbElectrons();
				}// if trouve
	
			} // if
			
		} // for
	
		int nbElecADistribuer = getNbElecCharge() + getSumAtomsNbElecPi();
		int tot=0;
		for (int nbelec  :  repartitionElec)
			tot+=nbelec;
		
		System.out.println(nbElecADistribuer + " <==> "   + tot);
		System.out.println(Arrays.toString(repartitionElec));
		
		
		return repartitionElec;
	}
	

	//@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		this.fireBlocDelocalizedHuckelObjectAdded(e);

	}

	//@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		this.fireBlocDelocalizedHuckelObjectRemoved(e);

	}

	//@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {

		this.fireBlocDelocalizedNbElectronsChanged(e);

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

	@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		throw new RuntimeException("Cannot trigger a monoexcitation in a localized structure. It must have the same configuration as the delocalized structure");
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

	/**
	 * 
	 * @param source
	 * @param oldValue
	 * @param newValue
	 */
	protected void fireBlocDelocalizedAdded(Molecule source, Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		if (!isEnabledNotify()) {
			HuckelIO
			.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		MoleculeEvent event = null;

		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeBlocDelocalizedAdded(event);
		}
	}
	
	/**
	 * 
	 * @param e
	 */
	protected void fireBlocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		if (e.getSource().getNbElectron() >0 && this.isEnabledFlyCalculate())
			calculate();

		if (!isEnabledNotify()) {
			HuckelIO
			.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		for (IMoleculeValueListener listener : this.getValueListeners()) {

			((IMoleculeValueListener) listener)
			.blocDelocalizedHuckelObjectAdded(e);
		}
	}

	/**
	 * 
	 * @param e
	 */
	protected void fireBlocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		if (e.getSource().getNbElectron() >0 && this.isEnabledFlyCalculate())
			calculate();
	
		if (!isEnabledNotify()) {
			HuckelIO
			.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		for (IMoleculeValueListener listener : this.getValueListeners()) {

			((IMoleculeValueListener) listener)
			.blocDelocalizedHuckelObjectRemoved(e);
		}
	}
	
	/**
	 * 
	 * @param e
	 */
	protected void fireBlocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		if (this.isEnabledFlyCalculate())
			calculate();
		
		if (!isEnabledNotify()) {
			HuckelIO
			.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		for (IMoleculeValueListener listener :this.getValueListeners()) {

			((IMoleculeValueListener) listener)
			.blocDelocalizedNbElectronsChanged(e);
		}
	}

	

	/**
	 * 
	 * @param source
	 * @param oldValue
	 * @param newValue
	 */
	protected void fireBlocDelocalizedRemoved(Molecule source,
			Object oldValue, Object newValue) {
		if (this.isEnabledFlyCalculate())
			calculate();
		
		if (!isEnabledNotify()) {
			HuckelIO
			.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		MoleculeEvent event = null;

		for (IMoleculeValueListener listener : this.getValueListeners()) {
			if (event == null)
				event = new MoleculeEvent(source, oldValue, newValue);
			listener.moleculeBlocDelocalizedRemoved(event);
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
 }
