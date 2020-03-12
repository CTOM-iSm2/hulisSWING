package views;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.Molecule;
import org.ctom.hulis.huckel.structures.symmetry.Symmetry;

import views.prefs.PreferencesGUI;

/**
 * This class contains methods for drawing an atom and select a bond on a GUI
 * 
 * @author Nicolas GOUDARD
 * 
 */
public class AtomDrawer {
	public static final boolean DISPLAY_INDEX = false; // pour debugger
	public static final Color ATOM_TEXT_NUM_COLOR = Color.MAGENTA;
	public static final Color ATOM_TEXT_HX_COLOR = Color.BLACK;
	public static final Color ATOM_TEXT_CHARGE_COLOR = new Color(0,128,0);

	public static final String DELTA_SYMBOL = "\u03B4";
	public static final Color ATOM_NEGATIVE_CHARGE_COLOR = Color.RED;
	public static final Color ATOM_POSITIVE_CHARGE_COLOR = Color.BLUE;

	public static final Color ATOM_RADICAL_R_COLOR = Color.BLACK;
	public static final int DEFAULT_ATOM_RADIUS = (int) Molecule.ATOM_ZONE;

	public static final double DIAMETRE_CHARGE = 12.0;
	public static final double DistXCercleValCharge = 7;
	public static final int H_ATOM_RADIUS = 8;
	public static final double ProjXCercleCharge = 2.08;
	public static final double ProjXradR = -1.5;
	public static final double ProjXValCharge = 1.3;
	static final double ProjYCercleCharge = -1.2;
	public static final double ProjYradR = -0.4;
	public static final double ProjYValCharge = -0.4;
	static final double RADR_RADIUS = AtomDrawer.DEFAULT_ATOM_RADIUS / 4;

	public static final String  FONT_FAMILY = "Helvetica";
	public static final int  FONT_STYLE = Font.PLAIN;
	public static final int  FONT_SIZE_INFOS = 14;
	public static final int  FONT_SIZE_CHARGE = 14;
	public static final int  FONT_SIZE_DELTA = 16;

	public static final double SpaceCercleCharge = 3;
	public static final double ProjXAtomSymbol = -6;
	public static final double ProjYAtomSymbol = 6;
	public static final Stroke STROKE_ATOM_SYMBOL = new BasicStroke(1.0F);
	private static final Color COLOR_ATOM_SYMBOL = Color.BLACK;
	private static final double FONT_SIZE_ATOM_SYMBOL = 18;
	protected Atom atom;
	protected double ProjAtomXInfo = 1.3;

	protected double ProjAtomYInfo = 1.2;

	private final StructureView structureView;
	private  Shape atomShape = null;
	private  Shape lightShape = null;
	
	Color atomBorderColor = null;
	Color atomFillColor = null;
	AtomColor atomColor;
	double atomRadius = 0.0;
	String symbol;
	protected AtomDrawer(StructureView structureView, Atom atom) {
		this.structureView = structureView;
		this.atom = atom;
		//String input ="";
		try {
			atomColor = AtomColor.valueOf(this.atom.getSigle());
			atomRadius =  atomColor.getRadius();
			atomBorderColor =  atomColor.getBorderColor();
			atomFillColor =  atomColor.getFillcolor();
			if (this.atom.getElement() == null)
				symbol  = this.atom.getSigle();
			else
				symbol = this.atom.getElement().toString();			
		}
		catch (IllegalArgumentException e) {
			atomColor = AtomColor.Xx;
			atomRadius = atomColor.getRadius();
			atomBorderColor =  atomColor.getBorderColor();
			atomFillColor =  atomColor.getFillcolor();
			symbol = this.atom.getSigle();
		}
		//symbol = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();

	}



