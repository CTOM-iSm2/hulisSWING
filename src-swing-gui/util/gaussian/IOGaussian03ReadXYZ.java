package util.gaussian;

import java.io.LineNumberReader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.ctom.hulis.files.gaussian.Gaussian03ReadXYZ;

import util.xyz.StreamTokenizerAdapter;

public class IOGaussian03ReadXYZ extends Gaussian03ReadXYZ{


	public IOGaussian03ReadXYZ(LineNumberReader lineNumberReader) {
		super(new StreamTokenizerAdapter(new StreamTokenizer(lineNumberReader)));
	}

	public IOGaussian03ReadXYZ(String string)  {
		super(new StreamTokenizerAdapter(new StreamTokenizer(new StringReader(string))));
	}

}
