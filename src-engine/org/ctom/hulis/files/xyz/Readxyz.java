package org.ctom.hulis.files.xyz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.io.HuckelIO;

public class Readxyz extends ReadCoordinatesxyz {
	
	private int nAtoms;
	private boolean isStrict;
	
	/**
	 * 
	 * @param donnees
	 */
	public Readxyz(String donnees) {
		super(new StreamTokenizer(new StringReader(donnees)));
		this.nAtoms = 0;
		this.isStrict = true;
	}

	/**
	 * @param file
	 * @throws FileNotFoundException 
	 */
	public Readxyz(File file) throws FileNotFoundException {
		super(new StreamTokenizer(new FileReader(file)));
		this.nAtoms = 0;
		this.isStrict = true;
	}
	
	/**
	 * @param donnees :  a tokenizer with a stream already set
	 */
	public Readxyz(StreamTokenizer donnees) {
		super(donnees);
		this.nAtoms = 0;
		this.isStrict = true;
	}

	/**
	 * 
	 * @return the line number reached after reading the datas
	 */
	public int getLineNumber() {
		return this.lineNumber;
	}

	/**
	 * The number of atoms in the header must be  the same as the number of read atoms
	 * @param : true or false
	 */
	protected void strictNumberAtoms(boolean isStrict) {
		this.isStrict = isStrict;
	}
	
	
	/**
	 * @param donnees
	 */
	protected void readHeader(StreamTokenizer donnees) throws ReadxyzException  {
		try {
			// la premiere ligne contient le nombre d'atomes
			if ((token = donnees.nextToken() )== StreamTokenizer.TT_NUMBER) {
				this.nAtoms = (int) donnees.nval;
				HuckelIO.PrintIf("nAtoms read = " + nAtoms + "\n");
			} else {
				throw new XYZErreurLigne1();
			}
			this.lineNumber ++;
			if ((token = donnees.nextToken() )!= StreamTokenizer.TT_EOL) {
				HuckelIO.PrintIf(donnees+"\n");
				throw new XYZErreurLigne2();
			}
			this.lineNumber ++;
			// la troisieme ligne est ignoree
			while ((token = donnees.nextToken() )!= StreamTokenizer.TT_EOL) {
			}
			this.lineNumber++;
			token = donnees.nextToken();			
		} catch (XYZErreurLigne1 e) {
			throw new ReadxyzException(e.getMessage());
		} catch (XYZErreurLigne2 e) {
			throw new ReadxyzException(e.getMessage());
		} catch (IOException e) {
			throw new ReadxyzException(e.getMessage());
		}


	}

	/**
	 * @param donnees
	 * @throws ReadxyzException
	 */
	protected void readxyzDatas(StreamTokenizer donnees) throws ReadxyzException  {
		super.readxyzDatas(donnees);
		if (this.isStrict && (nbAtomLus != this.nAtoms))
			throw new ReadxyzException( this.nAtoms+" expected atoms, specified at the beginning of the file, but " + nbAtomLus + " have been read ");
	


	}

	

	/**
	 * 
	 * @return
	 */
	protected Geometry getGeometry () {
		return this.geomXYZ;
	}

}