package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.io.SwingIO;
import views.FrameApp;

/**
 * This command is called when the user selects the option to display polynomes on the structures
 * 
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class ActionDisplayPolynomes implements ActionListener {
	FrameApp app;

	public ActionDisplayPolynomes(FrameApp app) {
		this.app = app;
		}

	public void actionPerformed(ActionEvent e) {
		execute();
	}

	
	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "display polynomes");
		this.app.getCurrentMesomeryView().switchPolynomes();
		this.app.getCurrentMesomeryView().refresh();
	}

}