package org.ctom.hulis.files.gaussian;

import java.util.ArrayList;
import java.util.Collections;

import org.ctom.hulis.files.Writer;
import org.ctom.hulis.files.xyz.WritexyzException;
import org.ctom.hulis.huckel.Atom;
import org.ctom.hulis.huckel.Bond;
import org.ctom.hulis.huckel.HuckelAtom;
import org.ctom.hulis.huckel.HuckelBond;
import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.exception.GeometryException;
import org.ctom.hulis.huckel.methods.IMethod;
import org.ctom.hulis.huckel.structures.StructureDelocalized;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.hulis.util.geometry.Geometry;
import org.ctom.hulis.util.geometry.VirtualBond;
import org.ctom.util.format.DefaultNumberFormater;
import org.ctom.util.format.NumberFormater;

public class WriteGaussian implements IGaussianConstants {
	Mesomery mesomery;
	NumberFormater numberFormater;
	Writer out;
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
	public void writeAll(Writer out) throws WritexyzException {
		this.out = out;
		this.writeAll(this.mesomery, this.out);
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
	protected void writeAll (Mesomery m, Writer out) throws WritexyzException  {
		writeXYZ (m.getDelocalizedStructure(), out);
		writeNBOList(out);
		writeNRTList (m,  out);
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
	protected void writeXYZ  (StructureDelocalized s, Writer out) throws WritexyzException   {
		GaussianWriteXYZ gaussianWriteXYZ = new GaussianWriteXYZ(s);
		gaussianWriteXYZ.setMessageHeader(this.getMessageHeader());
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
	protected void writeNBOList (Writer out){
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
	protected void writeNRTList (Mesomery m, Writer out){
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
		}

	}


	
	protected void writeNRT(StructureLocalized s, Writer out,
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
		
		
		if (s.getCouple() != null)
			bondsNRT.add(new BondNRT(s.getCouple().getHuckelAtom1().getSuperSymbol(),s.getCouple().getHuckelAtom2().getSuperSymbol(), "S"));
		
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

		out.println(" " + IGaussianConstants.KEYWORD_END.toUpperCase() + "\n"
				+ indent + IGaussianConstants.KEYWORD_END.toUpperCase());

	}
	
	protected int getNbPairs(HuckelAtom a) {
		return (a.getNbRadR() + a.getNbElecSigma()) / 2;
	}
}
