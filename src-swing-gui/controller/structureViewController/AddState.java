package controller.structureViewController;

import java.awt.Toolkit;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.MoleculeAtomNullException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.util.maths.Point3D;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.BondDrawer;
import views.FrameApp;
import views.Selection;
import views.StructureView;
import controller.globalCommands.EnableActionManager;
import controller.globalCommands.ReplaceAtom;

/***
 * this class describes the mouse events in the molecule panel if the Add button is selected
 * 
 * @author CTOM
 *
 */
public class AddState implements IStructureViewControllerState  {

	private Atom atomToLink;

	public AddState(){
		this.atomToLink = null;
	}
	
	//@Override
	public void mouseClicked(StructureViewController structureViewController, Point2D p) {

		Structure structure = structureViewController.getStructure();
		StructureView structureView = structureViewController.getStructureView();
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		Point3D p3d = structureView.getPoint3D(structureView.getAntecedentPoint(p));

		if (!(structure instanceof StructureLocalized)) {
			Atom a = structureView.getAtom(p);
			if (a != null) { // si emplacement non libre
				if (a.getElement() == PeriodicTable.Element.H)
					try { // si H : remplace H par C
						app.getStateManager().initNewState();
						HuckelAtom carbon = (HuckelAtom) PeriodicTable.getNewAtom(PeriodicTable.Entry.C);
						SwingIO.log(this.getClass().getName(), "mouseClicked", "replace " + a + " by " + carbon);
						new ReplaceAtom(structure, a, carbon);
						EnableActionManager.getInstance().updateActionsToEnable(app);

						app.getStateManager().saveState();
					} catch (HuckelBondException e) {
						SwingIO.error(this.getClass().getName(), "mouseClicked",
								e.getMessage(), e);
						SwingIO.reportError(app);
					} catch (BondException e) {
						SwingIO.error(this.getClass().getName(), "mouseClicked",
								e.getMessage(), e);
						SwingIO.reportError(app);
					} catch (MoleculeAtomNullException e) {
						SwingIO.error(this.getClass().getName(), "mouseClicked",
								e.getMessage(), e);
						SwingIO.reportError(app);
					} catch (MoleculeTooManyNeighboursException e) {
						SwingIO.error(this.getClass().getName(), "mouseClicked",
								e.getMessage(), e);
						SwingIO.reportError(app);
					} catch (MoleculeCoherenceException e) {
						SwingIO.error(this.getClass().getName(), "mouseClicked",
								e.getMessage(), e);
						SwingIO.reportError(app);
					} catch (MoleculeBondExistsException e) {
						SwingIO.error(this.getClass().getName(), "mouseClicked",
								e.getMessage(), e);
						SwingIO.reportError(app);
				}
				else { // si deja un atome autre que H : bip
					Toolkit.getDefaultToolkit().beep();
				}
			} // else if a= null(clic sur emplacement libre)
			else {
				try {
					app.getStateManager().initNewState();
					Atom atom = structure.createNewAtom(PeriodicTable.Entry.C, p3d);
					SwingIO.log(this.getClass().getName(), "mouseClicked", "create atom " + (HuckelAtom) atom);
					EnableActionManager.getInstance().updateActionsToEnable(app);
					app.getStateManager().saveState();
				} catch (HuckelBondException e) {
					SwingIO.error(this.getClass().getName(), "mouseClicked",
							e.getMessage(), e);
					SwingIO.reportError(app);
				} catch (BondException e) {
					SwingIO.error(this.getClass().getName(), "mouseClicked",
							e.getMessage(), e);
					SwingIO.reportError(app);
				} catch (MoleculeTooManyNeighboursException e) {
					SwingIO.error(this.getClass().getName(), "mouseClicked",
							e.getMessage(), e);
					SwingIO.reportError(app);
				} catch (MoleculeCoherenceException e) {
					SwingIO.error(this.getClass().getName(), "mouseClicked",
							e.getMessage(), e);
					SwingIO.reportError(app);
				} catch (MoleculeBondExistsException e) {
					SwingIO.error(this.getClass().getName(), "mouseClicked",
							e.getMessage(), e);
					SwingIO.reportError(app);
				}

			}
		}
		
	}

	//@Override
	public void mouseDragged(StructureViewController structureViewController, Point2D p) {
		Structure structure = structureViewController.getStructure();
		StructureView structureView = structureViewController.getStructureView();
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		Point3D p3d = structureView.getPoint3D(structureView.getAntecedentPoint(p));
		if (atomToLink == null && structureView.getAtom(p)== null) {
			try {
				app.getStateManager().initNewState();
				SwingIO.log(this.getClass().getName(), "mouseDragged", "create C in dragging");
				this.atomToLink = structure.createNewAtom(PeriodicTable.Entry.C, p3d);
				EnableActionManager.getInstance().updateActionsToEnable(app);
				app.getStateManager().saveState();
			} catch (HuckelBondException e) {
				SwingIO.error(this.getClass().getName(), "mouseClicked",
						e.getMessage(), e);
				SwingIO.reportError(app);
			} catch (BondException e) {
				SwingIO.error(this.getClass().getName(), "mouseClicked",
						e.getMessage(), e);
				SwingIO.reportError(app);
			} catch (MoleculeTooManyNeighboursException e) {
				SwingIO.error(this.getClass().getName(), "mouseClicked",
						e.getMessage(), e);
				SwingIO.reportError(app);
			} catch (MoleculeCoherenceException e) {
				SwingIO.error(this.getClass().getName(), "mouseClicked",
						e.getMessage(), e);
				SwingIO.reportError(app);
			} catch (MoleculeBondExistsException e) {
				SwingIO.error(this.getClass().getName(), "mouseClicked",
						e.getMessage(), e);
				SwingIO.reportError(app);
			}
		}
			
		Line2D ligne = new Line2D.Double(structureView.getImagePoint(structureView.getPoint2D(atomToLink.getLocation())),  p);
		
		structureView.setSelection(new Selection(ligne,BondDrawer.BOND_COLOR, BondDrawer.BOND_LINE_STROKE ));
		structureView.repaint();
	
	}

