package controller.structureViewController;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.IHuckelObject;
import org.ctom.hulis.huckel.IMoleculeComponent;
import org.ctom.hulis.huckel.OrbitaleAtomique;
import org.ctom.hulis.huckel.OrbitaleMoleculaire;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.prefs.Preferences;
import org.ctom.hulis.prefs.Preferences.DISPLAY_ORBITAL_TYPE;

import views.FrameApp;
import views.OrbitalDrawer;
import views.StructureView;

/**
 * This class describes the mouse actions in the molecule panel
 * 
 * @author Nicolas GOUDARD
 * 
 */
public class StructureViewController implements MouseListener, MouseMotionListener {

	
	StructureView structureView;
	Structure structure;

	
	/**
	 * Constructor
	 * 
	 */
	public StructureViewController(StructureView structureView, Structure structure) {
		this.structureView = structureView;
		this.structure = structure;
		
	}

	public StructureView getStructureView() {
		return this.structureView;
		
	}

	public Structure getStructure() {
		return this.structure;
		
	}

	/**
	 * This method is called when the user click on the mouse button
	 * 
	 * @param e :  mouse event
	 */

	public void mouseClicked(MouseEvent e) {
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		IStructureViewControllerState state = app.getCurrentStructureViewControllerState();

		if (state != null)
			state.mouseClicked(this, e.getPoint());
	}

	/**
	 * this method is called when the user drags the mouse
	 * pressing the button
	 * 
	 * @param e :   mouse event
	 */
	public void mouseDragged(MouseEvent e) {
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		IStructureViewControllerState state = app.getCurrentStructureViewControllerState();

		if (state != null)
			state.mouseDragged(this, e.getPoint());

	}

	/**
	 * this method is called when the mouse entered in the panel
	 * pressing the button
	 * 
	 * @param e :   mouse event
	 */
	public void mouseEntered(MouseEvent e) {
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		IStructureViewControllerState state = app.getCurrentStructureViewControllerState();

		if (state != null)
			state.mouseEntered(this, e.getPoint());
	}
	
	/**
	 * this method is called when the mouse exited the panel
	 * pressing the button
	 * 
	 * @param e :   mouse event
	 */
	public void mouseExited(MouseEvent e) {
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		IStructureViewControllerState state = app.getCurrentStructureViewControllerState();

		if (state != null)
			state.mouseExited(this, e.getPoint());
	}
	
	/**
	 * this method is called when the mouse moved
	 * pressing the button
	 * 
	 * @param e :   mouse event
	 */
	public void mouseMoved(MouseEvent e) {
		Point2D p = e.getPoint();
		this.setToolTipText(p);
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		IStructureViewControllerState state = app.getCurrentStructureViewControllerState();

		if (state != null)
			state.mouseMoved(this, p);


	}

	/**
	 * this method is called when the user presses on the mouse
	 * button
	 * 
	 * @param e :    mouse event
	 */
	public void mousePressed(MouseEvent e) {
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		IStructureViewControllerState state = app.getCurrentStructureViewControllerState();

		if (state != null)
			state.mousePressed(this, e.getPoint());


	}

	/**
	 *  this method is called when the user releases the mouse
	 * button
	 * 
	 * @param e :  mouse event
	 */

	public void mouseReleased(MouseEvent e) {
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		IStructureViewControllerState state = app.getCurrentStructureViewControllerState();

		if (state != null)
			state.mouseReleased(this, e.getPoint());
	}


	public void setToolTipText (Point2D p) {
		Structure structure = structureView.getStructure();
	
		String tooltipText = "";
		tooltipText = "<html><p style=\"font-family: 'Arial';font-size: 10;\">";

		if (this.structureView.getShownOrbital() != null) {
			OrbitaleAtomique orbs[] = structure.getOAs();
			OrbitaleMoleculaire  spinOrb = structureView.getShownOrbital();

			int size=orbs.length;

			OrbitaleAtomique orb=null;
			double coeff=0.0;
			boolean found= false;
			for (int i=0; i<size; i++) {
				orb = orbs[i];
				coeff=spinOrb.getCoeff(i);

				if (new OrbitalDrawer(structureView, orb, coeff).isContained(p)) {
					found = true;
					break;
				}
			}

 
			if (orb != null && found) {
				HuckelAtom a = orb.getHuckelAtom();
				tooltipText += "C<sub>" + a.toString() + "</sub> = "
				+ FrameApp.form2.format(coeff);
				if (Preferences.DISPLAY_ORBITAL==DISPLAY_ORBITAL_TYPE.NATURAL) {
					tooltipText += " c2: " + FrameApp.form2.format(coeff*coeff);
				}
				tooltipText += "<p></html>";
				this.structureView.setToolTipText(tooltipText);
			}
			else
				this.structureView.setToolTipText(null);
		}
		else {
			IMoleculeComponent component = this.structureView
			.getMoleculeComponent(p);
	
			if (component != null)  {
				
				tooltipText += component.toString();
				if (component instanceof IHuckelObject) {
					IHuckelObject huckelobj = (IHuckelObject) component;
					tooltipText += "<br>" + huckelobj.getNameParamHuckelHTML()
					+ " = " + huckelobj.getParamHuckel();
				}
				tooltipText += "<p></html>";
				this.structureView.setToolTipText(tooltipText);
			}
			else 
				this.structureView.setToolTipText(null);
		}

	}
	
}


