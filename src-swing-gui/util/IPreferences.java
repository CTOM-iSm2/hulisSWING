package util;

/**
 * @author macbook
 *
 */
public interface IPreferences {
	
	/**
	 * @param key
	 * @param def
	 * @return
	 */
	public double getDouble(String key, double def) ;
	
	/**
	 * @param key
	 * @param def
	 * @return
	 */
	public int getInt(String key, int def) ;
	
	/**
	 * @param key
	 * @param def
	 * @return
	 */
	public boolean getBoolean(String key, boolean def) ;
	
	/**
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	public String getString(String key, String def) ;
	
	/**
	 * @param key
	 * @param value
	 */
	public void putDouble(String key, double value) ;
	
	/**
	 * @param key
	 * @param value
	 */
	public void putInt(String key, int value) ;
	
	/**
	 * @param key
	 * @param value
	 */
	public void putBoolean(String key, boolean value) ;
	
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void putString(String key, String value);
	
	/**
	 * clear the preferences
	 */
	public void clear();
	
}
