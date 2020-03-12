package util.gaussian;

import java.io.LineNumberReader;
import java.io.Reader;

import org.ctom.hulis.files.LineNumberReadeable;

public class LineNumberReaderAdapter extends LineNumberReader implements  LineNumberReadeable {

	public LineNumberReaderAdapter(Reader in) {
		super(in);
		// TODO Auto-generated constructor stub
	}
	
	public LineNumberReaderAdapter(Reader in, int sz) {
		super(in, sz);
		// TODO Auto-generated constructor stub
	}

	

}
