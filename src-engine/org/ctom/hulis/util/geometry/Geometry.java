package org.ctom.hulis.util.geometry;


import java.util.ArrayList;
import java.util.List;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.GeometryException;
import org.ctom.hulis.huckel.exception.HuckelAtomException;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.maths.Point3D;
import org.ctom.util.maths.Rotation;
import org.ctom.util.maths.VecteurCTOM;


public class Geometry {
	
	public static final int SCALE = 50;
	public static final double TRESHOLD_DIST = 0.2;
	
	ArrayList<VirtualMethyl> lstVirtualMethyls = new ArrayList<VirtualMethyl>();  // liste des groupement methyl dans la geometrie

	
	private ArrayList<Atom> geom = new ArrayList<Atom>();
	private List<Bond> lstBonds3D ;
	int nombre = 0;
	private InitialOrientation initialOrientation;

	private int nbAtomSav;

	/**
	 * Create an empty geometry
	 */
	public Geometry() {
		// TODO Auto-generated constructor stub
		this.initialOrientation = new InitialOrientation();
		this.nbAtomSav = 0;
	}
	
	
	/**
	 * this function returns the co-ordinates of the center of the molecule
	 * 
	 * @return Point3D
	 */
	public Point3D getCenter() {
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;

		double nbAtomes = countAtoms();
		HuckelIO.PrintIf(nbAtomes);

		if (nbAtomes ==0)
			return new Point3D(0.0, 0.0, 0.0);

		for (Atom a : this.geom) {
			x = x + a.getX();
			y = y + a.getY();
			z = z + a.getZ();
		}

		return new Point3D(x / nbAtomes, y / nbAtomes, z /nbAtomes);


	}
	
	/**
	 * 
	 * @param atom
	 * @return
	 */
	public PeriodicTable.Entry atomToEntry(Atom atom) {
		/*int iAtom = atom.getSuperSymbol();
		int neighbourgs=0;
		for (Atom a : this.geom) {
			if (a.getSuperSymbol() < iAtom) {
				for (int i : a.getConnect())
					if (i==iAtom)
						neighbourgs++;
			}
		}
		int nbConnect = atom.getConnect().size() + neighbourgs;
		return PeriodicTable.getElementToEntry (atom.getElement(), nbConnect);*/
		return PeriodicTable.getElementToEntry (atom.getElement(), atom.getConnect().size());
	}


	/**
	 * constructor by copy
	 * @param that
	 */
	public Geometry(Geometry that) {
		this.nombre = that.nombre;
		this.geom = (ArrayList<Atom>) that.geom.clone();
		this.lstVirtualMethyls =(ArrayList<VirtualMethyl>) that.lstVirtualMethyls.clone();
		this.initialOrientation = (InitialOrientation) that.initialOrientation.clone();
		this.nbAtomSav = that.nbAtomSav;

	}

	/**
	 * construct a geometry to a molecule
	 * @param molecule
	 * @throws GeometryException
	 */
	public Geometry(Molecule molecule) throws GeometryException {
		this.nbAtomSav = 0;
		synchronize(molecule);
		this.initialOrientation = new InitialOrientation();
	
	}
	
	/**
	 * add an atom in the geometry and assign a supersymbol to it
	 * @param atom
	 */
	public void add(Atom atom) {
		this.nombre++;
		atom.setSuperSymbol(this.nombre);
		atom.setXAndXGeom3D(atom.getX() * Geometry.SCALE); 
		atom.setYAndYGeom3D(atom.getY() * Geometry.SCALE);
		atom.setZAndZGeom3D(atom.getZ() * Geometry.SCALE);
		
		this.geom.add(atom);
		this.nbAtomSav++;
	}

	/**
	 * returns the number of atom of 3D geometry
	 * @return
	 */
	public int countAtoms() {
		return this.geom.size();
	}

	/**
	 * clear the atoms connectivities
	 */
	public void clearConnectivity() {
		for (Atom atom : getAtoms()) {
			atom.getConnect().clear();
		}	
	}


