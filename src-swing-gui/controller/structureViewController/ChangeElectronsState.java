package controller.structureViewController;

import java.awt.geom.Point2D;

import javax.swing.JOptionPane;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.exception.HuckelAtomException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;
import views.StructureView;
import controller.globalCommands.ActionStopConsumingProcess;
/***
 * this class describes the mouse events in the molecule panel if the Change bond /electrons button is selected
 * 
 * @author CTOM
 *
 */
public class ChangeElectronsState implements IStructureViewControllerState {
	public ChangeElectronsState(){

	}
	
	
	//@Override
	public void mouseClicked(StructureViewController structureViewController, Point2D p) {

		StructureView structureView = structureViewController.getStructureView();
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("SkelMouseController");
		final FrameApp app = structureView.getMesomeryView().getFrameApp();
		final Structure structure = structureViewController.getStructure();
		
		if (!(structure instanceof StructureLocalized)) 
			return;

			
		if (structureView.isAtom(p))
		{
			
			try {

				Atom a = structureView.getAtom(p);
				if (a == null)
					return;
				
				// impossible de mmodifier un atome d'hydrogene
				if (!(a instanceof HuckelAtom)) {
					app.statusBar.setTxtState(bundle.getString("HRadR"));
					return;
				}
				final HuckelAtom hAt = (HuckelAtom) a;
				// ipossible de modifier un atome qui appartien a un bloc delocalise
				if (hAt.getBlocDelocalizedParent() != null) return;

				try { 
					if ((structure.getMesomeryParent() != null) && (structure.getMesomeryParent().isCalculatingWeights())) {
						Thread stop = ActionStopConsumingProcess.stopCalculateWeights(app);
						stop.join();
					}
				
					new Thread(new Runnable() {

						public void run() {
							app.getStateManager().initNewState();
							int nRadR = hAt.countRadR();
							if (nRadR == HuckelAtom.MAX_RAD_R)
								nRadR = 0;
							else
								nRadR++;
							SwingIO.log(this.getClass().getName(), "mouseClicked", "set " + nRadR + "electrons on " + hAt);
							try {
								hAt.setRadR(nRadR);
							} catch (ArithmeticException e) {
								JOptionPane.showMessageDialog(app, "Cannot compute weight : Exceed valence", "HLP", JOptionPane.WARNING_MESSAGE);
								SwingIO.error(this.getClass().getName(), "mouseClicked",
										e.getMessage(), e);							
							} catch (HuckelAtomException e) {
								SwingIO.error(this.getClass().getName(), "mouseClicked",
										e.getMessage(), e);
								SwingIO.reportError(app);
							}
							catch (Exception e) {
								SwingIO.error(this.getClass().getName(), "mouseClicked",
										e.getMessage(), e);
								SwingIO.reportError(app);
							}
							app.getStateManager().saveState();					
						}
					}).start();

				} catch (ArithmeticException e) {
					structure.getMesomeryParent().forceCalculatingWeights(false);							
					JOptionPane.showMessageDialog(app, "Cannot compute weight : Exceed valence", "HLP", JOptionPane.WARNING_MESSAGE);
					SwingIO.error(this.getClass().getName(), "mouseClicked",
							e.getMessage(), e);
					
				} catch (Exception e) {
					SwingIO.error(this.getClass().getName(), "mouseClicked",
							e.getMessage(), e);
					SwingIO.reportError(app);
				}
				
	
			} catch (Exception e) {
				SwingIO.error(this.getClass().getName(), "mouseClicked", e
						.getMessage(), e);
				SwingIO.reportError(app);
			}

		}
		else if (structureView.isBond(p))
		 {
			
			Bond l = structureView.getBond(p);
			try {
				if (l== null) 
					return;
				
				if (! (l instanceof HuckelBond)) {
					app.statusBar.setTxtState(bundle
							.getString("changeHBond"));
					return;
				}
					
				final HuckelBond l2 = (HuckelBond) l;
				// impossible de modifier une liaison qui appartien a un bloc delocalise
				if (l2.getBlocDelocalizedParent() != null) return;
				if ((structure.getMesomeryParent() != null) && (structure.getMesomeryParent().isCalculatingWeights())) {
					Thread stop = ActionStopConsumingProcess.stopCalculateWeights(app);
					stop.join();
				}
				
					
				new Thread(new Runnable() {

					public void run() {
						app.getStateManager().initNewState();
						try {
							l2.setBondType(l2.getBondType() % HuckelBond.MAX_TYPE_BOND
									+ 1);
						} catch (ArithmeticException e) {
							structure.getMesomeryParent().forceCalculatingWeights(false);							

							JOptionPane.showMessageDialog(app, "Cannot compute weight : Exceed valence", "HLP", JOptionPane.WARNING_MESSAGE);
							SwingIO.error(this.getClass().getName(), "mouseClicked",
									e.getMessage(), e);
						} catch (HuckelBondException e) {
							SwingIO.error(this.getClass().getName(), "mouseClicked",
									e.getMessage(), e);
						}
						catch (Exception e) {
							SwingIO.error(this.getClass().getName(), "mouseClicked",
									e.getMessage(), e);
							SwingIO.reportError(app);
						}
						SwingIO.log(this.getClass().getName(), "mouseClicked", "set " + l2.getBondType() + "electrons on " + l2);
						app.getStateManager().saveState();
						
					}}).start();
				
				

			
			
			} catch (Exception e) {
				SwingIO.error(this.getClass().getName(), "mouseClicked",
						e.getMessage(), e);
				SwingIO.reportError(app);
			}
		}
	}

	//@Override
	public void mouseDragged(StructureViewController structureViewController, Point2D p) {
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mouseReleased(StructureViewController structureViewController, Point2D p) {
		// TODO Auto-generated method stub
		
	}

}
