/**
 * 
 */
package org.ctom.hulis.files.gaussian;

import org.ctom.hulis.files.LineNumberReadeable;
import org.ctom.hulis.files.ReadSectionException;
import org.ctom.hulis.files.SectionReader;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.GeometryException;
import org.ctom.hulis.huckel.exception.HuckelAtomException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureNotExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.geometry.VirtualBond;

/**
 * @author macbook
 *
 */
public class NRTSTRreader {
	
	
	private SectionReader sectionReader;
	private Mesomery mesomery;

	
	/**
	 * 
	 * @param lnr
	 */
	public NRTSTRreader(LineNumberReadeable lnr) {
		this.sectionReader = new SectionReader(lnr) ;
		this.mesomery = null;
	}
	
	/**
	 * 
	 * @param mesomery
	 * @param lnr
	 */
	public NRTSTRreader(Mesomery mesomery, LineNumberReadeable lnr) {
		this.sectionReader = new SectionReader(lnr) ;
		this.mesomery = mesomery;
	}
	
	/**
	 * 
	 * @param mesomery
	 */
	public void setMesomery(Mesomery mesomery){
		this.mesomery = mesomery;
	}
	
	
	
	/**
	 * 
	 * @param mesomery
	 * @param sectionReader
	 */
	public NRTSTRreader(Mesomery mesomery, SectionReader sectionReader) {
		this.sectionReader = sectionReader;
		this.mesomery = mesomery;
	}
	
	/**
	 * 
	 * @throws HuckelBondException
	 * @throws ReadGaussianException
	 * @throws MesomeryDelocalizedStructureNotExistsException
	 * @throws CoupleException
	 * @throws IMethodException
	 * @throws MesomeryNoStructureLocalizedException
	 * @throws HuckelAtomException
	 */
	public void readAll() throws HuckelBondException, ReadGaussianException, MesomeryDelocalizedStructureNotExistsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException, HuckelAtomException {
		if (this.mesomery == null)
			throw new ReadGaussianException("cannnot start to read NRTSTR : Mesomery is null");
		this.readNRTSTR_Namelist();
	}

