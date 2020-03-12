package controller.structureViewController;

import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.Atom;
import org.ctom.util.maths.Point3D;

import util.io.SwingIO;
import views.FrameApp;
import views.MesomeryView;
import views.StructureView;

public class MoveState implements IStructureViewControllerState  {
	Atom atomToMove;
	Point2D moleculeMoveFrom;
	
	public MoveState(){
		atomToMove = null;
		moleculeMoveFrom = null;
	}

	
	//@Override
	public void mouseClicked(StructureViewController structureViewController, Point2D p) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mouseDragged(StructureViewController structureViewController, Point2D p) {
		StructureView structureView = structureViewController.getStructureView();
		double dx;
		double dy;
		Point3D p3d = structureView.getPoint3D(structureView.getAntecedentPoint(p));
		if (atomToMove != null) {
			SwingIO.log(this.getClass().getName(), "mouseDragged", "move atom " + this.atomToMove );

			this.atomToMove.setLocation(p3d);
			structureView.repaint();
		} else if (moleculeMoveFrom != null) {
			SwingIO.log(this.getClass().getName(), "mouseDragged", "move molecule" );
			dx = p.getX() - moleculeMoveFrom.getX();
			dy = p.getY() - moleculeMoveFrom.getY();
			MesomeryView mesomeryView = structureView.getMesomeryView();
			structureView.getMesomeryView().setXMargin(structureView.getXMargin() + dx);
			structureView.getMesomeryView().setYMargin(structureView.getYMargin() + dy);
			moleculeMoveFrom = p;
			mesomeryView.refresh();
		}		
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
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		app.getStateManager().initNewState();
		Atom a = structureView.getAtom(p);
		if (a != null) 
			this.atomToMove = a;
		else		
			this.moleculeMoveFrom = p;

	}

	//@Override
	public void mouseReleased(StructureViewController structureViewController, Point2D p) {
		StructureView structureView = structureViewController.getStructureView();
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		this.atomToMove = null;
		this.moleculeMoveFrom = null;
		app.getStateManager().saveState();
	
	}

}
