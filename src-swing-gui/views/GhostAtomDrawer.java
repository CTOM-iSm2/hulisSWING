package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import org.ctom.hulis.huckel.Atom;

import views.prefs.PreferencesGUI;

/**
 * This class contains methods for drawing an atom and select a bond on a GUI
 * 
 * @author YC
 * 
 */
public class GhostAtomDrawer {
	protected Atom atom;
	private final StructureView structureView;
	private  Shape atomShape = null;
	Color atomBorderColor = null;
	Color atomFillColor = null;
	AtomColor atomColor;
	double atomRadius = 0.0;
	String symbol;
	protected GhostAtomDrawer(StructureView structureView, Atom atom) {
		this.structureView = structureView;
		this.atom = atom;
		//String input ="";
		try {
			atomColor = AtomColor.valueOf(this.atom.getSigle());
			atomRadius =  atomColor.getRadius();
			atomBorderColor =  atomColor.getBorderColor();
			atomFillColor =  atomColor.getFillcolor();
			if (this.atom.getElement() == null)
				symbol  = this.atom.getSigle();
			else
				symbol = this.atom.getElement().toString();			
		}
		catch (IllegalArgumentException e) {
			atomColor = AtomColor.Xx;
			atomRadius = atomColor.getRadius();
			atomBorderColor =  atomColor.getBorderColor();
			atomFillColor =  atomColor.getFillcolor();
			symbol = this.atom.getSigle();
		}
	}



	/**
	 * This method draws an atom object
	 * 
	 * @param g2d :
	 *            graphics object
	 */
	protected void draw(Graphics2D g2d) {
		// dessine la bordure de l'atome 
		g2d.setStroke(new BasicStroke(1.0f));
		if (PreferencesGUI.getInstance().isDisplayAtomsCircles())
			g2d.setColor(atomBorderColor);
		else
			g2d.setColor(this.structureView.getBackground());

		g2d.draw(getForm());

		// dessine interieur de l'atome
		if (PreferencesGUI.getInstance().isDisplayAtomsCircles())
				g2d.setColor(atomFillColor);
			else
				g2d.setColor(this.structureView.getBackground());

		g2d.fill(getForm());

	}



	/**
	 * 
	 * @return
	 */
	protected Shape getForm() {
		if (this.atomShape != null)
			return this.atomShape;
					
		this.atomShape =  new Ellipse2D.Double( this.atom.getX()- atomRadius, this.atom.getY()- atomRadius, 2 * atomRadius, 2 * atomRadius);
		return this.atomShape;
	}

}