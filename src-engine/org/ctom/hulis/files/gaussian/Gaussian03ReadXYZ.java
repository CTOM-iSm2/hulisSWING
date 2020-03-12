package org.ctom.hulis.files.gaussian;

import java.io.IOException;

import org.ctom.hulis.files.Tokenizable;
import org.ctom.hulis.files.xyz.GeometryReader;
import org.ctom.hulis.files.xyz.ReadxyzException;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.PeriodicTable.Element;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.io.HuckelIO;

public class Gaussian03ReadXYZ extends GeometryReader {
	
	
	Geometry geomXYZ;
	Tokenizable donnees;
	private int lineNumber;
	int token;
	
	/**
	 * @param donnees
	 */
	public Gaussian03ReadXYZ(Tokenizable donnees) {
		this.donnees = donnees;
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
	protected void readHeader(Tokenizable donnees) 	{	// lignes vides tolerees
		try {
			while ((token = donnees.nextToken()) == donnees.isEOL()) {
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
	protected void readxyzDatas(Tokenizable donnees) throws ReadxyzException  {
		int flag = 0;
		Atom atom = null;
		boolean emptyLine = true;

		try {
			while (true) {		
	
				if (token == donnees.isEOF()) {
				
					if (atom != null) {
						if (flag < 6) 
							throw new ReadxyzException("a coordinate is missing at line " + this.lineNumber);
						this.geomXYZ.add(atom);
					}


					throw new ReadxyzException("EOF reached");
				}
				else if  (token == donnees.isEOL()) {
					// TEST FINAL LINE
					if (emptyLine) 
						throw new ReadxyzException("Empty line found at line " + this.lineNumber);

					emptyLine = true;

					if (atom == null) 
						throw new ReadxyzException("atom not found at line " + this.lineNumber);

					if (flag < 6) 
						throw new ReadxyzException("a coordinate is missing at line " + this.lineNumber);

					
					// ADD AN ITEM TO THE GEOMETRY
					this.geomXYZ.add(atom);
					atom = null;
					flag = 0;
					this.lineNumber++;
				}
				else if (token == donnees.isWord()) {
					emptyLine = false;

					throw new ReadxyzException("value is not a number : string \"" + donnees.getSVal() + "\" not expected in Gaussian03 format at line " + this.lineNumber );
							
				
				}else if  (token == donnees.isNumber()) {
					emptyLine = false;

					flag++;
					switch (flag) {
					case (1):
						break;
					case (2):
						int atomicNumber =(int)donnees.getNVal();
						Element element = PeriodicTable.getElementByAtomicNumber(atomicNumber);
						if (element == null)
							throw new ReadxyzException("Atom not recongized for the atomic number = " + atomicNumber  +" at the line " + this.lineNumber);
						
						atom = new Atom(0, null, element,
									element.toString(), element.toString(), 0, 0);
						
						break;
					case (3):
						break;
					case (4):
						setX(atom, donnees.getNVal());
						break;
					case (5):
						setY(atom, donnees.getNVal());
						break;
					case (6):
						setZ(atom, donnees.getNVal());
						break;
					default:
						atom.addConnect((int) donnees.getNVal());
					}
				}
					
				token = donnees.nextToken();

			}// while
			

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