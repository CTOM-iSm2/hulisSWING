/**
 * 
 */
package org.ctom.hulis.huckel.selectors;

import org.ctom.hulis.huckel.Mesomery;
import org.ctom.hulis.huckel.structures.Structure;

/**
 * This interface defines a structure selector. 
 * The selector selects the structures to compute until a defined max value.
 * 
 * 
 * @author macbook
 * 
 *
 */
public interface IStructuresSelector {
	
	/**
	 * @return a copy of the structure selector
	 */
	public Object clone();

	/**
	 * Set the treshold value : The selected structure are under the treshold
	 * @param o : the treshold value
	 */
	public void setSelectionTreshold(Object o);
	
	/**
	 * return the treshold value under which the structure are selected
	 * @return
	 */
	public Object getSelectionTreshold();

	/**
	 * 
	 * Set the trehsold value when there is not structure in the mesomery
	 */
	public void setTresholdValueWhenNoStructure();

	
	/**
	 * 
	 * Set the treshold value to the minimal value of the localized structures
	 */
	public void setTresholdToLocalizedStructureMinimalValue();

	/**
	 * get the maximal value of all the structures
	 * @return the maximal value
	 */
	public Object getMaximalValue();



	/**
	 * return true if the current value relative to structure is lower than the current treshold
	 * else return false
	 * @param s : structure
	 * @return
	 */
	public boolean isCurrentValueLowerThanTreshold(Structure s);

	
	/**
	 * get the treshold value for a structure given in parameter
	 * @param s
	 * @return
	 */
	public Object getCurrentValue(Structure s);

	
	/**
	 * Select the structures to compute that are under the current treshold
	 */
	public void selectStructures();

	
	/**
	 * 
	 * @param mesomery
	 */
	public void setMesomery(Mesomery mesomery);
}
