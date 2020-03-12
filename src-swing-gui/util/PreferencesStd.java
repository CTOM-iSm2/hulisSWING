package util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Manage preferences with the standard api
 * 	
 *  Preferences store : 
 *   Mac OS X : home directory : ~/Library/Preferences/com.apple.java.util.prefs.plist 
 *	 Windows : in the registry key:  /HKEY_CURRENT_USER/Software/JavaSoft/Prefs/   
 *	 Linux : home directory ;  ~/.java/.userPrefs/  
 *
 * 
 * @author macbook
 *
 */
public class PreferencesStd implements IPreferences {

	Preferences preferences;

	/**
	 * the standard preference user node
	 * @param userRootNode
	 */
	public PreferencesStd(String userRootNode) {
		preferences =  Preferences.userRoot().node(userRootNode);
	}
	
	/* (non-Javadoc)
	 * @see util.IPreferences#getDouble(java.lang.String, double)
	 */
	public double getDouble(String key, double def) {
		return preferences.getDouble(key, def);
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#getInt(java.lang.String, int)
	 */
	public int getInt(String key, int def) {
		return preferences.getInt(key, def);
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#getBoolean(java.lang.String, boolean)
	 */
	public boolean getBoolean(String key, boolean def) {
		return preferences.getBoolean(key, def);

	}
	
	/* (non-Javadoc)
	 * @see util.IPreferences#getString(java.lang.String, java.lang.String)
	 */
	public String getString(String key, String def) {
		return preferences.get(key, def);
	}
	
	/* (non-Javadoc)
	 * @see util.IPreferences#putDouble(java.lang.String, double)
	 */
	public void putDouble(String key, double value) {
		preferences.putDouble(key, value);
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#putInt(java.lang.String, int)
	 */
	public void putInt(String key, int value) {
		preferences.putInt(key, value);
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#putBoolean(java.lang.String, boolean)
	 */
	public void putBoolean(String key, boolean value) {
		preferences.putBoolean(key, value);
	}

	/* (non-Javadoc)
	 * @see util.IPreferences#putString(java.lang.String, java.lang.String)
	 */
	public void putString(String key, String value) {
		preferences.put(key, value);

	}
	
	/* (non-Javadoc)
	 * @see util.IPreferences#clear()
	 */
	public void clear() {
		try {
			preferences.clear();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
		
	}

	
}
