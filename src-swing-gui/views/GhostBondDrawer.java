package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.structures.StructureLocalized;

/**
 * This class contains methods for drawing a bond and select a bond on a GUI
 * 
 * @author Nicolas GOUDARD
 * 
 */

public class GhostBondDrawer {
	enum BondForm {BROKEN_DOUBLE_LINE, FULL_SIMPLE_LINE, FULL_DOUBLE_LINE};
	public static final Color BOND_COLOR = Color.LIGHT_GRAY;
	public static final Color BOND_TEXT_HXY_COLOR = Color.BLACK;
	public static final Color BOND_TEXT_BOND_ORDER_COLOR = new Color(0,128,0);


	public static final Color BOND_INFOS_COLOR2 = new Color(150, 75, 0);
	public static final float BOND_LINE_STROKE = 3.0f;
	public static final int BOND_MOUSE_SENSIBILITY = 10;
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

	/**
	 * 
	 */
	private final StructureView structureView;

	/**
	 * @param structureView
	 * @param bond
	 */
	protected GhostBondDrawer(StructureView structureView, Bond bond) {
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

		float bondLineStroke = GhostBondDrawer.BOND_LINE_STROKE;
			
		g2d.setColor(GhostBondDrawer.BOND_COLOR);

		g2d.setStroke(new BasicStroke(bondLineStroke));
		g2d.draw(this.line1);

	}




	protected Shape getForm() {

		double dX = this.bond.getAtom2().getX() - this.bond.getAtom1().getX();
		double dY = this.bond.getAtom2().getY() - this.bond.getAtom1().getY();
		double hyp = Math.sqrt(dX * dX + dY * dY);

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
			projX = (((dX / hyp) * Math.cos(Math.PI / 2)) + ((dY / hyp) * Math
							.sin(Math.PI / 2)));
			projY = (((dY / hyp) * Math.cos(Math.PI / 2)) - ((dX / hyp) * Math
							.sin(Math.PI / 2)));
		}

		Point2D a11 = new Point2D.Double((int) (this.bond.getAtom1().getX() + projX), (int) (this.bond.getAtom1().getY() + projY));
		Point2D a12 = new Point2D.Double((int) (this.bond.getAtom2().getX() + projX), (int) (this.bond.getAtom2().getY()+ projY));

		if (hyp != 0) {
			projX = (((dX / hyp) * Math.cos(Math.PI / 2)) - ((dY / hyp) * Math
							.sin(Math.PI / 2)));
			projY = (((dY / hyp) * Math.cos(Math.PI / 2)) + ((dX / hyp) * Math
							.sin(Math.PI / 2)));
		}

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
					this.line1 = new Line2D.Double(this.bond.getAtom1().getX(), this.bond.getAtom1().getY(), this.bond.getAtom2().getX(), this.bond.getAtom2().getY());
					path.append(this.line1, false);
				} else {
					this.line1 = new Line2D.Double(a11, a12);
					path.append(this.line1, false);
				}
				break;
			case 2:
				this.line1 = new Line2D.Double(a11, a12);
				path.append(this.line1, false);

				break;
			}
		else {
			this.line1 = new Line2D.Double(this.bond.getAtom1().getX(), this.bond.getAtom1().getY(), this.bond.getAtom2().getX(), this.bond.getAtom2().getY());
			path.append(this.line1, false);

		}

		path.closePath();
		return path;
	}

	protected Line2D getLine1() {
		return this.line1;

	}

}