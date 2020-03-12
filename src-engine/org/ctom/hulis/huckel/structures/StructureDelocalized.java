package org.ctom.hulis.huckel.structures;


import java.io.Serializable;
import java.util.ArrayList;

import org.ctom.hulis.files.xyz.GeometryReader;
import org.ctom.hulis.files.xyz.ReadxyzException;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.MonoExcitation;
import org.ctom.hulis.huckel.Spin;
import org.ctom.hulis.huckel.SpinOrbitaleMoleculaire;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.HulisException;
import org.ctom.hulis.huckel.exception.MoleculeAtomNullException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.exception.MonoExcitationException;
import org.ctom.hulis.huckel.exception.SpinOrbitaleMoleculaireException;
import org.ctom.hulis.huckel.listeners.IMoleculeValueListener;
import org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Maths;
import org.ctom.util.maths.Rotation;

import Jama.Matrix;

public class StructureDelocalized extends Structure implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1509299117295377271L;
	public final static int PRECISION = 2;

	public static final String NAME_DELOCALIZED_STRUCTURE = "tot";

	public static int precision = 2;

	public StructureDelocalized() {
		super();
		setName(StructureDelocalized.NAME_DELOCALIZED_STRUCTURE);
	}

	public StructureDelocalized(GeometryReader readxyz) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException, ReadxyzException {
		super();
		setName(StructureDelocalized.NAME_DELOCALIZED_STRUCTURE);
		setAutoCorrectH(false);
		this.geometry = new Geometry();
		readxyz.readAll(geometry);
		createMolecule(geometry);
		setAutoCorrectH(true);
		if (isEnabledFlyCalculate())
			calculate();
	}


	/**
	 * @param cloneResults
	 * @return
	 */
	public Object clone(boolean cloneResults) {
		return new StructureDelocalized (this, cloneResults);
	}


	public StructureDelocalized(StructureDelocalized s) {
		this(s, s.isCloneResultsCacheWhenCloneStructure()) ;
	}

	
	public StructureDelocalized(StructureDelocalized s, boolean cloneStructureResults) {
		super(s);
		if (cloneStructureResults) 
			this.copyArrays(s);
		
		setName(s.getName());
	}



	@Override
	public void addAtom(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {

		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new MoleculeCoherenceException ("Cannot add an atom in the delocalized structure. There are localized structures in the mesomery");
		super.addAtom(a);
	}


	@Override
	public void addBond(Bond b) throws  MoleculeCoherenceException, HuckelBondException, BondException, MoleculeBondExistsException, MoleculeTooManyNeighboursException {
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new MoleculeCoherenceException ("Cannot add an bond in the delocalized structure. There are localized structures in the mesomery");
		super.addBond(b);
	}

	@Override
	public void removeAtom(Atom a) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new MoleculeCoherenceException ("Cannot remove an atom in the delocalized structure. There are localized structures in the mesomery");
		super.removeAtom(a);
	}

	@Override
	public void addBlocDelocalized(BlocDelocalized blocDelocalized) {
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new RuntimeException ("Cannot add a block in the delocalized structure. There are localized structures in the mesomery");
		super.addBlocDelocalized(blocDelocalized);
	
	}

	@Override
	public void removeBlocDelocalized(BlocDelocalized blocDelocalized) {
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new RuntimeException ("Cannot remove a block in the delocalized structure. There are localized structures in the mesomery");
		super.removeBlocDelocalized(blocDelocalized);
	
	}

	@Override
	public void removeBond(Bond l) throws HuckelBondException, BondException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException {
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new MoleculeCoherenceException ("Cannot remove a bond in the delocalized structure. There are localized structures in the mesomery");
		super.removeBond(l);
	}


	@Override
	public void replaceAtom(Atom old, Atom newAtom) throws HuckelBondException, BondException, MoleculeAtomNullException, MoleculeTooManyNeighboursException, MoleculeCoherenceException, MoleculeBondExistsException{
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new MoleculeCoherenceException ("Cannot replace an atom in the delocalized structure. There are localized structures in the mesomery");
		super.replaceAtom(old, newAtom);
	}

	@Override
	public void removeMonoExcitation (MonoExcitation monoExcitation){
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new RuntimeException("Cannot remove excitation in the delocalized structure. There are localized structures in the mesomery");
		super.removeMonoExcitation(monoExcitation);
	}


	@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new RuntimeException("Cannot trigger a monoexcitation in the delocalized structure. There are localized structures in the mesomery");
		super.monoExcitationTriggered(e);
	}
	
	

	@Override
	public void rotate(Rotation mRot) {
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new RuntimeException("Cannot rotate the delocalized structure. There are localized structures in the mesomery");
		super.rotate(mRot);
	}

	@Override
	public void move(double dx, double dy, double dz) {
		// TODO :  decommenter et reprogrammer Controller.Center par rapport a une marge dans la vue et ne pas appeler move
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new RuntimeException("Cannot move the delocalized structure. There are localized structures in the mesomery");
		super.move(dx, dy, dz);

	}

	@Override
	public void setNbElecCharge(int nbElecCharge) throws MoleculeCoherenceException  {		
		if  ((this.mesomeryParent != null) && (this.mesomeryParent.countLocalizedStructs() >0))
			throw new MoleculeCoherenceException ("Cannot change eletrons of the delocalized structure. There are localized structures in the mesomery");
		super.setNbElecCharge(nbElecCharge);
	}


	@Override
	public void resetSeqNumListHuckelAtomToZero() {
		 if  ( (this.getMesomeryParent() != null) && (this.getMesomeryParent() .countLocalizedStructs() >0))
				throw new RuntimeException("Cannot number the atoms. There are localized structures in the mesomery");
		 super.resetSeqNumListHuckelAtomToZero();

	}
	
	@Override
	public void setAutoSeqNumListHuckelAtom() {
		 if  ( (this.getMesomeryParent() != null) && (this.getMesomeryParent() .countLocalizedStructs() >0))
				throw new RuntimeException("Cannot number the atoms. There are localized structures in the mesomery");
		 super.setAutoSeqNumListHuckelAtom();

	}

	@Override
	protected void appliqueCouplage() {
	}

	@Override
	protected int[] calcDistributionElecArray() {

		int[] repartitionElec = new int[countHuckelAtoms()];
		double[] energies = getOrbitalsEnergies();
		int nbElecADistribuer = getNbElecCharge() + getSumAtomsNbElecPi();

		int i;

		for (i = 0; i < repartitionElec.length; i++)
			repartitionElec[i] = 0;

		int deb = 0;
		int end = 0;


		while ((deb < this.energiesArray.length) && (nbElecADistribuer > 0)) {
			end = deb;

			while ((Maths.round(energies[deb],
					StructureDelocalized.precision) == Maths.round(
							energies[end], StructureDelocalized.precision))
							&& (end < repartitionElec.length)) {
				end++;
				if (end == repartitionElec.length)
					break;

			}

			for (int nb = 0; nb < 2; nb++) {
				i = deb;
				while ((i < end) && (nbElecADistribuer > 0)) {
					repartitionElec[i]++;
					nbElecADistribuer--;
					i++;
				}
			}

			deb = end;

		}

		repartitionElec = TreatExcitations(repartitionElec);

		return repartitionElec;
	}

	public int[] TreatExcitations(int[] repartitionInit) {
		int[] repartitionNew = repartitionInit.clone();
		// yannick
		// traitement des monoexcitations
		/*
		 * a problem could appear if one does not make sure that the orbitals
		 * are occupied or empty depending on the order the excitations are done
		 * Thus we treat them in a chronological order
		 */
		ArrayList<MonoExcitation> excitations = getMonoExcitations();
		if (excitations == null)
			return repartitionNew;
		/*TODO : mettre les excitations sous forme de hashtable
		 * 
		 */
		for (int i = 0; i < excitations.size(); i++) {
			MonoExcitation ex = excitations.get(i);
			int ifrom=-1;
			int ito=-1;
			try {
				/*
				 * passage du couches ferm�es aux couches ouvertes
				 */
				ifrom = ex.getFrom().getIndex()/2;
				ito = ex.getTo().getIndex()/2;
			} catch (Exception e) {
				e.printStackTrace();
			}
			repartitionNew[ifrom]--;
			repartitionNew[ito]++;
			System.out.print("Excitation from " + ifrom + " to " + ito
					+ " done\n");
		}
		return repartitionNew;
	}



	@Override
	protected int calcSumOfPiElec() {
		int nbElecPi = 0;

		nbElecPi = getSumAtomsNbElecPi() + getNbElecCharge();

		return nbElecPi;

	}

	@Override
	public Object clone() {
		return new StructureDelocalized(this);
	}

	public StructureLocalized cloneToLocalized() {
		return new StructureLocalized(this);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		
		if (!(o instanceof StructureDelocalized))
			return false;
		StructureDelocalized s = (StructureDelocalized) o;

		return super.equals(s);
	}

	
	@Override
    public int hashCode () {
        final int multiplier = 23;
        if (hashCode == 0) {
            int code = 133;
            code = multiplier * code + super.hashCode();
            hashCode = code;
        }
        return hashCode;
	
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


		for (HuckelAtom a : this.getHuckelAtoms()){
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
				hamiltonien.set(x, y, hxy);
				hamiltonien.set(y, x, hxy);
			}

		}


		return hamiltonien;
	}

	public void switchSinguletTriplet() throws SpinOrbitaleMoleculaireException {

		int initialSpin=this.getTotalSpin();
		HuckelIO.PrintIf("Total Spin : "+initialSpin);

		try {
			SpinOrbitaleMoleculaire tmpHOMO=null;
			SpinOrbitaleMoleculaire tmpLUMO= null;
			MonoExcitation monoExcitation=null;
			// NG Remarque : code source identique dans case 0 et case 2
			switch (initialSpin) {
			case 0:
				HuckelIO.PrintIfln("singulet -> triplet");
				monoExcitation = new MonoExcitation(this);
				this.getMonoExcitations().add(monoExcitation);	
				tmpHOMO=this.getHOMO(Spin.BETA);
				tmpLUMO=this.getLUMO(Spin.NOSPIN_ALPHA);
				monoExcitation.setFrom(tmpHOMO);
				monoExcitation.setTo(tmpLUMO);
				tmpHOMO.removeElectron();
				tmpLUMO.addElectron();
				this.distributionElecPiArray = calcDistributionElecArray();
				this.chargeDensitiesArray = calcChargeDensitiesArray();
				this.sumOfChargeDensities = calcSumOfChargesDensities();
				this.sumOfDistributedElec = calcSumofDistributionElec();
				this.sumOfPiElec = calcSumOfPiElec();
				this.bondOrdersArray = calcBondOrdersArray();
				this.totalEnergy.set(getTotalEnergyAlpha(), getTotalEnergyBeta());

				this.fireStructureDelocalizedSpinChanged(this, null, null);
				break;
			case 2:
				HuckelIO.PrintIfln("triplet -> singulet");				
				monoExcitation = new MonoExcitation(this);
				this.getMonoExcitations().add(monoExcitation);	
				tmpHOMO=this.getHOMO(Spin.ALPHA);
				tmpLUMO=this.getLUMO(Spin.NOSPIN_BETA);
				monoExcitation.setFrom(tmpHOMO);
				monoExcitation.setTo(tmpLUMO);
				tmpHOMO.removeElectron();
				tmpLUMO.addElectron();
				this.distributionElecPiArray = calcDistributionElecArray();
				this.chargeDensitiesArray = calcChargeDensitiesArray();
				this.sumOfChargeDensities = calcSumOfChargesDensities();
				this.sumOfDistributedElec = calcSumofDistributionElec();
				this.sumOfPiElec = calcSumOfPiElec();
				this.bondOrdersArray = calcBondOrdersArray();
				this.totalEnergy.set(getTotalEnergyAlpha(), getTotalEnergyBeta());


				this.fireStructureDelocalizedSpinChanged(this, null, null);
				break;
			default:
				HuckelIO.warning(this.getClass().toString(), "switchSinguletTriplet", "Spin modification only for singlet and triplet");
			}


		} catch (MonoExcitationException  e) {
			e.printStackTrace();
			HuckelIO.error(this.getClass().toString(), "switchSinguletTriplet", "monoExcitation erreur", e);
		} catch (HulisException e) {
			e.printStackTrace();
			HuckelIO.error(this.getClass().toString(), "switchSinguletTriplet", "SOM non disponibles.", e);
		}


	}

	public void switchSinguletCouchesOuvertes() throws SpinOrbitaleMoleculaireException {

		int initialSpin=this.getTotalSpin();
		HuckelIO.PrintIfln("Total Spin : "+initialSpin);

		try {
			SpinOrbitaleMoleculaire tmpHOMO=null;
			SpinOrbitaleMoleculaire tmpLUMO= null;
			MonoExcitation monoExcitation= null;
			// NG Remarque : code source identique dans case 0 et case 2
			switch (initialSpin) {
			case 0:
				HuckelIO.PrintIfln("singulet CF -> singulet CO");
				monoExcitation = new MonoExcitation(this);
				this.getMonoExcitations().add(monoExcitation);
				tmpHOMO = this.getHOMO(Spin.BETA);
				tmpLUMO =this.getLUMO(Spin.NOSPIN_BETA);
				monoExcitation.setFrom(tmpHOMO);
				monoExcitation.setTo(tmpLUMO);
				tmpLUMO.addElectron();
				tmpHOMO.removeElectron();
				this.distributionElecPiArray = calcDistributionElecArray();
				this.chargeDensitiesArray = calcChargeDensitiesArray();
				this.sumOfChargeDensities = calcSumOfChargesDensities();
				this.sumOfDistributedElec = calcSumofDistributionElec();
				this.sumOfPiElec = calcSumOfPiElec();
				this.bondOrdersArray = calcBondOrdersArray();
				this.totalEnergy.set(getTotalEnergyAlpha(), getTotalEnergyBeta());

				this.fireStructureDelocalizedSpinChanged(this, null, null);
				break;
			case 2:
				HuckelIO.PrintIfln("triplet -> singulet CO");				
				monoExcitation = new MonoExcitation(this);
				this.getMonoExcitations().add(monoExcitation);
				tmpHOMO = this.getHOMO(Spin.BETA);
				tmpLUMO = this.getLUMO(Spin.NOSPIN_BETA);
				monoExcitation.setFrom(tmpHOMO);
				monoExcitation.setTo(tmpLUMO);
				tmpLUMO.addElectron();
				tmpHOMO.removeElectron();
				this.distributionElecPiArray = calcDistributionElecArray();
				this.chargeDensitiesArray = calcChargeDensitiesArray();
				this.sumOfChargeDensities = calcSumOfChargesDensities();
				this.sumOfDistributedElec = calcSumofDistributionElec();
				this.sumOfPiElec = calcSumOfPiElec();
				this.bondOrdersArray = calcBondOrdersArray();
				this.totalEnergy.set(getTotalEnergyAlpha(), getTotalEnergyBeta());

				this.fireStructureDelocalizedSpinChanged(this, null, null);
				break;
			default:
				HuckelIO.warning(this.getClass().toString(), "switchSinguletCouchesOuvertes", "Spin modification only for singlet and triplet");
			}
			


		} catch (MonoExcitationException e) {
			e.printStackTrace();
			HuckelIO.error(this.getClass().toString(), "switchSinguletCouchesOuvertes", "monoExcitation erreur",e);
		} catch (HulisException e) {
			e.printStackTrace();
			HuckelIO.error(this.getClass().toString(), "switchSinguletCouchesOuvertes", "SOM non disponibles.",e);
		}


	}

	@Override
	public boolean aUnCouplage() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int[] getCouplage() {
		// TODO Auto-generated method stub
		return null;
	}

	protected void fireStructureDelocalizedSpinChanged(StructureDelocalized source, Object oldValue, Object newValue) {
		resetSOM();
		if (!isEnabledNotify()) {
			HuckelIO.PrintIf("Fire : Warning for developpers : molecule notify change = false.\n");
			return;
		}

		StructureDelocalizedEvent event = null;

		for (IMoleculeValueListener listener :  this.getValueListeners()) {
			if (event == null)
				event = new StructureDelocalizedEvent(source, oldValue, newValue);
			((IStructureDelocalizedListener)listener).structureDelocalizedSpinChanged(event);
		}	
	}

	public void addListener(IStructureDelocalizedListener listener) {
		super.addListener(listener);
	}

	public void removeListener(IStructureDelocalizedListener listener) {
		super.removeListener(listener);
	}

}
