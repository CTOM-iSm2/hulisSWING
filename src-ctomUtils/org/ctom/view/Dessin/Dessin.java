package org.ctom.view.Dessin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;



public interface Dessin {
	
	public abstract void draw(Graphics2D g2d);
	public abstract Shape getShape();
	public abstract void setLineWidth(int lw);
	public abstract Color getColor();
	public abstract void setColor(Color col);
	public abstract DessinMode getMode();
	public abstract void setMode(DessinMode mode);
	public abstract void setSelected();
	public abstract void unsetSelected();

}
