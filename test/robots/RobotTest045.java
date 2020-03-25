package robots;

import java.awt.AWTException;
import java.util.Date;

import org.ctom.hulis.huckel.PeriodicTable;
import org.junit.Assert;

import tools.TestOut;
import views.ErrorReport;
import views.FrameApp;

public class RobotTest045 extends TutorialCommands {

	public RobotTest045(FrameApp applet) throws AWTException {
		super(applet);
	}

	@Override
	public void go() {
		TestOut.println("Probleme corrige : Le fait de generer la mesomerie directement apres create 1, donc avec Phi1 ayant un nombre incorrect d'electrons, ne doit pas provoquer une erreur .");
		TestOut.println("* " + new Date());
		TestOut.println("*avec HLCI et HLP .");
		TestOut.println("*");

		applet.selectMethod  ("HLCI", true);
		applet.selectMethod  ("HLP", true);
		
		clickEraseAll();

		syncAndDelay();
		delay(2000);

		TestOut.println("-- Cree le butadiene");
		putAtom (PeriodicTable.Entry.C, 80,170);
		putAtom (PeriodicTable.Entry.C, 140,170,80,170);
		putAtom (PeriodicTable.Entry.C, 200,210,140,170);
		putAtom (PeriodicTable.Entry.C, 260,170,200,210);
		if (!applet.menuBar.menuExp.isSelected()) {
			TestOut.println("-- active menu expert");
			this.clickMenuExpert();
			syncAndDelay();
		}
		TestOut.println("-- create 1");
		this.clickCreate1();			
		TestOut.println("-- generate all");
		this.clickBTGenerateAll();
		syncAndDelay();
			

		TestOut.println("Resultat attendu : No error report (isErrorReported = 0)");
		TestOut.println("Resultat obtenu : ");

		if (ErrorReport.isErrorReported() ) {	
			TestOut.println("TEST FAILED !!!.");

			Assert.fail();

		}else{
			TestOut.println("TEST OK.");

		}

	}

}
