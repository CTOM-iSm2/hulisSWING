package org.ctom.view.Dessin.Interactif;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.TreeSet;

import javax.swing.JMenu;

import org.ctom.view.Dessin.Dessin;
import org.ctom.view.Dessin.DessinMode;


public class Rectangle extends Rectangle2D implements Dessin, DessinInteractif {

	/*
	 * a finir
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	private DessinMode mode=DessinMode.NORMAL;
	private Color color=Color.BLACK;
	
	private Point2D pt0;
	private Point2D pt1;

	public Rectangle(Point2D pt0,Point2D pt1) {
		this.pt0=pt0;
		this.pt1=pt1;
		this.setColor(color);
	}

	//@Override
	public void setRect(double x, double y, double w, double h) {
		this.pt0.setLocation(x, y);
		this.pt1.setLocation(x+w, y+h);
	}

	//@Override
	public double getHeight() {
		return pt1.getY()-pt0.getY();
	}

	//@Override
	public double getWidth() {
		return pt0.getX()-pt1.getX();
	}

	//@Override
	public double getX() {
		return pt0.getX();
	}

	//@Override
	public double getY() {
		return pt0.getY();
	}

	//@Override
	public boolean isEmpty() {
		return false;
	}

	//@Override
	public ArrayList<String> getToolTipText(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public TreeSet<JMenu> getPopupMenu(ActionListener al, int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public void draw(Graphics2D g2d) {
		g2d.draw(this.getShape());
	}

	//@Override
	public Shape getShape() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public void setLineWidth(int lw) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public Color getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public void setColor(Color col) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public DessinMode getMode() {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public void setMode(DessinMode mode) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void setSelected() {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void unsetSelected() {
		// TODO Auto-generated method stub
		
	}
	//@Override
	public Rectangle2D createIntersection(Rectangle2D r) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public Rectangle2D createUnion(Rectangle2D r) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public int outcode(double x, double y) {
		// TODO Auto-generated method stub
		return 0;
	}


}
