package controller.globalCommands;

import java.awt.event.ActionEvent;

import access.Rights;
import util.io.SwingIO;
import views.FrameApp;
import views.StructureView;

/**
 * This command is called when the user enter in "bloc" mode.
 * The Bloc button becomes non-visible and the End Bloc button is selected end become visible
 * 
 * @author Nicolas
 * 
 */
@SuppressWarnings("serial")
public class ActionModeBloc extends ActionChangeMode implements ICommand {
	FrameApp app;

	public ActionModeBloc(FrameApp app) {
		super(app, IGlobalCommands.BLOC);
		this.app = app;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "mode bloc selected");
		app.toolBarMesomery.btBloc.setVisible(false);
		app.toolBarMesomery.btEndBloc.setVisible(true && Rights.ALLOW_BLOCKS);
		app.toolBarMesomery.btEndBloc.setSelected(true);
		StructureView currentStructureView = app.getCurrentMesomeryView().getCurrentStructureView();
	

		EnableActionManager.getInstance().updateActionsToEnable(app);
		currentStructureView.repaint(); // repeindre pour afficher le message "terminer votre bloc"
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
