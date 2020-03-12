package controller.globalCommands;

import java.awt.event.ActionEvent;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;

/**
 * This command is called when the user selects the option to display text on the structures
 * 
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class ActionDisplayCharges extends ActionCommand implements ICommand,
		IGlobalCommands {
	FrameApp app;

	public ActionDisplayCharges(FrameApp app) {
		super(IGlobalCommands.DISPLAY_CHARGES);

		this.app = app;
		updateLanguage();
		}

	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void updateLanguage() {
		MyResourceBundle rb3 = LanguageManager.getInstance().getResource("ToolBarHuckel");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(
				rb3.getString("kcharges"), bundle.getString("kcharges1"), null);
	}
	
	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "switch display charges");
		this.app.getCurrentMesomeryView().switchCharges();
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