	private void readBOND(Structure structure) throws ReadGaussianException, HuckelBondException, HuckelAtomException {
		int rememberLine = this.sectionReader.getLineNumber();
		String[] bonds;
		try {
			bonds = this.sectionReader.readSectionUntilEnd(IGaussianConstants.KEYWORD_END);
		} catch (ReadSectionException e1) {
			throw new ReadGaussianException(e1);
		}
	
	
		int i = 0;
		int iat =0, jat =0;
		String tmp = "";
		String type;
		Geometry geom = null;
		try {
			geom = structure.getGeometry();
		} catch (GeometryException e) {
			throw new ReadGaussianException(e);
		}
	
		if (! bonds[0].toLowerCase().equals(IGaussianConstants.KEYWORD_BOND))
			throw new ReadGaussianException(
					"Error in Bond section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber() + " : the section must begin with the keyword \"" + IGaussianConstants.KEYWORD_BOND + "\"");
	
		// i++ evaluates as i
		// ++i evaluates as i+1
		while (!((type = bonds[++i])).toLowerCase().equals(IGaussianConstants.KEYWORD_END)) {
			try {
				tmp = bonds[++i];
				iat = Integer.parseInt(tmp);		
				tmp = bonds[++i];
				jat = Integer.parseInt(tmp);
			} 
			catch (NumberFormatException e) {
				throw new ReadGaussianException(
						"Error in reading the Bond section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber() + " : atom number expected instead of \""+ tmp + "\" word for structure " + structure.getName());		
			}
	
			int bondType = 0;
			Atom atom1 = geom.getAtomBySuperSymbol(iat);
			Atom atom2 = geom.getAtomBySuperSymbol(jat);
			if (atom1 == null) {
				throw new ReadGaussianException(
						"Error in reading the Bond section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber() + " : atom " + iat + " not found in the structure " + structure.getName() + " that contains " + structure.countAtoms() + " atom(s)");
			}
			if (atom2 == null) {
				throw new ReadGaussianException(
						"Error in reading the Bond section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber() + " : atom " + jat + " not found in the structure " + structure.getName() + " that contains " + structure.countAtoms() + " atom(s)");
			}
			if ((type.toLowerCase()).equals("s")) {
				bondType = 1;
			} else if ((type.toLowerCase()).equals("d")) {
				bondType = 2;
			} else
				throw new ReadGaussianException(
						"Error in reading the Bond section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber() + " : no such bondType : \""
								+ bondType + "\" between atom " + iat + " and " + jat + " in the structure " + structure.getName() + "\"s\" or \"d\" keywords expected");
			Bond l = geom.getBond(atom1, atom2);
			if (l == null) {
				if ( (atom1 instanceof HuckelAtom) && (atom2 instanceof HuckelAtom)) {
					((HuckelAtom) atom1).setRadR(1);
					((HuckelAtom) atom2).setRadR(1);		
				}
			}
			else if (l instanceof HuckelBond)
				((HuckelBond) l).setBondType(bondType);
			else if (l instanceof VirtualBond) {
				VirtualBond vb = (VirtualBond) l; 
				vb.setBondType(bondType);
				Bond b  = structure.getBond(vb.getSubistutuedME(), vb.getSubistutuedME().getNeighbours().get(0));
				((HuckelBond) b).setBondType(bondType);
	
			}
			else {
				if (bondType != 1)
					throw new ReadGaussianException(
							"Error in reading the Bond section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber() + " : S expected for the bond " + l + " betwwen atoms "
									+ iat + " and " + jat + " in the structure " + structure.getName());
			}
	
		}
	}

	private void readLONE(Structure structure) throws ReadGaussianException, HuckelAtomException {
		int rememberLine = this.sectionReader.getLineNumber();
		String[] pairs;
		try {
			pairs = this.sectionReader.readSectionUntilEnd(IGaussianConstants.KEYWORD_END);
		} catch (ReadSectionException e1) {
			throw new ReadGaussianException(e1);
		}
		int i = 0;
		int iat =0, npair =0;
		String tmp = "";
	
		Geometry geom;
		try {
			geom = structure.getGeometry();
		} catch (GeometryException e) {
			throw new ReadGaussianException(e);
		}
	
		if (! pairs[0].toLowerCase().equals(IGaussianConstants.KEYWORD_LONE))
			throw new ReadGaussianException(
					"Error in Lone section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber() + " : the section must begin with the keyword \"" + IGaussianConstants.KEYWORD_LONE + "\"");
	
		// i++ evaluates as i
		// ++i evaluates as i+1
		while (!(pairs[i + 1].toLowerCase().equals(IGaussianConstants.KEYWORD_END))) {
			try {
				tmp = pairs[++i];
				iat = Integer.parseInt(tmp);		
			} 
			catch (NumberFormatException e) {
				throw new ReadGaussianException(
						"Error in reading the Lone section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber() + " : atom number expected instead of \""+ tmp + "\" word for structure " + structure.getName());		
			}
	
			try {
				tmp = pairs[++i];
				npair = Integer.parseInt(tmp);
			}
			catch (NumberFormatException e) {
				throw new ReadGaussianException(
						"Error in reading the Lone section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber()+ " : number of pair electrons expected instead of \""+ tmp + "\" word for structure " + structure.getName());		
			}
	
			Atom atom = geom.getAtomBySuperSymbol(iat);
			if (atom == null) {
				throw new ReadGaussianException(
						"Error in reading the Lone section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber() + " : atom " + iat + " not found in structure " + structure.getName() + " that contains " + structure.countAtoms() + " atom(s)");
			}
			if (atom instanceof HuckelAtom) {
				setRadR((HuckelAtom) atom, npair);
				// si l'atome est un C substituant un methyl, il faut mettre a jour le nombre de RadR sur le ME
				if ((atom.getElement() == PeriodicTable.Element.C) && (structure.getAtomBySuperSymbol(atom.getSuperSymbol()).getElement() == PeriodicTable.Element.Me)) {
					setRadR((HuckelAtom)structure.getAtomBySuperSymbol(atom.getSuperSymbol()),npair);
				}
	
	
			} else
				throw new ReadGaussianException(
						"Error in reading the Lone section between lines " + rememberLine + " and " + this.sectionReader.getLineNumber()+ " : cannot put " + npair + " pairs on the atom "
								+ iat + " in structure " + structure.getName() + " because it is of element " + atom.getElement() );
	
		}
	
		// return lonesPairNRT.toArray(new LonePairNRT[lonesPairNRT.size()]);
	}

	private void readNRTSTR_Namelist() throws ReadGaussianException, HuckelBondException, MesomeryDelocalizedStructureNotExistsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException, HuckelAtomException {
		if (this.mesomery.getDelocalizedStructure() == null)
			throw new MesomeryDelocalizedStructureNotExistsException("Cannot create mesomery : delocalized structure does not exist.");
		
		String keyword="";
		try {
			keyword = this.sectionReader.getNextKeyword();
		} catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		} // $NRTSTR attendu
		if (!(keyword.toLowerCase().equals(IGaussianConstants.NAMELIST_NRTSTR_INIT))) {
			throw new ReadGaussianException("Keyword \""
					+ IGaussianConstants.NAMELIST_NRTSTR_INIT + "\" expected line " + this.sectionReader.getLineNumber()+ " in namelist NRTSTR instead of \"" + keyword +"\"");
		}
		try {
			keyword = this.sectionReader.getNextKeyword();
		} catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		} // Serie de blocs STR / LONE .. END / BOND ...
		// END / END
		if (!(keyword.toLowerCase().equals(IGaussianConstants.KEYWORD_STR) || keyword.toLowerCase().equals(IGaussianConstants.NAMELIST_END)) ) {
			throw new ReadGaussianException("Keywords \""+ IGaussianConstants.KEYWORD_STR + "\" or \"" + IGaussianConstants.NAMELIST_END + "\" expected at line " + this.sectionReader.getLineNumber() + " instead of \"" + keyword + "\"");
		}
	
		while (keyword.toLowerCase().equals(IGaussianConstants.KEYWORD_STR)) {
			readSTR();
			try {
				keyword = this.sectionReader.getNextKeyword();
			} catch (ReadSectionException e) {
				throw new ReadGaussianException(e);
			} // END attendu
			if (!(keyword.toLowerCase().equals(IGaussianConstants.KEYWORD_END))) {
				throw new ReadGaussianException("Keyword \""+ IGaussianConstants.KEYWORD_END + "\" expected at line " + this.sectionReader.getLineNumber() + " in namelist NRTSTR instead of \"" + keyword +"\"");
			}
			try {
				keyword = this.sectionReader.getNextKeyword();
			} catch (ReadSectionException e) {
				throw new ReadGaussianException(e);
			} // STR ou $END
	
		}
	
	}

	private void readSTR() throws MesomeryDelocalizedStructureNotExistsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException, ReadGaussianException, HuckelAtomException, HuckelBondException {
		Structure structure;
		//if (this.mesomery.getDelocalizedStructure().countAtoms() == 0)
		//return;
		structure = this.mesomery.createLocalizedStructure();
		readLONE(structure);
		readBOND(structure);
		// createRads (structure, lonesPairNRT);
	
	}

	/**
	 * 
	 * @param a
	 * @param npair
	 * @throws HuckelAtomException
	 */
	protected void setRadR(HuckelAtom a, int npair) throws HuckelAtomException {
		a.setRadR((npair - a.getNbElecSigma() / 2) * 2);
	}
	
	

}
