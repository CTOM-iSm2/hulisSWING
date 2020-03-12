package util.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import org.ctom.hulis.files.gaussian.test.ReadGaussianTest;

import util.gaussian.LineNumberReaderAdapter;
import util.xyz.StreamTokenizerAdapter;


public class IOReadGaussianTest extends ReadGaussianTest {

	public IOReadGaussianTest(File file) throws FileNotFoundException {
		super(new LineNumberReaderAdapter(new FileReader(file)), new StreamTokenizerAdapter());
	}

	public IOReadGaussianTest(Reader reader)  { 
		super(new LineNumberReaderAdapter(reader), new StreamTokenizerAdapter());
	}
}
