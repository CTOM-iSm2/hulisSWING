package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.ctom.hulis.prefs.Preferences;

import util.io.SwingIO;
import views.FrameApp;


/**
 * This command switches OM and density
 * 
 * @author ycarissan
 *
 */
public class SwitchDensity implements ICommand, ActionListener {
	FrameApp app;

	public SwitchDensity(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "OM <-> Density");

		SwingIO.PrintIfln("OM <-> Density");
		Preferences.switchDISPLAY_DENSITY();
		/* NG : ne devrait pas se trouver ici sinon cela modifie la preference utilisateur. déplacement de la condition dans OrbitalDrawer.getScaleOrbitals();
		if (Preferences.isDISPLAY_DENSITY()) {
			PreferencesGUI.setSCALE_ORB(PreferencesGUI.getScaleOrbDefault()/4);
		} else {
			PreferencesGUI.setSCALE_ORB(PreferencesGUI.getScaleOrbDefault());
		}*/
		app.getCurrentMesomeryView().getCurrentStructureView().repaint();
		app.getCurrentMesomeryView().getCurrentStructureView().getGraphOM().repaint();

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
