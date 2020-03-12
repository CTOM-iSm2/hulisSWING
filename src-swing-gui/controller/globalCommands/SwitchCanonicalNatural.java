package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ctom.hulis.prefs.Preferences;

import util.io.SwingIO;
import views.FrameApp;


/**
 * This command switches canonical and natural orbitals
 * 
 * @author ycarissan
 *
 */
public class SwitchCanonicalNatural implements ICommand, ActionListener {
	FrameApp app;

	public SwitchCanonicalNatural(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "Natural <-> Canonical");
			
		SwingIO.PrintIfln("Natural <-> Canonical");
		Preferences.switchDISPLAY_ORBITAL();
		app.getCurrentMesomeryView().getCurrentStructureView().setShownOrbital(null);
		app.getCurrentMesomeryView().getCurrentStructureView().repaint();
		app.getCurrentMesomeryView().getCurrentStructureView().getGraphOM().repaint();

//		this.app.getCurrentMesomery().preferenceChanged();
	}

	/**
	 * 
	 * @return
	 */
	public boolean isSucceed() {
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFailReason(){
		return "";
	}
}
