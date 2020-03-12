package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.io.SwingIO;
import views.FrameApp;

/**
 * This command applies the default zoom to the structures
 * @author ngoudard
 *
 */
public class ZoomD implements ICommand, ActionListener {
	FrameApp app;

	public ZoomD(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "default zoom");
		this.app.getCurrentMesomeryView().setZoom(this.app.getCurrentMesomeryView().DEFAULT_ZOOM);
		this.app.getCurrentMesomeryView().refresh();

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
