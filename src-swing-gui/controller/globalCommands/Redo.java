package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.io.SwingIO;
import views.FrameApp;

/*** This command redoes an action restoring a state of the applet
 * It restores the mesomery and the current structure from the UndoRedoManager
 * 
 * @author ngoudard
 *
 */
public class Redo  implements ICommand, ActionListener{
	
	FrameApp app;
	
	public Redo(FrameApp app) {
		this.app = app;
	}

	

	public void actionPerformed(ActionEvent e) {
		try {
			this.execute();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
	}

	public void execute() throws Exception {
		SwingIO.log(this.getClass().getName(), "execute", "redo");
		this.app.getStateManager().redo();	
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
