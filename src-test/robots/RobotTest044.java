package robots;

import java.awt.AWTException;

import org.ctom.hulis.huckel.PeriodicTable;
import org.junit.Assert;

import tools.TestOut;
import views.ErrorReport;
import views.FrameApp;

public class RobotTest044 extends TutorialCommands {

	public RobotTest044(FrameApp applet) throws AWTException {
		super(applet);
	}

	@Override
	public void go() {
		TestOut.println("Probleme corrige : L'auto-couplage ne doit pas renvoyer un rapport d'erreur .");
		TestOut.println("Test effectue : on cree une mesomerie de butadiene");
		TestOut.println("sur la troisieme structure, on deture le couple en mettant deux electrons sur un des atomes couple");
		TestOut.println("on autocouple en remettant un seul electron. On ne doit pas obtenir de rapport d'erreur ");
		clickEraseAll();
	
		applet.selectMethod  ("HLCI", true);
		applet.selectMethod  ("HLP", false);
		
		syncAndDelay();
		delay(2000);


		TestOut.println("-- Cree le butadiene");
		putAtom (PeriodicTable.Entry.C, 80,170);
		putAtom (PeriodicTable.Entry.C, 140,170,80,170);
		putAtom (PeriodicTable.Entry.C, 200,210,140,170);
		putAtom (PeriodicTable.Entry.C, 260,170,200,210);
		TestOut.println("-- Genere la mesomerie");
		this.clickMenuExpert();
		this.clickBTGenerateAll();
		delay(1000);
		while (applet.getCurrentMesomery().isAutoGenerating());
		TestOut.println("-- met le slider au maximum");		
		this.selectSliderValue(-2.0);
		delay(1000);

		
		TestOut.println("-- Selectionne la troisieme molecule");
		this.clickOnTab(3);
		TestOut.println("-- Met deux electrons sur atome 1 pour detruire le couple qu'il y avait entre 1 et 4");

		this.putElectrons(1, 2);
		syncAndDelay();
		TestOut.println("-- Remet un seul electron sur atome 1 pour recreer le couple entre 1 et 4");
		this.putElectrons(1, 1);
		
		TestOut.println("Resultat attendu : No error report (isErrorReported = 0)");
		TestOut.println("Resultat obtenu : ");
		if (ErrorReport.isErrorReported() ) {	
			TestOut.println("Rapport erreur -> TEST FAILED !!! ");
			Assert.fail();

		}else{
			TestOut.println("Pas de rapport d'erreur -> TEST OK. ");
		}


	}

}
