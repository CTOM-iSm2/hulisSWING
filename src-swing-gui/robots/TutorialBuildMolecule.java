package robots;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.List;

import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.structures.Structure;

import multimedia.SousTitres;
import util.ComponentsTools;
import views.FrameApp;
import views.GraphOM;
import views.OrbitalControl;
import views.StructureView;

public class TutorialBuildMolecule extends TutorialCommands {

	FrameApp applet;

	public TutorialBuildMolecule(FrameApp applet) throws AWTException {
		super(applet);
		this.applet = applet;
	}

	private void createMolecule() {
		SousTitres st;
		// aller sur bouton add (en son centre)
		this.clickEraseAll();
		this.clickBTAdd();


		// aller sur le panneau de la molecule a 50, 50 (coord relatives)
		StructureView structureview = this.applet.getCurrentMesomeryView()
				.getCurrentStructureView();
		Structure structure = structureview.getStructure();

		Point dest = ComponentsTools.getLocationOnScreen(structureview);
	
		
		this.mouseMoveSlowly((int) dest.getX() + 50, (int) dest.getY() + 50);

		// dessiner 4 atomes lies
		mouseLeftSimpleClick();
		new SousTitres("Drag", 500);
		Point2D p = this.getImageLocationAtomBySeqNum(1);
		link ((int) p.getX(), (int) p.getY(), (int)(p.getX() + 50), (int)(p.getY() + 50));
		syncAndDelay();

		p = this.getImageLocationAtomBySeqNum(2);
		link ( (int) p.getX(), (int) p.getY(), (int)(p.getX() + 50), (int)(p.getY()  -50));
		syncAndDelay();

		p = this.getImageLocationAtomBySeqNum(3);
		link ((int) p.getX(), (int) p.getY(), (int)(p.getX() + 50), (int)(p.getY() + 50));
		syncAndDelay();


		p = this.getImageLocationAtomBySeqNum(4);
		changeAtom ((int) p.getX(), (int) p.getY(), PeriodicTable.Entry.O1);
		syncAndDelay();

		this.clickBT(this.applet.toolBarHuckel.btCenter);
		this.clickBT(this.applet.toolBarHuckel.btOptimize);


		GraphOM energiesGraph = structureview.getGraphOM(); 
		List<OrbitalControl>controls = energiesGraph
				.getOrbitalControls();
		dest = ComponentsTools.getLocationOnScreen(energiesGraph);
	
		int x, y;
		for (int i = 0; i < controls.size(); i+=2) {
			x = (int) (dest.getX() + controls.get(i).getPoint().getX());
			y = (int) (dest.getY() + controls.get(i).getPoint().getY());
			this.mouseMoveSlowly(x, y);
			mouseLeftSimpleClick();
			syncAndDelay();

			
		}
		


	}

	@Override
	public void go() {
		createMolecule();
	}

	
	/**
	 * 
	 * @return
	 */
	public boolean isSucceed() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFailReason(){
		return "";
	}


}
