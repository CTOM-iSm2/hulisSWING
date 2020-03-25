package robots;

import java.awt.AWTException;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.PeriodicTable;

import tools.Utils;
import util.MyJOptionPane;
import util.ThreadUtils;
import views.AppletToFrame;
import views.FrameApp;

public class RobotTest033_037 extends TutorialCommands {
	protected int numTuto;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		final FrameApp app;
		ThreadUtils.NEVER_INVOKE_LATER = true;
		MyJOptionPane.setDefaultEnabledModal(false);
		app = AppletToFrame.createAndShowGUI(false);
					
		
		try {
			RobotTest033_037 robot = new RobotTest033_037(app, 1);
			robot.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
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
		case 3:
			mesomery3();
			break;
		case 4:
			mesomery4();
			break;
		case 5:
			mesomery5();
			break;
		}

		
	}

	public void mesomery1() {
		applet.selectMethod  ("HLCI", true);
		applet.selectMethod  ("HLP", true);
		clickEraseAll();
		this.drawDelocalized();
		draw1();
		draw2();
		draw3();
		draw4();
		draw5();	
		delay(2000);
		Utils.compareMesomeryXML (applet,"imidazolylidene1");	

	}
	
	public void mesomery2() {
		clickOnTab("1");
		clickErase1();
		delay(2000);
		Utils.compareMesomeryXML (applet,"imidazolylidene2");	
	}
	
	public void mesomery3() {
		clickEraseMesomery();
		delay(2000);
		draw1();
		draw2();
		draw3();
		draw4();
		draw5();	
		draw6();
		draw7();
		draw8();
		delay(2000);
		Utils.compareMesomeryXML (applet,"imidazolylidene3");				


	}
	
	public void mesomery4() {
		clickOnTab("1");
		clickErase1();
		delay(2000);
		Utils.compareMesomeryXML (applet,"imidazolylidene4");			

	}

	public void mesomery5() {
		clickOnTab("3");
		clickErase1();
		delay(2000);
		clickErase1();
		delay(2000);
		Utils.compareMesomeryXML (applet,"imidazolylidene5");			

	}
	
	
	
	public void drawDelocalized() {
		selectDelocalizedStructure();
		putAtom (PeriodicTable.Entry.B, 0.0, 107, 88);
		Point2D p1 = this.getImageLocationAtomBySeqNum(1);
		putAtom (PeriodicTable.Entry.N2, 59,139, (int) p1.getX(), (int) p1.getY());
		Point2D p = this.getImageLocationAtomBySeqNum(2);
		putAtom (PeriodicTable.Entry.C, 91,194,(int) p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(3);	
		putAtom (PeriodicTable.Entry.C, 157,182,(int) p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(4);
		putAtom (PeriodicTable.Entry.N2, 168,120,(int) p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(5);
		link ((int) p.getX(), (int) p.getY() , (int) p1.getX(), (int) p1.getY() );
		changeHXY (1,2, 0.89);
		changeHXY (5,1, 0.89);
	}
	
	
	public void draw1() {
		clickCreate1();
		putElectrons(2, 2);
		putElectrons(5, 2);
		putElectrons(3, 4, 2);
	}

	
	public void draw2() {
	
		clickCreate1();
		putElectrons(1, 2, 2);
		putElectrons(3, 4, 2);
		putElectrons(5, 2);
	}

	public void draw3() {
		clickCreate1();
		putElectrons(2, 2);
		putElectrons(3, 4, 2);
		putElectrons(1, 5, 2);
		
	}
	
	public void draw4() {
		clickCreate1();
		putElectrons(2, 2);
		putElectrons(3, 2);
		putElectrons(4, 5, 2);
		
	}	

	public void draw5() {
		clickCreate1();
		putElectrons(2, 3, 2);
		putElectrons(4, 2);
		putElectrons(5, 2);
	}	
	
	public void draw6() {
		clickCreate1();
		putElectrons(2, 3, 2);
		putElectrons(4, 5, 2);
		putElectrons(1, 2);
	}	

	public void draw7() {
		clickCreate1();
		putElectrons(1, 2, 2);
		putElectrons(3, 2);
		putElectrons(4, 5, 2);
	}	

	public void draw8() {
		clickCreate1();
		putElectrons(2, 3, 2);
		putElectrons(4, 2);
		putElectrons(1, 5, 2);
	}	
	
	
	
	public RobotTest033_037(FrameApp applet, int numTuto) throws AWTException {
		super(applet);
		this.numTuto = numTuto;

	}
	
	

	
}