	/**
	 * if the atoms have not connectivity, create a list of connectivity for each atom between them
	 */
	public void generateConnectivity() {
		if (!isConnectivityEmpty()) {
			return;
		}
		for (Atom atom1 : getGeom3D()) {
			for (Atom atom2 : getGeom3D()) {
				double dist = atom1.getDist3D(atom2, getscale());
				if (dist <= (atom1.getCovalentRadius() + atom2
						.getCovalentRadius() + TRESHOLD_DIST)) {
					if (atom1.getSuperSymbol() != atom2.getSuperSymbol()) {
						HuckelIO.PrintIf("!! create BOND : "
								+ atom1.getSuperSymbol() + " - "
								+ atom2.getSuperSymbol()+"\n");
						atom1.addConnect(atom2.getSuperSymbol());
					}
					// atom2.addConnect(atom1.getSuperSymbol());
				}
			}
		}
	}
	
	
	/**
	 * if the atoms have not connectivity, create a list of connectivity for each atom between them from list bonds
	 */
	public void forceConnectivyFromBonds() {
		for (Atom a : this.geom)
			a.clearConnect();
		
		for (Bond b : this.lstBonds3D) {
			b.getAtom1().addConnect(b.getAtom2().getSuperSymbol());
			
		}
	}

	/**
	 * Be careful! if synchronize(Molecule) has never been called, atoms in the returned list
	 * and VirtualMethyl are only Atom instances and not HuckelAtom instances
	 * @return
	 */
	public ArrayList<Atom> getAtoms() {
		return getGeom3D();
	}
	
	

	/**
	 * Be careful! if synchronize(Molecule) has never been called, atoms in the returned list
 	 * are only Atom instances and not HuckelAtom instances
	 * @return
	 */
	public ArrayList<Atom> getGeom2D() {
		return getGeom();
	}

	/**
	 * Return the list of 2D structure : X-CH3 are replaced by X-ME
	 * Be careful! if synchronize(Molecule) has never been called, atoms in the returned list
	 * are only Atom instances and not HuckelAtom instances
	 * @deprecated : use getGeom2D() instead
	 * @return
	 */
	@Deprecated
	public ArrayList<Atom> getGeom() {
		ArrayList<Atom> geom2D = new ArrayList<Atom>();
		for (Atom atom : this.geom) {
			if (atom.getElement() != null) {
				geom2D.add(atom);
			}
		}
		this.replaceCH3ByME(geom2D);
		return geom2D;
		
	}

	/**
	 * return the list of geometry atoms 
	 * e careful! if synchronize(Molecule) has never been called, atoms in the returned list
	 * and VirtualMethyl are only Atom instances and not HuckelAtom instances
	 * @return
	 */
	public ArrayList<Atom> getGeom3D() {
		return this.geom;
	}

	/**
	 * 
	 * @return
	 */
	public int getscale() {
		return Geometry.SCALE;
	}

	

