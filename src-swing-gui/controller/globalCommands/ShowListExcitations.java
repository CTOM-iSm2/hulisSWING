package controller.globalCommands;

import java.awt.event.ActionEvent;

import org.ctom.hulis.huckel.structures.Structure;

import util.io.SwingIO;
import views.FrameApp;
import views.FrameExcitations;

/**
 * This commands shows the excitations list frame for the current structure
 * 
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class ShowListExcitations extends ActionCommand implements ICommand,
		IGlobalCommands {
	FrameApp app;

	public ShowListExcitations(FrameApp app) {
		super(IGlobalCommands.SHOW_LIST_EXCITATIONS);
		
		this.app = app;
	}
	

	
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "show list excitations");
	// app.statusBar.setTxtState(null);
		Structure structure = this.app.getCurrentMesomeryView().getCurrentStructure();
		FrameExcitations.getInstance(structure).setVisible(true);
		
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
