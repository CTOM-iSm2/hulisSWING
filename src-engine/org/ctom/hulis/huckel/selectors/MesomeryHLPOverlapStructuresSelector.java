/**
 * 
 */
package org.ctom.hulis.huckel.selectors;

import java.io.Serializable;
import java.util.List;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.structures.Structure;
import org.ctom.hulis.huckel.structures.StructureLocalized;
import org.ctom.util.maths.Maths;

/**
 * @author macbook
 *
 */
public class MesomeryHLPOverlapStructuresSelector implements IStructuresSelector, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2672586164032318807L;
	
	private Mesomery mesomery;
	private double overlapValue;

	/**
	 * @param mesomery
	 */
	public MesomeryHLPOverlapStructuresSelector() {
		this.mesomery=null;
		this.overlapValue= 1.0;
	}

	/**
	 * 
	 * @param mesomeryEnergyStructuresSelector
	 */
	public MesomeryHLPOverlapStructuresSelector(MesomeryHLPOverlapStructuresSelector mesomeryEnergyStructuresSelector) {
		this.overlapValue= mesomeryEnergyStructuresSelector.overlapValue;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new MesomeryHLPOverlapStructuresSelector(this);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#setSelectionTreshold(java.lang.Object)
	 */
	public void setSelectionTreshold(Object o) {
		this.overlapValue =(Double) o;

	}

	

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#selectStructures()
	 */
	public void selectStructures() {

		Structure struct;
		int j = 0;
		//System.out.println("select until " + this.energyMax);
		
		// one ne selectionne que les structures jusquau seuil de recouvrement HLP choisi
		this.mesomery.clearListSelectedStructures();
		//System.out.println("OVERLAP VALUE " + this.overlapValue);
		for (int i = 0; i < this.mesomery.countAllSructs(); i++) {
			struct = this.mesomery.getAllStructures().get(i);
			
			if (Math.abs(Maths.round(struct.getOverlapHLP(), this.mesomery.getPrecision())) >= 
					Math.abs(Maths.round(this.overlapValue, this.mesomery.getPrecision())) ) {
				//System.out.println("select " + struct.getName() + " : " + struct.getTotalEnergy().getBeta());
				this.mesomery.addSelectedStructure(struct);
			}
			
		//	System.out.println(struct.getName() + " " + struct.getOverlapHLP() + " " +  (Math.abs(Maths.round(struct.getOverlapHLP(), this.mesomery.getPrecision())) >= 
			//		Math.abs(Maths.round(this.overlapValue, this.mesomery.getPrecision()))));
		}

		// vu que les structures selectionnes par overlap ne sont pas foecement dans l'ordre, on renommme toutes les structures dans cette boucle a part
		for (int i = 0; i < this.mesomery.countAllSructs(); i++) {
			struct = this.mesomery.getAllStructures().get(i);
			if (struct instanceof StructureLocalized) 
				struct.setName(Integer.toString(i));
			
		}

	}


	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#setMesomery(org.ctom.hulis.huckel.Mesomery)
	 */
	public void setMesomery(Mesomery mesomery){
		this.mesomery = mesomery;
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#getSelectionTreshold()
	 */
	public Object getSelectionTreshold() {
		return this.overlapValue;
	}


	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#isCurrentValueLowerThanTreshold(org.ctom.hulis.huckel.Structure)
	 */
	public boolean isCurrentValueLowerThanTreshold(Structure s) {

		return Math.abs(((Double) this.getCurrentValue(s))) <= Math.abs(this.overlapValue);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#setTresholdValueWhenNoStructure()
	 */
	public void setTresholdValueWhenNoStructure() {
		this.overlapValue = 0.0;

	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#getCurrentValue(org.ctom.hulis.huckel.Structure)
	 */
	public Object getCurrentValue(Structure s) {
		return s.getOverlapHLP();

	}


	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#setTresholdToLocalizedStructureMinimalValue()
	 */
	public void setTresholdToLocalizedStructureMinimalValue() {

		double maxOverlap = 0.0;
		double overlap= 0.0;
		StructureLocalized struct;
		List<StructureLocalized>locstructures = this.mesomery.getLocalizedStructures();
		for (int i = 0; i < this.mesomery.countLocalizedStructs(); i++) {
			struct = locstructures.get(i);
			overlap= struct.getOverlapHLP();
			// HuckelIO.PrintIf(struct.getTotalEnergy().getBeta() + "\n");
			if (Maths.round(overlap, this.mesomery.getPrecision()) > maxOverlap) {
				maxOverlap = overlap;
			}
		}
		this.overlapValue =  maxOverlap;


	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#getMaximalValue()
	 */
	public Object getMaximalValue() {
		if (mesomery.getSelectedLocalizedStructures().size()  == 0) 
			return 0.0;
	
		double hlpOverlap = Math.abs(mesomery.getSelectedLocalizedStructures().get(0).getOverlapHLP());
		
		for (Structure s : mesomery.getSelectedLocalizedStructures()) {
			if (Math.abs(s.getOverlapHLP()) < hlpOverlap) 
				hlpOverlap = Math.abs(s.getOverlapHLP());
			
		}
		return -hlpOverlap;
		
	}


}
