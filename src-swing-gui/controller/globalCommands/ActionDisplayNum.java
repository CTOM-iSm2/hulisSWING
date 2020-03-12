package controller.globalCommands;

import java.awt.event.ActionEvent;

import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;
/**
 *  This command is called when the user slects the option to display the huckel numbering
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class ActionDisplayNum extends ActionCommand implements  ICommand,
		IGlobalCommands {
	FrameApp app;

	public ActionDisplayNum(FrameApp app) {
		super(IGlobalCommands.DISPLAY_NUM);
		this.app = app;
		updateLanguage();
	}
	public void updateLanguage() {
		MyResourceBundle rb3 = LanguageManager.getInstance().getResource("ToolBarHuckel");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(
				rb3.getString("knum"), bundle.getString("knum1"), null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "switch display atoms sequence numbers");
		this.app.getCurrentMesomeryView().switchNum();
		this.app.getCurrentMesomeryView().refresh();
		this.app.statusBar.setTxtState(null);

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
