package robots;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.geom.Point2D;

import javax.swing.JToggleButton;

import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.util.maths.Point3D;

import util.ComponentsTools;
import views.DialogChangeAtom;
import views.DialogChangeHuckelBond;
import views.FrameApp;
import views.StructureView;
import controller.globalCommands.ChooseMenuExp;

public abstract  class TutorialCommands extends Tutorial {

	/**
	 * 
	 * @param seqNum
	 * @return
	 */
	public synchronized Point2D getImageLocationAtomBySeqNum(int seqNum){
		Point3D location = null;

		while (location == null) {
			try {
				if(this.applet.getCurrentMesomery() == null)
					throw new Exception ("MESOMERY IS NULL");
				if(this.applet.getCurrentMesomery().getDelocalizedStructure() == null)
					throw new Exception ("STRUCTURE DELOCALIZED IS NULL");
				if(this.applet.getCurrentMesomery().getDelocalizedStructure().getAtomBySeqNum(seqNum) == null)
					throw new Exception ("NO ATOM WITH SEQNUM==" + seqNum);
				if(this.applet.getCurrentMesomery().getDelocalizedStructure().getAtomBySeqNum(seqNum).getLocation() == null)
					throw new Exception ("NO LOCATION FOR ATOM SEQNUM==" + seqNum);
			} catch (Exception e) {
				e.printStackTrace();
			}


			location = this.applet.getCurrentMesomery().getDelocalizedStructure().getAtomBySeqNum(seqNum).getLocation();
			syncAndDelay();
		}
		StructureView structureView  = this.applet.getCurrentMesomeryView().getCurrentStructureView();
		return structureView.getImagePoint(structureView.getPoint2D(location));
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public synchronized Point2D getImageLocationAtomByIndex(int index){
		Point3D location = null;

		while (location == null) {
			try {
				if(this.applet.getCurrentMesomery() == null)
					throw new Exception ("MESOMERY IS NULL");
				if(this.applet.getCurrentMesomery().getDelocalizedStructure() == null)
					throw new Exception ("STRUCTURE DELOCALIZED IS NULL");
				if(this.applet.getCurrentMesomery().getDelocalizedStructure().getAtomByIndex(index) == null)
					throw new Exception ("NO ATOM WITH INDEX==" + index);
				if(this.applet.getCurrentMesomery().getDelocalizedStructure().getAtomByIndex(index).getLocation() == null)
					throw new Exception ("NO LOCATION FOR ATOM INDEX==" + index);
			} catch (Exception e) {
				e.printStackTrace();
			}


			location = this.applet.getCurrentMesomery().getDelocalizedStructure().getAtomByIndex(index).getLocation();
			syncAndDelay();
		}
		StructureView structureView  = this.applet.getCurrentMesomeryView().getCurrentStructureView();
		return structureView.getImagePoint(structureView.getPoint2D(location));
	}



	/**
	 * 
	 * @param numAtom
	 * @param nb
	 */
	public synchronized void putElectrons(int numAtom, int nb) {
		if (!applet.toolBarMesomery.btChangeElectrons.isSelected())
			this.clickChangeElectrons();

		StructureView structureView = applet.getCurrentMesomeryView().getCurrentStructureView();
		Point2D p = ComponentsTools.getLocationOnScreen(structureView);
		int x2 =  (int) p.getX();
		int y2 = (int)  p.getY();
		
		HuckelAtom atom = (HuckelAtom) structureView.getStructure().getAtomBySeqNum(numAtom);
		p = structureView.getImagePoint(structureView.getPoint2D(atom.getLocation()));
		this.mouseMoveSlowly((int) (x2 + p.getX()),  (int)(y2 + p.getY()));


		while (atom.countRadR() != nb) {
			this.mouseLeftSimpleClick();
			syncAndDelay();
		}

		syncAndDelay();


	}

	/**
	 * 
	 * @param numAtom1
	 * @param numAtom2
	 * @param nb
	 */
	public synchronized void putElectrons(int numAtom1, int numAtom2, int nb)  {
		if (!applet.toolBarMesomery.btChangeElectrons.isSelected())
			this.clickChangeElectrons();

		StructureView structureView = applet.getCurrentMesomeryView().getCurrentStructureView();
		Point2D p = ComponentsTools.getLocationOnScreen(structureView);
		int x2 =  (int) p.getX();
		int y2 = (int)  p.getY();
		Structure s = structureView.getStructure();
		HuckelBond b = (HuckelBond) s.getBond( s.getAtomBySeqNum(numAtom1),s.getAtomBySeqNum(numAtom2)) ;
		p = structureView.getImagePoint(structureView.getPoint2D(b.getLocation()));
		this.mouseMoveSlowly((int) (x2 + p.getX()),  (int)(y2 + p.getY()));


		while (b.getBondType() != nb) {
			this.mouseLeftSimpleClick();
			syncAndDelay();
		}

		syncAndDelay();

	}

	/**
	 * 
	 * @param nb
	 */

	public synchronized void clickAddMoleculeCharge(int nb)  {
		for (int i=0; i<nb; i++) {
			this.clickBT(applet.toolBarHuckel.btMoleculeRemoveElecCharge);
		}
		syncAndDelay();
	}

	/**
	 * 
	 * @param nb
	 */
	public synchronized void clickRemoveMoleculeCharge(int nb)  {
		for (int i=0; i<nb; i++) {
			this.clickBT(applet.toolBarHuckel.btMoleculeAddElecCharge);
		}
		syncAndDelay();
	}


	/**
	 * 
	 */
	public synchronized void selectDelocalizedStructure () {
		applet.getCurrentMesomeryView().setCurrentStructure(applet.getCurrentMesomeryView().getMesomery().getDelocalizedStructure());
		syncAndDelay();
	}



	/**
	 * 
	 */
	public synchronized void clickCreate1() {
		clickBT (applet.toolBarMesomery.btDuplicate);

	}

	public void clickChangeElectrons() {
		clickBT (applet.toolBarMesomery.btChangeElectrons);		
	}

	/**
	 * 
	 */
	public synchronized void clickEraseMesomery() {
		clickBT (applet.toolBarMesomery.btEraseMesomery);

		applet.getCurrentMesomeryView().setSelectedIndex(0);
		syncAndDelay();
	}

	/**
	 * 
	 * @param name of structure "tot"  "1" "2" ...
	 */
	public synchronized void clickOnTab(String name)  {
		syncAndDelay();
		if (name == StructureDelocalized.NAME_DELOCALIZED_STRUCTURE)  
			applet.getCurrentMesomeryView().setSelectedIndex(0);
		else 
			applet.getCurrentMesomeryView().setSelectedIndex(Integer.valueOf(name));


		syncAndDelay();

	}

	/**
	 * 
	 * @param n : index of tab : 0 1 2 3 ...
	 */
	public synchronized void clickOnTab(int n)  {
		syncAndDelay();
		applet.getCurrentMesomeryView().setSelectedIndex(n);
		syncAndDelay();
		delay(NORMAL_DELAY);

	}

	/**
	 * 
	 */
	public synchronized void clickErase1() {
		clickBT (applet.toolBarMesomery.btErase1);
		syncAndDelay();
	}

	/**
	 * 
	 */
	public synchronized void clickEraseAll() {
		if((this.applet.getCurrentMesomery().countLocalizedStructs() ==0) && (this.applet.getCurrentMesomery().getDelocalizedStructure().countAtoms()==0))
			return;
		clickBT (applet.toolBarHuckel.btErase);
		syncAndDelay();

	}

	/**
	 * 
	 */

	public void clickBTAdd() {
		clickBT(applet.toolBarHuckel.btAdd);

	}

	/**
	 * 
	 */
	public void clickBTShowHxHxy() {
		clickBT(applet.toolBarHuckel.btDisplayHxHxy);

	}

	/**
	 * 
	 */
	public void clickBTShowNumbering() {
		clickBT(applet.toolBarHuckel.btDisplayNum);

	}

	/**
	 * 
	 */
	public void clickBTChange() {

		clickBT(applet.toolBarHuckel.btChange);
	}


	/**
	 * 
	 */
	public void clickBTBloc() {
		this.clickMenuExpert();	
		clickBT(applet.toolBarMesomery.btBloc);
	}

	/**
	 * 
	 */
	public void clickBTEndBloc() {

		clickBT(applet.toolBarMesomery.btEndBloc);
	}

	/**
	 * 
	 */
	public void clickBTShowStructureResults() {

		clickBT(applet.toolBarHuckel.btShowResults);
	}

	/**
	 * 
	 */
	public void clickBTShowMesomeryResults() {

		clickBT(applet.toolBarMesomery.btShowResultsMesomery);
	}

	/**
	 * 
	 */
	public void clickBTGenerateAll() {
		clickBT(applet.toolBarMesomery.btAutoGenStruct);
		syncAndDelay();
		delay(NORMAL_DELAY);
		syncAndDelay();

	}

	/**
	 * 
	 */
	public synchronized void clickMenuExpert() {
		if (applet.menuBar.menuExp.isSelected())
			return;
		
		clickBT(applet.menuBar.computeMenu);
		clickBT(applet.menuBar.menuExp);

		delay(NORMAL_DELAY);
	}


	


	/**
	 * 
	 * @param beta
	 */
	public synchronized void selectSliderValue(double value) {
		applet.toolBarMesomery.setValueManually(value);
		syncAndDelay();
	}


	

	/**
	 * 
	 */
	public synchronized void clickMenuHLCI() {
		if (applet.menuBar.getItemMethod("HLCI").isSelected())
			return;
		clickBT(applet.menuBar.computeMenu);
		clickBT(applet.menuBar.getItemMethod("HLCI"));
	}
	
	/**
	 * 
	 */
	public synchronized void clickMenuHLP() {
		if (applet.menuBar.getItemMethod("HLP").isSelected())
				return;
		clickBT(applet.menuBar.computeMenu);
		clickBT(applet.menuBar.getItemMethod("HLP"));
	}
	
	
	/**
	 * 
	 * @param button
	 */
	public  synchronized void moveToBT(Component button) {
		Point2D bt = ComponentsTools.getCenter(button);
		this.mouseMoveSlowly((int) bt.getX(),  (int) bt.getY());
	}


	
	
	
	
	/**
	 * 
	 * @param button
	 */
	public  synchronized void clickBT(Component button) {
		moveToBT(button);
		this.mouseLeftSimpleClick();
		syncAndDelay();
	}

	/**
	 * 
	 * @param button
	 */
	public synchronized void clickBT(JToggleButton button) {
		if (button.isSelected()) return;
		moveToBT(button);
		this.mouseLeftSimpleClick();
		syncAndDelay();
	}

	/**
	 * 
	 * @param entry
	 * @param x
	 * @param y
	 */
	public void putAtom (PeriodicTable.Entry entry, int x, int y) {
		putAtom (entry,0.0, false ,x,y, 0 ,0 , false);

	}


	/**
	 * 
	 * @param entry
	 * @param hX
	 * @param x
	 * @param y
	 */
	public void putAtom (PeriodicTable.Entry entry, double hX,  int x, int y) {
		putAtom (entry, hX, true ,x,y, 0 ,0 , false);

	}

	/**
	 * 
	 * @param entry
	 * @param x
	 * @param y
	 * @param pX
	 * @param pY
	 */
	public void putAtom (PeriodicTable.Entry entry, int x, int y, int pX, int pY) {
		putAtom (entry, 0.0, false ,x,y, pX ,pY , true);

	}

	/**
	 * 
	 * @param entry
	 * @param hX
	 * @param x
	 * @param y
	 * @param pX
	 * @param pY
	 */
	public void putAtom (PeriodicTable.Entry entry, double hX, int x, int y, int pX, int pY) {
		putAtom (entry, hX, true ,x,y, pX ,pY , true);
	}


	/*
	 * 
	 * 
	 */
	private synchronized void putAtom (PeriodicTable.Entry entry, double hX, boolean changeHX, int x, int y,  int pX, int pY, boolean link) {

		StructureView structureView = applet.getCurrentMesomeryView().getCurrentStructureView();
		Point p = ComponentsTools.getLocationOnScreen(structureView);
		int x2 =  (int) p.getX();
		int y2 = (int)  p.getY();

		clickBTAdd();	

		if (!link) {
			this.mouseMoveSlowly((int) (x2 + x),  (int) (y2 + y) );
			this.mouseLeftSimpleClick();

			syncAndDelay();


		}
		else {
			link (pX,  pY,  x,  y );

		}

		if ((entry != PeriodicTable.Entry.C) || changeHX)
			this.changeAtom(x, y, entry, hX, changeHX);

		syncAndDelay();

	}

	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */

	public synchronized void link (int x1, int y1, int x2, int y2) {
		clickBTAdd();	
		StructureView structureView = applet.getCurrentMesomeryView().getCurrentStructureView();
		Point p = ComponentsTools.getLocationOnScreen(structureView);
		int xR =  (int) p.getX();
		int yR = (int)  p.getY();
		this.mouseMoveSlowly(xR + x1, yR + y1);
		syncAndDelay();
		mousePress(InputEvent.BUTTON1_MASK);
		syncAndDelay();
		this.mouseMoveSlowly(xR + x2 , yR + y2);
		syncAndDelay();
		mouseRelease(InputEvent.BUTTON1_MASK);
		syncAndDelay();

	}

	/**
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 */

	public synchronized void selectRectangleBloc (int x1, int y1, int x2, int y2) {
		clickBTBloc();	
		StructureView structureView = applet.getCurrentMesomeryView().getCurrentStructureView();
		Point p = ComponentsTools.getLocationOnScreen(structureView);
		int xR =  (int) p.getX();
		int yR = (int)  p.getY();

		this.mouseMoveSlowly(xR + x1, yR + y1);
		mousePress(InputEvent.BUTTON1_MASK);
		this.mouseMoveSlowly(xR + x2 , yR + y2);
		mouseRelease(InputEvent.BUTTON1_MASK);
		//sleep(VERYLONG_DELAY);
		syncAndDelay();

	}


	/**
	 * 
	 * @param x
	 * @param y
	 * @param element
	 * @param hX
	 */
	public void changeAtom (int x, int y, PeriodicTable.Entry element, double hX) {
		changeAtom (x, y, element, hX, true);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param element
	 */
	public void changeAtom (int x, int y, PeriodicTable.Entry element) {

		changeAtom (x,y,element, 0.0, false);
	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param hX
	 */
	public void changeAtom (int x, int y, double hX)  {

		changeAtom (x,y,null, hX, true);
	}


	/*
	 * 
	 */

	private synchronized void changeAtom (int x, int y, PeriodicTable.Entry element, double hX, boolean changeHX ) {
		StructureView structureView = applet.getCurrentMesomeryView().getCurrentStructureView();
		Point p = ComponentsTools.getLocationOnScreen(structureView);
		int x2 =  (int) p.getX();
		int y2 = (int)  p.getY();

		this.clickBTChange();

		this.mouseMoveSlowly(x2+x, y2+ y);
		mouseLeftSimpleClick();
		syncAndDelay();
		// la fenetre de choix s'ouvre choisir hydrogene
		DialogChangeAtom dialog = null; 
		while (dialog == null) {
			dialog =this.getDialogChangeAtom() ;
			syncAndDelay();
		}
		// attendre que la fenetre soit visible sinon
		// l'instruction suivante renvoie une exception


		if (element != null)
			this.clickBT(dialog
					.getButton(element.toString()));

		if (changeHX) {
			this.clickBT(dialog.txtHx);
			syncAndDelay();
			dialog.txtHx.selectAll();
			syncAndDelay();
			dialog.txtHx.setText(Double.toString(hX));
			syncAndDelay();
		}


		this.clickBT(dialog.btOK);


		syncAndDelay();


	}

	/**
	 * 
	 * @param atom1
	 * @param atom2
	 * @param hXY
	 */
	public synchronized void changeHXY (int atom1, int atom2, double hXY ) {
		this.clickBTChange();

		StructureView structureView = applet.getCurrentMesomeryView().getCurrentStructureView();
		Point2D p = ComponentsTools.getLocationOnScreen(structureView);
		int x2 =  (int) p.getX();
		int y2 = (int)  p.getY();

		Structure s = structureView.getStructure();

		Bond b = s.getBond( s.getAtomBySeqNum(atom1), s.getAtomBySeqNum(atom2));
		p = structureView.getImagePoint(structureView.getPoint2D(b.getLocation()));

		this.mouseMoveSlowly((int) ( x2+   p.getX()) ,
				(int) ( y2+ p.getY() ));
		mouseLeftSimpleClick();	
		syncAndDelay();

		DialogChangeHuckelBond dialog = null; 
		while (dialog == null) {
			dialog =this.getDialogChangeHuckelBond();
			syncAndDelay();
		}

		this.clickBT(dialog.txtHxy);
		syncAndDelay();
		dialog.txtHxy.selectAll();
		syncAndDelay();
		dialog.txtHxy.setText(Double.toString(hXY));
		syncAndDelay();
		this.clickBT(dialog.btOK);

		syncAndDelay();
	}



	/**
	 * 
	 * @param applet
	 * @throws AWTException
	 */
	public TutorialCommands(FrameApp applet) throws AWTException {
		super(applet);
	}


	abstract public void go();



}