	//@Override
	public void mouseEntered(StructureViewController structureViewController, Point2D p) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mouseExited(StructureViewController structureViewController, Point2D p) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mouseMoved(StructureViewController structureViewController, Point2D p) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mousePressed(StructureViewController structureViewController, Point2D p) {
		StructureView structureView = structureViewController.getStructureView();
		if (structureViewController.getStructure() instanceof StructureLocalized)
			return;

		Atom a = structureView.getAtom(p);

		if ((this.atomToLink == null)  && (a != null)) 
			this.atomToLink = a;

	}

	//@Override
	public void mouseReleased(StructureViewController structureViewController, Point2D p) {
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("SkelMouseController");
		StructureView structureView = structureViewController.getStructureView();
		Structure structure = structureViewController.getStructure();	
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		Point3D p3d = structureView.getPoint3D(structureView.getAntecedentPoint(p));
		if (this.atomToLink != null) {
			Atom a = structureView.getAtom(p);
			// s'il y a un atome pointe
			if (a != null) {

				if (!structure.areLinked(atomToLink, a)) { 
					// si les deux atomes ne sont pas lies  s'il n'y a pas deja 
					//le nb de voisins max pour les deux atomes
					
					if (!structure
							.isNbNeighboursMaxReachedExcludingH(atomToLink)
							&& !structure
									.isNbNeighboursMaxReachedExcludingH(a)) {
						try {
							app.getStateManager().initNewState();
							SwingIO.log(this.getClass().getName(), "mouseReleased", "create bond between " + atomToLink + " and " + a);
							structure.createNewBond(atomToLink, a);
							app.getStateManager().saveState();

						} catch (BondException e) {
							/*SwingIO.error(this.getClass().getName(), "mouseReleased",
									e.getMessage(), e);*/
	
						} catch (MoleculeTooManyNeighboursException e) {
							SwingIO.error(this.getClass().getName(), "mouseReleased",
									e.getMessage(), e);
							SwingIO.reportError(app);
						} catch (MoleculeCoherenceException e) {
							SwingIO.error(this.getClass().getName(), "mouseReleased",
									e.getMessage(), e);
							SwingIO.reportError(app);
						} catch (MoleculeBondExistsException e) {
							SwingIO.error(this.getClass().getName(), "mouseReleased",
									e.getMessage(), e);
							SwingIO.reportError(app);
						}
					} // fin si pas nb de voisns max sinon message
					else
						structureView
								.getMesomeryView()
								.getFrameApp()
								.getStatusBar()
								.setTxtState(
										bundle
												.getString("ktoomanyneighbours2"));



				} // fin : si les deux atomes ne sont pas deja lies...;

			} // fin si liaisons entre deux atomes existants

			// else if liaison dans le vide
			else {
				if (!structure
						.isNbNeighboursMaxReachedExcludingH(atomToLink)) {

					// cree un atome de carbone ou le curseur est dnas le vide
					try {
						app.getStateManager().initNewState();
						boolean savCalcFly = structure.isEnabledFlyCalculate();
						structure.stopFlyCalculate();
						structure.setEnabledNotify(false);
						SwingIO.log(this.getClass().getName(), "mouseReleased", "create atom C");
						Atom to = structure.createNewAtom(
								PeriodicTable.Entry.C, p3d);
						// l'atome
						// a
						// lier
						// et
						// l'atome
						// C
						// cree
						structure.setEnabledFlyCalculate(savCalcFly);
						structure.setEnabledNotify(true);
						SwingIO.log(this.getClass().getName(), "mouseReleased", "create bond between" + atomToLink + " and new atom" + to);

						structure.createNewBond(atomToLink, to); // lie
						
					
					} catch (HuckelBondException e) {
						SwingIO.error(this.getClass().getName(), "mouseReleased", e
								.getMessage(), e);
						SwingIO.reportError(app);
					} catch (BondException e) {
						SwingIO.error(this.getClass().getName(), "mouseReleased", e
								.getMessage(), e);
						SwingIO.reportError(app);
					} catch (MoleculeTooManyNeighboursException e) {
						SwingIO.error(this.getClass().getName(), "mouseReleased", e
								.getMessage(), e);
						SwingIO.reportError(app);
					} catch (MoleculeCoherenceException e) {
						SwingIO.error(this.getClass().getName(), "mouseReleased", e
								.getMessage(), e);
						SwingIO.reportError(app);
					} catch (MoleculeBondExistsException e) {
						SwingIO.error(this.getClass().getName(), "mouseReleased", e
								.getMessage(), e);
						SwingIO.reportError(app);
					}
					app.getStateManager().saveState();

				} // fin si pas le nb de voisin max
				else
					structureView.getMesomeryView().getFrameApp()
							.getStatusBar().setTxtState(
									bundle
											.getString("ktoomanyneighbours2"));

				// JOptionPane.showMessageDialog(structureView.getMesomeryView().getFrameApp(),bundle.getString("ktoomanyneighbours2"),
				// common.ksoftname, JOptionPane.INFORMATION_MESSAGE);

			} // fin else liaison dans le vide
			atomToLink = null;
			structureView.setSelection(null);
			structureView.repaint();
		} // is bond mode	
	}

	
	
}
