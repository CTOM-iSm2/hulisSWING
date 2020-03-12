package org.ctom.hulis.files.gaussian.test;

import org.ctom.hulis.files.Writer;
import org.ctom.hulis.files.gaussian.WriteGaussian;
import org.ctom.hulis.files.gaussian.WriteGaussianException;
import org.ctom.hulis.files.xyz.WritexyzException;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.util.io.HuckelIO;

public class WriteGaussianTest extends WriteGaussian {


	final static public String KEYWORD_HX = "hx";
	final static public String KEYWORD_HXY = "hxy";

	@Override
	protected int getNbPairs(HuckelAtom a) {
		return a.getNbRadR() ;
	}
	
	public WriteGaussianTest(Mesomery m) throws WriteGaussianException  {
		super(m);
	}


	protected void writeAll (Mesomery m, Writer out) throws WritexyzException  {
		writeXYZ (m.getDelocalizedStructure(), out);
		writeHX (m.getDelocalizedStructure(), out);
		writeHXY (m.getDelocalizedStructure(), out);
		writeNBOList(out);
		writeNRTList (m,  out);
	}
	
	protected void writeHX(StructureDelocalized s, Writer out) {
		out.print(KEYWORD_HX.toUpperCase());
		for (HuckelAtom a : s.getHuckelAtoms()) {
			if (a.getHx() != ((HuckelAtom) PeriodicTable.getNewAtom(a.getEntry())).getHx() ) 
				out.print(" " + a.getSuperSymbol() + " " + a.getHx() );
		}
		out.print(" " + KEYWORD_END.toUpperCase() + "\n" );

	
	}

	protected void writeHXY(StructureDelocalized s, Writer out) {
		out.print(KEYWORD_HXY.toUpperCase());
		for (HuckelBond b : s.getHuckelBonds()) {
			try {
				if ((b.getAtom1().getEntry() != null) && (b.getAtom2().getEntry() != null) && (b.getHxy() != HuckelBond.getHxy(b.getAtom1().getEntry(), b.getAtom2().getEntry())) ) 
					out.print(" " + b.getHxy() + " " + b.getAtom1().getSuperSymbol() + " " + b.getAtom2().getSuperSymbol() );
			} catch (HuckelBondException e) {
					HuckelIO.error(this.getClass().getName(), "writeHXY", e.getMessage(),e);
					e.printStackTrace();
			}
		}
		out.print(" " + KEYWORD_END.toUpperCase() + "\n" );

	
	}
	


}
