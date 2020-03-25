package robots;

import java.awt.AWTException;
import java.awt.geom.Point2D;

import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.junit.Assert;

import tools.TestOut;
import views.FrameApp;

public class RobotTest041 extends TutorialCommands {

	public RobotTest041(FrameApp applet) throws AWTException {
		super(applet);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void go() {
		// TODO Auto-generated method stub
		try {
			TestOut.println("Probleme corrige : Quand une molecule est chargee, le fait de modifier un atome provoque une erreur de charge et de nombre d'electrons sur les orbitales");
			TestOut.println("Test effectue : C-C-C-N2");
			TestOut.println("On met une charge sur la molecule et on remplace l'atome N2 par un atome Cl. La somme des electrons des orbitales doit etre correcte.");
			
			applet.selectMethod  ("HLCI", true);
			applet.selectMethod  ("HLP", false);
			
			clickEraseAll();

			syncAndDelay();
			delay(2000);

			putAtom (PeriodicTable.Entry.C, 80,170);
			putAtom (PeriodicTable.Entry.C, 140,170,80,170);
			putAtom (PeriodicTable.Entry.C, 200,210,140,170);
			putAtom (PeriodicTable.Entry.N2, 260,170,200,210);
			clickAddMoleculeCharge(1);
			Point2D p = this.getImageLocationAtomBySeqNum(4);
			changeAtom ((int)p.getX(), (int)p.getY(),PeriodicTable.Entry.Cl);

			StructureDelocalized s = applet.getCurrentMesomery().getDelocalizedStructure();
			TestOut.println("Resultat attendu :" );
			TestOut.println("charge : 1" );
			TestOut.println("sum of eletrons on orbitals : 4"  );		
			TestOut.println("Resultat obtenu  :" );
			TestOut.println("charge : " + -s.getNbElecCharge() );
			TestOut.println("sum of eletrons on orbitals : " + s.getSumOfDistributedElec()  );
			if (s.getNbElecCharge() !=-1 && s.getSumOfDistributedElec() != 4) {	
				TestOut.println("TEST FAILED  !!!!");		
				Assert.fail();

			}else{
				TestOut.println("TEST OK");
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
