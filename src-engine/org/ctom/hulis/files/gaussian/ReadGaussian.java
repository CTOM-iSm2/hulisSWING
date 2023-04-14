package org.ctom.hulis.files.gaussian;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.ArrayList;

import org.ctom.hulis.files.ReadSectionException;
import org.ctom.hulis.files.SectionReader;
import org.ctom.hulis.files.xyz.ReadxyzException;
import org.ctom.hulis.huckel.Mesomery;
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

public class ReadGaussian implements IGaussianConstants {
	int charge;;
	// TODO header should be replaced by more complex objects
	private ArrayList<String> header = null;
	protected Mesomery mesomery;
	int multiplicity;

	protected boolean SWITCH_NRT = false;
	StringBuffer xyz;
	StreamTokenizer tokenizer;
	protected SectionReader sectionReader; 


	/**
	 * 
	 * @param file
	 * @throws FileNotFoundException 
	 */
	public ReadGaussian(File file) throws FileNotFoundException  {
		FileReader fileReader = new FileReader(file);
		this.tokenizer = new StreamTokenizer(fileReader);
		this.sectionReader = new SectionReader(new LineNumberReader(fileReader));

	}

	/**
	 * @param str
	 */
	public ReadGaussian(String str) {
		StringReader stringReader = new StringReader(str);
		this.tokenizer = new StreamTokenizer(stringReader);
		this.sectionReader = new SectionReader(new LineNumberReader(stringReader));
	}


	private void bufferizeXYZ() throws ReadGaussianException {
		try {
			String line;
			while (true) {
				line = this.sectionReader.getWholeLine();
				if ((line == null) || (line.length() == 0))
					return;
				this.xyz.append(line + "\n");
			}
		}  catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		}
	}

	/**
	 * 
	 * @param mesomery
	 * @throws ReadGaussianException
	 */
	public void readAll (Mesomery mesomery) throws ReadGaussianException {
		this.header = new ArrayList<String>();
		this.mesomery = mesomery;
		this.xyz = new StringBuffer();
		try {
			readAll();
		} catch (HuckelBondException e) {
			throw new ReadGaussianException(e);
		} catch (BondException e) {
			throw new ReadGaussianException(e);
		} catch (MesomeryDelocalizedStructureExistsException e) {
			throw new ReadGaussianException(e);
		} catch (MoleculeTooManyNeighboursException e) {
			throw new ReadGaussianException(e);
		} catch (MoleculeBondExistsException e) {
			throw new ReadGaussianException(e);
		} catch (MoleculeCoherenceException e) {
			throw new ReadGaussianException(e);
		} catch (ReadxyzException e) {
			throw new ReadGaussianException(e);
		} catch (MesomeryDelocalizedStructureNotExistsException e) {
			throw new ReadGaussianException(e);
		} catch (CoupleException e) {
			throw new ReadGaussianException(e);
		} catch (IMethodException e) {
			throw new ReadGaussianException(e);
		} catch (MesomeryNoStructureLocalizedException e) {
			throw new ReadGaussianException(e);
		} catch (HuckelAtomException e) {
			throw new ReadGaussianException(e);
		} catch (NumberFormatException e) {
			throw new ReadGaussianException(e);
		} catch (HulisException e) {
			throw new ReadGaussianException(e);

		}
	}




	protected void readAll() throws ReadGaussianException, HuckelBondException, BondException, MesomeryDelocalizedStructureExistsException, MoleculeTooManyNeighboursException, MoleculeBondExistsException, MoleculeCoherenceException, ReadxyzException, MesomeryDelocalizedStructureNotExistsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException, HuckelAtomException, NumberFormatException, HulisException  {
		readHeader();
		readChargeMultiplicity();
		readXYZ();
		readNRT();
	}

	protected void readXYZ() throws HuckelBondException, BondException, MesomeryDelocalizedStructureExistsException, MoleculeTooManyNeighboursException, MoleculeBondExistsException, MoleculeCoherenceException, ReadGaussianException, ReadxyzException  {
		bufferizeXYZ();
		// read the xyz geometry
		this.tokenizer = new StreamTokenizer(new StringReader(this.xyz.toString()));
		GaussianReadXYZ gaussianReadXYZ = new GaussianReadXYZ(this.tokenizer);
		this.mesomery.addDelocalizedStruct(new StructureDelocalized(
				gaussianReadXYZ));
		this.mesomery.getDelocalizedStructure().setNbElecCharge(
				-this.charge);
	}


	protected void readChargeMultiplicity() throws ReadGaussianException {
		// read the charge and multiplicity
		String line;
		try {
			line = this.sectionReader.getWholeLine();
		} catch (ReadSectionException e) {
			throw new ReadGaussianException(e);
		}
		String error = "The line "+ this.sectionReader.getLineNumber() + " must contains two numbers (charge and multiplicity) delimited by a space";
		if (line == null)
			throw new ReadGaussianException(error);
		String[] s = line.split(" ");
		if (s.length != 2)
			throw new ReadGaussianException(error);
		this.charge = Integer.valueOf(s[0]);
		this.multiplicity = Integer.valueOf(s[1]);
	}

	protected  ArrayList<String> readHeader() throws ReadGaussianException {
		int nEmptyLine = 0;
		while (nEmptyLine < 2) {
			String line = null;
			try {
				line = this.sectionReader.getWholeLine();
			} catch (ReadSectionException e) {
				throw new ReadGaussianException(e);
			}
			if (line != null)
				line = line.trim();
			this.header.add(line);
			if ((line == null) || (line.length() == 0))
				nEmptyLine++;
		}
		return this.header;
	}

	protected Mesomery getMesomery() {
		return this.mesomery;
	}



	protected void readNRT() throws ReadGaussianException, HuckelBondException, MesomeryDelocalizedStructureNotExistsException, CoupleException, IMethodException, MesomeryNoStructureLocalizedException, HuckelAtomException {
		// read the nboNamelist
	
		if (!(this.SWITCH_NRT = readNBO_Namelist()))
			return;
		
		new NRTSTRreader(mesomery, sectionReader).readAll();	
	
	}



	protected boolean readNBO_Namelist() throws ReadGaussianException {
		String[] words=null;
		try {
			 words = this.sectionReader.readSectionUntilEnd(IGaussianConstants.NAMELIST_END);
		}
	    catch (ReadSectionException e) {
			// EOF
			return false;
		}
			
		if (!words[0].toLowerCase().equals(IGaussianConstants.NAMELIST_NBO_INIT)) 
			return false; //A VOIR CONFIRMATION SPECIFICATION HUMBEL mars 2013 : on doit lire nrt meme s'il n'y a pas le mot clef nbo
	
		
		boolean nrt = false;//A VOIR CONFIRMATION SPECIFICATION HUMBEL mars 2013 : on doit lire nrt meme s'il n'y a pas le mot clef nbo
		 
		for (String word :words)
			if (word.toLowerCase().toLowerCase().equals(IGaussianConstants.KEYWORD_NRT)) {
				nrt = true;
				break;
			}
	
		// $end attendu
		if (! words[words.length -1].equals(IGaussianConstants.NAMELIST_END)) 
			throw new ReadGaussianException("Keyword \""
					+ IGaussianConstants.NAMELIST_END + "\" expected at line " + this.sectionReader.getLineNumber() +" in namelist NRTSTR  instead of  \"" + words[words.length -1] +"\"");
	
		return nrt;
	}
	
	

}
