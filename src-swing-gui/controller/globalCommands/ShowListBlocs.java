package controller.globalCommands;

import java.awt.event.ActionEvent;

import util.io.SwingIO;
import views.FrameApp;
import views.FrameBlocs;
import views.StructureView;

/**
 * This commands shows the blocs list frame for the current structure
 * 
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class ShowListBlocs extends ActionCommand implements ICommand,
		IGlobalCommands {
	FrameApp app;

	public ShowListBlocs(FrameApp app) {
		super(IGlobalCommands.SHOW_LIST_BLOCS);
		
		this.app = app;
	}
	

	
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "show list blocks");
	// app.statusBar.setTxtState(null);
		StructureView structureView = this.app.getCurrentMesomeryView().getCurrentStructureView();
		FrameBlocs.getInstance(structureView).setVisible(true);
		
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
