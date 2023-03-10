package sqlbridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

import access.Rights;
import util.Version;
import util.io.SwingIO;
import views.FrameApp;

public class ConnectSQL {

	public final static String PROBABLE_COUNTRY_URL = "http://www.hulis.free.fr/register/getPaysProbable.php";
	public final static String CONNECT_URL = "http://www.hulis.free.fr/register/connectapplet.php";
	public final static String CURRENT_VERSION = "http://www.hulis.free.fr/register/currentversion.php";
	public final static String DECONNECT_URL = "http://www.hulis.free.fr/register/deconnectapplet.php";
	public final static String DURATION = "http://www.hulis.free.fr/register/duree.php";
	public final static boolean STOP_DATABASE_CONNECTION = Rights.STOP_DATABASE_CONNECTION;
	public final static String TEST_VERSION = "http://www.hulis.free.fr/register/testversion.php";
	public final static String USER_URL = "http://www.hulis.free.fr/register/getnewuser.php";

	
	private  static ConnectSQL instance = null;
	
	public static void clear() {
		instance = null;
	}
	
	public static ConnectSQL getInstance() {
		if (instance == null)
			instance =  new ConnectSQL();
		return instance;
	}
	

	public static String getProbableCountry() {
		if (ConnectSQL.STOP_DATABASE_CONNECTION)
			return Locale.getDefault().getCountry();

		try {
			
			URL u = new URL(ConnectSQL.PROBABLE_COUNTRY_URL);
			InputStream i = u.openStream();
			BufferedReader b = new BufferedReader(new InputStreamReader(i));
			String s = null;
			while  ( ((s= b.readLine() ) != null))  // lit chaque ligne jusqu'a ce qu'on tombe sur une ligne non vide
				if (!s.trim().equals("")) 
					return s;
	
		} catch (MalformedURLException e) {
			SwingIO.warning("ConnectSQL", "getProbableCountry",
					"maformed url", e);
		} catch (IOException e) {
			SwingIO
			.warning(
					"ConnectSQL",
					"getProbableCountry",
					"network error",
					e);
		}

		return Locale.getDefault().getCountry();
	}
	

	public static Version getCurrentVersion() {
		if (ConnectSQL.STOP_DATABASE_CONNECTION)
			return new Version(0, 0, 0);

		try {
			URL u = new URL(ConnectSQL.CURRENT_VERSION);
			InputStream i = u.openStream();

			BufferedReader b = new BufferedReader(new InputStreamReader(i));

			int maj = Integer.valueOf(b.readLine());
			int min = Integer.valueOf(b.readLine());
			int corr = Integer.valueOf(b.readLine());

			return new Version(maj, min, corr);

		} catch (NumberFormatException e) {
			SwingIO.warning("ConnectSQL", "getCurrrentVersion",
					"bad number format", e);
		} catch (MalformedURLException e) {
			SwingIO.warning("ConnectSQL", "getCurrrentVersion",
					"malformed url", e);
		} catch (IOException e) {
			SwingIO
			.warning(
					"ConnectSQL",
					"getCurrrentVersion",
					"network error",
					e);
		}

		return null;
	}

	public static Version getMinimalVersion() {
		if (ConnectSQL.STOP_DATABASE_CONNECTION)
			return new Version(0, 0, 0);

		try {
			
			URL u = new URL(ConnectSQL.TEST_VERSION);
			InputStream i = u.openStream();

			BufferedReader b = new BufferedReader(new InputStreamReader(i));

			int maj = Integer.valueOf(b.readLine());
			int min = Integer.valueOf(b.readLine());
			int corr = Integer.valueOf(b.readLine());
			return new Version(maj, min, corr);

			// else return false;
		} catch (NumberFormatException e) {
			SwingIO.warning("ConnectSQL", "getMinimalVersion",
					"bad number format", e);
		} catch (MalformedURLException e) {
			SwingIO.warning("ConnectSQL", "getMinimalVersion",
					"malformed url", e);
		} catch (IOException e) {
			SwingIO
			.warning(
					"ConnectSQL",
					"getMinimalVersion",
					"network error",
					e);
		}

		return null;
	}

	public static  boolean isOutOfDateVersion(Version v) {

		if (ConnectSQL.STOP_DATABASE_CONNECTION)
			return false;

		Version version = getMinimalVersion();
		if (version == null)
			return false;
		
		return (v.compareTo(version) < 0);

	}



	public  boolean registerDisconnectApplet(FrameApp app) {
		if (ConnectSQL.STOP_DATABASE_CONNECTION)
			return true;

		boolean registerOK = false;
		RegisterDisconnectApplet t = new RegisterDisconnectApplet(app.getId());

		t.start();
		try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registerOK = true;
		return registerOK;
	}

	public  boolean registerDuration(FrameApp app) {
		if (ConnectSQL.STOP_DATABASE_CONNECTION)
			return true;

		boolean registerOK = false;
		RegisterDuration t = new RegisterDuration(app.getId());
		t.start();
		registerOK = true;
		return registerOK;
	}

	public  boolean registerDuration(FrameApp app, String lastCommand) {
		if (ConnectSQL.STOP_DATABASE_CONNECTION)
			return true;

		boolean registerOK = false;
		app.setLastAction(lastCommand);
		RegisterDuration t = new RegisterDuration(app.getId(), lastCommand);
		t.start();
		registerOK = true;
		return registerOK;
	}
	
	public  boolean registerNewHulisUser(FrameApp app) {
		if (ConnectSQL.STOP_DATABASE_CONNECTION)
			return true;

		boolean registerOK = false;
		RegisterNewUSer t = new RegisterNewUSer(
				app);
		t.start();
		registerOK = true;
		return registerOK;
	}

	

}
