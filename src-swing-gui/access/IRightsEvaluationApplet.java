package access;

import java.util.Calendar;
import java.util.Date;

public interface IRightsEvaluationApplet {
	public boolean STOP_DATABASE_CONNECTION =false;
	
	public boolean CONSOLE_VERBOSE = false;
	public boolean ALLOW_EXCITATIONS = false;
	public boolean ALLOW_POLYNOMES = false;
	public boolean ALLOW_BLOCKS = false;
	public boolean ALLOW_SAVE_TEST_FILE_FORMAT = false;
	public boolean  ALLOW_XML_FILE_FORMAT =false;
	public boolean ALLOW_NRT = false;

	public static final boolean MOVE_MOUSE_QUICKLY_DURING_TESTS = false;

	public final static boolean CHECK_USER_ACCESS_RIGHTS = true;

	public final static boolean ALLOW_EXECUTE_ON_INTERNET = true;
	public static boolean ALLOW_EXECUTE_APPLET_VIEWER = true;

	public static boolean ALLOW_ON_ANY_URL = false;

	public final static boolean ALLOW_EXECUTE_STANDALONE = true;
	public final static boolean ALLOW_WITHOUT_NETWORK = true;
	
	public final static String URL_ALLOWED = "http://www.hulis.free.fr/";

	public static boolean BYPASS_LIMIT_DATE = true;

	public static boolean BYPASS_CHECK_MINIMAL_VERSION_IN_DATABASE = true;

	public static final Date DATE_LIMIT = new Date(3015 - 1900,
			Calendar.DECEMBER, 31);

	public final static String URL_TESTED_IF_NOT_ALLOWED_WITHOUT_NETWORK = "http://www.hulis.free.fr";


}