	/**
	 * This method draws an atom object
	 * 
	 * @param g2d :
	 *            graphics object
	 */
	protected void draw(Graphics2D g2d) {
		// Location of displayed informations are proportional to the Atom
		// location and his radios
		double xTemp;
		double yTemp;

		double zoom = this.structureView.getZoom();
		double decalageSymbol = 0;
		if (symbol.length() >2)
			decalageSymbol = (symbol.length() -1) * 8 * zoom;

		double projXAtomSymbol = AtomDrawer.ProjXAtomSymbol * zoom;

		double projYAtomSymbol = AtomDrawer.ProjYAtomSymbol * zoom;

		double projYradR = AtomDrawer.ProjYradR * zoom
				* atomRadius;
		double projXradR = AtomDrawer.ProjXradR * zoom
				*atomRadius;
		double radR_RADIUS = AtomDrawer.RADR_RADIUS
				* zoom;
		double projXValCharge = AtomDrawer.ProjXValCharge
				* zoom
				* atomRadius + decalageSymbol;
		double projYValCharge = AtomDrawer.ProjYValCharge
				* zoom
				* atomRadius;
		double projXCercleCharge = AtomDrawer.ProjXCercleCharge
				* zoom
				* atomRadius + decalageSymbol;
		double projYCercleCharge = AtomDrawer.ProjYCercleCharge
				* zoom
				* atomRadius;
		double diametreCharge = AtomDrawer.DIAMETRE_CHARGE
				* zoom;
		double spaceCercleCharge = AtomDrawer.SpaceCercleCharge
				* zoom;
		double distXCercleValCharge = AtomDrawer.DistXCercleValCharge;
		int charge;
		int absCharge;
		String txtCharge;

		boolean isAtomInOverFlightBloc = false;
		boolean isAtomInSelectedBloc = false;

		// evalue si l'atome est dans un bloc survole ou un bloc selectionne
		if (atom instanceof HuckelAtom) {
			HuckelAtom huckelAtom =  (HuckelAtom) atom;
			if ((huckelAtom.getBlocDelocalizedParent() == this.structureView.getSelectedBlocDelocalized())
					&&  ( this.structureView.getSelectedBlocDelocalized() != null) ) 
				isAtomInSelectedBloc = true;

			else if ( (huckelAtom.getBlocDelocalizedParent() == this.structureView.getOverflightBlocDelocalized())
					&&  ( this.structureView.getOverflightBlocDelocalized() != null) )
				isAtomInOverFlightBloc = true;
		}


		//***  draw atom


		// dessine la bordure de l'atome 
		g2d.setStroke(new BasicStroke(1.0f));
		if (isAtomInOverFlightBloc)
			g2d.setColor(StructureView.OVERFLIGHT_BLOC_DELOCALIZED_COLOR);
		else if (PreferencesGUI.getInstance().isDisplayAtomsCircles())
			g2d.setColor(atomBorderColor);
		else
			g2d.setColor(this.structureView.getBackground());

		g2d.draw(getForm());

		// dessine interieur de l'atome
		if (isAtomInOverFlightBloc)
			g2d.setColor(StructureView.OVERFLIGHT_BLOC_DELOCALIZED_COLOR);

		else {
			if (PreferencesGUI.getInstance().isDisplayAtomsCircles())
				g2d.setColor(atomFillColor);
			else
				g2d.setColor(this.structureView.getBackground());
		}

		g2d.fill(getForm());
		if (PreferencesGUI.getSuperMario()) {
			draw3d(g2d);
			/*
			GradientPaint light = getLight();
			Paint p = g2d.getPaint();
			g2d.setPaint(light);
			g2d.fill(getLightShape());
			//ajout de la symetrie
			Composite comp = g2d.getComposite();
			applySymmetry(g2d);
			g2d.fill(getForm());
			g2d.setComposite(comp);
			g2d.setPaint(p);
			 */
		}

		Point2D atomLocation =  this.structureView.getImagePoint(this.structureView.getPoint2D(this.atom.getLocation()));

		if (PreferencesGUI.getInstance().isDisplayAtomsSymbols()) {

			xTemp = atomLocation.getX()
					+ projXAtomSymbol - symbol.length() * zoom/2;
			yTemp = atomLocation.getY()
					+ projYAtomSymbol- symbol.length() * zoom/2;

			Font fontAtom = new Font(FONT_FAMILY, FONT_STYLE, (int) (FONT_SIZE_ATOM_SYMBOL * (this.structureView.getZoom() % StructureView.MODULO_TEXT_ZOOM)));
			g2d.setFont(fontAtom);
			g2d.setStroke(STROKE_ATOM_SYMBOL);
			g2d.setColor(COLOR_ATOM_SYMBOL);;
			g2d.drawString(symbol, (int) xTemp, (int) yTemp);	
		}


		// dessine un carre autour de l'atome s'il est dans un bloc selectionne
		if (isAtomInSelectedBloc) {
			g2d.setStroke(new BasicStroke(StructureView.SELECTED_BLOC_DELOCALIZED_STROKE));
			g2d.setColor(StructureView.SELECTED_BLOC_DELOCALIZED_COLOR);
			Point2D location =  this.structureView.getImagePoint(new Point2D.Double(atom.getX() - atomRadius,  atom.getY()- atomRadius));

			g2d.draw(new Rectangle2D.Double( location.getX(), location.getY(), 2 * atomRadius * zoom,  2 * atomRadius * zoom) );
		}



		// ****  display the charge

		charge = this.atom.getCharge();
		absCharge = Math.abs(charge);
		txtCharge = Integer.toString(absCharge);

		boolean isDisplayDelta = false;
		if ( (atom instanceof HuckelAtom  ) && (((HuckelAtom) atom).getBlocDelocalizedParent() != null))
			isDisplayDelta = true;


		// si on affiche une charge de type delta (si atome dans un bloc)
		g2d.setStroke(new BasicStroke(1.0f));

		if (isDisplayDelta) {
			xTemp = atomLocation.getX()
					+ projXValCharge;
			yTemp = atomLocation.getY()
					+ projYValCharge;
			g2d.setColor(StructureView.SELECTED_BLOC_DELOCALIZED_COLOR);

			Font fontI = new Font(FONT_FAMILY, FONT_STYLE, (int) (FONT_SIZE_DELTA * (this.structureView
					.getZoom() % StructureView.MODULO_TEXT_ZOOM)));
			g2d.setFont(fontI);
			g2d.drawString(DELTA_SYMBOL, (int) xTemp, (int) yTemp);	
		}
		else {
			// si on affiche pas de charge de type delta on affiche + ou -

			if (charge != 0) {

				//****  display the sign of the charge (+ or -)

				if (absCharge != 1) // decaler le dessin +/- en fonction de la taille du texte de la valeur de la charge
					xTemp = atomLocation.getX()	+ projXCercleCharge
					+ (distXCercleValCharge * (txtCharge.length() - 1)); 
				else
					xTemp = atomLocation.getX() + projXValCharge;

				yTemp = atomLocation.getY() + projYCercleCharge;
				g2d.setStroke(new BasicStroke(1.0f));
				if (charge > 0) {
					g2d.setColor(AtomDrawer.ATOM_POSITIVE_CHARGE_COLOR);
					g2d.draw(new Line2D.Double(xTemp + (diametreCharge / 2), yTemp
							+ spaceCercleCharge, xTemp + (diametreCharge / 2),
							yTemp + diametreCharge - spaceCercleCharge));
				} else {
					g2d.setColor(AtomDrawer.ATOM_NEGATIVE_CHARGE_COLOR);
				}

				g2d.draw(new Line2D.Double(xTemp + spaceCercleCharge, yTemp
						+ (diametreCharge / 2), xTemp + diametreCharge
						- spaceCercleCharge, yTemp + (diametreCharge / 2)));

				// draw the cirle around the sign of the charge
				g2d.draw(new Ellipse2D.Double(xTemp, yTemp, diametreCharge,
						diametreCharge));

				// display the value of the charge if it is not esqual to +1 or -1
				if (absCharge != 1) {
					xTemp = atomLocation.getX()
							+ projXValCharge;
					yTemp = atomLocation.getY()
							+ projYValCharge;
					Font fontI = new Font(FONT_FAMILY, FONT_STYLE, (int) (FONT_SIZE_CHARGE * (this.structureView
							.getZoom() % StructureView.MODULO_TEXT_ZOOM)));
					g2d.setFont(fontI);
					g2d.drawString(txtCharge, (int) xTemp, (int) yTemp);

				}

			} // if charge <>0

		} // if atom in selected bloc

		// display Radical R : one or two small filled circles

		g2d.setColor(AtomDrawer.ATOM_RADICAL_R_COLOR);

		if (this.atom instanceof HuckelAtom) {
			HuckelAtom hAt = (HuckelAtom) this.atom;
			if (hAt.countRadR() > 0) {
				Ellipse2D.Double rad1 = new Ellipse2D.Double(atomLocation.getX()- radR_RADIUS
						+ projXradR, atomLocation.getY() - radR_RADIUS
						+ projYradR, 2 * radR_RADIUS, 2 * radR_RADIUS);
				g2d.fill(rad1);
				if (hAt.countRadR() == 2) {
					Ellipse2D.Double rad2 = new Ellipse2D.Double(atomLocation.getX()
							- radR_RADIUS
							+ projXradR, atomLocation.getY() - radR_RADIUS
							- projYradR, 2 * radR_RADIUS, 2 * radR_RADIUS);
					g2d.fill(rad2);
				}

			}
		}

	}



