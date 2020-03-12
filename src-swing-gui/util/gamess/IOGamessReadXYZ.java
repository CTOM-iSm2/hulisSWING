package util.gamess;

import java.io.LineNumberReader;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.ctom.hulis.files.gamess.GamessReadXYZ;

import util.xyz.StreamTokenizerAdapter;

public class IOGamessReadXYZ extends GamessReadXYZ{


	public IOGamessReadXYZ(LineNumberReader lineNumberReader)  {
		super(new StreamTokenizerAdapter(new StreamTokenizer(lineNumberReader)));
	}

	public IOGamessReadXYZ(String string) {
		super(new StreamTokenizerAdapter(new StreamTokenizer(new StringReader(string))));
	}

}
