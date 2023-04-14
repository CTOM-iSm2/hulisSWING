package org.ctom.hulis.files.gaussian;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.ctom.hulis.files.xyz.WritexyzException;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.BlocDelocalized;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.PeriodicTable;
import org.ctom.hulis.huckel.exception.GeometryException;
import org.ctom.hulis.huckel.exception.HuckelBondException;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.geometry.VirtualBond;
import org.ctom.hulis.util.io.HuckelIO;
import org.ctom.util.format.DefaultNumberFormater;
import org.ctom.util.format.NumberFormater;

public class WriteGaussian implements IGaussianConstants {
	Mesomery mesomery;
	NumberFormater numberFormater;
	private boolean isWriteInitialOrientation;
	private String messageHeader;
	private boolean isWriteConnectivity;
	
	/**
	 * 
	 * @param m : mesomery
	 */
	public WriteGaussian(Mesomery m)  {
		this.mesomery = m;
		this.numberFormater = new DefaultNumberFormater();
		this.isWriteInitialOrientation =  false;
		this.messageHeader="";
		this.isWriteConnectivity=false;
	}
	
	/**
	 * Write the mesomery into the writer, in a gaussian format
	 * @param out : the writer
	 * @throws WritexyzException
	 */
	public void writeAll(PrintWriter out) throws WritexyzException {
		this.writeAll(this.mesomery, out);
	}
	
	/**
	 * @param numberFormater
	 */
	public void setNumberFormater(NumberFormater numberFormater) {
		this.numberFormater = numberFormater;
	}

	/**
	 * 
	 * @param messageHeader
	 */
	public void setMessageHeader(String messageHeader) {
		this.messageHeader = messageHeader;
	}

	
	/**
	 * 
	 * @return
	 */
	public String getMessageHeader() {
		return this.messageHeader;
	}


	/**
	 * @param m
	 * @param out
	 * @throws WritexyzException
	 */
	protected void writeAll (Mesomery m, PrintWriter out) throws WritexyzException  {
		writeXYZ (m.getDelocalizedStructure(), out);
		writeNBOList(out);
		writeNRTList (m,  out);
	}
	
	
	/**
	 * 
	 * @param s
	 * @param out
	 */
	protected void writeHX(StructureDelocalized s, PrintWriter out) {
		out.print( " ! " + KEYWORD_HX.toUpperCase());
		for (HuckelAtom a : s.getHuckelAtoms()) {
			if (a.getHx() != ((HuckelAtom) PeriodicTable.getNewAtom(a.getEntry())).getHx() ) 
				out.print(" " + a.getSuperSymbol() + " " + a.getHx() );
		}
		out.print(" " + KEYWORD_END.toUpperCase() + "\n" );

	
	}

	/**
	 * 
	 * @param s
	 * @param out
	 */
	protected void writeHXY(StructureDelocalized s, PrintWriter out) {
		out.print( " ! " + KEYWORD_HXY.toUpperCase());
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
	
	
	/**
	 * @param isWriteInitialOrientation
	 */
	public void setWriteInitialOrientation(boolean isWriteInitialOrientation) {
		this.isWriteInitialOrientation=isWriteInitialOrientation;
	}
	
	/**
	 * @return isWriteInitialOrientation
	 */
	public boolean isWriteInitialOrientation() {
		return this.isWriteInitialOrientation;
	}
	

	/**
	 * @param s
	 * @param out
	 * @throws WritexyzException
	 */
	protected void writeXYZ  (StructureDelocalized s, PrintWriter out) throws WritexyzException   {
		GaussianWriteXYZ gaussianWriteXYZ = new GaussianWriteXYZ(s);
		String header=this.getMessageHeader();
		StringWriter stringWriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringWriter);
        printwriter.println(header);
        this.writeHX(s, printwriter);
        this.writeHXY(s, printwriter);
		gaussianWriteXYZ.setMessageHeader(header);
		gaussianWriteXYZ.setWriteInitialOrientation(this.isWriteInitialOrientation());
		gaussianWriteXYZ.setWriteConnectivity(this.isWriteConnectivity());
		gaussianWriteXYZ.writeAll(out);
		out.println();

	}
	
	/**
	 * @return
	 */
	public boolean isWriteConnectivity() {
		return this.isWriteConnectivity;
	}
	
	/**
	 * @return
	 */
	public void setWriteConnectivity(boolean isWriteConnectivity) {
		this.isWriteConnectivity = isWriteConnectivity;
	}
	
	

	/**
	 * @param out
	 */
	protected void writeNBOList (PrintWriter out){
		if (mesomery.countSelectedLocalizedStructs() > 0) { 
			out.print(IGaussianConstants.NAMELIST_NBO_INIT + " ");
			out.print(IGaussianConstants.KEYWORD_NRT + " ");
			out.print(IGaussianConstants.NAMELIST_END + "\n");
		}
	}
	
	/**
	 * @param m
	 * @param out
	 */
	protected void writeNRTList (Mesomery m, PrintWriter out){
		if (mesomery.countSelectedLocalizedStructs() > 0) { 
			String trustFactor ="";
			for (IMethod method : mesomery.getMethods()) {
				if (method.isImplementedTau())
					trustFactor += numberFormater.formatNumber( method.getTau() * 100) + "% ("+ method.getName() +") ";
			}
			
			out.println(IGaussianConstants.NAMELIST_NRTSTR_INIT.toUpperCase() + " ! Trust factor = " + trustFactor);
			for (StructureLocalized s : m.getSelectedLocalizedStructures()) {
				writeNRT (s, out, 2);
			}	
			out.println(IGaussianConstants.NAMELIST_END.toUpperCase());
			out.println();
		}

	}


