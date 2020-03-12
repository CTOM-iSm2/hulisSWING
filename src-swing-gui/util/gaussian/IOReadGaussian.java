package util.gaussian;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import org.ctom.hulis.files.gaussian.ReadGaussian;

import util.xyz.StreamTokenizerAdapter;

public class IOReadGaussian extends ReadGaussian {
	public IOReadGaussian(File file) throws FileNotFoundException {
		super(new LineNumberReaderAdapter(new FileReader(file)), new StreamTokenizerAdapter());
	}

	public IOReadGaussian(Reader reader)  { 
		super(new LineNumberReaderAdapter(reader), new StreamTokenizerAdapter());
	}

	public IOReadGaussian(String string)  {
		super(new LineNumberReaderAdapter(new StringReader(string)) , new StreamTokenizerAdapter());

	}
}
