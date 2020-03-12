package controller.structureViewController;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.CoupleLocal;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;

import util.io.SwingIO;
import views.FrameApp;
import views.Selection;
import views.StructureView;
import controller.globalCommands.ActionStopConsumingProcess;
/***
 * this class describes the mouse events in the molecule panel if the couple button is selected
 * 
 * @author CTOM
 *
 */
public class CoupleState implements IStructureViewControllerState {
	private HuckelAtom atomToCouple;

	public CoupleState(){
		atomToCouple = null;
	}

	//@Override
	public void mouseClicked(StructureViewController structureViewController, Point2D p) {
		// TODO Auto-generated method stub

	}

	//@Override
	public void mouseDragged(StructureViewController structureViewController, Point2D p) {
		StructureView structureView = structureViewController.getStructureView();
		if (atomToCouple == null) return;
		Line2D ligne = new Line2D.Double(structureView.getImagePoint(structureView.getPoint2D(atomToCouple.getLocation())),  p);
		structureView.setSelection(new Selection(ligne,StructureView.COUPLE_COLOR, StructureView.COUPLE_LINE_STROKE ));
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
		final FrameApp app = structureView.getMesomeryView().getFrameApp();
		Atom a = structureView.getAtom(p);

		if (!(a instanceof HuckelAtom))
			return;
		final HuckelAtom huckelAtom = (HuckelAtom) a;

		final Structure structure = structureViewController.getStructure();

		if ((structure instanceof StructureLocalized)
				&& (atomToCouple == null) && (a != null)) {
			if (huckelAtom.getNbRadR() == 1) {
				SwingIO.log(this.getClass().getName(), "mousePressed", "remove current couple");
				if ((structure.getMesomeryParent() != null) && (structure.getMesomeryParent().isCalculatingWeights())) {
					Thread stop = ActionStopConsumingProcess.stopCalculateWeights(app);
					try {
						stop.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				 new Thread(new Runnable() {

					public void run() {
						StructureLocalized s = (StructureLocalized) structure;
						s.removeCouple();
						atomToCouple = huckelAtom;
					}}).start();


			}
		}		
	}

	//@Override
	public void mouseReleased(StructureViewController structureViewController, Point2D p) {
		final StructureView structureView = structureViewController.getStructureView();
		final FrameApp app = structureView.getMesomeryView().getFrameApp();
		try {
			if (atomToCouple != null) {
				
				Structure structure = structureViewController.getStructure();
				Atom a = structureView.getAtom(p);
				if (a != null) {
					if (!(a instanceof HuckelAtom)) {
						structureView.setSelection(null);
						atomToCouple=null;
						structureView.repaint();
						return;
					}
						
					final HuckelAtom hAt = (HuckelAtom) a;
					if ((structure instanceof StructureLocalized)
							&& (hAt.getNbRadR() == 1)) {
						
						final StructureLocalized s = (StructureLocalized) structure;
						if (!s.areCoupled((HuckelAtom) atomToCouple, hAt)
								&&((HuckelAtom) atomToCouple!= hAt)) {
							SwingIO.log(this.getClass().getName(), "mouseReleased", "create couple between " + (HuckelAtom) atomToCouple + " and " + hAt );
							new Thread(new Runnable() {

								public void run() {
									try {
										System.out.println("atomToCouple" + atomToCouple );
										System.out.println("autre " + hAt );
										structureView.setSelection(null);
										s.setCouple(new CoupleLocal(
												(HuckelAtom) atomToCouple, hAt));
										atomToCouple = null;			
										
									} catch (CoupleException e) {
										SwingIO.error(this.getClass().getName(), "mouseReleased", e
												.getMessage(), e);
										SwingIO.reportError(app);;
									}						
									catch (Exception e) {
										SwingIO.error(this.getClass().getName(), "mouseReleased",
												e.getMessage(), e);
										SwingIO.reportError(app);
									}
								}}).start();

						}
					}
				}else {
					
					structureView.setSelection(null);
					atomToCouple=null;
					structureView.repaint();
					return;
					
				}

			}
		}
		catch (Exception e) {
			SwingIO.error(this.getClass().getName(), "coupleTo", e
					.getMessage(), e);
			SwingIO.reportError(app);

		}	
	}

}
