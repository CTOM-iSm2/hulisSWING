package controller.globalCommands;

import java.awt.event.ActionEvent;

import util.MyDecimalFormat;
import util.MyResourceBundle;
import util.io.SwingIO;
import util.languages.LanguageManager;
import views.FrameApp;
import views.FrameResultMesomery;
import views.prefs.PreferencesGUI;

/**
 * This commands opens the results frame for the mesomery
 * 
 * @author ngoudard
 *
 */
@SuppressWarnings("serial")
public class ActionShowResultMesomery extends ActionCommand implements
		ICommand, IGlobalCommands {
		FrameApp app;

	public ActionShowResultMesomery(FrameApp app) {
		super(IGlobalCommands.SHOW_RESULTS_MESOMERY);

		updateLanguage();
		this.app = app;
	}
	
	public void updateLanguage() {
		MyResourceBundle rb2 = LanguageManager.getInstance().getResource("ToolBarMesomery");
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("ActionsApp");
		setLang(rb2.getString("kresults"),
				bundle.getString("kresults2"), null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		execute();
	}

	public void execute() {
		SwingIO.log(this.getClass().getName(), "execute", "show mesomery results");

		FrameResultMesomery frameResultMesomery = FrameResultMesomery
				.getInstance(this.app);
		frameResultMesomery.setMyDecimalFormat(new MyDecimalFormat(PreferencesGUI.getInstance().getResultsPrecisionMO()), new MyDecimalFormat(PreferencesGUI.getInstance().getResultsPrecisionEnergies()));
		frameResultMesomery.setVisible(true);
		
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