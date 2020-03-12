package controller.structureViewController;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.IHuckelObject;
import org.ctom.hulis.huckel.IMoleculeComponent;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.BlocDelocalizedException;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.IncorrectNumberPiElectronsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.util.maths.Point3D;

import util.io.SwingIO;
import views.DialogChangeBloc;
import views.Selection;
import views.StructureView;
import controller.globalCommands.ActionEndBloc;
/***
 * this class describes the mouse events in the molecule panel if the bloc button is selected
 * 
 * @author CTOM
 *
 */
public class BlocState implements IStructureViewControllerState{
	
	Point2D pRectBegin;
	Point2D pRectEnd;
	BlocDelocalized currentBlocDelocalized;
	boolean savStructCalculateFly ;
	boolean savMesomeryCalculateFly ;
	
	public BlocState(){
		pRectBegin = null;
		pRectEnd = null;
		currentBlocDelocalized= null;
	}
	
	//@Override
	public void mouseClicked(StructureViewController structureViewController, Point2D p) {
		if (!ActionEndBloc.checkCurrentBlocInCurrentStructure(structureViewController.getStructureView().getMesomeryView().getFrameApp()))
			return;
		SwingIO.log(this.getClass().getName(), "mouseClicked", "create or modify bloc");
				
		StructureView structureView = structureViewController.getStructureView();
		Structure structure = structureViewController.getStructure();
		
		Point3D p3d = structureView.getPoint3D(p);
		IMoleculeComponent component = structureView.getMoleculeComponent(p);
	
		//  selectionner le bloc de l'object ou on clique ou creer un nouveau bloc
		currentBlocDelocalized = structureView.getSelectedBlocDelocalized();
		if ( currentBlocDelocalized == null){
			BlocDelocalized blocSelect = structureView.getBlocDelocalized(p);
			if (blocSelect != null){
				SwingIO.log(this.getClass().getName(), "mouseClicked", "open dialog change bloc");
				new DialogChangeBloc(structureView, structureView.getMousePointerLocationOrRelativeCoordinatesOnScreeen(p3d), blocSelect ).setVisible(true);
				return;
			}
			else
			{				
				if (!(component instanceof IHuckelObject))
					return;

				currentBlocDelocalized = new BlocDelocalized();
				structureView.setSelectedBlocDelocalized(currentBlocDelocalized);
				structure.addBlocDelocalized(currentBlocDelocalized);
			}
		}
		
		IHuckelObject huckelObject = (IHuckelObject) component;

		if (huckelObject.getBlocDelocalizedParent() == null) {
			try {
				SwingIO.log(this.getClass().getName(), "mouseClicked", "add " + huckelObject  + " in block");
				currentBlocDelocalized.addHuckelObject(huckelObject);
			} catch (BlocDelocalizedException e) {
				// si un atome ou liaison n'appartient pas a un bloc, cree un log
				SwingIO.warning(this.getClass().getName(), "mouseClicked", e.getMessage(), e);		
				e.printStackTrace();
			}
		}
		else if ( currentBlocDelocalized == huckelObject.getBlocDelocalizedParent()) {
			SwingIO.log(this.getClass().getName(), "mouseClicked", "remove " + huckelObject  + " from block");
			currentBlocDelocalized.removeHuckelObject(huckelObject);
		}
		currentBlocDelocalized.setNbElectron(currentBlocDelocalized.getSumAtomsNbElecPi());
	}

