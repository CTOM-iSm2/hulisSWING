package views;

import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import sqlbridge.ConnectSQL;
import util.MyLocale;
import util.MyResourceBundle;
import util.languages.LanguageManager;
import views.prefs.PreferencesGUI;

/**
 * 
 * @author macbook
 *
 */
public class Informations  {
	
	private static FrameApp app;

	/**
	 * 
	 * @param app
	 */
	public static void setFrameApp (FrameApp app) {
		Informations.app = app;
	}
	
	/**
	 * 
	 */
	public Informations() {
		MyResourceBundle bundle = LanguageManager.getInstance().getResource("informations");
		
		String informations = "<html><br>";
		informations += bundle.getString("message") + "<br><br>";
		informations += bundle.getString("nb_use") + app.getId() + "<br>";
		informations += bundle.getString("country") + ConnectSQL.getProbableCountry() + "<br>";
		informations += bundle.getString("type_of_use") + app.getTypeOfUse() + "<br>";
		informations += bundle.getString("total_use") + PreferencesGUI.getInstance().getTotalUse() + "<br>";
		informations += bundle.getString("java_lang") + Locale.getDefault() + "<br>";
		informations += bundle.getString("java_version") + Locale.getDefault().getCountry() + "<br>";
		informations += bundle.getString("os") + MyLocale.getJavaVersion() + "<br>";
		informations += bundle.getString("browser") + app.getBrowser() + "<br>";
		informations += bundle.getString("hulis_version") + app.getVersion()  + "<br>";
		informations += bundle.getString("start_time") + app.getTimeToStart() +  "<br>";
		informations += bundle.getString("date_applet")  + app.getTimeBeginUse() + "<br>";
		informations += bundle.getString("duration") + ( new Date().getTime() - app.getTimeBeginUse() )+  "<br>";
		informations += bundle.getString("error") +   (ErrorReport.isErrorReported() ? 1 : 0)+ "<br>";
		informations += bundle.getString("last_command") + app.getLastAction() +  "<br>";
		informations += "</html>";
		
		
		new SimpleDialogScrollText((JFrame) JOptionPane.getFrameForComponent(app), bundle.getString("informations"), informations);
	}
	
}
