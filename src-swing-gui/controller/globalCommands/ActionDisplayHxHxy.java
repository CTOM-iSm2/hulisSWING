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
public class ActionDisplayHxHxy extends ActionCommand implements ICommand,
		IGlobalCommands {
	FrameApp app;

	public ActionDisplayHxHxy(FrameApp app) {
		super(IGlobalCommands.DISPLAY_INFOS);

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
				rb3.getString("kinfos"), bundle.getString("kinfos1"), null);
	}
	
	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "switch display hx-hxy");
		this.app.getCurrentMesomeryView().switchHxHxy();
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