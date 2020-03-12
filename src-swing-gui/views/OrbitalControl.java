package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.ctom.hulis.huckel.OrbitaleMoleculaire;

public abstract class OrbitalControl {
	
	public final static Color COLOR_LINE = Color.BLACK;
	public final static Color COLOR_SELECT = new Color(83, 251, 234);

	protected Point2D p;
	protected GraphOM gom;
	protected OrbitaleMoleculaire om;

	public OrbitalControl(GraphOM graphOM, OrbitaleMoleculaire om,
			Point2D p) {
		this.gom=graphOM;
		this.om=om;
		this.p=p;
	}

	public OrbitaleMoleculaire getOM() {
		return this.om;		
	}
	
	public boolean contains(Point2D p) {
		/*return new Rectangle2D.Double(this.p.getX() - EnergyControl.WIDTH
				/ 2, this.p.getY() - EnergyControl.HEIGHT / 2,
				EnergyControl.WIDTH, EnergyControl.HEIGHT).contains(p
				.getX(), p.getY());*/
		return getCircle(p).contains(p);
	}

	public Ellipse2D getCircle(Point2D p) {
		return new Ellipse2D.Double (this.p.getX() - EnergyControl.WIDTH,
									 this.p.getY() - EnergyControl.HEIGHT  ,
									 EnergyControl.WIDTH  * 2,
									 EnergyControl.HEIGHT * 2);
	}

	public Point2D getPoint() {
		return this.p;
	}

	public boolean intersects(OrbitalControl eC) {
		return new Rectangle2D.Double(this.p.getX() - EnergyControl.WIDTH
				/ 2, this.p.getY() - EnergyControl.HEIGHT / 2,
				EnergyControl.WIDTH, EnergyControl.HEIGHT)
		.intersects(new Rectangle2D.Double(eC.p.getX()
				- EnergyControl.WIDTH / 2, eC.p.getY()
				- EnergyControl.HEIGHT / 2, EnergyControl.WIDTH,
				EnergyControl.HEIGHT));
	}

	public void setLocation(Point2D p) {
		this.p = p;
	}

	protected void draw(Graphics2D g2d) {
		// DESSINE UN ROND POUR L'ENERGIE SELECTIONNEE
		g2d.setStroke(new BasicStroke(EnergyControl.DEFAULT_STROKE));
		if (gom.getsOMSelected() == this.getOM()) {
			g2d.setColor(this.COLOR_SELECT);
			g2d.fill(getCircle(p));
		}
	
		// DESSINE UN TRAIT HORIZONTAL
	
		g2d.setStroke(new BasicStroke(EnergyControl.LINE_STROKE));
		g2d.setColor(this.COLOR_LINE);
	
		g2d.setStroke(new BasicStroke(EnergyControl.LINE_STROKE));
		g2d.draw(new Line2D.Double(this.p.getX() - EnergyControl.WIDTH / 2,
				this.p.getY(), this.p.getX() + EnergyControl.WIDTH / 2,
				this.p.getY()));
	}

}
