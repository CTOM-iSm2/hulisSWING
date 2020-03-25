package robots;

import java.awt.AWTException;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.HulisException;
import org.junit.Assert;

import tools.TestOut;
import views.DialogChangeAtom;
import views.ErrorReport;
import views.FrameApp;

public class RobotTest042 extends TutorialCommands {

	public RobotTest042(FrameApp app) throws AWTException, HulisException {
		
		super(app);
	}

	@Override
	public void go() {
		TestOut.println("Probleme corrige : Le fait de ne pas selectionner de type d'atome, mais de valider directement, quand on modifie un atome d'hydrogene ne doit pas provoquer de rapport d'erreur.");
		TestOut.println("Test effectue :");
		TestOut.println("On dessine un atome C. Ses voisins sont des H");
		TestOut.println("On clique sur un H en mode modif. Dans la fenetre de modif on clique OK sans selectionner d'atome (donc pas de de modif de type d'atome). On ne doit pas obtenir de rapport d'erreur ");
		clickEraseAll();
	
		applet.selectMethod  ("HLCI", true);
		applet.selectMethod  ("HLP", false);
		
		syncAndDelay();
		delay(2000);

		putAtom (PeriodicTable.Entry.C, 80,170);
		Point2D p =this.getImageLocationAtomByIndex(3); // p atom H
		changeAtom ((int) p.getX(), (int) p.getY(), 2.0);
		// a ce  niveau on devrait avoir une boite de dialogue "vous devez indiquer le type d'atome"
		// mais celle ci est cachee car les boites de dialogues sont desactivees dans la procedure de test car modales
		// et non fermables par le robot. Le message est donc affiche en console.
		syncAndDelay();
		DialogChangeAtom dialog = null; 
		while (dialog == null) {
			dialog =this.getDialogChangeAtom() ;
			syncAndDelay();

		}
		dialog.dispose(); // on ferme de force la boite de dialogue car sinon AllTests
		// ne passe pas au test suivant
		

		syncAndDelay();
		TestOut.println("Resultat attendu : No error report (isErrorReported = 0)");
		TestOut.println("Resultat obtenu : ");
		if (!ErrorReport.isErrorReported()) {
			// boite de dialogue le type d'atome doit etre renseigne
			TestOut.println("No error report-> TEST OK. Read test.out file");
		}else{
			TestOut.println("Error report -> TEST FAILED  !!!.");
			Assert.fail();
		}	

	}


}
