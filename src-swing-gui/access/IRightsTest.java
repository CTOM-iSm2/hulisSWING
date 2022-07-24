package access;

import java.util.Date;

public interface IRightsTest {
	
	public boolean STOP_DATABASE_CONNECTION =false;

	public final boolean CONSOLE_VERBOSE = false;
	public boolean ALLOW_EXCITATIONS = true;
	public boolean ALLOW_POLYNOMES = true;
	public boolean ALLOW_BLOCKS = true;
	public boolean ALLOW_SAVE_TEST_FILE_FORMAT = true;
	public boolean  ALLOW_XML_FILE_FORMAT =true;
	public boolean ALLOW_NRT = true;
	
	public static final boolean MOVE_MOUSE_QUICKLY_DURING_TESTS = false;
	
	public final static boolean CHECK_USER_ACCESS_RIGHTS = false;

	public final static boolean ALLOW_EXECUTE_ON_INTERNET = true;
	public static boolean ALLOW_EXECUTE_APPLET_VIEWER = false;

	public static boolean ALLOW_ON_ANY_URL = false;

	public final static boolean ALLOW_EXECUTE_STANDALONE = false;

	public final static boolean ALLOW_WITHOUT_NETWORK = false;

	public final static String URL_ALLOWED = "http://www.hulis.free.fr/";

	public static boolean BYPASS_LIMIT_DATE = true;

	public static boolean BYPASS_CHECK_MINIMAL_VERSION_IN_DATABASE = true;

	public static final Date DATE_LIMIT = null;

	public final static String URL_TESTED_IF_NOT_ALLOWED_WITHOUT_NETWORK = "http://www.hulis.free.fr";
}
