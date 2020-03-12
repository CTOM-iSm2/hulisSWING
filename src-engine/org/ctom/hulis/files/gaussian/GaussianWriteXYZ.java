package org.ctom.hulis.files.gaussian;


import org.ctom.hulis.files.Writer;
import org.ctom.hulis.files.xyz.Writexyz;
import org.ctom.hulis.files.xyz.WritexyzException;
import org.ctom.hulis.huckel.structures.StructureDelocalized;


public class GaussianWriteXYZ extends Writexyz {
	public final static String MESSAGE_2 = "Molecule name";

	public GaussianWriteXYZ(StructureDelocalized s) throws WritexyzException{
		super(s);
	}

	
	@Override
	public void writeHeader(Writer out) {
		
		out.println("%mem=256MB");
		out.println("#P HF 6-31G(d) pop=nboread");
		out.println();
		if (this.getMessageHeader() =="")
			out.println("# " +Writexyz.MESSAGE_CREE_PAR );
		else
			out.println("! " + this.getMessageHeader());
		out.println();

		String pair = " 2";
		if ((-this.getStructure().getNbElecCharge()) % 2 ==0)
			pair = " 1";

		out.println(-getStructure().getNbElecCharge() + pair);

	}
}
