package util.xyz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.LineNumberReader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.ctom.hulis.files.xyz.ReadCoordinatesxyz;

public class IOReadCoordinatesxyz extends ReadCoordinatesxyz{

	public IOReadCoordinatesxyz(StreamTokenizer streamTokenizer)  {
		super (new StreamTokenizerAdapter (streamTokenizer));
	}
	public IOReadCoordinatesxyz(File file) throws FileNotFoundException    {
		super (new StreamTokenizerAdapter (new StreamTokenizer(new FileReader(file))));
	}

	public IOReadCoordinatesxyz(InputStream inputStream)  {
		super (new StreamTokenizerAdapter (new StreamTokenizer(new StringReader(inputStream.toString()))));
	}

	public IOReadCoordinatesxyz(LineNumberReader lineNumberReader)  {
		super (new StreamTokenizerAdapter(new StreamTokenizer(lineNumberReader)));
	}

	public IOReadCoordinatesxyz(String string) {
		super (new StreamTokenizerAdapter(new StreamTokenizer(new StringReader(string))));
	}

}