	/**
	 * 
	 * @param s
	 * @param out
	 * @param nbCarIndent
	 */
	protected void writeNRT(StructureLocalized s, PrintWriter out,
			int nbCarIndent) {

		String indent = "";
		for (int i = 0; i < nbCarIndent; i++)
			indent += " ";
		double weight = 0;
		String weights = "";
		
		Geometry geom = null;
		try {
			geom = s.getGeometry();
			
		} catch (GeometryException e) {
			out.println("Error getting the structure geometry of Psi" + s.getName() + " : error : " + e.getMessage());
			e.printStackTrace();
			return;
		}
		
		for (IMethod method : mesomery.getMethods()) {
			weight = s.getWeight(method.getName());
			weights += numberFormater.formatNumber(weight * 100) + "% ("+ method.getName() +") ";
		}
		out.print((indent + IGaussianConstants.KEYWORD_STR.toUpperCase()
				+ "        ! HuLiS Wgt = "
				+ weights +  " S" + s.getName()
				+ "\n" + indent + "  " + IGaussianConstants.KEYWORD_LONE
				.toUpperCase()));

		ArrayList<LonePairNRT> lones = new ArrayList<LonePairNRT>();
		int nPairs;
		
		for (Atom a : geom.getGeom3D()) {
			if (a instanceof HuckelAtom){
				HuckelAtom huckelAtom= (HuckelAtom)a;
				nPairs = getNbPairs(huckelAtom);
				if (nPairs > 0) 
					lones.add(new LonePairNRT(huckelAtom.getSuperSymbol(), nPairs));
			}

		}

		Collections.sort(lones);
		for (LonePairNRT lone : lones) {
			out.print(" " + lone.toString());
		}

		out
				.print(" " + IGaussianConstants.KEYWORD_END.toUpperCase()
						+ "\n" + indent + "  "
						+ IGaussianConstants.KEYWORD_BOND.toUpperCase());

		String type;
		ArrayList<BondNRT> bondsNRT = new ArrayList<BondNRT>();
		
		
		HuckelAtom[] couple=s.guessCouple();
		if (couple!= null)
			bondsNRT.add(new BondNRT(couple[0].getSuperSymbol(),couple[1].getSuperSymbol(), "S"));
		
		// on parcourt toutes les liaisons de la GEOM3D donc les CH3
		for (Bond b : geom.getBonds3D()) {
			if (b instanceof HuckelBond) {
				HuckelBond hb = (HuckelBond) b;
				if (hb.getBondType() == 2)
					type = "D";
				else
					type = "S";
			} 
			else if (b instanceof VirtualBond) {
				VirtualBond vb = (VirtualBond) b;
				if (vb.getBondType() == 2)
					type = "D";
				else
					type = "S";
			}
			else
				type = "S";
			bondsNRT.add(new BondNRT(b.getAtom1().getSuperSymbol(), b.getAtom2()
					.getSuperSymbol(), type));
		}

		Collections.sort(bondsNRT);
		for (BondNRT bondNRT : bondsNRT) {
			out.print(" " + bondNRT.toString());
		}

		out.println(" " + IGaussianConstants.KEYWORD_END.toUpperCase());
				
		writeBLW (s, out, 4 );
		
		out.println(indent + IGaussianConstants.KEYWORD_END.toUpperCase());
	}
	
	/**
	 * 
	 * @param s
	 * @param out
	 * @param nbCarIndent
	 */
	protected void writeBLW(StructureLocalized s, PrintWriter out, int nbCarIndent) {
		List<BlocDelocalized> blocks=s.getListBlocDelocalized();
		if (blocks.isEmpty())
			return;
		String indent = "";
		for (int i = 0; i < nbCarIndent; i++)
			indent += " ";
		out.print(indent+  IGaussianConstants.KEYWORD_BLW.toUpperCase() + "\n");
		out.print(indent+  "  " + blocks.size()+ "\n");
		out.print(indent+  IGaussianConstants.KEYWORD_BLW_END.toUpperCase() + "\n");
		out.print(indent+  IGaussianConstants.KEYWORD_BLW_DAT.toUpperCase() + "\n");
		for (BlocDelocalized block : blocks) {
			out.print(indent+ "  " +  block.getNbElectron() + " " + block.getListHuckelAtom().size() + "\n");
			out.print(indent+ " ");
			ArrayList<HuckelBond> excludes=new ArrayList<HuckelBond>();
			for (HuckelAtom a : block.getListHuckelAtom()) {
				out.print( " " + a.getSuperSymbol());
				for (Bond b : a.getBonds()) {
					if (b instanceof HuckelBond) {
						HuckelBond huckelBond = (HuckelBond) b;
						if ( ! excludes.contains(huckelBond) && block.containsHuckelObject((HuckelAtom) a) && 
								block.containsHuckelObject((HuckelAtom) b.getAtomLinkedWith(a)) && 
								! block.containsHuckelObject(huckelBond))
							excludes.add(huckelBond);
					}
				}
			}
			out.print("\n");
			String sExcludes="";
			for (HuckelBond b : excludes)
				sExcludes += "  " + b.getAtom1().getSuperSymbol() + " " + b.getAtom2().getSuperSymbol();
			out.println(indent + "  0" + sExcludes); 
		}
		out.println(indent+  IGaussianConstants.KEYWORD_BLW_END.toUpperCase());
	}
	
	
	/**
	 * @param a
	 * @return
	 */
	protected int getNbPairs(HuckelAtom a) {
		return (a.getNbRadR() + a.getNbElecSigma()) / 2;
	}
}
