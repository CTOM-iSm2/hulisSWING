package org.ctom.hulis.util.configS;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeSet;

import org.ctom.hulis.huckel.CoupleLocal;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.ConfException;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.prefs.Preferences;


public class Conf {

	private static TreeSet<Conf> List = null;

	private static ArrayList<Masque> masques = null;
	private static int MaxLevel;
	private static int nAtoms;
	private final static int TOP = 0;
	private static int[][] topology = null;
	private static int[] Zpi = null;
	private static Mesomery mesomery;

	private static boolean areAdjacent(int iat, int jat) {
		return Conf.topology[iat][jat] == 1;
	}

	public static void setMesomery(Mesomery mesomery) {
		Conf.mesomery = mesomery;

	}

	private static int compareConf(int[] conf1, int[] conf2) {
		int rc = 0;
		int diff;
		for (int iat = 0; iat < Conf.nAtoms; iat++) {
			diff = conf1[iat] - conf2[iat];
			if (diff != 0)
				return diff;
		}
		return rc;
	}

	private static void genMasques(int charge) {
		Conf.masques = new Masque(Conf.nAtoms).getList(charge, Preferences.getNUM_CHARGE_SEPARATION());
	}

	public static final TreeSet<Conf> getList() {
		return Conf.List;
	}

	private static boolean isNew(Conf conf) {
		boolean rc = Conf.List.isEmpty();
		rc = Conf.List.contains(conf);
		return !rc;
	}

	public static int nMasque() {
		return Conf.masques.size();
	}

	private ArrayList<BondConf> bonds;

	private TreeSet<Integer> bondSignature = null;

	private int[] charge = null;

	private int[] conf = null;

	private int level;

	private ArrayList<Conf> next;


	private Conf(Conf model) {
		this.conf = model.conf.clone();
		this.charge = model.charge.clone();
		
		this.next = null;
		this.level = model.level;
		this.bondSignature = new TreeSet<Integer>();
		for (int bondval : model.bondSignature) {
			this.bondSignature.add(bondval);
		}
		this.bonds = new ArrayList<BondConf>();
		for (BondConf bond : model.bonds) {
			this.bonds.add(bond);
		}
	}

	public Conf(StructureDelocalized skeleton) {
		this.level = Conf.TOP;
		skeleton.updateTopology();
		int[][] topo = skeleton.getTopology();
		int nHuckelAtoms = skeleton.countHuckelAtoms();
		int[] nElecPi = skeleton.getZpi();
		int totalCharge = skeleton.getNbElecCharge();
		//
		init(topo, nHuckelAtoms, nElecPi, totalCharge);
	}

	public Conf(int[][] topo, int nHuckelAtoms, int[] nElecPi, int totalCharge) {
		init(topo, nHuckelAtoms, nElecPi, totalCharge);
	}

	/**
	 * @param topo
	 * @param nHuckelAtoms
	 * @param nElecPi
	 * @param totalCharge
	 */
	private void init(int[][] topo, int nHuckelAtoms, int[] nElecPi,
			int totalCharge) {
		if (Conf.topology == null) {
			Conf.topology = topo;
		}
		if (Conf.List == null) {
			ConfComparator confComparator = new ConfComparator();
			Conf.List = new TreeSet<Conf>(confComparator);
		}
		this.next = null;
		Conf.nAtoms = nHuckelAtoms;
		this.conf = new int[Conf.nAtoms];
		for (int iat = 0; iat < Conf.nAtoms; iat++) {
			this.conf[iat] = 0;
		}
		this.bonds = new ArrayList<BondConf>();
		this.bondSignature = new TreeSet<Integer>();
		if (Conf.Zpi == null) {
			Conf.Zpi = nElecPi;
		}
		genNeutralConf();
		Conf.genMasques(-totalCharge);
		this.charge = new int[Conf.nAtoms];
	}

	private boolean addMasque(Masque masque) {
		for (int iat = 0; iat < this.conf.length; iat++) {
			this.conf[iat] += masque.get(iat);
			if (this.conf[iat] < 0 || this.conf[iat] > 2) {
				return false;
			}
		}
		return true;
	}

