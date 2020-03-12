package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.io.SwingIO;
import views.FrameApp;
import views.prefs.PreferencesGUI;

/**
 * This command is called when the user selects the option to display polynomes on the structures
 * 
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class SuperMario implements ActionListener {
	FrameApp app;

	public SuperMario(FrameApp app) {
		this.app = app;
		}

	public void actionPerformed(ActionEvent e) {
		execute();
	}

	
	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "Super Mario");
		PreferencesGUI.switchSuperMario();
		this.app.getCurrentMesomeryView().refresh();
	}

}