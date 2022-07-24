package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import views.prefs.PreferencesGUI;

/**
 * This class contains methods for drawing a bond and select a bond on a GUI
 * 
 * @author Nicolas GOUDARD
 * 
 */

public class BondDrawer {
	enum BondForm {BROKEN_DOUBLE_LINE, FULL_SIMPLE_LINE, FULL_DOUBLE_LINE};
	public static final Color BOND_COLOR = Color.LIGHT_GRAY;
	public static final Color BOND_TEXT_HXY_COLOR = Color.BLACK;
	public static final Color BOND_TEXT_BOND_ORDER_COLOR = new Color(0,128,0);


	public static final Color BOND_INFOS_COLOR2 = new Color(150, 75, 0);
	public static final float BOND_LINE_STROKE = 3.0f;
	public static final int BOND_MOUSE_SENSIBILITY = 16;
	public static final String  FONT_FAMILY = "Helvetica";
	public static final int  FONT_STYLE = Font.PLAIN;
	public static final int  FONT_SIZE_INFOS = 14;

	float MAX_TAILLE_POINTILLES = 6.5f;

	/**
	 * this function returns geometric form of the bond : Paralell lines
	 * 
	 * @return Shape : geometric form of the bond
	 */

	private Bond bond;
	private Shape form;

	private Line2D line1;
	private Line2D line2;
	private BondForm bondForm;

	/**
	 * 
	 */
	private final StructureView structureView;

	/**
	 * @param structureView
	 * @param bond
	 */
	protected BondDrawer(StructureView structureView, Bond bond) {
		this.structureView = structureView;
		this.bond = bond;
		this.form = getForm();
	}

	/**
	 * this method draw a bond
	 * 
	 * @param g2d :
	 *            graphics object
	 */
	protected void draw(Graphics2D g2d) {

		float bondLineStroke = BondDrawer.BOND_LINE_STROKE
				* (float) this.structureView.getZoom();

		if (bond instanceof HuckelBond  ){
			HuckelBond huckelBond =  (HuckelBond) bond;
			if ((huckelBond.getBlocDelocalizedParent() == StructureView.getSelectedBlocDelocalized())
					&& (StructureView.getSelectedBlocDelocalized()!=null)	)
				g2d.setColor(StructureView.SELECTED_BLOC_DELOCALIZED_COLOR);
			else if ( (huckelBond.getBlocDelocalizedParent() == this.structureView.getOverflightBlocDelocalized())
					&& (this.structureView.getOverflightBlocDelocalized() != null))
				g2d.setColor(StructureView.OVERFLIGHT_BLOC_DELOCALIZED_COLOR);
			else
				g2d.setColor(BondDrawer.BOND_COLOR);

		}
		else
			g2d.setColor(BondDrawer.BOND_COLOR);

		switch (this.bondForm) {
		case BROKEN_DOUBLE_LINE :
			float taille_pointille = 0.0f;
			// si les calculs ne sont pas activs, tracer par defaut des pointilles reguliers sibon proportionnels au bond order
			if (this.bond instanceof HuckelBond && ((Structure) this.bond.getMoleculeContainer()).isEnabledFlyCalculate()) {
				// normalement on est tj en huckelbond si this.line2 != null mais mieux vaut mettre le "if" ....
				double bondorder = Math.abs(((HuckelBond) this.bond).getBondOrder());
				taille_pointille =  (float) bondorder * MAX_TAILLE_POINTILLES;

			}else {
				taille_pointille  = MAX_TAILLE_POINTILLES/2;
			}

			if (taille_pointille >MAX_TAILLE_POINTILLES)
				taille_pointille  = MAX_TAILLE_POINTILLES;

			float[] style = { taille_pointille, MAX_TAILLE_POINTILLES -taille_pointille }; // taille des pointilles / taille des blancs


			g2d.setStroke(new BasicStroke(bondLineStroke));
			//
			Paint p = g2d.getPaint();
			if (PreferencesGUI.getSuperMario()) {
				Point2D center = new Point2D.Float((float)line1.getBounds().getCenterX(),(float)line1.getBounds().getCenterY());
				float radius=(float) (line1.getP1().distance(line1.getP2())*0.5);
				float[] dist={0.0f,1.0f};
				Color[] colors={Color.yellow,Color.lightGray};
				RadialGradientPaint rgp = new RadialGradientPaint(center, radius,dist ,colors);
				g2d.setPaint(rgp);
			}
			//
			g2d.draw(this.line1);
			g2d.setPaint(p);
			g2d.setStroke(new BasicStroke(bondLineStroke,
					BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10f,
					style, 0)); // pointilles
			if (PreferencesGUI.getSuperMario()) {
				Point2D center = new Point2D.Float((float)line1.getBounds().getCenterX(),(float)line1.getBounds().getCenterY());
				float radius=(float) (line1.getP1().distance(line1.getP2())*0.5);
				float[] dist={0.0f,1.0f};
				Color[] colors={Color.lightGray,Color.yellow};
				RadialGradientPaint rgp = new RadialGradientPaint(center, radius,dist ,colors);
				g2d.setPaint(rgp);
			}
			g2d.draw(this.line2);
			g2d.setPaint(p);

			break;
		case FULL_SIMPLE_LINE:
			g2d.setStroke(new BasicStroke(bondLineStroke));
			g2d.draw(this.line1);

			break;

		case FULL_DOUBLE_LINE :
			g2d.setStroke(new BasicStroke(bondLineStroke));
			g2d.draw(this.line1);
			g2d.draw(this.line2);

			break;

		}


	}

