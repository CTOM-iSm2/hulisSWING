package util;

import java.applet.Applet;

/**
 * Manage preferences with cookies
 * 
 * @author macbook
 *
 */
public class PreferencesCookies implements IPreferences {
	CookieManager cookieManager;
	
	/* (non-Javadoc)
	 * @see util.IPreferences#getDouble(java.lang.String, double)
	 */
	
	public PreferencesCookies(CookieManager cookieManager) {
		this.cookieManager =  cookieManager;
	}
	
	
	/* (non-Javadoc)
	 * @see util.IPreferences#getDouble(java.lang.String, double)
	 */
	public double getDouble(String key, double def) {
		String value =  cookieManager.getCookie(key);
		if (value == null)
			return def;
		else
			return Double.valueOf(value); 
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#getInt(java.lang.String, int)
	 */
	public int getInt(String key, int def) {
		String value =  cookieManager.getCookie(key);
		if (value == null)
			return def;
		else
			return Integer.valueOf(value); 
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#getBoolean(java.lang.String, boolean)
	 */
	public boolean getBoolean(String key, boolean def) {
		String value =  cookieManager.getCookie(key);
		if (value == null)
			return def;
		else
			return Boolean.valueOf(value); 
	}

	
	/* (non-Javadoc)
	 * @see util.IPreferences#getString(java.lang.String, java.lang.String)
	 */
	public String getString(String key, String def) {
		String value =  cookieManager.getCookie(key);
		if (value == null)
			return def;
		else
			return value; 
	}

	
	/* (non-Javadoc)
	 * @see util.IPreferences#putDouble(java.lang.String, double)
	 */
	public void putDouble(String key, double value) {
		cookieManager.writeCookie(key,  String.valueOf(value) );
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#putInt(java.lang.String, int)
	 */
	public void putInt(String key, int value) {
		cookieManager.writeCookie(key,  String.valueOf(value) );
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#putBoolean(java.lang.String, boolean)
	 */
	public void putBoolean(String key, boolean value) {
		cookieManager.writeCookie(key,  String.valueOf(value) );
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#putString(java.lang.String, java.lang.String)
	 */
	public void putString(String key, String value) {
		cookieManager.writeCookie(key,  value );
		
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#clear()
	 */
	public void clear() {
		cookieManager.clear();	
	}


	



}
