package views.prefs;

import java.util.Locale;

import util.IPreferences;
import util.MyLocale;
import util.PreferencesMap;

/**
 * 
 * @author macbook
 *
 */
public final class PreferencesGUI {
	
	public static final boolean CLEAR_PREFERENCES_WHEN_START = false;
	public static final boolean MANAGE_LICENCE = true;
	
	public static  final int RESULTS_PRECISION_MO_DEFAULT = 2;  
	public static final int RESULTS_PRECISION_ENERGIES_DEFAULT = 2;  
	public static final boolean DISPLAY_ATOMS_CIRCLES_DEFAULT = true;
	public static final boolean DISPLAY_ATOMS_SYMBOLS_DEFAULT = true;
	private static final boolean CHECK_VERSION_DEFAULT = true;
	private static final boolean SEND_INFORMATION_DEFAULT = true;
	private static final int TOTAL_USE_DEFAULT = 0;
	private static double SCALE_ORB_DEFAULT = 4.0;
	private static boolean SHOWSYMMETRY=false;

	private static boolean printingSet=false;
	private static String radicalPrint="";
	private static String extPrint="";
	private static String dirPrint="";

	private static PreferencesGUI instance;

	private static boolean superMario=true;
	private IPreferences prefs;
	private boolean devMode = true;
	