	protected Shape getForm() {

		double dX = this.bond.getAtom2().getX() - this.bond.getAtom1().getX();
		double dY = this.bond.getAtom2().getY() - this.bond.getAtom1().getY();
		double hyp = Math.sqrt(dX * dX + dY * dY);

		double ecartCentreAtome = 1;

		// space between parallel lines depends of the type of the bond
		if (this.bond instanceof HuckelBond)
			switch (((HuckelBond) this.bond).getBondType()) {
			case 1:
				if (this.bond.getMoleculeContainer() instanceof StructureDelocalized) {
					ecartCentreAtome = (AtomDrawer.DEFAULT_ATOM_RADIUS / 3)
							* this.structureView.getZoom();
					break;
				}
			case 2:
				ecartCentreAtome = (AtomDrawer.DEFAULT_ATOM_RADIUS / 3)
				* this.structureView.getZoom();
				break;
				// case 3 :
				// ecartCentreAtome = (AtomDrawer.DEFAULT_ATOM_RADIUS / 2) *
				// this.structureView.getZoom();
				// break;
			}

		/*
		 * Trigonometric formula reference cos (a - b) = cos a cos b + sin a sin
		 * b sin (a - b) = sin a cos b - cos a sin b cos (a + b) = cos a cos b -
		 * sin a sin b sin (a + b) = sin a cos b + cos a sin b cos (-theta) =
		 * cos theta sin (-theta) = - sin theta b = PI / 2
		 */

		// calculate the parallel lines in function of the positions of the two
		// atoms which compose the bond
		double projX = 0;
		double projY = 0;

		if (hyp != 0) {
			projX = ecartCentreAtome
					* (((dX / hyp) * Math.cos(Math.PI / 2)) + ((dY / hyp) * Math
							.sin(Math.PI / 2)));
			projY = ecartCentreAtome
					* (((dY / hyp) * Math.cos(Math.PI / 2)) - ((dX / hyp) * Math
							.sin(Math.PI / 2)));
		}

		Point2D atom1Location =  this.structureView.getImagePoint(this.structureView.getPoint2D(this.bond.getAtom1().getLocation()));
		Point2D atom2Location =  this.structureView.getImagePoint(this.structureView.getPoint2D(this.bond.getAtom2().getLocation()));

		Point2D a11 = new Point2D.Double((int) (atom1Location.getX() + projX), (int) (atom1Location.getY() + projY));
		Point2D a12 = new Point2D.Double((int) (atom2Location.getX() + projX), (int) (atom2Location.getY()+ projY));

		if (hyp != 0) {
			projX = ecartCentreAtome
					* (((dX / hyp) * Math.cos(Math.PI / 2)) - ((dY / hyp) * Math
							.sin(Math.PI / 2)));
			projY = ecartCentreAtome
					* (((dY / hyp) * Math.cos(Math.PI / 2)) + ((dX / hyp) * Math
							.sin(Math.PI / 2)));
		}

		Point2D a21 = new Point2D.Double((int) (atom1Location.getX() + projX), (int) (atom1Location.getY() + projY));
		Point2D a22 = new Point2D.Double((int) (atom2Location.getX() + projX), (int) (atom2Location.getY()+ projY));

		// constructs parallel lines for the bond
		GeneralPath path = new GeneralPath();

		if (this.bond instanceof HuckelBond)
			switch (((HuckelBond) this.bond).getBondType()) {
			case 1:
				boolean isHuckelBondinBlocDelocalized = false;
				if (this.bond instanceof HuckelBond)
					if (((HuckelBond) this.bond).getBlocDelocalizedParent() != null)
						isHuckelBondinBlocDelocalized = true;

				if (this.bond.getMoleculeContainer() instanceof StructureLocalized && !isHuckelBondinBlocDelocalized) {
					this.line1 = new Line2D.Double(atom1Location.getX(), atom1Location.getY(), atom2Location.getX(), atom2Location.getY());
					path.append(this.line1, false);
					this.line2 = null;
					this.bondForm = BondForm.FULL_SIMPLE_LINE;
				} else {
					this.line1 = new Line2D.Double(a11, a12);
					path.append(this.line1, false);
					this.line2 = new Line2D.Double(a21, a22);
					path.append(this.line2, false);
					this.bondForm = BondForm.BROKEN_DOUBLE_LINE;
				}
				break;
			case 2:
				this.line1 = new Line2D.Double(a11, a12);
				path.append(this.line1, false);
				this.line2 = new Line2D.Double(a21, a22);
				path.append(this.line2, false);
				this.bondForm = BondForm.FULL_DOUBLE_LINE;

				break;
				// case 3 :
				// path.append(new Line2D.Double(a11, a12), false);
				// path.append(new Line2D.Double(bond.getAtom1().getX() *
				// this.structureView.getZoom(), bond.getAtom1().getY() *
				// this.structureView.getZoom(), bond.getAtom2().getX() *
				// this.structureView.getZoom(), bond.getAtom2().getY() *
				// this.structureView.getZoom()), false);
				// path.append(new Line2D.Double(a21, a22), false);
				// break;
			}
		else {
			this.line1 = new Line2D.Double(atom1Location.getX(), atom1Location.getY(), atom2Location.getX(), atom2Location.getY());
			path.append(this.line1, false);
			this.line2 = null;
			this.bondForm = BondForm.FULL_SIMPLE_LINE;

		}

		path.closePath();
		return path;
	}

