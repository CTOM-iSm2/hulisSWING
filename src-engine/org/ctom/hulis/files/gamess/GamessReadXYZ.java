package org.ctom.hulis.files.gamess;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;

import org.ctom.hulis.files.xyz.GeometryReader;
import org.ctom.hulis.files.xyz.ReadxyzException;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.PeriodicTable.Element;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.io.HuckelIO;

public class GamessReadXYZ extends GeometryReader {
	public static final double BOHR_TO_ANGSTROM = 0.529177211;

	Geometry geomXYZ;
	StreamTokenizer donnees;
	private int lineNumber;
	int token;

	/**
	 * @param string
	 */
	public GamessReadXYZ(String string) {
		this.donnees = new StreamTokenizer(new StringReader(string));
		this.lineNumber = 0;
		this.token= 0;
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
	public void readAll (Geometry geometry) throws ReadxyzException {
		this.geomXYZ = geometry;
		donnees.eolIsSignificant(true);
		readHeader(donnees);
		readxyzDatas(donnees);
		this.geomXYZ.flatGeom();		
	}

	/**
	 * @param donnees
	 * 
	 * @throws IOException
	 */
	protected void readHeader(StreamTokenizer donnees) 	{	// lignes vides tolerees
		try {
			while ((token = donnees.nextToken()) == StreamTokenizer.TT_EOL) {
				this.lineNumber++;

			}
			this.lineNumber++;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @param donnees
	 * @throws ReadxyzException
	 */
	protected void readxyzDatas(StreamTokenizer donnees) throws ReadxyzException  {
		int flag = 0;
		boolean emptyLine = true;
		int nbAtomLus =0;
		// on lit les lignes suivantes

		Atom atom = null;
		try {
			while (true) {
				
				if (token == StreamTokenizer.TT_EOF) {
								
					if (atom != null) {
						if (flag < 4) 
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
			
					if (flag < 4) 
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
					if ( (flag==2) )
						throw new ReadxyzException("atomic number must be a number at the line " + this.lineNumber + " instead of " + donnees.sval);

					else if ( (flag >=3) &&  (flag <=5) )
						throw new ReadxyzException("coordinate must be a number at the line " + this.lineNumber + " instead of " + donnees.sval);

				}
				else if  (token == StreamTokenizer.TT_NUMBER) {
					emptyLine = false;
					flag++;
					
					switch (flag) {
					case (1):
						int atomicNumber =(int)donnees.nval;
					Element element = PeriodicTable.getElementByAtomicNumber(atomicNumber);
					if (element == null)
						throw new ReadxyzException("Atom not recongized for the atomic number = " + atomicNumber  +" at the line " + this.lineNumber);

					atom = new Atom(0, null, element,
							element.toString(), element.toString(), 0, 0);

					break;
					case (2):
						setX(atom, donnees.nval * BOHR_TO_ANGSTROM);
					break;
					case (3):
						setY(atom, donnees.nval * BOHR_TO_ANGSTROM);
					break;
					case (4):
						setZ(atom, donnees.nval * BOHR_TO_ANGSTROM);
					break;
					default:
						atom.addConnect((int) donnees.nval);
					}
				}

			
				token = donnees.nextToken();

			}// while


		} catch (Exception e) {
			e.printStackTrace();
				
			if ( (e.getMessage() != null) &&  (e.getMessage().equals("EOF reached")
					|| e.getMessage().startsWith("Empty line found"))) {
					this.geomXYZ.print();
			} else {
				HuckelIO.error(this.getClass().getName(), "readxyzDatas", e
						.getMessage(), e);
				throw new ReadxyzException(e.getMessage());
			}
		}



		this.geomXYZ.generateConnectivity();

	}



	/**
	 * 
	 * @return
	 */
	protected Geometry getGeometry () {
		return this.geomXYZ;
	}
	
	

}