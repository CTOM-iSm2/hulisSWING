/**
 * 
 */
package org.ctom.view;

import java.awt.BasicStroke;
import java.awt.Color;

/**
 * @author yannick
 *
 */
public enum StdLineType {
	
	TYPE_NORMAL(new BasicStroke(1.0f)),
	TYPE_BOLD(new BasicStroke(2.0f)),
	TYPE_SELECTED(new BasicStroke(2.0F, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0F, new float[] {10.0F, 3.0F, 3.0F, 3.0F}, 0.F));

	/** L'attribut qui contient la valeur associé à l'enum */
	private final BasicStroke stroke;

	/** Le constructeur qui associe une valeur à l'enum */
	private StdLineType(BasicStroke stroke) {
		this.stroke = stroke;
	}

	/** La méthode accesseur qui renvoit la valeur de l'enum */
	public BasicStroke getStroke() {
		return this.stroke;
	}
}