	protected Line2D getLine1() {
		return this.line1;

	}

	protected Line2D getLine2() {
		return this.line2;

	}

	/**
	 * isContained : this functon test if the point in parameter is on the bond
	 * in function of the constant BOND_MOUSE_SENSIBILITY (width of the side of
	 * square around the point)
	 * 
	 * @param p :
	 *            point to test
	 * @return boolean
	 */
	public boolean isContained(Point2D p) {
		if (p == null)
			return false;

		double bondSensibility = BondDrawer.BOND_MOUSE_SENSIBILITY
				* this.structureView.getZoom();

		Rectangle2D r = new Rectangle2D.Double(p.getX() - bondSensibility/ 2, 
				p.getY() - bondSensibility/ 2, 
				bondSensibility,
				bondSensibility);

		return this.form.intersects(r);

	}

	public void writeInfos(Graphics2D g2d) {
		double zoom = this.structureView.getZoom();
		double x = 0.0;
		double y = 0.0;
		String txtHxy = "";
		String txtCharge = "";
		Font fontI = new Font(
				"Helvetica",
				Font.PLAIN,
				(int) (FONT_SIZE_INFOS * (zoom % StructureView.MODULO_TEXT_ZOOM)));
		g2d.setFont(fontI);
		double bondLineSroke = BondDrawer.BOND_LINE_STROKE
				* this.structureView.getZoom();

		HuckelBond bond2 = null;
		if (this.bond instanceof HuckelBond) {
			bond2 = (HuckelBond) this.bond;

			Point2D infoLocation =  this.structureView.getImagePoint(new Point2D.Double( (this.bond.getAtom1().getX() + this.bond.getAtom2().getX()) / 2, (this.bond.getAtom1().getY() + this.bond.getAtom2().getY()) / 2)); 

			x = infoLocation.getX() + bondLineSroke;
			y = infoLocation.getY() - bondLineSroke;
			if (this.structureView.isDisplayHxHxy()) {
				g2d.setColor(BondDrawer.BOND_TEXT_HXY_COLOR);
				txtHxy = "hXY=" + bond2.getHxy();
			}
			g2d.drawString(txtHxy, (int) x, (int) y);

			if (this.structureView.isDisplayCharges()) {
				g2d.setColor(BondDrawer.BOND_TEXT_BOND_ORDER_COLOR);
				txtCharge = "I=" + FrameApp.form.format(bond2.getBondOrder());
			}
			FontMetrics metrics = this.structureView.getFontMetrics(g2d.getFont());
			x = x+metrics.stringWidth(txtHxy+" ");		
			g2d.drawString(txtCharge, (int) x, (int) y);

		}
	}

}