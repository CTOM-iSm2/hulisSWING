package util;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.JFrame;

import views.FrameApp;

/**
 * ComponentsTools : This class contains tools to display unicode characters
 * (greek)
 * 
 * @version 1.0
 * @since unicode
 * @author Nicolas GOUDARD
 */

public class ComponentsTools {

	/**
	 * 
	 * @param component
	 * @return
	 */
	public static Point2D getCenter(Component component) {
		Point p = ComponentsTools.getLocationOnScreen(component);
		double width = component.getWidth();
		double height = component.getHeight();
		if (width == 0)
			width = component.getPreferredSize().getHeight();
		if (height == 0)
			height = component.getPreferredSize().getHeight();
		return new Point2D.Double(p.getX() + width / 2, p.getY() + height / 2);
	}

	/**
	 * 
	 * @param component
	 * @return
	 */
	public static JFrame getParent(Component component) {
		JFrame frame = null;
		Container container = component.getParent();
		while (container != null) {
			if (container instanceof JFrame) {
				frame = ((JFrame) container);
				break;
			}
			container = container.getParent();
		}
		return frame;
	}

	/**
	 * 
	 * @param c
	 * @param res
	 */
	public static void setExternalFont(Component c, String res) {
		try {
			InputStream in = c.getClass().getResourceAsStream(res);
			Font font = null;
			font = Font.createFont(Font.TRUETYPE_FONT, in);
			font = font.deriveFont(0, c.getFont().getSize());
			c.setFont(font);
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 *  return the center of the screen 
	 * @return Point2D : screen0 x center,  screen0 y center 
	 */
	public static Point2D getScreenCenter () {
		boolean forceCenterRelativeToFirstScreen = false;
		
		double screenCenterX = 0;
		double screenCenterY = 0;
		if (forceCenterRelativeToFirstScreen) {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();   
			GraphicsDevice[] gd = ge.getScreenDevices();
			GraphicsDevice screen = gd[0]; //on suppose qu'il y a au moins un ecran 
			Rectangle bounds = screen.getDefaultConfiguration().getBounds(); // on suppose que l'ecran a une config non nulle 
			screenCenterX = bounds.x + bounds.width /2 ; 
			screenCenterY = bounds.y + bounds.height/2 ;
	
		}
		else { // attention bug jdk7 opensuse 13.1 : si 2 ecran, les deux sont consideres comme un seul ecran
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			screenCenterX = screenSize.width /2 ;
			screenCenterY = screenSize.height / 2;	
		}
		return new Point2D.Double(screenCenterX, screenCenterY);

	}

	/**
	 * 
	 * @param component
	 * @return
	 */
	public static Point getLocationOnScreen(Component component) {
		Point p =  component.getLocationOnScreen();
		//SwingUtilities.convertPointToScreen(p, component.getParent());
		return p;
	}

	/**
	 * Center the component compToCenter in the component inOther
	 * @param compToCenter
	 * @param inOther
	 */
	public static void center(Component compToCenter, Component inOther) {
		compToCenter.setLocation((int) ComponentsTools.getLocationOnScreen(inOther).getX() + inOther.getWidth() / 2 - (compToCenter.getWidth() / 2), 
				(int) ComponentsTools.getLocationOnScreen(inOther).getY() + inOther.getHeight() / 2 - (compToCenter.getHeight() / 2));		
	}

	/**
	 * center the component in the screen
	 * @param component
	 */
	public static void centerToScreen(Component component) {
		Point2D screenCenter = ComponentsTools.getScreenCenter();

		component.setLocation((int)screenCenter.getX() - (component.getWidth() / 2),
				(int)screenCenter.getY() - (component.getHeight() / 2));
		
	}

}
