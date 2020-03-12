package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.io.SwingIO;
import views.FrameApp;

/*** This command cancels an action restoring a state of the applet
 * 
 * @author ngoudard
 *
 */
public class Undo  implements ICommand, ActionListener{
	
	FrameApp app;
	
	public Undo(FrameApp app) {
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
		SwingIO.log(this.getClass().getName(), "execute", "undo");
		this.app.getStateManager().undo();	
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
