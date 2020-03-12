package util.xyz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import org.ctom.hulis.files.Writer;

public class PrintWriterAdapter extends PrintWriter implements Writer{
	
	public PrintWriterAdapter(File file) throws FileNotFoundException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterAdapter(StringWriter stringWriter) {
		super(stringWriter);
	}

	public void printfUS(String format, String symbol, double x, double y,
			double z) {
		super.printf(Locale.US, format, symbol, x, y, z);
		
	}


	public void printf(String format, int i) {
		super.printf(format, i);
		
	}

	
	

}
