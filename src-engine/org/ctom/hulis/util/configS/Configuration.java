 package org.ctom.hulis.util.configS;
//
//
// import org.ctom.hulis.huckel.Bond;
// import org.ctom.hulis.huckel.HuckelBond;
// import org.ctom.hulis.huckel.StructureDelocalized;
// import org.ctom.hulis.huckel.StructureLocalized;
// import org.ctom.hulis.huckel.Exceptions.HulisException;
//
// import java.util.ArrayList;
// import java.util.Random;
//
//
//
// /** This class describes a configuration object
// *
// * @author Yannick CARISSAN
// * @version 1.4.5
// * @since 1.0
// *
// */
//
public class Configuration {
//
//
// public static final int SIG_NORMAL=0;
// public static final int SIG_NOMORERADICALS=10;
// public static final int SIG_NOTLEGAL=20;
// public static final int SIG_TOOMANYITERATIONS=30;
// public static final int SIG_COULDNOTCHARGE=40;
// private ArrayList<Configuration> next;
//
// private int [] conf;
// private int [] confinit;
// private int [] bondType;
// private int nExtraCharge;
// private int nRadical;
//
// final static int nMaxIter = 100;
//
// StructureLocalized skeleton;
//
// /** Copy Constructor : Copy a Configuration into a new one
// *
// */
// public Configuration (Configuration orig) {
//
// this.nExtraCharge = orig.nExtraCharge;
// this.nRadical = orig.nRadical;
//
// this.skeleton = new StructureLocalized(orig.skeleton);
// this.conf = new int [orig.conf.length];
// this.confinit = new int [orig.conf.length];
// this.bondType = new int [orig.bondType.length];
//
// this.conf=orig.conf.clone();
// this.confinit=orig.confinit.clone();
// this.bondType=orig.bondType.clone();
// this.next=null;
//
// }
//
// // /** Constructor : Creates a new Configuration using the Molecule given as
// argument
// // *
// // */
// // public Configuration(StructureDelocalized skeleton_orig) throws Exception
// {
// //
// // this.skeleton = new StructureLocalized(skeleton_orig);
// // this.skeleton.startFlyCalculate();
// //
// // int nAtoms = this.skeleton.countHuckelAtoms();
// // int nBonds = this.skeleton.countHuckelBonds();
// //
// // this.nExtraCharge = 0;
// // this.nRadical = 0;
// //
// // this.conf = new int [nAtoms];
// // this.confinit = new int [nAtoms];
// // this.bondType = new int [nBonds];
// // this.next=null;
// // }
//
// // /** Constructor : Creates a new Configuration using the Molecule given as
// argument
// // * followed by the charge and the nbre of radical
// // */
// // public Configuration(StructureDelocalized skeleton_orig, int nbreCharge,
// int nbreRadical) throws Exception {
// //
// // this(skeleton_orig);
// // this.nExtraCharge = nbreCharge;
// // this.nRadical = nbreRadical;
// // }
// //
// /** Init : initializes an object Configuration
// * this routine creates for a given basicMolecule a configuration which is
// * as neutral as possible
// */
// // private void Init() throws Exception {
// //
// // int nElectrons =
// this.skeleton.getSumAtomsNbElecPi()-this.skeleton.getNbElecCharge();
// // int nAtoms = this.skeleton.countAtoms();
// //
// //// Creates the first configuration
// // for (int iat = 0; iat<nAtoms; iat++) {
// // this.conf[iat]=0;
// // }
// //
// // //On fait du remplissage d'orbitales avec les electrons
// // for (int iat = 0; iat<nAtoms; iat++) {
// //// cette procedure genere une structure la plus neutre possible
// // int nPiElectrons =
// this.skeleton.getHuckelAtomBySeqNum(iat+1).getNbElecPi();
// //
// // if (nElectrons >= nPiElectrons) {
// // this.conf[iat]=nPiElectrons;
// // nElectrons = nElectrons - nPiElectrons;
// // } else {
// // this.conf[iat]=nElectrons;
// // nElectrons=0;
// // break;
// // }
// // }
// // for (int iat = 0; iat<nAtoms; iat++) {
// // int nPiElectrons =
// this.skeleton.getHuckelAtomBySeqNum(iat+1).getNbElecPi();
// //
// this.skeleton.getHuckelAtomBySeqNum(iat+1).addCharge(nPiElectrons-this.conf[iat]);
// // }
// // this.confinit=this.conf.clone();
// // }
//
// /** InitBonds : set all the bonds of the molecule to singles
// *
// */
// // private void InitBonds() throws Exception {
// //
// //// On met toutes les liaisons simlples
// // int nBonds = this.skeleton.countBonds();
// //
// // for (int ibond=0; ibond<nBonds; ibond++) {
// // int type=1;
// // this.setBondType(ibond, type);
// // this.bondType[ibond]=type;
// // }
// //
// // }
// //
// // private void changeBondType(int ibond, int type) throws Exception {
// // HuckelBond l;
// // if (!(skeleton.getBond(ibond) instanceof HuckelBond))
// // throw new HulisException ("the bond " + ibond + " is not a HuckelBond");
// // l = (HuckelBond) skeleton.getBond(ibond);
// // int deltaType = type - l.getBondType();
// // if (skeleton.changeBondType(ibond, type)) {
// //
// // int iat=skeleton.getAtomIdx(l.getAtom1());
// // int jat=skeleton.getAtomIdx(l.getAtom2());
// //
// // conf[iat] -= deltaType;
// // conf[jat] -= deltaType;
// //
// // bondType[ibond]=type;
// // }
// // }
// //
// // private void setBondType(int iBond, int type) throws Exception {
// // this.bondType[iBond] = type;
// // this.skeleton.setBondType(iBond, type);
// // }
//
// // /** Counts the number of radicals (atoms with one and one only electorn)
// // * @return number of radicals
// // */
// // private int countRadical() {
// // return GetRadicalList().size();
// // }
// //
// // /** Generates the list of atoms which carry a radical
// // *
// // * @return list of atoms which carry a radical
// // */
// // private ArrayList<Integer> GetRadicalList() {
// // ArrayList<Integer> lstRadical = new ArrayList<Integer>();
// // for (int iat = 0; iat < skeleton.countAtoms(); iat++) {
// // if (conf[iat] == 1) {
// // lstRadical.add(iat+1);
// // }
// // }
// // return lstRadical;
// // }
//
//
//
// /** drawElectrons : put the radicals on a skeleton of a Configuration
// * according to the electronic distribution
// *
// */
// // private void drawElectrons() throws Exception {
// //
// // int nAtoms = this.skeleton.countAtoms();
// //
// //// on ajoute les radicaux
// // for (int iat=0; iat< nAtoms; iat++ ) {
// // if (this.conf[iat] > 0) {
// // this.skeleton.getHuckelAtomBySeqNum(iat+1).setRadR(this.conf[iat]);
// // } else {
// // this.skeleton.getHuckelAtomBySeqNum(iat+1).setRadR(0);
// // }
// // }
// // }
//
// /** update : updates the drawing
// *
// */
// // private void update() throws Exception {
// // this.drawElectrons();
// // }
//
// // public int runGenConf() throws Exception {
// //
// // this.Init();
// // if (this.charge() == false) {
// // return SIG_COULDNOTCHARGE;
// // }
// // this.InitBonds();
// // this.generateAll();
// //
// // return SIG_NORMAL;
// // }
// //
// // public void generateAll() throws Exception{
// // //on compte combien on peut faire de liaison <=> paire d'atomes adjacents
// // for (int iat = 0 ; iat<this.conf.length; iat++) {
// // for (int jat = iat+1 ; jat<this.conf.length; jat++) {
// // Bond bond = this.canBind(iat, jat);
// // if ( bond != null ) {
// // Configuration newConf = new Configuration(this);
// // int ibond = bond.getSeqNum();
// // newConf.changeBondType(ibond-1, 2);
// //// newConf.way += " "+iat+"-"+jat;
// // if ( this.next == null ) {
// // this.next = new ArrayList<Configuration>();
// // }
// // this.next.add(newConf);
// //// if (newConf.countRadical() == newConf.nRadical) {
// //// return;
// //// } else {
// //// newConf.generateAll();
// //// }
// // newConf.generateAll();
// // }
// // }
// // }
// // }
//
// // public ArrayList<Configuration> getFinalList() {
// // ArrayList<Configuration> lstFinal = new ArrayList<Configuration>();
// // if (this.next == null) {
// //// if (this.countRadical() != this.nRadical) {return null;}
// // try {
// // this.drawElectrons();
// // } catch (Exception e) {
// // // TODO Auto-generated catch block
// // e.printStackTrace();
// // }
// // lstFinal.add(this);
// // } else {
// // for (Configuration config : this.next) {
// // lstFinal.addAll(config.getFinalList());
// // }
// // }
// // return lstFinal;
// // }
//
// // private Bond canBind(int iat, int jat) {
// // Bond bond;
// // bond = this.skeleton.areAdjacentAtoms(iat+1, jat+1);
// // if ( bond == null) {
// // return null; // the atoms must be adjacent
// // }
// // if ( this.conf[iat] != 1 || this.conf[jat] != 1 ) {
// // return null; // can electrons be shared (n>1 && no lone pair)
// // }
// // return bond;
// // }
// //
// // private boolean charge() {
// // if (this.nExtraCharge==0) {
// // return true;
// // }
// // Random generator = new Random();
// //
// // boolean go=true;
// // int niter=0;
// // while (this.checkConf()==false||go) {
// // int idx = 0;
// // int jdx = 0;
// // niter++;
// // if (niter > 100) {
// // return false;
// // }
// // go=false;
// // this.conf=this.confinit.clone();
// // while (idx==jdx) {
// // idx = generator.nextInt(this.skeleton.countAtoms());
// // jdx = generator.nextInt(this.skeleton.countAtoms());
// // }
// // int q=this.nExtraCharge;
// // this.conf[idx] = this.conf[idx] + q;
// // this.skeleton.getHuckelAtomBySeqNum(idx+1).addCharge(-q);
// // this.conf[jdx] = this.conf[jdx] - q;
// // this.skeleton.getHuckelAtomBySeqNum(jdx+1).addCharge( q);
// // }
// // this.confinit=this.conf.clone();
// // return true;
// // }
//
// // private boolean checkConf() {
// // for (int iconf : this.conf) {
// // if (iconf<0) {
// // return false;
// // }
// // if (iconf>2) {
// // return false;
// // }
// // }
// // return true;
// // }
//
// /** Print : Prints a configuration to standard output
// *
// */
// // public void Print() {
// //
// // int nAtoms = this.skeleton.countAtoms();
// // int nBonds = this.skeleton.countBonds();
// // System.out.print("\n\nCONFIGURATION\n\n");
// //
// // System.out.print("Configuration :");
// // for (int iat=0; iat<nAtoms; iat++){
// // System.out.print(" "+this.conf[iat]);
// // }
// // System.out.print(" (INIT :");
// // for (int iat=0; iat<nAtoms; iat++){
// // System.out.print(" "+this.confinit[iat]);
// // }
// // System.out.print(" )\n");
// //
// // System.out.print("Bond Types :");
// // for (int ibond=0; ibond<nBonds; ibond++){
// // System.out.print(" "+this.bondType[ibond]);
// // }
// // System.out.print("\n");
// // }
//
// /** isEqualTo : returns true if configurations are the same
// *
// * @param that : configuration to compare
// * @return true if the configurations are alike
// */
// public boolean isEqualTo(Configuration that) {
//
// if (this.nExtraCharge != that.nExtraCharge) {return false;}
// // if (this.nRadical != that.nRadical) {return false;}
// if (this.skeleton.countAtoms() != that.skeleton.countAtoms()) {return false;}
// if (this.skeleton.countBonds() != that.skeleton.countBonds()) {return false;}
//
// for (int iat = 0; iat<this.skeleton.countAtoms(); iat++) {
// if (this.confinit[iat] != that.confinit[iat]) {
// return false;
// }
// }
// for (int iBond = 0; iBond < this.bondType.length; iBond++) {
// if (this.bondType[iBond] != that.bondType[iBond]) {return false;}
// }
// return true;
// }
//
// /** CreateMolecule : returns the basicMolecule associated to the
// Configuration
// *
// */
// public StructureLocalized getMolecule() throws Exception {
//
// StructureLocalized newmolecule = new StructureLocalized(this.skeleton);
// return newmolecule;
// }
//
 }
