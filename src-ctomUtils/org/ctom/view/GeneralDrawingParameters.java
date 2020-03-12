package org.ctom.view;

import java.awt.BasicStroke;
import java.awt.Stroke;

public class GeneralDrawingParameters {
	
	public static final Stroke SELECTED_STROKE = new BasicStroke(2);
	private static double RADIUS=10;
	private static double DELTAX=10;
	private static double DELTAY=20;
	private static double HEIGHT;
	/**
	 * @return the rADIUS
	 */
	public static double getRADIUS() {
		return RADIUS;
	}
	/**
	 * @return the dELTAX
	 */
	public static double getDELTAX() {
		return DELTAX;
	}
	/**
	 * @return the dELTAY
	 */
	public static double getDELTAY() {
		return DELTAY;
	}
	/**
	 * @param rADIUS the rADIUS to set
	 */
	public static void setRADIUS(double rADIUS) {
		RADIUS = rADIUS;
	}
	/**
	 * @param dELTAX the dELTAX to set
	 */
	public static void setDELTAX(double dELTAX) {
		DELTAX = dELTAX;
	}
	/**
	 * @param dELTAY the dELTAY to set
	 */
	public static void setDELTAY(double dELTAY) {
		DELTAY = dELTAY;
	}
	
	public static double getHEIGHT() {
		return HEIGHT;
	}

	public static void setHEIGHT(double hEIGHT) {
		HEIGHT=hEIGHT;
	}
	
}