	/**
	 * 
	 * @param dirPrint
	 * @param radicalPrint
	 * @param extPrint
	 */
	public static void setPrinting(String dirPrint, String radicalPrint, String extPrint) {
		PreferencesGUI.dirPrint=dirPrint;
		PreferencesGUI.radicalPrint=radicalPrint;
		PreferencesGUI.extPrint=extPrint;
		printingSet=true;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getDirPrint() {
		return dirPrint;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getExtPrint() {
		return extPrint;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isPrintingSet() {
		return printingSet;
	}
	
	/**
	 * 
	 * @return
	 */
	public static String getRadicalPrint() {
		return radicalPrint;
	}
	
	/**
	 * 
	 * @return
	 */
	public static double getScaleOrbDefault() {
		return SCALE_ORB_DEFAULT;
	}
	
	
	/**
	 * @return new instance
	 */
	public static PreferencesGUI getInstance() {
		if (instance == null)
			instance =new PreferencesGUI();
		return instance;
	}

	
	private PreferencesGUI(){
		prefs = new PreferencesMap();
	}

	/**
	 * @param : set a preferences manager that instantiates the interface (cookie manager, file preferences, ...) 
	 */
	public void setPreferencesManager(IPreferences preferences) {
		prefs = preferences; 
	}
	

	public static void setSuperMario(boolean b) {
		superMario=b;
	}

	public static boolean getSuperMario() {
		return superMario;
	}

	public static void switchSuperMario() {
		setSuperMario(!getSuperMario());
	}
	
	/**
	 * 
	 * @return
	 */
	public  double getSCALE_ORB() {
		return prefs.getDouble("SCALE_ORB", SCALE_ORB_DEFAULT);
	}
	
	/**
	 * 
	 * @param 
	 */
	public  void setSCALE_ORB(double sCALE_ORB) {
		prefs.putDouble("SCALE_ORB", sCALE_ORB);
	}

	/**
	 * 
	 * @return the energies precision in results window 
	 */
	public  int getResultsPrecisionEnergies() {
		return prefs.getInt("resultsPrecisionEnergies", RESULTS_PRECISION_ENERGIES_DEFAULT);
	}
	/**
	 * 
	 * @return the MO precision in results window
	 */
	public  int getResultsPrecisionMO() {
		return prefs.getInt("resultsPrecisionMO", RESULTS_PRECISION_MO_DEFAULT);
	}
	
	/**
	 * 
	 * @return true if atoms are drawn as circles
	 */
	public  boolean isDisplayAtomsCircles() {
		return prefs.getBoolean("isDisplayAtomsCircles", DISPLAY_ATOMS_CIRCLES_DEFAULT);
	}
	
	/**
	 * 
	 * @return true if  atom symbols are displayed
	 */
	public  boolean isDisplayAtomsSymbols() {
		return prefs.getBoolean("isDisplayAtomsSymbols", DISPLAY_ATOMS_SYMBOLS_DEFAULT);
	}
	
	/**
	 * 
	 * @param precisionMO
	 */
	public  void setResultsPrecisionMO(int precisionMO) {
		prefs.putInt("resultsPrecisionMO", precisionMO);
		
	}

	/**
	 * 
	 * @param precisionEnergies
	 */
	public  void setResultsPrecisionEnergies(int precisionEnergies) {
		prefs.putInt("resultsPrecisionEnergies", precisionEnergies);
		
	}

	/**
	 * 
	 * @param selected
	 */
	public  void setDisplayAtomsCircles(boolean selected) {
		prefs.putBoolean("isDisplayAtomsCircles", selected);
	
	}

	/**
	 * 
	 * @param selected
	 */
	public  void setDisplayAtomsSymbols(boolean selected) {
		prefs.putBoolean("isDisplayAtomsSymbols", selected);
			
	}
	
	
	/**
	 * 
	 * @param locale
	 */
	public void setLanguage(Locale locale) {
		prefs.putString("language", locale.getLanguage().toString());
	}

	/**
	 * 
	 * @param accept
	 */
	public  void setAcceptLicense(boolean accept) {
		prefs.putBoolean("acceptLicense", accept);
			
	}
	
	
	/**
	 * 
	 * @param isCheckVersion
	 */
	public void setCheckVersion(boolean isCheckVersion) {
		prefs.putBoolean("isCheckVersion", isCheckVersion);

	}
	
	/**
	 * 
	 * @param isSendInformation
	 */
	public void setSendInformation(boolean isSendInformation) {
		prefs.putBoolean("isSendInformation", isSendInformation);

	}
	
	/**
	 * 
	 * @param totalUse
	 */
	public  void  setTotalUse(int totalUse) {
		prefs.putInt("totalUse", totalUse);
	}
	 
	/**
	 * 
	 * @return true if the user has accepted the license
	 */
	public  boolean isLicenseAlreadyAccepted() {
		return prefs.getBoolean("acceptLicense", false);
			
	}


	/**
	 * 
	 */
	public void clearPreferences() {
		prefs.clear();
	}

	/**
	 * 
	 * @return true if check new available version automatically
	 */
	public boolean isCheckVersion() {
		return prefs.getBoolean("isCheckVersion", CHECK_VERSION_DEFAULT);

	}

	/**
	 * 
	 * @return true if information is sent to the statistics server
	 */
	public boolean isSendInformation() {
		return prefs.getBoolean("isSendInformation", SEND_INFORMATION_DEFAULT);
	}

	/**
	 * 
	 * @return the total use of the software
	 */
	public  int  getTotalUse() {
		return prefs.getInt("totalUse", TOTAL_USE_DEFAULT);
	}
	
	
	/**
	 * 
	 * return locale
	 */
	public Locale getLanguage() {
		return new Locale(prefs.getString("language",  MyLocale.getDefault().getLanguage()));
	}
	/**
	 * @return
	 */
	public static boolean isSHOWSYMMETRY() {
		return SHOWSYMMETRY;
	}

	/**
	 * @param sHOWSYMMETRY
	 */
	public static void setSHOWSYMMETRY(boolean sHOWSYMMETRY) {
		SHOWSYMMETRY = sHOWSYMMETRY;
	}

	

	/**
	 * 
	 * @return
	 */
	public boolean isDevMode() {
		return this.devMode;
	}
	
	/**
	 * 
	 * @param b
	 */
	public void setDevMode(boolean b) {
		this.devMode=b;
		
	}

}
