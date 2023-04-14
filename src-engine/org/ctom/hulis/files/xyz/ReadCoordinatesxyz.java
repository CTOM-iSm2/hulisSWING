package org.ctom.hulis.files.xyz;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.io.HuckelIO;

public class ReadCoordinatesxyz extends GeometryReader {
	
	
	Geometry geomXYZ;
	StreamTokenizer donnees;
	protected int lineNumber;
	protected int token;
	int nbAtomLus=0;
	
	/**
	 * @param donnees
	 */
	public ReadCoordinatesxyz(StreamTokenizer donnees) {
		this.donnees = donnees;
		this.lineNumber = 0;
		this.token = 0;
		this.nbAtomLus=0;
	}
	
	/**
	 * @param string
	 */
	public ReadCoordinatesxyz(String string) {
		this( new StreamTokenizer(new StringReader(string)));
	}

	/**
	 * 
	 * @return the line number reached after reading the datas
	 */
	public int getLineNumber() {
		return this.lineNumber;
	}


	/**
	 * 
	 * @param geometry
	 * @throws ReadxyzException 
	 */
	public void readAll (Geometry geometry) throws ReadxyzException  {
		this.geomXYZ = geometry;
		donnees.eolIsSignificant(true);
		readHeader(donnees);
		readxyzDatas(donnees);
		this.geomXYZ.generateConnectivity();
		this.geomXYZ.flatGeom();		
	}

	/**
	 * @param donnees
	 * @throws ReadxyzException 
	 */
	protected void readHeader(StreamTokenizer donnees) throws ReadxyzException 	{	// lignes vides tolerees
		try {
			while ((token = donnees.nextToken()) == StreamTokenizer.TT_EOL) {
				this.lineNumber++;

			}
			this.lineNumber++;
		} catch (IOException e) {
			throw new ReadxyzException(e.getMessage());
		}

	}

	/**
	 * @param donnees
	 * @throws ReadxyzException
	 */
	protected void readxyzDatas(StreamTokenizer donnees) throws ReadxyzException  {
		int flag = 0;
		nbAtomLus=0;
		boolean emptyLine = true;

		// on lit les lignes suivantes

		Atom atom = null;
		try {
			while (true) {
	
				if (token == StreamTokenizer.TT_EOF){
				
					if (atom != null) {
						if (flag < 3) 
							throw new ReadxyzException("a coordinate is missing at line " + this.lineNumber);
						this.geomXYZ.add(atom);
					}

					throw new ReadxyzException("EOF reached");
				}
				else if  (token == StreamTokenizer.TT_EOL) {
					// TEST FINAL LINE			
					if (emptyLine) 
						throw new ReadxyzException("Empty line found at line " + this.lineNumber);

					emptyLine = true;

					if (atom == null) 
						throw new ReadxyzException("atom not found at line " + this.lineNumber);
	
					if (flag < 3) 
						throw new ReadxyzException("a coordinate is missing at line " + this.lineNumber);

		
					// ADD AN ITEM TO THE GEOMETRY
					this.geomXYZ.add(atom);
					nbAtomLus++;
					atom = null;
					flag = 0;
					this.lineNumber++;
				}
				else if (token == StreamTokenizer.TT_WORD) {
					emptyLine = false;

					if (atom != null) 
						throw new ReadxyzException("string number format expected to define atom coordinates at line " + this.lineNumber);
							
					String symbol = donnees.sval;
					try {
						atom = new Atom(0, null, PeriodicTable.Element.valueOf(symbol),
								symbol, symbol, 0, 0);
					}
					catch(IllegalArgumentException e) {
						throw new ReadxyzException(symbol + " is not a recognized atomic element, at the line " + this.lineNumber);
					}
				}else if  (token == StreamTokenizer.TT_NUMBER) {
					emptyLine = false;
					if (atom == null)
						throw new ReadxyzException("Atom symbol expected at the beginning of the line " + this.lineNumber);
					flag++;
					switch (flag) {
					case (1):
						setX(atom, donnees.nval);
						break;
					case (2):
						setY(atom, donnees.nval);
						break;
					case (3):
						setZ(atom, donnees.nval);
						break;
					default:
						atom.addConnect((int) donnees.nval);
					}
				}
						
				token = donnees.nextToken();

			} // while
			

		} catch (Exception e) {
			if ( (e.getMessage() != null) &&  (e.getMessage().equals("EOF reached")
					|| e.getMessage().startsWith("Empty line found"))) {
				this.geomXYZ.print();
			} else {
				HuckelIO.error(this.getClass().getName(), "readxyzDatas", e
						.getMessage(), e);
				throw new ReadxyzException(e.getMessage());
			}
		}
		
		
	
	}

	

	/**
	 * 
	 * @return
	 */
	protected Geometry getGeometry () {
		return this.geomXYZ;
	}

	

}