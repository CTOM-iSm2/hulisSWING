/**
 * 
 */
package org.ctom.hulis.files;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedList;

import org.ctom.hulis.util.io.HuckelIO;

/**
 * @author macbook
 *
 */
public class SectionReader {

	private LineNumberReader lnr = null;
	private int lineNumber=0;;

	/**
	 * 
	 * @param lnr
	 */
	public SectionReader(LineNumberReader lnr){
		this.lnr = lnr;
		this.lineNumber = 0;
		
	}
	
	/**
	 * 
	 * @param s
	 */
	public SectionReader(String s){
		this(new LineNumberReader(new StringReader(s)));
		
	}

	/**
	 * 
	 * @return
	 * @throws ReadSectionException
	 */
	public String[] getLine() throws ReadSectionException {
		String[] words = new String [0];
		String ligne = getWholeLine();
		if (ligne == null)
			return null; // end of file
		
		words = (ligne.trim()).split("[\\r\\n\\s]+"); // si 1 ou plusieurs espaces ou retour chariot
		// separent les mots
	
		for (int i = 0; i < words.length; i++) {
			String word = words[i];
			// return only the words before the comments
			if (word.length() > 0)
				if (word.toCharArray()[0] == '!') {
					String[] nocomment = new String[i];
					for (int j = 0; j < i; j++) {
						nocomment[j] = words[j];
					}
					return clean(nocomment);
				}
		}
		// no comment was found => return the complete line
		return clean(words);
	}

	/**
	 * @param : toLowerCase : transform the line to lower case after reading it
	 * @return
	 * @throws ReadSectionException
	 */
	public String getWholeLine(boolean toLowerCase) throws ReadSectionException {
		try {
			String line = this.lnr.readLine();
			this.lineNumber++;
			HuckelIO.PrintIfln(this.lineNumber + " : " + line);
			if (toLowerCase && (line != null))
				line = line.toLowerCase();
			return line;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new ReadSectionException("end of file not expecteded at line " + this.lineNumber);
		}
	
	}
	

	/**
	 * 
	 * @return
	 * @throws ReadSectionException
	 */
	public String getWholeLine() throws ReadSectionException {
		return this.getWholeLine(false);
	
	}

	/**
	 * 
	 * @param end
	 * @return
	 * @throws ReadSectionException
	 */
	public String[] readSectionUntilEnd(String end) throws ReadSectionException {
		LinkedList<String> section = new LinkedList<String>();
	
		String[] line;
		while (true) {
			line = getLine();
			if (line == null)
				throw new ReadSectionException("Keyword \"" + end + "\" not found. end of file not expected at line " + this.lineNumber);
	
			for (int i = 0; i < line.length; i++) {
				section.add(line[i]);
				if ((line[i]).toLowerCase().equals(end))
					return section.toArray(new String[section.size()]);
			}
		}
	
	}

	/**
	 * delete string of length == 0 in the string array
	 * @param stringArrayToclean
	 * @return
	 */
	private  String[] clean(String[] stringArrayToclean) {
		ArrayList<String> result = new ArrayList<String>();
		for (String string  : stringArrayToclean ) {
			if(string.length() != 0)
				result.add(string);
		}
		return result.toArray(new String[result.size()]);
	}

	/**
	 * @return
	 */
	public int getLineNumber() {
		return this.lineNumber;
	}
	
	
	/**
	 * skip empty lines and get the next keyword
	 * @return
	 * @throws ReadSectionException
	 */
	public String getNextKeyword() throws ReadSectionException{
		String[] keywords= null;
		while(((keywords = getLine()) != null) && keywords.length ==0);
		if (keywords == null)
			throw new ReadSectionException("End of file not expected at line " + this.lineNumber);

		return  keywords[0];
	
	}
	
	
}
