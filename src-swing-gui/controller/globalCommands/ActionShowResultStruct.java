package controller.globalCommands;

import java.awt.event.ActionEvent;

import util.MyDecimalFormat;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;
import views.FrameResultStruct;
import views.prefs.PreferencesGUI;

/**
 * This commands opens the results frame for the current structure
 * 
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class ActionShowResultStruct extends ActionCommand implements ICommand,
		IGlobalCommands {
	FrameApp app;

	public ActionShowResultStruct(FrameApp app) {
		super(IGlobalCommands.SHOW_RESULTS);
		updateLanguage();
		this.app = app;
	}
	
	public void updateLanguage () {
		MyResourceBundle rb3 = LanguageManager.getInstance().getResource("ToolBarHuckel");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(
				rb3.getString("kresults"), bundle.getString("kresults1"),
				null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "show structure results");
		// app.statusBar.setTxtState(null);
		FrameResultStruct frameResultStruct = FrameResultStruct.getInstance(this.app);
		frameResultStruct.setMyDecimalFormat(new MyDecimalFormat(PreferencesGUI.getInstance().getResultsPrecisionMO()), new MyDecimalFormat(PreferencesGUI.getInstance().getResultsPrecisionEnergies()));
		frameResultStruct.setVisible(true);
			
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
