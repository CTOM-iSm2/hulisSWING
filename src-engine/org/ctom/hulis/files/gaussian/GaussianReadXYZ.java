package org.ctom.hulis.files.gaussian;

import java.io.StreamTokenizer;

import org.ctom.hulis.files.xyz.Readxyz;
import org.ctom.hulis.files.xyz.ReadxyzException;


public class GaussianReadXYZ extends Readxyz implements IGaussianConstants {

	
	
	/**
	 * @param donnees
	 */
	public GaussianReadXYZ(StreamTokenizer donnees)  {
		super(donnees);
		this.strictNumberAtoms(false);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void readHeader(StreamTokenizer donnees) {
		// pas de header
	}
}
