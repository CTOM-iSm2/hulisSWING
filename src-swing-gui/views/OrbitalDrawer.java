/**
 * 
 */
package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.OrbitaleAtomique;
import org.ctom.hulis.prefs.Preferences;
import org.ctom.util.maths.Maths;

import views.prefs.PreferencesGUI;

public class OrbitalDrawer {

	public static final double ProjXInfoOrb = 1 * AtomDrawer.DEFAULT_ATOM_RADIUS;
	public static final double ProjYInfoOrb = 1 * AtomDrawer.DEFAULT_ATOM_RADIUS;
	private OrbitaleAtomique orb;
	private double coeff;
	public final double GAP_FACTOR = 1/15;
	public final int SPACE_X_BACK_CIRCLE = 4;
	public final int SPACE_X_FORE_CIRCLE = 0;

	public final int SPACE_Y_BACK_CIRCLE = -8;
	public final int SPACE_Y_FORE_CIRCLE = 0;
	/**
	 * 
	 */
	private final StructureView structureView;

	public OrbitalDrawer(StructureView structureView, OrbitaleAtomique orb, double coeff) {
		this.structureView = structureView;
		this.coeff=coeff;
		this.orb = orb;
	}
	/**
	 * @return the coeff
	 */
	public double getCoeff() {
		return this.coeff;
	}
	
	public double getScaleOrbitals() {
		if (Preferences.isDISPLAY_DENSITY()) {
			return PreferencesGUI.getScaleOrbDefault()/4;
		} else {
			return PreferencesGUI.getInstance().getSCALE_ORB();
		}
	}

	public void activeTransparency(Graphics2D g2d) {
		float transparency = 0.7F; // 0.5f ;
		g2d
				.setComposite(java.awt.AlphaComposite.getInstance(
						java.awt.AlphaComposite.SRC_OVER, Math.min(
								transparency, 1.0f)));

	}

	/**
	 * This method draws an atom object
	 * 
	 * @param g2d :
	 *            graphics object
	 */
	protected void draw(int order, Graphics2D g2d) {

		double radius = getRadius();
		// draw orbitale
		Color colorOM = Color.RED;
		Color colorDensity = Color.GREEN;
		Color colorNatOrb = Color.ORANGE;
		Color color=null;
		if (Preferences.isDISPLAY_DENSITY()) {
			color = colorDensity;
		} else {
			switch (Preferences.DISPLAY_ORBITAL) {
			case CANONICAL:
				color = colorOM;
				break;
			case NATURAL:
				color = colorNatOrb;
				break;
			default:
				break;
			}
		}
		if (this.getCoeff() * this.getScaleOrbitals()  < 0) { // coeff negatif
			if (order == 1) { // orbitale du fond
				activeTransparency(g2d);
				g2d.setColor(color);
				g2d.setStroke(new BasicStroke(1.0f));
				g2d.fill(getBackCircle(radius)); // remplit de rouge transparent
				stopTransparency(g2d);
				g2d.setStroke(new BasicStroke(1.0f));
				g2d.draw(getBackCircle(radius)); // bordure rouge non transparente
			} else { // orbitale de devant
				activeTransparency(g2d);
				g2d.setColor(Color.WHITE);
				g2d.setStroke(new BasicStroke(1.0f));
				g2d.fill(getForeCircle(radius)); // remplit de blanc transparent
				stopTransparency(g2d);
				g2d.setColor(color);
				g2d.setStroke(new BasicStroke(2.0f));
				g2d.draw(getForeCircle(radius));// bordure rouge non transparent
			}
		} else if (this.getCoeff() * this.getScaleOrbitals()   > 0) { // coef positif
			if (order == 1) {// orbitale du fond
				activeTransparency(g2d);
				g2d.setColor(Color.WHITE);
				g2d.setStroke(new BasicStroke(1.0f));
				g2d.fill(getBackCircle(radius)); // remplit de blanc transparent
				g2d.setColor(color);
				g2d.setStroke(new BasicStroke(1.0f));
				g2d.draw(getBackCircle(radius)); // bordure rouge transparente
			} else { // orbitale de devant
				activeTransparency(g2d);
				g2d.setColor(color);
				g2d.setStroke(new BasicStroke(1.0f));
				g2d.fill(getForeCircle(radius)); // remplit de rouge
				// transparent
				stopTransparency(g2d);
				g2d.setStroke(new BasicStroke(2.0f));
				g2d.draw(getForeCircle(radius));// bordure rouge non transparent

			}
		}

		g2d.setStroke(new BasicStroke(1.0f));

		// Restore the default transparency
		stopTransparency(g2d);

	}