	public boolean applyMasque(int imask) {
		genNeutralConf();
		if (!addMasque(Conf.masques.get(imask - 1)))
			return false;
		this.charge = getCharges();
		for (int iatom = 0; iatom < Conf.nAtoms; iatom++) {
			for (int jatom = iatom + 1; jatom < Conf.nAtoms; jatom++) {
				// if 2 adjacent toms carry opposite charges, don't go further
				if (this.charge[iatom] * this.charge[jatom] < 0) {
					if (Conf.areAdjacent(iatom, jatom))
						return false;
				}
			}
		}
		return true;
	}

	private boolean areRadical(int iat, int jat) {
		return this.conf[iat] == 1 && this.conf[jat] == 1;
	}

	private void bindAtoms(int iat, int jat) {
		this.conf[iat]--;
		this.conf[jat]--;
		if (this.conf[iat] < 0 || this.conf[jat] < 0) {
			//	if (HuckelIO.isverbose())
			//		System.out.format("iat jat %5d %5d", iat, jat);
		}
		this.bonds.add(new BondConf(iat, jat));
		int bondVal = (int) (Math.pow(3, iat) + Math.pow(3, jat));
		this.bondSignature.add(bondVal);
	}

	private boolean checkConf(Conf conf) {
		// check if 2 ajacent centers carry a radical
		for (int iat = 0; iat < Conf.nAtoms; iat++) {
			for (int jat = iat + 1; jat < Conf.nAtoms; jat++) {
				if (Conf.areAdjacent(iat, jat) && conf.areRadical(iat, jat))
					return false;
			}
		}
		return true;
	}

	/*
	 * soient deux Confs a et b a>b si size(bondtype(a)) > size(bondtype(b)) a>b
	 * si bondtype(a)(i) > bondtype(b)(i) a>b si conf(a) > conf(b)
	 */
	public int compareTo(Conf that) {
		int isize = this.bondSignature.size();
		int jsize = that.bondSignature.size();
		if (isize != jsize) {
			return (new Integer(isize)).compareTo(jsize);
		}
		Iterator itthis = this.bondSignature.iterator();
		Iterator itthat = that.bondSignature.iterator();
		while (itthis.hasNext()) {
			int ival = (Integer) itthis.next();
			int jval = (Integer) itthat.next();
			int icomp = (new Integer(ival)).compareTo(jval);
			if (icomp != 0)
				return icomp;
		}
		int rc = Conf.compareConf(this.conf, that.conf);
		return (rc);
	}

	public int countRadical() {
		int nbre = 0;
		for (int iat = 0; iat < Conf.nAtoms; iat++) {
			if (this.conf[iat] == 1)
				nbre++;
		}
		return nbre;
	}

	public void dressStructureLocalized(StructureLocalized struct)
			throws Exception {
		// we add double bonds
		for (BondConf bond : this.bonds) {
			int type = 2;
			if (struct.changeBondType(bond.getAtom1() + 1, bond.getAtom2() + 1,
					type) != 0) {
				//if (HuckelIO.isverbose())
				/*	System.out
						.format(
								"Error in dressStructureLocalized iat, jat, type %d5%d5%d5\n",
								bond.getAtom1(), bond.getAtom2(), type);*/
			}
		}
		/*if (HuckelIO.isverbose()) {
			System.out.format("conf");
			for (int iat = 0; iat < Conf.nAtoms; iat++) {
				System.out.format("%5d", this.conf[iat]);
			}
			System.out.format("\n");
		}*/
		// we add radicals
		for (int iat = 0; iat < Conf.nAtoms; iat++) {
			int irad = this.conf[iat];
			struct.getHuckelAtomBySeqNum(iat + 1).do_setRadR(irad);
		}
		// we couple the radical iff there are 2
		boolean toCouple = false;
		int iAtom = -1;
		int jAtom = -1;
		LoopOverAtoms: for (int iat = 0; iat < Conf.nAtoms; iat++) {
			for (int jat = iat + 1; jat < Conf.nAtoms; jat++) {
				/* attention, le couplage ne se fait pas MAIS
				 * lors de l'affichage (ou du calcul de recouvrement, le couplage est forc�
				 */
				if ((Preferences.isCOUPLE_AUTOGEN()) && (!Conf.areAdjacent(iat, jat)) && areRadical(iat, jat)) {
					if ((iAtom == -1) || (jAtom == -1)) {
						iAtom = iat + 1;
						jAtom = jat + 1;
						toCouple = true;
					} else {
						// if we find more than one pair of radical to couple
						// we don't do a thing
						break LoopOverAtoms;
					}
				}
			}
		}
		// couple the two radicals if needed
		if (toCouple) {
			// check if atoms are rightly numbered
			// this slows down a bit the process but double check is best than
			// one
			if (!(struct.getAtomBySeqNum(iAtom) instanceof HuckelAtom)) {
				throw new ConfException("Atome " + iAtom
						+ " a coupler automatiquement n'est pas un HuckelAtom");
			}
			if (!(struct.getAtomBySeqNum(jAtom) instanceof HuckelAtom)) {
				throw new ConfException("Atome " + jAtom
						+ " a coupler automatiquement n'est pas un HuckelAtom");
			}
			struct.setCouple(new CoupleLocal((HuckelAtom) struct
					.getAtomBySeqNum(iAtom), (HuckelAtom) struct
					.getAtomBySeqNum(jAtom)));
		}

		// we add charges
		for (int iat = 0; iat < Conf.nAtoms; iat++) {
			int icharge = this.charge[iat];
			struct.getHuckelAtomBySeqNum(iat + 1).setCharge(icharge);
		}
	}

