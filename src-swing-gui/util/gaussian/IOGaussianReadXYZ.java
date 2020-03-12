package util.gaussian;

import java.io.LineNumberReader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.ctom.hulis.files.gaussian.GaussianReadXYZ;

import util.xyz.StreamTokenizerAdapter;

public class IOGaussianReadXYZ extends GaussianReadXYZ{


	public IOGaussianReadXYZ(LineNumberReader lineNumberReader)  {
		super(new StreamTokenizerAdapter(new StreamTokenizer(lineNumberReader)));
	}

	public IOGaussianReadXYZ(String string) {
		super(new StreamTokenizerAdapter(new StreamTokenizer(new StringReader(string))));
	}

}
