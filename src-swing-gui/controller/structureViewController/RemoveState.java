package controller.structureViewController;

import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.IMoleculeComponent;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.IMoleculeComponentException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;
import views.StructureView;
import controller.globalCommands.EnableActionManager;
/***
 * this class describes the mouse events in the molecule panel if the remove button is selected
 * 
 * @author CTOM
 *
 */
public class RemoveState implements IStructureViewControllerState {

	
	public RemoveState(){

	}

	//@Override
	public void mouseClicked(StructureViewController structureViewController, Point2D p) {
	
		StructureView structureView = structureViewController.getStructureView();
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("SkelMouseController");
	
		IMoleculeComponent c = structureView.getMoleculeComponent(p);
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		if (c== null) 
			return;
		try {
			if (c instanceof Bond) {
				Bond b = (Bond) c;
				if (b.contains(PeriodicTable.Element.H))
					app.getStatusBar().setTxtState(
							bundle.getString("removeHBond"));
				else {
					app.getStateManager().initNewState();
					SwingIO.log(this.getClass().getName(), "mouseClicked", "remove bond " + b );
					b.remove();
					b = null;
					app.getStateManager().saveState();

				}
			} else if (c instanceof Atom) {
				Atom a = (Atom) c;
				if ((a.getElement() == PeriodicTable.Element.H))
					app.getStatusBar().setTxtState(
							bundle.getString("removeHAtom"));
				else {
					app.getStateManager().initNewState();
					SwingIO.log(this.getClass().getName(), "mouseClicked", "remove atom " + a );
					a.remove();
					a = null;
					EnableActionManager.getInstance().updateActionsToEnable(app);
					app.getStateManager().saveState();
				}
			}

		} catch (IMoleculeComponentException e) {
			SwingIO.error(this.getClass().getName(), "mouseClicked", e
					.getMessage(), e);
			SwingIO.reportError(app);
		} catch (MoleculeTooManyNeighboursException e) {
			SwingIO.error(this.getClass().getName(), "mouseClicked", e
					.getMessage(), e);
			SwingIO.reportError(app);
		} catch (MoleculeCoherenceException e) {
			SwingIO.error(this.getClass().getName(), "mouseClicked", e
					.getMessage(), e);
			SwingIO.reportError(app);
		} catch (MoleculeBondExistsException e) {
			SwingIO.error(this.getClass().getName(), "mouseClicked", e
					.getMessage(), e);
			SwingIO.reportError(app);
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
