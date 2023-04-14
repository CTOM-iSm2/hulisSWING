package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.CoupleLocal;
import org.ctom.hulis.huckel.Energy;
import org.ctom.hulis.huckel.IHuckelObject;
import org.ctom.hulis.huckel.IMoleculeComponent;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.OrbitaleAtomique;
import org.ctom.hulis.huckel.OrbitaleMoleculaire;
import org.ctom.hulis.huckel.SpinOrbitaleMoleculaire;
import org.ctom.hulis.huckel.events.BlocDelocalizedEvent;
import org.ctom.hulis.huckel.events.GeometryEvent;
import org.ctom.hulis.huckel.events.HuckelAtomEvent;
import org.ctom.hulis.huckel.events.HuckelBondEvent;
import org.ctom.hulis.huckel.events.MoleculeEvent;
import org.ctom.hulis.huckel.events.MonoExcitationEvent;
import org.ctom.hulis.huckel.events.StructureDelocalizedEvent;
import org.ctom.hulis.huckel.events.StructureEvent;
import org.ctom.hulis.huckel.events.StructureLocalizedEvent;
import org.ctom.hulis.huckel.exception.NoSOMAvailableException;
import org.ctom.hulis.huckel.exception.RedundanceException;
import org.ctom.hulis.huckel.exception.UnavailableSOMException;
import org.ctom.hulis.huckel.listeners.IMoleculeGeometryListener;
import org.ctom.hulis.huckel.listeners.IStructureDelocalizedListener;
import org.ctom.hulis.huckel.listeners.IStructureListener;
import org.ctom.hulis.huckel.listeners.IStructureLocalizedListener;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.huckel.structures.symmetry.Symmetry;
import org.ctom.hulis.prefs.Preferences;
import org.ctom.util.maths.Point3D;

import controller.globalCommands.IGlobalCommands;
import controller.structureViewController.GhostMolecule;
import controller.structureViewController.StructureViewController;
import util.ComponentsTools;
import util.MyJOptionPane;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;

/**
 * Atom : This class describes a org.ctom.view for a structure object
 * 
 * @see StructureDrawer
 * @version 1.1
 * @since
 * @author Nicolas GOUDARD
 */

@SuppressWarnings("serial")
public class StructureView extends JPanel implements IStructureLocalizedListener, IStructureDelocalizedListener, IStructureListener, IMoleculeGeometryListener {
	public static boolean WARNING_NEGATIVE_CHARGE = false; 
	public static boolean DEBUG = true; // HuckelIO.isverbose();
	private static final Color COLOR_ETOT = Color.BLACK;

	private static final Color COLOR_ERROR_INFOS = Color.RED;
	private static final Color COLOR_BLOC_NB_ELECTRONS = Color.BLUE;
	public static final Color COUPLE_COLOR = Color.GREEN;
	public static final Color  SELECTED_BLOC_DELOCALIZED_COLOR =new Color (197,162 /*150-160*/,3); //or marron 
	public static final float  SELECTED_BLOC_DELOCALIZED_STROKE =2.0f;
	public static final Color  OVERFLIGHT_BLOC_DELOCALIZED_COLOR =  new Color (226,181,0); //gold;
	public static final float COUPLE_LINE_STROKE = 3.0f;
	public final static DecimalFormat FORM = new DecimalFormat("#.#");
	public static final int MODULO_TEXT_ZOOM = 5;
	public static final String FONT_TEXT_ENERGY_FAMILY = "Helvetica";
	public static final int FONT_TEXT_ENERGY_STYLE = Font.PLAIN;
	public static final int FONT_TEXT_ENERGY_DEFAULT_SIZE =  13;
	public static final Font FONT_TEXT_ENERGY = new Font(FONT_TEXT_ENERGY_FAMILY, FONT_TEXT_ENERGY_STYLE, FONT_TEXT_ENERGY_DEFAULT_SIZE);
	public static final String FONT_TEXT_INFORMATIONS_FAMILY = "Helvetica";
	public static final int FONT_TEXT_INFORMATIONS_STYLE = Font.PLAIN;
	public static final int FONT_TEXT_INFORMATIONS_DEFAULT_SIZE =  14;
	public static final Font FONT_TEXT_INFORMATIONS =   new Font(FONT_TEXT_INFORMATIONS_FAMILY, FONT_TEXT_INFORMATIONS_STYLE, FONT_TEXT_INFORMATIONS_DEFAULT_SIZE);
	public static final String FONT_FAMILY_BLOC_NB_ELECTRONS =  "Helvetica";
	public static final int FONT_STYLE_BLOC_NB_ELECTRONS = Font.BOLD;
	public static final int FONT_SIZE_BLOC_NB_ELECTRONS =14;
	public static int SPACE_BETWEEN_MOLECULE_AND_TEXT = 10; // space between molecule graphic and text under
	private static final int X_TEXT_MARGIN = 10; // text margin under the molecule in the panel
	private static  BlocDelocalized selectedBlocDelocalized;
	public static void setSelectedBlocDelocalized(BlocDelocalized blocDelocalized) {
		selectedBlocDelocalized = blocDelocalized;
	}

