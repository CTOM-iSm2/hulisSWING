package robots;

import java.awt.AWTException;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.PeriodicTable;

import tools.Utils;
import views.FrameApp;

public class RobotTest038_039 extends TutorialCommands {
	int numTuto;
	
	@Override
	public void go() {
		syncAndDelay();

		switch (numTuto) {
		case 1:
			mesomery1();
			break;
		case 2:
			mesomery2();
			break;
		}

	}

	public void mesomery1() {
		applet.selectMethod  ("HLCI", true);
		applet.selectMethod  ("HLP", true);
		clickEraseAll();
		drawDelocalized();
		clickCreate1();
		draw1();
		clickCreate1();
		draw2();
		delay(2000);
		Utils.compareMesomeryXML (applet,"CCl21");					
		
	}
	
	public void mesomery2() {
		clickCreate1();
		draw3();
		delay(2000);
		Utils.compareMesomeryXML (applet,"CCl22");	

	}
		
	public void drawDelocalized() {
		selectDelocalizedStructure();
		putAtom (PeriodicTable.Entry.Cl, 80,170);
		Point2D p1 = this.getImageLocationAtomBySeqNum(1);
		putAtom (PeriodicTable.Entry.O2, 0.0, 160,90, (int)p1.getX(), (int)p1.getY());
		Point2D p2 = this.getImageLocationAtomBySeqNum(2);
		putAtom (PeriodicTable.Entry.Cl, 240,170, (int)p2.getX(), (int)p2.getY());
		changeHXY (1,2, 0.62);
		changeHXY (2,3, 0.62);
		clickAddMoleculeCharge(2);
	}
	
	
	public void draw1() {
		putElectrons(1, 2);
		putElectrons(2, 3, 2);

	}

	
	public void draw2() {
		putElectrons(1, 2, 2);
		putElectrons(3, 2);
		
	}

	public void draw3() {
		putElectrons(1, 2);
		putElectrons(3, 2);
		
	}
	
	public RobotTest038_039(FrameApp applet, int numTuto) throws AWTException {
		super(applet);
		this.numTuto= numTuto;
	}
	

}
