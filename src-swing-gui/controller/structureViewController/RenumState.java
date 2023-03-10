package controller.structureViewController;

import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.HuckelAtom;

import controller.globalCommands.EnableActionManager;
import util.io.SwingIO;
import views.StructureView;

/***
 * this class describes the mouse events in the molecule panel if the renum button is selected
 * 
 * @author CTOM
 *
 */
public class RenumState implements IStructureViewControllerState {

	public RenumState(){

	}
	
	//@Override
	public void mouseClicked(StructureViewController structureViewController, Point2D p) {
		SwingIO.log(this.getClass().getName(), "mouseClicked", "click to remove renum atom" );
		StructureView structureView = structureViewController.getStructureView();
		Atom a;
		if ( ((a = structureView.getAtom(p)) != null) && (a instanceof HuckelAtom)) {
			SwingIO.log(this.getClass().getName(), "mouseClicked", "renum atom " + ((HuckelAtom) a) );
			((HuckelAtom) a).autoSeqNum();
			SwingIO.log(this.getClass().getName(), "mouseClicked", "new atom number : " + ((HuckelAtom) a) );
		}
		EnableActionManager.getInstance().updateActionsToEnable(structureView.getMesomeryView().getFrameApp());

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
