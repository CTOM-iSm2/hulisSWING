package org.ctom.view.Dessin.Interactif;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MultipleGradientPaint.CycleMethod;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JMenu;

import org.ctom.view.Dessin.Dessin;
import org.ctom.view.Dessin.DessinMode;


public class BouleRelief implements Dessin, DessinInteractif {
	public BufferedImage back;
	public Color color;
	public BufferedImage fore;
	public int h;
	public int w;
	public int x0;
	public int y0;

	public BouleRelief(double x0, double y0, double h, double w, Color color) {
		this.x0 = (int) x0;
		this.y0 = (int) y0;
		this.h = (int) h;
		this.w = (int) w;
		this.color = color;
	}

	//@Override
	public boolean contains(double x, double y) {
		boolean contains = this.getShape().contains(x, y);
		return contains;
	}

	//@Override
	public void draw(Graphics2D g2d) {
		this.createBackground();
		this.createForeground();
		//translation to
		g2d.translate(this.x0, this.y0);
		g2d.translate(-this.w/2, -this.h/2);
		//actually draws the orbital
		if (this.back!=null) {
			g2d.drawImage(this.back, 0, 0, null);
			if (this.fore!=null) {
				g2d.drawImage(this.blur(this.fore), 0, 0, null);
			}
		}
		//translation back
		g2d.translate(this.w/2, this.h/2);
		g2d.translate(-this.x0, -this.y0);
	}

	//@Override
	public Color getColor() {
		return this.color;
	}

	//@Override
	public DessinMode getMode() {
		return null;
	}

	//@Override
	public TreeSet<JMenu> getPopupMenu(ActionListener al, int x, int y) {
		return null;
	}

	//@Override
	public Shape getShape() {
		Ellipse2D c = new Ellipse2D.Double(0, 0, 2 * this.h, 2 * this.h);
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

	/**
	 * @return the x0
	 */
	public int getX() {
		return this.x0;
	}

	/**
	 * @return the y0
	 */
	public int getY() {
		return this.y0;
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
	public void setLineWidth(int lw) {
	}

	//@Override
	public void setMode(DessinMode mode) {
	}

	/**
	 * @param y0
	 *            the y0 to set
	 */
	public void setR(int r) {
		this.h = r;
		this.w = r;
	}

	//@Override
	public void setSelected() {
	}

	/**
	 * @param x0
	 *            the x0 to set
	 */
	public void setX(int x0) {
		this.x0 = x0;
	}

	/**
	 * @param y0
	 *            the y0 to set
	 */
	public void setY(int y0) {
		this.y0 = y0;
	}

	//@Override
	public void unsetSelected() {
	}

	private BufferedImage blur(BufferedImage img) {
		if (img==null) return null;
		BufferedImage out = new BufferedImage(img.getWidth(null),
				img.getHeight(null), BufferedImage.TYPE_INT_ARGB_PRE);
		float data[] = { 0.0625f, 0.125f, 0.0625f, 0.125f, 0.25f, 0.125f,
				0.0625f, 0.125f, 0.0625f };
		Kernel kernel = new Kernel(3, 3, data);
		ConvolveOp convolve = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP,
				null);
		convolve.filter(img, out);
		return out;
	}

	private void createBackground() {
		if ((this.h==0)&&(this.w==0)) return;
		this.back = new BufferedImage(this.h, this.w,
				BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g2d = (Graphics2D) this.back.getGraphics();
		g2d.setColor(this.color);
		g2d.fillOval(0, 0, this.h, this.w);
	}

	private void createForeground() {
		if ((this.h==0)&&(this.w==0)) return;
		this.fore = new BufferedImage(this.h, this.w,
				BufferedImage.TYPE_INT_ARGB_PRE);
		Graphics2D g2d = (Graphics2D) this.fore.getGraphics();
		Point2D center = new Point2D.Float(this.h / 3, this.w / 3);
		float radius = (this.h + this.w) / 6;
		if (radius <= 0) return;
		Point2D focus = new Point2D.Float(this.h / 3, this.w / 3);
		float[] dist = { 0.0f, 1.0f };
		Color[] colors = { Color.WHITE, this.color };
		RadialGradientPaint p = new RadialGradientPaint(center, radius, focus,
				dist, colors, CycleMethod.NO_CYCLE);
		g2d.setPaint(p);
		g2d.setComposite(AlphaComposite.DstOver);
		g2d.fillOval(this.h / 5, this.w / 5, this.h / 3, this.w / 3);
	}

	private int getCenterX() {
		return this.getX() + (this.w / 2);
	}

	private int getCenterY() {
		return this.getY() + (this.h / 2);
	}

}