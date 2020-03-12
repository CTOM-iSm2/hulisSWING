package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;

import org.ctom.hulis.huckel.Spin;
import org.ctom.hulis.huckel.SpinOrbitaleMoleculaire;

public class EnergyControl extends OrbitalControl {
	
	public final static float ARROW_STROKE = 1.9f;
	public final static int VMARGIN_ARROW = 2;
	public final static int VDECALAGE_ARROW = 2;
	public final static float DEFAULT_STROKE = 0.0f;
	public final static double HEIGHT = 15;

	public final static float LINE_STROKE = 1.52f;
	public final static double WIDTH = 13;
	public final Color COLOR_ARROW = Color.RED;
	public final Color COLOR_EXCITATION = Color.ORANGE;
	
	public EnergyControl(GraphOM graphOM, SpinOrbitaleMoleculaire som,
			Point2D p) {
		super(graphOM, som, p);
	}

	public void draw(Graphics2D g2d) {
		super.draw(g2d);

		// DESSINE DEUX FLECHES VERTICALES

		g2d.setStroke(new BasicStroke(EnergyControl.ARROW_STROKE));
		g2d.setColor(this.COLOR_ARROW);
		
		if (this.getSOM().getSpin() == Spin.ALPHA) {
			// left arrow
			g2d.draw(new Line2D.Double(
					this.p.getX() - EnergyControl.WIDTH / 4,
					this.p.getY() - EnergyControl.HEIGHT / 2 + VMARGIN_ARROW - VDECALAGE_ARROW ,
					this.p.getX() - EnergyControl.WIDTH / 4, 
					this.p.getY() + EnergyControl.HEIGHT / 2 - VMARGIN_ARROW - VDECALAGE_ARROW ));
			if (GraphOM.DRAW_ARROW_HEAD_PART1) 
				g2d.draw(new Line2D.Double(
						this.p.getX() - EnergyControl.WIDTH * 3 / 8, 
						this.p.getY() - EnergyControl.HEIGHT / 8 - VDECALAGE_ARROW , 
						this.p.getX() - EnergyControl.WIDTH / 4,
						this.p.getY() - EnergyControl.HEIGHT / 2 -  VDECALAGE_ARROW ));
			if (GraphOM.DRAW_ARROW_HEAD_PART2) 
				g2d.draw(new Line2D.Double(
						this.p.getX()- EnergyControl.WIDTH / 4, 
						this.p.getY()- EnergyControl.HEIGHT / 2 - VDECALAGE_ARROW, 
						this.p.getX() - EnergyControl.WIDTH / 8, 
						this.p.getY()- EnergyControl.HEIGHT / 8 - VDECALAGE_ARROW));
			
		}
		if (this.getSOM().getSpin() == Spin.BETA) {
			// right arrow
		// right arrow
				g2d.draw(new Line2D.Double(
						this.p.getX() + EnergyControl.WIDTH / 4, 
						this.p.getY()- EnergyControl.HEIGHT / 2 + VMARGIN_ARROW + VDECALAGE_ARROW , 
						this.p.getX()+ EnergyControl.WIDTH / 4, 
						this.p.getY()+ EnergyControl.HEIGHT / 2 - VMARGIN_ARROW + VDECALAGE_ARROW ));
				if (GraphOM.DRAW_ARROW_HEAD_PART1) {
					g2d.draw(new Line2D.Double(
							this.p.getX() + EnergyControl.WIDTH * 3 / 8, 
							this.p.getY() + EnergyControl.HEIGHT / 8 + VDECALAGE_ARROW , 
							this.p.getX()+ EnergyControl.WIDTH / 4,
							this.p.getY() + EnergyControl.HEIGHT / 2 + VDECALAGE_ARROW  ));
				if (GraphOM.DRAW_ARROW_HEAD_PART2) 
					g2d.draw(new Line2D.Double(
							this.p.getX()+ EnergyControl.WIDTH / 4, 
							this.p.getY()+ EnergyControl.HEIGHT / 2 + VDECALAGE_ARROW , 
							this.p.getX() + EnergyControl.WIDTH / 8, 
							this.p.getY() + EnergyControl.HEIGHT / 8 + VDECALAGE_ARROW ));
						}
		}

		if (gom.isDrawingExcitation()
				&& gom.getPointFrom() != null) {
			g2d.setStroke(new BasicStroke(EnergyControl.ARROW_STROKE));
			g2d.setColor(this.COLOR_EXCITATION);
			/*
			 * Point2D pointFrom = null; for (EnergyControl ec :
			 * getEnergyControls()) { if (ec.getEOrb()==eO) { pointFrom =
			 * ec.getPoint(); break; } }
			 */
			Point pointTo = gom.getCurrentPointDragged();
			// create new QuadCurve2D.Float
			QuadCurve2D q = new QuadCurve2D.Float();
			// draw QuadCurve2D.Float with set coordinates
			double x1 = gom.getPointFrom().getX();
			double y1 = gom.getPointFrom().getY();
			double x2 = pointTo.getX();
			double y2 = pointTo.getY();
			double ctrlx = x1 * 0.7;
			double ctrly = y2 * 0.9;
			q.setCurve(x1, y1, ctrlx, ctrly, x2, y2);
			g2d.draw(q);

			// g2d.draw(new Line2D.Double (pointFrom.getX(),
			// pointFrom.getY(), pointTo.getX(), pointTo.getY()));
		}
		g2d.setStroke(new BasicStroke(EnergyControl.DEFAULT_STROKE));
	}

	public Spin getSpin() {
		return ((SpinOrbitaleMoleculaire) this.om).getSpin();
	}
	
	public SpinOrbitaleMoleculaire getSOM() {
		return (SpinOrbitaleMoleculaire) super.getOM();
	}

} // class EnergyControl