	/**
	 * 
	 * @return
	 */
	private boolean isConnectivityEmpty() {
		for (Atom atom : getAtoms()) {
			if (atom.getConnect().size() != 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 */
	public void print() {
		for (Atom atom : this.geom) {
			atom.print();
		}
		HuckelIO.PrintIfln();
	}



	/**
	 * @param initialOrientation
	 */
	public void saveInitialOrientation(InitialOrientation initialOrientation) {
		this.initialOrientation =initialOrientation;
		
	}
	
	/**
	 * 
	 * @return
	 */
	public InitialOrientation getInitialOrientation() {
		return this.initialOrientation;
	}
	
	
	
	
	
	/**
	 * Transform geom 3D to 2D by changing atoms locations
	 */
	public void flatGeom() {		

		// on cree une liste d ' ensemble de 3 atomes A, B,C differents l'un l'autre
		List <Atom >tripletHuckelAtom;
		List <List <Atom>> lstTripletAtom = new ArrayList<List <Atom>>();
		Atom atom1, atom2, atom3;
		int nb = this.getAtoms().size();
		for (int i = 0 ; i < nb - 2 ; i++)	{
			atom1 = this.getAtoms().get(i);
		//	if (atom1.getElement() != PeriodicTable.Element.H) {
				for (int j = i+1; j < nb -1; j++ )	{
					atom2 = this.getAtoms().get(j);
			//		if (atom2.getElement() != PeriodicTable.Element.H) {
						for (int k = j+1; k< nb ; k++)	{
							atom3 = this.getAtoms().get(k);
					//		if ( (atom3.getElement() != PeriodicTable.Element.H) && (atom1 != atom2) && (atom2 != atom3) && (atom1 != atom3) ) {
								tripletHuckelAtom = new ArrayList<Atom> ();
								tripletHuckelAtom.add(atom1);
								tripletHuckelAtom.add(atom2);
								tripletHuckelAtom.add(atom3);
								lstTripletAtom.add(tripletHuckelAtom);
								HuckelIO.PrintIfln(atom1 + " "+  atom2 + " " + atom3 + atom3.getLocation() );
				//			}
						}
			//		}
				}
		//	}
		}
				
		Atom a,b,c;
		VecteurCTOM 	ab, ac, n;
		double angle; 
		
		List <VecteurCTOM> lstPlan = new ArrayList<VecteurCTOM>();
		// on calcul l'angle des vecteurs AB (X1,Y1,Z1),AC (X2,Y2,Z2)  ou X1=xA-XB, etc.... 
		VecteurCTOM memo_n = null;
		VecteurCTOM vZ = VecteurCTOM.V_Z_AXIS; //vecteur de l'axe z;
		nb =lstTripletAtom.size();
		for (int i = 0 ; i < nb ; i++) {
			tripletHuckelAtom = lstTripletAtom.get(i);
			a= tripletHuckelAtom.get(0);
			b= tripletHuckelAtom.get(1);
			c= tripletHuckelAtom.get(2);
			//HuckelIO.PrintIf (a + " "+  b + " " + c + c.getLocation() );

			ab = new VecteurCTOM(a.getLocation(), b.getLocation());
			ac = new VecteurCTOM(a.getLocation(), c.getLocation());
			angle = ab.getAngle(ac);
			// on verifie si A,B,C sont alignes 
			// s'ils ne le sont pas on calcule le vecteur normal a leur plan 
			if (angle == 0.0)
				continue;
			n = ab.getCrossProduct(ac);
			HuckelIO.PrintIf("i =" + i);
			if (memo_n == null && n.getDotProduct(vZ) ==0.0 ){
				memo_n = n.normalize();
				HuckelIO.PrintIf ("a "  + i);
			}
			else if (memo_n == null) {
				n.timesEquals(n.getDotProduct(vZ));
				n.normalizeEquals();
				memo_n = (VecteurCTOM) n.clone();
				HuckelIO.PrintIf ("b "  + i);
			}
			else{
				n.timesEquals(n.getDotProduct(memo_n));
				n.normalizeEquals();
				HuckelIO.PrintIf ("c "  + i);
		}
		
		
			HuckelIO.PrintIfln("abc " + a + b + c + "   n=" + n);
			lstPlan.add(n);

		}
		
		// on calcule la moyenne
		double abar = 0.0, bbar =0.0, cbar = 0.0;
		nb =lstPlan.size();
		for (int i = 0; i< nb; i++) {
			n = lstPlan.get(i);
			abar += n.getX();
			bbar += n.getY();
			cbar += n.getZ();
		}

		
		abar = abar / lstPlan.size();
		bbar = bbar / lstPlan.size();
		cbar = cbar / lstPlan.size();
	
		// on obtient un vecteur et un angle
		VecteurCTOM nbar = new VecteurCTOM (abar, bbar, cbar);
		nbar.normalizeEquals();
		
		HuckelIO.PrintIfln("n moy= " + nbar);
		
		
		angle = Math.acos(nbar.getDotProduct(vZ) );
		
		//VecteurCTOM v = new VecteurCTOM(-bbar, abar, 0.0);
		VecteurCTOM v = nbar.getCrossProduct(vZ);
		v.normalizeEquals();
	
		HuckelIO.PrintIfln("angle = " + angle);
		HuckelIO.PrintIfln("cos = " + Math.cos(angle));
		HuckelIO.PrintIfln("sin = " + Math.sin(angle));
		HuckelIO.PrintIfln("v = " + v);
		
		// Definition de la matrice rotation
		Rotation mRotate = Rotation.getRotation3D(v, angle);
				
				
		// translation
		HuckelIO.PrintIfln("avant translation");
		Point3D center = this.getCenter();
		HuckelIO.PrintIfln("center " + center.getX() / 50 + " " + center.getY()/50 + " " + center.getZ() /50);
	
		for (Atom atom : this.getAtoms()) {
			HuckelIO.PrintIfln(atom + " " + atom.getX() / 50 + " " + atom.getY()/50 + " "+ atom.getZ() / 50 );
		}
	
		// translation au centre de la geometrie avant rotation pour eviter les angles et les vecteurs nulls
		Point3D origine =new Point3D(0,0,0);
		for (Atom atom : this.getAtoms()) {
			Point3D xyzOrign = (Point3D) atom.getLocationGeom3D().clone();
			atom.translate(new VecteurCTOM (origine, center)); 
			atom.setLocationGeom3D(xyzOrign);
		}
		
		HuckelIO.PrintIfln("apres translation avant rotation");
		for (Atom atom : this.getAtoms()) {
			HuckelIO.PrintIfln(atom + " " + atom.getX() / 50 + " " + atom.getY()/50 + " "+ atom.getZ() / 50 );
		}
		
		if (!Double.isNaN(angle)  && !v.isInfinite() &&  (angle % Math.PI != 0) ) {
			for (Atom atom : this.getAtoms()){
				Point3D xyzOrign = (Point3D) atom.getLocationGeom3D().clone();
				atom.rotate(mRotate);
				atom.setLocationGeom3D(xyzOrign);
			}
		}
		else
			HuckelIO.PrintIfln("Rotation annulee");
			

		
		HuckelIO.PrintIfln("apres rotation");
		
		for (Atom atom : this.getAtoms()) {
			HuckelIO.PrintIfln(atom + " " + atom.getX() / 50 + " " + atom.getY()/50 + " "+ atom.getZ() / 50 );
		}	
		
		InitialOrientation savedInitialOrientation = new InitialOrientation(v, angle, this.getCenter());
		this.saveInitialOrientation(savedInitialOrientation);
		
		//HuckelIO.PrintIf ("Readxyz.flatGeom() initial orientation saved = " + savedInitialOrientation );
	}
	
	/**
	 * 
	 * @param i
	 * @return
	 */
	public Atom getAtomBySuperSymbol(int i ) {
		for (Atom a : this.getAtoms()) {
			if (a.getSuperSymbol() == i)
				return a;
		}
		
		return null;
	}


	/**
	 * Replace CH3 by ME
	 * Be careful! if synchronize(Molecule) has never been called, 
	 * C atom in CH3 is instance of Atom and not HuckelAtom
	 * @param lstAtoms2D
	 */
	protected void replaceCH3ByME(List<Atom> lstAtoms2D) {
		this.lstVirtualMethyls= new ArrayList<VirtualMethyl>();
		List <Atom> lstHToRemove = new ArrayList<Atom>();
	   // on parcourt tous les atoms
		for (int index = 0;  index < lstAtoms2D.size(); index++) {
			Atom a = lstAtoms2D.get(index);
			// si le carbone est un C : verifier les connexions 
			if (a.getElement() == PeriodicTable.Element.C) {
				HuckelIO.PrintIfln ("C trouve");
				List<Integer> lstConnect =  a.getConnect();
				List<Atom>lstAtomConnect =  new ArrayList<Atom>();
				List<Atom> lstH = new ArrayList<Atom>();
				Atom autreAtomeLieQueH = null;
				// contrrole les atomes liees
				for (int i = 0; i < lstConnect.size(); i++) {
					Atom atomLie = this.getAtomBySuperSymbol(lstConnect.get(i));
					if ( (atomLie != null) && (atomLie.getElement() == PeriodicTable.Element.H) )
						lstH.add(atomLie);
					else if (atomLie != null) 
						autreAtomeLieQueH = atomLie;
					
					lstAtomConnect.add(atomLie);
				}
				
				// si le carbonne a 4 connections dont 3 H c'est un methyl, on devra supprimer les H et on le remplace par un ME
				if (lstAtomConnect.size() == 4  && lstH.size()==3) {
					HuckelIO.PrintIfln("methyl detected");

					lstHToRemove.add(lstH.get(0));
					lstHToRemove.add(lstH.get(1));
					lstHToRemove.add(lstH.get(2));
					Atom me = PeriodicTable.getNewAtom(PeriodicTable.Entry.Me);
					try {
						if (a instanceof HuckelAtom)
							((HuckelAtom) me).setRadR( ((HuckelAtom)a).countRadR());
					} catch (HuckelAtomException e1) {
						e1.printStackTrace();
					}
					me.setLocation((Point3D) a.getLocation().clone()); 
					me.setLocationGeom3D(a.getLocationGeom3D()); 
					me.setSuperSymbol(a.getSuperSymbol());
					me.addConnect(autreAtomeLieQueH.getSuperSymbol());
					
					// on sauvegarde le C et les 3 H dans une liste d'objet methyl. cela permettra de les reconstruire quand on voudra reconstruire la geom3d a partir d'une molecule modifiee
					lstAtoms2D.set(index, me);
					try {
						this.lstVirtualMethyls.add(new VirtualMethyl(a, lstH.get(0), lstH.get(1),lstH.get(2), me));
					} catch (GeometryException e) {
						e.printStackTrace();
					}
				}
								
			} // atom = C
		}
		
		// supprimer les H des methyl
		lstAtoms2D.removeAll(lstHToRemove);
	}

	
	
	/**
	 * This function returns the bond corresponding to both atoms
	 * Be careful! if synchronize(Molecule) has never been called, the function will return null
	 * @param atom1 :
	 *            fist atom
	 * @param atom2 :
	 *            second atom
	 * @return Bond : the found bond or null if atoms are not linked
	 */

	public Bond getBond(Atom a1, Atom a2) {

		if ((a1 == null) || (a1 == null))
			return null;

		for (Bond l : this.lstBonds3D) {
			if (l.contains(a1, a2))
				return l;
		}

		return null;
	}

	/**
	 * Returns the bonds of 3d geometry
	 * @param molecule
	 * @return
	 */
	private List<Bond> getBonds3D(Molecule molecule) {
		List<Bond> lstBonds2D = molecule.getBonds();
		List <Bond> lstBonds3D = new ArrayList<Bond>();
		Atom me;
		VirtualMethyl virtualMethyl;
		
		// construit une liste de liaison a partir de la liste des liaisons de la molecule.
		// mais dans cette liste on remplacle  les liaison X-ME par X-CH3
		for (Bond b : lstBonds2D) {
			me = b.getAtom(PeriodicTable.Element.Me);
			
			if (me != null)
				try {
					virtualMethyl = this.getVirtualMethyl(me);
					Atom neighbour = b.getAtomLinkedWith(me);
					Atom c = virtualMethyl.getC();
					int bondType = 1;
					if (b instanceof HuckelBond)
						bondType= ((HuckelBond) b).getBondType();
					lstBonds3D.add(new VirtualBond (neighbour, c, me, bondType));
							
					lstBonds3D.addAll(virtualMethyl.getBonds());
				} catch (BondException e) {
					e.printStackTrace();
				}
			else
				lstBonds3D.add(b);
			
		}
		return lstBonds3D;
	}
	
	/**
	 * Return the list of bonds (Bond, HuckelBond, VirtualBond) if the geometry has been synchronized.
	 * a VirtualBond represents a Bond or a HuckelBond between X-CH3. CH3 is a VirtualMethyl object that subistute a ME ; so The bond beetween X-CH3 is considered as virtual 
	 * because in a molecule object there is a real Bond or HuckelBond between X-ME where ME is a "real" methyl in the molecule object
	 * with the molecule by calling of synchonize(molecule) before.
	 * else return an empty list
	 * @return
	 */
	public List<Bond> getBonds3D() {
		return this.lstBonds3D;
	}
	
	/**
	 * return null if the atom in parameter is not a ME or if its supersymbol == 0
	 * else returns the virtual methyl group corresponding to ME specified in parameter, or  null
	 * if it is not found.
	 * @param me
	 * @return
	 */
	private VirtualMethyl getVirtualMethyl(Atom me) {
		try {
			// si l'atom passe n'est pas un ME on renvoie une exception sans planter
			if (me.getElement() != PeriodicTable.Element.Me)
				throw new GeometryException("getSubsitutedMethyl : this atom is not a methyl ME");
				
			
			// si le supersymbol = 0 on renvoie une exception sans planter
			if (me.getSuperSymbol() == 0)
			throw new GeometryException("getSubsitutedMethyl : the supersymbol of ME in parameter == 0 : cannot get substituted methyl");
		
		}
		catch (GeometryException e) {
			e.printStackTrace();
			return null;
		}
		
		for (VirtualMethyl virtualMethyl : this.getMethyls()) {
			if (virtualMethyl.isSubsituteOf(me)) {
				return virtualMethyl;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	private List<VirtualMethyl> getMethyls() {
		return this.lstVirtualMethyls;
	}
	
	
	/**
	 * 
	 * @param me
	 * @return
	 */
	protected VirtualMethyl replaceMEByCH3(Atom me) {

		// il peut y avoir des methyls ayant ete sauves lors d'un Molecule.createMolecule(geom) : readXYZ 
		//  ceux-ci ont un supersymbol !=0
		// -> on les restaure avec le supersymbol d'origine
		//     
		// il peut y des nouveaux methyl (ME.supersymbol =0) 
		//-> il faut les creer : C a la place de ME  et des atomes H
		

		// si ce n'est pas un nouvel ME : supersymbol != 0, recuperer le groupe methyl correspondant
		VirtualMethyl virtualMethyl = this.getVirtualMethyl(me);
		if (virtualMethyl != null) {
			HuckelIO.PrintIfln("replaceMEByCH3: methyl restaure");
			virtualMethyl.autoUpdate();
			return virtualMethyl;
		}
		else if (me.getElement() == PeriodicTable.Element.Me){
			HuckelIO.PrintIfln("replaceMEByCH3 : nouveau methyl");
			// si c'est un nouveau methyl, creer le nouveau methyl
			List<Atom> methylGroup = new ArrayList<Atom>(4);
			Atom c = PeriodicTable.getNewAtom(PeriodicTable.Entry.C);
			c.setSuperSymbol(me.getSuperSymbol());
			methylGroup.add(c);
			for (int i = 0; i <3; i++) {
				Atom h = PeriodicTable.getNewAtom(PeriodicTable.Entry.H);
				methylGroup.add(h);
			}
			try {
				virtualMethyl =  new VirtualMethyl(methylGroup, me);
				virtualMethyl.autoUpdate();
				return virtualMethyl;
			} catch (GeometryException e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			HuckelIO.PrintIfln("Probleme replaceMEByCH3 : Le  methyl n'a pas pu etre restaure et il ne s'agit pas non plus d'un nouveau methyl : element = " + me.getElement() + " symbol =" + me.getSuperSymbol());
			return null;
		}
	}


	/**
	 * The geometry is adjusted to conform to user changes on the molecule
	 * 
	 * @param molecule
	 */
	public void synchronize(Molecule molecule) throws GeometryException {
		//if (molecule instanceof Structure)
	//		System.out.println("synchronize geom with " + ((Structure) molecule).getName());
	
			// si on a une premiere geometrie cree a partir d'un fichier (readxyz) puis
		// que la  molecule est recree a partir de la geometry par : molecule.createMolecule(geometry)
		// les atomes crees sont differents de ceux de la geometrie, de plus les atomes de la geometrie n'appartienne pas a une molecule
		// on doit donc faire resynchroniser la liste methyls sauvegardes pour qu'il pointent sur les ME de la molecule
		for (VirtualMethyl virtualMethyl : this.lstVirtualMethyls) {
			Atom me = molecule.getAtomBySuperSymbol(virtualMethyl.getSubstituteME().getSuperSymbol());
			if (me != null)
				virtualMethyl.setSubsituteOf(me);
		}
		
			
		//verifie s'il ya de nouveaux atomes, car si ce n'est pas le cas, on ne doit pas renumeroter les sypersymbols pour que le output soit dans l'ordre
		boolean isNewAtoms = false;
		if (molecule.countAtoms() != (this.getAtoms().size() - (this.lstVirtualMethyls.size() * 3) ))
			isNewAtoms = true;
		else
			for (Atom a : molecule.getAtoms()) {
				if (a.getSuperSymbol() == 0) {
					isNewAtoms = true;
				}	
			}
		
		// s'il n'y a pas de nouveaus atomes il faut juste synchronizer les CH3 (position des H, nb Rad.... )
		// etles atomes de la geom doivent pointer sur les memes atomes que ceux de la molecule puis QUITTER

		if (!isNewAtoms ) {
			HuckelIO.PrintIfln("there is not new atoms");
			// on fait pointer les atomes  de geom sur ceux de la molecule 
			for (Atom a: molecule.getAtoms()) {
				if (a.getElement() != PeriodicTable.Element.Me) {
					for (int i= 0 ; i < this.getAtoms().size() ; i++) {
						if (this.getAtoms().get(i).getSuperSymbol()==a.getSuperSymbol())
							this.geom.set(i, a);
					}
				}
			}
			
			for (VirtualMethyl virtualMethyl : this.lstVirtualMethyls)
				virtualMethyl.autoUpdate();
			
			// on rafraichit la liste des liaisons 3D tenant compte des methyls et de la structure 2D
			this.lstBonds3D =  this.getBonds3D(molecule);

			this.forceConnectivyFromBonds();
			
			// QUITTER
			return;
		}
		
		// S'IL Y A DE NOUVEAUX ATOMES : 
		HuckelIO.PrintIfln("there is new atoms : recreate geom");

		// vide la liste d'atomes
		this.geom.clear();
		this.nombre = 0;
		// il faut supprimer les connectivites
		this.clearConnectivity();

		ArrayList<VirtualMethyl> lstVirtualMethyls= new ArrayList<VirtualMethyl>();
		
		
		// on reconstruit la liste d'atomes de la geometrie 3D a partir des atomes de de la molecule 
		// A ce niveau les supersymbols  des atomes peuvent etre egaux a 0 : cas des nouveaux atomes
		// Les supersymbols peuvent aussi ne plus se suivre : cas d'atomes supprimes dans la molecule
		// le plus simple est de renumeroter les supersymbols
		// cas des methyls : si un atom est un methyl il faut le remplacer par un CH3  dans la liste  Geom et creer les connectivites C-H
		for (Atom a : molecule.getAtoms()){							
		
			if (a == null) {
				HuckelIO.error(this.getClass().getName(), "set",
						"Atom is null in Geometry.set(molecule)");
				throw new GeometryException("error geometry.synchronize(molecule) : atom is null");
			}
			
			// si l'atom en cours n'est pas un methyl
			if (a.getElement() != PeriodicTable.Element.Me){
				this.nombre++;	
				a.setSuperSymbol(this.nombre);
				this.geom.add(a);
			}
			else {
				// si l'atom en cours est un methyl, on restaure le CH3 ou on en cree un nouveau
				VirtualMethyl virtualMethyl = this.replaceMEByCH3(a);
				if (virtualMethyl != null) {
					this.nombre++;	
					// l'atome ME prend le meme symbol que le C de CH3 ce qui permettra de les comparer
					a.setSuperSymbol(this.nombre); 
					virtualMethyl.getC().setSuperSymbol(this.nombre);
					this.nombre++;	
					virtualMethyl.getH1().setSuperSymbol(this.nombre);
					this.nombre++;	
					virtualMethyl.getH2().setSuperSymbol(this.nombre);
					this.nombre++;	
					virtualMethyl.getH3().setSuperSymbol(this.nombre);
					this.geom.addAll(virtualMethyl.getCH3());
					lstVirtualMethyls.add(virtualMethyl);
				}
				else
					throw new GeometryException("geometry.synchronize(molecule) : replaceMEByCH3 has returned null !");
					
			}
		}	
		
		// on rafraichit la liste des methyls avec les nouveaux methyl recuperes de la structure 2D
		this.lstVirtualMethyls = lstVirtualMethyls;
		
		
		
		//on cree les connectivites par rapport aux liaisons
		for (Bond b : molecule.getBonds()) {
			b.getAtom1().addConnect(b.getAtom2().getSuperSymbol());
			b.getAtom2().addConnect(b.getAtom1().getSuperSymbol());
		}
		
		// on rafraichit la liste des liaisons 3D tenant compte des methyls et de la structure 2D
		this.lstBonds3D =  this.getBonds3D(molecule);
		this.nbAtomSav = molecule.countAtoms();
		
	}

}