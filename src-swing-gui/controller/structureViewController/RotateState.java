package controller.structureViewController;

import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.util.maths.Point3D;
import org.ctom.util.maths.Rotation;
import org.ctom.util.maths.VecteurCTOM;

import util.io.SwingIO;
import views.FrameApp;
import views.StructureView;
/***
 * this class describes the mouse events in the molecule panel if the rotate button is selected
 * 
 * @author CTOM
 *
 */
public class RotateState implements IStructureViewControllerState{
	Point2D rotateFrom;
	
	public RotateState(){
		rotateFrom= null;
	}
	
	//@Override
	public void mouseClicked(StructureViewController structureViewController, Point2D p) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mouseDragged(StructureViewController structureViewController, Point2D p) {
		SwingIO.log(this.getClass().getName(), "mouseDragged", "drag to rotate molecule" );
		StructureView structureView = structureViewController.getStructureView();
		Point3D pOrigin = structureView.getPoint3D(rotateFrom);
		Point3D pDestination = structureView.getPoint3D(p);

		Structure structure = structureViewController.getStructure();
		
		Point3D imageCenter3D = structureView.getPoint3D(structureView.getImagePoint( structureView.getPoint2D(structure.getCenter())));
		VecteurCTOM CO = new VecteurCTOM (imageCenter3D, pOrigin);
		VecteurCTOM CD = new VecteurCTOM (imageCenter3D, pDestination);
		
		double sensAngle = Math.signum(CO.getCrossProduct(CD).getZ()); 
		double angle = CD.getAngle(CO);
		
		if (!Double.isNaN(angle) && ! Double.isNaN(sensAngle) && (angle != 0.0) && (sensAngle != 0)) {
			structure.rotate(Rotation.getRotationZ(angle * sensAngle));
			structureView.repaint();
		}
		rotateFrom = p;		
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
		rotateFrom = p;
	
	}

	//@Override
	public void mouseReleased(StructureViewController structureViewController, Point2D p) {
		StructureView structureView = structureViewController.getStructureView();
		FrameApp app = structureView.getMesomeryView().getFrameApp();
		rotateFrom = p;
		app.getStateManager().saveState();

		
	}

}
