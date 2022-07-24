package robots;

import java.awt.AWTException;
import java.io.File;
import java.util.Date;

import org.junit.Assert;

import tools.TestOut;
import tools.Utils;
import util.MyJOptionPane;
import util.ThreadUtils;
import util.io.SwingIO;
import util.xyz.IOReadxyz;
import views.AppletToFrame;
import views.FrameApp;
import controller.globalCommands.ActionEraseMesomery;
import controller.globalCommands.ActionMenuMethod;
import controller.globalCommands.ReadXYZCommand;

public class RobotTest050 extends TutorialCommands {
	
	
	public static void main (String[] args){
		final FrameApp app;
		ActionMenuMethod.RUNNING_TEST = true;
		ThreadUtils.NEVER_INVOKE_LATER = true;
		MyJOptionPane.setDefaultEnabledModal(false);
		app = AppletToFrame.createAndShowGUI(false);
					
			

		   
		
		TestOut.createOut();
		TestOut.println("*****************************************************");
		TestOut.println("* Test HULIS " + new Date());
		TestOut.println("*****************************************************");
		TestOut.println("* Test le temps de calcul HLP et HLCI ");
		TestOut.println("\n****************  Test Speed Benzene  ****************");
	
	
		RobotTest050 robot;
		try {
			new ActionEraseMesomery(app).execute(); 
			robot = new RobotTest050(app);
			robot.go();
			TestOut.println("TEST OK");
	
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			TestOut.println("TEST FAILED !!!");
			Assert.fail();
		}
	
	}
	
	
	
	
	
	
	public RobotTest050(FrameApp app)  throws AWTException  {
		super (app);
	}
	
	public void go() {
		TestOut.println("* Test le temps de calcul HLP et HLCI ");
		TestOut.println("\n****************  Test Speed Benzene  ****************");

		clickEraseAll();
		syncAndDelay();
		delay(2000);
		//applet.selectMethod  ("HLCI", false);
		applet.selectMethod  ("HLP", false);
		try {

			new ReadXYZCommand(applet, new IOReadxyz(new File("./test/datas/xyz/benzene.xyz") )).execute();
			syncAndDelay();
			this.clickMenuExpert();
			delay (2000);
	
			// on genere tout sans hlp ni hlci
			this.clickBTGenerateAll();
			System.out.println("autogenerating and delay .... ");
			delay (5000);
			while (applet.getCurrentMesomery().isAutoGenerating());
			
			
			// on selectionne tout

			System.out.println("dragging slider to beta = -2  and delay .... ");
			this.selectSliderValue(-2.0);
			delay (1000);
				
			
			// on selection hlp et on calcule la duree d'execution
			System.out.println("Select HLCI  method. Please wait.... ");
			Date begin = new Date();
			applet.selectMethod  ("HLCI", true);
			syncAndDelay();
			TestOut.println("Duree HLCI : " + (new Date().getTime() - begin.getTime()) +" ms" );
			
			// on deselect hlci et on selectionne  hlp et on calcule la duree d'execution

			applet.selectMethod  ("HLCI", false);	
			System.out.println("Select HLP method. Please wait.... ");
			begin = new Date();
			applet.selectMethod  ("HLP", true);
			TestOut.println("Duree HLP : " + (new Date().getTime() - begin.getTime()) +" ms" );
		
			applet.selectMethod  ("HLCI", true);	

			Utils.compareMesomeryXML (applet , "benzene");
			
			
			TestOut.println("TEST OK");

			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
			Assert.fail();
		}
	}
}
