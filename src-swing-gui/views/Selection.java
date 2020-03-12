package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;

public class Selection {
	Shape shape;
	Color color;
	float stroke;
	
	public Shape getShape() {
		return shape;
	}
	
	public Selection (Shape shape, Color color, float stroke) {
		this.shape = shape;
		this.color = color;
		this.stroke = stroke;
	}
	
	public Selection (Rectangle2D shape, Color color, float stroke) {
		this.shape = shape;
		this.color = color;
		this.stroke = stroke;
	}
	
	public void draw (Graphics2D g2d) {
		g2d.setColor(color);
		g2d.setStroke(new BasicStroke(stroke));
		g2d.draw(shape);
	}
}
