package org.ctom.view.Dessin.Interactif;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JMenu;

import org.ctom.view.GeneralDrawingParameters;
import org.ctom.view.StdColor;
import org.ctom.view.Dessin.Dessin;
import org.ctom.view.Dessin.DessinMode;


public class Ligne extends Line2D implements Dessin, DessinInteractif {

	private Color color = StdColor.NormalColor.getColor();
	private int lw = 4;

	private DessinMode mode = DessinMode.NORMAL;

	private int offset = 4;

	private double x1;
	private double x2;
	private double y1;
	private double y2;

	public Ligne(double x1, double y1, double x2, double y2) {
		super();
		this.setLine(x1, y1, x2, y2);
		this.mode = DessinMode.NORMAL;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.geom.Line2D#contains(double, double)
	 */
	//@Override
	public boolean contains(double x, double y) {
		return this.getShape().contains(x, y);
	}

	//@Override
	public void draw(Graphics2D g2d) {
		Stroke s_current = g2d.getStroke();
		Color c_current = g2d.getColor();
		switch (this.getMode()) {
			case SELECTED:
				g2d.setStroke(GeneralDrawingParameters.SELECTED_STROKE);
				g2d.setColor(StdColor.SelectdColor.getColor());
				g2d.draw(this);				
				break;
			default:
				g2d.setColor(this.getColor());
				g2d.draw(this);				
				break;
		}
		g2d.setColor(c_current);
		g2d.setStroke(s_current);
	}

	//@Override
	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(Math.min(this.x1, this.x2), Math.min(
				this.y1, this.y2), Math.abs(this.x2 - this.x1),
				Math.abs(this.y2 - this.y1));
	}

	public double getCenterX() {
		return (this.getX1() + this.getX2()) / 2;
	}

	public double getCenterY() {
		return (this.getY1() + this.getY2()) / 2;
	}

	/**
	 * @return the color
	 */
	//@Override
	public Color getColor() {
		return this.color;
	}

	/**
	 * @return the mode
	 */
	//@Override
	public DessinMode getMode() {
		return this.mode;
	}

	//@Override
	public Point2D getP1() {
		return new Point2D.Double(this.x1, this.y1);
	}

	//@Override
	public Point2D getP2() {
		return new Point2D.Double(this.x2, this.y2);
	}

	//@Override
	public ArrayList<String> getToolTipText(int x, int y) {
		ArrayList<String> str;
		if (this.contains(x, y)) {
			str = new ArrayList<String>();
			str.add(this.getClass().toString());
			str.add("x:"+this.getCenterX());
			str.add("y:"+this.getCenterY());
		} else {
			str=null;
		}
		return str;
	}
	
	//@Override
	public Shape getShape() {
		Rectangle2D r = new Rectangle2D.Double(-this.offset, -this.offset, 2
				* this.offset + this.getP1().distance(this.getP2()), 2
				* this.offset + this.lw);
		AffineTransform at = new AffineTransform();
		at.setToTranslation(this.getX1(), this.getY1());
		at.rotate(this.getX2() - this.getX1(), this.getY2() - this.getY1());
		return at.createTransformedShape(r);
	}

	/**
	 * @return the x1
	 */
	//@Override
	public double getX1() {
		return this.x1;
	}

	/**
	 * @return the x2
	 */
	//@Override
	public double getX2() {
		return this.x2;
	}

	/**
	 * @return the y1
	 */
	//@Override
	public double getY1() {
		return this.y1;
	}

	/**
	 * @return the y2
	 */
	//@Override
	public double getY2() {
		return this.y2;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	//@Override
	public void setColor(Color color) {
		this.color = color;
	}

	//@Override
	public void setLine(double x1, double y1, double x2, double y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

	//@Override
	public void setLineWidth(int lw) {
		this.lw = lw;
	}

	/**
	 * @param mode
	 *            the mode to set
	 */
	//@Override
	public void setMode(DessinMode mode) {
		this.mode = mode;
	}

	//@Override
	public TreeSet<JMenu> getPopupMenu(ActionListener al, int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setSelected() {
		this.setMode(mode.SELECTED);
	}

	public void unsetSelected() {
		this.setMode(mode.NORMAL);
	}


}
