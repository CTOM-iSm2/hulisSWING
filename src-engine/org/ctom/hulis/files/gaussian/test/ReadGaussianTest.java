package org.ctom.hulis.files.gaussian.test;

import java.io.File;
import java.io.FileNotFoundException;

import org.ctom.hulis.files.ReadSectionException;
import org.ctom.hulis.files.gaussian.IGaussianConstants;
import org.ctom.hulis.files.gaussian.ReadGaussian;
import org.ctom.hulis.files.gaussian.ReadGaussianException;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.HuckelAtomException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.HulisException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureExistsException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureNotExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.exception.MoleculeBondExistsException;
import org.ctom.hulis.huckel.exception.MoleculeCoherenceException;
import org.ctom.hulis.huckel.exception.MoleculeTooManyNeighboursException;
import org.ctom.hulis.huckel.structures.StructureDelocalized;


public class ReadGaussianTest extends ReadGaussian {
	/**
	 * @throws FileNotFoundException 
	 * 
	 */
	public ReadGaussianTest(File file) throws FileNotFoundException {
		super(file);
	}

	final static public String KEYWORD_HX = "hx";
	final static public String KEYWORD_HXY = "hxy";
	

	protected void readAll() throws ReadGaussianException, HuckelBondException, BondException, MesomeryDelocalizedStructureExistsException, MoleculeTooManyNeighboursException, MoleculeBondExistsException, MoleculeCoherenceException, org.ctom.hulis.files.xyz.ReadxyzException, NumberFormatException, HulisException, MesomeryDelocalizedStructureNotExistsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException, HuckelAtomException {
		readHeader();
		readChargeMultiplicity();
		readXYZ();
		readHX();
		readHXY();
		readNRT();
	}
	
	@Override
	protected void readNRT() throws ReadGaussianException, HuckelBondException, MesomeryDelocalizedStructureNotExistsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException, HuckelAtomException {
		// read the nboNamelist
	
		if (!(this.SWITCH_NRT = readNBO_Namelist()))
			return;
		
		new TestNRTSTRreader(mesomery, sectionReader).readAll();	
	
	}
	
	public void readHX()  throws ReadGaussianException {
		String[] str = null;
		try {
			str = this.sectionReader.readSectionUntilEnd(IGaussianConstants.KEYWORD_END);
		} catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		}
		int i = 0;
		if (! ((str[0].toLowerCase()).equals(KEYWORD_HX) ) )
			throw new ReadGaussianException(KEYWORD_HX + "  expected");
		StructureDelocalized s = this.getMesomery().getDelocalizedStructure();
		while (!(str[i + 1].toLowerCase().equals(IGaussianConstants.KEYWORD_END))) {
			int iat = Integer.parseInt(str[++i]);
			double hx = Double.parseDouble(str[++i]);
			Atom atom = s.getAtomByIndex(iat);
			if (atom == null) {
				throw new ReadGaussianException(
						"Error in reading the HX section : atom not found "
								+ iat);
			}
			if (atom instanceof HuckelAtom) {
				((HuckelAtom) atom).setHx(hx);
	
			} else
				throw new ReadGaussianException(
						"Error in reading the HX section : HuckelAtom expected "
								+ iat);

		}

		
	}
	
	/**
	 * 
	 */
	public void readHXY()  throws ReadGaussianException, NumberFormatException, HulisException, HuckelBondException {
		String[] str = null;
		try {
			str = this.sectionReader.readSectionUntilEnd(IGaussianConstants.KEYWORD_END);
		} catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		}
		int i = 0;
		String sHxy;
	
		if (! ((str[0].toLowerCase()).equals(KEYWORD_HXY) ) )
			throw new ReadGaussianException(KEYWORD_HXY + "  expected");
		StructureDelocalized s = this.getMesomery().getDelocalizedStructure();
		while (!((sHxy = str[++i])).toLowerCase().equals(IGaussianConstants.KEYWORD_END)) {
			int iat = Integer.parseInt(str[++i]);
			int jat = Integer.parseInt(str[++i]);
			Atom atom1 = s.getAtomByIndex(iat);
			Atom atom2 = s.getAtomByIndex(jat);
			if (atom1 == null) {
				throw new ReadGaussianException(
						"Error in reading the HXY section : atom not found "
								+ iat);
			}
			if (atom2 == null) {
				throw new ReadGaussianException(
						"Error in reading the HXY section : atom not found "
								+ jat);
			}
			Bond l = s.getBond(atom1, atom2);
			if (l == null)
				throw new ReadGaussianException(
						"Error in reading the HXY section : no such bond beetween "
								+ iat + " and " + jat);
			if (l instanceof HuckelBond)
				((HuckelBond) l).setHxy(Double.parseDouble(sHxy));
			else {
				throw new ReadGaussianException(
					"Error in reading the HXY section : Huckel bond expected betwwen "
								+ iat + " and " + jat);
			}
		}

		
	}
	
}