	/**
	 * 
	 * @return
	 */
	protected Shape getForm() {
		if (this.atomShape != null)
			return this.atomShape;
					
		double radius = this.structureView.getZoom()
				* atomRadius;
		Point2D p = this.structureView.getImagePoint(new Point2D.Double(this.atom.getX(), this.atom.getY()));
		this.atomShape =  new Ellipse2D.Double( p.getX()- radius, p.getY()- radius, 2 * radius, 2 * radius);
		return this.atomShape;
	}

	protected Shape getLightShape() {
		if (this.lightShape != null)
			return this.lightShape;
		double radius = this.structureView.getZoom()
				* atomRadius;
		Point2D p = this.structureView.getImagePoint(new Point2D.Double(this.atom.getX(), this.atom.getY()));
		radius*=0.9;
		this.lightShape =  new Ellipse2D.Double( p.getX()- radius, p.getY()- radius, 2 * radius, 2 * radius);
		return this.lightShape;
	}

	private void draw3d(Graphics2D g2d) {
		/*
		 * adapte de http://www.java2s.com/Code/Java/2D-Graphics-GUI/RadialGradient.htm
		 * voir la licence pour utilisation 
		 */
		
		Color fillColor = getColorSymmetry();
//		fillColor = fillColor.brighter();
		
		Paint paintInit = g2d.getPaint();
		Composite compInit = g2d.getComposite();
		
		Color backgroundColor = this.structureView.getBackground();
		g2d.setColor(backgroundColor);
		g2d.setStroke(new BasicStroke(1.0f));
		g2d.draw(getForm());
		g2d.fill(getForm());
		
		Graphics2D g2=g2d;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(fillColor);
		g2d.fill(getForm());
		double radius = this.structureView.getZoom()
				* atomRadius;
		Point2D pt = this.structureView.getImagePoint(new Point2D.Double(this.atom.getX(), this.atom.getY()));
		GradientPaint gp = new GradientPaint((float)(pt.getX()- radius), (float)(pt.getY()- radius),
				new Color(0.0f, 0.0f, 0.0f, 0.4f),
				(float)(pt.getX()+ radius), (float)(pt.getY()+ radius),
				new Color(0.0f, 0.0f, 0.0f, 0.0f));
        g2.setPaint(gp);
		g2d.fill(getForm());
		gp = new GradientPaint((float)(pt.getX()- radius), (float)(pt.getY()- radius),
				new Color(1.0f, 1.0f, 1.0f, 0.0f),
				(float)(pt.getX()+ radius), (float)(pt.getY()+ radius),
				new Color(1.0f, 1.0f, 1.0f, 0.4f));
        g2.setPaint(gp);
		g2d.fill(getForm());
		RadialGradientPaint rgp = new RadialGradientPaint(pt, (float) (2*radius),
                new float[] { 0.0f, 1.0f },
                new Color[] { new Color(6, 76, 160, 127),
                    new Color(0.0f, 0.0f, 0.0f, 0.8f) });
		g2.setPaint(rgp);
		g2d.fill(getForm());
		rgp = new RadialGradientPaint(new Point2D.Double(pt.getX(),
                pt.getY()+radius * 0.5), (float)radius / 2.3f,
                new Point2D.Double(pt.getX(), pt.getY()+0.75*radius + 0),
                new float[] { 0.0f, 0.8f },
                new Color[] { new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 255),
                    new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(), 0) },
                RadialGradientPaint.CycleMethod.NO_CYCLE,
                RadialGradientPaint.ColorSpaceType.SRGB,
                AffineTransform.getScaleInstance(1.0, 1.0));
        g2.setPaint(rgp);
        g2d.fill(getForm());
        rgp = new RadialGradientPaint(new Point2D.Double(pt.getX(),
                pt.getY()), (float)radius / 1.4f,
                new Point2D.Double(45.0, 25.0),
                new float[] { 0.0f, 0.5f },
                new Color[] { new Color(1.0f, 1.0f, 1.0f, 0.4f),
                    new Color(1.0f, 1.0f, 1.0f, 0.0f) },
                RadialGradientPaint.CycleMethod.NO_CYCLE);
        g2.setPaint(rgp);
        g2d.fill(getForm());

		g2d.setComposite(compInit);
		g2d.setPaint(paintInit);

	}
	
    static String[] ColourValues = new String[] { 
        "FF0000", "00FF00", "0000FF", "FFFF00", "FF00FF", "00FFFF", "000000", 
        "800000", "008000", "000080", "808000", "800080", "008080", "808080", 
        "C00000", "00C000", "0000C0", "C0C000", "C000C0", "00C0C0", "C0C0C0", 
        "400000", "004000", "000040", "404000", "400040", "004040", "404040", 
        "200000", "002000", "000020", "202000", "200020", "002020", "202020", 
        "600000", "006000", "000060", "606000", "600060", "006060", "606060", 
        "A00000", "00A000", "0000A0", "A0A000", "A000A0", "00A0A0", "A0A0A0", 
        "E00000", "00E000", "0000E0", "E0E000", "E000E0", "00E0E0", "E0E0E0"
    };
    
 
    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