	@Override
	public boolean equals(Object o1) {
		Conf that = (Conf) o1;
		for (int bondVal : this.bondSignature) {
			if (!that.bondSignature.contains(bondVal))
				return false;
		}
		return true;
	}

	public void erase() {
		if (Conf.masques != null)
			for (Masque mask : Conf.masques) {
				mask.erase();
				mask = null;
			}
		Conf.List = null;
		Conf.masques = null;
		Conf.Zpi = null;
		Conf.topology = null;
		Conf.MaxLevel = -1;
		Conf.nAtoms = -1;

		this.conf = null;
		this.charge = null;
		this.bondSignature = null;
		this.level = -99;
		this.next = null;
		this.bonds = null;

	}

	// public void print(int lvl){
	// if (this.level == lvl) {
	// for (int iat=0; iat<nAtoms; iat++) {
	// System.out.format("%3d", conf[iat]);
	// }
	// for (int bondval : this.bondSignature) {
	// System.out.format("%5d",bondval);
	// }
	// System.out.format("\n");
	// } else if (this.next == null) {
	// return;
	// } else {
	// for (Conf config : this.next) {
	// config.print(lvl);
	// }
	// }
	// }

	// public void print(){
	// this.print(MaxLevel);
	// }

	public int generate(int nrad) {
		int nbre = 0;
		for (int iat = 0; iat < Conf.nAtoms; iat++) {
			for (int jat = iat + 1; jat < Conf.nAtoms; jat++) {
				if (Conf.mesomery != null && Conf.mesomery.isStopConsumingProccess())
					break;
				if (Conf.areAdjacent(iat, jat) && areRadical(iat, jat)) {
					if (Conf.mesomery != null && Conf.mesomery.isStopConsumingProccess())
						break;
					Conf newConf = new Conf(this);
					newConf.bindAtoms(iat, jat);
					if (Conf.isNew(newConf)) {
						newConf.levelUP();
						if (this.next == null) {
							this.next = new ArrayList<Conf>();
						}
						this.next.add(newConf);
						int newNrad = newConf.countRadical();
						if (newNrad > nrad) {
							nbre += newConf.generate(nrad);
						}
						if (newNrad == nrad) {
							if (checkConf(newConf)) {
								Conf.List.add(newConf);
								nbre++;
							}
						}
					}
				}
			}

		}
		return nbre;
	}

	private void genNeutralConf() {
		// cette procedure genere une structure la plus neutre possible
		for (int iat = 0; (iat < Conf.nAtoms); iat++) {
			this.conf[iat] = Conf.Zpi[iat];
		}
	}

	public int[] getCharges() {
		int[] charges = new int[Conf.nAtoms];
		for (int iat = 0; iat < Conf.nAtoms; iat++) {
			charges[iat] = Conf.Zpi[iat] - this.conf[iat];
		}
		return charges;
	}