	public static BlocDelocalized getSelectedBlocDelocalized() {
		return selectedBlocDelocalized;
	}

	protected MyResourceBundle bundle;

	StructureViewController controller;

	private GraphOM graphOM;

	private OrbitaleMoleculaire orbitalMoleculaireShown;
	
	private Structure structure;

	private MesomeryView mesomeryView;

	private Selection selection;
	private BlocDelocalized overflightBlocDelocalized;

	private Symmetry symmetry;

	private GhostMolecule ghost;

	private ArrayList<Integer> atomsToHighlight;

	/**
	 * StructureView : constructor
	 * 
	 * @param :
	 *            structure
	 * @param :
	 *            controller
	 * @throws Exception
	 *             if a parametter is null
	 */

	public StructureView(Structure structure, MesomeryView mesomeryView)
			throws Exception {
		if (structure == null)
			throw new Exception("Structure must be initialized");
		if (structure.getMesomeryParent() != mesomeryView.getMesomery())
			throw new Exception(
					"Structure does not belong to mesomery parent viewer");

		this.structure = structure;
		this.mesomeryView = mesomeryView;
	
		this.controller = new StructureViewController(this, structure);
		addMouseListener(this.controller);
		addMouseMotionListener(this.controller);
		this.structure.addListener((IStructureListener) this);
		this.graphOM = new GraphOM(structure, this);
		this.selection = null;
		this.symmetry = null;

	}

	/**
	 * return the  coordinates  (x', y') of the image point that is displayed from the real point (x, y) :
	 * x' = (x + xmargin) * zoom
	 * y' = (y + ymargin) * zoom
	 * @param p
	 * @return
	 */
	public Point2D getImagePoint(Point2D p){
		return new Point2D.Double( (this.getXMargin() + p.getX()) * this.getZoom(), (this.getYMargin() + p.getY()) * this.getZoom());
	}

	/**
	 * return the coordinates  (x, y) of the point that is displayed from the image point of coordinates (x', y') :
	 * x = x / zoom - xmargin
	 * y = y / zoom - ymargin
	 * @param p
	 * @return
	 */
	public Point2D getAntecedentPoint(Point2D p){
		return new Point2D.Double(  p.getX() / this.getZoom() - this.getXMargin(), p.getY() / this.getZoom()- this.getYMargin() );

	}

	/**
	 * @param p2d
	 * @return p3d
	 */
	public Point3D getPoint3D(Point2D p2d){
		return new Point3D(p2d.getX(), p2d.getY(), 0.0);

	}


	/**
	 * @param p2d
	 * @return p3d
	 */
	public Point2D getPoint2D(Point3D p3d){
		return new Point2D.Double(p3d.getX(), p3d.getY());

	}


	/**
	 * 
	 * @return
	 */
	public double getXMargin() {
		return this.mesomeryView.getXMargin();
	}

	/**
	 * 
	 * @return
	 */
	public double getYMargin() {
		return this.mesomeryView.getYMargin();
	}



	/**
	 * 
	 * @param selection
	 */



	public void setSelection (Selection selection){
		this.selection = selection;
	}

	public Selection getSelection (){
		return this.selection;
	}

