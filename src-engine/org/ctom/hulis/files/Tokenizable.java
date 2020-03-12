/**
 * 
 */
package org.ctom.hulis.files;

import java.io.IOException;

/**
 * you must implement this interface to send a tokenizable in parameter of the Readxyz constructor 
 * @author macbook
 *
 */
public interface Tokenizable {
	public int isNumber();
	public double getNVal();
	public int isWord();
	public String getSVal();
	public int isEOL();
	public int isEOF();
	public int nextToken() throws IOException;
	public void eolIsSignificant(boolean significant);
	/**
	 * @param string
	 */
	public void tokenize(String string);
	
}
