package util;

import java.util.HashMap;

/**
 * Manage preference with a map
 * 
 * @author macbook
 *
 */
public class PreferencesMap implements IPreferences {
	HashMap <String, Object> prefs;
	/* (non-Javadoc)
	 * @see util.IPreferences#getDouble(java.lang.String, double)
	 */
	
	/**
	 * 
	 */
	public PreferencesMap() {
		prefs =  new HashMap <String, Object>();
	}
	
	
	/* (non-Javadoc)
	 * @see util.IPreferences#getDouble(java.lang.String, double)
	 */
	public double getDouble(String key, double def) {
		Object value =  prefs.get(key);
		if (value == null)
			return def;
		else
			return (Double) value; 
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#getInt(java.lang.String, int)
	 */
	public int getInt(String key, int def) {
		Object value =  prefs.get(key);
		if (value == null)
			return def;
		else
			return (Integer) value; 
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#getBoolean(java.lang.String, boolean)
	 */
	public boolean getBoolean(String key, boolean def) {
		Object value =  prefs.get(key);
		if (value == null)
			return def;
		else
			return (Boolean) value; 
	}

	
	/* (non-Javadoc)
	 * @see util.IPreferences#getString(java.lang.String, java.lang.String)
	 */
	public String getString(String key, String def) {
		Object value =  prefs.get(key);
		if (value == null)
			return def;
		else
			return (String) value; 
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#putDouble(java.lang.String, double)
	 */
	public void putDouble(String key, double value) {
		prefs.put(key, value);
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#putInt(java.lang.String, int)
	 */
	public void putInt(String key, int value) {
		prefs.put(key, value);
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#putBoolean(java.lang.String, boolean)
	 */
	public void putBoolean(String key, boolean value) {
		prefs.put(key, value);
	}


	/* (non-Javadoc)
	 * @see util.IPreferences#putString(java.lang.String, java.lang.String)
	 */
	public void putString(String key, String value) {
		prefs.put(key, value);
	
	}
	/* (non-Javadoc)
	 * @see util.IPreferences#clear()
	 */
	public void clear() {
		prefs.clear();
	}





}