	public Point getMousePointerLocationOrRelativeCoordinatesOnScreeen(Point3D location) {
		Point pos = null;
		try {

			// les vraies coordonnees du pointeur de la souris : renvoie une exception
			// si utilisation en applet non signee
			pos = (Point) AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {
					return MouseInfo.getPointerInfo().getLocation();

				}
			});

		} catch (Exception e) {
			SwingIO.PrintIf(e); // getPointerInfo () ne fonctionne pas dans
			// Applet non signee
			pos = new Point((int) (ComponentsTools.getLocationOnScreen(this)
					.getX() + location.getX()
					* this.getZoom()), (int) (ComponentsTools.getLocationOnScreen(this).getY() + location.getY()
							* this.getZoom()));
		}
		return pos;
	}

	public boolean isDisplayPolynomes() {
		return this.mesomeryView.isDisplayPolynomes();
	}

	public boolean isDisplayCharges() {
		return this.mesomeryView.isDisplayCharges();
	}

	public boolean isDisplayHxHxy() {
		return this.mesomeryView.isDisplayHxHxy();
	}

	public boolean isDisplayNum() {
		return this.mesomeryView.isDisplayNum();
	}

	public void drawMolecule(Graphics2D g2d) {

		// ********** orbitales du fond


		if (!Preferences.isDISPLAY_DENSITY()) {
			if (this.orbitalMoleculaireShown != null) {
				drawOrbitalUnder(g2d);
			}
		} else {
			if (this.structure instanceof Structure) {
				drawDensityUnder(g2d);
			}
		}

		drawSkeleton(g2d);

		// ********** orbitale de s devant

		if (!Preferences.isDISPLAY_DENSITY()) {
			if (this.orbitalMoleculaireShown != null) {
				drawOrbitalOver(g2d);
			}
		} else {
			if (this.structure instanceof Structure) {
				drawDensityOver(g2d);
			}
		}


		//**** ecrit les infos sous les liaisons et les orbitales

		for (Atom a : this.structure.getAtoms()) {
			new AtomDrawer(this, a).writeInfos(g2d);
		}

		for (Bond b : this.structure.getBonds()) {
			new BondDrawer(this, b).writeInfos(g2d);
		}

		if (isDisplayNum() ) {


			if (this.orbitalMoleculaireShown != null) {
				int size=this.orbitalMoleculaireShown.getSize();

				for (int i=0; i<size; i++) {
					double coeff=this.orbitalMoleculaireShown.getCoeff(i);
					OrbitaleAtomique orb = this.structure.getOAs()[i];

					new OrbitalDrawer(this, orb, coeff).writeInfos(g2d);
				}
			}
		}


	}


	private void drawOrbitalUnder(Graphics2D g2d) {
		drawOrbital(g2d, 1);
	}

	private void drawOrbitalOver(Graphics2D g2d) {
		drawOrbital(g2d, 2);
	}

	private void drawDensityUnder(Graphics2D g2d) {
		drawDensity(g2d, 1);
	}

	private void drawDensityOver(Graphics2D g2d) {
		drawDensity(g2d, 2);
	}

	private void drawOrbital(Graphics2D g2d, int jval) {
		int size=this.orbitalMoleculaireShown.getSize();
		for (int i=0; i<size; i++) {
			double coeff=this.orbitalMoleculaireShown.getCoeff(i);
			OrbitaleAtomique orb = this.structure.getOAs()[i];
			new OrbitalDrawer(this, orb, coeff).draw(jval, g2d);
		}
	}

	private void drawDensity(Graphics2D g2d, int jval) {
		OrbitaleAtomique[] oAs = this.structure.getOAs();
		int nOAs=oAs.length;
		SpinOrbitaleMoleculaire[] occSOM=null;
		try {
			occSOM = this.structure.getOccSOM();
		} catch (NoSOMAvailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnavailableSOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int nOccOM = occSOM.length;
		for (int iOA=0; iOA<nOAs; iOA++) {
			OrbitaleAtomique orb = oAs[iOA];
			double coeff=0;
			for (int iOM=0; iOM<nOccOM; iOM++) {
				coeff+=Math.pow(occSOM[iOM].getCoeff(iOA),2);
			}
			new OrbitalDrawer(this, orb, coeff).draw(jval, g2d);
		}
	}

	private void drawSkeleton(Graphics2D g2d) {
		//***** dessine les liaisons entre atomes
		for (Bond b : this.structure.getBonds()) {
			new BondDrawer(this, b).draw(g2d);
		}

		//****  dessine les atomes par dessus les liaisons
		for (Atom a : this.structure.getAtoms()) {
			new AtomDrawer(this, a).draw(g2d);
		}
		
	}

	private void drawGhost(Graphics2D g2d) {
		if (this.ghost!=null) {
			AffineTransform at_reset = g2d.getTransform();
			AffineTransform trans = new AffineTransform();
			trans.translate(ghost.getOffset().getX(),ghost.getOffset().getY());
			g2d.transform(trans);
			for (Atom a : this.ghost.getAtoms()) {
				new GhostAtomDrawer(this, a).draw(g2d);
			}
			for (Bond b : this.ghost.getBonds()) {
				new GhostBondDrawer(this, b).draw(g2d);
			}
			g2d.setTransform(at_reset);
		}
	}

	public void erase() {
		addMouseListener(this.controller);
		addMouseMotionListener(this.controller);
		this.structure.removeListener((IStructureListener) this);
		this.graphOM.erase();
		this.graphOM = null;

	}

	/**
	 * this function returns the atom at the specified coordinates in the
	 * molecule
	 * 
	 * @param p :
	 *            coordinates in Point2D
	 * @return Atom
	 */

	public Atom getAtom(Point2D p) {

		for (Atom a : this.structure.getAtoms()) {
			if ((new AtomDrawer(this, a)).isContained(p))
				return a;
		}

		return null;

	}

	/**
	 * this function returns the bond at the specified coordinates in the
	 * molecule
	 * 
	 * @param p :
	 *            coordinates in Point2D
	 * @return Bond
	 */
	public Bond getBond(Point2D p) {

		for (Bond b : this.structure.getBonds()) {
			if (new BondDrawer(this, b).isContained(p))
				return b;
		}

		return null;
	}

	/**
	 * 
	 * @return
	 */
	public GraphOM getGraphOM() {

		return this.graphOM;
	}

	/**
	 * 
	 * @return
	 */
	public MesomeryView getMesomeryView() {
		return this.mesomeryView;
	}

	/**
	 * this function returns the molecule component at the specified coordinates
	 * 
	 * @param p :
	 *            coordinates in Point2D
	 * @return IMoleculeComponent
	 */

	public IMoleculeComponent getMoleculeComponent(Point2D p) {

		Atom a = getAtom(p);

		if (a != null)
			return a;

		Bond l = getBond(p);

		if (l != null)
			return l;

		return null;

	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	public OrbitaleAtomique getOrbital(Point2D p) {
		if (getShownOrbital() == null)
			return null;


		int size=this.orbitalMoleculaireShown.getSize();

		for (int i=0; i<size; i++) {
			double coeff=this.orbitalMoleculaireShown.getCoeff(i);
			OrbitaleAtomique orb = this.structure.getOAs()[i];

			if ((new OrbitalDrawer(this, orb, coeff)).isContained(p)) 
				return orb;
		}

		// si aucune orbitale n'est renvoyee a ce niveau peut etre que sont rayon est plus petit que celui de l'atome, donc 
		// tester les atomes correspondants
		for (int i=0; i<size; i++) {
			OrbitaleAtomique orb = this.structure.getOAs()[i];
			if ((new AtomDrawer(this, orb.getHuckelAtom())).isContained(p))
				return orb;
		}

		return null;

	}

	/**
	 * 
	 * @return
	 */
	public OrbitaleMoleculaire getShownOrbital() {

		return this.orbitalMoleculaireShown;
	}

	/**
	 * This function returns the structure showed in the panel
	 * 
	 * @return
	 */

	public Structure getStructure() {
		return this.structure;
	}

	/**
	 * 
	 * @return
	 */
	public double getZoom() {
		return this.mesomeryView.zoom;
	}


	/**
	 * 
	 * @return
	 */
	public double getAngle() {
		return this.mesomeryView.getAngle();
	}

	/**
	 * this function tests if the specified point is contained by an atom
	 * 
	 * @param p :
	 *            point to be tested
	 * @return boolean
	 */

	public boolean isAtom(Point2D p) {

		return (getAtom(p) != null);
	}

	/**
	 * this function tests if the specified point is contained by a bond
	 * 
	 * @param p :
	 *            point to be tested
	 * @return boolean
	 */
	public boolean isBond(Point2D p) {
		return (getBond(p) != null);
	}

	/**
	 * 
	 */
	public void moleculeDeleted(MoleculeEvent e) {
		repaint();		
	}

	/**
	 * 
	 * @param g2d
	 */
	protected void drawCouple(Graphics2D g2d) {
		if (this.structure instanceof StructureLocalized) {
			CoupleLocal couple = ((StructureLocalized) this.structure).getCouple();
			if (couple != null) {
				// SwingIO.PrintIf("DRAW COUPLE");
				// SwingIO.PrintIf(couple.getAtom1().toString());
				// SwingIO.PrintIf(couple.getAtom2().toString());
				// SwingIO.PrintIf("DRAW COUPLE");
				g2d.setColor(StructureView.COUPLE_COLOR);
				g2d.setStroke(new BasicStroke((float) getZoom()
						* StructureView.COUPLE_LINE_STROKE));
				AtomColor atomColor1 = null;
				try{
					atomColor1 =AtomColor.valueOf(couple.getHuckelAtom1().getSigle());
				}
				catch (IllegalArgumentException e) {
					atomColor1 = AtomColor.Xx;
				}
				AtomColor atomColor2 = null;
				try{
					atomColor2 =AtomColor.valueOf(couple.getHuckelAtom2().getSigle());
				}
				catch (IllegalArgumentException e) {
					atomColor2 = AtomColor.Xx;
				}
				g2d.draw(new Line2D.Double(
						this.getImagePoint(new Point2D.Double(couple.getHuckelAtom1().getX() + AtomDrawer.ProjXradR
								* atomColor1.getRadius(),
								couple.getHuckelAtom1().getY() + AtomDrawer.ProjYradR
								* atomColor1
								.getRadius())), 

								this.getImagePoint(new Point2D.Double(couple.getHuckelAtom2().getX() + AtomDrawer.ProjXradR
										* atomColor2
										.getRadius(),
										couple.getHuckelAtom2().getY() + AtomDrawer.ProjYradR
										* atomColor2
										.getRadius()))));
				// g2d.draw(new Line2D.Double((couple.getAtom1().getX() ) *
				// getZoom(), (couple.getAtom1().getY() )* getZoom(),
				// (couple.getAtom2().getX() ) * getZoom(),
				// (couple.getAtom2().getY() )* getZoom()));

			}
		}
	}

	/**
	 * paint : this method displays the structure
	 * 
	 * @param g :
	 *            graphics
	 */

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		this.bundle = LanguageManager.getInstance().getResource("StructureView");

		if (selection != null)
			selection.draw (g2d);

		if (this.structure.countAtoms() == 0)
			return;

		/*	AffineTransform transform= new AffineTransform();
		transform.translate( (this.getXMargin() + this.structure.getWidth()/2 * this.getZoom()), (this.getYMargin() + this.structure.getHeight()/2) * this.getZoom() );
		transform.rotate(this.getAngle());
		g2d.setTransform(transform);*/

		drawCouple (g2d);

		drawMolecule(g2d);


		writeBlocElectronsNumber(g2d);
		writeInfos(g2d);	
	}


	/**
	 * 
	 * @param g2d
	 */
	protected void writeBlocElectronsNumber(Graphics2D g2d) {
		if (structure instanceof StructureDelocalized)
			return;
		g2d.setFont(getFontBlocElectronsNumber());

		for (BlocDelocalized blocDelocalized : ((StructureLocalized) structure).getListBlocDelocalized()) {
			if (blocDelocalized.countHuckelObjects() == 0) continue;

			Point2D barycentre = getBlocBarycenter(blocDelocalized);
			double xBary = barycentre.getX();
			double yBary = barycentre.getY();
			if (blocDelocalized == StructureView.getSelectedBlocDelocalized())
				g2d.setColor(SELECTED_BLOC_DELOCALIZED_COLOR);
			else if (blocDelocalized == this.getOverflightBlocDelocalized())
				g2d.setColor(OVERFLIGHT_BLOC_DELOCALIZED_COLOR);
			else
				g2d.setColor(COLOR_BLOC_NB_ELECTRONS);

			g2d.drawString(getFormattedStringBlocElectronsNumber(blocDelocalized), (int) xBary, (int) yBary );

		}


	}

	/****
	 * Retourne le bloc au point p si le point p est un composant du bloc ou le nombre d'electron ecrit au barycentre 
	 *
	 * @param p
	 * @return
	 */
	public BlocDelocalized getBlocDelocalized(Point2D p){
		if (structure instanceof StructureDelocalized)
			return null;
		
		IMoleculeComponent moleculeComponent  = this.getMoleculeComponent(p);
		// un bloc est survole si on passe sur un composant du bloc ou le nombre d'electron au barycentre 
		if (moleculeComponent instanceof IHuckelObject) {
			IHuckelObject huckelObject = (IHuckelObject) moleculeComponent;
			BlocDelocalized bloc =  huckelObject.getBlocDelocalizedParent();
			return bloc;
		}
		else
		{
			Structure structure = this.getStructure();
			// probleme FontDesignMetrics incompatible java 1.5 
			//FontMetrics fontMetrics = FontDesignMetrics.getMetrics(this.getFontBlocElectronsNumber());
			int height = 0;// fontMetrics.getHeight();
			int width;
			Point2D barycentre;
			for (BlocDelocalized bloc : ((StructureLocalized) structure).getListBlocDelocalized()){
				width = 0; //fontMetrics.stringWidth(this.getFormattedStringBlocElectronsNumber(bloc));
				barycentre = this.getBlocBarycenter(bloc);

				if ( (p.getX() >= barycentre.getX()) && 
						(p.getX() <= ( barycentre.getX() + width))
						&& (
								( p.getY()  >=  (barycentre.getY() - height) ) 
								&& (p.getY() <= barycentre.getY() ))  )
					return bloc;
			} // for
		} // if

		return null;

	}

	/**
	 * 
	 * @param blocDelocalized
	 * @return
	 */
	public String getFormattedStringBlocElectronsNumber (BlocDelocalized blocDelocalized) {
		return "[" + blocDelocalized.getNbElectron() + "]";
	}

	/**
	 * 
	 * @return
	 */
	public Font getFontBlocElectronsNumber () {
		return new Font(FONT_FAMILY_BLOC_NB_ELECTRONS, FONT_STYLE_BLOC_NB_ELECTRONS, (int) (FONT_SIZE_BLOC_NB_ELECTRONS * (this.getZoom() % StructureView.MODULO_TEXT_ZOOM)));
	}


	/**
	 * 	Gets the barycenter of a block
	 * 
	 */
	public Point2D getBlocBarycenter(BlocDelocalized bloc) {
		Point2D pTemp;
		double xtemp = 0.0;;
		double ytemp = 0.0;
		double pond = 0.0;
		double radius = 0.0;
		double zoom = this.getZoom();
		for (IHuckelObject huckelObject : bloc.getListHuckelObject() ) {
			if(huckelObject instanceof Atom) {
				AtomColor atomColor = null;
				try{
					atomColor =AtomColor.valueOf(((Atom) huckelObject).getSigle());
				}
				catch (IllegalArgumentException e) {
					atomColor = AtomColor.Xx;
				}
				radius = atomColor.getRadius() * zoom;
			}
			else
				radius = this.getZoom(); // c'est a dire : 1.0 * zoom;
			pond += radius;
			pTemp = this.getImagePoint(this.getPoint2D(huckelObject.getLocation()));
			xtemp += pTemp.getX() * radius;
			ytemp += pTemp.getY() * radius;

		}
		double xBary = xtemp / pond;
		double yBary = ytemp / pond;

		return new Point2D.Double (xBary, yBary);

	}

	/**
	 * Displays informations of the structure on the screen
	 * @param g2d
	 */
	protected void writeInfos(Graphics2D g2d) {
		Font fontText = (Font) bundle.getObject("font");
		String mess=""; 

		g2d.setColor(StructureView.COLOR_ETOT);
		g2d.setFont(FONT_TEXT_ENERGY);
		g2d.setStroke(new BasicStroke(1.0f));
		int xResult;
		int yResult;
		int incr = 0;
		int verticalSpaceBetweenLines = (Integer) bundle.getObject("kverticalspacebetweenlines");

		xResult = X_TEXT_MARGIN; //(int) (this.structure.getXMin() * getZoom() - getZoom()
		//	* AtomDrawer.DEFAULT_ATOM_RADIUS);
		yResult = (int) ( (this.getYMargin() + this.structure.getYMax()) * getZoom() + getZoom()
				* AtomDrawer.DEFAULT_ATOM_RADIUS +  SPACE_BETWEEN_MOLECULE_AND_TEXT);

		// ecrire la valeur de l'orbitale selectionnee dans le graphe d'energie : epsiloni = aAlpha + bBeta 
		if (this.getShownOrbital() != null ) {
			int y = yResult + (++incr) ;
			Font currentFont = g2d.getFont();
			FontMetrics metrics = this.getFontMetrics(currentFont);		
			int indexOrb=0, index=0, indexPartner=0;
			if ( this.getShownOrbital() instanceof SpinOrbitaleMoleculaire) {
				indexOrb=((SpinOrbitaleMoleculaire)this.getShownOrbital()).getIndexTableOrbitale();
				index=((SpinOrbitaleMoleculaire)this.getShownOrbital()).getIndex();
				indexPartner=((SpinOrbitaleMoleculaire)this.getShownOrbital()).getPartner().getIndex();
			}
			if (DEBUG)
				g2d.drawString("(" + index + ", " + indexPartner + ") " + indexOrb  + " - " + Energy.EPSILON_SYMBOL , xResult, y );
			else
				g2d.drawString(Energy.EPSILON_SYMBOL, xResult, y );
			Font f = new Font(currentFont.getFamily(), currentFont.getStyle(), currentFont.getSize()-3);
			g2d.setFont(f);
			//	FontMetrics metrics2 = this.getFontMetrics(g2d.getFont());		
			int l1=metrics.stringWidth(Energy.EPSILON_SYMBOL); 
			if (DEBUG)
				l1= metrics.stringWidth("(" + index + ", " + indexPartner + ") " + indexOrb  + " - " + Energy.EPSILON_SYMBOL);
		
			//	String sId=Integer.toString(this.getShownOrbital().getIndex());

			/*g2d.drawString(sId, xResult + l1, y + 5 );*/ // PB : Index incorrect depuis spinOM

			//g2d.setFont(currentFont);
			g2d.drawString(" = "+ this.getShownOrbital().getEnergy(), xResult + l1  /*+ metrics2.stringWidth(sId)*/, y);
		} // fin ecrire valeur de l'orbitale selectionnee dans le graphe d'energie

		// ecrire : terminer votre bloc
		if (this.mesomeryView.getFrameApp().getCurrentEnabledCommand() == IGlobalCommands.BLOC){
			g2d.setColor(StructureView.COLOR_ERROR_INFOS);
			g2d.setFont(fontText);

			String messTerminateBloc;
			StructureView tmp = this.getMesomeryView().getFirstStructureWithSelectedBloc();
			if (tmp != null) {
				Object args[] = {MesomeryView.PSI + tmp.getStructure().getName()};
				messTerminateBloc = MessageFormat.format(bundle.getString("kendbloc"), args);
			}
			else
				messTerminateBloc =bundle.getString("kselectbloc");

			g2d
			.drawString(
					messTerminateBloc,
					xResult,
					yResult
					+ (++incr)	* verticalSpaceBetweenLines);

			incr++;

		}

		//g2d.drawString(this.structure.getIndex() + " -- " + this.structure.getIndexSelection() + " -- " + this.structure.getOverlapHLP(), xResult, yResult
		//		+ (++incr)	* verticalSpaceBetweenLines);

		// display errors or informations
		g2d.setFont(FONT_TEXT_INFORMATIONS);

		if (isDisplayPolynomes() && ((this.structure instanceof StructureDelocalized) || (((StructureLocalized)this.structure).getError()==StructureLocalized.Error.NOT_ERROR))) {
			g2d.drawString(this.structure.getCharacteristicPolynomial().toString(), xResult, this.getHeight() - verticalSpaceBetweenLines);
		}

		Mesomery mesomery = this.structure.getMesomeryParent();
		// les erreurs dans les structures sont calculees dans la classe Mesomery : ne pas les afficher si les calculs
		// sont desactives
		if (this.structure instanceof StructureLocalized && mesomery != null && mesomery.isEnabledFlyCalculate()) {
			StructureLocalized localized = (StructureLocalized) this.structure;
			g2d.setColor(StructureView.COLOR_ERROR_INFOS);
			g2d.setFont(fontText);

			switch (localized.getError()) {
			case NOT_ERROR:
				break;
			case ERROR_INCORRECT_NB_PI_ELEC:
				int nbElecPiDelocalized = this.structure.getMesomeryParent()
				.getDelocalizedStructure().getSumOfPiElec();
				Object args[] = { new Integer(Math.abs(nbElecPiDelocalized
						- this.structure.getSumOfPiElec())) };
				if (nbElecPiDelocalized > this.structure.getSumOfPiElec())
					mess = "kelecerror1";
				else
					mess = "kelecerror2";
				g2d
				.drawString(
						this.bundle.getString("kelecerror"),
						xResult,
						yResult
						+ (++incr)
						* verticalSpaceBetweenLines);
				g2d
				.drawString(
						this.bundle.getString("kelecerrorb"),
						xResult,
						yResult
						+ (++incr)
						* verticalSpaceBetweenLines);
				g2d.drawString(MessageFormat.format(
						this.bundle.getString(mess), args), xResult, yResult
						+ (++incr) * verticalSpaceBetweenLines);
				break;
			case ERROR_TOO_MUCH_SINGLE_ELECTRONS:
				mess = bundle.getString("tooMuchSingleElec") ;
				g2d
				.drawString(
						this.bundle.getString("kelecerror"),
						xResult,
						yResult
						+ (++incr)
						* verticalSpaceBetweenLines);
				g2d
				.drawString(
						this.bundle.getString("kelecerrorb"),
						xResult,
						yResult
						+ (++incr)
						* verticalSpaceBetweenLines);
				g2d.drawString(mess, xResult, yResult
						+(++incr) * verticalSpaceBetweenLines);
				break;

			case ERROR_ALREADY_EXIST:

				g2d
				.drawString(
						this.bundle.getString("kelecerror4"),
						xResult,
						yResult
						+ (++incr)	* verticalSpaceBetweenLines);

				g2d
				.drawString(
						this.bundle.getString("kelecerror4b")
						+ MesomeryView.PSI
						+ localized.getIdenticalStruct()
						.getName(),
						xResult,
						yResult
						+ (++incr) * verticalSpaceBetweenLines);
				break;



			default : break;
			} // switch case

			// erreurs de calcul de methode
			for (IMethod method : this.getMesomeryView().getMesomery().getMethods()) {
				if (method.getError() != null) {
					if (method.getError() instanceof RedundanceException)
						g2d
						.drawString(
								this.bundle.getString("kErrorRedundance"),
								xResult,
								yResult
								+ (++incr)	* verticalSpaceBetweenLines);
					else {
						Object args[] = { method.getName(), method.getError().getMessage()  };
						g2d
						.drawString(
								MessageFormat.format(bundle.getString("methoderror"), args),
								xResult,
								yResult
								+ (++incr)	* verticalSpaceBetweenLines);	
					}
				}
			} // for method


		} // if structurelocalized





	} // writeinfos 





	public void setShownOrbital(OrbitaleMoleculaire orbShown) {
		this.orbitalMoleculaireShown = orbShown;
	}

	
	public StructureViewController getController() {
		return controller;
	}

	public void setController(StructureViewController controller) {
		this.controller = controller;
	}

	public void setGhost(GhostMolecule ghostMolecule) {
		this.ghost=ghostMolecule;
	}
	
	
	public void setAtomsToHighlight(ArrayList<Integer> list) {
		for (int i : list) {
			System.out.println(i);
		}
		this.atomsToHighlight=list;
	}

	public ArrayList<Integer> getAtomsToHighlight() {
		return this.atomsToHighlight;
	}


	//@Override
	public void structureLocalizedWeightChanged(StructureLocalizedEvent event) {
		repaint();		
	}

	//@Override
	public void structureLocalizedCoupleChanged(StructureLocalizedEvent event) {
		// mise en commentaire : car le rafraichissement est pris en charge par mesomeryview car mesomery envoie l'evenement avant molecule
		// en effet mesomery ecoute molecule avant structureview donc huckelAtomRadRChanged est renvoye a mesomeryview avant le lancement des laculs des poids
		// molecule.huckelAtomRadRChanged est renvoye  apres le calcul. donc si les calculs sont longs, le retour d'information n'est pa instantane
		//repaint()	;		
	}

	//@Override
	public void structureNameChanged(StructureEvent event) {
		repaint();		

	}

	//@Override
	public void moleculeAtomAdded(MoleculeEvent e) {
		createSymmetry();
		repaint();		
	}

	//@Override
	public void moleculeAtomRemoved(MoleculeEvent e) {
		createSymmetry();
		repaint();		
	}

	//@Override
	public void moleculeAtomReplaced(MoleculeEvent e) {
		createSymmetry();
		repaint();		
	}

	//@Override
	public void moleculeBondAdded(MoleculeEvent e) {
		createSymmetry();
		repaint();		
	}

	//@Override
	public void moleculeBondRemoved(MoleculeEvent e) {
		createSymmetry();
		repaint();		
	}


	//@Override
	public void moleculeChargeChanged(MoleculeEvent event) {
		if ( (WARNING_NEGATIVE_CHARGE) && ((Integer) event.getOldValue() <= 0)
				&& ((Integer) event.getNewValue() > 0))
			MyJOptionPane
			.showMessageDialog(this, this.bundle
					.getString("kWarNegCharge"), this.bundle
					.getString("kwarning"),
					JOptionPane.WARNING_MESSAGE);
		repaint();
	}

	//@Override
	public void moleculeListHuckelAtomSeqNumAutoSetted(MoleculeEvent event) {
		repaint();		
	}

	//@Override
	public void moleculeListHuckelAtomSeqNumReset(MoleculeEvent event) {
		repaint();		
	}

	//@Override
	public void atomLocationChanged(GeometryEvent event) {
		repaint();		

	}

	//@Override
	public void moleculeMoved(GeometryEvent e) {
		repaint();		
	}

	//@Override
	public void moleculeRotated(GeometryEvent e) {
		repaint();		

	}

	//@Override
	public void huckelAtomHxChanged(HuckelAtomEvent e) {
		createSymmetry();
		repaint();		

	}

	//@Override
	public void huckelAtomRadRChanged(HuckelAtomEvent e) {
		// mise en commentaire : car le rafraichissement est pris en charge par mesomeryview car mesomery envoie l'evenement avant molecule
		// en effet mesomery ecoute molecule avant structureview donc huckelAtomRadRChanged est renvoye a mesomeryview avant le lancement des laculs des poids
		// molecule.huckelAtomRadRChanged est renvoye  apres le calcul. donc si les calculs sont longs, le retour d'information n'est pa instantane
		//repaint()	;	
	}

	//@Override
	public void huckelAtomSeqNumChanged(HuckelAtomEvent e) {
		repaint();		
	}

	//@Override
	public void huckelBondBondTypeChanged(HuckelBondEvent e) {
		// mise en commentaire : car le rafraichissement est pris en charge par mesomeryview car mesomery envoie l'evenement avant molecule
		// en effet mesomery ecoute molecule avant structureview donc huckelAtomRadRChanged est renvoye a mesomeryview avant le lancement des laculs des poids
		// molecule.huckelAtomRadRChanged est renvoye  apres le calcul. donc si les calculs sont longs, le retour d'information n'est pa instantane

		//repaint()	;

	}

	//@Override
	public void huckelBondHxyChanged(HuckelBondEvent e) {
		createSymmetry();
		repaint();		
	}

	//@Override
	public void monoExcitationTriggered(MonoExcitationEvent e) {
		repaint();		
	}

	//@Override
	public void structureDelocalizedSpinChanged(StructureDelocalizedEvent event) {
		repaint();		
	}

	//@Override
	public void moleculeBlocDelocalizedAdded(
			MoleculeEvent event) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void moleculeBlocDelocalizedRemoved(
			MoleculeEvent event) {
		repaint();		

	}

	//@Override
	public void blocDelocalizedHuckelObjectAdded(BlocDelocalizedEvent e) {
		// mise en commentaire : car le rafraichissement est pris en charge par mesomeryview car mesomery envoie l'evenement avant molecule
		// en effet mesomery ecoute molecule avant structureview donc huckelAtomRadRChanged est renvoye a mesomeryview avant le lancement des laculs des poids
		// molecule.huckelAtomRadRChanged est renvoye  apres le calcul. donc si les calculs sont longs, le retour d'information n'est pa instantane
		//repaint()	;			
	}

	//@Override
	public void blocDelocalizedHuckelObjectRemoved(BlocDelocalizedEvent e) {
		// mise en commentaire : car le rafraichissement est pris en charge par mesomeryview car mesomery envoie l'evenement avant molecule
		// en effet mesomery ecoute molecule avant structureview donc huckelAtomRadRChanged est renvoye a mesomeryview avant le lancement des laculs des poids
		// molecule.huckelAtomRadRChanged est renvoye  apres le calcul. donc si les calculs sont longs, le retour d'information n'est pa instantane
		//repaint()	;	
	}

	//@Override
	public void blocDelocalizedNbElectronsChanged(BlocDelocalizedEvent e) {
		// mise en commentaire : car le rafraichissement est pris en charge par mesomeryview car mesomery envoie l'evenement avant molecule
		// en effet mesomery ecoute molecule avant structureview donc huckelAtomRadRChanged est renvoye a mesomeryview avant le lancement des laculs des poids
		// molecule.huckelAtomRadRChanged est renvoye  apres le calcul. donc si les calculs sont longs, le retour d'information n'est pa instantane
		//repaint()	;	
	}

	public BlocDelocalized getOverflightBlocDelocalized() {
		return this.overflightBlocDelocalized;
	}

	public void setOverflightBlocDelocalized(BlocDelocalized blocDelocalized) {
		this.overflightBlocDelocalized = blocDelocalized;
	}

	/**
	 * get structure smymetry 
	 * @return symmetry
	 */
	public Symmetry getSymmetry() {
		if (symmetry == null)
			createSymmetry();
		return symmetry;
	}
	
	
	/*
	 * create structure symmetry
	 */
	private void createSymmetry() {
		symmetry = new Symmetry(this.getStructure());
	}

	@Override
	public void structureLocalizedCoupleRemoved(StructureLocalizedEvent event) {
		repaint();
	    
	}



}