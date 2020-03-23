package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.io.SwingIO;
import views.prefs.PreferencesGUI;

public class ClearPreferences implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		SwingIO.log(this.getClass().getName(), "init",
					"clear all preferences");
		PreferencesGUI.getInstance().clearPreferences();
		
		
	}

}
