package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.io.SwingIO;
import views.FrameApp;


/**
 * This command zooms in the structures
 * 
 * @author ngoudard
 *
 */
public class ZoomP  implements ICommand, ActionListener {
	FrameApp app;

	public ZoomP(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent arg0) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "zoom in");
		double tmp = this.app.getCurrentMesomeryView().getZoom() + this.app.getCurrentMesomeryView().INCR_ZOOM;
		if (tmp < this.app.getCurrentMesomeryView().MAX_ZOOM) {
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
