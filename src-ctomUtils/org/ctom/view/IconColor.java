/**
 * 
 */
package org.ctom.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.Icon;

/**
 * @author yannick
 *
 */
public class IconColor implements Icon{

	private int width = 32;
	private int height = 22;
	private Color color;

	/**
	 * @param color
	 */
	public IconColor(Color color) {
		super();
		this.color = color;
	}

	public IconColor(Color color, float transparency) {
		super();
		int r=color.getRed();
		int g=color.getGreen();
		int b=color.getBlue();
		this.color = new Color((float)r/255,(float)g/255,(float)b/255, transparency);
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();

		g2d.setColor(this.getColor());
		g2d.fillRect(x +1 ,y + 1,width -2 ,height -2);

		g2d.setColor(Color.BLACK);
		g2d.drawRect(x +1 ,y + 1,width -2 ,height -2);

		g2d.setColor(Color.RED);

		g2d.dispose();
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	public int getIconWidth() {
		return width;
	}

	public int getIconHeight() {
		return height;
	}
}