//    int randomNum = rand.nextInt((max - min) + 1) + min;

	private Color getColorSymmetry() {
		Color color;
		Symmetry sym = structureView.getSymmetry();
		
		int num = sym.getAtomGroupNumber(this.atom);
		//int tot = sym.getTotalNumberOfGroups();
	   // Random rand = new Random();
		//int num = rand.nextInt((55 - 0) + 1) + 0;
		color = Color.decode("#"+ColourValues[num]);
		return color;
	}

	private void applySymmetry(Graphics2D g2d) {
		Symmetry sym = structureView.getSymmetry();
		int num = sym.getAtomGroupNumber(this.atom);
		int tot = sym.getTotalNumberOfGroups();
		if (num!=0) {
			g2d.setColor(Color.blue);
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,num * 1.0f/tot));
		}
	}

	private GradientPaint getLight() {
		double radius = this.structureView.getZoom()
				* atomRadius;
		Point2D p = this.structureView.getImagePoint(new Point2D.Double(this.atom.getX(), this.atom.getY()));
		GradientPaint gp = new GradientPaint((float)(p.getX()- radius), (float)(p.getY()- radius), Color.white,(float)(p.getX()+ radius), (float)(p.getY()+ radius), Color.yellow);
		return gp;
	}

	/**
	 * 
	 * @param p
	 * @return
	 */
	public boolean isContained(Point2D p) {
		return getForm().contains(p);
	}

	/**
	 * 
	 * @param g2d
	 */
	public void writeInfos(Graphics2D g2d) {
		g2d.setStroke(new BasicStroke(1.0f));

		Font fontI = new Font(
				"Helvetica",
				Font.PLAIN,
				(int) (FONT_SIZE_INFOS * (this.structureView.getZoom() % StructureView.MODULO_TEXT_ZOOM)));
		g2d.setFont(fontI);

		double projXInfo = this.structureView.getZoom()
				* atomRadius
				* this.ProjAtomXInfo;
		double projYInfo = this.structureView.getZoom()
				* atomRadius
				* this.ProjAtomYInfo;

		Point2D infoLocation =  this.structureView.getImagePoint(this.structureView.getPoint2D(this.atom.getLocation()));
		int x = (int) (infoLocation.getX()
				+ projXInfo);
		int y = (int) (infoLocation.getY()
				+ projYInfo);

		String txtNum = "";
		String txtSigle = "";
		String txtHx = "";
		String txtChargeDensity = "";
		if (DISPLAY_INDEX) {
			g2d.setColor(Color.BLACK);
			g2d.drawString(Integer.toString(this.atom.getIndex()),  x,  y);
		}
		
		if (!DISPLAY_INDEX && (this.atom instanceof HuckelAtom)) {
			HuckelAtom hAt = (HuckelAtom) this.atom;

			if (this.structureView.isDisplayNum()
					|| !this.structureView.getStructure()
					.areCorrectAtomsSeqNum())
				txtNum = Integer.toString(((HuckelAtom) this.atom)
						.getSeqNum())+ " " ;

			if (this.structureView.isDisplayHxHxy() && this.atom instanceof HuckelAtom) {
				txtSigle = this.atom.getSigle() ;
				txtHx = ": hX=" + ((HuckelAtom) this.atom).getHx();
			}

			if (this.structureView.isDisplayCharges()) {
				if (txtHx != "") 
					txtChargeDensity += " ; ";
				else 
					txtHx +=  this.atom.getSigle() + ": ";
				txtChargeDensity +=  "q=" + FrameApp.form.format(hAt.getDensity());
			}

			FontMetrics metrics = this.structureView.getFontMetrics(g2d.getFont());

			g2d.setColor(AtomDrawer.ATOM_TEXT_NUM_COLOR);
			g2d.drawString(txtNum + txtSigle,  x,  y);


			x +=  metrics.stringWidth (txtNum + txtSigle);
			g2d.setColor(AtomDrawer.ATOM_TEXT_HX_COLOR);
			g2d.drawString(txtHx ,  x,  y);

			x +=  metrics.stringWidth (txtHx);
			g2d.setColor(AtomDrawer.ATOM_TEXT_CHARGE_COLOR);
			g2d.drawString(txtChargeDensity,  x, y);
		} // if huckelAtom
	}
}