package controller.structureViewController;

import java.awt.geom.Point2D;

/**
 * This interface describes the mouse events to implements foreach applet state in a class by state
 * The states classes are managed by the StructureViewController class that redirect on the state class
 * depends on the applet state
 * 
 * 
 * Each method has a point2d in parameter. It is the image point touched (x', y') in the structure view with the pointer of the mouse
 * and not the real point (x, y) of the structure. In the structure view :
 * 
 * x' = (xmargin + x) * zoom  ->  x = x' / zoom -  xmargin
 * y' = (ymargin + y) * zoom  -> y = x' / zoom -  ymargin
 *
 * To get the antecedent of an image point, you can call : structureViewController.getStructureView().getAntecedentPoint(pImage);
 * So, to get image point of a real point, you can call : structureViewController.getStructureView().getImagePoint(pReal);
 * 
 * @author macbook
 *
 */
public interface IStructureViewControllerState {
	 public void mouseClicked(StructureViewController structureViewController, Point2D p);
	 public void mouseDragged(StructureViewController structureViewController, Point2D p);
	 public void mouseEntered(StructureViewController structureViewController, Point2D p);
	 public void mouseExited(StructureViewController structureViewController, Point2D p);
	 public void mouseMoved(StructureViewController structureViewController, Point2D p);
	 public void mousePressed(StructureViewController structureViewController, Point2D p);
	 public void mouseReleased(StructureViewController structureViewController, Point2D p);
}