	public ArrayList<Conf> getList(int lvl) {
		ArrayList<Conf> lst = new ArrayList<Conf>();
		if ((this.next == null) || (this.level == lvl)) {
			lst.add(this);
		} else {
			for (Conf config : this.next) {
				config.getList(lvl);
			}
		}
		return lst;
	}

	private void levelUP() {
		this.level++;
		Conf.MaxLevel = Math.max(Conf.MaxLevel, this.level);
	}

	public void stopFlyCalculate(Mesomery mesomery) {
		mesomery.isEnabledFlyCalculate = false;
	}

	private enum Exemple {
		BUTADIENE(
				new int[][] {
						{0, 1, 0, 0},
						{1, 0, 1, 0},
						{0, 1, 0, 1},
						{0, 0, 1, 0}
				}, new int[] {1,1,1,1}, 0),
				NAPHTALENE(
						new int[][] {
								{0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
								{1, 0, 1, 0, 0, 0, 0, 0, 0, 0},
								{0, 1, 0, 1, 0, 0, 0, 1, 0, 0},
								{0, 0, 1, 0, 1, 0, 0, 0, 0, 0},
								{0, 0, 0, 1, 0, 1, 0, 0, 0, 0},
								{0, 0, 0, 0, 1, 0, 1, 0, 0, 0},
								{0, 0, 0, 0, 0, 1, 0, 1, 0, 0},
								{0, 0, 1, 0, 0, 0, 1, 0, 1, 0},
								{0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
								{1, 0, 0, 0, 0, 0, 0, 0, 1, 0},
						}, new int[] {1,1,1,1,1,1,1,1,1,1}, 0),
				BENZENE(
						new int[][] {
								{0, 1, 0, 0, 0, 1},
								{1, 0, 1, 0, 0, 0},
								{0, 1, 0, 1, 0, 0},
								{0, 0, 1, 0, 1, 0},
								{0, 0, 0, 1, 0, 1},
								{1, 0, 0, 0, 1, 0},
						}, new int[] {1,1,1,1,1,1}, 0)
						
						;
		private int[][] topo;
		public int[][] getTopo() {
			return topo;
		}

		public int[] getnElecPi() {
			return nElecPi;
		}

		public int getTotalCharge() {
			return totalCharge;
		}

		private int[] nElecPi;
		private int totalCharge;

		private Exemple(int[][] topo, int[]nElecPi, int totalCharge) {
			this.topo=topo;
			this.nElecPi=nElecPi;
			this.totalCharge=totalCharge;
		}
	}

	public static void main(String[] args) {

		int[][] topo;
		int nHuckelAtoms;
		int[] nElecPi;
		int totalCharge;
		for (Exemple ex : Exemple.values()) {
			long t0=System.currentTimeMillis();
			Conf.Zpi=null;
			Conf.List=null;
			Conf.topology=null;
			topo=ex.getTopo();
			nHuckelAtoms=topo.length;
			nElecPi=ex.getnElecPi();
			totalCharge=ex.getTotalCharge();
			System.out.println(ex.toString()+":");

			Conf config = new Conf(topo, nHuckelAtoms, nElecPi, totalCharge);
			for (int imask = 1; imask <= Conf.nMasque(); imask++) {
				if (!config.applyMasque(imask))
					continue;
				int nMaxRadical = 2;
				if (!Preferences.isCOUPLE_AUTOGEN()) nMaxRadical=0;
				for (int nRadical = 0; nRadical <= nMaxRadical; nRadical++) {
					config.generate(nRadical);
				}
			}
			System.out.println(Conf.getList().size()+" structures found:");
			for (Conf conf : Conf.getList()) {
				for (BondConf bc : conf.bonds) {
					int iAt1 = bc.getAtom1();
					int iAt2 = bc.getAtom2();
					System.out.print(iAt1 + "=" +iAt2+" ");
				}
				for (int iat=0; iat<conf.conf.length; iat++) {
					System.out.print(conf.conf[iat]);
				}
				System.out.println();
			}
			long t1=System.currentTimeMillis();
			System.out.println("time:"+(t1-t0)+" ms");
		}

	}


}
