package robots;

import java.awt.AWTException;

import org.ctom.hulis.huckel.PeriodicTable;

import tools.TestOut;
import tools.Utils;
import util.MyJOptionPane;
import util.ThreadUtils;
import views.AppletToFrame;
import views.FrameApp;

public class RobotTest040 extends TutorialCommands {
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		final FrameApp app;
		ThreadUtils.NEVER_INVOKE_LATER = true;
		MyJOptionPane.setDefaultEnabledModal(false);
		app = AppletToFrame.createAndShowGUI(false);
		TestOut.createOut();	
	
		try {
			RobotTest040 robot = new RobotTest040(app);
			robot.go();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public RobotTest040(FrameApp applet) throws AWTException {
		super(applet);
	}

	@Override
	public void go() {
		TestOut.println("objet du test : vérifie que le bloc est bien cree et que les poids de la mesomerie soient corrects");
		
		this.clickMenuHLCI();
		this.clickMenuHLP();
		
		syncAndDelay();
		delay(2000);
		clickEraseAll();

		TestOut.println("-- Cree le butadiene");
		putAtom (PeriodicTable.Entry.C, 80,70);
		putAtom (PeriodicTable.Entry.C, 50,100,80,70);
		putAtom (PeriodicTable.Entry.C, 50,140,50,100);
		putAtom (PeriodicTable.Entry.C, 80,170,50,140);
		putAtom (PeriodicTable.Entry.C, 110,140,80,170);
		putAtom (PeriodicTable.Entry.C, 110,100,110,140);
		this.link(110, 100, 80, 70);
		putAtom (PeriodicTable.Entry.C, 140,170,110,140);
		putAtom (PeriodicTable.Entry.C, 170,140,140,170);
		putAtom (PeriodicTable.Entry.C, 200,170,170,140);
		putAtom (PeriodicTable.Entry.C, 230,140,200,170);
System.out.println(applet.menuBar.menuExp.isSelected());
		if (!applet.menuBar.menuExp.isSelected()) {
			TestOut.println("-- active menu expert");
			this.clickMenuExpert();
			syncAndDelay();
		}
		this.clickBTBloc();
		this.selectRectangleBloc(40, 60, 120, 180);
		this.clickBTEndBloc();

		this.clickCreate1();
		this.putElectrons(7, 8, 2);
		this.putElectrons(9, 10, 2);

		this.clickCreate1();
		this.putElectrons(8, 9, 2);
		this.putElectrons(10, 2);
		
		this.clickCreate1();
		this.putElectrons(7, 2);
		this.putElectrons(8, 9, 2);
		
		delay(2000);
		Utils.compareMesomeryXML (applet,"benzene_N");

	}



}
