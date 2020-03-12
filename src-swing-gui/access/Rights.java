package access;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;

import sqlbridge.ConnectSQL;
import util.io.SwingIO;
import views.FrameApp;
import views.prefs.PreferencesGUI;

/**
 * This class checks the security rights when the software is started.
 * 
 * Uncomment the header of the class :
 * - To create a test version, implement the IRightsTest interface.
 * - To create an official version, on the web site, implement IRightsOfficialApplet
 * - To create a standalone version, implement IRightsEvaluationApplet
 * 
 * The security algorithm works like it :
 * - if CHECK_USER_ACCESS_RIGHTS = false 
 *    -> user is allowed, exit checks
 * - if applet is executed on internet and not ALLOW_EXECUTE_ON_INTERNET 
 *   or 
 *   url applet is not executed on internet ALLOW_EXECUTE_STANDALONE 
 *      -> user is not authorized, reject reason = MODE_EXECUTION, exit checks
 * - if applet is executed on internet  and url is not URL_ALLOWED or !ALLOW_ON_ANY_URL 
 *      -> user is not authorized, reject reason = NOT_ALLOWED_URL, exit checks
 * - if url starts with "file:" and ALLOW_EXECUTE_APPLET_VIEWER = false
 *      -> user is nit authorized, reject reason = MODE_EXECUTION, exit checks
 * - if user executes the applet on internet
 *      -> user is authorized, exit checks
 *  else (standalone) 
 * - if the user has not network and ALLOW_WITHOUT_NETWORK = false
 *      -> user is not authorized, reject reason = NETWORK_ERROR, exit checks
 * - if out of date version and  BYPASS_CHECK_MINIMAL_VERSION_IN_DATABASE = false
 *      -> user is not authorized, reject reason = OUTOFDATEVERSION, exit checks
 * - if system date > limit date and  BYPASS_LIMIT_DATE = false
 *      -> user is not authorized, reject reason = OUTOFDATEVERSION, exit checks
 */
//public class Rights implements IRightsEvaluationAppletNRT {
public class Rights implements IRightsTest {
//public class Rights implements IRightsOfficialApplet {
//public class Rights implements IRightsEvaluationApplet {


	// Reject reasons
	public static enum RejectReason {NETWORK_ERROR, MODE_EXECUTION, NOT_ALLOWED_URL, OUTOFDATEVERSION};

	// variables
	private boolean isValidUser;

	private RejectReason rejectReason;

	
	/**
	 * check permissions
	 * @param app
	 */
	public Rights(FrameApp app) {
		URL url = app.getCodeBase();
			
		SwingIO.log(this.getClass().getName(), "Rights",
				"Checking security controls for HuLis...");
		
		// le user est toujours valide si la securite n'est pas activee
		if (!Rights.CHECK_USER_ACCESS_RIGHTS) {
			SwingIO.warning(this.getClass().getName(), "Rights",
					"Security is disabled !!!");

			this.isValidUser = true;
			this.rejectReason = null;
			return;
		}
		SwingIO.log(this.getClass().getName(), "Rights",
				"Security is enabled.");

		
		SwingIO.log(this.getClass().getName(), "Rights",
				"check mode connexion");
		// if url of an applet
		if ((  app.isExecutedOnInternet()  && !Rights.ALLOW_EXECUTE_ON_INTERNET)
				|| (  !app.isExecutedOnInternet()   && !Rights.ALLOW_EXECUTE_STANDALONE)) {
			/*
			 * si on est dans une applet ou main mais qu'on n'a pas le droit
			 * d'executer Hulis en applet ou en main, on rejette l'utilisateur
			 */
			this.isValidUser = false;
			this.rejectReason = RejectReason.MODE_EXECUTION;
			return;
		}

		/*
		 * si on est sur l'url autorisee ou si l'appli est autorisee sur toute
		 * url , on ne rejette pas l'utilisateur Dans le cas contraire oui
		 */
		SwingIO.log(this.getClass().getName(), "Rights",
				"check if allowed url");

		boolean authorizedUrl;
	
		
		if (app.isExecutedOnInternet()) {
			try {
				authorizedUrl = url.sameFile(new URL(Rights.URL_ALLOWED)); // logiquement url non null ici

				if (!(authorizedUrl || Rights.ALLOW_ON_ANY_URL)) {
					this.isValidUser = false;
					this.rejectReason = RejectReason.NOT_ALLOWED_URL;
					return;
				}
			} catch (MalformedURLException e) {
				SwingIO.warning(this.getClass().getName(), "Rights",
						"malformed url", e);
			}
		} else if ( (url == null|| url.toString().startsWith("file:"))
				&& !Rights.ALLOW_EXECUTE_APPLET_VIEWER) {
			this.isValidUser = false;
			this.rejectReason = RejectReason.MODE_EXECUTION;
			return;
		}

		// si on est connecte sur le web les autres tests ne servent a rien et a ce stade on est autorise
	
		if ( app.isExecutedOnInternet()) {
			this.isValidUser = true;
			this.rejectReason = null;
			return;
		}

		// les tests qui suivent concernent l'execution depuis void main ou appletviewer
		
		SwingIO
				.log(this.getClass().getName(), "Rights", "check if network ok");

		if (!Rights.ALLOW_WITHOUT_NETWORK)
			if (!readNetWorkOk()) {
				this.isValidUser = false;
				this.rejectReason = RejectReason.NETWORK_ERROR;
				return;
			}

		SwingIO.log(this.getClass().getName(), "Rights",
				"check minimal version");
		
		if (!Rights.BYPASS_CHECK_MINIMAL_VERSION_IN_DATABASE)
			if (PreferencesGUI.getInstance().isCheckVersion() && ConnectSQL.isOutOfDateVersion(app.getVersion())) {
				this.isValidUser = false;
				this.rejectReason = RejectReason.OUTOFDATEVERSION;
				return;
			}

		SwingIO.log(this.getClass().getName(), "Rights", "check limit date ");

		Calendar calend = Calendar.getInstance();
		Date datesys = calend.getTime();

		if ( !Rights.BYPASS_LIMIT_DATE && (Rights.DATE_LIMIT != null ) && (datesys.after(Rights.DATE_LIMIT))) {
			this.isValidUser = false;
			this.rejectReason = RejectReason.OUTOFDATEVERSION;
			return;
		}

		SwingIO.log(this.getClass().getName(), "Rights", "user is valid");
		this.isValidUser = true;
		this.rejectReason = null;

	};


	
	
	public RejectReason getRejectReason() {
		return  rejectReason;
	}

	
	public boolean isValidUser() {
		return this.isValidUser;
	}

	private boolean readNetWorkOk() {
		try {
			// On initialise l'URL
			URL u = new URL(Rights.URL_TESTED_IF_NOT_ALLOWED_WITHOUT_NETWORK);
			HttpURLConnection urlConn = (HttpURLConnection)u.openConnection();
			urlConn.connect();

			return true;
		} catch (IOException e) {
			SwingIO.warning(this.getClass().getName(), "readNetWorkOk",
					"network error", e);
			return false;
		}
	}

}
