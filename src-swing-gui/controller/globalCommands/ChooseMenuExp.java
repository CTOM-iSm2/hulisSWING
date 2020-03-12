package controller.globalCommands;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import util.io.SwingIO;
import views.FrameApp;
import access.Rights;

/**
 * This command is called when the user choose expert mode.
 * It adds buttons.
 * 
 * @author ngoudard
 *
 */
public class ChooseMenuExp implements ICommand, ActionListener {
	FrameApp app;

	public ChooseMenuExp(FrameApp app) {
		this.app = app;
	}

	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "choose expert menu");
		

		
		boolean expert = this.app.menuBar.menuExp.isSelected();
		//this.app.toolBarMesomery.btAutoGenStruct.setVisible(expert);
		this.app.toolBarMesomery.btCouple.setVisible(expert);
		this.app.toolBarMesomery.btBloc.setVisible(Rights.ALLOW_BLOCKS && expert);
		this.app.toolBarMesomery.btCouple.setVisible(expert);
		this.app.menuBar.menuExcitations.setVisible(Rights.ALLOW_EXCITATIONS && expert);
		this.app.menuBar.menuBlocs.setVisible(Rights.ALLOW_BLOCKS && expert);
		
		this.app.setExpertMode(expert);

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
