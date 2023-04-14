package org.ctom.hulis.prefs;

public class Preferences {

	public static enum COUPLAGE_TYPE {ORBITALS, ATOMS};
	
	public enum DISPLAY_ORBITAL_TYPE {
		CANONICAL, NATURAL;

	}
	
	public static final COUPLAGE_TYPE COUPLAGE_TYPE_DEFAULT = COUPLAGE_TYPE.ORBITALS;

	//nombre de separation de charges lors de la generation automatique
	public static final int NUM_CHARGE_SEPARATION_DEFAULT = 1;
	private static int NUM_CHARGE_SEPARATION = NUM_CHARGE_SEPARATION_DEFAULT;
	//separation de charge longue distance autorise lors de la generation automatique
	public static final boolean SEP_CHARGE_LONG_DIST_AUTOGEN_DEFAULT = true;
	private static boolean SEP_CHARGE_LONG_DIST_AUTOGEN = SEP_CHARGE_LONG_DIST_AUTOGEN_DEFAULT;
	//couplage longue distance autorise lors de la generation automatique
	public static final boolean COUPLE_LONG_DIST_AUTOGEN_DEFAULT = true;
	private static boolean COUPLE_LONG_DIST_AUTOGEN = COUPLE_LONG_DIST_AUTOGEN_DEFAULT;
	//couplage autorise lors de la generation automatique
	public static final boolean COUPLE_AUTOGEN_DEFAULT = false;
	private static boolean COUPLE_AUTOGEN = COUPLE_AUTOGEN_DEFAULT;
	private static final DISPLAY_ORBITAL_TYPE DISPLAY_ORBITAL_TYPE_DEFAULT=DISPLAY_ORBITAL_TYPE.CANONICAL;
	public static DISPLAY_ORBITAL_TYPE DISPLAY_ORBITAL=DISPLAY_ORBITAL_TYPE_DEFAULT;
	private final static boolean DISPLAY_DENSITY_DEFAULT=false;
	public static boolean DISPLAY_DENSITY=DISPLAY_DENSITY_DEFAULT;
	
	public void reset() {
		setNUM_CHARGE_SEPARATION(NUM_CHARGE_SEPARATION_DEFAULT);
		setSEP_CHARGE_LONG_DIST_AUTOGEN(SEP_CHARGE_LONG_DIST_AUTOGEN_DEFAULT);
		setCOUPLE_LONG_DIST_AUTOGEN(COUPLE_LONG_DIST_AUTOGEN_DEFAULT);
		setCOUPLE_AUTOGEN(COUPLE_AUTOGEN_DEFAULT);
	}

	public static int getNUM_CHARGE_SEPARATION() {
		return NUM_CHARGE_SEPARATION;
	}

	public static void setNUM_CHARGE_SEPARATION(int nUM_CHARGE_SEPARATION) {
		NUM_CHARGE_SEPARATION = nUM_CHARGE_SEPARATION;
	}

	public static boolean isCOUPLE_AUTOGEN() {
		return COUPLE_AUTOGEN;
	}

	public static void setCOUPLE_AUTOGEN(boolean cOUPLE_AUTOGEN) {
		COUPLE_AUTOGEN = cOUPLE_AUTOGEN;
	}

	public static boolean isCOUPLE_LONG_DIST_AUTOGEN() {
		return COUPLE_LONG_DIST_AUTOGEN;
	}

	public static void setCOUPLE_LONG_DIST_AUTOGEN(boolean cOUPLE_LONG_DIST_AUTOGEN) {
		COUPLE_LONG_DIST_AUTOGEN = cOUPLE_LONG_DIST_AUTOGEN;
	}

	public static boolean isSEP_CHARGE_LONG_DIST_AUTOGEN() {
		return SEP_CHARGE_LONG_DIST_AUTOGEN;
	}

	public static void setSEP_CHARGE_LONG_DIST_AUTOGEN(
			boolean sEP_CHARGE_LONG_DIST_AUTOGEN) {
		SEP_CHARGE_LONG_DIST_AUTOGEN = sEP_CHARGE_LONG_DIST_AUTOGEN;
	}
	public static DISPLAY_ORBITAL_TYPE getDISPLAY_ORBITAL() {
		return DISPLAY_ORBITAL;
	}

	private static void setDISPLAY_ORBITAL(DISPLAY_ORBITAL_TYPE dISPLAY_ORBITAL) {
		DISPLAY_ORBITAL = dISPLAY_ORBITAL;
	}
	
	public static void switchDISPLAY_ORBITAL() {
		switch (DISPLAY_ORBITAL) {
		case CANONICAL:
			setDISPLAY_ORBITAL(DISPLAY_ORBITAL_TYPE.NATURAL);
			break;
		case NATURAL:
			setDISPLAY_ORBITAL(DISPLAY_ORBITAL_TYPE.CANONICAL);
			break;

		default:
			break;
		}
	}
	
	public static void switchDISPLAY_DENSITY() {
		DISPLAY_DENSITY = !DISPLAY_DENSITY;
	}

	public static boolean isDISPLAY_DENSITY() {
		return DISPLAY_DENSITY;
	}

}
