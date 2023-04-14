/**
 * 
 */
package org.ctom.hulis.files.gaussian;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.ctom.hulis.files.ReadSectionException;
import org.ctom.hulis.files.SectionReader;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.IHuckelObject;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.BlocDelocalizedException;
import org.ctom.hulis.huckel.exception.BondException;
import org.ctom.hulis.huckel.exception.CoupleException;
import org.ctom.hulis.huckel.exception.GeometryException;
import org.ctom.hulis.huckel.exception.HuckelAtomException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.exception.IMethodException;
import org.ctom.hulis.huckel.exception.MesomeryDelocalizedStructureNotExistsException;
import org.ctom.hulis.huckel.exception.MesomeryNoStructureLocalizedException;
import org.ctom.hulis.huckel.structures.StructureLocalized;
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
	 * @param z
	 */
	public NRTSTRreader(String string) {
		this(new LineNumberReader(new StringReader(string))) ;
	}
	/**
	 * 
	 * @param lnr
	 */
	public NRTSTRreader(LineNumberReader lnr) {
		this.sectionReader = new SectionReader(lnr) ;
		this.mesomery = null;
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
	 * @param mesomery
	 */
	public void setMesomery(Mesomery mesomery){
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

	/*
	 * 
	 * @param structure
	 * @throws ReadGaussianException
	 * @throws HuckelBondException
	 * @throws HuckelAtomException
	 */
	private void readBOND(StructureLocalized structure) throws ReadGaussianException, HuckelBondException, HuckelAtomException {
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

	/*
	 * lit la chaine entre LONE et END
	 * @param structure
	 * @throws ReadGaussianException
	 * @throws HuckelAtomException
	 */
	private void readLONE(StructureLocalized structure) throws ReadGaussianException, HuckelAtomException {
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

	/*
	 * lit la chaine entre (NRTSTR et (STR ou END))
	 * @throws ReadGaussianException
	 * @throws HuckelBondException
	 * @throws MesomeryDelocalizedStructureNotExistsException
	 * @throws CoupleException
	 * @throws IMethodException
	 * @throws MesomeryNoStructureLocalizedException
	 * @throws HuckelAtomException
	 */
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
			boolean isBLWSection = readSTR();
			// s'il n'y a pas de section BLW, on a deja lu le mot end, sinon on doit tomber dessus
			try {
				if (!isBLWSection)
					keyword = IGaussianConstants.KEYWORD_END;
				else
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

	/*
	 * 
	 */
	protected boolean readSTR() throws MesomeryDelocalizedStructureNotExistsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException, ReadGaussianException, HuckelAtomException, HuckelBondException {
		StructureLocalized structure;
		//if (this.mesomery.getDelocalizedStructure().countAtoms() == 0)
		//return;
		structure = this.mesomery.createLocalizedStructure();
		readLONE(structure);
		readBOND(structure);
		boolean isBLWSection = readBLW(structure);
		// createRads (structure, lonesPairNRT);
		return isBLWSection;

	}

	/*
	 * cree les blocs a partir de la la section BLW 
	 *  $BLW  
		    Nblock
		$END
		$BLWDAT 
		For each block:NE   NBF   
		List of basis functions
		$END

		Exemple 1 Fulvene (pour prolonger notre excellent article de 2016)
		$BLW  
		    2
		$END
		$BLWDAT 
		4 3 
		3 1 2
		        0 1  
		4 
		$END
	 */
	private boolean readBLW(StructureLocalized structure) throws ReadGaussianException {
		String[] line;
		String keyword="";
		try {
			keyword = this.sectionReader.getNextKeyword();
		} catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		} 
	
		// si on tombe sur le mot cle END, il n'y a pas de section BLW
		if (keyword.toLowerCase().equals(IGaussianConstants.KEYWORD_END))
			return false;
	
		// $BLW ou END attendu (END si aucun bloc BLW)
		if (!(keyword.toLowerCase().equals(IGaussianConstants.KEYWORD_BLW) || keyword.toLowerCase().equals(IGaussianConstants.KEYWORD_END))) {
			throw new ReadGaussianException("Keyword \""
					+ IGaussianConstants.KEYWORD_BLW + "\" expected line " + this.sectionReader.getLineNumber()+ " in namelist NRTSTR instead of \"" + keyword +"\"");
		}
	
		// section $BLW $END
		// assertion : on a lu le mot clé $BLW 
		// lit la ligne qui contient le nombre de blocks entre $BLW   et $END
		int nBlocksDeclares=0;
		try {
			line = this.sectionReader.getLine();
		} catch (ReadSectionException e) {
			throw new ReadGaussianException("number of blocks expected at " + this.sectionReader.getLineNumber()  + " " + e.getMessage() );
		}
		if (line.length !=1)
			throw new ReadGaussianException("number of blocks expected at " + this.sectionReader.getLineNumber() + " instead of " + line  );
		nBlocksDeclares=Integer.valueOf(line[0]);
	
	
		try {
			keyword = this.sectionReader.getNextKeyword();
		} catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		}
	
		// on attend le mot $END
		if (!keyword.toLowerCase().equals(IGaussianConstants.KEYWORD_BLW_END))
			throw new ReadGaussianException("Keyword \""
					+ IGaussianConstants.KEYWORD_BLW_END + "\" expected line " + this.sectionReader.getLineNumber()+ " in namelist NRTSTR instead of \"" + keyword +"\"");
	
		// section $BLWDAT $END
		// on attend le mot $BLWDAT
		try {
			keyword = this.sectionReader.getNextKeyword();
		} catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		}
		if (!keyword.toLowerCase().equals(IGaussianConstants.KEYWORD_BLW_DAT))
			throw new ReadGaussianException("Keyword \""
					+ IGaussianConstants.KEYWORD_BLW_DAT + "\" expected line " + this.sectionReader.getLineNumber()+ " in namelist NRTSTR instead of \"" + keyword +"\"");
	
	
		// apres le mot $BLWDAT on doit boucler jusquau mot cle $END
		// on choisit de faire le nombre d'iterations en fonction du nombre de blocs declares
		int iterations=0;
		int nbElecLu=0;
		int nbAtomLu=0;
		
		while (iterations < nBlocksDeclares) {
			// on lit la ligne nbElec espace nbAtomes_dans_le_bloc
			try {
				line = this.sectionReader.getLine();
			} catch (ReadSectionException e) {
				throw new ReadGaussianException("NbElec NbAtom expected at " + this.sectionReader.getLineNumber() + " " + e.getMessage() );
			}
			
			// si on arrive a la ligne $end, il faut qu'on ait attend le nombre de lignes attendues
			if ((line.length==1) && line[0].equals(IGaussianConstants.KEYWORD_BLW_END) && (iterations!=nBlocksDeclares))
				throw new ReadGaussianException( IGaussianConstants.KEYWORD_BLW_END + " reached but " +  iterations + "blocks read instead of  " + nBlocksDeclares + " expected at line " + this.sectionReader.getLineNumber() );
	
			// Ligne nomnbre de blocks, nombre d'electrons, on attend deux caracteres
			if (line.length !=2)
				throw new ReadGaussianException("NbElec NbAtom expected at " + this.sectionReader.getLineNumber() + " instead of " + line  );
						
			nbElecLu=Integer.valueOf(line[0]);
			nbAtomLu=Integer.valueOf(line[1]);
		
			
			// on lit la ligne atom1 atom2 atom3... et on recupere les liaisons et atomes dans un tableau	
			// sauf dans le cas ou on a 1 atom et zero electron (cas d'un bloc virtuel)
			List <IHuckelObject> huckelObjects = new ArrayList<IHuckelObject>();
			try {
				 line = this.sectionReader.getLine();
			} catch (ReadSectionException e) {
				throw new ReadGaussianException("atom1 atom2 atom3... expected at " + this.sectionReader.getLineNumber() + " " + e.getMessage() );
	
			}
			if (line.length != nbAtomLu)
				throw new ReadGaussianException(line.length + " atoms read, instead of " + nbAtomLu  + " expected at " + this.sectionReader.getLineNumber());
			// remplir le tableau avec les atomes et liaisons correspondant aux numeros d'atomes lus 
			// TODO on devrait aussi gerer le cas natome=0 et nelec >0
			if (nbElecLu >= 0 && nbAtomLu > 0) {
				for (int i = 0; i < line.length; i++) {
					Atom a1=structure.getAtomByIndex(Integer.valueOf(line[i]));
					if (! (a1 instanceof HuckelAtom))
						throw new ReadGaussianException(line.length + "cannot create block - atom index =" + i + " " + a1 + " huckel atom expected" + this.sectionReader.getLineNumber());
					HuckelAtom hA1=(HuckelAtom) a1;
					if ((hA1.countRadR()==0) && !huckelObjects.contains(hA1))
						huckelObjects.add(hA1);
					for (int j = i+1; j < line.length; j++) {
						if (i != j) {
							Atom a2=structure.getAtomByIndex(Integer.valueOf(line[j]));
							if (! (a2 instanceof HuckelAtom))
								throw new ReadGaussianException(line.length + "cannot create block - atom index =" + j + " " + a2 + " huckel atom expected" + this.sectionReader.getLineNumber());
							HuckelAtom hA2=(HuckelAtom) a2;
							if ((hA2.countRadR()==0) && !huckelObjects.contains(hA2))
								huckelObjects.add(hA2);
	
							Bond b = structure.getBond(hA1, hA2);
							// normalement ne peut pas arriver 
							if (b != null) {			        		
								if (!(b instanceof HuckelBond)) 
									throw new ReadGaussianException("atoms ("+ i + ", " + j + ") : huckel bond expected at " + this.sectionReader.getLineNumber());
								HuckelBond huckelBond = (HuckelBond) b;
						        if ((huckelBond.getBondType() < 2) && !huckelObjects.contains(huckelBond) && huckelObjects.contains(hA1) && huckelObjects.contains(hA2))
						        	huckelObjects.add(huckelBond);
							}
						}
					}
				}
			}
			
			// on lit la ligne exclude 0 1 2 et on supprime les exclusion de la liste d'objets de huckel remplie precedement
			// TODO : ce n'est pas propre, le mieux est de lire tout le fichier a l'avance et de ne pas inserer les paires a exclure dans la liste
			try {
				line = this.sectionReader.getLine();
			} catch (ReadSectionException e) {
				throw new ReadGaussianException("exepected pair of atom exclusion, block" +  iterations + " at line " + this.sectionReader.getLineNumber() + " " + e.getMessage() );
			}
			if ( (line.length==0) || ! line[0].equals("0"))
				throw new ReadGaussianException("the line must start with 0, block " + iterations + " at line " + this.sectionReader.getLineNumber() );
	
			if ((line.length-1) % 2 != 0)
				throw new ReadGaussianException("expected even number, block " + iterations + " at line " + this.sectionReader.getLineNumber() );
	
			int i=1;
			while (i< line.length) {
				int iA1=Integer.parseInt(line[i++]);
				Atom a1 = structure.getAtomByIndex(iA1);
				int iA2=Integer.parseInt(line[i++]);
				Atom a2 = structure.getAtomByIndex(iA2);
				try {
					huckelObjects.remove(new HuckelBond((HuckelAtom) a1, (HuckelAtom)a2));
				} catch (BondException e1) {
					throw new ReadGaussianException("expected huckel Bond " + iA1 + "-"+ iA2 + " block " + iterations + " at line " + this.sectionReader.getLineNumber() );
	
				}
			}
			
			// on cree le block a partir du tableau de liaisons et du nombre d'electrons
			if (huckelObjects.size()>0) {
				try {
					structure.addBlocDelocalized(new BlocDelocalized(huckelObjects, nbElecLu));
				} catch (BlocDelocalizedException e) {
					throw new ReadGaussianException("error in creating block " + iterations + " at line " + this.sectionReader.getLineNumber() + " " + e.getMessage() );
	
				}
			}
	
			iterations++;
	
		}// while
	
		// on attend le mot $end a la fin de la section BLW
		try {
			keyword = this.sectionReader.getNextKeyword();
		} catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		} // $END attendu
		if (!(keyword.toLowerCase().equals(IGaussianConstants.KEYWORD_BLW_END))) {
			throw new ReadGaussianException("Keyword \""+ IGaussianConstants.KEYWORD_BLW_END + "\" expected at line " + this.sectionReader.getLineNumber() + " in namelist NRTSTR instead of \"" + keyword +"\"");
		}
	
		return true;
	
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
