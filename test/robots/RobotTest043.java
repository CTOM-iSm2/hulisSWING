package robots;

import java.awt.AWTException;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.util.maths.Maths;
import org.junit.Assert;

import tools.TestOut;
import views.FrameApp;

public class RobotTest043 extends TutorialCommands {

	public RobotTest043(FrameApp applet) throws AWTException {
		super(applet);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void go() {
		TestOut.println("Probleme corrige: Quand clique sur un atome (en mode modification) puis, que dans la fenetre 'modifier', on modifie le type de l'atome ET la valeur hX, puis qu'on valide, le type a bien ete modifie mais la valeur hX n'est pas prise en compte.");
		TestOut.println("Test effectue :");
		TestOut.println("On dessine  l'acroleine C-C-C-O1");
		TestOut.println("On change hXY(C-O1) par 1.5 puis hX(O1) par 2.0. L'energie doit etre correcte");
		clickEraseAll();
		
		applet.selectMethod  ("HLCI", true);
		applet.selectMethod  ("HLP", false);
		
		syncAndDelay();
		delay(2000);


		// TODO Auto-generated method stub
		TestOut.println("cree l'acroleine avec hXY (C-O1) = 1.5  hX(O1)= 2 ");
		
		putAtom (PeriodicTable.Entry.C, 80,170);
		putAtom (PeriodicTable.Entry.C, 140,170,80,170);
		putAtom (PeriodicTable.Entry.C, 200,210,140,170);
		putAtom (PeriodicTable.Entry.O1, 260,170,200,210);
		this.changeHXY(3, 4, 1.5);
		Point2D p = this.getImageLocationAtomBySeqNum(4);
		
		this.changeAtom((int)p.getX(), (int)p.getY(), 2.0);
		
		StructureDelocalized s = applet.getCurrentMesomery().getDelocalizedStructure();
		double bonBeta= 8.08;
		TestOut.println("Resultat attendu : ");
		TestOut.println("beta  Etot =  "  + bonBeta);
		TestOut.println("Resultat obtenu : ");
		TestOut.println("beta Etot = "  + s.getTotalEnergy().getBeta());
		if (Maths.round(s.getTotalEnergy().getBeta(),2) != bonBeta ) {	
			TestOut.println("TEST FAILED !!! "); 
			Assert.fail();

		}else{
			TestOut.println("TEST OK.");
		}
	}

}
