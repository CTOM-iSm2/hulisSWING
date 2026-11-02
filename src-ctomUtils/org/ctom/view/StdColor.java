/**
 * 
 */
package org.ctom.view;

import java.awt.Color;
import java.awt.SystemColor;

/**
 * @author yannick
 *
 */
public enum StdColor {

	White(Color.WHITE),
	Lightgray(Color.LIGHT_GRAY),
	Gray(Color.GRAY),
	Darkgray(Color.DARK_GRAY),
	Black(Color.BLACK),
	Red(Color.RED),
	Orange(Color.ORANGE),
	Yellow(Color.YELLOW),
	Green(Color.GREEN),
	Cyan(Color.CYAN),
	Blue(Color.BLUE),
	SelectdColor(SystemColor.controlHighlight),
	HighLightColor(Color.PINK),
	Magenta(Color.MAGENTA),
	Pink(Color.PINK),
	NormalColor(Color.BLACK);
	/** L'attribut qui contient la valeur associée à l'enum */
	private final Color color;

	/** Le constructeur qui associe une valeur à l'enum */
	private StdColor(Color color) {
		this.color = color;
	}

	/** La méthode accesseur qui renvoit la valeur de l'enum */
	public Color getColor() {
		return this.color;
	}
}
