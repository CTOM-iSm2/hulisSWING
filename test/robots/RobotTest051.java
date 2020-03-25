package robots;

import java.awt.AWTException;
import java.io.File;
import java.util.Date;

import org.junit.Assert;

import tools.TestOut;
import tools.Utils;
import util.io.SwingIO;
import util.xyz.IOReadxyz;
import views.FrameApp;
import controller.globalCommands.ReadXYZCommand;

public class RobotTest051 extends TutorialCommands {
	


	
	
	public RobotTest051(FrameApp app)  throws AWTException  {
		super (app);
	}
	
	public void go() {
		TestOut.println("* Test le temps de calcul HLP et HLCI ");
		TestOut.println("\n****************  Test C5H5- (Anion)  ****************");

		clickEraseAll();
		syncAndDelay();
		delay(2000);
		SwingIO.setVerbose(false);
		applet.selectMethod  ("HLCI", false);
		applet.selectMethod  ("HLP", false);
		try {
		
			new ReadXYZCommand(applet, new IOReadxyz(new File("./test/datas/xyz/C5H5.xyz") )).execute();
			syncAndDelay();
			this.clickMenuExpert();
			this.clickRemoveMoleculeCharge(1);
			delay (2000);

			this.clickBTGenerateAll();
			System.out.println("autogenerating and delay .... ");
			delay (5000);
			while (applet.getCurrentMesomery().isAutoGenerating());

			System.out.println("dragging slider to beta = 0  and delay .... ");
			this.selectSliderValue(0);
			delay (1000);

			
			System.out.println("select HLCI method. Please weight.... ");
			Date begin = new Date();
			applet.selectMethod  ("HLCI", true);
			TestOut.println("Duration HLCI : " + (new Date().getTime() - begin.getTime()) +" ms" );
			
			applet.selectMethod  ("HLCI", false);	
			System.out.println("select HLP method. Please wait.... ");
			begin = new Date();
			applet.selectMethod  ("HLP", true);
			TestOut.println("Duration HLP : " + (new Date().getTime() - begin.getTime()) +" ms" );
		
			applet.selectMethod  ("HLCI", true);	

			Utils.compareMesomeryXML (applet , "C5H5");

			
			SwingIO.setVerbose(true);

			TestOut.println("TEST OK. Open "+TestOut.OUT_NAME + " to read the speed time");
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			TestOut.println("error opening benzene.xyz");
			Assert.fail();
		}
	}
}
