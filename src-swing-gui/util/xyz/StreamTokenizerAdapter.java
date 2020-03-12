package util.xyz;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.ctom.hulis.files.Tokenizable;

public class StreamTokenizerAdapter implements Tokenizable {

	StreamTokenizer streamTokenizer;

	public StreamTokenizerAdapter (){
		this.streamTokenizer=null;
	}
	
	public StreamTokenizerAdapter (StreamTokenizer streamTokenizer){
		this.streamTokenizer=streamTokenizer;
	}
	
	public int isNumber(){
		return StreamTokenizer.TT_NUMBER;
	}
	public double getNVal() {
		return streamTokenizer.nval;
		
	}
	public int isWord() {
		return StreamTokenizer.TT_WORD;
		
	}
	public String getSVal(){
		return streamTokenizer.sval;
	}
	public int isEOL(){
		return StreamTokenizer.TT_EOL;
	}
	public int isEOF() {
		return StreamTokenizer.TT_EOF;
	}
	public int nextToken() throws IOException {
		return streamTokenizer.nextToken();
	}
	
	public void eolIsSignificant(boolean significant) {
		streamTokenizer.eolIsSignificant(significant);
	}

	public void tokenize(String string) {
		this.streamTokenizer = new StreamTokenizer(new StringReader(string));
		
	}
}