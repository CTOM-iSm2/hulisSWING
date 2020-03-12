package sqlbridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import util.MyLocale;
import util.io.SwingIO;
import views.FrameApp;
import views.prefs.PreferencesGUI;
import access.Rights;

public class RegisterNewUSer extends Thread {

	private FrameApp app;

	public RegisterNewUSer(FrameApp app) {
		this.app = app;
	}

	public void run() {	
	
		try {
			
			// On initialise l'URL
					
			String url = ConnectSQL.USER_URL + "?hulis_type_of_use=" + app.getTypeOfUse()
			+ "&total_use=" + PreferencesGUI.getInstance().getTotalUse()
			+ "&jvm_localisation=" + Locale.getDefault()
			+ "&java_version=" + MyLocale.getJavaVersion().replace(" ", "%20")
			+ "&operating_system=" + MyLocale.getOSName().replace(" ", "%20")
			+"&browser=" +  ((app.getBrowser() != null) ? app.getBrowser().replace(" ", "%20") : "")
			+"&hulis_version=" + app.getVersion().toString().replace(" ", "%20")
			+"&ms_start=" + app.getTimeToStart();
	
			URL u = new URL(url);
	
			InputStream i = u.openStream();
			BufferedReader b = new BufferedReader(new InputStreamReader(i));
			int id = 0;
			String s = null;
			while  ( ((s= b.readLine() ) != null) && ((id ==0) )) {  // lit chaque ligne jusqu'a ce qu'on tombe sur une ligne non vide
	
				try {
					if ( !(s = s.trim()).equals("")) 
						id = Integer.valueOf(s);
				} catch (NumberFormatException e) {
					//exception normale si s != nombre
	
				}
			}
	
			app.setId(id );
			SwingIO.log(this.getClass().getName(), "run", "RegisterNewUSer ID = " + id);
		} catch (MalformedURLException e) {
			SwingIO.warning(this.getClass().getName(), "run",
					"RegisterNewUSer cannot register user : bad url", e);
		} catch (IOException e) {
			SwingIO
			.warning(
					this.getClass().getName(),
					"run",
					"RegisterNewUSer cannot register user : network error",
					e);
		}
	
	
	
	}
}
