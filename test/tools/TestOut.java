package tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;

public class TestOut {

	public final static String OUT_NAME = "test.out";
	static PrintWriter writer = null;
	static boolean enableCreateOut = true;
	static boolean enableCloseOut = true;
	
	
	public static void setEnableCreateOut(boolean defaultEnableCreateOut) {
		enableCreateOut = defaultEnableCreateOut;
	}
	
	public static void setEnableCloseOut(boolean defaultEnableCloseOut) {
		enableCloseOut = defaultEnableCloseOut;
	}
	
	public static void createOut () {
		if (!enableCreateOut) return ;

		try {
				writer= new PrintWriter(new FileOutputStream(new File (OUT_NAME)));
			} catch (FileNotFoundException e) {

			e.printStackTrace();
		}

	}
	
	public static void println (String s) {	
		writer.println(s);
		writer.flush();
		System.out.println(s);

	}

	public static void println (int s) {	
		writer.println(Integer.toString(s));

	}
	public static void closeOut() {	
		if (!enableCloseOut) return;
		writer.println("END TEST : " + new Date());
		writer.close();

	}

	public static void println() {
		writer.println();
		
	}
		
}
