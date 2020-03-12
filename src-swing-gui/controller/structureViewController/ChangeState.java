package controller.structureViewController;

import java.awt.Point;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.IMoleculeComponent;

import util.MyResourceBundle;
import util.ThreadUtils;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.DialogChangeAtom;
import views.DialogChangeHuckelBond;
import views.FrameApp;
import views.StructureView;
/***
 * this class describes the mouse events in the molecule panel if the Change button is selected
 * 
 * @author CTOM
 *
 */
public class ChangeState implements  IStructureViewControllerState {

	public ChangeState(){
	}
	
	//@Override
	public void mouseClicked(StructureViewController structureViewController, Point2D p) {
		StructureView structureView = structureViewController.getStructureView();
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("SkelMouseController");
	
		IMoleculeComponent moleculeComponent = structureView.getMoleculeComponent(p);

		if (moleculeComponent == null)
			return;

		FrameApp app = structureView.getMesomeryView().getFrameApp();

		Point pos = structureView.getMousePointerLocationOrRelativeCoordinatesOnScreeen(moleculeComponent.getLocation());

		try {
			if (moleculeComponent instanceof Atom) {
				SwingIO.log(this.getClass().getName(), "mouseClicked", "open dialog box to change atom : " + (Atom) moleculeComponent );

				final DialogChangeAtom dialog= new DialogChangeAtom(app, pos, (Atom) moleculeComponent);
				 // this solves the problem where the dialog was not getting
				 // focus the second time it was displayed
				 //http://www.devdaily.com/blog/post/jfc-swing/input-focus-dialog-jdialog-java-swing-problem-solution
				ThreadUtils.invokeAsSoonAsPossible(new Runnable() 
				  {  
				    public void run() {  
				    	dialog.requestFocusInWindow();
				    }
				  });
				dialog.setModal();
				dialog.setVisible(true);
			}

			if (moleculeComponent instanceof HuckelBond) {
				SwingIO.log(this.getClass().getName(), "mouseClicked", "open dialog box to change bond " + (HuckelBond) moleculeComponent );
				final DialogChangeHuckelBond dialog = new DialogChangeHuckelBond(app, pos,
						(HuckelBond) moleculeComponent);
				// this solves the problem where the dialog was not getting
				 // focus the second time it was displayed
				 //http://www.devdaily.com/blog/post/jfc-swing/input-focus-dialog-jdialog-java-swing-problem-solution
				ThreadUtils.invokeAsSoonAsPossible(new Runnable() 
				  {  
				    public void run() {  
				    	dialog.requestFocusInWindow();
				    }
				  });
				dialog.setModal();
				dialog.setVisible(true);
			}
			
			
			
			if (moleculeComponent instanceof Bond) // non hukel bond
				app.getStatusBar().setTxtState(
						bundle.getString("changeHBond"));

		} catch (Exception e) {
			SwingIO.error(this.getClass().getName(), "changeMolecule", e
					.getMessage(), e);
			SwingIO.reportError(app);
			;
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