	public Shape getBackCircle(double radius) {
		// les orbitales sont decalees d'un facteur proportionnel au coefficients
		Point2D atomLocation =  this.structureView.getImagePoint(new Point2D.Double(this.orb.getHuckelAtom().getLocation().getX()  + this.SPACE_X_BACK_CIRCLE + radius *this.GAP_FACTOR, this.orb.getHuckelAtom().getY() + this.SPACE_Y_BACK_CIRCLE+ radius *this.GAP_FACTOR) ); 
		
		return new Ellipse2D.Double(
				atomLocation.getX() 
						- radius,
						atomLocation.getY()
						- radius, 2 * radius, 2 * radius);
	}

	public Shape getForeCircle(double radius) {
		Point2D atomLocation =  this.structureView.getImagePoint(new Point2D.Double(this.orb.getHuckelAtom().getLocation().getX()  + this.SPACE_X_FORE_CIRCLE, this.orb.getHuckelAtom().getY() + this.SPACE_Y_FORE_CIRCLE)); 
		
		return new Ellipse2D.Double(
				atomLocation.getX() 
						- radius,
						atomLocation.getY()
						- radius, 2 * radius, 2 * radius);
	}

	protected Shape getForm() {
		GeneralPath path = new GeneralPath();
		double radius = getRadius();
		path.append(getBackCircle(radius), false);
		path.append(getForeCircle(radius), false);
		return path;
	}

	public double getRadius() {
		double radius = Math.abs(this.structureView.getZoom()
				* AtomDrawer.DEFAULT_ATOM_RADIUS * this.getCoeff()
				* this.getScaleOrbitals());
		return radius;
	}

	public boolean isContained(Point2D p) {
		return getForm().contains(p);
	}

	public void stopTransparency(Graphics2D g2d) {
		g2d.setComposite(java.awt.AlphaComposite.getInstance(
				java.awt.AlphaComposite.SRC_OVER, 1.0f));

	}

	public void writeInfos(Graphics2D g2d) {
		if (Maths.round(this.getCoeff(), FrameApp.PRECISION) != 0.0) {
			AtomColor atomColor = null;
			try{
				atomColor =AtomColor.valueOf(this.orb.getHuckelAtom().getSigle());
			}
			catch (IllegalArgumentException e) {
				atomColor = AtomColor.Xx;
			}
			
			double projInfo = this.structureView.getZoom()
					* atomColor.getRadius() * Math.abs(this.getCoeff())
					* this.getScaleOrbitals();
			Font fontI = new Font("Helvetica", Font.PLAIN,
					(int) (14 * (this.structureView
							.getZoom() % StructureView.MODULO_TEXT_ZOOM)));
			g2d.setFont(fontI);
			g2d.setColor(Color.RED);
			Point2D atomLocation =  this.structureView.getImagePoint(this.structureView.getPoint2D(this.orb.getHuckelAtom().getLocation())); 
			double xTemp = atomLocation.getX() + projInfo;
			double yTemp = atomLocation.getY() + projInfo;
			String txtInfo = "C" + this.orb.getHuckelAtom().getSeqNum() + "="
					+ FrameApp.form.format(this.getCoeff());
			g2d.drawString(txtInfo, (int) xTemp, (int) yTemp);
		}
	}

}