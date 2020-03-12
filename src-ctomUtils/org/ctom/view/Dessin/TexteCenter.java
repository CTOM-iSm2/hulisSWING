package org.ctom.view.Dessin;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;

import org.ctom.view.StdColor;


public class TexteCenter implements Dessin {

	private Color color=Color.BLACK;

	/**
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	String str;
	double x;
	double y;
	private DessinMode mode=DessinMode.NORMAL;

	public TexteCenter(String string, double x, double y) {
		this.str=string;
		this.x=x;
		this.y=y;
	}

	//@Override
	public void draw(Graphics2D g2d) {
		FontRenderContext frc = g2d.getFontRenderContext();
		double string_W = g2d.getFont().getStringBounds(str, frc).getWidth();
		double string_H = g2d.getFont().getStringBounds(str, frc).getHeight();
		Color c_current = g2d.getColor();
		switch (this.getMode()) {
			case SELECTED:
				g2d.setColor(StdColor.SelectdColor.getColor());
				break;
			default:
				break;
		}
		g2d.drawString(str, (int)(x-string_W/2), (int)(y+string_H/2));
		g2d.setColor(c_current);
	}

	//@Override
	public Shape getShape() {
		return null;
	}

	//@Override
	public void setLineWidth(int lw) {
	}

	public String getStr() {
		return str;
	}

	//@Override
	public DessinMode getMode() {
		return this.mode;
	}

	//@Override
	public void setMode(DessinMode mode) {
		this.mode=mode;
	}

	public void unsetSelected() {
		this.mode=DessinMode.NORMAL;
	}

	//@Override
	public void setSelected() {
		this.mode=DessinMode.SELECTED;
	}


}
