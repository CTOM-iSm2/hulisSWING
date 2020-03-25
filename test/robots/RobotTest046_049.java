package robots;

import java.awt.AWTException;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.PeriodicTable;
import org.junit.Assert;

import tools.TestOut;
import util.MyJOptionPane;
import util.ThreadUtils;
import views.AppletToFrame;
import views.ErrorReport;
import views.FrameApp;
import controller.globalCommands.ActionEraseAll;

public class RobotTest046_049 extends TutorialCommands {

	int numtest;
	
	public static void main (String args[]){
		final FrameApp app;
		ThreadUtils.NEVER_INVOKE_LATER = true;
		MyJOptionPane.setDefaultEnabledModal(false);
		app = AppletToFrame.createAndShowGUI(false);
		TestOut.createOut();		
		
		try {
			   
			new ActionEraseAll(app).execute(); 
			RobotTest046_049 robot = new RobotTest046_049(app, 1);
			robot.go();

			robot.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	

	

	}
	
	public RobotTest046_049(FrameApp applet, int numtest) throws AWTException {
		super(applet);
		this.numtest=numtest;
	}

	@Override
	public void go() {

		clickEraseAll();

		syncAndDelay();
		delay(2000);

		switch (numtest) {
		case 1 : 
			TestOut.println("Probleme corrige :  Dessine Stot et S1 Me-O-C=C-C=C en commencant par Me sans HLP");
			TestOut.println(" cliquer sur l'oxygene pour retirer les 2 electron ne devrait pas generer de rapport d'erreur quel que soit l'ordre de creation des atomes  " );
			TestOut.println("Test effectue :");

			clickEraseAll();
			TestOut.println("-- Me-O-C-C-C-C avec HLCI et sans HLP");
			applet.selectMethod  ("HLCI", true);
			applet.selectMethod  ("HLP", true);
			drawSens1(); 
			break;
		case 2 : 
			TestOut.println("Probleme corrige : Dessine Stot et S1 Me-O-C=C-C=C en commencant par C sans HLP");
			TestOut.println(" cliquer sur l'oxygene pour retirer les 2 electron ne devrait pas generer de rapport d'erreur quel que soit l'ordre de creation des atomes " );
			TestOut.println("Test effectue :");

			clickEraseAll();
			TestOut.println("-- C-C-C-C-O-Me avec HLCI et sans HLP");
			applet.selectMethod  ("HLCI", true);
			applet.selectMethod  ("HLP", false);
			drawSens2(); 
			break;		
		case 3 : 
			TestOut.println("Probleme corrige : Dessine Stot et S1 Me-O-C=C-C=C en commencant par Me avec HLP");
			TestOut.println(" cliquer sur l'oxygene pour retirer les 2 electron ne devrait pas generer de rapport d'erreur quel que soit l'ordre de creation des atomes " );
			TestOut.println("Test effectue :");
			clickEraseAll();
			TestOut.println("-- Me-O-C-C-C-C avec HLCI et HLP");
			applet.selectMethod  ("HLCI", true);
			applet.selectMethod  ("HLP", true);
			drawSens1(); 
			break;		
		case 4 : 
			TestOut.println("Probleme corrige : Dessine Stot et S1 Me-O-C=C-C=C en commencant par C avec HLP");
			TestOut.println(" cliquer sur l'oxygene pour retirer les 2 electron ne devrait pas generer de rapport d'erreur quel que soit l'ordre de creation des atomes " );
			TestOut.println("Test effectue :");
			clickEraseAll();
			TestOut.println("-- C-C-C-C-O-Me avec HLCI et HLP");
			applet.selectMethod  ("HLCI", true);
			applet.selectMethod  ("HLP", true);
			drawSens2(); 
			break;		
		}
		
		TestOut.println("Resultat attendu : No error report (isErrorReported = 0)");
		TestOut.println("Resultat obtenu : ");

		if (ErrorReport.isErrorReported() ) {	
			TestOut.println("TEST FAILED !!!t.");

			Assert.fail();

		}else{

			TestOut.println("TEST OK");
		}

	}

	public void drawSens1() {
		TestOut.println("-- Me-O-C-C-C-C");
		putAtom (PeriodicTable.Entry.Me, 20,170);
		Point2D p = this.getImageLocationAtomBySeqNum(1);
		putAtom (PeriodicTable.Entry.O2, 60,180,(int) p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(2);
		putAtom (PeriodicTable.Entry.C, 100,210, (int)p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(3);
		putAtom (PeriodicTable.Entry.C, 140,170, (int)p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(4);
		putAtom (PeriodicTable.Entry.C, 180,210, (int) p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(5);
		putAtom (PeriodicTable.Entry.C, 220,170, (int)p.getX(), (int) p.getY());

		this.clickCreate1();

		TestOut.println("-- Selectionne la premiere molecule");

		this.clickOnTab(1);
	
		TestOut.println("-- 2-2-C=C-C=C");
		this.putElectrons(1, 2);
		this.putElectrons(2, 2);
		this.putElectrons(3, 4, 2);
		this.putElectrons(5, 6, 2);

		TestOut.println("-- 2-2=C=C-C=C");
		this.putElectrons(2,3, 2);

		TestOut.println("--enleve 2 electrons sur O");
		this.putElectrons(2, 0);
		
		
	}
	
	
	public void drawSens2() {
		TestOut.println("-- C-C-C-C-O-Me");
		putAtom (PeriodicTable.Entry.C, 220,170);
		Point2D p = this.getImageLocationAtomBySeqNum(1);
		putAtom (PeriodicTable.Entry.C, 180,210, (int)p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(2);
		putAtom (PeriodicTable.Entry.C, 140,170, (int)p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(3);
		putAtom (PeriodicTable.Entry.C, 100,210, (int)p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(4);
		putAtom (PeriodicTable.Entry.O2, 60,180, (int)p.getX(), (int) p.getY());
		p = this.getImageLocationAtomBySeqNum(5);
		putAtom (PeriodicTable.Entry.Me, 20,210, (int)p.getX(), (int) p.getY());

		this.clickCreate1();
		TestOut.println("-- Selectionne la premiere molecule");

		this.clickOnTab(1);
	
		TestOut.println("-- C=C-C=C-2-2");
		this.putElectrons(6, 2);
		this.putElectrons(5, 2);
		this.putElectrons(3, 4, 2);
		this.putElectrons(1, 2, 2);
		
		TestOut.println("-- C=C-C=C=2-2");
		this.putElectrons(4,5, 2);

		TestOut.println("--enleve 2 electrons sur O");
		this.putElectrons(5, 0);


	}
	
}
