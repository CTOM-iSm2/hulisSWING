package util.xyz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.ctom.hulis.files.xyz.Readxyz;

public class IOReadxyz extends Readxyz{

	public IOReadxyz(StreamTokenizer streamTokenizer)  {
		super (new StreamTokenizerAdapter (streamTokenizer));
	}
	public IOReadxyz(File file) throws FileNotFoundException    {
		super (new StreamTokenizerAdapter (new StreamTokenizer(new FileReader(file))));
	}

	public IOReadxyz(InputStream inputStream)  {
		super (new StreamTokenizerAdapter (new StreamTokenizer(new StringReader(inputStream.toString()))));
	}

	public IOReadxyz(LineNumberReader lineNumberReader)  {
		super (new StreamTokenizerAdapter(new StreamTokenizer(lineNumberReader)));
	}

	public IOReadxyz(String string) {
		super (new StreamTokenizerAdapter(new StreamTokenizer(new StringReader(string))));
	}

}
