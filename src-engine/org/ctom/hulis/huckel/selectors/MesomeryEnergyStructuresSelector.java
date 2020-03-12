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
public class MesomeryEnergyStructuresSelector implements IStructuresSelector, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -173683615706926362L;
	
	private transient Mesomery mesomery;
	double energyMax;

	/**
	 * @param mesomery
	 */
	public MesomeryEnergyStructuresSelector() {
		this.mesomery=null;
		this.energyMax = 0.0;
	}

	/**
	 * 
	 * @param mesomeryEnergyStructuresSelector
	 */
	public MesomeryEnergyStructuresSelector(MesomeryEnergyStructuresSelector mesomeryEnergyStructuresSelector) {
		this.energyMax = mesomeryEnergyStructuresSelector.energyMax;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		return new MesomeryEnergyStructuresSelector(this);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#setSelectionTreshold(java.lang.Object)
	 */
	public void setSelectionTreshold(Object o) {
		this.energyMax =(Double) o;

	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#selectStructures()
	 */
	public void selectStructures() {
		this.mesomery.clearListSelectedStructures();
		Structure struct;
		//System.out.println("select until " + this.energyMax);
		for (int i = 0; i < this.mesomery.countAllSructs(); i++) {
			struct = this.mesomery.getAllStructures().get(i);
			if (Maths.round(struct.getTotalEnergy().getBeta(), this.mesomery.getPrecision()) >= 
					Maths.round(this.energyMax, this.mesomery.getPrecision()) ) {
	
				//System.out.println("select " + struct.getName() + " : " + struct.getTotalEnergy().getBeta());
				this.mesomery.addSelectedStructure(struct);
			}
			if (struct instanceof StructureLocalized) {
				struct.setName(Integer.toString(i));
			}
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
		return this.energyMax;
	}
	
	
	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#isCurrentValueLowerThanTreshold(org.ctom.hulis.huckel.Structure)
	 */
	public boolean isCurrentValueLowerThanTreshold(Structure s) {
		return Math.abs(((Double) this.getCurrentValue(s))) <= Math.abs(this.energyMax);
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#setTresholdValueWhenNoStructure()
	 */
	public void setTresholdValueWhenNoStructure() {
		this.energyMax = 0.0;
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#getCurrentValue(org.ctom.hulis.huckel.Structure)
	 */
	public Object getCurrentValue(Structure s) {
		return s.getTotalEnergy().getBeta();
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#setTresholdToLocalizedStructureMinimalValue()
	 */
	public void setTresholdToLocalizedStructureMinimalValue() {
		
		double beta = 0.0;
		double e= 0.0;
		StructureLocalized struct;
		List<StructureLocalized>locstructures = this.mesomery.getLocalizedStructures();
		for (int i = 0; i < this.mesomery.countLocalizedStructs(); i++) {
			struct = locstructures.get(i);
			e= struct.getTotalEnergy().getBeta();
			// HuckelIO.PrintIf(struct.getTotalEnergy().getBeta() + "\n");
			if (Maths.round(e, this.mesomery.getPrecision()) > beta) {
				beta = e;
			}
		}
		this.energyMax =  beta;
		
	}

	/* (non-Javadoc)
	 * @see org.ctom.hulis.huckel.IStructuresSelector#getMaximalValue()
	 */
	public Object getMaximalValue() {
		// select the structures with the greatest energy
		double energy = mesomery.getDelocalizedStructure().getTotalEnergy().getBeta();
		for (Structure s : mesomery.getAllStructures()) {
			if (s.getTotalEnergy().getBeta() < energy)
				energy = s.getTotalEnergy().getBeta();
			
		}
				
		return -energy;
		
	}


}
