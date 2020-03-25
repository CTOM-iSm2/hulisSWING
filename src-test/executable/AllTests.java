package executable;
/**
 * Tests total energy and weights comparing xml files to .com.test (special gaussian)
 */
import java.awt.AWTException;
import java.util.Date;

import javax.swing.SwingUtilities;

import org.ctom.hulis.huckel.exception.HulisException;
import org.ctom.hulis.huckel.selectors.MesomeryEnergyStructuresSelector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import access.IRightsTest;
import robots.RobotTest033_037;
import robots.RobotTest038_039;
import robots.RobotTest040;
import robots.RobotTest041;
import robots.RobotTest042;
import robots.RobotTest043;
import robots.RobotTest044;
import robots.RobotTest045;
import robots.RobotTest046_049;
import robots.RobotTest050;
import robots.RobotTest051;
import tools.TestOut;
import tools.Utils;
import util.MyJOptionPane;
import util.ThreadUtils;
import views.AppletToFrame;
import views.FrameApp;
import controller.globalCommands.ActionEraseMesomery;
import controller.globalCommands.ActionMenuMethod;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AllTests {

	static volatile FrameApp app;

	/**
	 * Before the test : 
	 * open the applet and select all methods in menu bar
	 * @throws Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ActionMenuMethod.RUNNING_TEST = true;
		ThreadUtils.NEVER_INVOKE_LATER = true;
		MyJOptionPane.setDefaultEnabledModal(false);
		SwingUtilities.invokeAndWait(new Runnable() {

			public void run() {
				app = AppletToFrame.createAndShowGUI(false);
				if (!(app.getRights() instanceof IRightsTest))
					System.out.println("!!! access.Rights must be an instance of ITests !!!");
				Assert.assertTrue(app.getRights() instanceof IRightsTest);


			}});



		TestOut.createOut();
		TestOut.println("*****************************************************");
		TestOut.println("* Test HULIS " + new Date());
		TestOut.println("*****************************************************");
	}


	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		TestOut.closeOut();
		//	app.terminate(0);
		System.gc();
		Thread.sleep(1000);
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	public static void main(String args[]) {
		ActionMenuMethod.RUNNING_TEST = true;
		ThreadUtils.NEVER_INVOKE_LATER = true;
		MyJOptionPane.setDefaultEnabledModal(false);
	/*	try {
			SwingUtilities.invokeAndWait(new Runnable() {

				public void run() {*/
					app = AppletToFrame.createAndShowGUI(false);

			//	}});
	/*	} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		TestOut.createOut();
		TestOut.println("*****************************************************");
		TestOut.println("* Test HULIS " + new Date());
		TestOut.println("*****************************************************");
		new ActionEraseMesomery(app).execute();
		app.selectMethod  ("HLCI", true);
		app.selectMethod  ("HLP", true);
		app.getCurrentMesomery().setStructureSelector(new MesomeryEnergyStructuresSelector());
		Utils.openGaussianAndCompareToXML(app, "acideformique");
		TestOut.closeOut();
		//	app.terminate(0);

	}
	
	
	public void testFile(String file) {
		new ActionEraseMesomery(app).execute();
		app.selectMethod  ("HLCI", true);
		app.selectMethod  ("HLP", true);
		app.getCurrentMesomery().setStructureSelector(new MesomeryEnergyStructuresSelector());
		Utils.openGaussianAndCompareToXML(app, file);
	}

	@Test
	public void test001_AcideFormique() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test001               *");
		TestOut.println("*******************************");
		TestOut.println();
		testFile("acideformique");
	}


	@Test
	public void test002_Acrolein1() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test002               *");
		TestOut.println("*******************************");
		testFile("acrolein1");
	}	


	@Test
	public void test003_Acrolein2() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test003               *");
		TestOut.println("*******************************");
		testFile("acrolein2");
	}

	@Test
	public void test004_AllylCl1() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test004               *");
		TestOut.println("*******************************");
		testFile("allylCl1");
	}

	@Test
	public void test005_AllylCl2() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test005               *");
		TestOut.println("*******************************");
		testFile("allylCl2");
	}

	@Test
	public void test006_AllylF1() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test006               *");
		TestOut.println("*******************************");
		testFile("allylF1");
	}

	@Test
	public void test007_AllylF2() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test007               *");
		TestOut.println("*******************************");
		testFile("allylF2");
	}


	@Test
	public void test008_AllylH1() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test008               *");
		TestOut.println("*******************************");
		testFile("allylH1");
	}

	@Test
	public void test009_AllylH2() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test009               *");
		TestOut.println("*******************************");
		testFile("allylH2");
	}

	@Test
	public void test010_AllylNH21() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test010               *");
		TestOut.println("*******************************");
		testFile("allylNH21");
	}	

	@Test
	public void test011_AllylNH22() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test011               *");
		TestOut.println("*******************************");
		testFile("allylNH22");
	}

	@Test
	public void test012_AllylOH1() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test012               *");
		TestOut.println("*******************************");
		testFile("allylOH1");
	}

	@Test
	public void test013_AllylOH2() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test013               *");
		TestOut.println("*******************************");
		testFile("allylOH2");
	}

	@Test
	public void test014_AlphaIminoCarbonium() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test014               *");
		TestOut.println("*******************************");
		testFile("alpha-imino-carbonium");
	}

	@Test
	public void test015_Amide() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test015               *");
		TestOut.println("*******************************");
		testFile("amide");
	}

	@Test
	public void test016_HCl21() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test016               *");
		TestOut.println("*******************************");
		testFile("BHCl21");
	}

	@Test
	public void test017_BHCl22() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test017               *");
		TestOut.println("*******************************");
		testFile("BHCl22");
	}

	@Test
	public void test018_Butadiene() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test018               *");
		TestOut.println("*******************************");
		testFile("butadiene");
	}

	@Test
	public void test019_Enamine() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test019               *");
		TestOut.println("*******************************");
		testFile("enamine");
	}	

	@Test
	public void test020_Enol() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test020               *");
		TestOut.println("*******************************");
		testFile("enol");
	}		

	@Test
	public void test021_Enolate() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test021               *");
		TestOut.println("*******************************");
		testFile("enolate");
	}	

	@Test
	public void test022_Furan() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test022               *");
		TestOut.println("*******************************");
		testFile("furan");
	}	

	@Test
	public void test023_Pyrrole() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test023               *");
		TestOut.println("*******************************");
		testFile("pyrrole");
	}

	@Test
	public void test024_Thiophene() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test024               *");
		TestOut.println("*******************************");
		testFile("thiophene");
	}

	@Test
	public void test025_Uracil() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test025               *");
		TestOut.println("*******************************");
		testFile("uracil");
	}



	@Test
	public void test026_Imidazolylidene1() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test026               *");
		TestOut.println("*******************************");
		testFile("imidazolylidene1");
	}

	@Test
	public void test027_Imidazolylidene2() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test027               *");
		TestOut.println("*******************************");
		testFile("imidazolylidene2");
	}

	@Test
	public void test028_Imidazolylidene3() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test028               *");
		TestOut.println("*******************************");
		testFile("imidazolylidene3");
	}


	@Test
	public void test029_Imidazolylidene4() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test029               *");
		TestOut.println("*******************************");
		testFile("imidazolylidene4");
	}

	@Test
	public void test030_Imidazolylidene5() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test030               *");
		TestOut.println("*******************************");
		testFile("imidazolylidene5");
	}

	@Test
	public void test031_CCl21() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test031               *");
		TestOut.println("*******************************");
		testFile("CCl21");
	}

	@Test
	public void test032_CCl22() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test032               *");
		TestOut.println("*******************************");
		testFile("CCl22");
	}



	private volatile static  boolean hasRunCCCl21 = false;
	private  volatile static boolean hasRunImidazo1 = false;
	private  volatile static boolean hasRunImidazo2 = false;
	private  volatile static boolean hasRunImidazo3 = false;
	private  volatile static boolean hasRunImidazo4 = false;
	private  volatile  static boolean hasRunImidazo5 = false;

	@Test
	public void test033_Imidazolydene1Interactif() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test033               *");
		TestOut.println("*******************************");

		RobotTest033_037 tutorial;
		try {
			tutorial = new RobotTest033_037(app, 1);
			tutorial.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}


		hasRunImidazo1 = true;

	}

	@Test
	public void test034_Imidazolydene2Interactif() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test034               *");
		TestOut.println("*******************************");
		if (!hasRunImidazo1) 
			this.test033_Imidazolydene1Interactif();

		RobotTest033_037 tutorial;
		try {
			tutorial = new RobotTest033_037(app, 2);	
			tutorial.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}

		hasRunImidazo2= true;

	}

	@Test
	public void test035_Imidazolydene3Interactif() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test035               *");
		TestOut.println("*******************************");

		if (!hasRunImidazo2) 
			this.test034_Imidazolydene2Interactif();


		RobotTest033_037 tutorial;
		try {
			tutorial = new RobotTest033_037(app, 3);
			tutorial.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}


		hasRunImidazo3= true;

	}


	@Test
	public void test036_Imidazolydene4Interactif() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test036               *");
		TestOut.println("*******************************");
		if (!hasRunImidazo3) 
			this.test035_Imidazolydene3Interactif();

		RobotTest033_037 tutorial;
		try {
			tutorial = new RobotTest033_037(app, 4);
			tutorial.go();

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}


		hasRunImidazo4 = true;

	}

	@Test
	public void test037_Imidazolydene5Interactif() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test037               *");
		TestOut.println("*******************************");
		if (!hasRunImidazo4) 
			this.test036_Imidazolydene4Interactif();
		RobotTest033_037 tutorial;
		try {
			tutorial = new RobotTest033_037(app, 5);
			tutorial.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}

		hasRunImidazo5=true;

	}



	@Test
	public void test038_CCl21Interactif() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test038               *");
		TestOut.println("*******************************");
		RobotTest038_039 tutorial;
		try {
			tutorial = new RobotTest038_039(app, 1);
			tutorial.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}



		hasRunCCCl21 = true;

	}

	@Test
	public void test039_CCl22Interactif() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test039               *");
		TestOut.println("*******************************");
		if (!hasRunCCCl21)
			this.test038_CCl21Interactif();

		RobotTest038_039 tutorial;
		try {
			tutorial = new RobotTest038_039(app, 2);
			tutorial.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}


	}


	@Test
	public void test040_Blocs() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test040               *");
		TestOut.println("*******************************");
		TestOut.println("Test les poids sur un bloc");

		RobotTest040 robot;
		try {
			robot = new RobotTest040(app);
			robot.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}





	}


	@Test
	public void test041_BugSumElectrons() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test041               *");
		TestOut.println("*******************************");

		try {

			RobotTest041 tutorial;					
			tutorial = new RobotTest041(app);
			tutorial.go();

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}




	}


	@Test
	public void test042_ModifieHxHydrogene() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test042               *");
		TestOut.println("*******************************");

		try {

			RobotTest042 tutorial;
			tutorial = new RobotTest042(app);
			tutorial.go();

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (HulisException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}

	}


	@Test
	public void test043_BugModifieTypeAtomeEtHx() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test043               *");
		TestOut.println("*******************************");

		try {

			RobotTest043 tutorial;				
			tutorial = new RobotTest043(app);
			tutorial.go();

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}


	}


	@Test
	public void test044_BugAutoCouplage() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test044               *");
		TestOut.println("*******************************");

		try {

			RobotTest044 tutorial;
			tutorial = new RobotTest044(app);
			tutorial.go();

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}



	}


	@Test
	public void test045_AuogenerationAvecPsi1Incorrect() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test045               *");
		TestOut.println("*******************************");

		try {
			RobotTest045 tutorial;
			tutorial = new RobotTest045(app);
			tutorial.go();


		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}



	}


	/****
	 * Dessine Stot et S1 Me-O-C=C-C=C en commencant par Me sans HLP
	 * @throws Exception
	 */	
	@Test
	public void test046_Retire2ElectSurOdebuteParMeSansHLP() throws Exception {

		TestOut.println("*******************************");
		TestOut.println("* Debut test046               *");
		TestOut.println("*******************************");

		try {
			RobotTest046_049 tutorial = new RobotTest046_049(app, 1);
			tutorial.go();

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}




	}


	/****
	 * Dessine Stot et S1 Me-O-C=C-C=C en commencant par C sans HLP
	 * @throws Exception
	 */	
	@Test
	public void test047_Retire2ElectSurOdebuteParCSansHLP() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test047               *");
		TestOut.println("*******************************");

		try {
			RobotTest046_049 tutorial = new RobotTest046_049(app, 2);
			tutorial.go();


		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}




	}


	/****
	 * Dessine Stot et S1 Me-O-C=C-C=C en commencant par Me avec HLP
	 * @throws Exception
	 */	
	@Test
	public void test048_Retire2ElectSurOdebuteParMeAvecHLP() throws Exception {
		TestOut.println("*******************************");
		TestOut.println("* Debut test048               *");
		TestOut.println("*******************************");

		try {

			RobotTest046_049 tutorial = new RobotTest046_049(app, 3);
			tutorial.go();

		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}


	}


	/****
	 * Dessine Stot et S1 Me-O-C=C-C=C en commencant par C avec HLP
	 * @throws Exception
	 */	
	@Test
	public void test049_Retire2ElectSurOdebuteParCAvecHLP()  throws Exception {

		TestOut.println("*******************************");
		TestOut.println("* Debut test049               *");
		TestOut.println("*******************************");

		try {
			RobotTest046_049 tutorial;
			tutorial = new RobotTest046_049(app, 4);
			tutorial.go();


		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}

	}




	@Test
	public void test050_VitesseCalculBenzene (){
		TestOut.println("*******************************");
		TestOut.println("* Debut test050               *");
		TestOut.println("*******************************");

		RobotTest050 robot;
		try {
			robot = new RobotTest050(app);
			robot.go();


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}

	}


	@Test
	public void test051_VitesseCalculAnionC5H5() {
		TestOut.println("*******************************");
		TestOut.println("* Debut test051               *");
		TestOut.println("*******************************");

		RobotTest051 robot;
		try {
			robot = new RobotTest051(app);
			robot.go();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail();
		}

	}


}
