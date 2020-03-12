package org.ctom.view.Dessin.Interactif;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JMenu;

import org.ctom.view.GeneralDrawingParameters;
import org.ctom.view.StdColor;
import org.ctom.view.Dessin.Dessin;
import org.ctom.view.Dessin.DessinMode;
import org.ctom.view.Dessin.FillStyle;


public class Cercle extends Ellipse2D implements Dessin, DessinInteractif {

	private Color color = null;
	private FillStyle fillStyle = null;
	private DessinMode mode = DessinMode.NORMAL;

	private double r;

	private double x;

	private double y;

	public Cercle(double x, double y, double r) {
		super();
		this.x = x;
		this.y = y;
		this.r = r;
		this.mode = DessinMode.NORMAL;
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.geom.Line2D#contains(double, double)
	 */
	//@Override
	public boolean contains(double x, double y) {
		boolean contains = this.getShape().contains(x, y);
		return contains;
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
				if (this.getColor() == null) {
					g2d.setColor(StdColor.NormalColor.getColor());
				} else {
					g2d.setColor(this.getColor());
				}
				if (this.getFillStyle() == FillStyle.FILLED) {
					g2d.fill(this.getShape());
				}
				g2d.draw(this);
				break;
		}
		g2d.setColor(c_current);
		g2d.setStroke(s_current);
	}

	//@Override
	public Rectangle2D getBounds2D() {
		return new Rectangle2D.Double(this.x, this.y, 2 * this.r, 2 * this.r);
	}

	/**
	 * @return the color
	 */
	//@Override
	public Color getColor() {
		return this.color;
	}

	public Object getFillStyle() {
		return this.fillStyle;
	}

	//@Override
	public double getHeight() {
		return 2 * this.r;
	}

	//@Override
	public DessinMode getMode() {
		return this.mode;
	}

	//@Override
	public TreeSet<JMenu> getPopupMenu(ActionListener al, int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public Shape getShape() {
		Ellipse2D c = new Ellipse2D.Double(0, 0, 2 * this.r, 2 * this.r);
		AffineTransform at = new AffineTransform();
		at.setToTranslation(this.getX(), this.getY());
		return at.createTransformedShape(c);
	}

	//@Override
	public ArrayList<String> getToolTipText(int x, int y) {
		ArrayList<String> str;
		if (this.contains(x, y)) {
			str = new ArrayList<String>();
			str.add(this.getClass().toString());
			str.add("x:" + this.getCenterX());
			str.add("y:" + this.getCenterY());
		} else {
			str = null;
		}
		return str;
	}

	//@Override
	public double getWidth() {
		return this.getHeight();
	}

	//@Override
	public double getX() {
		return this.x;
	}

	//@Override
	public double getY() {
		return this.y;
	}

	//@Override
	public boolean isEmpty() {
		return false;
	}

	/**
	 * @param color
	 *            the color to set
	 */
	//@Override
	public void setColor(Color color) {
		this.color = color;
	}

	public void setFillStyle(FillStyle style) {
		this.fillStyle = style;
	}

	//@Override
	public void setFrame(double arg0, double arg1, double arg2, double arg3) {
		// TODO Auto-generated method stub
	}

	//@Override
	public void setLineWidth(int lw) {
	}

	//@Override
	public void setMode(DessinMode mode) {
		this.mode = mode;
	}

	/**
	 * @param r
	 *            the r to set
	 */
	public void setR(double r) {
		this.r = r;
	}

	//@Override
	public void setSelected() {
		this.mode = DessinMode.SELECTED;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	//@Override
	public void unsetSelected() {
		this.mode = DessinMode.NORMAL;
	}

}
