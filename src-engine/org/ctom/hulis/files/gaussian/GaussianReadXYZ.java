package org.ctom.hulis.files.gaussian;

import org.ctom.hulis.files.Tokenizable;
import org.ctom.hulis.files.xyz.Readxyz;
import org.ctom.hulis.files.xyz.ReadxyzException;


public class GaussianReadXYZ extends Readxyz implements IGaussianConstants {

	
	
	/**
	 * @param donnees
	 * @throws ReadxyzException
	 */
	public GaussianReadXYZ(Tokenizable donnees)  {
		super(donnees);
		this.strictNumberAtoms(false);
		// TODO Auto-generated constructor stub
	}


	@Override
	protected void readHeader(Tokenizable donnees) {
		// pas de header
	}
}
