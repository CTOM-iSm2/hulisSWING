package util.gaussian;

import java.io.Reader;
import java.io.StringReader;

import org.ctom.hulis.files.gaussian.NRTSTRreader;

public class IOReadNRTSTR extends NRTSTRreader {

	/**
	 * @param reader
	 */
	public IOReadNRTSTR(Reader reader)  { 
		super(new LineNumberReaderAdapter(reader));
	}
	
	
	/**
	 * @param string
	 */
	public IOReadNRTSTR(String string)  {
		super(new LineNumberReaderAdapter(new StringReader(string)));

	}
}
