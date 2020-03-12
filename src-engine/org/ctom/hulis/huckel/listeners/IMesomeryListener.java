package org.ctom.hulis.huckel.listeners;


import java.util.EventListener;

import org.ctom.hulis.huckel.events.MesomeryEvent;
/**
 * This interface must be implemented by classes that listen to mesomery events
 * @author ngoudard
 *
 */
public interface IMesomeryListener extends EventListener, IStructureDelocalizedListener, IStructureLocalizedListener, IStructureListener {

	/**
	 * Method to execute when the weights are calculated
	 * @param mesomery event that happened
	 */
	public void mesomeryListWeightCalculated(MesomeryEvent e);

	/**
	 * Method to execute when the energy to select localized structures has changed
	 * @param mesomery event that happened
	 */
	public void mesomeryStructuresSelectionTresholdChanged(MesomeryEvent e);

	
	/**
	 * Method to execute when a structure has been added
	 * @param mesomery event that happened
	 */
	public void mesomeryStructureAdded(MesomeryEvent e);

	/**
	 * Method to execute when a structure has been removed
	 * @param mesomery event that happened
	 */
	public void mesomeryStructureRemoved(MesomeryEvent e);


	/**
	 * 
	 * @param e
	 */
	public void mesomeryMethodAdded(MesomeryEvent e);

	/**
	 * 
	 * @param e
	 */
	public void mesomeryMethodRemoved(MesomeryEvent e);

	
	/**
	 * 
	 * @param e
	 * @param exception
	 */
	public void mesomeryExceptionOccured(MesomeryEvent e, Exception exception);

	/**
	 * @param event
	 */
	public void mesomeryStartComputeWeights(MesomeryEvent event);

	/**
	 * @param event
	 */
	public void mesomeryStructuresSelectorChanged(MesomeryEvent event);

	
}