	//@Override
	public void mouseDragged(StructureViewController structureViewController, Point2D p) {
		if (!ActionEndBloc.checkCurrentBlocInCurrentStructure(structureViewController.getStructureView().getMesomeryView().getFrameApp())) 
			return;

		SwingIO.log(this.getClass().getName(), "mouseDragged", "drag to select multiple element to add in a block");
		StructureView structureView = structureViewController.getStructureView();
		// tracer un rectangle pour la zone selectionnee
		Structure structure  = structureViewController.getStructure();
		
		pRectEnd = p;
	
		double zoom = structureView.getZoom();
		double x = pRectBegin.getX();
		double y = pRectBegin.getY() ;
		double width = pRectEnd.getX() - pRectBegin.getX();
		double height = pRectEnd.getY() - pRectBegin.getY();
		
		if (width<0) x = x + width;
		if (height<0) y = y + height;
			
		
		Rectangle2D rect = new Rectangle2D.Double(x,
				y, 
				Math.abs(width), 
				Math.abs(height));
		
		Selection selection =new Selection(rect,StructureView.SELECTED_BLOC_DELOCALIZED_COLOR, StructureView.SELECTED_BLOC_DELOCALIZED_STROKE );
		structureView.setSelection(selection);
		
		
		// s'il y a un bloc deja selecionne, on le prend et on le vide
		currentBlocDelocalized = structureView.getSelectedBlocDelocalized();
		if ( currentBlocDelocalized != null)
			currentBlocDelocalized.empty();
	
		Point2D p2d;
		// quand on tire la souris en appuyant sur le bouton, selectionner les objets a l'interieur de 
		// la selection qui n'appartiennent a aucun bloc
		for (IHuckelObject o : structure.getHuckelObjects()){	
			p2d = structureView.getImagePoint(structureView.getPoint2D(o.getLocation()));
			x = p2d.getX();
			y = p2d.getY();
			if (((Rectangle2D) (selection.getShape())).contains(x,y) && (o.getBlocDelocalizedParent()==null) ) {
			//  selectionner le bloc de l'object ou on clique ou creer un nouveau bloc
				// on le fait dnas cette boucle car ca ne sert a rien de creer un bloc si rien n'est selectionner
				// sinon ca fausse le comportemment de ActionEndBloc
				if ( currentBlocDelocalized == null){
					currentBlocDelocalized = new BlocDelocalized();
					structureView.setSelectedBlocDelocalized(currentBlocDelocalized);
					structure.addBlocDelocalized(currentBlocDelocalized);
				}
				
				try {
					SwingIO.log(this.getClass().getName(), "mouseDragged", "add " + o + " in block");
					currentBlocDelocalized.addHuckelObject(o);
				} catch (BlocDelocalizedException e) {
					// si un atome ou liaison n'appartient pas a un bloc, cree un log
					SwingIO.warning(this.getClass().getName(), "mouseDragged", e.getMessage(), e);		
					e.printStackTrace();
				}
			
				currentBlocDelocalized.setNbElectron(currentBlocDelocalized.getSumAtomsNbElecPi());

			}
		}
		
		
		structureView.repaint();
	}

	//@Override
	public void mouseEntered(StructureViewController structureViewController, Point2D p) {

	}

	//@Override
	public void mouseExited(StructureViewController structureViewController, Point2D p) {
		// TODO Auto-generated method stub
		
	}

	//@Override
	public void mouseMoved(StructureViewController structureViewController, Point2D p) {
		StructureView structureView = structureViewController.getStructureView();
		structureView.setOverflightBlocDelocalized(null); // d'abord indiquer qu'aucun bloc n'est survole
		structureView.repaint(); // et repeindre
		BlocDelocalized bloc = structureView.getBlocDelocalized(p);
		if (bloc != null){
			structureView.setOverflightBlocDelocalized(bloc);
			structureView.repaint();		
		}
	}
	

	//@Override
	public void mousePressed(StructureViewController structureViewController, Point2D p) {
		SwingIO.log(this.getClass().getName(), "mousePressed", "try to select multiple element to add in a block");
		Structure structure = structureViewController.getStructure();
		savStructCalculateFly = structure.isEnabledFlyCalculate();
		Mesomery mesomery = structure.getMesomeryParent();
		if (mesomery != null)
			savMesomeryCalculateFly = mesomery.isEnabledFlyCalculate();
		if (structure != null)
			structure.stopFlyCalculate();
		if (mesomery != null)
			mesomery.stopFlyCalculate();
		
		pRectBegin = p;
				
	}

	//@Override
	public void mouseReleased(StructureViewController structureViewController, Point2D p) {
		SwingIO.log(this.getClass().getName(), "mouseReleased", "end multiple selection : recalculate weights, etc...");
		StructureView structureView = structureViewController.getStructureView();
		Structure structure = structureViewController.getStructure();
		if (structure != null && savStructCalculateFly){
			structure.startFlyCalculate();
			structure.calculate();
		}
		Mesomery mesomery = structure.getMesomeryParent();
		if (mesomery != null && savMesomeryCalculateFly){
			mesomery.startFlyCalculate();
			try {
				mesomery.calculate();
			} catch (IncorrectNumberPiElectronsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MesomeryNoStructureLocalizedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CoupleException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		structureView.setSelection(null);
		structureView.repaint();
		pRectBegin = null;
		pRectEnd = null;
			
	}

}
