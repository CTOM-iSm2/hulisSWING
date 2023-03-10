package controller.graphOM;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import org.ctom.hulis.huckel.MonoExcitation;
import org.ctom.hulis.huckel.OrbitaleMoleculaire;
import org.ctom.hulis.huckel.SpinOrbitaleMoleculaire;
import org.ctom.hulis.huckel.exception.MonoExcitationException;
import org.ctom.hulis.prefs.Preferences;
import org.ctom.hulis.prefs.Preferences.DISPLAY_ORBITAL_TYPE;

import access.Rights;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.EnergyControl;
import views.GraphOM;
import views.OrbitalControl;

public class GraphOMController implements MouseListener, MouseMotionListener {
	public final static boolean ALLOW_EXCITATIONS = Rights.ALLOW_EXCITATIONS ; // Activer / Desactiver les excitations

	GraphOM graphOM;
	private MonoExcitation excitation = null;
	private SpinOrbitaleMoleculaire som_ex; // excitation : deplacement NG

	public GraphOMController(GraphOM graphOM) {
		this.graphOM = graphOM;
	}

	public void mouseClicked(MouseEvent e) {
		SwingIO.log(this.getClass().getName(), "mouseClicked", "graph MO click");

		Point2D p = e.getPoint();
		graphOM.setToolTipText(null);
		graphOM.setEOrbSelected(null);
		OrbitaleMoleculaire sav = graphOM.getStructureView().getShownOrbital();
		graphOM.getStructureView().setShownOrbital(null);
		for (OrbitalControl ec : graphOM.getOrbitalControls()) {

			if (ec.contains(p)) {

				if (sav == ec.getOM()) {
					graphOM.setEOrbSelected(null);
					graphOM.getStructureView().setShownOrbital(null);
					break;
				}
				graphOM.setEOrbSelected(ec.getOM());
				graphOM.getStructureView().setShownOrbital(ec.getOM());
				break;
			}
		}
		graphOM.repaint();
		graphOM.getStructureView().repaint();
	}

	public void mouseDragged(MouseEvent e) {
		if (ALLOW_EXCITATIONS && this.graphOM.getPointFrom() != null &&  (this.graphOM.getStructure().getMesomeryParent().countAllSructs()==1) ) {
			this.graphOM.setCurrentPointDragged (e.getPoint());
			this.graphOM.setDrawingExcitation (true); // NG MODIF
			this.graphOM.repaint();
		}
	}

	public void mouseEntered(MouseEvent e) {
		mouseMoved(e);
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		Point2D p = e.getPoint();
		this.graphOM.setToolTipText(null);
		for (OrbitalControl ec : this.graphOM.getOrbitalControls()) {
			if (ec.contains(p)) {
				// setToolTipText(ec.getEOrb().getValue().toString());
				MyResourceBundle bundle = LanguageManager.getInstance().getResource("SkelMouseController");
				this.graphOM.setToolTipText("<html><p style=\"font-family: 'Arial';font-size: 10;\">"
						+ ec.getOM().toHtml() + "</p></html>");
				break;
			}
		}

	}

	public void mousePressed(MouseEvent e) {
		SwingIO.log(this.getClass().getName(), "mousePressed", "graph MO press");
		if (Preferences.DISPLAY_ORBITAL==DISPLAY_ORBITAL_TYPE.CANONICAL) {
			// si on desine deja une excitation ou excitation deja cree ou structure localisee existe: abandon
			if ((!ALLOW_EXCITATIONS) || this.graphOM.isDrawingExcitation() ||  (this.excitation != null) ||   (this.graphOM.getStructure().getMesomeryParent().countAllSructs()>1))
				return; 

			Point2D p = e.getPoint();
			this.graphOM.setToolTipText(null);


			ArrayList<EnergyControl> potentialEC= this.graphOM.getOccupiedEnergyControlAt(p);
			if (potentialEC.size()>0) {
				System.out.print("Excitation BEGIN\n");

				EnergyControl ec=potentialEC.get(0); 
				/* take the first object of the list :
				 * there is no criterion regarding which one to take
				 */
				this.excitation = new MonoExcitation(this.graphOM.getStructure());
				this.som_ex = ec.getSOM(); // orbitale de depart de l'excitation
				this.graphOM.setPointFrom(ec.getPoint()); // on initialise le point de
				// depart du trait d'excitation				
			}
		}

	}

	public void mouseReleased(MouseEvent e) {
		SwingIO.log(this.getClass().getName(), "mouseReleased", "graph MO release");
		System.out.print("Excitation END\n");
		if ( (!ALLOW_EXCITATIONS) || (this.excitation == null) || (this.graphOM.getStructure().getMesomeryParent().countAllSructs()>1))
			return;

		if (this.graphOM.isDrawingExcitation()) {
			Point2D p = e.getPoint();
			this.graphOM.setToolTipText(null);
			ArrayList<EnergyControl> potentialEC= this.graphOM.getVirtualEnergyControlAt(p);
			if (potentialEC.size()>0) {
				EnergyControl ec=potentialEC.get(0); 
				/* take the first object of the list :
				 * there is no criterion regarding which one to take
				 */
				try {
					this.excitation.setFrom(this.som_ex);
					this.excitation.setTo(ec.getSOM());
					this.excitation.check();
				} catch (MonoExcitationException e1) {
					SwingIO.warning(this.getClass().getName(), "mouseReleased", e1
							.getMessage(), e1);
					JOptionPane.showMessageDialog(this.graphOM, e1.getMessage());
				}

				System.out.print("Excitation STOP\n");

			}
			this.graphOM.repaint();
		}
		this.graphOM.setDrawingExcitation(false);
		this.graphOM.setPointFrom(null);
		this.excitation = null;
	}

}
