package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.io.SwingIO;
import views.FrameApp;

/**
 * This command zooms out the structures
 * 
 * @author ngoudard
 *
 */
public class ZoomM implements ICommand, ActionListener {
	FrameApp app;

	public ZoomM(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "zoom out");
		double tmp = this.app.getCurrentMesomeryView().getZoom() - this.app.getCurrentMesomeryView().INCR_ZOOM;
		if (tmp > this.app.getCurrentMesomeryView().MIN_ZOOM) {
			this.app.getCurrentMesomeryView().setZoom(tmp);
			this.app.getCurrentMesomeryView().refresh();
		}

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
