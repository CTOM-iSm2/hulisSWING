package util;

import java.applet.Applet;
import java.util.Calendar;
import java.util.HashMap;

import netscape.javascript.JSObject;
import util.io.SwingIO;

/**
 * Note : il existe un cookie manager dans java mais il ne marche que pour les applets signees
 * Necessite le module plugin.jar pour fonctionner (appel JSObject)
 * 
 * @author macbook
 *
 */
public class CookieManager {

	private Applet applet;
	private boolean useCookieCache = true;
	private HashMap<String, String> cookieCache = new HashMap<String, String>();
	
	/**
	 * @param applet
	 */
	public CookieManager(Applet applet) {
		this.applet = applet;
	}

	/**
	 * The cache is usefull to not read each time in the webpage
	 * @param useCache
	 */
	public void setUseCache (boolean useCache) {
		this.useCookieCache = useCache;
	}
	
	/**
	 * 
	 * @return true if the cache is used
	 */
	public boolean isUseCache() {
		return this.useCookieCache;
	}
	
	/**
	 * Add a cookie that expires one month later
	 * @param name
	 * @param value
	 */
	public void writeCookie (String name, String value) {

		java.util.Calendar c = java.util.Calendar.getInstance();
		c.add(java.util.Calendar.MONTH, 1);
		writeCookie (name, value, c);
	}

	/**
	 * 
	 * @param name
	 * @param value
	 * @param expire
	 */
	public void writeCookie (String name, String value, Calendar expire) {
		/*  
		 **  write a cookie
		 **    computes the expiration date, good for 1 month
		 */

		if (useCookieCache)
			cookieCache.put(name, value);
			
		String expires = "; expires=" + expire.getTime().toGMTString(); 
		
		String s1 = name + "=" + value + expires; 

		JSObject myBrowser = JSObject.getWindow(applet);
		JSObject myDocument =  (JSObject) myBrowser.getMember("document");
		myDocument.setMember("cookie", s1);
	}

	/**
	 * 
	 * @param name
	 */
	public void deleteCookie(String name) {
		if (useCookieCache)
			cookieCache.remove(name);

		// delete a cookie, set the expiration in the past
		java.util.Calendar c = java.util.Calendar.getInstance();
		c.add(java.util.Calendar.MONTH, -1);
		this.writeCookie(name, "null", c);
	}

	/**
	 * get all the cookies
	 * @return
	 */
	public String getCookies() {
		//get all cookies for a document
		try {
			JSObject myBrowser = (JSObject) JSObject.getWindow(applet);	
			JSObject myDocument =  (JSObject) myBrowser.getMember("document");
			String myCookie = (String)myDocument.getMember("cookie");
			if (myCookie.length() > 0) 
				return myCookie;
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param name
	 * @return cookie value, or null if the cookie is not found
	 */
	public String getCookie(String name) {
		if (useCookieCache && this.cookieCache.containsKey(name))
			return cookieCache.get(name);
			
		// get a specific cookie by its name, parse the cookie.
		//    not used in this Applet but can be useful
		String myCookie = getCookies();
		String search = name + "=";
		if (myCookie.length() > 0) {
			int offset = myCookie.indexOf(search);
			if (offset != -1) {
				offset += search.length();
				int end = myCookie.indexOf(";", offset);
				if (end == -1) end = myCookie.length();
				String value =  myCookie.substring(offset,end);
				if (useCookieCache)
					this.cookieCache.put(name, value);
				return value;
			}
			else 
				SwingIO.log(this.getClass().getName(), "getCookie(name)" ,"getCookie(name) Did not find cookie: ");
		}
		if (useCookieCache)
			this.cookieCache.put(name, null);
		return null;
	}

	/**
	 * clear cookies and cache
	 */
	public void clear() {
		if (useCookieCache)
			cookieCache.clear();

		String pairs = getCookies();
		String[] kvPairs = pairs.split(";");
		for(String kvPair: kvPairs) {
			String[] kv = kvPair.split("=");
			String key = kv[0].trim();
			this.deleteCookie(key);
		}

	}

	/**
	 * print cookies list of key/value pairs
	 */
	public void printCookies() {
		// key=value;key=value;
		String pairs = getCookies();
		if (pairs.length()==0)
			return;
		String[] kvPairs = pairs.split(";");
		for(String kvPair: kvPairs) {
			String[] kv = kvPair.split("=");
			String key = kv[0].trim();
			String value = kv[1].trim();
			if (useCookieCache)
				this.cookieCache.put(key, value);
			
			System.out.println(key + " = " + value);
		}
	}

}
